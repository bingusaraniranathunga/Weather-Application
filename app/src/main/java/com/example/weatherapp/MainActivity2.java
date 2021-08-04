package com.example.weatherapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView tcity = (TextView) findViewById(R.id.city);
        TextView tday = (TextView) findViewById(R.id.day);
        TextView tdesc = (TextView) findViewById(R.id.description);
        ImageView image = (ImageView) findViewById(R.id.icon_n);
        TextView ttemperature = (TextView) findViewById(R.id.temperature);
        TextView tmax = (TextView) findViewById(R.id.maxt);
        TextView tmin = (TextView) findViewById(R.id.mint);
        TextView thumidity = (TextView) findViewById(R.id.humidity);
        TextView tdate = (TextView) findViewById(R.id.dateNo);
        TextView tfeel = (TextView) findViewById(R.id.feels);
        TextView tpres = (TextView) findViewById(R.id.pressure);



        String dDay = getIntent().getStringExtra("showDay");
        String dTemp = getIntent().getStringExtra("showTemperature");
        String dHum = getIntent().getStringExtra("showHum");
        String dDesc = getIntent().getStringExtra("showDesc");
        String dDate = getIntent().getStringExtra("showDate");
        String dCity= getIntent().getStringExtra("showCity");
        String dTmax = getIntent().getStringExtra("showTmax");
        String dTmin= getIntent().getStringExtra("showTmin");
        String dFeel = getIntent().getStringExtra("showTfeel");
        String dPres= getIntent().getStringExtra("showPres");
        Bundle bundle= getIntent().getExtras();
        if(bundle!=null){
            int res_image = bundle.getInt("showImage");
            image.setImageResource(res_image);
        }

        tcity.setText(dCity);
        tday.setText(dDay);
        ttemperature.setText(dTemp);
        tdesc.setText(dDesc);
        thumidity.setText(dHum);
        tdate.setText(dDate);
        tmax.setText(dTmax);
        tmin.setText(dTmin);
        tfeel.setText(dFeel);
        tpres.setText(dPres);


    }
}