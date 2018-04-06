package com.wenshi_egypt.wenshi.model;

/**
 * Created by michaelanis on 3/9/18.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


class DataParser {
    public String getDuration() {

        Log.i("JSON",duration );
        return duration;
    }

    public String getDistance() {
        Log.i("JSON",distance);

        return distance;
    }

    String duration = "";
    String distance ="";
    JSONArray jsonArray = null;

    public String getTimeSec() {

        Log.i("JSON",timeSec);
        return timeSec;
    }

    String timeSec="";




    private HashMap<String,String> getDuration(JSONArray googleDirectionsJson)
    {

        HashMap<String,String> googleDirectionsMap = new HashMap<>();


        if(googleDirectionsJson != null)
        try {

            duration = googleDirectionsJson.getJSONObject(0).getJSONObject("duration").getString("text");
            distance = googleDirectionsJson.getJSONObject(0).getJSONObject("distance").getString("value");
            timeSec = (googleDirectionsJson.getJSONObject(0).getJSONObject("duration").getString("value"));
            googleDirectionsMap.put("duration" , duration);
            googleDirectionsMap.put("distance", distance);
            googleDirectionsMap.put("timeSec", timeSec);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return googleDirectionsMap;
    }


    private HashMap<String, String> getPlace(JSONObject googlePlaceJson)
    {
        HashMap<String, String> googlePlacesMap = new HashMap<>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        Log.d("getPlace", "Entered");


        try {
            if(!googlePlaceJson.isNull("name"))
            {

                placeName = googlePlaceJson.getString("name");

            }
            if( !googlePlaceJson.isNull("vicinity"))
            {
                vicinity = googlePlaceJson.getString("vicinity");

            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference = googlePlaceJson.getString("reference");

            googlePlacesMap.put("place_name" , placeName);
            googlePlacesMap.put("vicinity" , vicinity);
            googlePlacesMap.put("lat" , latitude);
            googlePlacesMap.put("lng" , longitude);
            googlePlacesMap.put("reference" , reference);


            Log.d("getPlace", "Putting Places");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePlacesMap;
    }



    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String,String>> placesList = new ArrayList<>();
        HashMap<String,String> placeMap = null;
       // Log.d("Places", "getPlaces");

        for(int i = 0;i<count;i++)
        {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placesList;

    }

    public List<HashMap<String,String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
           // Log.d("Places", "parse");

            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPlaces(jsonArray);
    }

    public String[] parseDirections(String jsonData)
    {

        JSONObject jsonObject;
        JSONArray  jsonArrLegs = new JSONArray();

        try {
            jsonObject = new JSONObject(jsonData);
            //Log.i("Parsed Data",jsonObject.toString());
            this.jsonArray =jsonObject.getJSONArray("routes");
            //Log.i("Parsed Data Json2",this.jsonArray.toString());
            jsonArrLegs = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");

            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("ERR",jsonData);
        }
        getDuration(jsonArrLegs);
        return getPaths(jsonArray);
    }

    public String[] getPaths(JSONArray googleStepsJson )
    {
        int count= 0;

        if(googleStepsJson != null)
            count = googleStepsJson.length();
        String[] polylines = new String[count];

        for(int i = 0;i<count;i++)
        {
            try {
                polylines[i] = getPath(googleStepsJson.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return polylines;
    }

    public String getPath(JSONObject googlePathJson)
    {
        String polyline = "";
        try {
            polyline = googlePathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }



}
