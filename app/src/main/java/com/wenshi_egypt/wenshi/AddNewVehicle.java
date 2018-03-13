package com.wenshi_egypt.wenshi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewVehicle extends Fragment implements View.OnClickListener {

    double vicDouble = Math.random() * 1000;
    int vicCount = (int) vicDouble;
    EditText newType, newModel;
    TextView savedSuccess;
    Button newVehicle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.template_add_new_vehicle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //noinspection ConstantConditions
        newType = getActivity().findViewById(R.id.editText_newType);
        newModel = getActivity().findViewById(R.id.editText_newModel);
        savedSuccess = getActivity().findViewById(R.id.textView_vehicle_saved_label);
        newVehicle = getActivity().findViewById(R.id.button_new_vehicle);

        newVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(newType.getText()) || TextUtils.isEmpty(newModel.getText()))
                    Toast.makeText(getActivity(), "Cannot submit empty field", Toast.LENGTH_LONG).show();
                else {
                    String uid = ((CustomerMapActivity) getActivity()).getCustomer().getID();
                    DatabaseReference addVehicle = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(uid).child("Vehicles").child("newVic" + vicCount);
                    addVehicle.child("type").setValue(newType.getText().toString());
                    addVehicle.child("model").setValue(newModel.getText().toString());
                    addVehicle = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(uid).child("Profile");
                    addVehicle.child("carType").setValue(newType.getText().toString());
                    addVehicle.child("model").setValue(newModel.getText().toString());
                    vicCount++;
                    savedSuccess.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), String.format("%s has become your default car", newType.getText().toString()), Toast.LENGTH_LONG).show();

/*                        new CountDownTimer(3000, 10) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                               // startActivity(new Intent(AddNewVehicle.this, WelcomeActivity.class));
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

}
