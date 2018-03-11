package com.wenshi_egypt.wenshi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by michaelanis on 3/2/18.
 */

public class UserModel implements Parcelable {
    String ID ="ID";
    String Name = "FirstLastName";
    String email= "Email@email.com";
    String Mobile="00000";
    String Address = "Addres";
    VehicleModel defaultVehicle;
    double latitude;
    double longitude;
    DatabaseReference rootRef, profRef, vehicleRef, historicRef;


    public VehicleModel getDefaultVehicle() {
        return defaultVehicle;
    }

    ArrayList<VehicleModel> Vehicles = new ArrayList<VehicleModel>();


    public String getID() {
        if(ID != null)
        return ID;
        else return "";
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getAddress() {
        return Address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public UserModel(String id,String name, String email, String mobile, String Address) {
        this.ID = id;
        this.Name = name;
        this.email = email;
        this.Mobile = mobile;
        this.Address = Address;
        this.defaultVehicle = new VehicleModel("KIA","RIO 2014");
    }
    public UserModel(String id,String name, String email, String mobile,double latitude, double longitude, String Address,VehicleModel defaultVehicle) {
        this.ID = id;
        this.Name = name;
        this.email = email;
        this.Mobile = mobile;
        this.Address = Address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.defaultVehicle = defaultVehicle;
    }

    public ArrayList<VehicleModel> getVehicles() {
        return Vehicles;
    }

    public void setVehicle (VehicleModel vehicle) {
        Vehicles.add(vehicle);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ID);
        parcel.writeString(Name);
        parcel.writeString(email);
        parcel.writeString(Mobile);
        parcel.writeString(Address);
        parcel.writeParcelable(defaultVehicle,i);
        parcel.writeTypedList(Vehicles);


    }
    @Override
    public String toString() {
        return "{" +
                "\"ID\":\"" + ID + "\"" +
                ", \"Name\":\"" + Name + "\"" +
                ", \"email\":\"" + email + "\"" +
                ", \"Mobile\":\"" + Mobile + "\"" +
                ", \"Address\":\"" + Address + "\"" +
                ", \"Latitude\":\"" + latitude + "\"" +
                ", \"Longitude\":\"" + longitude + "\"" +
                ", \"Vehicle\":" + defaultVehicle.toString() + "" +
                '}';
    }
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {

        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private UserModel(Parcel in) {
        ID = in.readString();
        Name = in.readString();
        email = in.readString();
        Mobile = in.readString();
        Address = in.readString();
        defaultVehicle = in.readParcelable(VehicleModel.class.getClassLoader());
        in.readTypedList(Vehicles, VehicleModel.CREATOR);
    }

    void setDBReferences(String id, boolean userOrDriver)    {
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        if(userOrDriver) {
            this.profRef = rootRef.child("Users").child("Customers").child(id).child("Profile");
            this.vehicleRef = rootRef.child("Users").child("Customers").child(id).child("Vehicles");
            this.historicRef = rootRef.child("Users").child("Customers").child(id).child("Trips");
        }
        else    {
            this.profRef = rootRef.child("Users").child("Drivers").child(id).child("Profile");
            this.profRef = rootRef.child("Users").child("Drivers").child(id).child("Trips");
        }
    }
    public ArrayList<String> getProfData(String id, boolean userOrDriver) {
        setDBReferences(id, userOrDriver);
        final ArrayList<String> result = new ArrayList<String>();

        profRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                @SuppressWarnings("unchecked") HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                result.add(value.get("mobile"));
                result.add(value.get("address"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return result;
    }

    ArrayList<VehicleModel> getVehiclesData(String id, boolean userOrDriver) {
        setDBReferences(id, userOrDriver);
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
        setDBReferences(id, userOrDriver);
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

}
