package com.wenshi_egypt.wenshi;

import java.text.DecimalFormat;

public class HistoricTrip {

    public HistoricTrip(String date, String startTime, String endTime, String eta, String distance, String clientName, String cleintID, String viecleDetails, double cost) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eta = eta;
        this.distance = distance;
        this.clientName = clientName;
        this.cleintID = cleintID;
        this.viecleDetails = viecleDetails;
        this.cost = cost;
    }

    public HistoricTrip(String date, String startTime,  String eta, String distance, String clientName, String cleintID, String viecleDetails) {
        this.date = date;
        this.startTime = startTime;
        this.eta = eta;
        this.distance = distance;
        this.clientName = clientName;
        this.cleintID = cleintID;
        this.viecleDetails = viecleDetails;
    }



    private String date;
    private String startTime;

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

    public String getViecleDetails() {
        return viecleDetails;
    }

    public void setViecleDetails(String viecleDetails) {
        this.viecleDetails = viecleDetails;
    }

    private String endTime;
    private String eta;
    private String distance;
    private String clientName;
    private String cleintID;
    private String viecleDetails;
    private double cost;

    public double getTimeSec() {
        return timeSec;
    }

    public void setTimeSec(double timeSec) {
        this.timeSec = timeSec;
    }

    private double timeSec;


    String getCost() {
        DecimalFormat format = new DecimalFormat("0.#");
        return format.format(cost);
    }

    private void calculateCost(boolean sedan){



        String tempDis = distance.replace("KM","");
        if (tempDis.contains("M")) {
            tempDis = "1";
        }

        Double distanceDouble = Double.parseDouble(tempDis.trim());
        if(distanceDouble <= 40.0){
            if(sedan){
              this.cost = 152 + (distanceDouble *4.24) + ((timeSec/60)*2.125);
            }
            else {
                this.cost = 152 + (distanceDouble *4.6) + ((timeSec/60)*2.125);
            }

        }
        else{
            if(sedan){
                this.cost = 152 + (distanceDouble *3.31) + ((timeSec/60)*2.125);
            }
            else
            {
                this.cost = 152 + (distanceDouble *3.6) + ((timeSec/60)*2.125);
            }
        }
    }

    @SuppressWarnings("unused")
    public void setCost(double cost) {
        this.cost = cost;
    }
}