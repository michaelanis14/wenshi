package com.wenshi_egypt.wenshi.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by michaelanis on 3/2/18.
 */

public class TripModel implements Parcelable {
    String date, from, to, cost;

    public TripModel(String date, String from, String to, String cost) {
        this.date = date;
        this.from = from;
        this.to = to;
        this.cost = cost;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(from);
        parcel.writeString(to);
        parcel.writeString(cost);
    }
    @Override
    public String toString() {
        return "{" +
                "\"date\":\"" + date + "\"" +
                ", \"from\":\"" + from + "\"" +
                "\"to\":\"" + to + "\"" +
                ", \"cost\":\"" + cost + "\"" +
                '}';
    }


    public static final Creator<TripModel> CREATOR
            = new Creator<TripModel>() {
        public TripModel createFromParcel(Parcel in) {
            return new TripModel(in);
        }

        public TripModel[] newArray(int size) {
            return new TripModel[size];
        }
    };


    private TripModel(Parcel in) {
        date = in.readString();
        from = in.readString();
        to = in.readString();
        cost = in.readString();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

}
