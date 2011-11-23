<%@taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<portlet:defineObjects />
<% 
String defaultCity = renderRequest.getPreferences().getValue("defaultCity", "Miami");
int expirationTime = Integer.parseInt(renderRequest.getPreferences().getValue("expirationTime", "60")); 
boolean cacheEnabled = Boolean.parseBoolean(renderRequest.getPreferences().getValue("cacheEnabled", "true")); 
%>

<style>

.wFormEntry {
	float: left;
	margin-bottom: 5px;
}

.wLabel {
 float: inherit;
 width: 100px;
}

.wField {
 float: inherit;

}


</style>

<blockquote>
<h3>Weather Portlet Configuration</h3>
<portlet:actionURL var="action"/>
<form action="<%= action %>" method="post">
  <div class="wFormEntry" >
    <div class="wLabel"><label  for="defaultCity">Default City : </label></div>
    <div class="wField"><input type="text" name="defaultCity" size="15" id="expirationTime" value="<%= defaultCity %>"/></div>
  </div>

  
  <div class="wFormEntry" >
    <div class="wLabel"><label for="cacheEnabled" >Cache Result : </label></div>
    <div class="wField" >
    <input type="radio" name="cacheEnabled" value="true" <%= (cacheEnabled)?"checked":"" %> > Yes
    <input type="radio" name="cacheEnabled" value="false"<%= (cacheEnabled)?"":"checked" %> > No
	</div>
  </div>
 
  <div class="wFormEntry" >
  <div class="wLabel"><label for="expirationTime">Expiration Time : </label></div>
  <div class="wField"><input type="text" name="expirationTime" size="5" id="expirationTime" value="<%= expirationTime %>"/> seconds</div>
  </div>

  
  <input type="submit" label="Save" value="Save" />
</form>
<br/>
</blockquote>