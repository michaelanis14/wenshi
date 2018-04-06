package com.wenshi_egypt.wenshi.model;

/**
 * Created by michaelanis on 3/9/18.
 */

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.HashMap;


public class GetDirectionsData extends AsyncTask<Object, String, String> {

    public AsyncResponse delegate = null;
    GoogleMap mMap;
    String url;
    String googleDirectionsData;
    LatLng latLng;
    Polyline polylineFinal;
    String duration = "";
    String distance = "";
    double timeSec;

    public GetDirectionsData(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    public double getTimeSec() {
        return timeSec;
    }



    public String getDuration() {
        return duration;
    }

    public String getDistance() {
        return distance;
    }

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        latLng = (LatLng) objects[2];
        duration = (String) objects[3];
        distance = (String) objects[4];
        timeSec = (double) objects[5];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            String[] directionsList;
            DataParser parser = new DataParser();
            directionsList = parser.parseDirections(s);
            distance = parser.getDistance();
            duration = parser.getDuration();
            timeSec = Double.parseDouble(parser.getTimeSec());
            displayDirection(directionsList);
            Log.i("JSON", duration + distance + timeSec);

            delegate.gotDurationDistanceRout(s);
        } catch (Exception e) {

        }

    }

    public void displayDirection(String[] directionsList) {

        int count = directionsList.length;
        for (int i = 0; i < count; i++) {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.BLACK);
            options.width(10);
            options.addAll(PolyUtil.decode(directionsList[i]));

            polylineFinal = mMap.addPolyline(options);


        }
    }

    public void clearDirections() {
        polylineFinal.remove();

    }

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void gotDurationDistanceRout(String output);
    }


}

