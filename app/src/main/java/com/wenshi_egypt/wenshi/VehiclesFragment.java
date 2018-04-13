package com.wenshi_egypt.wenshi;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wenshi_egypt.wenshi.model.UserModel;
import com.wenshi_egypt.wenshi.model.VehicleModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VehiclesFragment extends Fragment implements View.OnClickListener {

    private static String type, model;
    private static String defType, defModel;
    private Button addNewVehicle;

    private UserModel user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vehicles, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addNewVehicle = getView().findViewById(R.id.btn_Aadd_new_vehicle);
        addNewVehicle.setBackgroundColor(Color.BLACK);
        addNewVehicle.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_Aadd_new_vehicle:
                addNewVehicle(0);
                break;
            default:
                addNewVehicle(view.getId());

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.vehicles_list_layout);
        user = ((CustomerMapActivity) getActivity()).getCustomer();

        if (user.getvehicles() == null || user.getvehicles().size() == 0) {
            //  addNewVehicle(0);
            getView().findViewById(R.id.noVehicles).setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), getResources().getString(R.string.you_must_have_atleast_one_vehicle), Toast.LENGTH_SHORT).show();
            ((CustomerMapActivity) getActivity()).setCURRENTSTATE(((CustomerMapActivity) getActivity()).INCOMPLETEVHICLES);


        } else for (Map.Entry<String, VehicleModel> vehicle : user.getvehicles().entrySet()) {
            getView().findViewById(R.id.noVehicles).setVisibility(View.GONE);
            Button button = (Button) getLayoutInflater().inflate(R.layout.list_button, null);
            button.setText(vehicle.getValue().getMake() + " " + vehicle.getValue().getModel() + " " + (vehicle.getValue().isType() ? "Sedan" : "SUV"));
            button.setId(Integer.parseInt(vehicle.getKey()));
            button.setOnClickListener(this);
            layout.addView(button);
        }

    }

    private void addNewVehicle(int vid) {

        ((CustomerMapActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.button_new_vehicle));
        if (((CustomerMapActivity) getActivity()).vehicleDetailsFragment == null)
            ((CustomerMapActivity) getActivity()).vehicleDetailsFragment = new AddNewVehicleFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        if (vid > 0) {
            Bundle args = new Bundle();
            args.putParcelable("DATA", ((CustomerMapActivity) getActivity()).getCustomer().getvehicles().get(vid + ""));
            ((CustomerMapActivity) getActivity()).vehicleDetailsFragment.setArguments(args);
        } else {
            Bundle args = new Bundle();
            args.putParcelable("DATA", null);
            ((CustomerMapActivity) getActivity()).vehicleDetailsFragment.setArguments(args);
        }
        ft.replace(R.id.mainFrame, ((CustomerMapActivity) getActivity()).vehicleDetailsFragment);
        ft.commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}