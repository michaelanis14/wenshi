package com.wenshi_egypt.wenshi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wenshi_egypt.wenshi.model.UserModel;
import com.wenshi_egypt.wenshi.model.VehicleModel;

import java.util.ArrayList;
import java.util.HashMap;

public class VehiclesFragment extends Fragment implements View.OnClickListener{

    DatabaseReference rootRef, vehicleRef;
    TextView demoValue, addNewVehicle, noVehicle;
    static String type, model;
    static String defType, defModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vehicles, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final ArrayList<VehicleModel> vehicle = getVehicle();
        //noinspection ConstantConditions
        demoValue = getView().findViewById(R.id.tvValue2);
        addNewVehicle = getView().findViewById(R.id.textView_vehicle_addNew);
        noVehicle = getView().findViewById(R.id.textView_no_vehicle);

        final ListView lv1 = getView().findViewById(R.id.listView_vehicles);

       // lv1.setAdapter(new MyVehiclesAdapter(getActivity(), vehicle));

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                defaultVehicle = getView().findViewById(R.id.textView_vehicle_default);
//                defaultVehicle.setVisibility(View.VISIBLE);
                //Toast.makeText(getActivity(), "position number: " + position, Toast.LENGTH_LONG).show();
                defType = vehicle.get(position).getType();
                defModel = vehicle.get(position).getModel();
        //        Toast.makeText(getActivity(), "Type: " + defType, Toast.LENGTH_SHORT).show();
        //        Toast.makeText(getActivity(), "Model: " + defModel, Toast.LENGTH_SHORT).show();

                DatabaseReference defVehicle = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(((CustomerMapActivity) getActivity()).getCustomer().getID());
                defVehicle.child("carType").setValue(defType);
                defVehicle.child("model").setValue(defModel);
            }
        });

        addNewVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddNewVehicle.class);
                Bundle b = new Bundle();
                b.putString("uid", ((CustomerMapActivity) getActivity()).getCustomer().getID());
                intent.putExtras(b);
                startActivity(intent);

                //startActivity(new Intent(getContext(), AddNewVehicle.class));
            }
        });
    }


    private ArrayList<VehicleModel> getVehicle(){
        final ArrayList<VehicleModel> results = new ArrayList<>();
        UserModel user = ((CustomerMapActivity) getActivity()).getCustomer();

        //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();
        vehicleRef = rootRef.child("Users").child("Customers").child(user.getID()).child("Vehicles");
        vehicleRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    //noinspection unchecked
                    HashMap<String, String> value = (HashMap<String, String>) child.getValue();
                    assert value != null;
                    type = value.get("type");
                    model = String.format("%s", value.get("model"));

                    demoValue.setText("");
                    if(!type.isEmpty()) {
                        noVehicle.setVisibility(View.INVISIBLE);
                        results.add(new VehicleModel(type, model));
                    }
                }
                if(results.size()==0)
                    noVehicle.setVisibility(View.VISIBLE);
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