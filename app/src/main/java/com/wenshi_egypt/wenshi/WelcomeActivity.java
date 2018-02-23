package com.wenshi_egypt.wenshi;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity {

    Switch swtch_RiderOrDriver;
    View lbl_Rider;
    View lbl_Driver;
    View btn_get_started;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        swtch_RiderOrDriver = (Switch) findViewById(R.id.riderOrDriver_swtch);
        lbl_Driver = findViewById(R.id.DriverText);
        lbl_Rider = findViewById(R.id.RiderText);
        btn_get_started = findViewById(R.id.get_started_btn);


    //getSupportActionBar().hide();
        FirebaseAuth auth = FirebaseAuth.getInstance();
       // mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        if (auth.getCurrentUser() != null) {

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Log.i("UID",uid);


            DatabaseReference isDriverRef = FirebaseDatabase.getInstance().getReference("Users").child("Drivers").child(uid);

            Log.i("DRRR",isDriverRef.toString());
            isDriverRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.i("isDriver","");
                        startActivity(new Intent(WelcomeActivity.this, DriverMapsActivity.class));
                        finish();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i("onError",databaseError.toString());
                }
            });



            DatabaseReference isUserRef = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(uid);

            Log.i("DRRR",isUserRef.toString());
            isUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        startActivity(new Intent(WelcomeActivity.this, CustomerMapActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i("onError",databaseError.toString());
                }
            });

        }





    }

@Override
protected void onStart(){
    super.onStart();
    swtch_RiderOrDriver.setVisibility(View.VISIBLE);
    lbl_Rider.setVisibility(View.VISIBLE);
    lbl_Driver.setVisibility(View.VISIBLE);
    btn_get_started.setVisibility(View.VISIBLE);


}
    public void getStarted(View view) {
        boolean customer = true;

        if (swtch_RiderOrDriver.isChecked()) {
            customer = false;
        }

        Intent loginInent = new Intent(WelcomeActivity.this, LoginActivity.class);
        loginInent.putExtra("customer", customer);
        startActivity(loginInent);
        finish();
    }
}
