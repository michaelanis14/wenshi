package com.wenshi_egypt.wenshi;
import android.content.Context;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wenshi_egypt.wenshi.model.UserModel;

import java.util.HashMap;


public class DriverProfileFragment extends Fragment implements View.OnClickListener {

    EditText username, email, mobile, carType;

    DatabaseReference rootRef, profRef;
    Button saveButton;
    private UserModel driver;
    private DriverProfileFragment.OnFragmentInteractionListener mListener;

    public DriverProfileFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mListener != null) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("www.merply.com")
                    .appendPath("winshy")
                    .appendPath("types")
                    .appendQueryParameter("type", "1")
                    .appendQueryParameter("sort", "relevance")
                    .fragment("profile");
            mListener.onFragmentInteraction(builder.build());
        }

        return inflater.inflate(R.layout.fragment_driver_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //noinspection ConstantConditions
        username = getView().findViewById(R.id.editText_driverProfile_userName);
        email = getView().findViewById(R.id.editText_driverProfile_email);
        mobile = getView().findViewById(R.id.editText_driverProfile_mobile);
        carType = getView().findViewById(R.id.editText_driverProfile_carType);
        saveButton = getView().findViewById(R.id.button_driverProfile_saveButton);
        assert getActivity() != null;
        driver = ((DriverMapsActivity)getActivity()).getDriver();


        rootRef = FirebaseDatabase.getInstance().getReference();
    //    Toast.makeText(getActivity(), "ID: " + driver.getID(), Toast.LENGTH_LONG).show();
        profRef = rootRef.child("Users").child("Drivers").child(driver.getID());

        profRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //noinspection unchecked
                HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                assert value != null;
                username.setText(value.get("userName"));
                email.setText(value.get("email"));
                mobile.setText(value.get("mobile"));
                carType.setText(value.get("carType"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getView().findViewById(R.id.button_driverProfile_saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!String.valueOf(username.getText()).isEmpty() &&
                        !String.valueOf(email.getText()).isEmpty() &&
                        !String.valueOf(mobile.getText()).isEmpty() &&
                        !String.valueOf(carType.getText()).isEmpty()) {
                            DatabaseReference profModify = rootRef.child("Users").child("Drivers").child(driver.getID());
                            profModify.child("userName").setValue(String.valueOf(username.getText()));
                            profModify.child("email").setValue(String.valueOf(email.getText()));
                            profModify.child("mobile").setValue(String.valueOf(mobile.getText()));
                            profModify.child("carType").setValue(String.valueOf(carType.getText()));
                            Toast.makeText(getActivity(), "Changes Saved Successfully", Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(getActivity(), "Please fill all details", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DriverProfileFragment.OnFragmentInteractionListener) {
            mListener = (DriverProfileFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
