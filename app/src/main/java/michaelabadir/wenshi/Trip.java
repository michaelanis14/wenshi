package michaelabadir.wenshi;


import com.google.firebase.database.Exclude;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by Michael on 2/1/2018.
*/

public class Trip {
    String date, from, to;
    double cost;

    public Trip(){};

    public Trip(String date, String from, String to, double cost)   {
        this.date = date;
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCost() {
        DecimalFormat format = new DecimalFormat("0.#");
        return format.format(cost);
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
