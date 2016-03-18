package com.example.inger.bijdevaate_sunapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/*
* Obtains relevant weather data for location given by user at current time
* and determines if it is day or night based on sunset and sunrise times.
 */
public class MainActivity extends AppCompatActivity {
    // initialise variables
    String input = "";
    WeatherData weatherData = null;

    /*
    * Sets text of headers and restores last weather data incase orientation has been changed
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check if there is saved data
        if(savedInstanceState != null){
            super.onRestoreInstanceState(savedInstanceState);

            // extract weatherdata from saved instance
            WeatherData weatherData = (WeatherData) savedInstanceState.getSerializable("weatherData");

            // check if weatherdata contains actual weather data (instead of null)
            if(weatherData != null){

                // set data
                setData(weatherData);
            }
            else{
                // else, set basic settings of main screen
                ((TextView) findViewById(R.id.header)).setText(R.string.headerText);

                // restore text of edittext field
                String editText = savedInstanceState.getString("editText");
                ((EditText)findViewById(R.id.inputField)).setText(editText);
            }
        }
        else{
            // if no data is saved, set basic settings of main screen
            ((TextView) findViewById(R.id.header)).setText(R.string.headerText);
        }
    }

    /*
    * Extracts relevant weather data for user input from the internet
     */
    public void search(View view) throws JSONException {
        // Get input from user
        input = ((EditText)findViewById(R.id.inputField)).getText().toString();

        // Remove white spaces
        input = input.replaceAll("\\s", "");

        // Message to user if input is empty
        if (input.matches("")){
            Toast.makeText(this, "No input is given...", Toast.LENGTH_SHORT).show();
            // clear input field
            ((EditText)findViewById(R.id.inputField)).setText("");
            return;
        }

        // Remove any non-characters
        input = input.replaceAll("\\P{L}","");

        // Create asynctask that handles data
        TagAsyncTask tagAsyncTask = new TagAsyncTask(MainActivity.this);
        try {
            tagAsyncTask.execute(input);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    * Display weather data obtained by asyncTask from the internet
     */
    public void setData(WeatherData weather){
        // store weather info in global variable
        weatherData = weather;

        // extract relevant weather info from the JSON object
        Long sunset = weatherData.getSunsetTime()*1000;
        Long sunrise = weatherData.getSunriseTime()*1000;
        int weatherCode = weatherData.getWeather();
        String city = weatherData.getCity();

        // create empty weather text
        String weatherText;

        // activate the layout for the weather icon
        (findViewById(R.id.image)).setVisibility(View.VISIBLE);

        // but make all weather icons initially invisible
        (findViewById(R.id.sunny)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.mostly)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.partly)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.cloudy)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.night)).setVisibility(View.INVISIBLE);

        // get current timestamp (timezone UTC + 1)
        Long timeStamp = System.currentTimeMillis()+3600000;

        // check whether it is daytime
        if(timeStamp < sunset && timeStamp > sunrise){

            // check the weather code
            if(weatherCode == 800){
                // activate sunny-icon
                (findViewById(R.id.sunny)).setVisibility(View.VISIBLE);
                // update weatherText to sunny
                weatherText = getString(R.string.sunnyText);
            }
            else if(weatherCode == 802){
                // activate partly sunny icon and text
                (findViewById(R.id.partly)).setVisibility(View.VISIBLE);
                weatherText = getString(R.string.partlyText);
            }
            else if(weatherCode == 801){
                // activate mostly sunny icon and text
                (findViewById(R.id.mostly)).setVisibility(View.VISIBLE);
                weatherText = getString(R.string.mostlyText);
            }
            else{
                // activate cloudy icon and text
                (findViewById(R.id.cloudy)).setVisibility(View.VISIBLE);
                weatherText = getString(R.string.cloudyText);
            }

            // convert timestamp into digital clock format
            Timestamp stamp = new Timestamp(timeStamp);
            Date date = new Date(stamp.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            String formattedDate = sdf.format(date);

            // set weather info to previously altered weather text and current time
            ((TextView) findViewById(R.id.textResult)).setText(weatherText + formattedDate + "!");

            // change background of app to light blue
            findViewById(R.id.mainLayout).setBackgroundColor(Color.parseColor("#0070FF"));
            findViewById(R.id.inputField).setBackgroundColor(Color.parseColor("#0080FF"));
        }
        // if it is nighttime
        else{
            // activate night icon and text
            (findViewById(R.id.night)).setVisibility(View.VISIBLE);
            weatherText = getString(R.string.nightText);

            // update weather text
            ((TextView)findViewById(R.id.textResult)).setText(weatherText);

            // change background of app to dark blue
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
    }

    /*
    * Refresh page by calling AsyncTask again with same input
     */
    public void refresh(View view) {
        // make AsyncTask
        TagAsyncTask tagAsyncTask = new TagAsyncTask(MainActivity.this);
        try {
            // gather relevant weather data for user input location
            tagAsyncTask.execute(input);
        }catch (Exception e) {
            e.printStackTrace();
        }

        // message user that info is refreshed
        Toast.makeText(this, R.string.refreshMessage, Toast.LENGTH_SHORT).show();
    }

    /*
    * Enable user to change location when this button is pressed
     */
    public void changeLocation(View view) {
        // return to main screen
        backToMain();
    }

    /*
    * Return  to main screen when button is pressed. The original method is overridden because this
    * would shut the app down when there is only one activity.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {

        // check if the back button is pressed
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // return to main screen
            backToMain();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    * Return to main screen where the user can input location
     */
    public void backToMain(){
        // activate input layout
        findViewById(R.id.inputLayout).setVisibility(View.VISIBLE);
        // disable output layout
        findViewById(R.id.outputLayout).setVisibility(View.INVISIBLE);
        // set header text back to normal
        ((TextView)findViewById(R.id.header)).setText(getString(R.string.headerText));
    }

    /*
    * Save weather data and input text when orientation changes
     */
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        // retrieve text from input field
        String editText = ((EditText)findViewById(R.id.inputField)).getText().toString();

        // put input text and weather data into outstate
        outState.putString("editText", editText);
        outState.putSerializable("weatherData", weatherData);
    }
}


