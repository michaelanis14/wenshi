package michaelabadir.wenshi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PaymentOptions extends Fragment {

    private RadioButton rb1;
    private TextView resultText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_options, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //noinspection ConstantConditions
        RadioGroup radioGroup = getView().findViewById(R.id.radiogroup);
        radioGroup.check(R.id.radioButton_payment_cash);

        addListenerOnButton();
    }

    public void addListenerOnButton() {

        //noinspection ConstantConditions
        Button btnDisplay = getView().findViewById(R.id.button_payment_submit);
        rb1 = getView().findViewById(R.id.radioButton_payment_cash);

        btnDisplay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(rb1.isChecked()) {
                    resultText = getView().findViewById(R.id.textView_payment_result);
                    resultText.setText(rb1.getText());
                }
                else    {
                    Intent ccEntry = new Intent(getActivity().getApplicationContext(), CreditCardEntry.class);
                    startActivityForResult(ccEntry, 0);
                }
            }

        });

    }
}
