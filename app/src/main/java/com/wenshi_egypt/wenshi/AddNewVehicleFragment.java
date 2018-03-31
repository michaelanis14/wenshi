package com.wenshi_egypt.wenshi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewVehicleFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    double vicDouble = Math.random() * 1000;
    int vicCount = (int) vicDouble;

   // TextView savedSuccess;
    Button newVehicle;

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
        newVehicle = getActivity().findViewById(R.id.button_new_vehicle);

        //get the spinner from the xml.
        Spinner dropdown = getActivity().findViewById(R.id.new_car_type);
        dropdown.setOnItemSelectedListener(this);

        String[] items = new String[]{"Alfa Romeo","Aston Martin","Audi","Baic","Bentley","BMW","Brilliance","Bugatti","Buick","Byd","Cadillac","Canghe","Chana","Changan","Chery","Chevrolet","Chrysler","Citroën","Daewoo","Daihatsu","Datsun","DFM","DFSK","Dodge","Emgrand","Faw","Ferrari","Fiat","Ford","Foton","GAC","Gaz","Geely","Gmc","Great Wall","Hafei","Haima","Haval","Hawtai","Honda","Hummer","Hyundai","Infiniti","Isuzu","Jac","Jaguar","Jeep","Jonway","Karry","Kenbo","Keyton","Kia","Lada","Lamborghini","Lancia","Land Rover","Landwind","Lexus","Lifan","Lincoln","Mahindra","Maserati","Mazda","Mercedes","Mercury","Mini","Mitsubishi","Morris Garage","Nissan","Opel","Peugeot","Pontiac","Porsche","Proton","Renault","Saab","Saipa","Scion","Seat","Senova","Skoda","Smart","Sokon","Soueast","Speranza","Ssang Yong","Subaru","Suzuki","Tata","Tesla","Toyota","UFO","Victory","Volkswagen","Volvo","ZNA","Zotye"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);




        newVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  if (TextUtils.isEmpty(newType.getText()) || TextUtils.isEmpty(newModel.getText()))
               //     Toast.makeText(getActivity(), "Cannot submit empty field", Toast.LENGTH_LONG).show();
                //else
                    {
                    String uid = ((CustomerMapActivity) getActivity()).getCustomer().getID();
                    DatabaseReference addVehicle = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(uid).child("Vehicles").child("newVic" + vicCount);
                   // addVehicle.child("type").setValue(newType.getText().toString());
                    //addVehicle.child("model").setValue(newModel.getText().toString());
                    addVehicle = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(uid).child("Profile");
                  //  addVehicle.child("carType").setValue(newType.getText().toString());
                   // addVehicle.child("model").setValue(newModel.getText().toString());
                    vicCount++;
                  //  savedSuccess.setVisibility(View.VISIBLE);
                   // Toast.makeText(getActivity(), String.format("%s has become your default car", newType.getText().toString()), Toast.LENGTH_LONG).show();

/*                        new CountDownTimer(3000, 10) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                               // startActivity(new Intent(AddNewVehicleFragment.this, WelcomeActivity.class));
                            }
                        }.start();

                        */
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    Log.i("Dropdown",""+i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
