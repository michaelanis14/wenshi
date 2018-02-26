package com.wenshi_egypt.wenshi;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.paymob.acceptsdk.IntentConstants;
import com.paymob.acceptsdk.PayActivity;
import com.paymob.acceptsdk.PayActivityIntentKeys;
import com.paymob.acceptsdk.PayResponseKeys;
import com.paymob.acceptsdk.SaveCardResponseKeys;
import com.paymob.acceptsdk.ToastMaker;

public class CreditCardEntry extends AppCompatActivity implements View.OnClickListener {

    Button button1;
    Button button2;
    Button button3;

    // Arbitrary number and used only in this activity. Change it as you wish.
    static final int ACCEPT_PAYMENT_REQUEST = 10;

    // Replace this with your actual payment key
    final String paymentKey = "'ZXlKaGJHY2lPaUpJVXpVeE1pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmhiVzkxYm5SZlkyVnVkSE1pT2pJMU1EQXdMQ0p2Y21SbGNsOXBaQ0k2TVRrME9Ua3lMQ0pqZFhKeVpXNWplU0k2SWtWSFVDSXNJbUpwYkd4cGJtZGZaR0YwWVNJNmV5Sm1hWEp6ZEY5dVlXMWxJam9pVjJWdWMyZ2lMQ0pzWVhOMFgyNWhiV1VpT2lKVVpYTjBYMWRsYm5Ob0lpd2ljM1J5WldWMElqb2lSWFJvWVc0Z1RHRnVaQ0lzSW1KMWFXeGthVzVuSWpvaU9EQXlPQ0lzSW1ac2IyOXlJam9pTkRJaUxDSmhjR0Z5ZEcxbGJuUWlPaUk0TURNaUxDSmphWFI1SWpvaVNtRnphMjlzYzJ0cFluVnlaMmdpTENKemRHRjBaU0k2SWxWMFlXZ2lMQ0pqYjNWdWRISjVJam9pUTFJaUxDSmxiV0ZwYkNJNkltMXBZMmhoWld3dVlXSmhaR2x5UUcxdlpYSnBZM011WTI5dElpd2ljR2h2Ym1WZmJuVnRZbVZ5SWpvaUt6SXdNVEF4T1Rrek1qUTROQ0lzSW5CdmMzUmhiRjlqYjJSbElqb2lNREU0T1RnaUxDSmxlSFJ5WVY5a1pYTmpjbWx3ZEdsdmJpSTZJazVCSW4wc0luVnpaWEpmYVdRaU9qWTJOaXdpWTJGeVpGOXBiblJsWjNKaGRHbHZibDlwWkNJNk56a3dmUS5EMFNoQXdGLXlUVVFzMG1VQXltQTlaZ3lnbTlYcVlYVmZjVXNlSW55dmtGM0dPeUpfd3R6S3hkek01Ym9PakVPLWNDTEJZbzN2bVZxa1ZVZVUzcm1oZw==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_paymob);
//
//        button1 = findViewById(R.id.Button1_paymob);
//        button1.setOnClickListener(this);
//        button2 = findViewById(R.id.Button2_paymob);
//        button2.setOnClickListener(this);
//        button3 = findViewById(R.id.Button3_paymob);
//        button3.setOnClickListener(this);
        startPayActivityNoToken(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Button1_paymob:
                startPayActivityNoToken(true);
                break;
            case R.id.Button2_paymob:
                startPayActivityNoToken(false);
                break;
            case R.id.Button3_paymob:
                startPayActivityToken();
                break;
        }
    }

    private void startPayActivityNoToken(Boolean showSaveCard) {
        Intent pay_intent = new Intent(this, PayActivity.class);

        putNormalExtras(pay_intent);
        pay_intent.putExtra(PayActivityIntentKeys.SAVE_CARD_DEFAULT, true);
        pay_intent.putExtra(PayActivityIntentKeys.SHOW_ALERTS, showSaveCard);
        pay_intent.putExtra(PayActivityIntentKeys.SHOW_SAVE_CARD, showSaveCard);
        pay_intent.putExtra(PayActivityIntentKeys.THEME_COLOR, 0x8033B5E5);

        startActivityForResult(pay_intent, ACCEPT_PAYMENT_REQUEST);
    }

    private void startPayActivityToken() {
        Intent pay_intent = new Intent(this, PayActivity.class);

        putNormalExtras(pay_intent);
        // replace this with your actual card token
        pay_intent.putExtra(PayActivityIntentKeys.TOKEN, "token");
        pay_intent.putExtra(PayActivityIntentKeys.MASKED_PAN_NUMBER, "xxxx-xxxx-xxxx-1234");
        pay_intent.putExtra(PayActivityIntentKeys.SAVE_CARD_DEFAULT, false);
        pay_intent.putExtra(PayActivityIntentKeys.SHOW_ALERTS, true);
        pay_intent.putExtra(PayActivityIntentKeys.SHOW_SAVE_CARD, false);

        startActivityForResult(pay_intent, ACCEPT_PAYMENT_REQUEST);
    }

    private void putNormalExtras(Intent intent) {
        // Pass the correct values for the billing data keys
        intent.putExtra(PayActivityIntentKeys.FIRST_NAME, "firsy_name");
        intent.putExtra(PayActivityIntentKeys.LAST_NAME, "last_name");
        intent.putExtra(PayActivityIntentKeys.BUILDING, "1");
        intent.putExtra(PayActivityIntentKeys.FLOOR, "1");
        intent.putExtra(PayActivityIntentKeys.APARTMENT, "1");
        intent.putExtra(PayActivityIntentKeys.CITY, "cairo");
        intent.putExtra(PayActivityIntentKeys.STATE, "new_cairo");
        intent.putExtra(PayActivityIntentKeys.COUNTRY, "egypt");
        intent.putExtra(PayActivityIntentKeys.EMAIL, "email@gmail.com");
        intent.putExtra(PayActivityIntentKeys.PHONE_NUMBER, "2345678");
        intent.putExtra(PayActivityIntentKeys.POSTAL_CODE, "3456");

        intent.putExtra(PayActivityIntentKeys.PAYMENT_KEY, paymentKey);

        intent.putExtra(PayActivityIntentKeys.THREE_D_SECURE_ACTIVITY_TITLE, "Verification");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = data.getExtras();

        if (requestCode == ACCEPT_PAYMENT_REQUEST) {

            if (resultCode == IntentConstants.USER_CANCELED) {
                // User canceled and did no payment request was fired
                ToastMaker.displayShortToast(this, "User canceled!!");
            } else if (resultCode == IntentConstants.MISSING_ARGUMENT) {
                // You forgot to pass an important key-value pair in the intent's extras
                ToastMaker.displayShortToast(this, "Missing Argument == " + extras.getString(IntentConstants.MISSING_ARGUMENT_VALUE));
            } else if (resultCode == IntentConstants.TRANSACTION_ERROR) {
                // An error occurred while handling an API's response
                ToastMaker.displayShortToast(this, "Reason == " + extras.getString(IntentConstants.TRANSACTION_ERROR_REASON));
            } else if (resultCode == IntentConstants.TRANSACTION_REJECTED) {
                // User attempted to pay but their transaction was rejected

                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(this, extras.getString(PayResponseKeys.DATA_MESSAGE));
            } else if (resultCode == IntentConstants.TRANSACTION_REJECTED_PARSING_ISSUE) {
                // User attempted to pay but their transaction was rejected. An error occured while reading the returned JSON
                ToastMaker.displayShortToast(this, extras.getString(IntentConstants.RAW_PAY_RESPONSE));
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL) {
                // User finished their payment successfully

                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(this, extras.getString(PayResponseKeys.DATA_MESSAGE));
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL_PARSING_ISSUE) {
                // User finished their payment successfully. An error occured while reading the returned JSON.
                ToastMaker.displayShortToast(this, "TRANSACTION_SUCCESSFUL - Parsing Issue");

                // ToastMaker.displayShortToast(this, extras.getString(IntentConstants.RAW_PAY_RESPONSE));
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL_CARD_SAVED) {
                // User finished their payment successfully and card was saved.

                // Use the static keys declared in PayResponseKeys to extract the fields you want
                // Use the static keys declared in SaveCardResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(this, "Token == " + extras.getString(SaveCardResponseKeys.TOKEN));
            } else if (resultCode == IntentConstants.USER_CANCELED_3D_SECURE_VERIFICATION) {
                ToastMaker.displayShortToast(this, "User canceled 3-d scure verification!!");

                // Note that a payment process was attempted. You can extract the original returned values
                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(this, extras.getString(PayResponseKeys.PENDING));
            } else if (resultCode == IntentConstants.USER_CANCELED_3D_SECURE_VERIFICATION_PARSING_ISSUE) {
                ToastMaker.displayShortToast(this, "User canceled 3-d scure verification - Parsing Issue!!");

                // Note that a payment process was attempted.
                // User finished their payment successfully. An error occured while reading the returned JSON.
                ToastMaker.displayShortToast(this, extras.getString(IntentConstants.RAW_PAY_RESPONSE));
            }
        }
    }
}


/*
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

        return mon > 0 && mon < 13 && year > 17 & sl == '/';
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
*/