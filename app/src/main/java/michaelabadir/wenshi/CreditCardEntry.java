package michaelabadir.wenshi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreditCardEntry extends AppCompatActivity {

    private static final char space = ' ';
    private static final char slash = '/';
    private static final char zero = '0';
    EditText cardNumber, expDate, cvv, name;
    TextView cardNumberError, expDateError, cvvError, nameError;
    Button submit;
    boolean numFlag, dateFlag, cvvFlag, nameFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_entry);

        cardNumber = findViewById(R.id.editText_CC_entry_card_number);
        expDate = findViewById(R.id.editText_CC_entry_card_date);
        cvv = findViewById(R.id.editText_CC_entry_card_cvv);
        name = findViewById(R.id.editText_CC_entry_card_name);
        submit = findViewById(R.id.button_add_card);

        cardNumberError = findViewById(R.id.textView_CC_entry_numberError);
        expDateError = findViewById(R.id.textView_CC_entry_dateError);
        cvvError = findViewById(R.id.textView_CC_entry_cvvError);
        nameError = findViewById(R.id.textView_CC_entry_nameError);

        cardNumber.addTextChangedListener(new cardWatch());
        expDate.addTextChangedListener(new dateWatch());

        addListenerOnButton();

    }

    public void addListenerOnButton() {


        submit = findViewById(R.id.button_add_card);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(cardNumber.getText().length() < 19) {
                    cardNumberError.setVisibility(View.VISIBLE);
                    numFlag = false;
                }
                else    {
                    cardNumberError.setVisibility(View.INVISIBLE);
                    numFlag = true;
                }
                if(expDate.getText().length() < 5 || !checkDate(expDate.getText().toString())) {
                    expDateError.setVisibility(View.VISIBLE);
                    dateFlag = false;
                }
                else    {
                    expDateError.setVisibility(View.INVISIBLE);
                    dateFlag = true;
                }
                if(cvv.getText().length() < 3) {
                    cvvError.setVisibility(View.VISIBLE);
                    cvvFlag = false;
                }
                else    {
                    cvvError.setVisibility(View.INVISIBLE);
                    cvvFlag = true;
                }
                if(name.getText().length() < 8) {
                    nameError.setVisibility(View.VISIBLE);
                    nameFlag = false;
                }
                else    {
                    nameError.setVisibility(View.INVISIBLE);
                    nameFlag = true;
                }

                if(numFlag && dateFlag && cvvFlag & nameFlag) {
                    Toast.makeText(CreditCardEntry.this, cardNumber.getText(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(CreditCardEntry.this, expDate.getText(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(CreditCardEntry.this, cvv.getText(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(CreditCardEntry.this, name.getText(), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public boolean checkDate(String s) {
        int mon = Integer.parseInt(s.substring(0,2));
        int year = Integer.parseInt(s.substring(3,5));
        char sl = s.charAt(2);

        if(mon > 0 && mon < 13 && year > 17 & sl == '/') {
            return true;
        }
        return false;
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
            // Insert zero
//            if(s.length()==1) {
//                char c = s.charAt(s.length() - 1);
//                if (Character.isDigit(c) && (TextUtils.split(s.toString(), String.valueOf(zero)).length <= 1))
//                    s.insert(s.length() - 1, String.valueOf(zero));
//            }

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
