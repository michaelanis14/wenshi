package com.wenshi_egypt.wenshi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddNewVehicle extends AppCompatActivity {

    double vicDouble = Math.random()*1000;
    int vicCount = (int) vicDouble;
    EditText newType, newModel;
    TextView savedSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_add_new_vehicle);

        newType = findViewById(R.id.editText_newType);
        newModel = findViewById(R.id.editText_newModel);
        savedSuccess = findViewById(R.id.textView_vehicle_saved_label);

        findViewById(R.id.button_new_vehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

  //              long numVic = getVehicleCount();
  //              Toast.makeText(AddNewVehicle.this, "Vehicles count " + numVic, Toast.LENGTH_SHORT).show();
                if(TextUtils.isEmpty(newType.getText())||TextUtils.isEmpty(newModel.getText()))
                    Toast.makeText(AddNewVehicle.this, "Cannot submit empty field",  Toast.LENGTH_LONG).show();
                else {

                    Bundle b = getIntent().getExtras();
                    String value = "";
                    assert b != null;
                    if(!b.isEmpty()) {
                        value = b.getString("uid");
                        DatabaseReference addVehicle = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(value).child("Vehicles").child("newVic" + vicCount);
                        addVehicle.child("type").setValue(newType.getText().toString());
                        addVehicle.child("model").setValue(newModel.getText().toString());
                        addVehicle = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(value).child("Profile");
                        addVehicle.child("carType").setValue(newType.getText().toString());
                        addVehicle.child("model").setValue(newModel.getText().toString());
                        vicCount++;

                        new CountDownTimer(3000, 10) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                savedSuccess.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFinish() {
                               // startActivity(new Intent(AddNewVehicle.this, WelcomeActivity.class));
                            }
                        }.start();
                    }

                }

            }
        });
    }

    private void dialContactPhone(String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

/*
    private long getVehicleCount()  {
        final long[] counter = {0};
        final ArrayList<Object> results = new ArrayList<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference vehicleRef = rootRef.child("Users").child("Customers").child("user1").child("Vehicles");
        vehicleRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    counter[0]++;
                    i++;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //   Toast.makeText(HistoricFragment.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        return counter.length;
    }
*/
}
