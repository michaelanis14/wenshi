package com.wenshi_egypt.wenshi;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wenshi_egypt.wenshi.model.UserModel;

import java.util.HashMap;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private String mParam1;
    private String mParam2;
    EditText username, email, mobile, carType, model, address;
    double vicDouble = Math.random()*1000;
    int vicCount = (int) vicDouble;

    DatabaseReference rootRef, profRef;
    Button saveButton;
    private UserModel user;
    private ProfileFragment.OnFragmentInteractionListener mListener;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mListener != null) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http").authority("www.merply.com").appendPath("Winshe").appendPath("types").appendQueryParameter("type", "1").appendQueryParameter("sort", "relevance").fragment("profile");
            mListener.onFragmentInteraction(builder.build());
        }

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        username = getView().findViewById(R.id.editText_profile_userName);
        email = getView().findViewById(R.id.editText_profile_email);
        mobile = getView().findViewById(R.id.editText_profile_mobile);
        carType = getView().findViewById(R.id.editText_profile_carType);
        model = getView().findViewById(R.id.editText_profile_model);
        address = getView().findViewById(R.id.editText_profile_address);
        saveButton = getView().findViewById(R.id.button_profile_saveButton);


        user = ((CustomerMapActivity)getActivity()).getCustomer();
        /*
        if (user != null) {

            Toast.makeText(getActivity(), "User: " + user.getID(), Toast.LENGTH_SHORT).show();

            Toast.makeText(getActivity(), "Username: " + user.getName(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Email: " + user.getEmail(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Mobile: " + user.getMobile(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Vehicle type: " + user.getVehicles(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(), "Vehicle Model: " + user.getName(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Address: " + user.getAddress(), Toast.LENGTH_SHORT).show();


            username.setText(user.getName());
            email.setText(user.getEmail());
            mobile.setText(user.getMobile());
            //carType.setText(user.getVehicle);
            //model.setText(String.format("%s", user.get("model")));
            address.setText(user.getAddress());
        }
        */



        rootRef = FirebaseDatabase.getInstance().getReference();
        profRef = rootRef.child("Users").child("Customers").child(user.getID()).child("FirstConstant");

        profRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                username.setText(value.get("userName"));
                email.setText(value.get("email"));
                mobile.setText(value.get("mobile"));
                carType.setText(value.get("carType"));
                model.setText(String.format("%s", value.get("model")));
                address.setText(value.get("address"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getView().findViewById(R.id.button_profile_saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference profModify = rootRef.child("Users").child("Customers").child(user.getID()).child("FirstConstant");
                profModify.child("userName").setValue(String.valueOf(username.getText()));
                profModify.child("email").setValue(String.valueOf(email.getText()));
                profModify.child("mobile").setValue(String.valueOf(mobile.getText()));
                profModify.child("carType").setValue(String.valueOf(carType.getText()));
                profModify.child("model").setValue(String.valueOf(model.getText()));
                profModify.child("address").setValue(String.valueOf(address.getText()));

//                DatabaseReference profVehicle = rootRef.child("Users").child("Customers").child(user.getID()).child("Vehicles").child("newVic" + vicCount);
//                profVehicle.child("type").setValue(String.valueOf(carType.getText()));
//                profVehicle.child("model").setValue(String.valueOf(model.getText()));
//                profVehicle.child("defaultVehicle").setValue("True");
//                vicCount++;

                Toast.makeText(getActivity(), "Changes Saved Successfully", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HelpFragment.OnFragmentInteractionListener) {
            mListener = (ProfileFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
