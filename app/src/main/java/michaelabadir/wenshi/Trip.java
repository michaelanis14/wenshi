package michaelabadir.wenshi;

import java.text.DecimalFormat;

public class Trip {
    private String date, from, to;
    private double cost;

    Trip(String date, String from, String to, double cost)   {
        this.date = date;
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    String getDate() {
        return date;
    }

    @SuppressWarnings("unused")
    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    String getTo() {
        return to;
    }

    @SuppressWarnings("unused")
    public void setTo(String to) {
        this.to = to;
    }

    String getCost() {
        DecimalFormat format = new DecimalFormat("0.#");
        return format.format(cost);
    }

    @SuppressWarnings("unused")
    public void setCost(double cost) {
        this.cost = cost;
    }
}