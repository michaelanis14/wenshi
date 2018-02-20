package michaelabadir.wenshi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Help extends Fragment {

    DatabaseReference rootRef;
    String step1, step2, step3;
    TextView demo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //ArrayList<String> request = howToRequest();
        demo = getView().findViewById(R.id.helpDemo);
        final ArrayList<String> results = new ArrayList<>();
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.child("Help").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                step1 = value.get("help");
                demo.setText(step1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

//    private ArrayList<String> howToRequest() {
//        final ArrayList<String> results = new ArrayList<>();
//        rootRef = FirebaseDatabase.getInstance().getReference();
//        rootRef.child("Help").orderByKey().addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot child : dataSnapshot.getChildren()) {
//                    //noinspection unchecked
//                    HashMap<String, String> value = (HashMap<String, String>) child.getValue();
//                    assert value != null;
//                    step1 = value.get("step1");
//                    step2 = value.get("step2");
//                    step3 = value.get("step3");
//
//                    demo.setText(step1 + step2 + step3);
//                  //  results.add(new Trip(date, from, to, Double.parseDouble(cost)));
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        return results;
//    }

}

