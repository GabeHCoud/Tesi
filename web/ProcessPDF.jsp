<%@page import="blogics.Consumo"%>
<%@page import="blogics.Fattura"%>
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

    if(status.equals("view"))
    {
    }else if (status.equals("process"))
    {
            InputStream is = request.getInputStream();            
            fattureManagement.setInputStream(is);            
            fattureManagement.processPDF();  
            fattureManagement.insert();            
            
            if(fattureManagement.getResult() >= 0)
            {
                Integer id = fattureManagement.getFattura().IdFattura;
                Cookie cookie = new Cookie("status","details");
                response.addCookie(cookie);
                cookie = new Cookie("idFattura",id.toString());
                response.addCookie(cookie);
                response.sendRedirect("Fatture.jsp");
            }
           
    }   
    
%>

<%@include file="Header.jsp" %>
<%if(fattureManagement.getErrorMessage() != null)
{%>     
    <div id="titolo">
        Si è verificato un Errore!
    </div>
    <div id="testo">
    <%=fattureManagement.getErrorMessage()%>
        <br/><br/><br/>
        <a href="Index.jsp">
            <div class="button" style="width: 150px; margin: 0 auto;">Torna all'Home Page</div>
        </a>
        <br/>
    </div>
<%}else{%>
    
<%
}%>
        
<%@include file="Footer.jsp" %>