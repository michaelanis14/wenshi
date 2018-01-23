package michaelabadir.wenshy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PaymentOptions extends AppCompatActivity {

    private RadioButton rb1;
    private TextView resultText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);
        RadioGroup radioGroup = findViewById(R.id.radiogroup);
        radioGroup.check(R.id.radioButton_payment_cash);

        addListenerOnButton();

    }

    public void addListenerOnButton() {


        Button btnDisplay = findViewById(R.id.button_payment_submit);

        rb1 = findViewById(R.id.radioButton_payment_cash);

        btnDisplay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
             //   int selectedId = radioGroup.getCheckedRadioButtonId();
             //   radioButton = (RadioButton) findViewById(selectedId);
             //   Toast.makeText(PaymentOptions.this, "Hello From the other sideeee!!!", Toast.LENGTH_SHORT).show();
                if(rb1.isChecked()) {
                    resultText = findViewById(R.id.textView_payment_result);
                    resultText.setText(rb1.getText());
                }
                else    {
                    Intent ccEntry = new Intent(getApplicationContext(), CreditCardEntry.class);
                    startActivityForResult(ccEntry, 0);

                }
            }

        });

    }
}
