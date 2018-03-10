package com.wenshi_egypt.wenshi.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

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
}
