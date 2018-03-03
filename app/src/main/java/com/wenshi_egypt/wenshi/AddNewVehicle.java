package com.wenshi_egypt.wenshi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Michael on 3/1/2018.
 */

public class AddNewVehicle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_add_new_vehicle);

        findViewById(R.id.button_new_vehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference addVehicle = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child("user1");
             //   addVehicle.setValue("Testing");
            //    dialContactPhone("123123123");
            }
        });
    }

    private void dialContactPhone(String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
}
