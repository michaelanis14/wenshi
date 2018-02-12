package com.wenshi_egypt.wenshi;


        import android.content.Intent;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.content.pm.Signature;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Base64;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Switch;

        import com.facebook.login.Login;
        import com.google.firebase.analytics.FirebaseAnalytics;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.Query;
        import com.google.firebase.database.ValueEventListener;

        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;


public class WelcomeActivity extends AppCompatActivity {

    Switch swtch_RiderOrDriver;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.packagename",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        swtch_RiderOrDriver = (Switch) findViewById(R.id.riderOrDriver_swtch);

    //getSupportActionBar().hide();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        if (auth.getCurrentUser() != null) {

            String currentDriverId = FirebaseAuth.getInstance().getCurrentUser().getUid();



/*
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            DatabaseReference ref = database.child("Users").child("Drivers");

            Query driversQuery = ref.orderByChild("Drivers").equalTo(currentDriverId);
            driversQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        user = singleSnapshot.getValue(User.class);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i("onError","");
                }
            });



*/



            Log.i("UID",currentDriverId);


            DatabaseReference isDriverRef = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(currentDriverId);

            Log.i("DRRR",isDriverRef.toString());
            isDriverRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.i("isDriver","");
                        startActivity(new Intent(WelcomeActivity.this, DriverMapsActivity.class));
                    } else  {
                      //  startActivity(new Intent(WelcomeActivity.this, DriverMapsActivity.class));

                        Log.i("isCustomer","");
                        startActivity(new Intent(WelcomeActivity.this, CustomerMapActivity.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i("onError","");
                }
            });

        }

    }



    public void getStarted(View view){
        boolean customer = true;

     if(swtch_RiderOrDriver.isChecked()){
         customer = false;

     }
        mFirebaseAnalytics.setUserProperty("RiderOrDriver", ""+customer);

        Intent loginInent = new Intent(WelcomeActivity.this, LoginActivity.class);
        loginInent.putExtra("customer", customer);
        startActivity(loginInent);
        finish();
    }
}
