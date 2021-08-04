package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] item;
    private final String[] month;
    private final String[] desc;
    private final Integer[] img;


    public CustomListAdapter(Activity context, String[] item, String[] month, Integer[] img, String[] desc) {
        super(context, R.layout.customadapter, item);
        this.context = context;
        this.item = item;
        this.month = month;
        this.img = img;
        this.desc = desc;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();

        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.customadapter, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.day);
        TextView txtTitle_1 = (TextView) rowView.findViewById(R.id.tempValue);
        TextView txtTitle_2 = (TextView) rowView.findViewById(R.id.desc);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(item[position]);
        txtTitle_1.setText(month[position]);
        txtTitle_2.setText(desc[position]);
        imageView.setImageResource(img[position]);
        return rowView;
    };
}
