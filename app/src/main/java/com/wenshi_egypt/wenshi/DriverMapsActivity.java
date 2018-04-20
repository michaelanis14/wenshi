package com.wenshi_egypt.wenshi;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.wenshi_egypt.wenshi.model.GetDirectionsData;
import com.wenshi_egypt.wenshi.model.HistoryModel;
import com.wenshi_egypt.wenshi.model.UserModel;
import com.wenshi_egypt.wenshi.model.VehicleModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DriverMapsActivity extends AppCompatActivity implements GetDirectionsData.AsyncResponse, View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DriverProfileFragment.OnFragmentInteractionListener, CustomerHistoryFragment.OnFragmentInteractionListener, OnNavigationItemSelectedListener, com.google.android.gms.location.LocationListener {

    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 3000;
    private static final float DISPLACMENT = 10;
    private static final int ONLINE = 0;
    private static final int NEWREQ = 1;
    private static final int ONROUT = 2;
    private static final int ARRIVE = 3;
    private static final int OFFLINE = 5;
    private static final int SIDENAV = 6;
    private static final int NEARCUSTOMER = 7;
    private static final int ARRIVED = 8;
    private static final int TOOKPHOTOS = 9;
    private static final int TODISTINATION = 10;
    private static final int ENDTRIP = 11;
    private static final int RATE = 12;
    private static final int ATDISTINATION = 14;
    //static final LatLng CAIRO = new LatLng(30.044281, 31.340002);
    final int MY_PERMISSION_REQ_CODE = 1234;
    final int PLAY_SERVICE_RESLUOTION_CODE = 2345;
    int PROXIMITY_RADIUS = 10000;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    DrawerLayout drawer;
    View mBottomSheet;
    TextView mBottomTextView;
    BottomSheetBehavior mBottomSheetBehavior;
    DatabaseReference driverAvalbl;
    DatabaseReference driver;
    UserModel driverMod;
    private LinkedHashMap<String, DataSnapshot> requestsMap;
    boolean onRout;
    Marker mCustomerMarker;
    double timeSec;
    private int CURRENTSTATE;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private DatabaseReference driverLocation;
    private GeoFire geoFireDriverLocation;
    private GeoFire geoFireOnRoutLocation;
    private String userId = "D_";
    private String assignedCustomer = "";
    private Boolean makeRequest = false;
    private DatabaseReference assignedCustomerPickupLocationRef;
    private ValueEventListener assignedCustomerPickupLocationRefEventListner;
    private Marker myCurrent;
    private Map<String, Marker> markers;
    private GeoQuery geoQuery;
    private ValueEventListener geoFireDriverRequests;
    private Switch swtch_onlineOffline;
    private LinearLayout mRelativeLayout;
    private LinearLayout monlineOfflineLayout;
    private Button bottomButton1_btn;
    private Button bottomButton2_btn;
    private ImageView call_btn, navigation_btn;
    private PopupWindow mPopupWindow;
    private UserModel cutomerMod = null;
    private Context mContext;
    private GetDirectionsData getDirectionsData;
    private Fragment currentFragment;
    private String duration;
    private String distance;
    private HistoryModel currentHistory;
    private HistoryDetailsFragment historyDetailsFragment;
    private DatabaseReference addHistory;
    private VehicleModel customerVehicle;
    private Menu menu;

    public HistoryModel getCurrentHistory() {
        return currentHistory;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_driver_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        Intent i = getIntent();
        driverMod = (UserModel) i.getParcelableExtra("CurrentUser");

        mContext = getApplicationContext();
        mRelativeLayout = (LinearLayout) findViewById(R.id.drive_main_layout);
        monlineOfflineLayout = (LinearLayout) findViewById(R.id.onlineOfflineLayout);
        swtch_onlineOffline = findViewById(R.id.onlineOffline_swtch);

        bottomButton1_btn = (Button) findViewById(R.id.btn_1);
        bottomButton1_btn.setBackgroundColor(Color.BLACK);
        bottomButton2_btn = (Button) findViewById(R.id.btn_2);
        bottomButton2_btn.setBackgroundColor(Color.BLACK);
        bottomButton1_btn.setOnClickListener(this);
        bottomButton2_btn.setOnClickListener(this);

        call_btn = (ImageView) findViewById(R.id.img_call_btn);
        call_btn.setOnClickListener(this);
        navigation_btn = (ImageView) findViewById(R.id.img_map_btn);
        navigation_btn.setOnClickListener(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.driver_map);
        mapFragment.getMapAsync(this);
        driverLocation = FirebaseDatabase.getInstance().getReference("DriversAvailable");
        setupLocation();
        onRout = false;
        drawer = (DrawerLayout) findViewById(R.id.driver_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.driver_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.bringToFront();
        navigationView.requestLayout();

        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.textViewUsernameNav);
        nav_user.setText(driverMod.getName());
        Button logout = (Button) hView.findViewById(R.id.nav_header_logout);
        logout.setOnClickListener(this);
        //hide the accept request buttons
        //

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        driverMod.setID(userId);
        driverAvalbl = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(userId).child("Requests");
        driver = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(userId);
        geoFireDriverLocation = new GeoFire(driverLocation);
        geoFireOnRoutLocation = new GeoFire(driver);

        markers = new HashMap<String, Marker>();
        requestsMap = (new LinkedHashMap<String, DataSnapshot>());


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
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    //  mRequest.setVisibility(View.VISIBLE);
                    //    if(mCancel.getVisibility() == View.VISIBLE){
                    //     mRequest.setText(R.string.cancelTrip);
                    //   }
                    //       else{
                    //   mRequest.setText(R.string.request_winsh_btn);
                    //     }
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
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

        // getSupportActionBar().setTitle("Wenshi Driver");


        swtch_onlineOffline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                displayLocation();
                reciveRequests(isChecked);
            }

        });
        //       Log.i("mBottomSheetBehavio", "" + mBottomSheetBehavior.getPeekHeight());

        getDriverProfile();
        getDriverHistory();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        // return true so that the menu pop up is opened
        menu.findItem(R.id.action_cancel).setVisible(false);
        return true;
    }

    public HistoryDetailsFragment getHistoryDetailsFragment() {
        return historyDetailsFragment;
    }

    public void setHistoryDetailsFragment(HistoryDetailsFragment historyDetailsFragment) {
        this.historyDetailsFragment = historyDetailsFragment;
    }


    private void getDriverProfile() {
        DatabaseReference getProfile = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userId).child("Profile");
        getProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("carType") != null && (dataSnapshot.child("carType").getValue() != null))
                    driverMod.setDriverCarType(dataSnapshot.child("carType").getValue().toString());
                if (dataSnapshot.child("plateNo") != null && (dataSnapshot.child("plateNo").getValue() != null))
                    driverMod.setDriverPlateNo(dataSnapshot.child("plateNo").getValue().toString());
                if ((driverMod.getMobile() == null || driverMod.getMobile().toString().isEmpty()) && dataSnapshot.child("mobile") != null && (dataSnapshot.child("mobile").getValue() != null))
                    driverMod.setMobile(dataSnapshot.child("mobile").getValue().toString());

                checkDriverProfile();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

    }


    private void reciveRequests(boolean state) {

        if (state) {
            driverViewStateControler(ONLINE);
            driverAvalbl = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(userId).child("Requests");
            driverAvalbl.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    // Log.i("CHILD TO ADDED", dataSnapshot.getKey());

                    if (requestsMap.size() == 0 && !requestsMap.containsKey(dataSnapshot.getKey()) && !dataSnapshot.getKey().equals("FirstConstant") && !dataSnapshot.getKey().equals("Accept")) {
                        //    Log.i("CHILD ADDED", dataSnapshot.getKey());
                        try {

                            JSONObject cust = new JSONObject(((String) dataSnapshot.child("Customer").getValue()));
                            String name = cust.getString("name");
                            String email = cust.getString("email");
                            String id = cust.getString("ID");
                            String mobile = cust.getString("mobile");
                            String service = cust.getString("Service");
                            String dropOFF = cust.getString("DropOFF");
                            String pickupAddress = cust.getString("PickupAddress");
                            String rating = cust.getString("rating");

                            //  String lat = cust.getString("Latitude");
                            //  String longt = cust.getString("Longitude");
                            // String address = cust.getString("address");
                            customerVehicle = new VehicleModel("", cust.getJSONObject("vehicle").getString("make"), cust.getJSONObject("vehicle").getString("model"), cust.getJSONObject("vehicle").getString("type").equals("true") ? true : false, cust.getJSONObject("vehicle").getString("color"), cust.getJSONObject("vehicle").getString("year"));


                            // Location locat = new Location("dummyprovider");
                            //locat.setLatitude(Double.parseDouble(lat));
                            //locat.setLongitude(Double.parseDouble(longt));
                            double ratingDouble = 0.0;
                            try {
                                ratingDouble = Double.parseDouble(rating);
                            } catch (Exception e) {
                                ratingDouble = 4.3;
                            }


                            cutomerMod = new UserModel(id, name, email, mobile, ratingDouble);
                            cutomerMod.setServiceType(service);
                            cutomerMod.setDestinationAddress(dropOFF);
                            cutomerMod.setPickupAddress(pickupAddress);

                            initDriverHistory(); //after Customer init
                            driverLocation.child(userId).removeValue();


                        } catch (Exception e) {
                            Log.i("ERROR", "Driver Map Activity" + e.toString());

                            cutomerMod = new UserModel("", "", "", "", 4.5);
                            customerVehicle = new VehicleModel("", "", "", true, "", "");
                        }
                        // Log.i("Location", geoDriverLocation.getDatabaseReference().toString());

                        getPickupLocation();
                        getDropOffLocation();


                        requestsMap.put(dataSnapshot.getKey(), dataSnapshot);


                        Calendar calendar1 = Calendar.getInstance();
                        SimpleDateFormat formatter1 = new SimpleDateFormat("dd/M/yyyy h:mm");
                        String currentDate = formatter1.format(calendar1.getTime());

                        Log.i("Time MODEL", currentDate);

                        currentHistory.setStartTime(currentDate);

                    } else if (!requestsMap.containsKey(dataSnapshot.getKey()) && !dataSnapshot.getKey().equals("FirstConstant")) {
                        //  requestsMap.put(dataSnapshot.getKey(), dataSnapshot);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {


                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    if (onRout && cutomerMod != null && !cutomerMod.getID().isEmpty() && requestsMap.containsKey(dataSnapshot.getKey()) && cutomerMod.getID().equals(dataSnapshot.getKey())) {
                        Log.i("REMOVE CURRENT CUSTOMER", cutomerMod.getID());
                        Toast.makeText(mContext, cutomerMod.getName() + " CANCELLED", Toast.LENGTH_SHORT).show();
                        addHistory.removeValue();
                        driverMod.getHistory().remove(currentHistory.getId());

                        currentHistory.setId("");
                        cutomerMod = null;
                        onRout = false;
                        requestsMap.remove(dataSnapshot.getKey());
                        driverViewStateControler(ONLINE);
                        displayLocation();

                    } else if (requestsMap.containsKey(dataSnapshot.getKey()) && requestsMap.size() > 1) {
                        requestsMap.remove(dataSnapshot.getKey());
                        DataSnapshot nextCustomer = (new ArrayList<DataSnapshot>(requestsMap.values())).get(0);
                        requestsMap.remove(nextCustomer.getKey());
                        if (CURRENTSTATE == ONLINE) onChildAdded(nextCustomer, "");
                    } else if (requestsMap.containsKey(dataSnapshot.getKey()) && requestsMap.size() == 1) {
                        cancel_btn(dataSnapshot.getKey());
                        requestsMap.remove(dataSnapshot.getKey());
                        // if (onRout && dataSnapshot.getKey().equals(cutomerMod.getID()))
                        //    Toast.makeText(mContext, cutomerMod.getName() + " CANCELLED", Toast.LENGTH_SHORT).show();
                    }
                    requestsMap.remove(dataSnapshot.getKey());

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            displayLocation();
        } else {
            if (driverAvalbl != null && geoFireDriverRequests != null)
                driverAvalbl.removeEventListener(geoFireDriverRequests);
            offline();

        }

    }

    private void getDropOffLocation() {
        DatabaseReference getPickup = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(userId).child("Requests").child(cutomerMod.getID()).child("DropOffLocation").child("l");
        getPickup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Object> map = (List<Object>) dataSnapshot.getValue();
                double locationLat = 0;
                double locationLng = 0;
                if (map != null && map.get(0) != null) {
                    locationLat = Double.parseDouble(map.get(0).toString());
                }

                if (map != null && map.get(1) != null) {
                    locationLng = Double.parseDouble(map.get(1).toString());
                }


                Location locat = new Location("dummyprovider");
                locat.setLatitude(locationLat);
                locat.setLongitude(locationLng);

                cutomerMod.setDestination(locat);
                Log.i("Destination", cutomerMod.getDestination().getLatitude() + "");

                currentHistory.setClientIntialDropOffAddress(locat.toString());

                if (CURRENTSTATE == TODISTINATION) showRout();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

    }

    private void getPickupLocation() {
        DatabaseReference getPickup = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(userId).child("Requests").child(cutomerMod.getID()).child("PickupLocation").child("l");
        getPickup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Object> map = (List<Object>) dataSnapshot.getValue();
                double locationLat = 0;
                double locationLng = 0;
                if (map != null && map.get(0) != null) {
                    locationLat = Double.parseDouble(map.get(0).toString());
                }
                if (map != null && map.get(1) != null) {
                    locationLng = Double.parseDouble(map.get(1).toString());
                }
                Marker mCustomerLocation = mMap.addMarker(new MarkerOptions().position(new LatLng(locationLat, locationLng)).title(cutomerMod.getName()));

                Location locat = new Location("dummyprovider");
                locat.setLatitude(locationLat);
                locat.setLongitude(locationLng);

                cutomerMod.setPickup(locat);
                markers.put(cutomerMod.getID(), mCustomerLocation);

                driverViewStateControler(NEWREQ);
                marksCameraUpdate();
                hidePopup();
                showPopup(getResources().getString(R.string.new_request), getResources().getString(R.string.textView_clientName) + " : " + cutomerMod.getName(), getResources().getString(R.string.textView_vehicle_make) + " : " + customerVehicle.getColor() + " - " + customerVehicle.getMake() + " - " + (customerVehicle.isType() ? getResources().getString(R.string.sedan) : getResources().getString(R.string.SUV)), getResources().getString(R.string.textView_vehicle_model) + " : " + customerVehicle.getYear() + ", " + customerVehicle.getModel(), getResources().getString(R.string.service) + " : " + cutomerMod.getServiceType());
                currentHistory.setClientIntialPickupAddress(locat.toString());
                if (CURRENTSTATE == ONROUT) showRout();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

    }

    private void marksCameraUpdate() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(myCurrent.getPosition());
        for (Marker marker : this.markers.values()) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 200; // offset from edges of the map in pixels
        if (onRout) padding = 100;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));
        mMap.animateCamera(cu);

    }

    private void setupLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQ_CODE);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQ_CODE);
        } else {
            if (checkPlayServices()) {
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
            if (CURRENTSTATE == ONROUT || CURRENTSTATE == TODISTINATION) showRout();

            if (myCurrent != null) myCurrent.remove();  //remove Old Marker
            LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            try {
                myCurrent = mMap.addMarker(new MarkerOptions().position(loc));
            } catch (Exception e) {

            }


            //update FireBase
            if (!onRout && swtch_onlineOffline != null && swtch_onlineOffline.isChecked()) {
                geoFireDriverLocation.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        //Add Marker
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15.0f));
                    }
                });

            } else if (onRout) {
                geoFireOnRoutLocation.setLocation("CurrentLocation", new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        marksCameraUpdate();
                    }
                });

            }

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

    private void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
            mGoogleApiClient.connect();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void initDriverHistory() {
        if (currentHistory == null || currentHistory.getId().isEmpty())
            currentHistory = new HistoryModel("", "", "", "", "", cutomerMod.getName(), cutomerMod.getID(), driverMod.getName(), driverMod.getID(), driverMod.getDriverCarType());
    }

    private void updateDriverState(final int state) {
        DatabaseReference driverState = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(userId).child("Requests").child(cutomerMod.getID()).child("DriverState");
        driverState.setValue(state+"").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("UPDATE DRIVER STATE", "STATE:" + state);
                } else {
                    Log.i("ERROR AT UPDATE DRIVER", "STATE:" + state);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
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
        if (driverMod != null) {
            driverMod.setCurrentLocation(location);
        }
        displayLocation();
        if (CURRENTSTATE == ONROUT && nearCustomer()) driverViewStateControler(NEARCUSTOMER);
        if (CURRENTSTATE == TODISTINATION && nearDestination())
            driverViewStateControler(ATDISTINATION);
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

    private void offline() {
        driverLocation.child(userId).removeValue();
        driverViewStateControler(OFFLINE);
        onRout = false;
    }

    private void clearMarkers() {
        try {
            if (this.markers != null) {
                for (Marker marker : this.markers.values()) {
                    marker.remove();
                }
                this.markers.clear();
            }
            if (this.mMap != null) {
                mMap.clear();
            }

        } catch (Exception e) {
            Log.i("Error", "DriverMap" + e.toString());
        }
    }

    /**
     * The driver is no longer available when he is not using the application
     */
    @Override
    protected void onStop() {
        super.onStop();
        hidePopup();
        //  offline();
        // hidePopup();
        //  if (geoQuery != null) this.geoQuery.removeAllListeners();
        // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this); //to remove the listener
        // FirebaseDatabase.getInstance().getReference("DriversAvailable").child(userId).removeValue();
    }

    @Override
    protected void onPause() {
        hidePopup();
        super.onPause();
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
//        Log.i("DRIVER CANCELLED", cutomerMod.getID());
        assignedCustomer = "";
        if (assignedCustomerPickupLocationRef != null) {
            assignedCustomerPickupLocationRef.removeEventListener(assignedCustomerPickupLocationRefEventListner);
        }
        mMap.clear();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.driver_drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (currentFragment != null) {

            driverViewStateControler(CURRENTSTATE);
            currentFragment = null;
        } else if (findViewById(R.id.mainFrame).getVisibility() == View.VISIBLE) {
            driverViewStateControler(CURRENTSTATE);

        } else {
            //   super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cancel) {
            cancel_btn(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        int id = item.getItemId();
        currentFragment = null;

        if (id == R.id.nav_profile) {
            viewProfilEdit();
            return true;
        } else if (id == R.id.nav_history) {
            currentFragment = new DriverHistoryFragment();
        } else if (id == R.id.nav_language) {


            final String[] languageList = {"Arabic", "English"};
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(DriverMapsActivity.this);
            mBuilder.setTitle("Choose Language ...");

            mBuilder.setSingleChoiceItems(languageList, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        setLanguage("ar");
                        recreate();
                    } else if (i == 1) {
                        setLanguage("en");
                        recreate();
                    }
                    dialogInterface.dismiss();
                }
            });
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        }
        if (currentFragment != null) {
            driverViewStateControler(SIDENAV);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, currentFragment);
            ft.commit();
        }

        //NOTE:  Closing the drawer after selecting
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.driver_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setLanguage(String lang) {

        setLocale(new Locale(lang));

    }

    private void setLocale(Locale locale) {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, displayMetrics);
        }

        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", locale.getLanguage());
        Log.i("LOCALEE", getResources().getString(R.string.vehicle));
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLanguage(language);
    }

    public void viewProfilEdit() {
        findViewById(R.id.mainFrame).setVisibility(View.VISIBLE);

        currentFragment = new DriverProfileFragment();
        if (currentFragment != null) {
            driverViewStateControler(SIDENAV);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, currentFragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.driver_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    public void viewRatingFragment() {
        findViewById(R.id.mainFrame).setVisibility(View.VISIBLE);
        currentFragment = new RateDriverFragment();
        if (currentFragment != null) {
//            driverViewStateControler(SIDENAV);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            ft.replace(R.id.mainFrame, currentFragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.driver_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // NOTE:  Code to replace the toolbar title based current visible fragment
        getSupportActionBar().setTitle(uri.getHost());
    }

    @Override
    public void onResume() {
        super.onResume();
        displayLocation();
    }

    private void showPopup(String title, String line1, String line2, String line3, String line4) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupLayout = inflater.inflate(R.layout.popup_layout, null);
        mPopupWindow = new PopupWindow(popupLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }

        if (!title.trim().isEmpty()) {
            ((TextView) popupLayout.findViewById(R.id.title_popup)).setVisibility(View.VISIBLE);
            ((TextView) popupLayout.findViewById(R.id.title_popup)).setText(title);
        } else
            ((TextView) popupLayout.findViewById(R.id.title_popup)).setVisibility(View.INVISIBLE);

        if (!line1.trim().isEmpty()) {
            ((TextView) popupLayout.findViewById(R.id.body_popup)).setVisibility(View.VISIBLE);
            ((TextView) popupLayout.findViewById(R.id.body_popup)).setText(line1);
        } else ((TextView) popupLayout.findViewById(R.id.body_popup)).setVisibility(View.INVISIBLE);

        if (!line2.trim().isEmpty()) {
            ((TextView) popupLayout.findViewById(R.id.body1_popup)).setVisibility(View.VISIBLE);
            ((TextView) popupLayout.findViewById(R.id.body1_popup)).setText(line2);
        } else
            ((TextView) popupLayout.findViewById(R.id.body1_popup)).setVisibility(View.INVISIBLE);

        if (!line3.trim().isEmpty()) {
            ((TextView) popupLayout.findViewById(R.id.body2_popup)).setVisibility(View.VISIBLE);
            ((TextView) popupLayout.findViewById(R.id.body2_popup)).setText(line3);
        } else
            ((TextView) popupLayout.findViewById(R.id.body2_popup)).setVisibility(View.INVISIBLE);

        if (!line4.trim().isEmpty()) {
            ((TextView) popupLayout.findViewById(R.id.body3_popup)).setVisibility(View.VISIBLE);
            ((TextView) popupLayout.findViewById(R.id.body3_popup)).setText(line4);
        } else
            ((TextView) popupLayout.findViewById(R.id.body3_popup)).setVisibility(View.INVISIBLE);
        mRelativeLayout.post(new Runnable() {
            public void run() {
                if (!mPopupWindow.isShowing())
                    mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER, 0, 0);
            }
        });
    }

    private void hidePopup() {

        if (mPopupWindow != null && mPopupWindow.isShowing()) mPopupWindow.dismiss();
    }

    public UserModel getDriver() {
        return driverMod;
    }

    private void acceptRequest() {

        if (cutomerMod != null) {
            driverViewStateControler(ONROUT);
            hidePopup();
            driverLocation.child(userId).removeValue();
            driverAvalbl = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(userId).child("Requests");
            driverAvalbl.child(cutomerMod.getID()).child("Accept").setValue(true);

            showRout();
            declineOtherRequests();
        }
    }

    private void declineOtherRequests() {
        if (this.requestsMap != null) {
            for (String customerID : this.requestsMap.keySet()) {
                if (cutomerMod != null && !cutomerMod.getID().isEmpty() && !cutomerMod.getID().contains(customerID)) {
                    FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(userId).child("Requests").child(customerID).removeValue();
                    requestsMap.remove(cutomerMod.getID());
                }
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                switch (CURRENTSTATE) {
                    case NEWREQ:
                        acceptRequest();
                        break;
                    case ARRIVED:
                        driverViewStateControler(TODISTINATION);
                        break;
                }
                break;
            case R.id.btn_2:
                switch (CURRENTSTATE) {
                    case NEARCUSTOMER:
                        driverViewStateControler(ARRIVED);
                        break;
                    case ARRIVED:
                        driverViewStateControler(TODISTINATION);
                        break;
                    case ATDISTINATION:
                        driverViewStateControler(ENDTRIP);
                        break;
                    case ENDTRIP:
                        driverViewStateControler(ONLINE);
                        break;
                }
                break;
            case R.id.nav_header_logout:
                Log.i("Driver Logout", driverMod.getID());
                FirebaseAuth.getInstance().signOut();
                Intent customerWelcome = new Intent(DriverMapsActivity.this, WelcomeActivity.class);
                startActivity(customerWelcome);
                break;
            case R.id.img_call_btn:
                if (cutomerMod != null && cutomerMod.getMobile() != null)
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", cutomerMod.getMobile(), null)));

                break;
            case R.id.img_map_btn:
                double destinationLatitude = 0;
                double destinationLongitude = 0;
                if (CURRENTSTATE == ONROUT && cutomerMod.getPickup() != null) {
                    destinationLatitude = cutomerMod.getPickup().getLatitude();
                    destinationLongitude = cutomerMod.getPickup().getLongitude();
                } else if (CURRENTSTATE == TODISTINATION && cutomerMod.getDestination() != null) {
                    destinationLatitude = cutomerMod.getDestination().getLatitude();
                    destinationLongitude = cutomerMod.getDestination().getLongitude();
                }
                if (destinationLatitude != 0) {
                    String url = "http://maps.google.com/maps?f=d&daddr=" + destinationLatitude + "," + destinationLongitude + "&dirflg=d&layer=t";
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
                break;
        }
    }

    private void showRout() {
        mMap.clear();
        clearMarkers();
        try {

            if (cutomerMod != null && !cutomerMod.getID().isEmpty() && cutomerMod.getPickup() != null && cutomerMod.getDestination() != null) {

                if (CURRENTSTATE == ONROUT) {
                    if (cutomerMod.getPickup().getLongitude() == 0) {
                        getPickupLocation();
                    }
                    mCustomerMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(cutomerMod.getPickup().getLatitude(), cutomerMod.getPickup().getLongitude())).title(cutomerMod.getName()));
                    markers.put(cutomerMod.getID(), mCustomerMarker);
                } else if (CURRENTSTATE == TODISTINATION) {
                    if (cutomerMod.getDestination().getLongitude() == 0) {
                        getDropOffLocation();
                    }
                    mCustomerMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(cutomerMod.getDestination().getLatitude(), cutomerMod.getDestination().getLongitude())).title(cutomerMod.getName()));
                    markers.put(cutomerMod.getID(), mCustomerMarker);
                }


            }
        } catch (Exception e) {

        }
        //   displayLocation();


        Object dataTransfer[] = new Object[2];
        dataTransfer = new Object[6];
        String url = getDirectionsUrl();
        getDirectionsData = new GetDirectionsData(this);
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        if (cutomerMod != null && !cutomerMod.getID().isEmpty() && cutomerMod.getPickup() != null && cutomerMod.getDestination() != null) {

            if (CURRENTSTATE == ONROUT) {
                dataTransfer[2] = new LatLng(cutomerMod.getPickup().getLatitude(), cutomerMod.getPickup().getLongitude());
            } else if (CURRENTSTATE == TODISTINATION) {
                dataTransfer[2] = new LatLng(cutomerMod.getDestination().getLatitude(), cutomerMod.getDestination().getLongitude());
            }

        }


        dataTransfer[3] = duration;
        dataTransfer[4] = distance;
        dataTransfer[5] = timeSec;

        getDirectionsData.execute(dataTransfer);


    }


    private String getDirectionsUrl() {

        // Log.i("Client Status","origin=" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude() + "&destination=" + cutomerMod.getDestination().getLatitude() + "," + cutomerMod.getDestination().getLongitude());
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin=" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());


        if (cutomerMod != null && !cutomerMod.getID().isEmpty() && cutomerMod.getPickup() != null && cutomerMod.getDestination() != null) {

            if (CURRENTSTATE == ONROUT) {
                googleDirectionsUrl.append("&destination=" + cutomerMod.getPickup().getLatitude() + "," + cutomerMod.getPickup().getLongitude());
            } else if (CURRENTSTATE == TODISTINATION) {
                googleDirectionsUrl.append("&destination=" + cutomerMod.getDestination().getLatitude() + "," + cutomerMod.getDestination().getLongitude());
            }

        }


        googleDirectionsUrl.append("&departure_time=now&key=" + getResources().getString(R.string.google_maps_key));

        return googleDirectionsUrl.toString();
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyBrVB9O0dT-F7P2NcAnHI-mjWW1sMCISns");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    //DRiver State Macines
    private void driverViewStateControler(int driverState) {
        switch (driverState) {
            case ONLINE:
                Log.i("STATE", "ONLINE");
                clearMarkers();
                hidePopup();
                requestsMap.clear();
                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                swtch_onlineOffline.setVisibility(View.VISIBLE);
                mBottomTextView.setText(getResources().getString(R.string.online));
                mBottomSheet.setVisibility(View.INVISIBLE);
                monlineOfflineLayout.setVisibility(View.VISIBLE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomButton2_btn.setVisibility(View.INVISIBLE);
                bottomButton1_btn.setVisibility(View.INVISIBLE);
                if (menu != null) menu.findItem(R.id.action_cancel).setVisible(false);
                swtch_onlineOffline.setVisibility(View.VISIBLE);
                call_btn.setVisibility(View.GONE);
                navigation_btn.setVisibility(View.GONE);
                CURRENTSTATE = driverState;
                break;
            case NEWREQ:
                Log.i("STATE", "NEWREQ");

                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                mBottomSheet.setVisibility(View.VISIBLE);
                monlineOfflineLayout.setVisibility(View.GONE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                // clearMarkers();
                bottomButton2_btn.setVisibility(View.INVISIBLE);
                bottomButton1_btn.setVisibility(View.VISIBLE);
                bottomButton1_btn.setText(getResources().getString(R.string.accept));
                CURRENTSTATE = driverState;
                if (menu != null) menu.findItem(R.id.action_cancel).setVisible(true);
                break;
            case ONROUT:
                Log.i("STATE", "ONROUT");
                Calendar calendar1 = Calendar.getInstance();
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd/M/yyyy h:mm");
                String currentDate = formatter1.format(calendar1.getTime());

                currentHistory.setStartTime(currentDate);
                saveHistory();

                onRout = true;
                hidePopup();
                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                mBottomSheet.setVisibility(View.INVISIBLE);
                monlineOfflineLayout.setVisibility(View.VISIBLE);
                swtch_onlineOffline.setVisibility(View.INVISIBLE);
                bottomButton1_btn.setVisibility(View.INVISIBLE);
                bottomButton2_btn.setVisibility(View.INVISIBLE);
                if (menu != null) menu.findItem(R.id.action_cancel).setVisible(true);
                CURRENTSTATE = driverState;
                swtch_onlineOffline.setVisibility(View.GONE);
                call_btn.setVisibility(View.VISIBLE);
                navigation_btn.setVisibility(View.VISIBLE);
                updateDriverState(ONROUT);
                break;
            case ARRIVE:
                Log.i("STATE", "ARR");
                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                CURRENTSTATE = driverState;
                bottomButton2_btn.setVisibility(View.INVISIBLE);
                bottomButton1_btn.setVisibility(View.VISIBLE);
                swtch_onlineOffline.setVisibility(View.GONE);
                call_btn.setVisibility(View.VISIBLE);
                navigation_btn.setVisibility(View.VISIBLE);
                break;
            case OFFLINE:
                Log.i("STATE", "OFF");
                hidePopup();
                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                mBottomSheet.setVisibility(View.INVISIBLE);
                monlineOfflineLayout.setVisibility(View.VISIBLE);
                swtch_onlineOffline.setVisibility(View.VISIBLE);
                mBottomTextView.setText(getResources().getString(R.string.offline));
                bottomButton1_btn.setVisibility(View.VISIBLE);
                bottomButton2_btn.setVisibility(View.INVISIBLE);
                clearMarkers();
                requestsMap.clear();
                if (menu != null) menu.findItem(R.id.action_cancel).setVisible(false);

                swtch_onlineOffline.setVisibility(View.VISIBLE);
                call_btn.setVisibility(View.GONE);
                navigation_btn.setVisibility(View.GONE);
                CURRENTSTATE = driverState;
                break;
            case SIDENAV:
                hidePopup();
                findViewById(R.id.mainFrame).setVisibility(View.VISIBLE);
                findViewById(R.id.mainFrame).bringToFront();
                findViewById(R.id.mainFrame).requestLayout();
                mBottomSheet.setVisibility(View.GONE);
                monlineOfflineLayout.setVisibility(View.GONE);
                break;

            case NEARCUSTOMER:
                Log.i("STATE", "NEAR");
                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                mBottomSheet.setVisibility(View.VISIBLE);
                monlineOfflineLayout.setVisibility(View.GONE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomButton1_btn.setVisibility(View.GONE);
                bottomButton2_btn.setVisibility(View.VISIBLE);
                bottomButton2_btn.setText(getResources().getString(R.string.arrived));

                CURRENTSTATE = driverState;
                updateDriverState(NEARCUSTOMER);

                break;
            case ARRIVED:
                Log.i("STATE", "ARRIVED");
                currentHistory.setClientActualPickupLocation(driverMod.getCurrentLocation());
                saveHistory();
                showPopup(getResources().getString(R.string.arrived), getResources().getString(R.string.textView_clientName) + " : " + cutomerMod.getName(), getResources().getString(R.string.textView_vehicle_make) + " : " + customerVehicle.getColor() + " - " + customerVehicle.getMake() + " - " + (customerVehicle.isType() ? getResources().getString(R.string.sedan) : getResources().getString(R.string.SUV)), getResources().getString(R.string.textView_vehicle_model) + " : " + customerVehicle.getYear() + ", " + customerVehicle.getModel(), getResources().getString(R.string.service) + " : " + cutomerMod.getServiceType());
                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                mBottomSheet.setVisibility(View.VISIBLE);
                monlineOfflineLayout.setVisibility(View.GONE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomButton1_btn.setVisibility(View.GONE);
                // bottomButton1_btn.setText(getResources().getString(R.string.takePhoto));
                bottomButton2_btn.setVisibility(View.VISIBLE);
                bottomButton2_btn.setText(getResources().getString(R.string.start));
                bottomButton2_btn.setEnabled(true);
                CURRENTSTATE = driverState;
                onRout = true;
                updateDriverState(ARRIVED);

                break;
            case TOOKPHOTOS:
                Log.i("STATE", "TOOKPHOTOS");
                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                mBottomSheet.setVisibility(View.VISIBLE);
                monlineOfflineLayout.setVisibility(View.GONE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomButton1_btn.setVisibility(View.VISIBLE);
                bottomButton2_btn.setText(getResources().getString(R.string.takePhoto));
                bottomButton2_btn.setVisibility(View.VISIBLE);
                bottomButton2_btn.setText(getResources().getString(R.string.start));
                bottomButton2_btn.setEnabled(true);
                CURRENTSTATE = driverState;
                // onRout = false;

                break;
            case TODISTINATION:
                Log.i("STATE", "TODISTINATION");
                onRout = true;
                hidePopup();
                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                mBottomSheet.setVisibility(View.INVISIBLE);
                monlineOfflineLayout.setVisibility(View.VISIBLE);
                swtch_onlineOffline.setVisibility(View.INVISIBLE);
                bottomButton1_btn.setVisibility(View.INVISIBLE);
                bottomButton2_btn.setVisibility(View.INVISIBLE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (menu != null) menu.findItem(R.id.action_cancel).setVisible(true);
                CURRENTSTATE = driverState;
                swtch_onlineOffline.setVisibility(View.GONE);
                call_btn.setVisibility(View.VISIBLE);
                navigation_btn.setVisibility(View.VISIBLE);
                showRout();
                updateDriverState(TODISTINATION);

                break;

            case ATDISTINATION:
                Log.i("STATE", "ATDIS");
                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                mBottomSheet.setVisibility(View.VISIBLE);
                monlineOfflineLayout.setVisibility(View.GONE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomButton1_btn.setVisibility(View.GONE);
                bottomButton2_btn.setVisibility(View.VISIBLE);
                bottomButton2_btn.setText(getResources().getString(R.string.arrived));
                CURRENTSTATE = driverState;
                updateDriverState(ATDISTINATION);

                break;
            case ENDTRIP:
                Log.i("STATE", "END");
                Log.i("Old_cost", currentHistory.getCost());
                if (menu != null) menu.findItem(R.id.action_cancel).setVisible(false);

                Calendar calendar2 = Calendar.getInstance();
                SimpleDateFormat formatter2 = new SimpleDateFormat("dd/M/yyyy h:mm");
                String currentDate2 = formatter2.format(calendar2.getTime());
                currentHistory.setEndTime(currentDate2);
                currentHistory.setClientActualDropOffLocation(driverMod.getCurrentLocation());

                currentHistory.setDistance(getActualDistanceBetweenPickupAndDropOff() + "");
                currentHistory.setTimeSec(getActualTimeBetweenPickupAndDropOff());
                currentHistory.setEta(getActualTimeBetweenPickupAndDropOff() + " Sec");
                currentHistory.setCompeleted(true);

                currentHistory.calculateCost(customerVehicle.isType());
                saveHistory();

                CURRENTSTATE = ENDTRIP;

                viewRatingFragment();
//                findViewById(R.id.mainFrame).setVisibility(View.INVISIBLE);
                mBottomSheet.setVisibility(View.VISIBLE);
                monlineOfflineLayout.setVisibility(View.GONE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomButton1_btn.setVisibility(View.GONE);
                // bottomButton1_btn.setText(getResources().getString(R.string.takePhoto));
                bottomButton2_btn.setVisibility(View.VISIBLE);
                bottomButton2_btn.setText(getResources().getString(R.string.fui_done));
                bottomButton2_btn.setEnabled(true);

                requestsMap.clear();
                updateDriverState(ENDTRIP);

                break;
        }


    }

    @Override
    public void gotDurationDistanceRout(String output) {
        mBottomTextView.setText(getResources().getString(R.string.eta) + " " + getDirectionsData.getDuration());
        //   driverViewStateControler(ONROUT); //must be after showRout to get the correct duration

        if (currentHistory != null) {
            currentHistory.setDistance(getDirectionsData.getDistance());
            currentHistory.setTimeSec(getDirectionsData.getTimeSec());
            currentHistory.calculateCost(customerVehicle.isType());
            Toast.makeText(this, currentHistory.getCost() + " L.E.", Toast.LENGTH_SHORT);

            // saveHistory();
        }
    }

    private boolean nearCustomer() {
        Log.i("NEAR CUSTOMER", "" + (cutomerMod != null) + (driverMod != null));
        if (cutomerMod != null && driverMod != null) {

            Location driversAvlbl = new Location("");
            driversAvlbl.setLatitude(driverMod.getCurrentLocation().getLatitude());
            driversAvlbl.setLongitude(driverMod.getCurrentLocation().getLongitude());

            Location pickupLoc = new Location("");
            pickupLoc.setLatitude(cutomerMod.getPickup().getLatitude());
            pickupLoc.setLongitude(cutomerMod.getPickup().getLongitude());
            Log.i("Distancee", "" + driversAvlbl.distanceTo(pickupLoc));
            if (driversAvlbl.distanceTo(pickupLoc) < 300) {
                return true;
            } else return false;
        } else return false;
    }

    private boolean nearDestination() {
        Log.i("NEAR CUSTOMER", "" + (cutomerMod != null) + (driverMod != null));
        if (cutomerMod != null && driverMod != null) {

            Location driversAvlbl = new Location("");
            driversAvlbl.setLatitude(driverMod.getCurrentLocation().getLatitude());
            driversAvlbl.setLongitude(driverMod.getCurrentLocation().getLongitude());

            Location pickupLoc = new Location("");
            pickupLoc.setLatitude(cutomerMod.getDestination().getLatitude());
            pickupLoc.setLongitude(cutomerMod.getDestination().getLongitude());
            Log.i("Distancee", "" + driversAvlbl.distanceTo(pickupLoc));
            if (driversAvlbl.distanceTo(pickupLoc) < 300) {
                return true;
            } else return false;
        } else return false;
    }

    private float getActualDistanceBetweenPickupAndDropOff() {
        float distance = 40;
        try {
            if (currentHistory != null && currentHistory.getClientActualDropOffLocation() != null && currentHistory.getClientActualPickupLocation() != null) {

                Location pickup = new Location("");
                pickup.setLatitude(currentHistory.getClientActualPickupLocation().getLatitude());
                pickup.setLongitude(currentHistory.getClientActualPickupLocation().getLongitude());

                Location dropoff = new Location("");
                dropoff.setLatitude(currentHistory.getClientActualDropOffLocation().getLatitude());
                dropoff.setLongitude(currentHistory.getClientActualDropOffLocation().getLongitude());

                distance = pickup.distanceTo(dropoff) * 2;

            }
        } catch (Exception e) {
            Log.i("Error", "getActualDistanceBetweenPickupAndDropOff" + e.toString());
        }

        return distance;
    }

    private long getActualTimeBetweenPickupAndDropOff() {
        long diffInSec = 60;
        try {
            Calendar calendar1 = Calendar.getInstance();
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/M/yyyy h:mm");
            String currentDate = formatter1.format(calendar1.getTime());
            Date date2 = formatter1.parse(currentHistory.getStartTime());
            Date date1 = formatter1.parse(currentHistory.getEndTime());
            long mills = date1.getTime() - date2.getTime();
            diffInSec = TimeUnit.MILLISECONDS.toSeconds(mills);


        } catch (Exception e) {
            Log.i("Error", "getActualTimeBetweenPickupAndDropOff" + e.toString());
        }

        return diffInSec;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == TOOKPHOTOS) {
            if (resultCode == Activity.RESULT_OK) {
//                String result =data.getStringExtra("result");
                driverViewStateControler(TOOKPHOTOS);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    private boolean checkDriverProfile() {
        boolean state = true;
        if (driverMod != null) {
            if (driverMod.getName() == null || driverMod.getName().isEmpty()) {
                state = false;
            }
            if (driverMod.getEmail() == null || driverMod.getEmail().isEmpty()) {
                state = false;
            }
            if (driverMod.getMobile() == null || driverMod.getMobile().isEmpty()) {
                state = false;
            }
            if (driverMod.getDriverCarType() == null || driverMod.getDriverCarType().isEmpty()) {
                state = false;
            }
            if (driverMod.getDriverPlateNo() == null || driverMod.getDriverPlateNo().isEmpty()) {
                state = false;
            }
        }
        if (!state) {
            Toast.makeText(mContext, "Please Compelete your profile", Toast.LENGTH_SHORT).show();
            viewProfilEdit();
        }
        return state;
    }

    private void cancel_btn(@Nullable String key) {
        driverViewStateControler(ONLINE);

        try {
            Calendar calendar1 = Calendar.getInstance();
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/M/yyyy h:mm");
            String currentDate = formatter1.format(calendar1.getTime());
            Date date2 = formatter1.getCalendar().getTime();
            Date date1 = formatter1.parse(currentHistory.getStartTime());
            long mills = date2.getTime() - date1.getTime();
            int mins = (int) ((mills / (1000 * 60)) % 60);

            if (mins <= 3) {
                addHistory.removeValue();
                driverMod.getHistory().remove(currentHistory.getId());

            } else {
                currentHistory.setCost(-150.0);
                currentHistory.setCompeleted(false);
                currentHistory.setEndTime(currentDate);
                saveHistory();
            }
            currentHistory.setId("");
        } catch (Exception e) {
            Log.i("Error", "CustomerMap" + e.toString());
        }

        hidePopup();
        displayLocation();
        if (cutomerMod != null && driverAvalbl != null)
            driverAvalbl.child(cutomerMod.getID()).removeValue();
        if (cutomerMod != null) requestsMap.remove(cutomerMod.getID());
        requestCancelled();
        reciveRequests(false);
        reciveRequests(true);
        driverViewStateControler(ONLINE);
        if (key != null && cutomerMod.getID().equals(key)) cutomerMod = null;


    }


    //History MODEL - CONTROLLER
    private void getDriverHistory() {
        DatabaseReference getTrips = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userId).child("Trips");
        getTrips.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int trips = 0;
                for (DataSnapshot historySnapshot : dataSnapshot.getChildren()) {

                    if (historySnapshot.getKey().equals("FirstConstant")) continue;

                    trips++;

                    HistoryModel historyModel = new HistoryModel(historySnapshot.getKey().toString(), historySnapshot.child("timeStamp").getValue() != null ? historySnapshot.child("timeStamp").getValue().toString() : "_date", historySnapshot.child("startTime").getValue() != null ? historySnapshot.child("startTime").getValue().toString() : "_startTime", historySnapshot.child("eta").getValue() != null ? historySnapshot.child("eta").getValue().toString() : "_ETA", historySnapshot.child("distance").getValue() != null ? historySnapshot.child("distance").getValue().toString() : "_distance", historySnapshot.child("clientName").getValue() != null ? historySnapshot.child("clientName").getValue().toString() : "_clientName", historySnapshot.child("cleintID").getValue() != null ? historySnapshot.child("cleintID").getValue().toString() : "_cleintID", historySnapshot.child("driverName").getValue() != null ? historySnapshot.child("driverName").getValue().toString() : "_driverName", historySnapshot.child("driverID").getValue() != null ? historySnapshot.child("driverID").getValue().toString() : "_driverID", historySnapshot.child("vehicleDetails").getValue() != null ? historySnapshot.child("vehicleDetails").getValue().toString() : "_vehicleDetails");

                    historyModel.setCost(Double.parseDouble(historySnapshot.child("cost").getValue() != null ? historySnapshot.child("cost").getValue().toString() : "404"));
                    historyModel.setTimeSec(Double.parseDouble(historySnapshot.child("timeSec").getValue() != null ? historySnapshot.child("timeSec").getValue().toString() : "404"));
                    historyModel.setCompeleted((historySnapshot.child("compeleted").getValue() != null ? (historySnapshot.child("compeleted").getValue().toString()).equals("true") ? true : false : false));


                    historyModel.setDriverStartAddress(historySnapshot.child("driverStartAddress").getValue() != null ? historySnapshot.child("driverStartAddress").getValue().toString() : "404");
                    historyModel.setClientIntialDropOffAddress(historySnapshot.child("clientIntialDropOffAddress").getValue() != null ? historySnapshot.child("clientIntialDropOffAddress").getValue().toString() : "404");
                    historyModel.setClientActualDroOffAddress(historySnapshot.child("clientActualDroOffAddress").getValue() != null ? historySnapshot.child("clientActualDroOffAddress").getValue().toString() : "404");
                    historyModel.setClientIntialPickupAddress(historySnapshot.child("clientIntialPickupAddress").getValue() != null ? historySnapshot.child("clientIntialPickupAddress").getValue().toString() : "404");
                    historyModel.setClientActualPickupAddress(historySnapshot.child("clientActualPickupAddress").getValue() != null ? historySnapshot.child("clientActualPickupAddress").getValue().toString() : "404");

                    //      historyModel.setDriverStartLocation(historySnapshot.child("driverStartLocation").getValue() != null ? Location(historySnapshot.child("driverStartLocation").getValue()) : "404");

                    //      historyModel.setClientIntialDropOffAddress(historySnapshot.child("clientIntialDropOffAddress").getValue() != null ? historySnapshot.child("clientIntialDropOffAddress").getValue().toString() : "404");

                    //     historyModel.setClientIntialDropOffAddress(historySnapshot.child("clientIntialDropOffAddress").getValue() != null ? historySnapshot.child("clientIntialDropOffAddress").getValue().toString() : "404");

                    //      historyModel.setClientIntialDropOffAddress(historySnapshot.child("clientIntialDropOffAddress").getValue() != null ? historySnapshot.child("clientIntialDropOffAddress").getValue().toString() : "404");

                    //    historyModel.setClientIntialDropOffAddress(historySnapshot.child("clientIntialDropOffAddress").getValue() != null ? historySnapshot.child("clientIntialDropOffAddress").getValue().toString() : "404");


                    driverMod.addHistory(historySnapshot.getKey().toString(), historyModel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });


    }

    private void saveHistory() {


        if (currentHistory != null) {
            if (currentHistory.getId() == null || currentHistory.getId().isEmpty()) {
                Calendar calendar1 = Calendar.getInstance();
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MMM-dd-HH-mm-ss");
                String currentDate = formatter1.format(calendar1.getTime());

                currentHistory.setId(currentDate);
                Log.i("HISTORY ID", currentDate);
            }
            String uid = userId;

            addHistory = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uid).child("Trips").child(currentHistory.getId());
            addHistory.child("timeStamp").setValue(ServerValue.TIMESTAMP);
            addHistory.child("startTime").setValue(currentHistory.getStartTime());
            addHistory.child("endTime").setValue(currentHistory.getEndTime());
            addHistory.child("eta").setValue(currentHistory.getEta());
            addHistory.child("distance").setValue(currentHistory.getDistance());
            addHistory.child("clientName").setValue(currentHistory.getClientName());
            addHistory.child("clientID").setValue(currentHistory.getCleintID());
            addHistory.child("driverName").setValue(currentHistory.getDriverName());
            addHistory.child("driverID").setValue(currentHistory.getDriverID());
            addHistory.child("vehicleDetails").setValue(currentHistory.getVehicleDetails());
            addHistory.child("cost").setValue(currentHistory.getCost().toString());
            addHistory.child("timeSec").setValue("" + currentHistory.getTimeSec());
            addHistory.child("compeleted").setValue(currentHistory.isCompeleted() + "");

            addHistory.child("cost").setValue(currentHistory.getCost());
            addHistory.child("compeleted").setValue(currentHistory.isCompeleted());


            addHistory.child("driverStartAddress").setValue(currentHistory.getDriverStartAddress());
            addHistory.child("clientIntialDropOffAddress").setValue(currentHistory.getClientIntialDropOffAddress());
            addHistory.child("clientActualDroOffAddress").setValue(currentHistory.getClientActualDroOffAddress());
            addHistory.child("clientIntialPickupAddress").setValue(currentHistory.getClientIntialPickupAddress());
            addHistory.child("clientActualPickupAddress").setValue(currentHistory.getClientActualPickupAddress());
            addHistory.child("driverStartLocation").setValue(currentHistory.getDriverStartLocation());
            addHistory.child("clientActualDropOffLocation").setValue(currentHistory.getClientActualDropOffLocation());
            addHistory.child("clientIntialDropOffLocation").setValue(currentHistory.getClientIntialDropOffLocation());
            addHistory.child("clientActualPickupLocation").setValue(currentHistory.getClientActualPickupLocation());
            addHistory.child("clientIntialPickupLocation").setValue(currentHistory.getClientIntialPickupLocation());

            driverMod.addHistory(currentHistory.getId().toString(), currentHistory);

        }
    }
}
