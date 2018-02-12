package com.wenshi_egypt.wenshi;

        import android.content.Intent;
        import android.support.annotation.StringRes;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;


        import com.firebase.ui.auth.AuthUI;
        import com.firebase.ui.auth.ErrorCodes;
        import com.firebase.ui.auth.IdpResponse;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.Arrays;

        import javax.xml.validation.Validator;

        import butterknife.BindView;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    boolean customer = false;
    @BindView(R.id.root)
    View mRootView;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bundle extras = getIntent().getExtras();
        customer = extras.getBoolean("customer");

        Log.i("RIDER",""+customer);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, DriverMapsActivity.class));
        }

       else   {

            if (isFacebookMisconfigured()) {
                Log.i("problem with Facebook", "configuration");
            }


            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    //   new AuthUI.IdpConfig.PhoneBuilder().build(),
                                   // new AuthUI.IdpConfig.GoogleBuilder().build(),
                                    new AuthUI.IdpConfig.FacebookBuilder().build(),
                                    new AuthUI.IdpConfig.TwitterBuilder().build()))
                            .build(),
                    RC_SIGN_IN);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                String user= auth.getCurrentUser().getUid();

                if(customer) {
                    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user);
                    currentUser.setValue(true); // to allow changes to happen
                    startActivity(new Intent(LoginActivity.this, CustomerMapActivity.class));
                }else{
                    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user);
                    currentUser.setValue(true); // to allow changes to happen
                    startActivity(new Intent(LoginActivity.this, DriverMapsActivity.class));
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
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}
