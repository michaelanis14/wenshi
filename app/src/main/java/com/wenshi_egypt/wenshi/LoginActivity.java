package com.wenshi_egypt.wenshi;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wenshi_egypt.wenshi.model.UserModel;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    boolean customer = false;
   // @BindView(R.id.root)
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bundle extras = getIntent().getExtras();
        customer = extras.getBoolean("customer");
        Log.i("RIDER", "" + customer);
        auth = FirebaseAuth.getInstance();




        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setTheme(R.style.GreenTheme).setLogo(R.drawable.full_logo).setTosUrl("http://wenshi-egypt.com/tos.html").setPrivacyPolicyUrl("http://wenshi-egypt.com/PrivacyPolicy.html")
                        .setAvailableProviders(Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()

               // new AuthUI.IdpConfig.TwitterBuilder().build()
                )).build(),
                RC_SIGN_IN);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                String name = user.getDisplayName();
                String email = user.getEmail();
                String mobil = user.getPhoneNumber();
                UserModel currenctUserModel = new UserModel(uid, name, email, mobil,4.5);


                if (customer) {
                    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(uid);
                    currentUser.child("Profile").child("FirstConstant").setValue(true);
                    currentUser.child("Trips").child("FirstConstant").setValue(true);
                    currentUser.child("Vehicles").child("FirstConstant").setValue(true);
                    currentUser.child("Family").child("FirstConstant").setValue(true);


                    //currentUser.setValue(true); // to allow changes to happen
                    try {
                        Intent customerIntent = new Intent(LoginActivity.this, CustomerMapActivity.class);
                        customerIntent.putExtra("CurrentUser", currenctUserModel);
                        startActivity(customerIntent);
                    } catch (Exception e) {
                        Log.i("Exception @ Customer", e.toString());
                        Intent customerIntent = new Intent(LoginActivity.this, CustomerMapActivity.class);
                        customerIntent.putExtra("CurrentUser", currenctUserModel);
                        startActivity(customerIntent);
                    }


                } else {


                    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uid);

                    currentUser.child("Profile").child("FirstConstant").setValue(true);
                    currentUser.child("Trips").child("FirstConstant").setValue(true);
                    currentUser.child("Vehicles").child("FirstConstant").setValue(true);
                    currentUser.child("Account").child("FirstConstant").setValue(true);

                    //   currentUser.setValue(true); // to allow changes to happen
                    DatabaseReference reqInit = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uid).child("Requests").child("FirstConstant");
                    reqInit.setValue(true); // to allow changes to happen
                    DatabaseReference currentLocInit = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uid).child("CurrentLocation");
                    currentLocInit.setValue(true); // to allow changes to happen
                    try {

                        Intent driverIntent = new Intent(LoginActivity.this, DriverMapsActivity.class);
                        driverIntent.putExtra("CurrentUser", currenctUserModel);
                        startActivity(driverIntent);
                    } catch (Exception e) {
                        Log.i("Exception @ Driver", e.toString());
                        Intent driverIntent = new Intent(LoginActivity.this, DriverMapsActivity.class);
                        driverIntent.putExtra("CurrentUser", currenctUserModel);
                        startActivity(driverIntent);
                    }
                }
                finish();
                return;

            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error);
                    return;
                }
            }

            showSnackbar(R.string.unknown_sign_in_response);
        }
    }

    private boolean isFacebookMisconfigured() {
        return AuthUI.UNCONFIGURED_CONFIG_VALUE.equals(("FACEBOOK_ERR112"));
    }


    private void showSnackbar(@StringRes int errorMessageRes) {
        if (this != null)
            Toast.makeText(this, errorMessageRes, Toast.LENGTH_LONG).show();
    }

}
