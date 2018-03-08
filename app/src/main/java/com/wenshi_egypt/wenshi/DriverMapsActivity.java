package com.wenshi_egypt.wenshi;

        import android.Manifest;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.design.widget.BottomSheetBehavior;
        import android.support.design.widget.NavigationView;
        import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.CompoundButton;
        import android.widget.LinearLayout;
        import android.widget.PopupWindow;
        import android.widget.Switch;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.firebase.geofire.GeoFire;
        import com.firebase.geofire.GeoLocation;
        import com.firebase.geofire.GeoQuery;
        import com.firebase.geofire.LocationCallback;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.GooglePlayServicesUtil;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.maps.CameraUpdate;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.LatLngBounds;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.wenshi_egypt.wenshi.model.UserModel;

        import java.util.Collections;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class DriverMapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DriverProfileFragment.OnFragmentInteractionListener,
        HistoricFragment.OnFragmentInteractionListener,
        OnNavigationItemSelectedListener,
        com.google.android.gms.location.LocationListener {

    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 3000;
    private static final float DISPLACMENT = 10;
    static final LatLng CAIRO = new LatLng(30.044281, 31.340002);

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private DatabaseReference driverLocation;
    private GeoFire geoFireDriverLocation;

    final int MY_PERMISSION_REQ_CODE = 1234;
    final int PLAY_SERVICE_RESLUOTION_CODE = 2345;

    private String userId = "D_";

  private String assignedCustomer = "";

    private Boolean makeRequest = false;

    private DatabaseReference assignedCustomerPickupLocationRef;
    private ValueEventListener assignedCustomerPickupLocationRefEventListner;
    DrawerLayout drawer;

    View mBottomSheet;
    TextView mBottomTextView;
    BottomSheetBehavior mBottomSheetBehavior;

    private Marker myCurrent;
    private Map<String, Marker> markers;

    private GeoQuery geoQuery;

    private ValueEventListener geoFireDriverRequests;

    private Switch swtch_onlineOffline;
    DatabaseReference driver;

    private LinearLayout mRelativeLayout;
    private LinearLayout monlineOfflineLayout;
    private Button mButton;

    private PopupWindow mPopupWindow;
    private Context mContext;

    UserModel driverMod;

    Map<String,String> requestsMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        driverMod = (UserModel) i.getParcelableExtra("CurrentUser");

        mContext = getApplicationContext();
        mRelativeLayout = (LinearLayout) findViewById(R.id.drive_main_layout);
        monlineOfflineLayout = (LinearLayout) findViewById(R.id.onlineOfflineLayout);
        swtch_onlineOffline = findViewById(R.id.onlineOffline_swtch);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.driver_map);
        mapFragment.getMapAsync(this);
        driverLocation = FirebaseDatabase.getInstance().getReference("DriversAvailable");
        geoFireDriverLocation = new GeoFire(driverLocation);
        setupLocation();



        drawer = (DrawerLayout) findViewById(R.id.driver_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.driver_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.bringToFront();
        navigationView.requestLayout();

        //hide the accept request buttons
      //

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        markers = new HashMap<String, Marker>();


        requestsMap = Collections.synchronizedMap(new HashMap<String,String>());




        mBottomTextView = findViewById(R.id.driver_bottom_view_lbl);

        //get the bottom sheet view
        mBottomSheet = findViewById(R.id.driver_bottom_sheet);
        // init the bottom sheet behavior
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);


        mBottomSheet.setVisibility(View.INVISIBLE);

        // change the state of the bottom sheet
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                  //  mRequest.setVisibility(View.VISIBLE);
                //    if(mCancel.getVisibility() == View.VISIBLE){
                   //     mRequest.setText(R.string.cancelTrip);
                 //   }
             //       else{
                     //   mRequest.setText(R.string.request_winsh_btn);
               //     }
                }
                else if(newState == BottomSheetBehavior.STATE_EXPANDED){
                 //   mRequest.setVisibility(View.GONE);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        //   mCancel = (Button) findViewById(R.id.cancelTrip);
      //  mCancel.setVisibility(View.GONE);
      //  getAssignedCustomer();

        navigationView.bringToFront();
        navigationView.requestLayout();

    findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);

        getSupportActionBar().setTitle("Wenshi Driver");






        swtch_onlineOffline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                displayLocation();
                reciveRequests(isChecked);
            }

        });
        Log.i("mBottomSheetBehavio",""+mBottomSheetBehavior.getPeekHeight());
    }

    private void reciveRequests(boolean state){

        if(state) {
            driver = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(userId).child("Requests");
            geoFireDriverRequests = driver.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mBottomSheet.setVisibility(View.INVISIBLE);
                    monlineOfflineLayout.setVisibility(View.VISIBLE);
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    hidePopup();
                    displayLocation();
                    for (DataSnapshot customerSnapshot : dataSnapshot.getChildren()) {
                        Log.d("TAG Driverr", "changeee: " + customerSnapshot.getKey());

                        if (!requestsMap.containsKey(customerSnapshot.getKey()) && !customerSnapshot.getKey().equals("FirstConstant")) {
                            mBottomSheet.setVisibility(View.VISIBLE);
                            monlineOfflineLayout.setVisibility(View.GONE);
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


                            GeoFire geoDriverLocation = new GeoFire(driver.child(customerSnapshot.getKey()));

                            geoDriverLocation.getLocation("Location", new LocationCallback() {
                                @Override
                                public void onLocationResult(String key, GeoLocation location) {
                                    if (location != null) {
                                        Marker mDriverMarker = mMap.addMarker(new MarkerOptions().position(new LatLng( location.latitude, location.longitude)).title("Wenshi"));
                                        markers.put(key,mDriverMarker);
                                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                        marksCameraUpdate();
                                    //    getDistanceBetweenPickUpToDriver(new LatLng(location.latitude, location.longitude));
                                    } else {
                                        System.out.println(String.format("customer uid for %s", userId));

                                        System.out.println(String.format("There is no location for key %s in GeoFire", key));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.err.println("There was an error getting the GeoFire location: " + databaseError);
                                }
                            });



                            //requestsMap.put(customerSnapshot.getKey(),customerSnapshot.getKey());
                            showPopup(getResources().getString(R.string.new_request),
                                    customerSnapshot.getValue().toString(),
                                    "",
                                    "",
                                    "");
                            break;
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("TAG ERRRR", "onDataChange V: " + databaseError.toString());

                }
            });
            mBottomTextView.setText(R.string.online);
            displayLocation();
        }
        else{
            if(driver != null)
                driver.removeEventListener(geoFireDriverRequests);
            offline();

        }

    }
    private void marksCameraUpdate(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(myCurrent.getPosition());
        for (Marker marker : this.markers.values()) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding =200 ; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));
        mMap.animateCamera(cu);

    }

    private void setupLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQ_CODE);
        } else {
            Log.i("else if", "@");
            if (checkPlayServices()) {
                Log.i("in if", "@");

                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }

        }
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mGoogleApiClient != null)
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            //update FireBase
            if(swtch_onlineOffline != null && swtch_onlineOffline.isChecked())
            geoFireDriverLocation.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    //Add Marker
                    if (myCurrent != null) myCurrent.remove();  //remove Old Marker
                    LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    myCurrent = mMap.addMarker(new MarkerOptions().position(loc));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15.0f));
                }
            });


            //  mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACMENT);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RESLUOTION_CODE).show();
            else {
                Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
            mGoogleApiClient.connect();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
/*
        Log.i("TEXT", "onLocationChanged: " + location.toString());
        if (getApplicationContext() != null) {

            mLastLocation = location;
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(11)); //to focus on the center could be changed later

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference driversAvailableRef = FirebaseDatabase.getInstance().getReference("DriversAvailable");
            GeoFire geoFireDriversAvailable = new GeoFire(driversAvailableRef);
            DatabaseReference driversWorkingRef = FirebaseDatabase.getInstance().getReference("DriversOnTrips");
            GeoFire geoFireDriversOnTrip = new GeoFire(driversWorkingRef);

            switch (assignedCustomer) {
                case "":
                    geoFireDriversOnTrip.removeLocation(userId);
                    geoFireDriversAvailable.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                    break;

                default:
                    geoFireDriversAvailable.removeLocation(userId);
                    geoFireDriversOnTrip.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                    break;
            }

        }
*/

    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQ_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                    mapFragment.getMapAsync(this);
                } else {
                    Toast.makeText(this, "Location can't be accessed", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void offline(){
        driverLocation.child(userId).removeValue();
        if (this.markers != null) {
            for (Marker marker : this.markers.values()) {
                marker.remove();
            }
            this.markers.clear();
        }
        mBottomTextView.setText(R.string.offline);
    }
    /**
     * The driver is no longer available when he is not using the application
     */
    @Override
    protected void onStop() {
        super.onStop();
        offline();
        if (geoQuery != null) this.geoQuery.removeAllListeners();



        // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this); //to remove the listener
    // FirebaseDatabase.getInstance().getReference("DriversAvailable").child(userId).removeValue();

    }



    private void getAssignedCustomer() {


        String currentDriverId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference customerAssignedRef = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(currentDriverId).child("CustomerID");
        customerAssignedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    makeRequest = true;
                    assignedCustomer = dataSnapshot.getValue().toString();
                //    getAssignedCustomerPickUpLocation();
                } else if (makeRequest) {
                    requestCancelled();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getAssignedCustomerPickUpLocation() {

        assignedCustomerPickupLocationRef = FirebaseDatabase.getInstance().getReference().child("Requests").child(assignedCustomer).child("l");


        assignedCustomerPickupLocationRefEventListner = assignedCustomerPickupLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && !assignedCustomer.equals("")) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();

                    if (map.get(0) != null && map.get(1) != null)
                        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(map.get(0).toString()), Double.parseDouble(map.get(1).toString()))).title("Customer Location"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void requestCancelled() {

        //mCancel.setVisibility(View.VISIBLE);
        //mCancel.setText("Driver cancelled the trip!!");
        assignedCustomer = "";
        if (assignedCustomerPickupLocationRef != null) {
            assignedCustomerPickupLocationRef.removeEventListener(assignedCustomerPickupLocationRefEventListner);
        }
        mMap.clear();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.customer_drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_signout){
            FirebaseAuth.getInstance().signOut();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        findViewById(R.id.mainFrame).setVisibility(View.VISIBLE);
        findViewById(R.id.mainFrame).bringToFront();
        findViewById(R.id.mainFrame).requestLayout();
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_profile) {
            fragment = new DriverProfileFragment();
        } else if (id == R.id.nav_history) {
            fragment = new HistoricFragment(false, getDriver().getID());
        }

        //NOTE: Fragment changing code
        if (fragment != null) {
            mBottomSheet.setVisibility(View.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }

        //NOTE:  Closing the drawer after selecting
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.driver_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // NOTE:  Code to replace the toolbar title based current visible fragment
        getSupportActionBar().setTitle(uri.getHost());
    }
    @Override
    public void onResume(){
        super.onResume();
        displayLocation();

    }

    private void showPopup(String title,String line1,String line2,String line3,String line4){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View popupLayout = inflater.inflate(R.layout.popup_layout,null);

                /*
                    public PopupWindow (View contentView, int width, int height)
                        Create a new non focusable popup window which can display the contentView.
                        The dimension of the window must be passed to this constructor.

                        The popup does not provide any background. This should be handled by
                        the content view.

                    Parameters
                        contentView : the popup's content
                        width : the popup's width
                        height : the popup's height
                */
        // Initialize a new instance of popup window
        if(mPopupWindow == null)
        mPopupWindow = new PopupWindow(
                popupLayout,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        // Get a reference for the custom view close button
       // ImageButton closeButton = (ImageButton) popupLayout.findViewById(R.id.ib_close);

        if(!title.trim().isEmpty()) {
            ((TextView) popupLayout.findViewById(R.id.title_popup)).setVisibility(View.VISIBLE);
            ((TextView) popupLayout.findViewById(R.id.title_popup)).setText(title);
        }else
            ((TextView) popupLayout.findViewById(R.id.title_popup)).setVisibility(View.INVISIBLE);

        if(!line1.trim().isEmpty()) {
            ((TextView) popupLayout.findViewById(R.id.body_popup)).setVisibility(View.VISIBLE);
            ((TextView) popupLayout.findViewById(R.id.body_popup)).setText(line1);
        }else
            ((TextView) popupLayout.findViewById(R.id.body_popup)).setVisibility(View.INVISIBLE);

        if(!line2.trim().isEmpty()) {
            ((TextView) popupLayout.findViewById(R.id.body1_popup)).setText(line2);
        }else
            ((TextView) popupLayout.findViewById(R.id.body1_popup)).setVisibility(View.INVISIBLE);

        if(!line3.trim().isEmpty()) {
            ((TextView) popupLayout.findViewById(R.id.body2_popup)).setText(line3);
        }else
            ((TextView) popupLayout.findViewById(R.id.body2_popup)).setVisibility(View.INVISIBLE);

        if(!line4.trim().isEmpty()) {
            ((TextView) popupLayout.findViewById(R.id.body3_popup)).setText(line4);
        }else
            ((TextView) popupLayout.findViewById(R.id.body3_popup)).setVisibility(View.INVISIBLE);



        // Set a click listener for the popup window close button
        //closeButton.setOnClickListener(new View.OnClickListener() {
       //     @Override
      //      public void onClick(View view) {
      //          // Dismiss the popup window
       //         mPopupWindow.dismiss();
        //    }
     //   });
if(!mPopupWindow.isShowing())
        mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);

    }

    private void hidePopup(){

        if(mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
    }

    public UserModel getDriver(){
        return  driverMod;
    }
}
