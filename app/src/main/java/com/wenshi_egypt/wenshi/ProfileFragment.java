package com.wenshi_egypt.wenshi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wenshi_egypt.wenshi.model.UserModel;

import java.util.HashMap;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    EditText username, email, mobile, carType, model, address;

    DatabaseReference rootRef, profRef;
    Button saveButton;
    ImageButton profAddVehcile;
    private UserModel user;
    private ProfileFragment.OnFragmentInteractionListener mListener;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mListener != null) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http").authority("www.merply.com").appendPath("Winshe").appendPath("types").appendQueryParameter("type", "1").appendQueryParameter("sort", "relevance").fragment("profile");
            mListener.onFragmentInteraction(builder.build());
        }

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        username = getView().findViewById(R.id.editText_profile_userName);
        email = getView().findViewById(R.id.editText_profile_email);
        mobile = getView().findViewById(R.id.editText_profile_mobile);
        carType = getView().findViewById(R.id.editText_profile_carType);
        model = getView().findViewById(R.id.editText_profile_model);
        address = getView().findViewById(R.id.editText_profile_address);
        saveButton = getView().findViewById(R.id.button_profile_saveButton);
        profAddVehcile = getView().findViewById(R.id.button_profile_addVehicle);

        user = ((CustomerMapActivity)getActivity()).getCustomer();

        if (user != null) {
            username.setText(user.getName());
            email.setText(user.getEmail());
            //mobile.setText(user.getProfData(user.getID(), true).get(0));
            //carType.setText(user.getVehicle);
            //model.setText(String.format("%s", user.get("model")));
            //address.setText(user.getAddress());
        }
        username.setEnabled(false);
        email.setEnabled(false);
        carType.setEnabled(false);
        model.setEnabled(false);


        rootRef = FirebaseDatabase.getInstance().getReference();
        this.profRef = rootRef.child("Users").child("Customers").child(user.getID()).child("Profile");

        profRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                @SuppressWarnings("unchecked") HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                mobile.setText(value.get("mobile"));
                carType.setText(value.get("carType"));
                model.setText(value.get("model"));
                address.setText(value.get("address"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        profAddVehcile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddNewVehicle.class);
                Bundle b = new Bundle();
                b.putString("uid", ((CustomerMapActivity) getActivity()).getCustomer().getID());
                intent.putExtras(b);
                startActivity(intent);

            }
        });
    /*

        profRef = rootRef.child("Users").child("Customers").child(user.getID());

        profRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                @SuppressWarnings("unchecked") HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                username.setText(value.get("userName"));
                email.setText(value.get("email"));
                mobile.setText(value.get("mobile"));
                carType.setText(value.get("carType"));
                model.setText(value.get("model"));
                address.setText(value.get("address"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */

        rootRef = FirebaseDatabase.getInstance().getReference();
        getView().findViewById(R.id.button_profile_saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!String.valueOf(mobile.getText()).isEmpty() &&
                   !String.valueOf(carType.getText()).isEmpty() &&
                   !String.valueOf(model.getText()).isEmpty() &&
                   !String.valueOf(address.getText()).isEmpty()) {
                        DatabaseReference profModify = rootRef.child("Users").child("Customers").child(user.getID()).child("Profile");
                        profModify.child("mobile").setValue(String.valueOf(mobile.getText()));
                        profModify.child("carType").setValue(String.valueOf(carType.getText()));
                        profModify.child("model").setValue(String.valueOf(model.getText()));
                        profModify.child("address").setValue(String.valueOf(address.getText()));
                        Toast.makeText(getActivity(), "Changes Saved Successfully", Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(getActivity(), "Please fill all details", Toast.LENGTH_LONG).show();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
