package com.wenshi_egypt.wenshi;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.wenshi_egypt.wenshi.model.UserModel;

import java.sql.Driver;


public class TraceDriverFragment extends Fragment implements View.OnClickListener {

    private UserModel driverModel;
    private TextView eta, driverName, winshType, plateNo;
    private Button callDriver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trace_driver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eta = ((TextView) getView().findViewById(R.id.etaTraceDriver));
        driverName = ((TextView) getView().findViewById(R.id.driverName));
        winshType = ((TextView) getView().findViewById(R.id.winshType_traceDriver));
        plateNo = ((TextView) getView().findViewById(R.id.plateNumber_traceDriver));
        callDriver = ((Button) getView().findViewById(R.id.button_call_driver));
        callDriver.setOnClickListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        if (args != null) {
        }

        if(((CustomerMapActivity) getActivity()).getGetDirectionsData() != null)
        eta.setText(getResources().getString(R.string.your_wenshi_will_arrive_in) + " " + ((CustomerMapActivity) getActivity()).getGetDirectionsData().getDuration());
        driverModel = ((CustomerMapActivity) getActivity()).getDriverModel();
        driverName.setText(driverModel.getName());
        winshType.setText(driverModel.getDriverCarType());
        plateNo.setText(driverModel.getDriverPlateNo());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_call_driver) {
            if (driverModel != null && driverModel.getMobile() != null)
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", driverModel.getMobile(), null)));
            else {
                Toast.makeText(getActivity(), getResources().getString(R.string.fui_error_too_many_attempts), Toast.LENGTH_SHORT).show();

            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
