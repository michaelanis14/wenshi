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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerMapActivity extends AppCompatActivity implements View.OnClickListener,
        ProfileFragment.OnFragmentInteractionListener,
        HistoricFragment.OnFragmentInteractionListener,
        PaymentOptions.OnFragmentInteractionListener,
        HelpFragment.OnFragmentInteractionListener,
        RateAndChargesFragment.OnFragmentInteractionListener,
        DriverFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 3000;
    private static final float DISPLACMENT = 10;
    private GoogleMap mMap;
    private Marker mDriverMarker;

    private SupportMapFragment mapFragment;
    static GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    final int MY_PERMISSION_REQ_CODE = 1234;
    final int PLAY_SERVICE_RESLUOTION_CODE = 2345;

    private Button mRequest, mCancel;
    private LatLng pickUpLocation;

    private boolean driverFound = false;
    private float radius = 1;
    private String requestedDriverID = "";

    private GeoQuery geoQuery;
    private DatabaseReference driverLocationRef;
    private ValueEventListener driverLocationRefListener;

    private String userId = "_";


    private AppCompatDelegate mDelegate;
    private int mThemeId = 0;
    private Resources mResources;
    DrawerLayout drawer;
    View mBottomSheet;
    TextView mBottomTextView;
    BottomSheetBehavior mBottomSheetBehavior;
    Button mButton;

    private GeoFire geoFireCustomerLocation;
    private DatabaseReference customerLocation;
    private Marker myCurrent;
    private Map<String, Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.customer_map);
        mapFragment.getMapAsync(this);
        customerLocation = FirebaseDatabase.getInstance().getReference("CustomersOnline");
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


        mBottomTextView = findViewById(R.id.bottom_view_lbl);
        // userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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


        //get the bottom sheet view

        mBottomSheet = findViewById(R.id.bottom_sheet);


// init the bottom sheet behavior
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);

// change the state of the bottom sheet
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        //
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

// set the peek height
        //     mBottomSheetBehavior.setPeekHeight(320);

// set hideable or not
       // mBottomSheetBehavior.setHideable(true);

// set callback for changes
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        getSupportActionBar().setTitle("Customer View");

        findViewById(R.id.toolbar).bringToFront();
        findViewById(R.id.toolbar).requestLayout();
        //  findViewById(R.id.main_body)
        // findViewById(R.id.customer_map).bringToFront();
        //findViewById(R.id.customer_map).requestLayout();
        findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
        navigationView.bringToFront();
        navigationView.requestLayout();


       // sendNotification("Michael", "This is a message to tell clients stop eating your feet");

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
        displayLocation();
    }


    private void requestButtonClicked() {


        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            mRequest.setVisibility(View.GONE);

        } else {

            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Requests");

            GeoFire geoFire = new GeoFire(dbRef); //The reference where the data is stored
            geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

            pickUpLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(pickUpLocation).title("Your Pickup"));

            mBottomTextView.setText("Requesting Winsh");
            ((Button) findViewById(R.id.request_wenshi_bottom_btn)).setVisibility(View.GONE);
            ((Button) findViewById(R.id.cancel_wenshi_btn)).setVisibility(View.VISIBLE);

            getClosestDriver();
        }
    }

    private void getClosestDriver() {
        DatabaseReference driverLoc = FirebaseDatabase.getInstance().getReference("DriversAvailable");

        GeoFire geoFire = new GeoFire(driverLoc);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(pickUpLocation.latitude, pickUpLocation.longitude), radius);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!driverFound) {
                    driverFound = true;
                    requestedDriverID = key;

                    DatabaseReference driver = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(requestedDriverID);

                    HashMap hm = new HashMap();
                    hm.put("CustomerID", userId);

                    driver.updateChildren(hm);
                    traceDriver();
                    mRequest.setText("We find driver for you!");
                    mCancel.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!driverFound) {
                    radius++;
                    //   getClosestDriver();
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

        Location driverLoc = new Location("");
        driverLoc.setLatitude(driverLatLang.latitude);
        driverLoc.setLongitude(driverLatLang.longitude);

        Location pickupLoc = new Location("");
        pickupLoc.setLatitude(pickUpLocation.latitude);
        pickupLoc.setLongitude(pickUpLocation.longitude);

        if (driverLoc.distanceTo(pickupLoc) < 100) {
            mRequest.setText("Your driver is here"); //Send a notification
        } else

            mRequest.setText(String.valueOf(driverLoc.distanceTo(pickupLoc)));
    }

    private void cancelTrip() {

        mMap.clear();
        driverLocationRef.removeEventListener(driverLocationRefListener);

        if (requestedDriverID != null) {   //Remove CustomerID child
            DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(requestedDriverID);
            driverRef.setValue(true);
            requestedDriverID = null;

        }
        //Intialize nearest driver query variables
        geoQuery.removeAllListeners();
        driverFound = false;
        radius = 1;

        //Remove request from DB
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Requests");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);


        ((Button) findViewById(R.id.request_wenshi_bottom_btn)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.cancel_wenshi_btn)).setVisibility(View.GONE);

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
        }
        else if (id == R.id.nav_history) {
            fragment = new HistoricFragment();
        }
        else if(id == R.id.nav_payment)    {
            fragment = new PaymentOptions();
        }
        else if(id == R.id.nav_help)    {
            fragment = new HelpFragment();
        }
        else if(id == R.id.nav_rateCharges)    {
            fragment = new RateAndChargesFragment();
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
        getSupportActionBar().setTitle(uri.getHost());

    }


    protected void onStop() {
        super.onStop();
        // remove all event listeners to stop updating in the background
        if (geoQuery != null) this.geoQuery.removeAllListeners();

        if (this.markers != null) {
            for (Marker marker : this.markers.values()) {
                marker.remove();
            }
            this.markers.clear();
        }
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
}
