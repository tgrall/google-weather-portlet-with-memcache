/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *
 */
package org.exoplatform.demo.weather.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class WeatherObject implements Serializable {

    
    private String city;
    private String postalCode;
    private String latitude;
    private String longiture;
    private Date forecastDate;
    private Date currentTime;
    private String unitSystem;
    private String condition;
    private int tempF;
    private int tempC;
    private String humidity;
    private String icon;
    private String wind;
    private String errorMessage = null;
    
    private List<Forecast> forecast = new ArrayList<Forecast>();
    
    
    public WeatherObject() {
    
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }
    
    public void addForecast(Forecast item ) {
        this.forecast.add(item);
    }

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }

    public Date getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(Date forecastDate) {
        this.forecastDate = forecastDate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongiture() {
        return longiture;
    }

    public void setLongiture(String longiture) {
        this.longiture = longiture;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getUnitSystem() {
        return unitSystem;
    }

    public void setUnitSystem(String unitSystem) {
        this.unitSystem = unitSystem;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTempC() {
        return tempC;
    }

    public void setTempC(int tempC) {
        this.tempC = tempC;
    }

    public int getTempF() {
        return tempF;
    }

    public void setTempF(int tempF) {
        this.tempF = tempF;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    
    @Override
    public String toString() {
        return "WeatherObject{" + "city=" + city + ", postalCode=" + postalCode + ", latitude=" + latitude + ", longiture=" + longiture + ", forecastDate=" + forecastDate + ", currentTime=" + currentTime + ", unitSystem=" + unitSystem + ", condition=" + condition + ", tempF=" + tempF + ", tempC=" + tempC + ", humidity=" + humidity + ", icon=" + icon + ", wind=" + wind + ", forecast=" + forecast + '}';
    }

    
    
    
}
