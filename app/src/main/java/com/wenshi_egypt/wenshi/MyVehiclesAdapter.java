package com.wenshi_egypt.wenshi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyVehiclesAdapter extends BaseAdapter {
        private static ArrayList<Vehicle> searchArrayList;

        private LayoutInflater mInflater;

    MyVehiclesAdapter(Context context, ArrayList<Vehicle> results) {
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
                convertView = mInflater.inflate(R.layout.template_vehicle, null);
                holder = new ViewHolder();
                holder.txtType = convertView.findViewById(R.id.textView_vehicle_type);
                holder.txtModel = convertView.findViewById(R.id.textView_vehicle_model);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtType.setText(searchArrayList.get(position).getType());
            holder.txtModel.setText(String.format(searchArrayList.get(position).getModel()));

            return convertView;
        }

        static class ViewHolder {
            TextView txtType;
            TextView txtModel;
        }
    }

