<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="util.Conversion"%>
<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
<%//@page import="services.sessionservice.*" %>
<%@page import="util.*"%>

<%@page buffer="40kb" %>
<%@page errorPage="/ErrorPage.jsp" %>

<jsp:useBean id="b" scope="page" class="prevbflows.FieldBF"/>
<jsp:setProperty name="b" property="*"/>
        
<% //inserire data
    String status=null;

    status=request.getParameter("status");
    if(status==null)
    {
       status="view"; 
       System.out.println("sono in view");
    }

    

%>

<%@include file="/preversion/Header.jsp" %>
          
<%  if(status.equals("view")){%>
   
     <form name="form1" action="/let.jsp" method="post" enctype="multipart/form-data">      
     
         <div id="titolo"><b>Tabella costi per telefono</b></div>
           
        
       
        <div id="testo">Carica il file<br/><br/><input type="file" name="file" value="" width="100"/>
            <span style="padding: 0 50px;">
                <input type="submit" value="ok"/>
            </span>
        </div>
        </form>
        <!--<form name="form2" action="let2.jsp" method="post" enctype="multipart/form-data">
            Tabella attivazione<br/><input type="file" name="file" value="" width="100"/>
            <span style="padding: 0 50px;">
                <input type="submit" value="ok"/>
            </span>  
        </form>
        <form name="form3" action="let3.jsp" method="post" enctype="multipart/form-data">
            Tabella servizi<br/><input type="file" name="file" value="" width="100"/>
            <span style="padding: 0 50px;">
                <input type="submit" value="ok"/>
            </span>  
        </form>-->
     
 <%}%>       

<%@include file="/preversion/Footer.jsp" %>
