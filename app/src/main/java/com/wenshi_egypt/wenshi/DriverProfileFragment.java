package com.wenshi_egypt.wenshi;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wenshi_egypt.wenshi.model.UserModel;

import java.util.HashMap;


public class DriverProfileFragment extends Fragment implements View.OnClickListener {

    EditText username, email, mobile, carType,plateNo;

    DatabaseReference rootRef, profRef;
    Button saveButton;
    private UserModel driver;
    boolean nameChanged, emailChanged;

    private DriverProfileFragment.OnFragmentInteractionListener mListener;

    public DriverProfileFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mListener != null) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("Wenshi Driver")
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
        plateNo = getView().findViewById(R.id.editText_driverProfile_PlateNo);

        saveButton = getView().findViewById(R.id.button_driverProfile_saveButton);
        saveButton.setOnClickListener(this);

        getUserData();




        fillDriverData();
        getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.INVISIBLE);
    }

    private void fillDriverData() {
        if (driver != null) {
            username.setText(driver.getName());
            email.setText(driver.getEmail());
            mobile.setText(driver.getMobile());
            plateNo.setText(driver.getDriverPlateNo());
            carType.setText(driver.getDriverCarType());
        }

        if(!mobile.getText().toString().isEmpty())
            mobile.setEnabled(false);
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
        if (view.getId() == R.id.button_driverProfile_saveButton) {

            if (driver != null) {
                nameChanged = false;
                emailChanged = false;
                if (!String.valueOf(plateNo.getText()).isEmpty() &&!String.valueOf(carType.getText()).isEmpty() &&!String.valueOf(mobile.getText()).isEmpty() && !String.valueOf(username.getText()).isEmpty() && !String.valueOf(email.getText()).isEmpty()) {

                    if (!driver.getName().equals(username.getText().toString()) && !username.getText().toString().isEmpty()) {
                        nameChanged = true;
                        getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.VISIBLE);
                        updateUserNameAuth();
                    }
                    if (!driver.getEmail().equals(email.getText().toString()) && !email.getText().toString().isEmpty()) {
                        emailChanged = true;
                        getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.VISIBLE);
                        updateUserEmailAuth();
                    }
                    if (!driver.getName().equals(username.getText().toString()) && !username.getText().toString().isEmpty()) {
                        nameChanged = true;
                        getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.VISIBLE);
                        updateUserNameFire();
                    }
                    if (!driver.getDriverCarType().equals(carType.getText().toString()) && !carType.getText().toString().isEmpty()) {
                        nameChanged = true;
                        getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.VISIBLE);
                        updateCarType();
                    }
                    if (!driver.getDriverPlateNo().equals(plateNo.getText().toString()) && !plateNo.getText().toString().isEmpty()) {
                        nameChanged = true;
                        getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.VISIBLE);
                        updatePlateNo();
                    }
                    if (!driver.getMobile().equals(mobile.getText().toString()) && !mobile.getText().toString().isEmpty()) {
                        nameChanged = true;
                        getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.VISIBLE);
                        updateDriverMobile();
                    }


                     saved();

                } else
                    Toast.makeText(getActivity(), getResources().getString(R.string.fields_cannot_empty), Toast.LENGTH_LONG).show();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getUserData() {
        driver = ((DriverMapsActivity)getActivity()).getDriver();

    }

    private void updateUserEmailFire() {
        try{
            getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.VISIBLE);

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference profModify = rootRef.child("Users").child("Drivers").child(driver.getID()).child("Profile");

            profModify.child("email").setValue(String.valueOf(email.getText()), new DatabaseReference.CompletionListener() {
                public void onComplete(DatabaseError error, DatabaseReference ref) {
                    ((DriverMapsActivity) getActivity()).getDriver().setEmail(String.valueOf(email.getText()));
                    getUserData();
                    Toast.makeText(getActivity(), "Email " + "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                    getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.INVISIBLE);
                    emailChanged = false;
                    saved();

                }
            });
        }catch (Exception e){
            Log.i("ERROR","ProfileFragment"+e.toString());
        }
    }
    private void updateUserEmailAuth() {
        try{
            getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.VISIBLE);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Email " + "Changes Saved For Auth", Toast.LENGTH_SHORT).show();
                        updateUserEmailFire();
                    } else
                        Toast.makeText(getActivity(), "Email Update Failed, Please contact support", Toast.LENGTH_SHORT).show();
                    getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.INVISIBLE);

                }
            });
        }catch (Exception e){
            Log.i("ERROR","ProfileFragment"+e.toString());
        }
    }

    private void saved() {
        // saveButton.setVisibility(View.VISIBLE);
        getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.INVISIBLE);
        if (!emailChanged && !nameChanged) {
            ((DriverMapsActivity) getActivity()).onBackPressed();
        }
    }
    private void updateUserNameAuth() {
        try{
            getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.VISIBLE);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username.getText().toString()).build();


            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        updateUserNameFire();
                    }
                    getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.INVISIBLE);

                }
            });
        }catch (Exception e){
            Log.i("ERROR","ProfileFragment"+e.toString());
        }

    }
    private void updateUserNameFire() {
        try{
            getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.VISIBLE);

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference profModify = rootRef.child("Users").child("Drivers").child(driver.getID()).child("Profile");

            profModify.child("name").setValue(String.valueOf(username.getText()), new DatabaseReference.CompletionListener() {
                public void onComplete(DatabaseError error, DatabaseReference ref) {
                    ((DriverMapsActivity) getActivity()).getDriver().setName(String.valueOf(username.getText()));
                    getUserData();
                    Toast.makeText(getActivity(), "Name " + "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                    getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.INVISIBLE);
                    nameChanged = false;
                    saved();

                }

            });
        }catch (Exception e){
            Log.i("ERROR","ProfileFragment"+e.toString());
        }
    }


    private void updatePlateNo() {
        try{
            getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.VISIBLE);

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference profModify = rootRef.child("Users").child("Drivers").child(driver.getID()).child("Profile");

            profModify.child("plateNo").setValue(String.valueOf(plateNo.getText()), new DatabaseReference.CompletionListener() {
                public void onComplete(DatabaseError error, DatabaseReference ref) {
                    ((DriverMapsActivity) getActivity()).getDriver().setDriverPlateNo(String.valueOf(plateNo.getText()));
                    getUserData();
                    Toast.makeText(getActivity(), "PlatNO " + "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                    getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.INVISIBLE);
                    nameChanged = false;
                    saved();

                }

            });
        }catch (Exception e){
            Log.i("ERROR","ProfileFragment"+e.toString());
        }
    }

    private void updateCarType() {
        try{
            getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.VISIBLE);

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference profModify = rootRef.child("Users").child("Drivers").child(driver.getID()).child("Profile");

            profModify.child("carType").setValue(String.valueOf(carType.getText()), new DatabaseReference.CompletionListener() {
                public void onComplete(DatabaseError error, DatabaseReference ref) {
                    ((DriverMapsActivity) getActivity()).getDriver().setDriverCarType(String.valueOf(carType.getText()));
                    getUserData();
                    Toast.makeText(getActivity(), "carType " + "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                    getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.INVISIBLE);
                    nameChanged = false;
                    saved();

                }

            });
        }catch (Exception e){
            Log.i("ERROR","ProfileFragment"+e.toString());
        }
    }

    private void updateDriverMobile() {
        try{
            getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.VISIBLE);

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference profModify = rootRef.child("Users").child("Drivers").child(driver.getID()).child("Profile");

            profModify.child("mobile").setValue(String.valueOf(mobile.getText()), new DatabaseReference.CompletionListener() {
                public void onComplete(DatabaseError error, DatabaseReference ref) {
                    ((DriverMapsActivity) getActivity()).getDriver().setMobile(String.valueOf(mobile.getText()));
                    getUserData();
                    Toast.makeText(getActivity(), "Mobile " + "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                    getView().findViewById(R.id.progress_wheel_driver_profile).setVisibility(View.INVISIBLE);
                    nameChanged = false;
                    saved();

                }

            });
        }catch (Exception e){
            Log.i("ERROR","ProfileFragment"+e.toString());
        }
    }
}
