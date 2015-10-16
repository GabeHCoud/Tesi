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
    <div class="titolo">
        Si è verificato un Errore!
    </div>
    <div class="testo">
    <%=dispositiviManagement.getErrorMessage()%>
        <br/><br/><br/>
        <a href="Index.jsp">
            <div class="button goHome-button">Torna all'Home Page</div>
        </a>
        <br/>
    </div>
<%  }else if(status.equals("view") || status.equals("addDevice") || status.equals("deleteDevice") || status.equals("editDevice"))
    {%>     

    <div class="titolo"><b>Dispositivi</b></div>
    <div class="testo" >        
        <table class="table-classic" cellspacing="0"> 
            <tr>
                <td class="w50"><b>Nome</b></td>
                <td class="w17"><center><b>Costo</b></center></td>
                <td class="w17"><center><b>Modifica</b></center></td>
                <td class="w17"><center><b>Elimina</b></center></td>
            </tr>
            <%for(Dispositivo d : dispositiviManagement.getDispositivi())
            {%>
            <tr>
                <td class="w50"> 
                     <%=d.Nome%>
                </td>
                <td class="w17">
                <center><%=d.Costo%></center>
                </td>
                <td class="w17">
                    <form name="edit" method="post" action="Dispositivi.jsp">
                        <input type="hidden" name="idDispositivo" value="<%=d.IdDispositivo%>">
                        <input type="hidden" name="status" value="viewEditDevice">
                        <center><input type="image" name="submit" src="images/edit.jpg"></center>
                    </form>
                </td>
                <td class="w17">
                    <form name="del" method="post" action="Dispositivi.jsp" onsubmit="return deleteDispositivoOnSubmit()">
                        <input type="hidden" name="idDispositivo" value="<%=d.IdDispositivo%>">
                        <input type="hidden" name="status" value="deleteDevice">
                        <center><input type="image" name="submit" src="images/delete.jpg"></center>
                    </form>
                </td>
            </tr>
            <%}%>
        </table>        
    </div>
    <div class="titolo"><b>Aggiungi un dispositivo </b></div>
    <div class="testo" >
        <form method="post" action="Dispositivi.jsp" onsubmit="return addDispositivoOnSubmit(this)">            
                <table class="invisible-table">
                    <tr>
                        <td class="w150p">Nome Dispositivo</td>
                        <td class="w250p">
                            <input type="text" name="nome" size="50" maxlength="50"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="w150p">Costo</td>
                        <td class="w250p">
                            <input type="text" name="costo" size="25" placeholder="esempio: 12.34" maxlength="50"/>
                        </td>
                    </tr>
                </table>
                
                <input type="hidden" name="status" value="addDevice"/>
                <center><input type="submit" value="Aggiungi"/></center>
        </form>
    </div>
<%}else if(status.equals("viewEditDevice")){%>

    <form class="back-button" name="back" method="post" action="Dispositivi.jsp">
        <input type="hidden" name="status" value="view">
        <input type="image" alt="indietro" name="submit" src="images/back.png">        
        <br/>
        <span class="f10">indietro</span>
    </form>
    <div class="titolo"><b>Modifica dispositivo</b></div>
    <div class="testo" >
        <form name="fregister" method="post" action="Dispositivi.jsp" onsubmit="return editDispositivoOnSubmit(this)">            
                <table class="invisible-table">
                    <tr>
                        <td width="150">Nome Dispositivo</td>
                        <td width="250">
                            <input type="text" name="nome" size="50" maxlength="50" value="<%=dispositiviManagement.getSelectedDispositivo().Nome%>"/>
                        </td>
                    </tr>
                    <tr>
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
