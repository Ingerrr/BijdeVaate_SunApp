package com.example.inger.bijdevaate_sunapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    String input = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.header)).setText(R.string.headerText);
    }

    public void search(View view) throws JSONException {
        input = ((EditText)findViewById(R.id.inputField)).getText().toString();
        input = input.replaceAll("\\s","");
        TagAsyncTask tagAsyncTask = new TagAsyncTask(MainActivity.this);
        try {
            tagAsyncTask.execute(input);
        }catch (Exception e){
            tagAsyncTask.cancel(true);
            //toast
        }

    }

    public void setData(WeatherData weatherData){
        Long sunset = weatherData.getSunsetTime()*1000;
        Long sunrise = weatherData.getSunriseTime()*1000;
        int weatherCode = weatherData.getWeather();
        String city = weatherData.getCity();
        String weatherText = "";

        (findViewById(R.id.sunny)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.mostly)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.partly)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.cloudy)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.night)).setVisibility(View.INVISIBLE);

        Long timeStamp = System.currentTimeMillis()+3600000;

        // set visibilities and change texts
        if(timeStamp < sunset && timeStamp > sunrise){
            if(weatherCode == 800){
                // clear
                Log.d("weather","clear");
                (findViewById(R.id.sunny)).setVisibility(View.VISIBLE);
                weatherText = "Yes, it is sunny at ";
            }
            else if(weatherCode == 802){
                // partly sunny
                Log.d("weather","partly sunny");
                (findViewById(R.id.partly)).setVisibility(View.VISIBLE);
                weatherText = "It is sort of sunny at ";
            }
            else if(weatherCode == 801){
                // mostly sunny
                Log.d("weather","mostly sunny");
                (findViewById(R.id.mostly)).setVisibility(View.VISIBLE);
                weatherText = "It is almost sunny at ";
            }
            else{
                // cloudy
                Log.d("weather","cloudy");
                (findViewById(R.id.cloudy)).setVisibility(View.VISIBLE);
                weatherText = "Sadly enough, it is not sunny at ";
            }

            Timestamp stamp = new Timestamp(timeStamp);
            Date date = new Date(stamp.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            String formattedDate = sdf.format(date);
            //Time date = new Time(stamp.getTime());
            ((TextView) findViewById(R.id.textResult)).setText(weatherText + formattedDate + "!");

            findViewById(R.id.mainLayout).setBackgroundColor(Color.parseColor("#0070FF"));
            findViewById(R.id.inputField).setBackgroundColor(Color.parseColor("#0080FF"));
        }
        else{
            // night
            Log.d("weather", "night");
            (findViewById(R.id.night)).setVisibility(View.VISIBLE);
            weatherText = "Go to sleep, man";
            ((TextView)findViewById(R.id.textResult)).setText(weatherText);
            findViewById(R.id.mainLayout).setBackgroundColor(Color.parseColor("#000030"));
            findViewById(R.id.inputField).setBackgroundColor(Color.parseColor("#000040"));
    }
        // update header
        ((TextView) findViewById(R.id.header)).setText(getString(R.string.headerCityText) + city + "?");

        // disable input layout
        (findViewById(R.id.inputLayout)).setVisibility(View.GONE);

        // activate output layout
        (findViewById(R.id.outputLayout)).setVisibility(View.VISIBLE);

        // clear input field
        ((EditText)findViewById(R.id.inputField)).setText("");

        // if ERROR
    }

    public void refresh(View view) {
        TagAsyncTask tagAsyncTask = new TagAsyncTask(MainActivity.this);
        try {
            tagAsyncTask.execute(input);
        }catch (Exception e) {
            tagAsyncTask.cancel(true);
            //toast
        }
    }

    public void changeLocation(View view) {
        findViewById(R.id.inputLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.outputLayout).setVisibility(View.INVISIBLE);
    }

    // save data when orientation changes
}


