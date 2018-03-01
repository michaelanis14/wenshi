package com.wenshi_egypt.wenshi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class VehiclesFragment extends Fragment implements View.OnClickListener{

    DatabaseReference rootRef, vehicleRef;
    TextView demoValue, addNewVehicle;
    static String type, type2, model;
    static String defType, defType2, defModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vehicles, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final ArrayList<Vehicle> vehicle = getVehicle();
        //noinspection ConstantConditions
        demoValue = getView().findViewById(R.id.tvValue2);
        addNewVehicle = getView().findViewById(R.id.textView_vehicle_addNew);

        final ListView lv1 = getView().findViewById(R.id.listView_vehicles);

        lv1.setAdapter(new MyVehiclesAdapter(getActivity(), vehicle));

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                defaultVehicle = getView().findViewById(R.id.textView_vehicle_default);
//                defaultVehicle.setVisibility(View.VISIBLE);
                //Toast.makeText(getActivity(), "position number: " + position, Toast.LENGTH_LONG).show();
                defType = vehicle.get(position).getType();
                defType2 = vehicle.get(position).getType2();
                defModel = vehicle.get(position).getModel();
                Toast.makeText(getActivity(), "Type: " + defType, Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Type2: " + defType2, Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Model: " + defModel, Toast.LENGTH_SHORT).show();
            }
        });

        addNewVehicle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startActivity(new Intent(getContext(), AddNewVehicle.class));
                return true;
            }
        });
    }

    private ArrayList<Vehicle> getVehicle(){
        final ArrayList<Vehicle> results = new ArrayList<>();

        //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();
        vehicleRef = rootRef.child("Vehicles");
        vehicleRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    //noinspection unchecked
                    HashMap<String, String> value = (HashMap<String, String>) child.getValue();
                    assert value != null;
                    type = value.get("type");
                    type2 = value.get("type2");
                    model = String.format("%s", value.get("model"));

                    demoValue.setText("");
                    results.add(new Vehicle(type, type2, model));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
             //   Toast.makeText(HistoricFragment.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        return results;
    }



    @Override
    public void onClick(View view) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}