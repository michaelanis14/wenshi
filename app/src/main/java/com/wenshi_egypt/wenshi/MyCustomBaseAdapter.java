package com.wenshi_egypt.wenshi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCustomBaseAdapter extends BaseAdapter {
        private static ArrayList<HistoricTrip> searchArrayList;

        private LayoutInflater mInflater;

    MyCustomBaseAdapter(Context context, ArrayList<HistoricTrip> results) {
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

        @SuppressLint("InflateParams")
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.template_historic_trip, null);
                holder = new ViewHolder();
                holder.txtDate = convertView.findViewById(R.id.textView_date);
                holder.txtFrom = convertView.findViewById(R.id.textView_from);
                holder.txtTo = convertView.findViewById(R.id.textView_to);
                holder.txtCost = convertView.findViewById(R.id.textView_cost);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtDate.setText(searchArrayList.get(position).getDate());
          //  holder.txtFrom.setText(String.format("From: %s", searchArrayList.get(position).getFrom()));
         //   holder.txtTo.setText(String.format("To: %s", searchArrayList.get(position).getTo()));
            holder.txtCost.setText(String.format("%s EGP", searchArrayList.get(position).getCost()));

            return convertView;
        }

        static class ViewHolder {
            TextView txtDate;
            TextView txtFrom;
            TextView txtTo;
            TextView txtCost;
        }
    }

