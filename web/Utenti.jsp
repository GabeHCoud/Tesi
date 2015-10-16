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
<div class="titolo">
    Si è verificato un Errore!
</div>
<div class="testo">
    <%=userManagement.getErrorMessage()%>
    <br/><br/><br/>
    <a href="Index.jsp">
        <div class="button goHome-button">Torna all'Home Page</div>
    </a>
    <br/>
</div>
<%  } else if (status.equals("view") || status.equals("editUser") || status.equals("deleteUser") || status.equals("addUser")) {%>     

<div class="titolo"><b>Aggiungi Utente</b></div>          
<div class="testo">
    <form name="edit" method="post" action="Utenti.jsp" onsubmit="return registerUserOnSubmit(this)">
        <table class="invisible-table">                    
            <tr>
                <td>Nome</td>
                <td>
                    <input type="text" name="nome" size="30" maxlength="20">
                </td>
            </tr>                    
            <tr>
                <td>Cognome</td>
                <td>
                    <input type="text" name="cognome" size="30" maxlength="20">
                </td>
            </tr>                    
            <tr>
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
<div class="titolo"><b>Utenti Registrati</b></div>
<div class="testo"> 
    <%if (userManagement.getUtenti() != null && !userManagement.getUtenti().isEmpty()) 
    {
        String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"}; 
        ArrayList<Character> done = new ArrayList<Character>();
        
        for(String s : alphabet)
        {
            %>
            <a href="#<%=s%>" ><%=s%></a>
            <%
        }        
    %>
    
    <table class="table-classic"> 
        <tr>        
            <td class="w15">
                <b>Utente</b>
            </td>     
            <td class="w100p">
                <b>Fondi</b>
            </td>
            <td class="w100p">
                <b>Numeri</b>
            </td>
            <td class="w200p">
            <center>
                <b>Contributi Abbonamenti</b>
            </center>
            </td>
            <td class="w100p">
            <center>
                <b>Dispositivi</b>
            </center>
            </td>                              
            <td class="w8">
            <center>
                <b>Modifica</b>
            </center>
            </td>         
            <td class="w8">
            <center>
                <b>Cancella</b>
            </center>
            </td>
            <td class="w8">
            <center>
                <b>Storico Mail</b>
            </center>
            </td>
        </tr>  
        <%       
        for (User u : userManagement.getUtenti()) 
        {%>
        <tr>
            <td >
                <%if(!done.contains(u.Cognome.charAt(0))){
                    Character letter = u.Cognome.charAt(0);
                    done.add(letter);                   
                    
                    %><a id="<%=letter%>" ></a>
                <%}%>
                <span class="f18"><%=u.Cognome%> <%=u.Nome%></span><br/>
                <span class="f12"><%=u.Email%></span>            
            </td>
            <td class="w100p">
                <%               
                for(Fondo f : userManagement.getFondi())
                {
                    if(f.Email.equals(u.Email))
                    {                       
                       %>
                       <span class="f12"><%=f.Nome%> 
                           <%if(f.Attivo){%> <img src="images/check.png" class="image14"/> <%}%>
                       </span><br/>
                       <%
                    }
                }%>
            </td>
            <td colspan='3' class="no-padding">  
                <table class='invisible'>                               
                    <%for (Telefono t : userManagement.getTelefoni()) {
                        if (t.Email.equals(u.Email)) {%>                            
                    <tr class='invisible'>
                        <td class="padding6tb w100p" >
                            <%=t.Numero%>
                        </td>
                        <td class="padding6tb w200p">
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
                        <td class="padding6tb w100p">
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
    <br/><br/><br/><br/><br/>
</div>
<%}%>
<%@include file="Footer.jsp" %>
