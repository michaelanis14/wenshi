package com.wenshi_egypt.wenshi;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Criteria;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.firebase.ui.auth.data.model.User;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.wenshi_egypt.wenshi.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerMapActivity extends AppCompatActivity implements View.OnClickListener, ProfileFragment.OnFragmentInteractionListener, HistoricFragment.OnFragmentInteractionListener, VehiclesFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener, PaymentOptions.OnFragmentInteractionListener, HelpFragment.OnFragmentInteractionListener, RateAndChargesFragment.OnFragmentInteractionListener, AboutFragment.OnFragmentInteractionListener, InviteFragment.OnFragmentInteractionListener, FamilyViewFragment.OnFragmentInteractionListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    static final LatLng CAIRO = new LatLng(30.044281, 31.340002);
    private static final long UPDATE_INTERVAL = 50000;
    private static final long FASTEST_INTERVAL = 30000;
    private static final float DISPLACMENT = 100;
    static GoogleApiClient mGoogleApiClient;
    final int MY_PERMISSION_REQ_CODE = 1234;
    final int PLAY_SERVICE_RESLUOTION_CODE = 2345;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    DrawerLayout drawer;
    View mBottomSheet;
    TextView mBottomTextView;
    BottomSheetBehavior mBottomSheetBehavior;
    Button mButton;
    UserModel user;
    DatabaseReference driversAvlbl;
    private GoogleMap mMap;
    private Marker mDriverMarker;
    private SupportMapFragment mapFragment;
    private Button mRequest, mCancel;
    private LatLng pickUpLocation;
    private boolean driverFound = false;
    private float radius = 2;
    private String requestedDriverID = "";
    private GeoQuery geoQuery;
    private DatabaseReference driverLocationRef;
    private ValueEventListener driverLocationRefListener;
    private String userId = "_";
    private AppCompatDelegate mDelegate;
    private int mThemeId = 0;
    private Resources mResources;
    private GeoFire geoFireCustomerLocation;
    private DatabaseReference customerLocation;
    private DatabaseReference requestDriver;
    private Marker myCurrent;
    private Map<String, Marker> markers;
    private List<String> driversID;

    TextView timerTextView;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            mBottomTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        user = (UserModel) i.getParcelableExtra("CurrentUser");

        Log.i("CCurrentUser", user.toString());

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.customer_map);
        mapFragment.getMapAsync(this);
        customerLocation = FirebaseDatabase.getInstance().getReference("CustomersOnline");
        requestDriver = FirebaseDatabase.getInstance().getReference("Users").child("Drivers");
        driversAvlbl = FirebaseDatabase.getInstance().getReference("DriversAvailable");

        geoFireCustomerLocation = new GeoFire(customerLocation);
        setupLocation();

        //Navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.customer_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.customer_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        driversID = new ArrayList<String>();

        markers = new HashMap<String, Marker>();

        mBottomTextView = findViewById(R.id.bottom_view_lbl);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRequest = (Button) findViewById(R.id.request_wenshi_btn);
        mCancel = (Button) findViewById(R.id.cancel_wenshi_btn);
        mCancel.setVisibility(View.GONE);
        mRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestButtonClicked();
            }
        });
        ((Button) findViewById(R.id.request_wenshi_bottom_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestButtonClicked();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTrip();
            }
        });
        if (mLastLocation == null) {
            // avoid unessecary crashes on new devices !!
            mLastLocation = new Location("dummyprovider");
            mLastLocation.setLatitude(CAIRO.latitude);
            mLastLocation.setLongitude(CAIRO.longitude);
        }

        //get the bottom sheet view
        mBottomSheet = findViewById(R.id.bottom_sheet);
        // init the bottom sheet behavior
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        // change the state of the bottom sheet
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mRequest.setVisibility(View.VISIBLE);
                    if (mCancel.getVisibility() == View.VISIBLE) {
                        mRequest.setText(getResources().getString(R.string.cancelTrip));
                    } else {
                        mRequest.setText(getResources().getString(R.string.request_winsh_btn));
                    }
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    mRequest.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        getSupportActionBar().setTitle("Wenshi Customer");

        findViewById(R.id.toolbar).bringToFront();
        findViewById(R.id.toolbar).requestLayout();
        //  findViewById(R.id.main_body)
        // findViewById(R.id.customer_map).bringToFront();
        //findViewById(R.id.customer_map).requestLayout();
        findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
        navigationView.bringToFront();
        navigationView.requestLayout();


        // sendNotification("Michael", "This is a message to tell clients stop eating your feet");

/*
        Button b = (Button) findViewById(R.id.cancel_wenshi_btn);
        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("stop")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("start");
                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText("stop");
                }
            }
        });


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


    private void setupLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQ_CODE);
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
            Toast.makeText(this, "Location can't be accessed - displayLocation", Toast.LENGTH_SHORT).show();

            return;
        }
        if (mGoogleApiClient != null)
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {


            //update FireBase
            geoFireCustomerLocation.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new GeoFire.CompletionListener() {
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

            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RESLUOTION_CODE).show();
                Toast.makeText(this, "checkPlayServices isUserRecoverableError" + PLAY_SERVICE_RESLUOTION_CODE, Toast.LENGTH_SHORT).show();

            } else {
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


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                // mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onResume() {
        super.onResume();
        displayLocation();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //   setupLocation();
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
        user.setLongitude(location.getLongitude());
        user.setLatitude(location.getLatitude());
        displayLocation();
    }


    private void requestButtonClicked() {


        driverFound = false;
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            mRequest.setVisibility(View.GONE);

        } else {

            //GeoFire geoFire = new GeoFire(dbRef); //The reference where the data is stored
            //geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));


            pickUpLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(pickUpLocation).title("Your Pickup"));

            mBottomTextView.setText(getResources().getString(R.string.reqwestingWenshi));
            ((Button) findViewById(R.id.request_wenshi_bottom_btn)).setVisibility(View.GONE);
            ((Button) findViewById(R.id.cancel_wenshi_btn)).setVisibility(View.VISIBLE);

            getClosestDriver();
        }
    }

    private void getClosestDriver() {

        GeoFire geoFire = new GeoFire(driversAvlbl);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(pickUpLocation.latitude, pickUpLocation.longitude), radius);


        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                driverFound = true;

                driversID.add(key);
                DatabaseReference driver = requestDriver.child(key).child("Requests").child(userId);

                HashMap requestDetails = new HashMap();
                requestDetails.put("Accept", "false");
                requestDetails.put("Customer", user.toString());
                requestDetails.put("Timestamp", ServerValue.TIMESTAMP);


                driver.updateChildren(requestDetails);

                GeoFire customerGeoFire = new GeoFire(driver);
                customerGeoFire.setLocation("Location", new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                GeoFire geoDriverLocation = new GeoFire(driversAvlbl);

                geoDriverLocation.getLocation(key, new LocationCallback() {
                    @Override
                    public void onLocationResult(String key, GeoLocation location) {
                        if (location != null) {
                            Marker mDriverMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title("Wenshi"));
                            markers.put(key, mDriverMarker);
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            marksCameraUpdate();
                            getDistanceBetweenPickUpToDriver(new LatLng(location.latitude, location.longitude));
                        } else {
                            System.out.println(String.format("There is no location for key %s in GeoFire", key));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("There was an error getting the GeoFire location: " + databaseError);
                    }
                });


                //  DatabaseReference driverAcceptState = FirebaseDatabase.getInstance().getReference("DriversAvailable").child(key).child("Accept");
                driverLocationRefListener = driversAvlbl.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot imageSnapshot : dataSnapshot.getChildren()) {
                            Log.d("TAG Customer", "onDataChange: " + imageSnapshot.getKey());
                            Log.d("TAG Customer", "onDataChange V: " + imageSnapshot.getValue());


                            //              HashMap<Object> map = (HashMap<Object>) dataSnapshot.getValue();


                            //           if (map.get(0) != null)
                            //              mDriverMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(map.get(0).toString()), Double.parseDouble(map.get(1).toString()))).title("Driver location"));
                            //             if (map.get(0) != null)
                            // Log.i("Driverr",""+map.get(0));
                            //          getDistanceBetweenPickUpToDriver(new LatLng(Double.parseDouble(map.get(0).toString()), Double.parseDouble(map.get(1).toString())));

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //  traceDriver();
                // mRequest.setText("We find driver for you!");
                mCancel.setVisibility(View.VISIBLE);


            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                if (!driverFound && radius < (8587.0 / 35)) {
                    geoQuery.setRadius(radius++);
                    Log.i("Radius", "" + radius);
                    // ==  getClosestDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private void traceDriver() {

        //Drivers on Trips location will be updated automatically from OnLocationChanged function in DriverMapAcitvity
        driverLocationRef = FirebaseDatabase.getInstance().getReference("DriversOnTrips").child(requestedDriverID).child("l");
        driverLocationRefListener = driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();


                    if (mDriverMarker != null)    //To check if it is not first time to add marker
                        mDriverMarker.remove();

                    if (map.get(0) != null && map.get(1) != null)
                        mDriverMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(map.get(0).toString()), Double.parseDouble(map.get(1).toString()))).title("Driver location"));

                    getDistanceBetweenPickUpToDriver(new LatLng(Double.parseDouble(map.get(0).toString()), Double.parseDouble(map.get(1).toString())));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getDistanceBetweenPickUpToDriver(LatLng driverLatLang) {            //Convert it to time

        Location driversAvlbl = new Location("");
        driversAvlbl.setLatitude(driverLatLang.latitude);
        driversAvlbl.setLongitude(driverLatLang.longitude);

        Location pickupLoc = new Location("");
        pickupLoc.setLatitude(pickUpLocation.latitude);
        pickupLoc.setLongitude(pickUpLocation.longitude);

        if (driversAvlbl.distanceTo(pickupLoc) < 100) {
            mRequest.setText("Your driver is here"); //Send a notification
        } else

            mRequest.setText(String.valueOf(driversAvlbl.distanceTo(pickupLoc)));
    }

    private void cancelTrip() {

        // mMap.clear();
        if (driverLocationRef != null)
            driverLocationRef.removeEventListener(driverLocationRefListener);

        // driverLocationRef.removeEventListener(driverLocationRefListener);

        if (requestedDriverID != null) {   //Remove CustomerID child
            //   DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(requestedDriverID);
            //  driverRef.setValue(true);
            // requestedDriverID = null;

        }

        for (String driverID : driversID) {
            requestDriver.child(driverID).child("Requests").child(userId).removeValue();
        }
        //Intialize nearest driver query variables
        if (geoQuery != null) geoQuery.removeAllListeners();
        driverFound = false;
        radius = 1;

        mBottomTextView.setText("Request Winsh");
        //Remove request from DB
        // for (String driverID : driversID) {
        //   customerLocation.child(driverID).removeValue();
        // }

        if (this.markers != null) {
            for (Marker marker : this.markers.values()) {
                marker.remove();
            }
            this.markers.clear();
        }
        ((Button) findViewById(R.id.request_wenshi_bottom_btn)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.cancel_wenshi_btn)).setVisibility(View.GONE);
        displayLocation();
    }

    @Override
    public void setTheme(@StyleRes final int resid) {
        super.setTheme(resid);
        // Keep hold of the theme id so that we can re-set it later if needed
        mThemeId = resid;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    /**
     * Support library version of {@link android.app.Activity#getActionBar}.
     * <p>
     * <p>Retrieve a reference to this activity's ActionBar.
     *
     * @return The Activity's ActionBar, or null if it does not have one.
     */

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
        } else if (id == R.id.action_signout) {
            FirebaseAuth.getInstance().signOut();

        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        findViewById(R.id.mainFrame).setVisibility(View.VISIBLE);
        findViewById(R.id.customer_nav_view).bringToFront();
        findViewById(R.id.customer_nav_view).requestLayout();


        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
        } else if (id == R.id.nav_history) {
            fragment = new HistoricFragment(true, getCustomer().getID());
        } else if (id == R.id.nav_myVehicles) {
            fragment = new VehiclesFragment();
        } else if (id == R.id.nav_payment) {
            fragment = new PaymentOptions();
        } else if (id == R.id.nav_help) {
            fragment = new HelpFragment();
        } else if (id == R.id.nav_rateCharges) {
            fragment = new RateAndChargesFragment();
        } else if (id == R.id.nav_about) {
            fragment = new AboutFragment();
        } else if (id == R.id.nav_invite) {
            fragment = new InviteFragment();
        } else if (id == R.id.nav_family) {
            fragment = new FamilyViewFragment();
        }

        //NOTE: Fragment changing code
        if (fragment != null) {
            mBottomSheet.setVisibility(View.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }

        //NOTE:  Closing the drawer after selecting
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.customer_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // NOTE:  Code to replace the toolbar title based current visible fragment
//        getSupportActionBar().setTitle(uri.getHost());

    }

    protected void onStop() {
        super.onStop();
        // cancelTrip();
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
       // Button b = (Button)findViewById(R.id.button);
       // b.setText("start");
    }

    private void marksCameraUpdate() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(myCurrent.getPosition());
        for (Marker marker : this.markers.values()) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 200; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));
        mMap.animateCamera(cu);

    }

    private void sendNotification(String title, String content) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.wenshi_logo_full).setContentTitle(title).setContentText(content);
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, CustomerMapActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        mBuilder.setContentIntent(contentIntent);
        Notification notification = mBuilder.build();
// Builds the notification and issues it.
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        mNotifyMgr.notify(mNotificationId, notification);


    }

    public UserModel getCustomer() {
        return user;
    }


}
