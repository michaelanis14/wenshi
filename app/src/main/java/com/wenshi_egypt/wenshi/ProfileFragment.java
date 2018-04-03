package com.wenshi_egypt.wenshi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wenshi_egypt.wenshi.model.UserModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    EditText username, email, mobile, codeVerfy;
    Button saveButton, resetPassword, sendCode, verifyCode, resendCode;
    String mVerificationId;
    private UserModel user;
    private ProfileFragment.OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


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

        codeVerfy = getView().findViewById(R.id.editText_code_mobile);
        sendCode = getView().findViewById(R.id.send_verify_btn);
        sendCode.setOnClickListener(this);

        verifyCode = getView().findViewById(R.id.verify_btn);
        verifyCode.setOnClickListener(this);

        resendCode = getView().findViewById(R.id.resend_verify_btn);
        resendCode.setOnClickListener(this);


        //  verifyLayout = getView().findViewById(R.id.verify_layout);
        mAuth = FirebaseAuth.getInstance();

        getUserData();
        fillUserData();
        //username.setEnabled(false);
        // email.setEnabled(false);

        hidePhoneAuth();


        mobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (user != null) {
                    if (!mobile.getText().toString().equals(user.getMobile())) {
                        sendCode.setVisibility(View.VISIBLE);
                    } else {
                        sendCode.setVisibility(View.GONE);
                        mobile.setEnabled(true);
                        codeVerfy.setVisibility(View.GONE);
                        verifyCode.setVisibility(View.GONE);
                        resendCode.setVisibility(View.GONE);
                        getView().findViewById(R.id.textView_profile_Code_label).setVisibility(View.GONE);


                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (user != null) {
                    if (!mobile.getText().toString().equals(user.getMobile())) {
                        sendCode.setVisibility(View.VISIBLE);
                    } else {
                        sendCode.setVisibility(View.GONE);
                        mobile.setEnabled(true);
                        codeVerfy.setVisibility(View.GONE);
                        verifyCode.setVisibility(View.GONE);
                        resendCode.setVisibility(View.GONE);
                        getView().findViewById(R.id.textView_profile_Code_label).setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (user != null) {
                    if (!mobile.getText().toString().equals(user.getMobile())) {
                        sendCode.setVisibility(View.VISIBLE);
                    } else {
                        sendCode.setVisibility(View.GONE);
                        mobile.setEnabled(true);
                        codeVerfy.setVisibility(View.GONE);
                        verifyCode.setVisibility(View.GONE);
                        resendCode.setVisibility(View.GONE);
                        getView().findViewById(R.id.textView_profile_Code_label).setVisibility(View.GONE);

                    }
                }
            }
        });
        getView().findViewById(R.id.progress_wheel_profile).setVisibility(View.INVISIBLE);


        if (user != null) {
            getUserData();
            fillUserData();
        }
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

    private void getUserData() {
        user = ((CustomerMapActivity) getActivity()).getCustomer();

    }

    private void fillUserData() {
        if (user != null) {
            username.setText(user.getName());
            email.setText(user.getEmail());
            mobile.setText(user.getMobile());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_profile_saveButton) {

            if (user != null) {
                if (!String.valueOf(mobile.getText()).isEmpty() && !String.valueOf(username.getText()).isEmpty() && !String.valueOf(email.getText()).isEmpty()) {

                    if (!user.getName().equals(username.getText().toString())  && !username.getText().toString().isEmpty()) {
                        updateUserNameAuth();
                    }
                    if (!user.getEmail().equals(email.getText().toString()) && !email.getText().toString().isEmpty()) {
                        updateUserEmailAuth();
                    }
                    if( ((CustomerMapActivity) getActivity()).getCURRENTSTATE() ==  ((CustomerMapActivity) getActivity()).INCOMPLETEPROFIE)
                        ((CustomerMapActivity) getActivity()).setCURRENTSTATE(((CustomerMapActivity) getActivity()).PICKUP);
                    ((CustomerMapActivity) getActivity()).onBackPressed();


                } else
                    Toast.makeText(getActivity(), getResources().getString(R.string.fields_cannot_empty), Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.reset_password_btn) {
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
        } else if (view.getId() == R.id.send_verify_btn) {
            verifyPhoneNumber();
            getOtp(String.valueOf(mobile.getText()));
            sendCode.setVisibility(View.GONE);
            mobile.setEnabled(false);
            resendCode.setVisibility(View.VISIBLE);


        } else if (view.getId() == R.id.verify_btn) {
            clickVerifyPhone();
        } else if (view.getId() == R.id.resend_verify_btn) {
            clickResend();
        }


    }


    private void hidePhoneAuth() {
        getView().findViewById(R.id.textView_profile_Code_label).setVisibility(View.GONE);
        mobile.setEnabled(true);
        codeVerfy.setVisibility(View.GONE);
        verifyCode.setVisibility(View.GONE);
        resendCode.setVisibility(View.GONE);
        sendCode.setVisibility(View.GONE);
    }

    private void verifyPhoneNumber() {

        if (mCallbacks == null)
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    Log.d("", "onVerificationCompleted:" + credential);


                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Log.w("", "onVerificationFailed", e);
                    mobile.setEnabled(true);

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.wrong_phone_number), Toast.LENGTH_LONG).show();
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        Toast.makeText(getActivity(), "Too Many Requests !", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                    Log.d("", "onCodeSent:" + verificationId);
                    mVerificationId = verificationId;
                    mResendToken = token;
                    Toast.makeText(getActivity(), "onCodeSent:", Toast.LENGTH_SHORT).show();
                    getView().findViewById(R.id.textView_profile_Code_label).setVisibility(View.VISIBLE);
                    codeVerfy.setVisibility(View.VISIBLE);
                    verifyCode.setVisibility(View.VISIBLE);
                    mobile.setEnabled(false);

                }
            };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference profModify = rootRef.child("Users").child("Customers").child(user.getID()).child("Profile");
                    profModify.child("mobile").setValue(String.valueOf(mobile.getText()), new DatabaseReference.CompletionListener() {
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            ((CustomerMapActivity) getActivity()).getCustomer().setMobile(String.valueOf(mobile.getText()));
                            getUserData();
                            hidePhoneAuth();
                            Toast.makeText(getActivity(), "Mobile " + "Changes Saved Successfully", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                }

                // ...
            }
        });
        ;
        /*
        mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TAGG", "signInWithCredential:success");
                    FirebaseUser user = task.getResult().getUser();
                    // startActivity(new Intent(PhoneAuthActivity.this, HomeActivity.class).putExtra("phone", user.getPhoneNumber()));
                    //   finish();
                    Toast.makeText(getActivity(), "DONEEE", Toast.LENGTH_LONG).show();

                } else {
                    // Log.w(TAG, "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(getActivity(), "Invalid code.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        */
    }


    private void getOtp(String phoneNumber) {
        Log.i("Auth", "002" + phoneNumber);
        PhoneAuthProvider.getInstance().verifyPhoneNumber("002" + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        if (verificationId != null && code != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        } else Toast.makeText(getActivity(), "NULL NULL", Toast.LENGTH_LONG).show();

    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }


    // @OnClick(R.id.button_verify_phone)
    public void clickVerifyPhone() {
        String code = codeVerfy.getText().toString();
        if (TextUtils.isEmpty(code)) {
            codeVerfy.setError("Cannot be empty.");
            return;
        } else verifyPhoneNumberWithCode(mVerificationId, code);
    }


    public void clickResend() {
        resendVerificationCode("002" + mobile.getText().toString(), mResendToken);
    }

    private void updateUserNameFire() {
        getView().findViewById(R.id.progress_wheel_profile).setVisibility(View.VISIBLE);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference profModify = rootRef.child("Users").child("Customers").child(user.getID()).child("Profile");

        profModify.child("name").setValue(String.valueOf(username.getText()), new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                ((CustomerMapActivity) getActivity()).getCustomer().setName(String.valueOf(username.getText()));
                getUserData();
                Toast.makeText(getActivity(), "Name " + "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                getView().findViewById(R.id.progress_wheel_profile).setVisibility(View.INVISIBLE);

            }

        });
    }

    private void updateUserEmailFire() {
        getView().findViewById(R.id.progress_wheel_profile).setVisibility(View.VISIBLE);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference profModify = rootRef.child("Users").child("Customers").child(user.getID()).child("Profile");

        profModify.child("email").setValue(String.valueOf(email.getText()), new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                ((CustomerMapActivity) getActivity()).getCustomer().setEmail(String.valueOf(email.getText()));
                getUserData();
                Toast.makeText(getActivity(), "Email " + "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                getView().findViewById(R.id.progress_wheel_profile).setVisibility(View.INVISIBLE);

            }
        });
    }

    private void updateUserNameAuth() {
        getView().findViewById(R.id.progress_wheel_profile).setVisibility(View.VISIBLE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username.getText().toString()).build();


        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    updateUserNameFire();
                }
                getView().findViewById(R.id.progress_wheel_profile).setVisibility(View.INVISIBLE);

            }
        });

    }

    //  @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void updateUserEmailAuth() {
        getView().findViewById(R.id.progress_wheel_profile).setVisibility(View.VISIBLE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Email " + "Changes Saved For Auth", Toast.LENGTH_SHORT).show();
                    updateUserEmailFire();
                } else
                    Toast.makeText(getActivity(), "Email Update Failed, Please contact support", Toast.LENGTH_SHORT).show();
                getView().findViewById(R.id.progress_wheel_profile).setVisibility(View.INVISIBLE);

            }
        });

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
