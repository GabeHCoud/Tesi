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
    
    if(status == null)
        status = "view";
    
    if (status.equals("editUserView")){
        userManagement.viewEditUser();
    }else if (status.equals("editUser")){
        userManagement.editUser();
    }else if (status.equals("deleteUser")){                
        userManagement.deleteUser(); 
    }else if(status.equals("editPhoneView")){
        userManagement.viewEditPhone();                       
    }else if (status.equals("addPhone")){
        userManagement.addPhone();            
    }else if(status.equals("editPhone")){
        userManagement.editPhone();
    }else if (status.equals("deletePhone")){
        userManagement.deletePhone();   
    }
    %>
<%@include file="Header.jsp" %>

<%  if(userManagement.getErrorMessage() != null)
    {%>     
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
<%}else if(status.equals("editUserView") || status.equals("addPhone") || status.equals("deletePhone") || status.equals("editPhone"))
{%>
    
    <form style="float: left; margin: 10px 20px;" name="back" method="post" action="Utenti.jsp">
        <input type="hidden" name="status" value="view">
        <input type="image" alt="indietro" name="submit" src="images/back.png">        
        <br/>
        <span style="font-size: 10px;">indietro</span>
    </form>
    <div id="titolo">
        <b>Utente <%=userManagement.getSelectedUser().Cognome%> <%=userManagement.getSelectedUser().Nome%></b>
    </div>
    <div id="titolo">
        <b>Modifica Utente</b>
    </div>          
    <div id="testo">
        <form name="edit" method="post" action="Utenti.jsp" onsubmit="return editUserOnSubmit(this)">
            <table cellspacing="0" >
                <tr style="background-color:#F0F8FF">  
                    <td>Nome</td>      
                    <td>
                        <input type="text" name="nome" size="30" maxlength="20" value="<%=userManagement.getSelectedUser().Nome%>">
                    </td>
                </tr>
                <tr style="background-color:#F0F8FF">  
                    <td>Cognome</td>  
                    <td>
                        <input type="text" name="cognome" size="30" maxlength="20" value="<%=userManagement.getSelectedUser().Cognome%>">
                    </td>
                </tr>
                <tr style="background-color:#F0F8FF">  
                    <td>Email</td>
                    <td>
                        <input type="text" name="newemail" size="50" maxlength="50" value="<%=userManagement.getSelectedUser().Email%>">
                    </td>
                </tr>
            </table>
            <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
            <input type="hidden" name="status" value="editUser"/>
            <input type="submit" value="Modifica Utente"/>
        </form>
    </div>    
    <br/>
    <div id="titolo"><b>Gestione Numeri</b></div>          
    <div id="testo">
        <%if(userManagement.getTelefoni() != null && !userManagement.getTelefoni().isEmpty())
        {
            %>
        <table cellspacing="0"> 
            <tr class="alternate">                
                <td width="100">
                    <b>Numero</b>
                </td>
                <td width="100">
                    <b>Contributi Abbonamenti</b>
                </td>
                <td width="100">
                    <b>Dispositivi</b>
                </td>                              
                <td width="50">
                    <b>Modifica</b>
                </td>         
                <td width="50">
                    <b>Cancella</b>
                </td>                
            </tr>   
            <%for(Telefono t : userManagement.getTelefoni())
            {                
                if((userManagement.getTelefoni().indexOf(t) % 2) == 0)
                {%>                
                <tr>
                <%}else{%>
                <tr class="alternate">
                <%}%>
                <td><%=t.Numero%></td>
                
                <td>
                    <%if(userManagement.getContributi() != null)
                    {
                        for(Contributo c : userManagement.getContributi())
                        {
                            if(c.IdContributo == t.IdContributo)
                            {%>
                                <%=c.Nome%>
                            <%}
                        }
                    }%>
                </td>
                <td>
                    <%if(userManagement.getDispositivi() != null)
                    {
                        for(Dispositivo d : userManagement.getDispositivi())
                        {
                            if(d.IdDispositivo == t.IdDispositivo)
                            {%>
                                <%=d.Nome%>
                            <%}
                        }
                    }%>
                </td>
                <td>
                    <form action="ModificaLinea.jsp" method="post">
                        <input type="hidden" name="status" value="editPhoneView"/>
                        <input type="hidden" name="idTelefono" value="<%=t.Id%>"/>
                        <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
                        <center><input type="image" name="submit" src="images/edit.jpg"></center>
                    </form>
                </td>
                <td>
                    <form action="ModificaUtente.jsp" method="post" onsubmit="return deleteTelefonoOnSubmit()">
                        <input type="hidden" name="status" value="deletePhone"/>
                        <input type="hidden" name="idTelefono" value="<%=t.Id%>"/>
                        <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
                        <center><input type="image" name="submit" src="images/delete.jpg"></center>
                    </form>
                </td>
            </tr>                    
            <%}%>
        </table>
        <%}else{%>
        <div>Non sono associati numeri all'utente selezionato</div>
        <%}%>
    </div>        
    <br/>
    <div id="titolo"><b>Aggiungi un Numero</b></div>
    <div id="testo">
        <form name="addNumber" method="post" action="ModificaUtente.jsp" onsubmit="return addTelefonoOnSubmit(this)">
            <input type="hidden" name="status" value="addPhone"/>
            <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
            <input type="text" name="numero" placeholder="esempio: 123-1234567" maxlength="11"/>
            <input type="submit" value="Aggiungi"/>
        </form>
    </div>
    <br/><br/><br/>
<%}%>

<%@include file="Footer.jsp" %>
