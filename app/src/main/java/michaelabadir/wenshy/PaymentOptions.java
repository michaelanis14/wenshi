package michaelabadir.wenshy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentOptions extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnDisplay;
    private TextView resultText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.check(R.id.radioButton_payment_cash);

        addListenerOnButton();

    }

    public void addListenerOnButton() {


        btnDisplay = (Button) findViewById(R.id.button_payment_submit);

        btnDisplay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
             //   Toast.makeText(PaymentOptions.this,
             //           "Hello From the other sideeee!!!", Toast.LENGTH_SHORT).show();
                resultText = (TextView)findViewById(R.id.textView_payment_result);
                resultText.setText(radioButton.getText());
            }

        });

    }
}
