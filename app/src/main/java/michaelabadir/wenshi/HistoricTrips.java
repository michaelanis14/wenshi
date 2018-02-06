package michaelabadir.wenshi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoricTrips extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_trips);

        ArrayList<Trip> Trip = GetTrip();

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
        ArrayList<Trip> results = new ArrayList<>();

        Trip sr1 = new Trip();
        sr1.setDate("08 April, 05:12 PM");
        sr1.setFrom("Madinaty");
        sr1.setTo("Rehab");
        sr1.setCost(500);
        results.add(sr1);

        sr1 = new Trip();
        sr1.setDate("08 April, 05:12 PM");
        sr1.setFrom("Madinaty");
        sr1.setTo("Rehab");
        sr1.setCost(450);
        results.add(sr1);

        sr1 = new Trip();
        sr1.setDate("08 April, 05:12 PM");
        sr1.setFrom("Madinaty");
        sr1.setTo("Rehab");
        sr1.setCost(780);
        results.add(sr1);

        sr1 = new Trip();
        sr1.setDate("08 April, 05:12 PM");
        sr1.setFrom("Madinaty");
        sr1.setTo("Rehab");
        sr1.setCost(500);
        results.add(sr1);

        sr1 = new Trip();
        sr1.setDate("08 April, 05:12 PM");
        sr1.setFrom("Madinaty");
        sr1.setTo("Rehab");
        sr1.setCost(500);
        results.add(sr1);

        sr1 = new Trip();
        sr1.setDate("08 April, 05:12 PM");
        sr1.setFrom("Madinaty");
        sr1.setTo("Rehab");
        results.add(sr1);
        sr1.setCost(2000);

        sr1 = new Trip();
        sr1.setDate("08 April, 05:12 PM");
        sr1.setFrom("Madinaty");
        sr1.setTo("Rehab");
        results.add(sr1);
        sr1.setCost(3000);

        return results;
    }
}