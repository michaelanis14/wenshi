package com.wenshi_egypt.wenshi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wenshi_egypt.wenshi.model.HistoryModel;
import com.wenshi_egypt.wenshi.model.UserModel;

import java.util.Map;


public class DriverHistoryFragment extends Fragment implements View.OnClickListener{

    public DriverHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_history, container, false);
    }


    @Override
    public void onClick(View view) {

        if (((DriverMapsActivity) getActivity()).getHistoryDetailsFragment() == null)
            ((DriverMapsActivity) getActivity()).setHistoryDetailsFragment(new HistoryDetailsFragment());
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

        Bundle args = new Bundle();
        args.putParcelable("DATA", ((HistoryModel) ((DriverMapsActivity) getActivity()).getDriver().getHistory().values().toArray()[view.getId()]));
        ((DriverMapsActivity) getActivity()).getHistoryDetailsFragment().setArguments(args);

        ft.replace(R.id.mainFrame, ((DriverMapsActivity) getActivity()).getHistoryDetailsFragment());
        ft.commit();
    }
    @Override
    public void onStart() {
        super.onStart();

        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.history_driver_list_layout);
        UserModel user = ((DriverMapsActivity) getActivity()).getDriver();

        if (user.getHistory() == null || user.getHistory().size() == 0) {
            //  addNewVehicle(0);
            getView().findViewById(R.id.noHistory_driver).setVisibility(View.VISIBLE);

        } else {

            try {

                getView().findViewById(R.id.noHistory_driver).setVisibility(View.GONE);
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
