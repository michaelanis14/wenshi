package com.wenshi_egypt.wenshi.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by michaelanis on 3/2/18.
 */

public class VehicleModel implements Parcelable {
    String type;
    String model;
    boolean sedan;
    String color;
    String year;

    public VehicleModel(String carType, String model,boolean sedan,String color,String year) {
        this.type = carType;
        this.model = model;
        this.sedan = sedan;
        this.color = color;
        this.year = year;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(model);
    }
    @Override
    public String toString() {
        return "{" +
                "\"type\":\"" + type + "\"" +
                ", \"model\":\"" + model + "\"" +
                ", \"sedan\":\"" + sedan + "\"" +
                ", \"color\":\"" + color + "\"" +
                ", \"year\":\"" + year + "\"" +
                '}';
    }
    public static final Parcelable.Creator<VehicleModel> CREATOR
            = new Parcelable.Creator<VehicleModel>() {
        public VehicleModel createFromParcel(Parcel in) {
            return new VehicleModel(in);
        }

        public VehicleModel[] newArray(int size) {
            return new VehicleModel[size];
        }
    };

    private VehicleModel(Parcel in) {
        type = in.readString();
        model = in.readString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
