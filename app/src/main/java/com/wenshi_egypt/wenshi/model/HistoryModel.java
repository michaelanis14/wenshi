package com.wenshi_egypt.wenshi.model;

import android.util.Log;

import java.text.DecimalFormat;

public class HistoryModel {

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
        Log.i("TIME IN SEC",""+timeSec);
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


        String tempDis = distance.replace("KM", "");
        if (tempDis.contains("M")) {
            tempDis = "1";
        }

        Double distanceDouble = Double.parseDouble(tempDis.trim());
        if (distanceDouble <= 40.0) {
            if (sedan) {
                this.cost = 152 + (distanceDouble * 4.24) + ((timeSec / 60) * 2.125);
            } else {
                this.cost = 152 + (distanceDouble * 4.6) + ((timeSec / 60) * 2.125);
            }

        } else {
            if (sedan) {
                this.cost = 152 + (distanceDouble * 3.31) + ((timeSec / 60) * 2.125);
            } else {
                this.cost = 152 + (distanceDouble * 3.6) + ((timeSec / 60) * 2.125);
            }
        }
    }
}