package com.example.inger.bijdevaate_sunapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Inger on 14-3-2016.
 */
public class TagAsyncTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private MainActivity activity;

    // Constructor
    public TagAsyncTask(MainActivity activity){
        super();
        this.activity = activity;
        this.context = this.activity.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // toast message
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return httpRequestHelper.downloadFromServer(params);
        } catch (JSONException e) {
            e.printStackTrace();
            return new String();
        }

    }

    @Override
    protected void onPostExecute(String json){
        super.onPostExecute(json);
        JSONObject data;
        WeatherData weatherData = null;
        if(json.length()==0){
            Toast.makeText(context,"Location not found", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            try {
                data = new JSONObject(json.toString());
                JSONObject weather = data.getJSONArray("weather").getJSONObject(0);
                int weatherCode = weather.getInt("id");
                Long sunrise = data.getJSONObject("sys").getLong("sunrise");
                Long sunset = data.getJSONObject("sys").getLong("sunset");
                String cityName = data.getString("name");
                weatherData = new WeatherData(sunset,sunrise,weatherCode,cityName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    this.activity.setData(weatherData);
    }
}
