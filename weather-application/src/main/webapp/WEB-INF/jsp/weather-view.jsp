<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Weather Application</title>

        <style>
            
            body {
                font-family:arial,sans-serif;
				font-size: small;
            }
            
            .wIcon {
                height: 40px;
                padding: 1px;
                width: 40px;
            }

            .wMainData {
                padding: 2px;
                float: left;
            }

            .wCurrenTemp {
                float: left;
                font-size: 180%;
                padding: 0 10px 0 5px;
            }

            .wForecast {
                float: left;
                padding: 0 10px 0 0;
                text-align: center;
            }

            .wTitle {
                font-weight: bold;	
                font-size:120%;
                text-align: left;
            }

            .wInnerBox {
                display: inline-block;
            }

            .wOuterBox {
                width: 100%;
                text-align: center;
                margin-top: 5px;
            }

        </style>


    </head>
    <body>




        <div class="wOuterBox">
            <div class="wInnerBox">


                <div>


                    <div class="wTitle"><c:out value='${weather.city}' /></div>

                    <div style="margin-top:10px" >


                        <c:if test="${weather.errorMessage == null }" >
                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                <tbody>
                                    <tr>
                                        <td >
                                            <div class="wMainData" >
                                                <div class="wIcon" style="float: inherit">
                                                    <img class="w_cci" src="http://g0.gstatic.com<c:out value='${weather.icon}' />" alt="Rain" width="40" height="40">
                                                </div>

                                                <div class="wCurrenTemp" style=" float: inherit" ><c:out value='${weather.tempC}' />&deg;C</div>

                                                <div class="w_cc_text" style=" float: inherit; text-align: left" id="w_4_c0_text">
                                                    Current: <c:out value='${weather.condition}'/><br>
                                                    <c:out value='${weather.wind}'/><br>
                                                    <c:out value='${weather.humidity}'/>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td>
                                            <div style="width:100%" >
                                                <c:forEach var="forecast" items='${weather.forecast}'>
                                                    <div class="wForecast" title="<c:out value='${forecast.condition}' />">
                                                        <c:out value='${forecast.dayOfTheWeek}' /><br>
                                                        <div>
                                                            <img  src="//g0.gstatic.com<c:out value='${forecast.icon}' />" alt="<c:out value='${forecast.condition}' />" width="40" height="40">
                                                        </div>
                                                        <nobr><c:out value='${forecast.lowTemperatureC}' />&deg;&nbsp;|&nbsp;<c:out value='${forecast.highTemperatureC}' />&deg;</nobr>
                                                    </div>
                                                </c:forEach>							
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </c:if>
                        <c:if test="${weather.errorMessage != null }" >
                            ERROR : <c:out value="${weather.errorMessage}" /> (<c:out value='${city}' />)
                        </c:if>                                     

                    </div>
                </div>
                <div style="margin-top:10px">
                    <form action="" method="post">
                        <label for="rinput">City: </label>
                        <input type="text" name="pCity" id="pCity" value="<c:out value='${city}' />" />&nbsp;
                        <input type="submit" name="Send" value="Go!"/>  
                    </form>
                </div>
            </div>



        <div style="float:right; margin:5px">
            Execution Time: <c:out value='${elapsedTime}' /> ms | Active Cache : <c:out value='${cacheEnabled}' /> 
        </div>   

        </div>


     



    </body>
</html>
