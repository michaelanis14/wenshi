package com.wenshi_egypt.wenshi;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wenshi_egypt.wenshi.model.HistoryModel;
import com.wenshi_egypt.wenshi.model.UserModel;
import com.wenshi_egypt.wenshi.model.VehicleModel;

import java.util.ArrayList;
import java.util.Map;

public class CustomerHistoryFragment extends Fragment implements View.OnClickListener {

    public CustomerHistoryFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_customer, container, false);

    }

    @Override
    public void onClick(View view) {

        if (((CustomerMapActivity) getActivity()).getHistoryDetailsFragment() == null)
            ((CustomerMapActivity) getActivity()).setHistoryDetailsFragment(new HistoryDetailsFragment());
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

        Bundle args = new Bundle();
        args.putParcelable("DATA", ((HistoryModel) ((CustomerMapActivity) getActivity()).getCustomer().getHistory().values().toArray()[view.getId()]));
        ((CustomerMapActivity) getActivity()).getHistoryDetailsFragment().setArguments(args);

        ft.replace(R.id.mainFrame, ((CustomerMapActivity) getActivity()).getHistoryDetailsFragment());
        ft.commit();
    }

    @Override
    public void onStart() {
        super.onStart();

        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.history_list_layout);
        UserModel user = ((CustomerMapActivity) getActivity()).getCustomer();

        if (user.getHistory() == null || user.getHistory().size() == 0) {
            //  addNewVehicle(0);
            getView().findViewById(R.id.noHistory).setVisibility(View.VISIBLE);

        } else {

            try {

                getView().findViewById(R.id.noHistory).setVisibility(View.GONE);
                int i = 0;
                for (Map.Entry<String, HistoryModel> historyItem : user.getHistory().entrySet()) {
                    Button button = (Button) getLayoutInflater().inflate(R.layout.list_button, null);
                    button.setText(historyItem.getValue().getStartTime() + "  -  " + historyItem.getValue().getCost() + " LE");
                    button.setId(i);
                    button.setOnClickListener(this);
                    layout.addView(button);
                    i++;
                }

            } catch (Exception e) {
                Log.i("DATABASE ERR", "Bad Structure" + e.toString());
            }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}