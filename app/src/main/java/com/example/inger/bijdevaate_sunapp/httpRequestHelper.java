package com.example.inger.bijdevaate_sunapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Inger on 14-3-2016.
 */
public class httpRequestHelper {

    private static final String urlFormat = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=da7170620a0ba8bcf1e2af71d1401764";


    protected static synchronized String downloadFromServer(String... params) throws JSONException {

        // Initialize return value
        ///WeatherData weatherData = null;
        ///JSONObject data = null;

        String returnJson = "";

        // Get input from user
        String input = params[0];

        // Get complete url
        URL url = null;

        try {
            url = new URL(String.format(urlFormat, input));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if(url != null){
            Log.d("url", url.toString());
        }


        // Make connection
        HttpURLConnection connection;
        if (url != null){
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                connection.setReadTimeout(10000); // millis
                connection.setConnectTimeout(15000); // millis
                connection.setDoOutput(true);
                connection.connect();

                // read the response
                int response = connection.getResponseCode();

                // read in data
                if (response >= 200 && response < 299){
                    // read data
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String line = "";

                    while ((line = br.readLine()) != null){
                        returnJson = returnJson + line;
                        ///returnJson.append(line).append("\n");
                    }
                    br.close();

                    ///data = new JSONObject(json.toString());


                    ///weatherData = new WeatherData(sunset, sunrise, weatherCode, cityName);
                }
                else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                    String line = "";
                    while ((line = br.readLine()) != null){
                        returnJson = returnJson + line;
                        ///returnJson.append(line).append("\n");
                    }
                    br.close();

                    ///return null;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ///return data;
        return returnJson;
        ///return weatherData;
    }
}
