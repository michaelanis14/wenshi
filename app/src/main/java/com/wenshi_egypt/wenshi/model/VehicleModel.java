package com.wenshi_egypt.wenshi.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by michaelanis on 3/2/18.
 */

public class VehicleModel implements Parcelable {
    String CarType;
    String Model;

    public VehicleModel(String carType, String model) {
        this.CarType = carType;
        this.Model = model;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(CarType);
        parcel.writeString(Model);
    }
    @Override
    public String toString() {
        return "VehicleModel{" +
                "CarType='" + CarType + '\'' +
                ", Model='" + Model + '\'' +
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
        CarType = in.readString();
        Model = in.readString();
    }
}
