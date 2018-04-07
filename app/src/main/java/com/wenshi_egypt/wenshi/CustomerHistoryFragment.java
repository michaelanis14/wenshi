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

public class CustomerHistoryFragment extends Fragment implements View.OnClickListener{

    DatabaseReference rootRef, histRef;
    TextView demoValue, noHistoric;
    static String date, from, to, cost;

    public CustomerHistoryFragment(){}

    @SuppressLint("ValidFragment")
    public CustomerHistoryFragment(boolean b, String uid)  {
        if (b)  {
            this.histRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(uid).child("Trips");
        }
        else
            this.histRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(uid).child("Trips");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_history_customer, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ArrayList<HistoryModel> trip ;
                //= GetTrip();
/*
        //noinspection ConstantConditions
        demoValue = getView().findViewById(R.id.tvValue);
        noHistoric = getView().findViewById(R.id.textView_no_historic);
        final ListView lv1 = getView().findViewById(R.id.listView_historic_trips);

        lv1.setAdapter(new MyCustomBaseAdapter(getActivity(), trip));

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
            }
        });

        */
    }

    private ArrayList<HistoryModel> GetTrip(){
        final ArrayList<HistoryModel> results = new ArrayList<>();
/*
        //database reference pointing to root of database
       // rootRef = FirebaseDatabase.getInstance().getReference();
       // histRef = rootRef.child("Users").child("Customers").child("user1").child("Trips");
        histRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    //noinspection unchecked
                    HashMap<String, String> value = (HashMap<String, String>) child.getValue();
                    assert value != null;
                    if(!child.hasChild("FirstConstantTrip")) {
                        date = value.get("date");
                        from = value.get("from");
                        to = value.get("to");
                        cost = String.format("%s", value.get("cost"));

                        demoValue.setText("");
                        if (!date.isEmpty()) {
                            noHistoric.setVisibility(View.INVISIBLE);
                            results.add(new HistoryModel(date, from, to, Double.parseDouble(cost)));
                        }
                    }
                }
                if(results.size()==0)
                    noHistoric.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
             //   Toast.makeText(CustomerHistoryFragment.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
  */
        return results;

    }




    @Override
    public void onClick(View view) {


        if (((CustomerMapActivity) getActivity()).historyDetailsFragment == null) ((CustomerMapActivity) getActivity()).historyDetailsFragment = new HistoryDetailsFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

            Bundle args = new Bundle();
            args.putParcelable("DATA", ((HistoryModel)((CustomerMapActivity) getActivity()).getCustomer().getHistory().values().toArray()[view.getId()]));
            ((CustomerMapActivity) getActivity()).historyDetailsFragment.setArguments(args);

        ft.replace(R.id.mainFrame, ((CustomerMapActivity) getActivity()).historyDetailsFragment);
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
                button.setText(historyItem.getValue().getDate() + " " + historyItem.getValue().getStartTime() + "  -  " + historyItem.getValue().getCost() + " LE");
                button.setId(i);
                button.setOnClickListener(this);
                layout.addView(button);
                i++;
            }

         }
         catch (Exception e){
             Log.i("DATABASE ERR","Bad Structure"+e.toString());
         }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}