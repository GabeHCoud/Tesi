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

<jsp:useBean id="userManagement" scope="page" class="bflows.UserManagement"/>
<jsp:setProperty name="userManagement" property="*"/>

<% 
    String status;
    status = request.getParameter("status");

    if (status == null) {
        status = "view";
    }

    if (status.equals("view")) {
        userManagement.viewUsers();
    } else if (status.equals("addUser")) {
        userManagement.addUser();
    } else if (status.equals("editUser")) {
        userManagement.editUser();
    } else if (status.equals("deleteUser")) {
        userManagement.deleteUser();
    }
%>

<%@include file="Header.jsp" %>
<%  if (userManagement.getErrorMessage() != null) {%>     
<div id="titolo">
    Si è verificato un Errore!
</div>
<div id="testo">
    <%=userManagement.getErrorMessage()%>
    <br/><br/><br/>
    <a href="Index.jsp">
        <div class="button" style="width: 150px; margin: 0 auto;">Torna all'Home Page</div>
    </a>
    <br/>
</div>
<%  } else if (status.equals("view") || status.equals("editUser") || status.equals("deleteUser") || status.equals("addUser")) {%>     
<div id="titolo"><b>Utenti Registrati</b></div>
<div id="testo"> 
    <%if (userManagement.getUtenti() != null && !userManagement.getUtenti().isEmpty()) 
    {%>
    <table cellspacing="0"> 
        <tr class="alternate">                
            <td width="15%">
                <b>Utente</b>
            </td>     
            <td width="100">
                <b>Fondi</b>
            </td>
            <td width="100">
                <b>Numeri</b>
            </td>
            <td width='200'>
            <center>
                <b>Contributi Abbonamenti</b>
            </center>
            </td>
            <td width='100'>
            <center>
                <b>Dispositivi</b>
            </center>
            </td>                              
            <td width='8%'>
            <center>
                <b>Modifica</b>
            </center>
            </td>         
            <td width='8%'>
            <center>
                <b>Cancella</b>
            </center>
            </td>
            <td width='8%'>
            <center>
                <b>Storico Mail</b>
            </center>
            </td>
        </tr>  
        
        <%       
        for (User u : userManagement.getUtenti()) 
        {            
            if ((userManagement.getUtenti().indexOf(u) % 2) == 0) {%>
        <tr>
            <%} else {%>
        <tr class="alternate"> 
            <%}%>
            <td >
                <span style="font-size: 18px;"><%=u.Cognome%> <%=u.Nome%></span><br/>
                <span style="font-size: 12px;"><%=u.Email%></span>            
            </td>
            <td width="100">
                <%               
                for(Fondo f : userManagement.getFondi())
                {
                    if(f.Email.equals(u.Email))
                    {                       
                       %>
                       <span style="font-size: 12px;"><%=f.Nome%> 
                           <%if(f.Attivo){%> <img src="images/check.png" style="width: 14px;height: 14px;"/> <%}%>
                       </span><br/>
                       <%
                    }
                }%>
            </td>
            <td colspan='3' style='padding-left: 0;padding-right: 0;'>  
                <table class='invisible'>                               
                    <%for (Telefono t : userManagement.getTelefoni()) {
                        if (t.Email.equals(u.Email)) {%>                            
                    <tr class='invisible'>
                        <td style='padding-bottom: 6px;padding-top: 6px;width: 100px;'>
                            <%=t.Numero%>
                        </td>
                        <td style='padding-bottom: 6px;padding-top: 6px;width: 200px;'>
                            <%if (userManagement.getContributi() != null) {
                                for (Contributo c : userManagement.getContributi()) {
                                    if (c.IdContributo == t.IdContributo) {%>
                        <center>
                            <%=c.Nome%><br/>
                        </center>
                            <%      }
                                }
                            }%>  
                        </td>
                        <td style='padding-bottom: 6px;padding-top: 6px;width: 100px;'>
                            <%if (userManagement.getDispositivi() != null) {
                                for (Dispositivo d : userManagement.getDispositivi()) {
                                    if (d.IdDispositivo == t.IdDispositivo) {%>
                        <center>
                            <%=d.Nome%><br/>
                        </center>
                            <%      }
                                }
                            }%>
                        </td>
                    </tr>
                    <%}
                                    }%>
                </table>  
            </td>   
            <td>
                <form name="edit" method="post" action="ModificaUtente.jsp">
                    <input type="hidden" name="email" value="<%=u.Email%>">
                    <input type="hidden" name="status" value="editUserView">
                    <center><input type="image" name="submit" src="images/edit.jpg"></center>
                </form>
            </td>
            <td>
                <form name="delete" method="post" action="Utenti.jsp" onSubmit="return deleteUserOnSubmit()">
                    <input type="hidden" name="email" value="<%=u.Email%>">
                    <input type="hidden" name="status" value="deleteUser">
                    <center><input type="image" name="submit" src="images/delete.jpg"></center>
                </form>
            </td>                        
            <td>
                <form name="mail" method="post" action="Mail.jsp">
                    <input type="hidden" name="userId" value="<%=u.Email%>">
                    <input type="hidden" name="status" value="viewUser">
                    <center><input type="image" name="submit" src="images/mail.jpg"></center>
                </form>
            </td> 
        </tr>               
      <%}%>         
    </table>        
    <%}else{%>
    Nessun utente nel database
    <%}%>
    <div id="titolo"><b>Aggiungi Utente</b></div>          
    <div id="testo">
        <form name="edit" method="post" action="Utenti.jsp" onsubmit="return registerUserOnSubmit(this)">
            <table cellspacing="0" >                    
                <tr style="background-color:#F0F8FF">  
                    <td>Nome</td>
                    <td>
                        <input type="text" name="nome" size="30" maxlength="20">
                    </td>
                </tr>                    
                <tr style="background-color:#F0F8FF">      
                    <td>Cognome</td>
                    <td>
                        <input type="text" name="cognome" size="30" maxlength="20">
                    </td>
                </tr>                    
                <tr style="background-color:#F0F8FF">
                    <td>Email</td>
                    <td>
                        <input type="text" name="email" placeholder="esempio: nome.cognome@unife.it" size="50" maxlength="50">
                    </td>
                </tr>
            </table>
            <input type="hidden" name="status" value="addUser"/>
            <input type="submit" value="Aggiungi Utente"/>
        </form>     
    </div>
    <br/><br/><br/><br/><br/>
</div>
<%}%>
<%@include file="Footer.jsp" %>
