package com.wenshi_egypt.wenshi.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DecimalFormat;

public class HistoryModel implements Parcelable {

    public static final Parcelable.Creator<HistoryModel> CREATOR = new Parcelable.Creator<HistoryModel>() {
        public HistoryModel createFromParcel(Parcel in) {
            return new HistoryModel(in);
        }

        public HistoryModel[] newArray(int size) {
            return new HistoryModel[size];
        }
    };
    Location driverStartLocation;
    Location clientActualDropOffLocation;
    Location clientIntialDropOffLocation;
    Location clientActualPickupLocation;
    Location clientIntialPickupLocation;
    private String id;
    private String date;
    private String startTime;
    private String endTime;
    private String eta;
    private String distance;
    private String clientName;
    private String cleintID;
    private String driverName;
    private String driverID;
    private String vehicleDetails;
    private double cost;
    private double timeSec;
    private boolean compeleted;
    private String driverStartAddress;
    private String clientIntialDropOffAddress;
    private String clientActualDroOffAddress;
    private String clientIntialPickupAddress;
    private String clientActualPickupAddress;
    public HistoryModel(String id, String date, String startTime, String endTime, String eta, String distance, String clientName, String cleintID, String driverName, String driverID, String vehicleDetails, double cost, boolean compeleted) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eta = eta;
        this.distance = distance;
        this.clientName = clientName;
        this.cleintID = cleintID;
        this.vehicleDetails = vehicleDetails;
        this.cost = cost;
        this.compeleted = compeleted;
        this.driverID = driverID;
        this.driverName = driverName;
    }

    public HistoryModel(String id, String date, String startTime, String eta, String distance, String clientName, String cleintID, String driverName, String driverID, String vehicleDetails) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.eta = eta;
        this.distance = distance;
        this.clientName = clientName;
        this.cleintID = cleintID;
        this.driverID = driverID;
        this.driverName = driverName;
        this.vehicleDetails = vehicleDetails;
    }

    private HistoryModel(Parcel in) {
        id = in.readString();
        date = in.readString();
        endTime = in.readString();
        eta = in.readString();
        startTime = in.readString();
        distance = in.readString();
        clientName = in.readString();
        cleintID = in.readString();
        driverName = in.readString();
        driverID = in.readString();
        vehicleDetails = in.readString();
        cost = in.readDouble();
        timeSec = in.readDouble();
        compeleted = in.readByte() != 0;

    }

    public Location getDriverStartLocation() {
        return driverStartLocation;
    }

    public void setDriverStartLocation(Location driverStartLocation) {
        this.driverStartLocation = driverStartLocation;
    }

    public String getDriverStartAddress() {
        return driverStartAddress;
    }

    public void setDriverStartAddress(String driverStartAddress) {
        this.driverStartAddress = driverStartAddress;
    }

    public String getClientIntialDropOffAddress() {
        return clientIntialDropOffAddress;
    }

    public void setClientIntialDropOffAddress(String clientIntialDropOffAddress) {
        this.clientIntialDropOffAddress = clientIntialDropOffAddress;
    }

    public String getClientActualDroOffAddress() {
        return clientActualDroOffAddress;
    }

    public void setClientActualDroOffAddress(String clientActualDroOffAddress) {
        this.clientActualDroOffAddress = clientActualDroOffAddress;
    }

    public Location getClientActualDropOffLocation() {
        return clientActualDropOffLocation;
    }

    public void setClientActualDropOffLocation(Location clientActualDropOffLocation) {
        this.clientActualDropOffLocation = clientActualDropOffLocation;
    }

    public Location getClientIntialDropOffLocation() {
        return clientIntialDropOffLocation;
    }

    public void setClientIntialDropOffLocation(Location clientIntialDropOffLocation) {
        this.clientIntialDropOffLocation = clientIntialDropOffLocation;
    }

    public String getClientIntialPickupAddress() {
        return clientIntialPickupAddress;
    }

    public void setClientIntialPickupAddress(String clientIntialPickupAddress) {
        this.clientIntialPickupAddress = clientIntialPickupAddress;
    }

    public String getClientActualPickupAddress() {
        return clientActualPickupAddress;
    }

    public void setClientActualPickupAddress(String clientActualPickupAddress) {
        this.clientActualPickupAddress = clientActualPickupAddress;
    }

    public Location getClientActualPickupLocation() {
        return clientActualPickupLocation;
    }

    public void setClientActualPickupLocation(Location clientActualPickupLocation) {
        this.clientActualPickupLocation = clientActualPickupLocation;
    }

    public Location getClientIntialPickupLocation() {
        return clientIntialPickupLocation;
    }

    public void setClientIntialPickupLocation(Location clientIntialPickupLocation) {
        this.clientIntialPickupLocation = clientIntialPickupLocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public boolean isCompeleted() {
        return compeleted;
    }

    public void setCompeleted(boolean compeleted) {
        this.compeleted = compeleted;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        //   Log.i("DISTANCE",""+distance);

        this.distance = distance;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCleintID() {
        return cleintID;
    }

    public void setCleintID(String cleintID) {
        this.cleintID = cleintID;
    }

    public String getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(String vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public double getTimeSec() {
        return timeSec;
    }

    public void setTimeSec(double timeSec) {
        //    Log.i("TIME IN SEC",""+timeSec);
        this.timeSec = timeSec;
    }

    public String getCost() {
        DecimalFormat format = new DecimalFormat("0.#");
        return format.format(cost);
    }

    @SuppressWarnings("unused")
    public void setCost(double cost) {
        this.cost = cost;
    }

    public void calculateCost(boolean sedan) {

        try {
            if (distance == null && distance.isEmpty()) return;
            String tempDis = "";
            tempDis = distance.replace("KM", "");
            if (tempDis.isEmpty() || tempDis.contains("M")) {
                tempDis = "1";
            }

            Double distanceDouble = Double.parseDouble(tempDis.trim());
            distanceDouble = (distanceDouble / 1000);
            if (distanceDouble <= 40.0) {
                Log.i("HISCOSTT", "" + (distanceDouble) + " " + (timeSec / 60));


                if (sedan) {
                    this.cost = 152 + ((distanceDouble) * 4.24) + ((timeSec / 60) * 2.125);
                } else {
                    this.cost = 152 + ((distanceDouble) * 4.6) + ((timeSec / 60) * 2.125);
                }

            } else {
                Log.i("HISCOSTT", "" + (distanceDouble) + " " + (timeSec / 60));

                if (sedan) {
                    this.cost = 152 + ((distanceDouble) * 3.31) + ((timeSec / 60) * 2.125);
                } else {
                    this.cost = 152 + ((distanceDouble) * 3.6) + ((timeSec / 60) * 2.125);
                }
            }
        } catch (Exception e) {
            Log.i("ERROR", "History Fragment   " + e.toString());
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(date);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeString(eta);
        parcel.writeString(distance);
        parcel.writeString(clientName);
        parcel.writeString(cleintID);
        parcel.writeString(driverName);
        parcel.writeString(driverID);
        parcel.writeString(vehicleDetails);
        parcel.writeByte((byte) (compeleted ? 1 : 0));     //if myBoolean == true, byte == 1
        parcel.writeDouble(cost);
        parcel.writeDouble(timeSec);
    }
}