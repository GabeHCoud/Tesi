<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="util.Conversion"%>
<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
<%//@page import="services.sessionservice.*" %>
<%@page import="util.*"%>

<%@page buffer="40kb" %>
<%@page errorPage="/ErrorPage.jsp" %>
        
<% 
    String status=null;

    status=request.getParameter("status");
    if(status==null)
    {
       status="view"; 
       System.out.println("sono in view");
    }
%>

<%@include file="Header.jsp" %>
          
<%  if(status.equals("view")){%>
   
    <form name="form1" action="Process.jsp" method="post" enctype="multipart/form-data">      
     
        <div id="titolo"><b>Tabella costi per telefono</b></div>         
        <div id="testo">Carica il file PDF<br/><br/><input type="file" name="file" />
            <span >
                <input type="submit" value="ok"/>
            </span>
        </div>
    </form>     
    <br/><br/><br/>
    <form name="form2" action="Process.jsp" method="post" enctype="multipart/form-data">          
        <div id="testo">Carica il file csv<br/><br/><input type="file" name="file"/>
            <span >
                <input type="submit" value="ok"/>
            </span>
        </div>
    </form>
 <%}%>       

<%@include file="Footer.jsp" %>
