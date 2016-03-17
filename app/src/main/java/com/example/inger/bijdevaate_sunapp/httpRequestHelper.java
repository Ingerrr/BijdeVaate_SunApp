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
 * Connects with the internet and enables downloading data
 */
public class httpRequestHelper {
    // initial url
    private static final String urlFormat = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=da7170620a0ba8bcf1e2af71d1401764";

    /**
     * Downloads data from openweathermap with user input as query
     */
    protected static synchronized String downloadFromServer(String... params) throws JSONException {
        // initialize return string and url
        String returnJson = "";
        URL url = null;

        // Get input from user
        String input = params[0];

        // combine user input and initial url
        try {
            url = new URL(String.format(urlFormat, input));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // check if url is correct
        HttpURLConnection connection;
        if (url != null){
            try {
                // make http connection
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                // read the response
                int response = connection.getResponseCode();

                // read in data if the response code is positive
                if (response >= 200 && response < 299){
                    // read data line by line
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = br.readLine()) != null){
                        returnJson = returnJson + line;
                    }
                    br.close();
                }
                // read error if response code was negative
                else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    // read error message line by line
                    String line = "";
                    while ((line = br.readLine()) != null){
                        returnJson = returnJson + line;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ///return data;
        return returnJson;
    }
}
