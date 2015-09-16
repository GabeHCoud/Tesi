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

<jsp:useBean id="contributiManagement" scope="page" class="bflows.ContributiManagement"/>
<jsp:setProperty name="contributiManagement" property="*"/>

<% 
    String status;    
    status = request.getParameter("status");
    
    if(status == null)
        status = "view";
    
    if(status.equals("view")){
        contributiManagement.view();
    } else if(status.equals("addSubscription")){
        contributiManagement.addSubscription();
    }else if (status.equals("viewEditSubscription")){
        contributiManagement.viewEditSubscription();
    }else if (status.equals("editSubscription")){
        contributiManagement.editSubscription();
    }else if (status.equals("deleteSubscription")){
        contributiManagement.deleteSubscription();
    }
    %>
 
<%@include file="Header.jsp" %>
<%  if(contributiManagement.getErrorMessage() != null)
    {%>     
    <div id="titolo">
        Si è verificato un Errore!
    </div>
    <div id="testo">
    <%=contributiManagement.getErrorMessage()%>
        <br/><br/><br/>
        <a href="Index.jsp">
            <div class="button" style="width: 150px; margin: 0 auto;">Torna all'Home Page</div>
        </a>
        <br/>
    </div>
<%  }else if(status.equals("view") || status.equals("addSubscription") || status.equals("deleteSubscription") || status.equals("editSubscription"))
    {%>     
    <div id="titolo"><b>Contributi ed Abbonamenti</b></div>
    <div id="testo" >        
        <table cellspacing="0"> 
            <tr class="alternate">
                <td width="50%"><b>Nome</b></td>
                <td width="17%"><center><b>Costo</b></center></td>
                <td width="17%"><center><b>Modifica</b></center></td>
                <td width="17%"><center><b>Elimina</b></center></td>
            </tr>
            <%for(Contributo c : contributiManagement.getContributi())
            {
                if((contributiManagement.getContributi().indexOf(c) % 2) ==0) 
                {%>   
                <tr>
                <%}else{%>
                <tr class="alternate">
                <%}%>
                    <td width="50%"> 
                             <%=c.Nome%>
                        </td>
                        <td width="17%">
                        <center><%=c.Costo%></center>
                        </td>
                        <td width="17%">
                            <form name="edit" method="post" action="Contributi.jsp">
                                <input type="hidden" name="idContributo" value="<%=c.IdContributo%>">
                                <input type="hidden" name="status" value="viewEditSubscription">
                                <center><input type="image" name="submit" src="images/edit.jpg"></center>
                            </form>
                        </td>
                        <td width="17%">
                            <form name="del" method="post" action="Contributi.jsp" onsubmit="return deleteContributoOnSubmit()">
                                <input type="hidden" name="idContributo" value="<%=c.IdContributo%>">
                                <input type="hidden" name="status" value="deleteSubscription">
                                <center><input type="image" name="submit" src="images/delete.jpg"></center>
                            </form>
                        </td>
                </tr>
            <%}%>
        </table>        
    </div>
    <div id="titolo"><b>Aggiungi un contributo o abbonamento </b></div>
    <div id="testo">
        <form name="fregister" method="post" action="Contributi.jsp" onsubmit="return addContributoOnSubmit(this)">            
                <table style="background-color:#F0F8FF; width: auto;padding-bottom:0px;padding-top:0px">
                    <tr style="background-color:#F0F8FF;">
                        <td width="150">Nome</td>
                        <td width="250">
                            <input type="text" name="nome" size="50" maxlength="50"/>
                        </td>
                    </tr>
                    <tr style="background-color:#F0F8FF;">
                        <td width="150">Costo</td>
                        <td width="250">
                            <input type="text" name="costo" placeholder="esempio: 12.34" size="25" maxlength="50"/>
                        </td>
                    </tr>
                </table>
                
                <input type="hidden" name="status" value="addSubscription"/>
                <center><input type="submit" value="Aggiungi"/></center>
        </form>
    </div>  
<%}else if(status.equals("viewEditSubscription")){%>

    <form style="float: left; margin: 10px 20px;" name="back" method="post" action="Contributi.jsp">
        <input type="hidden" name="status" value="view">
        <input type="image" alt="indietro" name="submit" src="images/back.png">        
        <br/>
        <span style="font-size: 10px;">indietro</span>
    </form>
    <div id="titolo"><b>Modifica contributo o abbonamento</b></div>
    <div id="testo" >
        <form name="fregister" method="post" action="Contributi.jsp" onsubmit="return editContributoOnSubmit(this)">            
                <table style="background-color:#F0F8FF; width: auto;padding-bottom:0px;padding-top:0px">
                     <tr style="background-color:#F0F8FF;">
                        <td width="150">Nome Dispositivo</td>
                        <td width="250">
                            <input type="text" name="nome" size="50" maxlength="50" value="<%=contributiManagement.getSelectedContributo().Nome%>"/>
                        </td>
                    </tr>
                    <tr style="background-color:#F0F8FF;">
                        <td width="150">Costo</td>
                        <td width="250">
                            <input type="text" name="costo" size="25" maxlength="50"  value="<%=contributiManagement.getSelectedContributo().Costo%>"/>
                        </td>
                    </tr>
                </table>
                <input type="hidden" name="idContributo" value="<%=contributiManagement.getSelectedContributo().IdContributo%>">                                        
                <input type="hidden" name="status" value="editSubscription"/>
                <center><input type="submit" value="Modifica"/></center>
        </form>
    </div>
<%}%>
 <%@include file="Footer.jsp" %>