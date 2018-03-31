package com.wenshi_egypt.wenshi;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class VehiclesFragment extends Fragment implements View.OnClickListener {

    static String type, model;
    static String defType, defModel;
    DatabaseReference rootRef, vehicleRef;
    TextView demoValue, noVehicle;
    Button addNewVehicle;

    UserModel user;


    AddNewVehicleFragment vehicleDetailsFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vehicles, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // final ArrayList<VehicleModel> vehicle = getVehicle();
        //noinspection ConstantConditions

        addNewVehicle = getView().findViewById(R.id.btn_Aadd_new_vehicle);
        addNewVehicle.setBackgroundColor(Color.BLACK);
        addNewVehicle.setOnClickListener(this);


        user = ((CustomerMapActivity) getActivity()).getCustomer();


        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.vehicles_list_layout);

        if (user.getVehicles().isEmpty()) {
            addNewVehicle();

        } else for (VehicleModel vehicle : user.getVehicles()) {
            Button button = (Button) getLayoutInflater().inflate(R.layout.list_button, null);
            button.setText(vehicle.getModel() + " " + vehicle.getType());
            button.setId(user.getVehicles().indexOf(vehicle));
            layout.addView(button);
        }

    }

/*
    private ArrayList<VehicleModel> getVehicle() {
        final ArrayList<VehicleModel> results = new ArrayList<>();
        assert getActivity() != null;
        UserModel user = ((CustomerMapActivity) getActivity()).getCustomer();

        //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();
        vehicleRef = rootRef.child("Users").child("Customers").child(user.getID()).child("Vehicles");
        vehicleRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //noinspection unchecked
                    HashMap<String, String> value = (HashMap<String, String>) child.getValue();
                    assert value != null;
                    if (!child.hasChild("FirstConstantVehicle")) {
                        type = value.get("type");
                        model = String.format("%s", value.get("model"));

                        demoValue.setText("");
                        if (!type.isEmpty()) {
                            noVehicle.setVisibility(View.INVISIBLE);
                            results.add(new VehicleModel(type, model, true, "", ""));
                        }
                    }
                }
                if (results.size() == 0) noVehicle.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //   Toast.makeText(HistoricFragment.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        return results;


    }
*/

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_Aadd_new_vehicle:
                addNewVehicle();
                break;

        }
    }


    private void addNewVehicle() {
        ((CustomerMapActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.button_new_vehicle));
        if (vehicleDetailsFragment == null) vehicleDetailsFragment = new AddNewVehicleFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        ft.replace(R.id.mainFrame, vehicleDetailsFragment);
        ft.commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}