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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class WeatherService {

    public final static String API_URL = "http://www.google.com/ig/api?weather=";
    public final static String XPATH_EXP_CITY = "//city[@data]";
    public final static String XPATH_EXP_CONDITION = "//condition[@data]";
    public final static String XPATH_EXP_HUMIDITY= "//humidity[@data]";
    public final static String XPATH_EXP_ICON = "//icon[@data]";
    public final static String XPATH_EXP_WIND = "//wind_condition[@data]";
    public final static String XPATH_EXP_TEMP_C = "//temp_c[@data]";
    public final static String XPATH_EXP_TEMP_F = "//temp_f[@data]";
    public final static String XPATH_EXP_FORECAST = "//forecast_conditions";
    public final static String XPATH_ERROR = "//problem_cause[@data]";
    public final static String XML_ATTR_NAME = "data";
    
    private String memcachedServerLocation = null;
    
    
    
    private boolean cacheEnabled = true;
    private int expirationTime = 60;

    public WeatherService() {
        try {
            // load the configuration from environment
            javax.naming.Context ctx =  new javax.naming.InitialContext();
            String value = (String) ctx.lookup("java:comp/env/memcachedServer");
            
            if (value == null) {
                memcachedServerLocation = "127.0.0.1:11211";
            } else {
                memcachedServerLocation = value;
            }            
        } catch (NamingException ex) {
            Logger.getLogger(WeatherService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public WeatherObject getWeather(String location) {
        WeatherObject weather = null;

        try {
            String encodedLocation = URLEncoder.encode(location.toLowerCase(), "UTF-8");

            MemcachedClient cache = new MemcachedClient(AddrUtil.getAddresses(memcachedServerLocation));

            String cacheKey = "weather." + encodedLocation;

            Object o = cache.get(cacheKey);

            if (o == null || !cacheEnabled) {

                // parse the XML Document
                URL url = new URL(API_URL + encodedLocation);


                StringBuilder sb = new StringBuilder();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String xmlAsString = sb.toString();


                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(true);
                DocumentBuilder builder = dbf.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(xmlAsString));
                is.setEncoding("UTF-8");
                Document doc = builder.parse(is);

                weather = new WeatherObject();
                weather.setCity(getDataFromXML(doc, XPATH_EXP_CITY ));
                weather.setCondition(getDataFromXML(doc, XPATH_EXP_CONDITION ));
                weather.setHumidity(getDataFromXML(doc, XPATH_EXP_HUMIDITY ));
                weather.setIcon(getDataFromXML(doc, XPATH_EXP_ICON));
                weather.setWind(getDataFromXML(doc, XPATH_EXP_WIND ));
                
                if (getDataFromXML(doc, XPATH_ERROR ) != null) {
                    String errorMessage = getDataFromXML(doc, XPATH_ERROR ).trim();
                    if (errorMessage.equals("")) {
                        errorMessage = "Error : Unknown City";
                    }
                    weather.setErrorMessage( errorMessage );
                }

                String tempC = getDataFromXML(doc, XPATH_EXP_TEMP_C);
                if (tempC != null) {
                    weather.setTempC(Integer.parseInt(tempC));
                }
                String tempF = getDataFromXML(doc, XPATH_EXP_TEMP_F );
                if (tempF != null) {
                    weather.setTempF(Integer.parseInt(tempF));
                }

                weather.setForecast(this.getForeCast(doc));

                // use the cache only when enabled
                if (cacheEnabled) {
                    cache.set(cacheKey, expirationTime, weather);
                }

            } else {


                weather = (WeatherObject) o;

            }


        } catch (Exception ex) {
            Logger.getLogger(WeatherService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return weather;
    }

    public String getDataFromXML(Document doc, String xpathExpression) {
        String data = null;
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile(xpathExpression);
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            if (result != null) {
                NodeList nl = (NodeList) result;
                if (nl != null && nl.item(0) != null) {
                    data = nl.item(0).getAttributes().getNamedItem("data").getNodeValue();
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(WeatherService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }

    public List<Forecast> getForeCast(Document doc) {
        List<Forecast> resultValue = new ArrayList();

        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile(XPATH_EXP_FORECAST);
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList children = null;

            if (result != null) {
                NodeList nl = (NodeList) result;
                for (int i = 0; i < nl.getLength(); i++) {
                    children = nl.item(i).getChildNodes();
                    // get all the attributes
                    Forecast fc = new Forecast();
                    fc.setDayOfTheWeek(children.item(0).getAttributes().getNamedItem("data").getNodeValue());
                    fc.setLowTemperatureF(Integer.parseInt(children.item(1).getAttributes().getNamedItem("data").getNodeValue()));
                    fc.setHighTemperatureF(Integer.parseInt(children.item(2).getAttributes().getNamedItem("data").getNodeValue()));
                    fc.setIcon(children.item(3).getAttributes().getNamedItem("data").getNodeValue());
                    fc.setCondition(children.item(4).getAttributes().getNamedItem("data").getNodeValue());
                    resultValue.add(fc);
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(WeatherService.class.getName()).log(Level.SEVERE, null, ex);
        }


        return resultValue;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public int getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(int expirationTime) {
        this.expirationTime = expirationTime;
    }
}
