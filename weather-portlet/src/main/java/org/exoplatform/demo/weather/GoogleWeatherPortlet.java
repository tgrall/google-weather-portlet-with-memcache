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
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.exoplatform.demo.weather.service.WeatherObject;
import org.exoplatform.demo.weather.service.WeatherService;

/**
 *
 * @author tgrall
 */
public class GoogleWeatherPortlet extends GenericPortlet {
    
    private final static String VIEW_PAGE = "/WEB-INF/jsp/GoogleWeatherPortlet/view.jsp";
    private final static String EDIT_PAGE = "/WEB-INF/jsp/GoogleWeatherPortlet/edit.jsp";
    
    public final static String PREF_EXP_TIME = "expirationTime";
    public final static String PREF_CACHE_ENABLED = "cacheEnabled";
    public final static String PREF_DEFAULT_CITY = "defaultCity";
    public final static String PARAM_CITY = "city";

    @Override
    protected void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        processRequest(request, response);
        
    }
    
    
    private void processRequest(RenderRequest request, RenderResponse response) throws PortletException, IOException {

        String city = request.getParameter("city");
        
        if (city == null) {
            city = request.getPreferences().getValue(PREF_DEFAULT_CITY, "Miami");
        }
        
        if (city != null) {
            WeatherService weatherService = new WeatherService();
            boolean cacheEnabled = Boolean.parseBoolean(request.getPreferences().getValue(PREF_CACHE_ENABLED, "true"));
            int expirationTime =  Integer.parseInt( request.getPreferences().getValue(PREF_EXP_TIME, "60") );
            
            
            weatherService.setCacheEnabled(cacheEnabled);
            weatherService.setExpirationTime(expirationTime);
            
            
            long start = System.currentTimeMillis();
            WeatherObject weather = weatherService.getWeather(city);    
            long end = System.currentTimeMillis();
            
            
            request.setAttribute("weather", weather);
            request.setAttribute("elapsedTime", (end-start));
            
            

            
        }
                    
                    
                    
        String pageToShow = VIEW_PAGE;
        
        if (PortletMode.EDIT.equals(request.getPortletMode())) {
            pageToShow = EDIT_PAGE;
        }    
        
        response.setContentType(request.getResponseContentType());
        if (getPortletContext().getResourceAsStream(pageToShow) != null) {
            try {
                // dispatch edit request to edit.jsp
                PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(pageToShow);
                dispatcher.include(request, response);
            } catch (IOException e) {
                throw new PortletException("Exception in Portlet", e);
            }
        } else {
            throw new PortletException("Exception JSP is missing.");
        }
        
    }

    
    
    @Override
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        
         if (request.getPortletMode().equals(PortletMode.EDIT)  ) {

            // retrieve parameters
            String expirationTime = request.getParameter(PREF_EXP_TIME);
            String cacheEnabled = request.getParameter("cacheEnabled");
            String defaultCity = request.getParameter("defaultCity");
            
            
            // demonstrates portlet preferences storage
            PortletPreferences pref = request.getPreferences();
            
            pref.setValue(PREF_EXP_TIME, expirationTime);
            pref.setValue(PREF_CACHE_ENABLED, cacheEnabled);
            pref.setValue(PREF_DEFAULT_CITY, defaultCity);
            pref.store();

            // back to view mode
            response.setRenderParameter(PARAM_CITY, defaultCity);
            response.setPortletMode(PortletMode.VIEW);
            
            
        } else {
            String city =  request.getParameter(PARAM_CITY) ;
            response.setRenderParameter(PARAM_CITY, city);             
        }
        
        
        
        
    }
    

    
    
    
    
    
    
}
