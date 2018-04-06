package com.wenshi_egypt.wenshi.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.wenshi_egypt.wenshi.helpers.AppUtils.Defs.CAIRO;

/**
 * Created by michaelanis on 3/2/18.
 */

public class UserModel implements Parcelable {
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods

    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {

        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    String ID = "ID";
    String name = "";
    String email = "";
    String mobile = "";
    String ServiceType;
    Location pickup;
    String pickupAddress;
    Location destination;
    String destinationAddress;
    Location currentLocation;
    String vehicleSelected = "";
    int vehicleSelectedIndex = 0;
    double rating;
    LinkedHashMap<String, VehicleModel> vehicles = new LinkedHashMap<String, VehicleModel>();
    LinkedHashMap<String, HistoryModel> history = new LinkedHashMap<String, HistoryModel>();
    public UserModel(String id, String name, String email, String mobile, double rating) {
        this.ID = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        Location locationCairo = new Location("");
        locationCairo.setLatitude(CAIRO.latitude);
        locationCairo.setLongitude(CAIRO.longitude);
        pickup = locationCairo;
        destination = locationCairo;
        this.rating = rating;
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
        //   in.readTypedList(vehicles, VehicleModel.CREATOR);
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getVehicleSelectedIndex() {
        return vehicleSelectedIndex;
    }

    public void setVehicleSelectedIndex(int vehicleSelectedIndex) {
        this.vehicleSelectedIndex = vehicleSelectedIndex;
    }

    public Map<String, HistoryModel> getHistory() {
        return history;
    }

    public void addHistory(String id, HistoryModel historyItem) {
        history.put(id, historyItem);
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getVehicleSelected() {
        return vehicleSelected;
    }

    public void setVehicleSelected(String vehicleSelected) {
        this.vehicleSelected = vehicleSelected;
    }

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

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Map<String, VehicleModel> getvehicles() {
        return vehicles;
    }

    public void setVehicle(String id, VehicleModel vehicle) {
        vehicles.put(id, vehicle);
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
        //  parcel.writeTypedList(vehicles);


    }

    @Override
    public String toString() {
        return "{" + "\"ID\":\"" + ID + "\"" + ", \"name\":\"" + name + "\"" + ", \"email\":\"" + email + "\"" + ", \"mobile\":\"" + mobile + "\"" + ", \"Service\":\"" + ServiceType + "\"" + ", \"DropOFF\":\"" + destinationAddress + "\"" + ", \"PickupAddress\":\"" + pickupAddress + "\"" + ", \"Pickup\":\"" + pickup.toString() + "\"" + ", \"destination\":\"" + destination.toString() + "\"" + ", \"vehicle\":\"" + vehicleSelected + "\"" + '}';
    }


}
