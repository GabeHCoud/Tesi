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
    }else if (status.equals("editFondoView")){
        userManagement.viewEditFondo();
    }else if (status.equals("addFondo")){
        userManagement.addFondo();
    }else if (status.equals("deleteFondo")){
        userManagement.deleteFondo();
    }else if (status.equals("editFondo")){  
        userManagement.editFondo();
    }else if (status.equals("activateFondo")){
        userManagement.activateFondo();    
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
<%}else if(status.equals("editUserView") || status.equals("addPhone") || status.equals("deletePhone") || status.equals("editPhone") || status.equals("addFondo") || status.equals("deleteFondo") || status.equals("editFondo") || status.equals("activateFondo"))
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
    <div id="titolo">
        <b>Gestione Fondi</b>
    </div>  
    <%if(userManagement.getFondi() != null && !userManagement.getFondi().isEmpty())
    {%>
    <div id="testo">       
        <table cellspacing="0"> 
            <tr class="alternate">                
                <td width="80%">
                    <b>Nome</b>
                </td>   
                <td width="5%">
                    <center>
                        <b>Attivo</b>
                    </center>   
                </td>
                <td width="5%">
                    <center>
                        <b>Imposta Attivo</b>
                    </center>   
                </td>
                <td width="5%">
                    <b>Modifica</b>
                </td>         
                <td width="5%">
                    <b>Cancella</b>
                </td>                
            </tr>   
            <%for(Fondo f : userManagement.getFondi())
            {    
                if(f.Email.equals(userManagement.getSelectedUser().Email))
                {
                    if((userManagement.getFondi().indexOf(f) % 2) == 0)
                    {%>                
                    <tr>
                    <%}else{%>
                    <tr class="alternate">
                    <%}%>
                    <td><%=f.Nome%></td>  
                    <td>
                        <center>
                            <%if(f.Attivo){%> 
                            <img src="images/on.png" style="width: 24px;height: 24px;"/>
                            <%}else{%>
                            <img src="images/off.png" style="width: 24px;height: 24px;"/>
                            <%}%>
                        </center>
                    </td>
                    <td>
                        <center>
                            <%if(!f.Attivo){%> 
                            <form action="ModificaUtente.jsp" method="post">
                                <input type="hidden" name="status" value="activateFondo"/>
                                <input type="hidden" name="idFondo" value="<%=f.Id%>"/>
                                <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
                                <center>
                                    <input type="image" name="submit" style="width: 32px;height: 32px;" src="images/check.png"/>
                                </center>
                            </form>
                            <%}%>
                        </center>
                    </td>
                    <td>
                        <form action="ModificaUtente.jsp" method="post">
                            <input type="hidden" name="status" value="editFondoView"/>
                            <input type="hidden" name="idFondo" value="<%=f.Id%>"/>
                            <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
                            <center><input type="image" name="submit" src="images/edit.jpg"></center>
                        </form>
                    </td>
                    <td>
                        <form action="ModificaUtente.jsp" method="post" onsubmit="return deleteFondoOnSubmit()">
                            <input type="hidden" name="status" value="deleteFondo"/>
                            <input type="hidden" name="idFondo" value="<%=f.Id%>"/>
                            <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
                            <center><input type="image" name="submit" src="images/delete.jpg"></center>
                        </form>
                    </td>
                </tr>                    
                <%}
            }%>
        </table>        
    </div>  
    <%}else{%>
    <div>Non sono associati fondi all'utente selezionato</div>
    <%}%>
    <br/>
    <div id="titolo"><b>Aggiungi un Fondo</b></div>
    <div id="testo">
        <form name="addFondo" method="post" action="ModificaUtente.jsp">
            <input type="hidden" name="status" value="addFondo"/>
            <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
            <input type="text" name="nome"/>
            <input type="submit" value="Aggiungi"/>
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
                        <input type="hidden" name="idTelefono" value="<%=t.IdTelefono%>"/>
                        <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
                        <center><input type="image" name="submit" src="images/edit.jpg"></center>
                    </form>
                </td>
                <td>
                    <form action="ModificaUtente.jsp" method="post" onsubmit="return deleteTelefonoOnSubmit()">
                        <input type="hidden" name="status" value="deletePhone"/>
                        <input type="hidden" name="idTelefono" value="<%=t.IdTelefono%>"/>
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
<%}else if (status.equals("editFondoView")){
%>
    <form style="float: left; margin: 10px 20px;" name="back" method="post" action="ModificaUtente.jsp">
        <input type="hidden" name="status" value="editUserView">
        <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
        <input type="image" alt="indietro" name="submit" src="images/back.png">        
        <br/>
        <span style="font-size: 10px;">indietro</span>
    </form>
    <div id="titolo">
        <b>Utente <%=userManagement.getSelectedUser().Cognome%> <%=userManagement.getSelectedUser().Nome%></b>
    </div>
    <div id="titolo"><b>Modifica Fondo</b></div>          
    <div id="testo">
        <form method="post" action="ModificaUtente.jsp" onsubmit="return editFondoOnSubmit(this);">
            <table cellspacing="0" >
                <tr style="background-color:#F0F8FF">  
                    <td>Fondo</td>      
                    <td>
                        <input type="text" name="nome" size="30" value="<%=userManagement.getSelectedFondo().Nome%>">
                    </td>
                </tr>                
            </table>
            <input type="hidden" name="idFondo" value="<%=userManagement.getSelectedFondo().Id%>"/>
            <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
            <input type="hidden" name="status" value="editFondo"/>
            <input type="submit" value="Modifica Fondo"/>
        </form>
    </div> 
<%}%>

<%@include file="Footer.jsp" %>
