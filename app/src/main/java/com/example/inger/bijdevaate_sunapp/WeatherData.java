package com.example.inger.bijdevaate_sunapp;

import java.io.Serializable;

/**
 * Object containing the weather and sunset and -rise time for specific city.
 */

public class WeatherData implements Serializable {

    // fields
    private Long sunsetTime;
    private Long sunriseTime;
    private int weather;
    private String city;

    // constructor
    /*
    * Constructs weather data class with sunset sunrise weather code and cityname
     */
    public WeatherData (Long sunset, Long sunrise, int weatherCode, String cityName) {
        sunsetTime = sunset;
        sunriseTime = sunrise;
        weather = weatherCode;
        city = cityName;
    }

    // methods
    /*
    * Returns sunset time
     */
    public Long getSunsetTime(){
        return sunsetTime;
    }

    /*
    * Returns sunrise time
     */
    public Long getSunriseTime(){
        return sunriseTime;
    }

    /*
    * Returns weather code
     */
    public int getWeather(){
        return weather;
    }

    /*
    * Returns city name
     */
    public String getCity(){
        return city;
    }
}