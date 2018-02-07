package michaelabadir.wenshi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoricTrips extends Activity {

    DatabaseReference rootRef;
    TextView demoValue;
    static String date, from, to, cost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_trips);

        ArrayList<Trip> Trip = GetTrip();
        demoValue = findViewById(R.id.tvValue);

        final ListView lv1 = findViewById(R.id.listView_historic_trips);
        lv1.setAdapter(new MyCustomBaseAdapter(this, Trip));

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //Object o = lv1.getItemAtPosition(position);
                //Trip fullObject = (Trip) o;
                //Toast.makeText(ListViewBlogPost.this, "You have chosen: " + " " + fullObject.getName(), Toast.LENGTH_LONG).show();
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
                    HashMap<String, String> value = (HashMap<String, String>) child.getValue();
                    date = value.get("date");
                    from = value.get("from");
                    to = value.get("to");
                    cost = value.get("cost");

                    demoValue.setText("");
//                    Toast.makeText(HistoricTrips.this, to, Toast.LENGTH_SHORT).show();
                    results.add(new Trip(date, from, to, Double.parseDouble(cost)));

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HistoricTrips.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        return results;
    }
}