<%@page import="prevblogics.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="util.Conversion"%>
<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
<%//@page import="services.sessionservice.*" %>
<%@page import="util.*"%>

<%@page buffer="40kb" %>
<%@page errorPage="/ErrorPage.jsp" %>

<jsp:useBean id="b" scope="page" class="prevbflows.UserBF"/>
<jsp:setProperty name="b" property="*"/>

<% 
    String status;
    status=request.getParameter("status");
    String tel=null,tel1=null,tel2=null;
    
            if(status==null)
            {
               status="view"; 
               System.out.println("sono in view");
            }
            if(status.equals("view")){}
        
        String message = null;
        
        if(status.equals("continua"))
                {
                    tel1=request.getParameter("tel1");
                    tel2=request.getParameter("tel2");
                    tel=tel1+"-"+tel2;
                    b.Register(tel); }
        
        if (b.getResult()==-1) 
        {
            throw new Exception("Errore nell'applicazion consultare i log!");
        } 
        else if (b.getResult()==-2)
        {
            message = b.getErrorMessage();
            status = "view";
        }
            
  %>        
  <%@include file="/preversion/Header.jsp" %>
   <%if (message!=null) {%>
        <script>alert("<%=message%>");</script>
    <%}%>
    
    <% if ( status.equals("view")) 
    { System.out.println("entro nella form view");%>
        <form name="fregister" method="post">
            <div id="titolo"><b> Registrazione </b></div>
            <div id="testo"> 
                
                <table style="background-color:#F0F8FF; width: auto;padding-bottom:0px;padding-top:0px">
                     <tr style="background-color:#F0F8FF;">
                    <td width="150">Telefono</td>
                    <td width="25">
                        <input type="text" name="tel1" size="3" maxlength="3"/>
                    </td>
                    <td width="3">
                        -
                    </td>
                    <td width="50">
                        <input type="text" name="tel2"size="7" maxlength="7"/>
                    </td>
                </tr>
                </table>
                <table style="background-color:#F0F8FF;width:auto;padding-top:0px;">
                <tr style="background-color:#F0F8FF;">
                    <td width="150">Nome</td>
                    <td width="250">
                        <input type="text" name="nome" size="25" maxlength="50"/>
                    </td>
                </tr>
                <tr style="background-color:#F0F8FF;">
                    <td width="150">Cognome</td>
                    <td width="250">
                        <input type="text" name="cognome" size="25" maxlength="50"/>
                    </td>
                </tr>
                
               
                <tr style="background-color:#F0F8FF;">
                    <td width="150">Mail</td>
                    <td width="250">
                        <input type="text" name="mail" size="25" maxlength="50" />
                    </td>
                </tr>
                
                        </table>
            
            <input type="hidden" name="status" value="continua"/>
         
      
        <span class="tab2"><a href="javascript:controlloReg()"> Inserisci</a></span>
            </div></form>  
        <%}if(status.equals("continua")){%>
        
                <form name="new" method="post" action="/UtenteNew.jsp">
             <div id="titolo">       Utente registrato</div><br/><br/>
                    
                    <input type="hidden" name="status" value="view"/>
                    <div id="testo">Inserire un <input type="submit" value="Nuovo Utente"/>
                   </div>
                </form>
        
        
        <%}%> 
  <%@include file="/preversion/Footer.jsp" %>