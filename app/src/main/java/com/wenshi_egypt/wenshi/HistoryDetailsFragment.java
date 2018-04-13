package com.wenshi_egypt.wenshi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wenshi_egypt.wenshi.model.HistoryModel;
import com.wenshi_egypt.wenshi.model.VehicleModel;

public class HistoryDetailsFragment extends Fragment {

    private HistoryModel HISTORY_MODEL;
    private EditText date, startTime, endTime, eta, distance, clientName, clientID, driverName, driverID, cost, completed, tripID;


    public HistoryDetailsFragment() {
        // Required empty public constructor
    }


    public static HistoryDetailsFragment newInstance(String param1, String param2) {
        HistoryDetailsFragment fragment = new HistoryDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_details, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        date = getActivity().findViewById(R.id.editText_date);
        startTime = getActivity().findViewById(R.id.editText_startTime);
        endTime = getActivity().findViewById(R.id.editText_endTime);
        eta = getActivity().findViewById(R.id.editText_eta);
        distance = getActivity().findViewById(R.id.editText_distance);
        clientName = getActivity().findViewById(R.id.editText_clientName);
        clientID = getActivity().findViewById(R.id.editText_cleintID);
        driverName = getActivity().findViewById(R.id.editText_driverName);
        driverID = getActivity().findViewById(R.id.editText_driverID);
        cost = getActivity().findViewById(R.id.editText_cost);
        completed = getActivity().findViewById(R.id.editText_compeleted);
        tripID = getActivity().findViewById(R.id.editText_tripID);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            HISTORY_MODEL = ((HistoryModel) args.getParcelable("DATA"));
            if (HISTORY_MODEL != null && !HISTORY_MODEL.getId().isEmpty()) {
                fillHistory();
            }
        }
    }

     private void fillHistory() {
        try {
            if (HISTORY_MODEL != null && HISTORY_MODEL.getId() != null && !HISTORY_MODEL.getId().isEmpty()) {
                date.setText(HISTORY_MODEL.getDate() != null ? HISTORY_MODEL.getDate().toString() : "");
                startTime.setText(HISTORY_MODEL.getStartTime() != null ? HISTORY_MODEL.getStartTime().toString() : "");
                endTime.setText(HISTORY_MODEL.getEndTime() != null ? HISTORY_MODEL.getEndTime().toString() : "");
                eta.setText(HISTORY_MODEL.getEta() != null ? HISTORY_MODEL.getEta().toString() : "");
                distance.setText(HISTORY_MODEL.getDistance() != null ? (Double.parseDouble(HISTORY_MODEL.getDistance().toString()) / 1000) + " KM" : "");
                clientName.setText(HISTORY_MODEL.getClientName() != null ? HISTORY_MODEL.getClientName().toString() : "");
                clientID.setText(HISTORY_MODEL.getCleintID() != null ? HISTORY_MODEL.getCleintID().toString() : "");
                driverName.setText(HISTORY_MODEL.getDriverName() != null ? HISTORY_MODEL.getDriverName().toString() : "");
                driverID.setText(HISTORY_MODEL.getDriverID() != null ? HISTORY_MODEL.getDriverID().toString() : "");
                cost.setText(HISTORY_MODEL.getCost() != null ? HISTORY_MODEL.getCost().toString() : "");
                completed.setText(HISTORY_MODEL.isCompeleted() ? getResources().getString(R.string.completed) : getResources().getString(R.string.canceled));
                tripID.setText(HISTORY_MODEL.getId() != null ? HISTORY_MODEL.getId().toString() : "");

            }
        }catch (Exception e){
            Log.i("Error","HistoryDetaislFragment"+e.toString());
        }
    }

}
