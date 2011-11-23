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
package org.exoplatform.demo.weather;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.demo.weather.service.WeatherObject;
import org.exoplatform.demo.weather.service.WeatherService;

public class GoogleWeatherServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String city = request.getParameter("pCity");
        String cacheEnabledParam = request.getParameter("pCache");
        boolean cacheEnabled = true;


        if (cacheEnabledParam != null) {
            cacheEnabled = Boolean.parseBoolean(cacheEnabledParam);
        }

        if (city == null) {
            city = "Miami";
        }


        WeatherService weatherService = new WeatherService();
        weatherService.setCacheEnabled(cacheEnabled);
        long start = System.currentTimeMillis();
        WeatherObject weather = weatherService.getWeather(city);
        long end = System.currentTimeMillis();
        
        request.setAttribute("city", city);
        request.setAttribute("cacheEnabled", cacheEnabled);
        request.setAttribute("weather", weather);
        request.setAttribute("elapsedTime", (end - start));
        

        request.getRequestDispatcher("/WEB-INF/jsp/weather-view.jsp").forward(request, response);
    }
}
