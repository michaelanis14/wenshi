package com.wenshi_egypt.wenshi.model;

import java.util.ArrayList;

/**
 * Created by michaelanis on 3/2/18.
 */

public class UserModel {
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



}
