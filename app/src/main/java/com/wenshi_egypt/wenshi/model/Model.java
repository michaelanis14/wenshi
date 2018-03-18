package com.wenshi_egypt.wenshi.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by michaelanis on 3/18/18.
 */

public class Model {
    public Model() {
    }

    DatabaseReference rootRef, profRef, vehicleRef, historicRef;


    void setDBReferences(boolean userOrDriver,String ID) {
        rootRef = FirebaseDatabase.getInstance().getReference();
        if (userOrDriver) {
            profRef = rootRef.child("Users").child("Customers").child(ID).child("Profile");
            vehicleRef = rootRef.child("Users").child("Customers").child(ID).child("Vehicles");
            historicRef = rootRef.child("Users").child("Customers").child(ID).child("Trips");
        } else {
            profRef = rootRef.child("Users").child("Drivers").child(ID).child("Profile");
            //profRef = rootRef.child("Users").child("Drivers").child(ID).child("Trips");
        }
    }



    ArrayList<VehicleModel> getVehiclesData(String id, boolean userOrDriver) {
        //setDBReferences(userOrDriver);
        final ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();

        vehicleRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                @SuppressWarnings("unchecked") HashMap<VehicleModel, VehicleModel> value = (HashMap<VehicleModel, VehicleModel>) dataSnapshot.getValue();
                result.add(value.get("type"));
                result.add(value.get("model"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return result;
    }

    ArrayList<TripModel> getTripsData(String id, boolean userOrDriver) {
        //setDBReferences(userOrDriver);
        final ArrayList<TripModel> result = new ArrayList<TripModel>();

        historicRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                @SuppressWarnings("unchecked") HashMap<TripModel, TripModel> value = (HashMap<TripModel, TripModel>) dataSnapshot.getValue();
                result.add(value.get("date"));
                result.add(value.get("from"));
                result.add(value.get("to"));
                result.add(value.get("cost"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return result;
    }


    public void linkProfData(boolean userOrDriver, final UserModel user) {
        setDBReferences( userOrDriver,user.getID());
        profRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot imageSnapshot : dataSnapshot.getChildren()) {
                    if(imageSnapshot.getKey().equals("mobile")){
                        user.setMobile( imageSnapshot.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
