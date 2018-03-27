package com.wenshi_egypt.wenshi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
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

import java.util.Arrays;
import java.util.HashMap;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    EditText username, email, mobile;

    Button saveButton, resetPassword;

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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        username = getView().findViewById(R.id.editText_profile_userName);
        email = getView().findViewById(R.id.editText_profile_email);
        mobile = getView().findViewById(R.id.editText_profile_mobile);

        saveButton = getView().findViewById(R.id.button_profile_saveButton);
        saveButton.setBackgroundColor(Color.BLACK);
        saveButton.setOnClickListener(this);

        resetPassword = getView().findViewById(R.id.reset_password_btn);
        resetPassword.setOnClickListener(this);

        user = ((CustomerMapActivity) getActivity()).getCustomer();

        if (user != null) {
            username.setText(user.getName());
            email.setText(user.getEmail());
            mobile.setText(user.getMobile());
        }
        //username.setEnabled(false);
       // email.setEnabled(false);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileFragment.OnFragmentInteractionListener) {
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
        if (view.getId() == R.id.button_profile_saveButton) {

            if (user != null) {



                if (!String.valueOf(mobile.getText()).isEmpty() && !String.valueOf(username.getText()).isEmpty()) {
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference profModify = rootRef.child("Users").child("Customers").child(user.getID()).child("Profile");
                    if (!user.getName().equals(username.getText()))
                        profModify.child("name").setValue(String.valueOf(username.getText()), new DatabaseReference.CompletionListener() {
                            public void onComplete(DatabaseError error, DatabaseReference ref) {
                                ((CustomerMapActivity) getActivity()).getCustomer().setName(String.valueOf(username.getText()));
                                Toast.makeText(getActivity(), "Name " + "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });

                    if (!user.getEmail().equals(email.getText()))
                        profModify.child("email").setValue(String.valueOf(email.getText()), new DatabaseReference.CompletionListener() {
                            public void onComplete(DatabaseError error, DatabaseReference ref) {
                                ((CustomerMapActivity) getActivity()).getCustomer().setEmail(String.valueOf(email.getText()));
                                setFirbaseUSerEmail();

                            }
                        });
                    if (!user.getMobile().equals(mobile.getText()))
                        profModify.child("mobile").setValue(String.valueOf(mobile.getText()), new DatabaseReference.CompletionListener() {
                            public void onComplete(DatabaseError error, DatabaseReference ref) {
                                ((CustomerMapActivity) getActivity()).getCustomer().setMobile(String.valueOf(username.getText()));
                                verifyPhoneNumber();
                                Toast.makeText(getActivity(), "Mobile " + "Changes Saved Successfully", Toast.LENGTH_SHORT).show();

                            }
                        });




                } else
                    Toast.makeText(getActivity(), getResources().getString(R.string.fields_cannot_empty), Toast.LENGTH_LONG).show();
            }
        }

        if (view.getId() == R.id.reset_password_btn) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public void setFirbaseUSerEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail( ((CustomerMapActivity) getActivity()).getCustomer().getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Email " + "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
public void verifyPhoneNumber(){
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build())).build(), 1000);

}

    //  @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
