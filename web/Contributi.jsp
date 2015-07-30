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

<jsp:useBean id="contributiManagement" scope="page" class="bflows.ContributiManagement"/>
<jsp:setProperty name="contributiManagement" property="*"/>

<% 
    String status;    
    status = request.getParameter("status");
    
    if(status == null)
        status = "view";
    
    switch(status)
    {
        case "view":
            contributiManagement.view();
            break;
        case "add":
            contributiManagement.add();
            break;
        case "delete":
            contributiManagement.delete();
            break;
        case "viewUserSubscriptions":
            contributiManagement.viewUserSubscriptions();
            break;
        case "addUserSubscription":
            contributiManagement.addUserSubscription();
            break;
        case "deleteUserSubscription":
            contributiManagement.deleteUserSubscription();
            break;
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
<%  }else if(status.equals("view") || status.equals("add") || status.equals("delete"))
    {%>     
    <div id="titolo"><b>Contributi ed Abbonamenti</b></div>
    <div id="testo" >        
        <table cellspacing="0"> 
            <tr class="alternate">
                <td width="50%"><b>Nome</b></td>
                <td width="25%"><center><b>Costo</b></center></td>
                <td width="25%"><center><b>Elimina</b></center></td>
            </tr>
            <%for(Contributo c : contributiManagement.getContributi())
            {%>   
            <tr>
                <td width="50%"> 
                         <%=c.Nome%>
                    </td>
                    <td width="25%">
                    <center><%=c.Costo%></center>
                    </td>
                    <td width="25%">
                        <form name="del" method="post" action="Contributi.jsp">
                            <input type="hidden" name="idContributo" value="<%=c.IdContributo%>">
                            <input type="hidden" name="status" value="delete">
                            <center><input type="image" name="submit" src="images/delete.jpg"></center>
                        </form>
                    </td>
            </tr>
            <%}%>
        </table>        
    </div>
    <div id="titolo"><b>Aggiungi un contributo o abbonamento </b></div>
    <div id="testo">
        <form name="fregister" method="post" action="Contributi.jsp">            
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
                        <input type="text" name="costo" size="25" maxlength="50"/>
                    </td>
                </tr>
                </table>
                
                <input type="hidden" name="status" value="add"/>
                <center><input type="submit" value="Aggiungi"/></center>
        </form>
    </div>  
<%}else if(status.equals("viewUserSubscriptions") || status.equals("addUserSubscription") || status.equals("deleteUserSubscription"))
{%>
    <div id="titolo"><b>Contributi ed abbonamenti di <%=contributiManagement.getUtente().Cognome%> <%=contributiManagement.getUtente().Nome%></b></div>
    <%if(contributiManagement.getUcontributi() != null && contributiManagement.getUcontributi().size() > 0)
    {%>
        <div id="testo" >        
            <table cellspacing="0"> 
                <tr class="alternate">
                    <td width="50%"><b>Nome</b></td>
                    <td width="25%"><center><b>Costo</b></center></td>
                    <td width="25%"><center><b>Elimina</b></center></td>
                </tr>
                <%for(UserContributo uc : contributiManagement.getUcontributi())
                {
                    for(Contributo c : contributiManagement.getContributi())
                    {
                        if(uc.IdContributo == c.IdContributo)
                        {%>   
                        <tr>
                            <td width="50%"> 
                                <%=c.Nome%>
                            </td>
                            <td width="25%">
                            <center><%=c.Costo%></center>
                            </td>
                            <td width="25%">
                                <form name="del" method="post" action="Contributi.jsp">
                                    <input type="hidden" name="idUserContributo" value="<%=uc.IdUtenteContributo %>">
                                    <input type="hidden" name="email" value="<%=contributiManagement.getUtente().Email%>"/>
                                    <input type="hidden" name="status" value="deleteUserSubscription">
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
            Nessun contributo o abbonamento associato all'utente selezionato.
        </div>
    <%}%>
    <div id="titolo"><b>Associa un contributo o un abbonamento a <%=contributiManagement.getUtente().Cognome%> <%=contributiManagement.getUtente().Nome%></b></div>
    <div id="testo">
        <form name="fregister" method="post" action="Contributi.jsp">            
            <select name="idContributo">
                <%for(Contributo c : contributiManagement.getContributi())
                {%>
                <option value="<%=c.IdContributo%>"><%=c.Nome%></option>
                <%}%>
            </select>
                
            <input type="hidden" name="email" value="<%=contributiManagement.getUtente().Email%>"/>
            <input type="hidden" name="status" value="addUserSubscription"/>
            <center><input type="submit" value="Aggiungi"/></center>
        </form>
    </div>
<%}%>
 <%@include file="Footer.jsp" %>