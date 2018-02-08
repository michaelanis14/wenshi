package com.wenshi_egypt.wenshi;


        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;



public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        // close splash activity


        Log.i("helpppp", "onCreate: ");

        finish();
    }
}
