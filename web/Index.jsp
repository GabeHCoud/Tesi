<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="util.Conversion"%>
<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
<%//@page import="services.sessionservice.*" %>
<%@page import="util.*"%>

<%@page buffer="40kb" %>
<%@page errorPage="/ErrorPage.jsp" %>
        

<jsp:useBean id="fattureManagement" scope="page" class="bflows.FattureManagement"/>
<jsp:setProperty name="fattureManagement" property="*"/>

<% 
    String status=null;

    status=request.getParameter("status");
    if(status==null)
    {
       status="view"; 
       fattureManagement.cleanDB();
    }
%>

<%@include file="Header.jsp" %>
          
<%  if(status.equals("view")){%>
   
    <form name="form1" action="ProcessPDF.jsp" method="post" enctype="multipart/form-data">      
     
        <div id="titolo"><b>Carica una fattura</b></div>         
        <div id="testo">Carica il file PDF<br/><br/><input type="file" name="file" />
            <span >
                <input type="submit" value="ok"/>
            </span>
        </div>
    </form>     
    <br/><br/><br/>
    <form name="form2" action="ProcessCSV.jsp" method="post" enctype="multipart/form-data">          
        <div id="testo">Carica il file csv<br/><br/><input type="file" name="file"/>
            <span >
                <input type="submit" value="ok"/>
            </span>
        </div>
    </form>
 <%}%>       

<%@include file="Footer.jsp" %>
