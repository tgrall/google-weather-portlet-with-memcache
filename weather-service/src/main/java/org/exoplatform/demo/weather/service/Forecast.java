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

/**
 *
 * @author tgrall
 */
public class Forecast implements Serializable{
    
    private String dayOfTheWeek;
    private int lowTemperatureF;
    private int highTemperatureF;
    private String icon;
    private String condition;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public int getHighTemperatureF() {
        return highTemperatureF;
    }
    
    public int getHighTemperatureC() {
        return convertToCelsius(highTemperatureF);
    }

    public void setHighTemperatureF(int highTemperatureF) {
        this.highTemperatureF = highTemperatureF;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getLowTemperatureF() {
        return lowTemperatureF;
    }

    public int getLowTemperatureC() {
        return  convertToCelsius(lowTemperatureF);
    }
    public void setLowTemperatureF(int lowTemperatureF) {
        this.lowTemperatureF = lowTemperatureF;
    }

    @Override
    public String toString() {
        return "Forecast{" + "dayOfTheWeek=" + dayOfTheWeek + ", lowTemperatureF=" + lowTemperatureF + ", highTemperatureF=" + highTemperatureF + ", icon=" + icon + ", condition=" + condition + '}';
    }
    

    protected int convertToFahrenheit(int c){
        return ((c * 9 / 5) + 32);
    }
    
    
    public static int convertToCelsius(int f){
        return ((f - 32) * 5 / 9);
    }    
    
    
}
