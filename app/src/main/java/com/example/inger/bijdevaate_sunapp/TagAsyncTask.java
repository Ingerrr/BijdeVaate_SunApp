package com.example.inger.bijdevaate_sunapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Enables http connection and downloading of data from service on different thread than main activity
 */
public class TagAsyncTask extends AsyncTask<String, Integer, String> {
    // Fields
    private Context context;
    private MainActivity activity;
    private String[] input;

    // Constructor
    public TagAsyncTask(MainActivity activity){
        super();
        this.activity = activity;
        this.context = this.activity.getApplicationContext();
    }

    /**
     * Message user that weather data is prepared
     */
    @Override
    protected void onPreExecute(){
        super.onPreExecute();

    }

    /*
    * Activates download weather data from the internet
     */
    @Override
    protected String doInBackground(String... params) {

        input = params;
        // Calls http request helper to download data
        try {
            return httpRequestHelper.downloadFromServer(input);
        } catch (JSONException e) {
            e.printStackTrace();
            return new String();
        }

    }

    /*
    * Hands over data to main activity after data has been downloaded
     */
    @Override
    protected void onPostExecute(String json){
        super.onPostExecute(json);

        // initialize variables
        JSONObject data;
        WeatherData weatherData;

        if(json.length()==0){
            // set error message if no data has been obtained
            Toast.makeText(context, R.string.internetMessage, Toast.LENGTH_LONG).show();
        }
        else{
            // extract data from json object
            try {
                // convert input string into json object
                data = new JSONObject(json);

                // check for possible error code
                int cod = data.getInt("cod");

                // if there was no error
                if(cod >= 200 && cod < 299) {
                    // extract all required info from json object
                    JSONObject weather = data.getJSONArray("weather").getJSONObject(0);
                    int temp = data.getJSONObject("main").getInt("temp");
                    int weatherCode = weather.getInt("id");
                    Long sunrise = data.getJSONObject("sys").getLong("sunrise");
                    Long sunset = data.getJSONObject("sys").getLong("sunset");
                    String cityName = data.getString("name");

                    // check if city name from JSON object is similar to user input to prevent wrong results
                    if(!cityName.replaceAll("\\s", "").toLowerCase().contains(input[0].toLowerCase())){
                        if(!input[0].toLowerCase().contains(cityName.replaceAll("\\s", "").toLowerCase())){
                            // show error
                            Toast.makeText(context, R.string.errorMessage, Toast.LENGTH_LONG).show();
                            // clear input field
                            ((EditText) activity.findViewById(R.id.inputField)).setText("");
                            return;
                        }
                    }

                    // create WeatherData object
                    weatherData = new WeatherData(sunset, sunrise, weatherCode, cityName, temp);

                    // return to mainActivity and set data
                    this.activity.setData(weatherData);
                }
                // if there has occurred an error
                else{
                    // obtain error message from json object
                    String errorMessage = data.getString("message");

                    // show error
                    Toast.makeText(context,errorMessage, Toast.LENGTH_LONG).show();

                    // clear input field
                    ((EditText) activity.findViewById(R.id.inputField)).setText("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
