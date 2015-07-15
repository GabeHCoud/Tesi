<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
<%@page import="util.*"%>

<%@page buffer="40kb" %>
<%@page errorPage="/ErrorPage.jsp" %>

<jsp:useBean id="fattureManagement" scope="page" class="bflows.FattureManagement"/>
<jsp:setProperty name="fattureManagement" property="*"/>

<% 
    String status=request.getParameter("status");
    if(status==null)
    {
       status="process"; 
    }  

    switch(status)
    {
        case "view":
            break;
        case "process":
            InputStream is = request.getInputStream();            
            fattureManagement.setInputStream(is);            
            fattureManagement.processCSV();  
            break;
    }   
%>

<%@include file="Header.jsp" %>

        
<%@include file="Footer.jsp" %>