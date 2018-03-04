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
    String Address = "";
    ArrayList<VehicleModel> Vehicles = new ArrayList<VehicleModel>();


    public UserModel(String id,String name, String email, String mobile, String Address) {
        this.ID = id;
        this.Name = name;
        this.email = email;
        this.Mobile = mobile;
        this.Address = Address;

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
        parcel.writeTypedList(Vehicles);


    }
    @Override
    public String toString() {
        return "UserModel{" +
                "ID='" + ID + '\'' +
                ", Name='" + Name + '\'' +
                ", email='" + email + '\'' +
                ", Mobile='" + Mobile + '\'' +
                ", Address='" + Address + '\'' +
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
        in.readTypedList(Vehicles, VehicleModel.CREATOR);
    }
}
