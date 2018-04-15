package com.wenshi_egypt.wenshi;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;


public class RateDriverFragment extends Fragment implements View.OnClickListener {

    private TextView totalCost, distance, time;
    private Button save;

    public RateDriverFragment() {
    }


    public static RateDriverFragment newInstance(String param1, String param2) {
        RateDriverFragment fragment = new RateDriverFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rate_driver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        totalCost = getActivity().findViewById(R.id.textView_trip_toal_cost);
        distance = getActivity().findViewById(R.id.textView_trip_toal_km);
        time = getActivity().findViewById(R.id.textView_trip_toal_time);
        save =  getView().findViewById(R.id.rate_save);
        save.setBackgroundColor(Color.BLACK);
        save.bringToFront();
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i("RATEEDRIVEER","CLICKKK");
        ((DriverMapsActivity) getActivity()).onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();

        save.bringToFront();

        try {
            ((DriverMapsActivity) getActivity()).getCurrentHistory().getCost();

            totalCost.setText(getResources().getString(R.string.textView_cost)+" : "+((DriverMapsActivity) getActivity()).getCurrentHistory().getCost() + "L.E.");
            DecimalFormat format = new DecimalFormat("0.#");
            distance.setText(getResources().getString(R.string.distance)+" : "+format.format((Double.parseDouble(((DriverMapsActivity) getActivity()).getCurrentHistory().getDistance()) / 1000)) + " KM");

            int totalSecs = (int) ((DriverMapsActivity) getActivity()).getCurrentHistory().getTimeSec();
            int hours = totalSecs / 3600;
            int minutes = (totalSecs % 3600) / 60;
           // int seconds = totalSecs % 60;

           String timeString = String.format("%02d:%02d", hours, minutes);
            time.setText(getResources().getString(R.string.trip_duration)+" : "+timeString);
        } catch (Exception e) {
            Log.i("ERROR", "Failt to parce");
        }
        // ((DriverMapsActivity)getActivity()).getCurrentHistory();

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
