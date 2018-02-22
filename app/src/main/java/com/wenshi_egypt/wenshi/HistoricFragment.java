package com.wenshi_egypt.wenshi;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoricFragment extends Fragment implements View.OnClickListener{

    DatabaseReference rootRef;
    TextView demoValue;
    static String date, from, to, cost;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_historic_trips, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ArrayList<Trip> trip = GetTrip();
        //noinspection ConstantConditions
        demoValue = getView().findViewById(R.id.tvValue);
        final ListView lv1 = getView().findViewById(R.id.listView_historic_trips);

        lv1.setAdapter(new MyCustomBaseAdapter(getActivity(), trip));

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
            }
        });
    }

    private ArrayList<Trip> GetTrip(){
        final ArrayList<Trip> results = new ArrayList<>();

        //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Trips").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    //noinspection unchecked
                    HashMap<String, String> value = (HashMap<String, String>) child.getValue();
                    assert value != null;
                    date = value.get("date");
                    from = value.get("from");
                    to = value.get("to");
                    cost = value.get("cost");

                    demoValue.setText("");
                    results.add(new Trip(date, from, to, Double.parseDouble(cost)));

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
             //   Toast.makeText(HistoricFragment.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        return results;
    }

    @Override
    public void onClick(View view) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}