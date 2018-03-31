package com.wenshi_egypt.wenshi.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import static com.wenshi_egypt.wenshi.helpers.AppUtils.Defs.CAIRO;

/**
 * Created by michaelanis on 3/2/18.
 */

public class UserModel implements Parcelable {
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods

    public void setID(String ID) {
        this.ID = ID;
    }

    String ID = "ID";

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String name = "";
    String email = "";

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    String mobile = "";

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    String ServiceType;
    Location pickup;
    String pickupAddress;
    Location destination;
    String destinationAddress;
    Location currentLocation;


    ArrayList<VehicleModel> Vehicles = new ArrayList<VehicleModel>();

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {

        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public UserModel(String id, String name, String email, String mobile) {
        this.ID = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        Location locationCairo = new Location("");
        locationCairo.setLatitude(CAIRO.latitude);
        locationCairo.setLongitude(CAIRO.longitude);

        pickup = locationCairo;
        destination = locationCairo;
    }
    public UserModel(String id, String name, String email, String mobile, Location pickup, Location destination) {
        this.ID = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.pickup = pickup;
        this.destination = destination;
    }
    // example constructor that takes a Parcel and gives you an object populated with it's values
    private UserModel(Parcel in) {
        ID = in.readString();
        name = in.readString();
        email = in.readString();
        mobile = in.readString();
       // pickup = (Location) in.readValue(Location.class.getClassLoader());
        pickupAddress = in.readString();
       // destination = (Location) in.readValue(Location.class.getClassLoader());
        destinationAddress = in.readString();
        //currentLocation = (Location) in.readValue(Location.class.getClassLoader());

     //   destination =Location.CREATOR.createFromParcel(in);
      //  pickup = Location.CREATOR.createFromParcel(in);
        in.readTypedList(Vehicles, VehicleModel.CREATOR);
    }

    public Location getPickup() {
        return pickup;
    }

    public void setPickup(Location pickup) {
        this.pickup = pickup;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getID() {
        if (ID != null) return ID;
        else return "";
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public ArrayList<VehicleModel> getVehicles() {
        return Vehicles;
    }

    public void setVehicle(VehicleModel vehicle) {
        Vehicles.add(vehicle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ID);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(mobile);
        //parcel.writeValue(pickup);
        parcel.writeString(pickupAddress);
       // parcel.writeValue(destination);
        parcel.writeString(destinationAddress);
      //  parcel.writeValue(currentLocation);
     //   pickup.writeToParcel(parcel, i);
      //  destination.writeToParcel(parcel, i);
        parcel.writeTypedList(Vehicles);


    }

    @Override
    public String toString() {
        return "{" + "\"ID\":\"" + ID + "\"" + ", \"name\":\"" + name + "\"" + ", \"email\":\"" + email + "\"" + ", \"mobile\":\"" + mobile + "\""+ ", \"Service\":\"" + ServiceType + "\"" + ", \"DropOFF\":\"" + destinationAddress + "\""  + ", \"PickupAddress\":\"" + pickupAddress + "\"" + ", \"Pickup\":\"" + pickup.toString() + "\"" + ", \"destination\":\"" + destination.toString() + "\""  + '}';
    }


}
