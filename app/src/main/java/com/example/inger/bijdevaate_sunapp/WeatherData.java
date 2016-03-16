package com.example.inger.bijdevaate_sunapp;

/**
 * Created by Inger on 14-3-2016.
 */

public class WeatherData{
    private Long sunsetTime;
    private Long sunriseTime;
    private int weather;
    private String city;


    public WeatherData(Long sunset, Long sunrise, int weatherCode, String cityName) {
        sunsetTime = sunset;
        sunriseTime = sunrise;
        weather = weatherCode;
        city = cityName;
    }

    public Long getSunsetTime(){
        return sunsetTime;
    }

    public Long getSunriseTime(){
        return sunriseTime;
    }

    public int getWeather(){
        return weather;
    }

    public String getCity(){
        return city;
    }
}