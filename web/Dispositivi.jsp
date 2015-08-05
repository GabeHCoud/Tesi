<%@page import="blogics.*"%>
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

<jsp:useBean id="dispositiviManagement" scope="page" class="bflows.DispositiviManagement"/>
<jsp:setProperty name="dispositiviManagement" property="*"/>

<% 
    String status;    
    status = request.getParameter("status");
    
    if(status == null)
        status = "view";
    
    if (status.equals("view")){
            dispositiviManagement.view();
    }else if (status.equals("addDevice")){
            dispositiviManagement.addDevice();
    }else if (status.equals("deleteDevice")){
            dispositiviManagement.deleteDevice();
            
    }else if (status.equals("viewUserDevices")){
            dispositiviManagement.viewUserDevices();
            
    }else if (status.equals("addUserDevice")){
            dispositiviManagement.addUserDevice();
    }else if (status.equals("deleteUserDevice")){
            dispositiviManagement.deleteUserDevice();
            
    }
    %>
 
<%@include file="Header.jsp" %>
<%  if(dispositiviManagement.getErrorMessage() != null)
    {%>     
    <div id="titolo">
        Si è verificato un Errore!
    </div>
    <div id="testo">
    <%=dispositiviManagement.getErrorMessage()%>
        <br/><br/><br/>
        <a href="Index.jsp">
            <div class="button" style="width: 150px; margin: 0 auto;">Torna all'Home Page</div>
        </a>
        <br/>
    </div>
<%  }else if(status.equals("view") || status.equals("addDevice") || status.equals("deleteDevice"))
    {%>     

    <div id="titolo"><b>Dispositivi</b></div>
    <div id="testo" >        
        <table cellspacing="0"> 
            <tr class="alternate">
                <td width="50%"><b>Nome</b></td>
                <td width="25%"><center><b>Costo</b></center></td>
                <td width="25%"><center><b>Elimina</b></center></td>
            </tr>
            <%for(Dispositivo d : dispositiviManagement.getDispositivi())
            {%>   
            <tr>
                <td width="50%"> 
                         <%=d.Nome%>
                    </td>
                    <td width="25%">
                    <center><%=d.Costo%></center>
                    </td>
                    <td width="25%">
                        <form name="del" method="post" action="Dispositivi.jsp">
                            <input type="hidden" name="idDispositivo" value="<%=d.IdDispositivo%>">
                            <input type="hidden" name="status" value="deleteDevice">
                            <center><input type="image" name="submit" src="images/delete.jpg"></center>
                        </form>
                    </td>
            </tr>
            <%}%>
        </table>        
    </div>
    <div id="titolo"><b>Aggiungi un dispositivo </b></div>
    <div id="testo" >
        <form name="fregister" method="post" action="Dispositivi.jsp">            
                <table style="background-color:#F0F8FF; width: auto;padding-bottom:0px;padding-top:0px">
                     <tr style="background-color:#F0F8FF;">
                    <td width="150">Nome Dispositivo</td>
                    <td width="250">
                        <input type="text" name="nome" size="50" maxlength="50"/>
                    </td>
                </tr>
                <tr style="background-color:#F0F8FF;">
                    <td width="150">Costo</td>
                    <td width="250">
                        <input type="text" name="costo" size="25" maxlength="50"/>
                    </td>
                </tr>
                </table>
                
                <input type="hidden" name="status" value="addDevice"/>
                <center><input type="submit" value="Aggiungi"/></center>
        </form>
    </div>
<%}else if(status.equals("viewUserDevices") || status.equals("addUserDevice") || status.equals("deleteUserDevice"))
{%>
    <div id="titolo"><b>Dispositivi di <%=dispositiviManagement.getUtente().Cognome%> <%=dispositiviManagement.getUtente().Nome%></b></div>
    <%if(dispositiviManagement.getUdispositivi() != null && dispositiviManagement.getUdispositivi().size() > 0)
    {%>
        <div id="testo">     
            <table cellspacing="0"> 
                <tr class="alternate">
                    <td width="50%"><b>Nome</b></td>
                    <td width="25%"><center><b>Costo</b></center></td>
                    <td width="25%"><center><b>Elimina</b></center></td>
                </tr>
                <%for(UserDispositivo ud : dispositiviManagement.getUdispositivi())
                {
                    for(Dispositivo d : dispositiviManagement.getDispositivi())
                    {
                        if(ud.IdDispositivo == d.IdDispositivo)
                        {%>   
                        <tr>
                            <td width="50%"> 
                                <%=d.Nome%>
                            </td>
                            <td width="25%">
                            <center><%=d.Costo%></center>
                            </td>
                            <td width="25%">
                                <form name="del" method="post" action="Dispositivi.jsp">
                                    <input type="hidden" name="idUserDispositivo" value="<%=ud.IdUtenteDispositivo%>">
                                    <input type="hidden" name="email" value="<%=dispositiviManagement.getUtente().Email%>"/>
                                    <input type="hidden" name="status" value="deleteUserDevice">
                                    <center><input type="image" name="submit" src="images/delete.jpg"></center>
                                </form>
                            </td>
                        </tr>
                        <%}
                    }
                }%>
            </table>        
        </div>
    <%}else{%>
        <div id="testo">  
            Nessun dispositivo associato all'utente selezionato.
        </div>
    <%}%>   
        
    <div id="titolo"><b>Associa un dispositivo a <%=dispositiviManagement.getUtente().Cognome%> <%=dispositiviManagement.getUtente().Nome%></b></div>
    <div id="testo">
        <form name="fregister" method="post" action="Dispositivi.jsp">            
            <select name="idDispositivo">
                <%for(Dispositivo d : dispositiviManagement.getDispositivi())
                {%>
                <option value="<%=d.IdDispositivo%>"><%=d.Nome%></option>
                <%}%>
            </select>
                
            <input type="hidden" name="email" value="<%=dispositiviManagement.getUtente().Email%>"/>
            <input type="hidden" name="status" value="addUserDevice"/>
            <center><input type="submit" value="Aggiungi"/></center>
        </form>
    </div>
<%}%>
<%@include file="Footer.jsp" %>
