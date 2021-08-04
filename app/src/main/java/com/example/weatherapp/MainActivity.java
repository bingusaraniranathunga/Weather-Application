package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String location;
    private String city = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FetchData fetchData = new FetchData();
        fetchData.execute();
    }

    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.setting:
                Intent i = new Intent(MainActivity.this,Preferences.class);
                startActivity(i);
                return true;
            case R.id.refresh:
                finish();
                startActivity(getIntent());
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    public class FetchData extends AsyncTask<String,Void,String>{
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected void onPostExecute(String s) {

            try {
                ListView listView =findViewById(R.id.dayList);

                final String [] cityy= new String[7];
                final String[] day  =  new String[7];
                final String[] temp = new String[7];
                final String[] tempmax = new String[7];
                final String[] tempmin = new String[7];
                final String[] weather = new String[7];
                final Integer[] icon_List = new Integer[7];
                final String[] humidity = new String[7];
                final String[] date = new String[7];
                final String[] dayn = new String[7];
                final String[] tempfeel = new String[7];
                final String[] pres = new String[7];

                JSONObject jOb = new JSONObject(forecastJsonStr);
                JSONArray a1= jOb.getJSONArray("daily");



                for(int i=0;i<7;i++) {
                    JSONObject object = a1.getJSONObject(i);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                    SimpleDateFormat datenn = new SimpleDateFormat("yyyy-MM-dd");
                    String unixTimeString = object.getString("dt");
                    long unixTimeInt = Integer.parseInt(unixTimeString);
                    Date dateFormat = new Date(unixTimeInt * 1000);
                    String Date = String.valueOf(dateFormat);
                    String nday = sdf.format(dateFormat);
                    datenn.setTimeZone(java.util.TimeZone.getTimeZone("GMT+05:30"));
                    String new_format_dime=datenn.format(dateFormat);

                    dayn[i] = nday;

                    if (i == 0) {
                        day[i] = nday + " "+"(TODAY)";
                    } else {
                        day[i] = nday;
                    }
                    date[i] =new_format_dime;



                    JSONObject temDay = a1.getJSONObject(i);
                    JSONObject t_tem = temDay.getJSONObject("temp");
                    Double tc = t_tem.getDouble("day");
                    Double Tmax = t_tem.getDouble("max");
                    Double Tmin = t_tem.getDouble("min");
                    JSONObject t_feel = temDay.getJSONObject("feels_like");
                    Double tf= t_feel.getDouble("day");
                    final SharedPreferences new_entered = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    String new_unit = new_entered.getString("Tempuni", "false");

                    if("1".equals(new_unit)){
                        Double tc1= tc-273.15;
                        Double t1ma= Tmax-273.15;
                        Double t1mi= Tmin-273.15;
                        Double tf1= tf-273.15;
                        temp[i]=String.format("%.2f"+"\u00B0"+"C", tc1);
                        tempmax[i]=String.format("%.2f"+"\u00B0"+"C", t1ma);
                        tempmin[i]=String.format("%.2f"+"\u00B0"+"C", t1mi);
                        tempfeel[i]=String.format("%.2f"+"\u00B0"+"C", tf1);
                    }else if("2".equals(new_unit)){
                        Double tc2 = (tc-273.15)*(9.0f/5.0f) +32.0f;
                        Double t2ma = (Tmax-273.15)*(9.0f/5.0f) +32.0f;
                        Double t2mi = (Tmin-273.15)*(9.0f/5.0f) +32.0f;
                        Double tf2 = (tf-273.15)*(9.0f/5.0f) +32.0f;
                        temp[i] =String.format("%.2f"+"\u00B0"+"F", tc2);
                        tempmax[i] =String.format("%.2f"+"\u00B0"+"F", t2ma);
                        tempmin[i] =String.format("%.2f"+"\u00B0"+"F", t2mi);
                        tempfeel[i]=String.format("%.2f"+"\u00B0"+"F", tf2);
                    }

                    final SharedPreferences new_entered_city= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    String new_city=( new_entered_city.getString("Newcity", "colombo"));

                    String location;
                    location = new_city;
                    cityy[i]=location;

                    JSONArray weather_part = object.getJSONArray("weather");
                    JSONObject up =weather_part.getJSONObject(0);
                    String description = up.getString("description");
                    String main = up.getString("main");

                    weather[i]=description;
                    String hum = object.getString("humidity");
                    humidity[i] =hum + " %";
                    String pressure = object.getString("pressure");
                    pres[i] =pressure + " hPa";

                    if(main.equals("Clear")){
                        if (description.equals("clear sky")){
                            icon_List[i] = R.drawable.p1;
                        }
                    }else if(main.equals("Clouds")){
                        if(description.equals("few clouds")){
                            icon_List[i] = R.drawable.p2;
                        }
                        if(description.equals("scattered clouds")){
                            icon_List[i] = R.drawable.p3;
                        }if(description.equals("broken clouds")){
                            icon_List[i] = R.drawable.p4;
                        }if(description.equals("overcast clouds")){
                            icon_List[i] = R.drawable.p4;
                        }
                    }else if(main.equals("Rain")){
                        if(description.equals("light rain") || description.equals("moderate rain") || description.equals("heavy intensity rain") || description.equals("very heavy rain") ||description.equals("extreme rain")){
                            icon_List[i] = R.drawable.p5;
                        }
                        if(description.equals("light intensity shower rain") || description.equals("shower rain") || description.equals("heavy intensity shower rain") || description.equals("ragged shower rain")){
                            icon_List[i] = R.drawable.p5;
                        }
                    }else if(main.equals("Thunderstorm")){
                        icon_List[i] = R.drawable.p7;
                    }else if(main.equals("Snow")){
                        icon_List[i] = R.drawable.p8;
                    }else{
                        icon_List[i] = R.drawable.p9;
                    }
                    CustomListAdapter adapter = new CustomListAdapter(MainActivity.this, day,temp,icon_List,weather);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String sDay = dayn[+position];//day
                            String sTemp = temp[+position];//temp val
                            String sHum = humidity[+position];//humidity
                            String sDesc = weather[+position];//description
                            Integer sImg = icon_List[+position];//image
                            String sDate = date[+position];//date
                            String sCity =cityy[+position];//city
                            String sTmax =tempmax[+position];
                            String sTmin =tempmin[+position];
                            String sFeels =tempfeel[+position];
                            String sPres =pres[+position];

                            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                            intent.putExtra("showDay", sDay);
                            intent.putExtra("showTemperature", sTemp);
                            intent.putExtra("showHum", sHum);
                            intent.putExtra("showDesc", sDesc);
                            intent.putExtra("showImage", sImg);
                            intent.putExtra("showDate",sDate);
                            intent.putExtra("showCity",sCity);
                            intent.putExtra("showTmax",sTmax);
                            intent.putExtra("showTmin",sTmin);
                            intent.putExtra("showTfeel",sFeels);
                            intent.putExtra("showPres",sPres);
                            startActivity(intent);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                final SharedPreferences new_entered_city= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String new_city=( new_entered_city.getString("Newcity", "colombo"));
                List<Double> c=null;
                String location;
                location = new_city;
                Geocoder geo = new Geocoder(getApplicationContext());
                List<Address> geol= geo.getFromLocationName(location, 5);

                c = new ArrayList<>(geol.size());
                for(Address a : geol) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        c.add(a.getLatitude());
                        c.add(a.getLongitude());
                    }
                }
                final String BASE_URL ="https://api.openweathermap.org/data/2.5/onecall?lat="+c.get(0)+"&lon="+c.get(1)+"&exclude=minutely,hourly,alerts&appid=d3b66adb47d3817fddc1ddc7fb2b2820";
                URL url = new URL(BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) { return null; }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line1;

                while ((line1 = reader.readLine()) != null) { buffer.append(line1 + "\n"); }
                if (buffer.length() == 0) { return null; }
                forecastJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e("Hi", "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) { urlConnection.disconnect(); }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Hi", "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }

}