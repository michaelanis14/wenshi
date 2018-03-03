package com.wenshi_egypt.wenshi.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by michaelanis on 3/2/18.
 */

public class UserModel implements Parcelable {
    String ID;
    String Name;
    String email;
    String Mobile;
    String CarType;
    String Model;
    String Address;
    ArrayList<VehicleModel> Vehicles;


    public UserModel(String id,String name, String email, String mobile) {
        ID = id;
        Name = name;
        this.email = email;
        Mobile = mobile;

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

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private MyParcelable(Parcel in) {
        mData = in.readInt();
    }
}
