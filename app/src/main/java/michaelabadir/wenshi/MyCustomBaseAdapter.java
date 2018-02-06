package michaelabadir.wenshi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Michael on 2/6/2018.
 */

    public class MyCustomBaseAdapter extends BaseAdapter {
        private static ArrayList<Trip> searchArrayList;

        private LayoutInflater mInflater;
        final String from = "From: ";
        final String to = " To: ";
        final String egp = " EGP";

        public MyCustomBaseAdapter(Context context, ArrayList<Trip> results) {
            searchArrayList = results;
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return searchArrayList.size();
        }

        public Object getItem(int position) {
            return searchArrayList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.template_historic_trip, null);
                holder = new ViewHolder();
                holder.txtDate = (TextView) convertView.findViewById(R.id.textView_date);
                holder.txtFrom = (TextView) convertView.findViewById(R.id.textView_from);
                holder.txtTo = (TextView) convertView.findViewById(R.id.textView_to);
                holder.txtCost = (TextView) convertView.findViewById(R.id.textView_cost);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtDate.setText(searchArrayList.get(position).getDate());
            holder.txtFrom.setText(from + searchArrayList.get(position).getFrom());
            holder.txtTo.setText(to + searchArrayList.get(position).getTo());
            holder.txtCost.setText(searchArrayList.get(position).getCost() + egp);

            return convertView;
        }

        static class ViewHolder {
            TextView txtDate;
            TextView txtFrom;
            TextView txtTo;
            TextView txtCost;
        }
    }

