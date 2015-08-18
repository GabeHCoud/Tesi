<%@page import="blogics.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="util.Conversion"%>
<%@page contentType="text/html" pageEncoding="ISO-8859-1" session="false"%>
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
    }else if(status.equals("viewEditDevice")){
        dispositiviManagement.viewEditDevice();
    }else if(status.equals("editDevice")){
        dispositiviManagement.editDevice();
    }else if (status.equals("deleteDevice")){
        dispositiviManagement.deleteDevice();            
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
<%  }else if(status.equals("view") || status.equals("addDevice") || status.equals("deleteDevice") || status.equals("editDevice"))
    {%>     

    <div id="titolo"><b>Dispositivi</b></div>
    <div id="testo" >        
        <table cellspacing="0"> 
            <tr class="alternate">
                <td width="50%"><b>Nome</b></td>
                <td width="17%"><center><b>Costo</b></center></td>
                <td width="17%"><center><b>Modifica</b></center></td>
                <td width="17%"><center><b>Elimina</b></center></td>
            </tr>
            <%for(Dispositivo d : dispositiviManagement.getDispositivi())
            {
                if((dispositiviManagement.getDispositivi().indexOf(d) % 2) == 0)
                {%>   
                <tr>
                <%}else{%>
                <tr class="alternate">
                <%}%>            
                    <td width="50%"> 
                         <%=d.Nome%>
                    </td>
                    <td width="17%">
                    <center><%=d.Costo%></center>
                    </td>
                    <td width="17%">
                        <form name="edit" method="post" action="Dispositivi.jsp">
                            <input type="hidden" name="idDispositivo" value="<%=d.IdDispositivo%>">
                            <input type="hidden" name="status" value="viewEditDevice">
                            <center><input type="image" name="submit" src="images/edit.jpg"></center>
                        </form>
                    </td>
                    <td width="17%">
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
<%}else if(status.equals("viewEditDevice")){%>
    <div id="titolo"><b>Modifica dispositivo</b></div>
    <div id="testo" >
        <form name="fregister" method="post" action="Dispositivi.jsp">            
                <table style="background-color:#F0F8FF; width: auto;padding-bottom:0px;padding-top:0px">
                     <tr style="background-color:#F0F8FF;">
                    <td width="150">Nome Dispositivo</td>
                    <td width="250">
                        <input type="text" name="nome" size="50" maxlength="50" value="<%=dispositiviManagement.getSelectedDispositivo().Nome%>"/>
                    </td>
                </tr>
                <tr style="background-color:#F0F8FF;">
                    <td width="150">Costo</td>
                    <td width="250">
                        <input type="text" name="costo" size="25" maxlength="50"  value="<%=dispositiviManagement.getSelectedDispositivo().Costo%>"/>
                    </td>
                </tr>
                </table>
                <input type="hidden" name="idDispositivo" value="<%=dispositiviManagement.getSelectedDispositivo().IdDispositivo%>">                                        
                <input type="hidden" name="status" value="editDevice"/>
                <center><input type="submit" value="Modifica"/></center>
        </form>
    </div>
<%}%>
<%@include file="Footer.jsp" %>
