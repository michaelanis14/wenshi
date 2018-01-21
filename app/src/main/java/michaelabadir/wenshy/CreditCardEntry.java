package michaelabadir.wenshy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class CreditCardEntry extends AppCompatActivity {

    private static final char space = ' ';
    private static final char slash = '/';
    EditText cardNumber, expDate, cvv, name;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_entry);
        cardNumber = (EditText) findViewById(R.id.editText_CC_page_card_number);
        expDate = (EditText) findViewById(R.id.editText_CC_page_card_date);
        cvv = (EditText) findViewById(R.id.editText_CC_page_card_cvv);
        name = (EditText) findViewById(R.id.editText_CC_page_card_name);
        submit = (Button) findViewById(R.id.button_add_card);
        cardNumber.addTextChangedListener(new cardWatch());
        expDate.addTextChangedListener(new dateWatch());

        addListenerOnButton();

    }


    public void addListenerOnButton() {


        submit = (Button) findViewById(R.id.button_add_card);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(CreditCardEntry.this, cardNumber.getText(), Toast.LENGTH_SHORT).show();
                Toast.makeText(CreditCardEntry.this, expDate.getText(), Toast.LENGTH_SHORT).show();
                Toast.makeText(CreditCardEntry.this, cvv.getText(), Toast.LENGTH_SHORT).show();
                Toast.makeText(CreditCardEntry.this, name.getText(), Toast.LENGTH_SHORT).show();
            }

        });

    }



    class cardWatch implements TextWatcher  {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Remove spacing char
            if (s.length() > 0 && (s.length() % 5) == 0) {
                char c = s.charAt(s.length() - 1);
                if (space == c) {
                    s.delete(s.length() - 1, s.length());
                }
            }
            // Insert char where needed.
            if (s.length() > 0 && (s.length() % 5) == 0) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a space
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }
        }

    }


    class dateWatch implements TextWatcher  {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // Remove slash
            if (s.length() > 0 && (s.length() % 3) == 0) {
                final char c = s.charAt(s.length() - 1);
                if (slash == c) {
                    s.delete(s.length() - 1, s.length());
                }
            }
            // Insert slash
            if (s.length() > 0 && (s.length() % 3) == 0) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a slash we insert a slash
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(slash)).length <= 1) {
                    s.insert(s.length() - 1, String.valueOf(slash));
                }
            }

        }
    }
}

