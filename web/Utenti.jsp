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
    <table cellspacing="0"> 
        <tr class="alternate">                
            <td width="15%">
                <b>Utente</b>
            </td>                
            <td width="20%">
                <b>Mail</b>
            </td>  
            <td width="10%">
                <b>Numeri</b>
            </td>
            <td width='21%'>
                <b>Contributi Abbonamenti</b>
            </td>
            <td width='10%'>
                <b>Dispositivi</b>
            </td>                              
            <td width='8%'>
                <b>Modifica</b>
            </td>         
            <td width='8%'>
                <b>Cancella</b>
            </td>
            <td width='8%'>
                <b>Storico Mail</b>
            </td>
        </tr>   
        <%if (userManagement.getUtenti() != null) {
                for (User u : userManagement.getUtenti()) {
                    if ((userManagement.getUtenti().indexOf(u) % 2) == 0) {%>
        <tr>
            <%} else {%>
        <tr class="alternate"> 
            <%}%>
            <td>
                <%=u.Cognome%> <%=u.Nome%> 
            </td>
            <td>
                <%=u.Email%>
            </td>
            <td colspan='3' style='padding-left: 0;padding-right: 0;'>  
                <table class='invisible'>                               
                    <%for (Telefono t : userManagement.getTelefoni()) {
                                    if (t.Email.equals(u.Email)) {%>                            
                    <tr class='invisible'>
                        <td style='padding: 0;'>
                            <%=t.Numero%><br/>
                        </td>
                        <td style='padding: 0;'>
                            <%if (userManagement.getContributi() != null) {
                                                for (Contributo c : userManagement.getContributi()) {
                                                    if (c.IdContributo == t.IdContributo) {%>
                            <span style='font-size: 14px'><%=c.Nome%></span><br/>
                            <%}
                                                        }
                                                    }%>  
                        </td>
                        <td style='padding: 0;'>
                            <%if (userManagement.getDispositivi() != null) {
                                                for (Dispositivo d : userManagement.getDispositivi()) {
                                                    if (d.IdDispositivo == t.IdDispositivo) {%>
                            <span style='font-size: 14px'><%=d.Nome%></span><br/>
                            <%}
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
                <form name="delete" method="post" action="Utenti.jsp">
                    <input type="hidden" name="email" value="<%=u.Email%>">
                    <input type="hidden" name="status" value="deleteUser">
                    <center><input type="image" name="submit" src="images/delete.jpg"></center>
                </form>
            </td>                        
            <td>
                <form name="mail" method="post" action="Mail.jsp">
                    <input type="hidden" name="userId" value="<%=u.Email%>">
                    <input type="hidden" name="status" value="view">
                    <center><input type="image" name="submit" src="images/mail.jpg"></center>
                </form>
            </td> 
        </tr>               
        <%}
                  }%>  
    </table>        

    <div id="titolo"><b>Aggiungi Utente</b></div>          
    <div id="testo">
        <form name="edit" method="post" action="Utenti.jsp">
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
                        <input type="text" name="email" size="50" maxlength="50">
                    </td>
                </tr>
            </table>
            <input type="hidden" name="status" value="addUser"/>
            <input type="submit" value="Aggiungi Utente"/>
        </form>     
    </div>
</div>

<%} else if (status.equals("editPhoneView") || status.equals("editPhone")) {%>      
<div id="titolo"><b>Utente <%=userManagement.getSelectedUser().Cognome%> <%=userManagement.getSelectedUser().Nome%> - Numero <%=userManagement.getSelectedTelefono().Numero%></b></div>

<div id="titolo"><b>Modifica Numero</b></div>          
<div id="testo">
    <form method="post" action="Utenti.jsp">
        <table cellspacing="0" >
            <tr style="background-color:#F0F8FF">  
                <td>Numero</td>      
                <td>
                    <input type="text" name="numero" size="30" maxlength="20" value="<%=userManagement.getSelectedTelefono().Numero%>">
                </td>
            </tr>                
        </table>
        <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
        <input type="hidden" name="status" value="editPhone"/>
        <input type="submit" value="Modifica Numero"/>
    </form>
</div>   
<div id="titolo"><b>Gestione Contributi Abbonamenti</b></div>          
<div id="testo" >        
    <table cellspacing="0"> 
        <tr class="alternate">
            <td width="50%"><b>Nome</b></td>
            <td width="25%"><center><b>Costo</b></center></td>
        <td width="25%"><center><b>Elimina</b></center></td>
        </tr>
        <%for (Contributo c : userManagement.getContributi()) {
                    if (c.IdContributo == userManagement.getSelectedTelefono().IdContributo) {%>   
        <tr>
            <td width="50%"> 
                <%=c.Nome%>
            </td>
            <td width="25%">
        <center><%=c.Costo%></center>
        </td>
        <td width="25%">
            <form name="del" method="post" action="Utenti.jsp">
                <input type="hidden" name="idContributo" value="<%=c.IdContributo%>">
                <input type="hidden" name="numero" value="<%=userManagement.getSelectedTelefono().Numero%>"/>
                <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
                <input type="hidden" name="status" value="deleteSubscription">
                <center><input type="image" name="submit" src="images/delete.jpg"></center>
            </form>
        </td>
        </tr>
        <%  }
                }%>
    </table>        
</div>
<div id="titolo"><b>Aggiungi un contributo o abbonamento </b></div>
<div id="testo">
    <form name="fregister" method="post" action="Contributi.jsp">  
        <select name="idContributo">
            <%for (Contributo c : userManagement.getContributi()) {%>
            <option value="<%=c.IdContributo%>"><%=c.Nome%></option>
            <%}%>
        </select>                
        <input type="hidden" name="numero" value="<%=userManagement.getSelectedTelefono().Numero%>"/>
        <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
        <input type="hidden" name="status" value="addSubscription"/>
        <center><input type="submit" value="Aggiungi"/></center>
    </form>
</div>  

<%}%>
<%@include file="Footer.jsp" %>
