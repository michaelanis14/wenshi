package com.wenshi_egypt.wenshi;

import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wenshi_egypt.wenshi.model.VehicleModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddNewVehicleFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    VehicleModel VEHICLE_MODEL = new VehicleModel();


    Button newVehicle_btn, deleteVehicle_btn;


    Spinner color, make, year;
    EditText model;
    RadioButton type, carSuv;
    List<String> carMakeItems;
    List<String> carColorItems;
    List<String> carYearItems;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vehices_add_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //  //noinspection ConstantConditions
        // newType = getActivity().findViewById(R.id.editText_newType);
        // newModel = getActivity().findViewById(R.id.editText_newModel);
        // savedSuccess = getActivity().findViewById(R.id.textView_vehicle_saved_label);


        //get the spinner from the xml.
        make = getActivity().findViewById(R.id.new_car_make);
        make.setOnItemSelectedListener(this);
        carMakeItems = Arrays.asList("Alfa Romeo", "Aston Martin", "Audi", "Baic", "Bentley", "BMW", "Brilliance", "Bugatti", "Buick", "Byd", "Cadillac", "Canghe", "Chana", "Changan", "Chery", "Chevrolet", "Chrysler", "Citroën", "Daewoo", "Daihatsu", "Datsun", "DFM", "DFSK", "Dodge", "Emgrand", "Faw", "Ferrari", "Fiat", "Ford", "Foton", "GAC", "Gaz", "Geely", "Gmc", "Great Wall", "Hafei", "Haima", "Haval", "Hawtai", "Honda", "Hummer", "Hyundai", "Infiniti", "Isuzu", "Jac", "Jaguar", "Jeep", "Jonway", "Karry", "Kenbo", "Keyton", "Kia", "Lada", "Lamborghini", "Lancia", "Land Rover", "Landwind", "Lexus", "Lifan", "Lincoln", "Mahindra", "Maserati", "Mazda", "Mercedes", "Mercury", "Mini", "Mitsubishi", "Morris Garage", "Nissan", "Opel", "Peugeot", "Pontiac", "Porsche", "Proton", "Renault", "Saab", "Saipa", "Scion", "Seat", "Senova", "Skoda", "Smart", "Sokon", "Soueast", "Speranza", "Ssang Yong", "Subaru", "Suzuki", "Tata", "Tesla", "Toyota", "UFO", "Victory", "Volkswagen", "Volvo", "ZNA", "Zotye");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, carMakeItems);
        make.setAdapter(adapter);

        model = getActivity().findViewById(R.id.editText_newModel);

        type = getView().findViewById(R.id.carSedan);
        carSuv = getView().findViewById(R.id.carSuv);


        color = getActivity().findViewById(R.id.car_color);
        color.setOnItemSelectedListener(this);
        carColorItems = Arrays.asList("White", "Silver", "Black", "Grey", "Blue", "Red", "Brown", "Green", "Others");
        ArrayAdapter<String> carColorAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, carColorItems);
        color.setAdapter(carColorAdapter);

        year = getActivity().findViewById(R.id.car_year);
        year.setOnItemSelectedListener(this);
        carYearItems = Arrays.asList("2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000", "1999", "1998");
        ArrayAdapter<String> carYearAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, carYearItems);
        year.setAdapter(carYearAdapter);


        newVehicle_btn = getActivity().findViewById(R.id.button_new_vehicle);
        newVehicle_btn.setBackgroundColor(Color.BLACK);
        newVehicle_btn.setOnClickListener(this);
        deleteVehicle_btn = getActivity().findViewById(R.id.delete_vehicle_btn);
        deleteVehicle_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_new_vehicle:
                if (!model.getText().toString().isEmpty()) saveVehicle();
                else
                    Toast.makeText(getActivity(), getResources().getString(R.string.please_enter_car_model), Toast.LENGTH_LONG).show();
                break;
            case R.id.delete_vehicle_btn:
                deleteVehicle();

        }
    }

    private void deleteVehicle() {
        String uid = ((CustomerMapActivity) getActivity()).getCustomer().getID();
        DatabaseReference addVehicle = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(uid).child("Vehicles").child(VEHICLE_MODEL.getId());
        addVehicle.removeValue();
        ((CustomerMapActivity) getActivity()).getCustomer().getvehicles().remove(VEHICLE_MODEL.getId());
        ((CustomerMapActivity) getActivity()).onBackPressed();
    }


    private void saveVehicle() {
        String uid = ((CustomerMapActivity) getActivity()).getCustomer().getID();
        DatabaseReference addVehicle = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(uid).child("Vehicles").child(VEHICLE_MODEL.getId());
        addVehicle.child("make").setValue(make.getSelectedItem().toString());
        addVehicle.child("model").setValue(model.getText().toString());
        addVehicle.child("type").setValue(type.isChecked() + "");
        addVehicle.child("color").setValue(color.getSelectedItem().toString());
        addVehicle.child("year").setValue(year.getSelectedItem().toString());


        VEHICLE_MODEL.setMake(make.getSelectedItem().toString());
        VEHICLE_MODEL.setModel(model.getText().toString());
        VEHICLE_MODEL.setType(type.isChecked());
        VEHICLE_MODEL.setColor(color.getSelectedItem().toString());
        VEHICLE_MODEL.setYear(year.getSelectedItem().toString());

        ((CustomerMapActivity) getActivity()).getCustomer().setVehicle(VEHICLE_MODEL.getId(), VEHICLE_MODEL);
        ((CustomerMapActivity) getActivity()).onBackPressed();
    }


    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            VEHICLE_MODEL = ((VehicleModel) args.getParcelable("DATA"));
            if (VEHICLE_MODEL != null && !VEHICLE_MODEL.getId().isEmpty()) {
                deleteVehicle_btn.setVisibility(View.VISIBLE);
                newVehicle_btn.setText(getResources().getString(R.string.save));
                fillVehicle();

            } else {
                newVehicle();
            }
        } else {
            newVehicle();
        }

//            Toast.makeText(getActivity(), "VISSS", Toast.LENGTH_LONG).show();

    }


    private  void newVehicle(){
        deleteVehicle_btn.setVisibility(View.GONE);
        double vicDouble = Math.random() * 1000;
        int vicCount = (int) vicDouble;
        VEHICLE_MODEL = new VehicleModel();
        VEHICLE_MODEL.setId("" + vicCount);
        newVehicle_btn.setText(getResources().getString(R.string.button_new_vehicle));
        model.setText("");
    }
    private void fillVehicle() {
        make.setSelection(carMakeItems.indexOf(VEHICLE_MODEL.getMake()));
        model.setText(VEHICLE_MODEL.getModel());
        if (VEHICLE_MODEL.isType()) {
            type.setChecked(true);
            carSuv.setChecked(false);
        } else {
            type.setChecked(false);
            carSuv.setChecked(true);
        }
        year.setSelection(carYearItems.indexOf(VEHICLE_MODEL.getYear()));
        color.setSelection(carColorItems.indexOf(VEHICLE_MODEL.getColor()));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //   Log.i("Dropdown", "" + i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
