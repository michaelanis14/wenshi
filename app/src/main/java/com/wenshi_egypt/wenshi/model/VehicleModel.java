package com.wenshi_egypt.wenshi.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by michaelanis on 3/2/18.
 */

public class VehicleModel implements Parcelable {


    public static final Parcelable.Creator<VehicleModel> CREATOR = new Parcelable.Creator<VehicleModel>() {
        public VehicleModel createFromParcel(Parcel in) {
            return new VehicleModel(in);
        }

        public VehicleModel[] newArray(int size) {
            return new VehicleModel[size];
        }
    };
    String id;
    String make;
    String model;
    boolean type;
    String color;
    String year;
    public VehicleModel() {
        this.make = "";
        this.model = "";
        this.type = false;
        this.color = "";
        this.year = "";
        this.id = "";
    }

    public VehicleModel(String id, String make, String model, boolean type, String color, String year) {
        this.make = make;
        this.model = model;
        this.type = type;
        this.color = color;
        this.year = year;
        this.id = id;
    }

    private VehicleModel(Parcel in) {
        make = in.readString();
        model = in.readString();
        type = in.readByte() != 0;
        color = in.readString();
        year = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(make);
        parcel.writeString(model);
        parcel.writeByte((byte) (type ? 1 : 0));     //if myBoolean == true, byte == 1
        parcel.writeString(color);
        parcel.writeString(year);
    }

    @Override
    public String toString() {
        return "{" + "\"make\":\"" + make + "\"" + ", \"model\":\"" + model + "\"" + ", \"type\":\"" + type + "\"" + ", \"color\":\"" + color + "\"" + ", \"year\":\"" + year + "\"" + '}';
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
