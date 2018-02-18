package com.wenshi_egypt.wenshi;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
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

public class CustomerMapActivity extends AppCompatActivity implements View.OnClickListener,
        ProfileFragment.OnFragmentInteractionListener,
        HistoryFragment.OnFragmentInteractionListener,
        DriverFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private Marker mDriverMarker;

    private SupportMapFragment mapFragment;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    final int location_request_code = 1;

    private Button mRequest, mCancel;
    private LatLng pickUpLocation;

    private boolean driverFound = false;
    private float radius = 1;
    private String requestedDriverID = "";

    private GeoQuery geoQuery;
    private DatabaseReference driverLocationRef;
    private ValueEventListener driverLocationRefListener;

    private String userId;


    private AppCompatDelegate mDelegate;
    private int mThemeId = 0;
    private Resources mResources;
    LocationManager locationManager;
    String provider;
    Location lastLocation;
    DrawerLayout drawer;
    View mBottomSheet;

    BottomSheetBehavior mBottomSheetBehavior;
    Button mButton;
    View fab;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.customer_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.customer_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.customer_map);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, location_request_code);
        } else {
            mapFragment.getMapAsync(this);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, location_request_code);
        }
        //  locationManager.requestLocationUpdates(provider, 800, 100);

        lastLocation = locationManager.getLastKnownLocation(provider);

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
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTrip();
            }
        });


    //get the bottom sheet view
        fab = findViewById(R.id.fab);
        mBottomSheet = findViewById(R.id.bottom_sheet);


// init the bottom sheet behavior
        mBottomSheetBehavior= BottomSheetBehavior.from(mBottomSheet);

// change the state of the bottom sheet
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
       //
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

// set the peek height
   //     mBottomSheetBehavior.setPeekHeight(320);

// set hideable or not
        mBottomSheetBehavior.setHideable(true);

// set callback for changes
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                                                               @Override
                                                               public void onStateChanged(@NonNull View bottomSheet,
                                                                                          int newState) {
                                                               }
                                                               @Override
                                                               public void onSlide(@NonNull View bottomSheet, float
                                                                       slideOffset) {
                                                                   fab.animate().scaleX(1 - slideOffset).scaleY(1 -
                                                                           slideOffset).setDuration(0).start();
                                                               } });

        getSupportActionBar().setTitle("Customer View");

        findViewById(R.id.toolbar).bringToFront();
        findViewById(R.id.toolbar).requestLayout();
      //  findViewById(R.id.main_body)
       // findViewById(R.id.customer_map).bringToFront();
        //findViewById(R.id.customer_map).requestLayout();
        findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
       navigationView.bringToFront();
        navigationView.requestLayout();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
               // mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
        if (lastLocation != null)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()), 15));

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Location location;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (location != null) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY); //drains battery

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerMapActivity.this ,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, location_request_code);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case location_request_code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapFragment.getMapAsync(this);
                } else {
                    Toast.makeText(this, "Location can't be accessed", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void requestButtonClicked(){


        if( mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    else {
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Requests");

            GeoFire geoFire = new GeoFire(dbRef); //The reference where the data is stored
            geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

            pickUpLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(pickUpLocation).title("Your Pickup"));

            mRequest.setText("Requesting Winsh");
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
                if(!driverFound){
                    driverFound = true;
                    driverFound = true;
                    requestedDriverID = key;

                    DatabaseReference driver = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(requestedDriverID);

                    HashMap hm = new HashMap();
                    hm.put("CustomerID",userId);

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
                if(!driverFound){
                    radius ++;
                    getClosestDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private void traceDriver(){

        //Drivers on Trips location will be updated automatically from OnLocationChanged function in DriverMapAcitvity
        driverLocationRef = FirebaseDatabase.getInstance().getReference("DriversOnTrips").child(requestedDriverID).child("l");
        driverLocationRefListener = driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();


                    if(mDriverMarker!=null)    //To check if it is not first time to add marker
                        mDriverMarker.remove();

                    if(map.get(0)!= null && map.get(1)!= null)
                        mDriverMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(map.get(0).toString()),Double.parseDouble(map.get(1).toString()))).title("Driver location"));

                    getDistanceBetweenPickUpToDriver(new LatLng(Double.parseDouble(map.get(0).toString()),Double.parseDouble(map.get(1).toString())));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    private void getDistanceBetweenPickUpToDriver(LatLng driverLatLang){            //Convert it to time

        Location driverLoc = new Location("");
        driverLoc.setLatitude(driverLatLang.latitude);
        driverLoc.setLongitude(driverLatLang.longitude);

        Location pickupLoc = new Location("");
        pickupLoc.setLatitude(pickUpLocation.latitude);
        pickupLoc.setLongitude(pickUpLocation.longitude);

        if(driverLoc.distanceTo(pickupLoc)<100){
            mRequest.setText("Your driver is here"); //Send a notification
        }
        else

            mRequest.setText(String.valueOf(driverLoc.distanceTo(pickupLoc)));
    }

    private void cancelTrip(){

        mMap.clear();
        driverLocationRef.removeEventListener(driverLocationRefListener);

        if (requestedDriverID != null){   //Remove CustomerID child
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
     *
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
        } else if (id == R.id.nav_history) {
            fragment = new HistoryFragment();
        }

        //NOTE: Fragment changing code
        if (fragment != null) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            fab.setVisibility(View.INVISIBLE);
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
}
