package com.wenshi_egypt.wenshi;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class WelcomeActivity extends AppCompatActivity {

    Switch swtch_RiderOrDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        swtch_RiderOrDriver = (Switch) findViewById(R.id.riderOrDriver_swtch);

/*
    //getSupportActionBar().hide();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        if (auth.getCurrentUser() != null) {

            String currentDriverId = FirebaseAuth.getInstance().getCurrentUser().getUid();



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
*/
    }


    public void getStarted(View view) {
        boolean customer = true;

        if (swtch_RiderOrDriver.isChecked()) {
            customer = false;
            startActivity(new Intent(WelcomeActivity.this, DriverMapsActivity.class));

        } else {
            startActivity(new Intent(WelcomeActivity.this, CustomerMapActivity.class));
        }

        finish();
        return;

/*
   mFirebaseAnalytics.setUserProperty("RiderOrDriver", ""+customer);

        Intent loginInent = new Intent(WelcomeActivity.this, LoginActivity.class);
        loginInent.putExtra("customer", customer);
        startActivity(loginInent);
        finish();
        */
    }
}
