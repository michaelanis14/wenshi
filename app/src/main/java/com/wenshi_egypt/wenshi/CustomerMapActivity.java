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
import android.os.ResultReceiver;
import android.service.chooser.ChooserTargetService;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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
import com.wenshi_egypt.wenshi.helpers.AppUtils;
import com.wenshi_egypt.wenshi.helpers.FetchAddressIntentService;
import com.wenshi_egypt.wenshi.model.GetDirectionsData;
import com.wenshi_egypt.wenshi.model.Model;
import com.wenshi_egypt.wenshi.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionClient;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.wenshi_egypt.wenshi.model.VehicleModel;

import static com.wenshi_egypt.wenshi.helpers.AppUtils.Defs.CAIRO;

public class CustomerMapActivity extends AppCompatActivity implements GetDirectionsData.AsyncResponse, View.OnClickListener,RateDriverFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, HistoricFragment.OnFragmentInteractionListener, VehiclesFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener, PaymentOptions.OnFragmentInteractionListener, HelpFragment.OnFragmentInteractionListener, RateAndChargesFragment.OnFragmentInteractionListener, AboutFragment.OnFragmentInteractionListener, InviteFragment.OnFragmentInteractionListener, FamilyViewFragment.OnFragmentInteractionListener, FamilyRequestFragment.OnFragmentInteractionListener, ReviewRequestFragment.OnFragmentInteractionListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    private static final long UPDATE_INTERVAL = 50000;
    private static final long FASTEST_INTERVAL = 30000;
    private static final float DISPLACMENT = 100;
    private static final int PICKUP = 0;
    private static final int SERVICECHOICE = 1;
    private static final int DESTINATION = 2;
    private static final int READYTOREQ = 3;
    private static final int REVIEWREQ = 4;
    private static final int REQ = 5;
    private static final int CANCELREQ = 6;
    private static final int TRACEDRIVER = 7;
    private static final int TODESTINATION = 8;
    private static final int RATEDRIVER = 9;


    private static final int REQUEST_CODE_AUTOCOMPLETE = 114;
    static GoogleApiClient mGoogleApiClient;
    private static String TAG = "MAP LOCATION";
    final int MY_PERMISSION_REQ_CODE = 1234;
    final int PLAY_SERVICE_RESLUOTION_CODE = 2345;
    public GetDirectionsData getDirectionsData;
    /**
     * The formatted location address.
     */
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    View mBottomSheet;
    TextView mBottomTextView;
    BottomSheetBehavior mBottomSheetBehavior;
    UserModel user;
    UserModel driverModel;
    DatabaseReference driversAvlbl;
    TextView timerTextView;
    long startTime = 0;
    Context mContext;
    TextView mPickupText;
    TextView mDestinationText;
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
    View mapView;
    Model model;
    private int CURRENTSTATE;
    private GoogleMap mMap;
    private Marker mDriverMarker;
    private Marker mPickupMarker;
    private Marker mDestination;
    private Marker mChoiceMarker;
    private SupportMapFragment mapFragment;
    private Button mbtn1, mbtn2, mbtn3;
    private boolean driverFound = false;
    private float radius = 2;
    private GeoQuery geoQuery;
    private DatabaseReference driverLocationRef;
    private ValueEventListener driverLocationRefListener;
    private String userId;
    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private Resources mResources;
    private GeoFire geoFireCustomerLocation;
    private DatabaseReference customerLocation;
    private DatabaseReference requestDriver;
    private Marker myCurrent;
    private Map<String, Marker> markers;
    private List<String> driversID;
    private LatLng mCenterLatLong;
    private AddressResultReceiver mResultReceiver;
    private String duration;
    private String distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mContext = this;

        model = new Model();

        Intent i = getIntent();
        user = (UserModel) i.getParcelableExtra("CurrentUser");
        user.setVehicle(new VehicleModel("KIA", "RIO 2014"));

        driverModel = new UserModel("", "", "", "", "");

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.customer_map);
        mapFragment.getMapAsync(this);

        customerLocation = FirebaseDatabase.getInstance().getReference("CustomersOnline");
        geoFireCustomerLocation = new GeoFire(customerLocation);
        requestDriver = FirebaseDatabase.getInstance().getReference("Users").child("Drivers");
        driversAvlbl = FirebaseDatabase.getInstance().getReference("DriversAvailable");


        // mLocationMarkerText = (TextView) findViewById(R.id.locationMarkertext);
        mPickupText = (TextView) findViewById(R.id.PickupLocality);
        mDestinationText = (TextView) findViewById(R.id.DestinationLocality);


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

        mbtn1 = (Button) findViewById(R.id.bottom_view_btn1);
        mbtn1.setOnClickListener(this);
        mbtn2 = (Button) findViewById(R.id.bottom_down_btn2);
        mbtn2.setOnClickListener(this);

        mbtn3 = (Button) findViewById(R.id.bottom_down_btn3);
        mbtn3.setOnClickListener(this);


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
                    //  mbtn1.setVisibility(View.VISIBLE);
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    //     mbtn1.setVisibility(View.GONE);
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
        navigationView.bringToFront();
        navigationView.requestLayout();


        mResultReceiver = new AddressResultReceiver(new Handler());

        mPickupText.setOnClickListener(this);
        mDestinationText.setOnClickListener(this);

        CURRENTSTATE = -1;


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
                    //    Toast.makeText(this, "Location can't be accessed", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void setupLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQ_CODE);
        }
        {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }

        }
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location can't be accessed - displayLocation", Toast.LENGTH_SHORT).show();
            setupLocation();

        }
        if (mGoogleApiClient != null)
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null && mMap != null) {

            /*
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

*/
            //  mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            myCurrent = mMap.addMarker(new MarkerOptions().position(loc));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15.0f));


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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            setupLocation();
            return false;
        }

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
            case R.id.bottom_view_btn1:
                switch (CURRENTSTATE) {
                    case PICKUP:
                        customerViewStateControler(SERVICECHOICE);
                        break;
                    case SERVICECHOICE:
                        user.setServiceType("ACCEDIENT");
                        customerViewStateControler(DESTINATION);
                        break;
                    case DESTINATION:
                        showRout();
                        break;
                    case REVIEWREQ:
                        customerViewStateControler(READYTOREQ);
                        break;
                    case READYTOREQ:
                        customerViewStateControler(REQ);
                        break;
                    case TRACEDRIVER:

                        if(driverModel != null && driverModel.getMobile() != null)
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", driverModel.getMobile(), null)));

                       // customerViewStateControler(REQ);
                        break;
                }
                // mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;


            case R.id.bottom_down_btn2:
                switch (CURRENTSTATE) {
                    case SERVICECHOICE:
                        user.setServiceType("CARBROKEDOWN");
                        customerViewStateControler(DESTINATION);
                        break;
                    case REQ:
                        cancelTrip();
                        customerViewStateControler(DESTINATION);
                        break;
                    case TRACEDRIVER:
                        cancelTrip();
                        customerViewStateControler(DESTINATION);
                        break;

                }
                // mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.bottom_down_btn3:
                switch (CURRENTSTATE) {
                    case PICKUP:
                        break;
                    case SERVICECHOICE:
                        break;
                }
                // mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.button:
                // mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.PickupLocality:
                customerViewStateControler(PICKUP);
                openAutocompleteActivity();
                break;
            case R.id.DestinationLocality:
                // customerViewStateControler(DESTINATION);
                openAutocompleteActivity();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.customer_map);
        mapView = mapFragment.getView();
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                // Log.d("Camera postion change" + "", cameraPosition + "");
                if (CURRENTSTATE == PICKUP || CURRENTSTATE == DESTINATION) {
                    if (CURRENTSTATE == DESTINATION && mDestination != null) {
                        mDestination.remove();
                        markers.remove("Destination");
                    }

                    mCenterLatLong = cameraPosition.target;
                    //  mMap.clear();
                    try {
                        Location mLocation = new Location("");
                        mLocation.setLatitude(mCenterLatLong.latitude);
                        mLocation.setLongitude(mCenterLatLong.longitude);
                        if (mChoiceMarker != null) mChoiceMarker.remove();

                        mChoiceMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(mLocation.getLatitude(), mLocation.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.add_marker)));


                        setLocation(mLocation);
                        startIntentService(mLocation);
                        //    mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            setupLocation();
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 0, 240);
        }
        customerViewStateControler(PICKUP);
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onResume() {
        super.onResume();
//        displayLocation();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //   setupLocation();
        // displayLocation();
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


        try {
            if (location != null) changeMap(location);
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeMap(Location location) {
        Log.d(TAG, "Reaching map" + mMap);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            setupLocation();
        }

        // check if map is created successfully or not
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;
            latLong = new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLong).zoom(15f).tilt(70).build();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mLastLocation = location;
            setLocation(location);
            startIntentService(location);
        } else {
            Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }

    }

    private void setLocation(Location location) {
        if (CURRENTSTATE == PICKUP) {
            user.setPickup(location);
            user.setDestination(location);
        } else if (CURRENTSTATE == DESTINATION) {
            user.setDestination(location);
        }
    }

    private void setLocationString(String location) {
        if (CURRENTSTATE == PICKUP) {
            user.setPickupAddress(location);
        } else if (CURRENTSTATE == DESTINATION) {
            user.setDestinationAddress(location);
        }
    }


    private void getClosestDriver() {

        Log.i("getClosestDriver", user.getPickup().toString());
        GeoFire geoFire = new GeoFire(driversAvlbl);

        geoQuery = geoFire.queryAtLocation(new GeoLocation(user.getPickup().getLatitude(), user.getPickup().getLongitude()), radius);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                driverFound = true;
                System.out.println(String.format("DRIVER FOUND", key));

                driversID.add(key);
                driverModel.setID(key);
                geoQuery.removeAllListeners();
                final DatabaseReference driver = requestDriver.child(key).child("Requests").child(userId);

                HashMap requestDetails = new HashMap();
                requestDetails.put("Accept", "false");
                requestDetails.put("Customer", user.toString());
                requestDetails.put("Timestamp", ServerValue.TIMESTAMP);


                driver.updateChildren(requestDetails);

                GeoFire customerGeoFire = new GeoFire(driver);
                customerGeoFire.setLocation("PickupLocation", new GeoLocation(user.getPickup().getLatitude(), user.getPickup().getLongitude()));
                customerGeoFire.setLocation("DropOffLocation", new GeoLocation(user.getDestination().getLatitude(), user.getDestination().getLongitude()));

                GeoFire geoDriverLocation = new GeoFire(driversAvlbl);


                geoDriverLocation.getLocation(key, new LocationCallback() {
                    @Override
                    public void onLocationResult(String key, GeoLocation location) {
                        if (location != null) {
//                            System.out.println(String.format("DRIVER FOUND", key));

                            if (mDriverMarker != null) {
                                mDriverMarker.remove();
                                markers.remove(mDriverMarker.getId());
                            }
                            if (driverModel != null) {
                                Location locat = new Location("dummyprovider");
                                locat.setLatitude(location.latitude);
                                locat.setLongitude(location.longitude);
                                driverModel.setCurrentLocation(locat);
                            }
                            mDriverMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title("Wenshi"));
                            markers.put(mDriverMarker.getId(), mDriverMarker);
                            marksCameraUpdate();
                            //   getDistanceBetweenPickUpToDriver(new LatLng(location.latitude, location.longitude));
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
                final DatabaseReference driverAccept = requestDriver.child(key).child("Requests").child(userId);
                driverLocationRefListener = driverAccept.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot imageSnapshot : dataSnapshot.getChildren()) {
                            if (imageSnapshot.getKey().equals("Accept") && imageSnapshot.getValue().toString().equals("true")) {
                                System.out.println(String.format("DRIVER Accepted", imageSnapshot.getKey()));
                                driverAccept.removeEventListener(driverLocationRefListener);
                                model.linkProfData(false, driverModel);
                                //   driverModel.linkProfData(false);
                                Log.i("DRIVER MODEL",driverModel.toString());
                                customerViewStateControler(TRACEDRIVER);

                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //  traceDriver();
                // mbtn1.setText("We find driver for you!");
                // mbtn2.setVisibility(View.VISIBLE);


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
        Log.i("TRACE BEFORE",driverModel.getID());
        //Drivers on Trips location will be updated automatically from OnLocationChanged function in DriverMapAcitvity
        driverLocationRef = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(driverModel.getID()).child("CurrentLocation").child("l");
        driverLocationRefListener = driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    Log.i("TRACE IN",map.get(0).toString());

                    if (mDriverMarker != null)    //To check if it is not first time to add marker
                        mDriverMarker.remove();

                    if (map.get(0) != null && map.get(1) != null)
                        mDriverMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(map.get(0).toString()), Double.parseDouble(map.get(1).toString()))).title("Driver location"));

                    Location locat = new Location("dummyprovider");
                    locat.setLatitude(Double.parseDouble(map.get(0).toString()));
                    locat.setLongitude(Double.parseDouble(map.get(1).toString()));

                    driverModel.setCurrentLocation(locat);
                    getDistanceBetweenPickUpToDriver();

                    marksCameraUpdate();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getDistanceBetweenPickUpToDriver() {            //Convert it to time

        if(CURRENTSTATE == PICKUP) {
            if (driverModel.getCurrentLocation().distanceTo(user.getPickup()) < 400) {
                mBottomTextView.setText("Your driver is here"); //Send a notification
                customerViewStateControler(TODESTINATION);
            }
        }
        else if(CURRENTSTATE == TODESTINATION){
            if (driverModel.getCurrentLocation().distanceTo(user.getPickup()) < 400) {
                mBottomTextView.setText("You have arrived"); //Send a notification
                customerViewStateControler(RATEDRIVER);
            }
        }
    }

    private void cancelTrip() {

        mMap.clear();
        markers.clear();
        setMarker(true);
        if (mDriverMarker != null) mDriverMarker.remove();
        marksCameraUpdate();

        if (driverLocationRef != null)
            driverLocationRef.removeEventListener(driverLocationRefListener);

        // driverLocationRef.removeEventListener(driverLocationRefListener);


        for (String driverID : driversID) {
            requestDriver.child(driverID).child("Requests").child(userId).removeValue();
        }
        //Intialize nearest driver query variables
        if (geoQuery != null) geoQuery.removeAllListeners();
        driverFound = false;
        radius = 1;


    }

    @Override
    public void setTheme(@StyleRes final int resid) {
        super.setTheme(resid);
        // Keep hold of the theme id so that we can re-set it later if needed
        //mThemeId = resid;
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
        } else if (findViewById(R.id.mainFrame).getVisibility() == View.VISIBLE) {
            customerViewStateControler(CURRENTSTATE);
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
        //builder.include(myCurrent.getPosition());
        for (Marker marker : this.markers.values()) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 350;
        if(CURRENTSTATE == TRACEDRIVER)
            padding = 200;
        // offset from edges of the map in pixels
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


    protected void displayAddressOutput() {
        try {
            //    mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude + mAddressOutput);
            if (CURRENTSTATE == PICKUP) mPickupText.setText(mAddressOutput);
            else if (CURRENTSTATE == DESTINATION) mDestinationText.setText(mAddressOutput);


            setLocationString(mAddressOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void startIntentService(Location mLocation) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        // Pass the result receiver as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver);
        // Pass the location data as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);
        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.

            AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().setTypeFilter(Place.TYPE_COUNTRY).setCountry("EG").build();

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(autocompleteFilter).build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(), 0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(mContext, data);
                LatLng latLong;
                latLong = place.getLatLng();

                Location locat = new Location("dummyprovider");
                locat.setLatitude(place.getLatLng().latitude);
                locat.setLongitude(place.getLatLng().latitude);
                setLocation(locat);
                //mPickupText.setText(place.getName() + "");

                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLong).zoom(15f).tilt(70).build();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    setupLocation();
                }
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }


        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(mContext, data);
        } else if (resultCode == RESULT_CANCELED) {
            // Indicates that the activity closed before a selection was made. For example if
            // the user pressed the back button.
        }
    }


    private void setMarker(boolean force) {

        if (mDriverMarker != null) {
            markers.remove(mDriverMarker.getId());
            mDriverMarker.remove();
        }

        if (CURRENTSTATE == PICKUP || force) {
            if (mPickupMarker == null || force)
                mPickupMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(user.getPickup().getLatitude(), user.getPickup().getLongitude())).title(getResources().getString(R.string.pickup)));
            else
                mPickupMarker.setPosition(new LatLng(user.getPickup().getLatitude(), user.getPickup().getLongitude()));
            markers.put("Pickup", mPickupMarker);
            mPickupMarker.showInfoWindow();
        }
        if (CURRENTSTATE == DESTINATION || force) {
            // Log.i("setMarkers", "" + CURRENTSTATE + "    -" + user.getDestination().getLatitude());
            if (mDestination == null || force)
                mDestination = mMap.addMarker(new MarkerOptions().position(new LatLng(user.getDestination().getLatitude(), user.getDestination().getLongitude())).title(getResources().getString(R.string.destination)));
            else
                mDestination.setPosition(new LatLng(user.getDestination().getLatitude(), user.getDestination().getLongitude()));
            markers.put("Destination", mDestination);
            //mDestination.showInfoWindow();
        }

        if (CURRENTSTATE == TRACEDRIVER && user != null && driverModel != null) {
            mPickupMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(user.getPickup().getLatitude(), user.getPickup().getLongitude())).title(getResources().getString(R.string.pickup)));
            markers.put("Pickup", mPickupMarker);
            mPickupMarker.showInfoWindow();

            if (driverModel.getCurrentLocation() != null) {
                mDriverMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(driverModel.getCurrentLocation().getLatitude(), driverModel.getCurrentLocation().getLongitude())).title("Wenshi"));
                markers.put(mDriverMarker.getId(), mDriverMarker);
            }

        }

        // marksCameraUpdate();
    }

    private void customerViewStateControler(int customerState) {
        switch (customerState) {
            case PICKUP:
                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);

                findViewById(R.id.PickupLayout).setVisibility(View.VISIBLE);
                mPickupText.setEnabled(true);
                findViewById(R.id.DestinationLayout).setVisibility(View.GONE);
                mDestinationText.setEnabled(false);
                mBottomTextView.setText(getResources().getString(R.string.confirm_pickup));
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mbtn2.setVisibility(View.GONE);
                mbtn3.setVisibility(View.GONE);
                mbtn1.setVisibility(View.VISIBLE);
                mbtn1.setText(getResources().getString(R.string.choose_service_btn));
                if (mChoiceMarker != null) mChoiceMarker.setVisible(true);
                CURRENTSTATE = customerState;
                break;

            case SERVICECHOICE:

                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);

                findViewById(R.id.PickupLayout).setVisibility(View.VISIBLE);
                mPickupText.setEnabled(false);
                findViewById(R.id.DestinationLayout).setVisibility(View.GONE);
                mDestinationText.setEnabled(false);

                mBottomTextView.setText(getString(R.string.choose_service_btn));

                findViewById(R.id.request_wenshi_info).setVisibility(View.GONE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                mbtn1.setVisibility(View.VISIBLE);
                mbtn1.setText(getResources().getString(R.string.accedent));
                mbtn2.setVisibility(View.VISIBLE);
                mbtn2.setText(getResources().getString(R.string.carbroke));
                mbtn3.setVisibility(View.GONE);
                setMarker(false);
                if (mChoiceMarker != null) mChoiceMarker.setVisible(false);
                CURRENTSTATE = customerState;
                break;

            case DESTINATION:
                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                findViewById(R.id.PickupLayout).setVisibility(View.VISIBLE);

                changeMap(user.getDestination());
                mPickupText.setEnabled(false);

                findViewById(R.id.DestinationLayout).setVisibility(View.VISIBLE);
                mDestinationText.setEnabled(true);
                mBottomTextView.setText(getResources().getString(R.string.confirm_destination));
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mbtn2.setVisibility(View.GONE);
                mbtn3.setVisibility(View.GONE);
                mbtn1.setVisibility(View.VISIBLE);
                mbtn1.setText(getResources().getString(R.string.confirm));
                setMarker(false); // delay the set Marker one step behind
                if (mChoiceMarker != null) mChoiceMarker.setVisible(true);
                CURRENTSTATE = customerState;
                break;
            case REVIEWREQ:
                findViewById(R.id.mainFrame).setVisibility(View.VISIBLE);
                findViewById(R.id.PickupLayout).setVisibility(View.INVISIBLE);
                findViewById(R.id.DestinationLayout).setVisibility(View.INVISIBLE);
                mDestinationText.setEnabled(false);
                mPickupText.setEnabled(false);
                Fragment fragment = new ReviewRequestFragment();

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, fragment);
                ft.commit();

                mBottomTextView.setText(getString(R.string.request_wenshi));
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mbtn1.setVisibility(View.VISIBLE);
                mbtn1.setText(getResources().getString(R.string.find_driver));

                //setMarker(false); // delay the set Marker one step behind
                CURRENTSTATE = customerState;
                if (mChoiceMarker != null) mChoiceMarker.setVisible(false);
                break;
            case READYTOREQ:
                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                findViewById(R.id.PickupLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.DestinationLayout).setVisibility(View.VISIBLE);
                mDestinationText.setEnabled(false);
                mPickupText.setEnabled(false);

                mBottomTextView.setText(getResources().getString(R.string.reqwestingWenshi));
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mbtn1.setVisibility(View.GONE);
                mbtn2.setVisibility(View.VISIBLE);
                mbtn2.setText(getResources().getString(R.string.cancelTrip));

                radius = 1;
                CURRENTSTATE = customerState;
                if (mChoiceMarker != null) mChoiceMarker.setVisible(false);
                customerViewStateControler(REQ);
                break;
            case REQ:
                getClosestDriver();
                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                findViewById(R.id.PickupLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.DestinationLayout).setVisibility(View.VISIBLE);
                mDestinationText.setEnabled(false);
                mPickupText.setEnabled(false);

                mBottomTextView.setText(getResources().getString(R.string.reqwestingWenshi));
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mbtn1.setVisibility(View.GONE);
                mbtn2.setVisibility(View.VISIBLE);
                mbtn2.setText(getResources().getString(R.string.cancelTrip));


                CURRENTSTATE = customerState;
                if (mChoiceMarker != null) mChoiceMarker.setVisible(false);
                break;
            case TRACEDRIVER:
                //   getClosestDriver();

                if (mDestination != null) mDestination.remove();


                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                findViewById(R.id.PickupLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.DestinationLayout).setVisibility(View.VISIBLE);
                mDestinationText.setEnabled(false);
                mPickupText.setEnabled(false);

                mBottomTextView.setText(getResources().getString(R.string.driver_confirmed_onrout));
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mbtn1.setVisibility(View.VISIBLE);
                mbtn1.setText(getResources().getString(R.string.callDriver));
                mbtn2.setVisibility(View.VISIBLE);
                mbtn2.setText(getResources().getString(R.string.cancelTrip));


                CURRENTSTATE = customerState;
                showRout();
                marksCameraUpdate();
                if (mChoiceMarker != null) mChoiceMarker.setVisible(false);
                traceDriver();
                break;
            case TODESTINATION:
                //   getClosestDriver();

                if (mDestination != null) mDestination.remove();


                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                findViewById(R.id.PickupLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.DestinationLayout).setVisibility(View.VISIBLE);
                mDestinationText.setEnabled(false);
                mPickupText.setEnabled(false);

                mBottomTextView.setText(getResources().getString(R.string.driver_confirmed_onrout));
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mbtn1.setVisibility(View.VISIBLE);
                mbtn1.setText(getResources().getString(R.string.callDriver));
                mbtn2.setVisibility(View.VISIBLE);
                mbtn2.setText(getResources().getString(R.string.cancelTrip));


                CURRENTSTATE = customerState;
                showRout();
                marksCameraUpdate();
                if (mChoiceMarker != null) mChoiceMarker.setVisible(false);
                traceDriver();
                break;
            case RATEDRIVER:
                //   getClosestDriver();

                if (mDestination != null) mDestination.remove();

                findViewById(R.id.mainFrame).setVisibility(View.VISIBLE);
                findViewById(R.id.PickupLayout).setVisibility(View.INVISIBLE);
                findViewById(R.id.DestinationLayout).setVisibility(View.INVISIBLE);
                mDestinationText.setEnabled(false);
                mPickupText.setEnabled(false);
                Fragment rateDriver = new ReviewRequestFragment();

                FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
                ftt.replace(R.id.mainFrame, rateDriver);
                ftt.commit();


                mDestinationText.setEnabled(false);
                mPickupText.setEnabled(false);

                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                mbtn1.setVisibility(View.INVISIBLE);
                mbtn2.setVisibility(View.INVISIBLE);


                CURRENTSTATE = customerState;


               // showRout();
                //marksCameraUpdate();
               // if (mChoiceMarker != null) mChoiceMarker.setVisible(false);
               // traceDriver();
                break;
        }
    }

    private String getDirectionsUrl() {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        if (user != null && !user.getID().isEmpty()) {
            googleDirectionsUrl.append("origin=" + user.getPickup().getLatitude() + "," + user.getPickup().getLongitude());

            if (CURRENTSTATE == DESTINATION)
                googleDirectionsUrl.append("&destination=" + user.getDestination().getLatitude() + "," + user.getDestination().getLongitude());
            else if (CURRENTSTATE == TRACEDRIVER && driverModel != null)
                googleDirectionsUrl.append("&destination=" + driverModel.getCurrentLocation().getLatitude() + "," + driverModel.getCurrentLocation().getLongitude());
        }

        googleDirectionsUrl.append("&departure_time=now&key=" + getResources().getString(R.string.google_geo_maps_key));

        return googleDirectionsUrl.toString();
    }

    private void showRout() {
        mMap.clear();
        markers.clear();
        // clearMarkers();

        //displayLocation();


        Object dataTransfer[] = new Object[2];
        dataTransfer = new Object[5];
        String url = getDirectionsUrl();
        getDirectionsData = new GetDirectionsData(this);
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        if (CURRENTSTATE == TRACEDRIVER && driverModel != null && driverModel.getCurrentLocation() != null) {
            dataTransfer[2] = new LatLng(driverModel.getCurrentLocation().getLatitude(), driverModel.getCurrentLocation().getLongitude());

        } else {
            dataTransfer[2] = new LatLng(user.getDestination().getLatitude(), user.getDestination().getLongitude());

        }
        dataTransfer[3] = duration;
        dataTransfer[4] = distance;
        getDirectionsData.execute(dataTransfer);
        if (CURRENTSTATE == TRACEDRIVER && user != null) {
            setMarker(false);
        } else {
            setMarker(true);
        }

    }

    @Override
    public void gotDurationDistanceRout(String output) {

        if (CURRENTSTATE == DESTINATION) customerViewStateControler(REVIEWREQ);

        // mBottomTextView.setText(getResources().getString(R.string.eta) + " " + getDirectionsData.getDuration());
        //   driverViewStateControler(ONROUT); //must be after showRout to get the correct duration


    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY);
            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);
            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET);

            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
                //    Log.i("ADDRESS", resultData.toString());


            }


        }

    }

}
