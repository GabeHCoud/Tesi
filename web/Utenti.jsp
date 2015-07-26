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

<jsp:useBean id="userManagement" scope="page" class="bflows.UserManagement"/>
<jsp:setProperty name="userManagement" property="*"/>
        
<% 
    String status;    
    status = request.getParameter("status");
    
    if(status == null)
        status = "view";
    
    switch(status)
    {
        case "view":
            userManagement.viewUsers();
            break;
        case "addUser":
            userManagement.add();
            break;
        case "editUserView":
            userManagement.viewEditUser();
            break;
        case "editUser":
            userManagement.edit();
            break;
        case "deleteUser":
            userManagement.delete();
            break; 
        case "addPhoneView":
            userManagement.viewAddPhone();
            break;
        case "addPhone":
            userManagement.addPhone();
            break;
        case "deletePhone":
            userManagement.deletePhone();
            break;
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
<%  }else if(status.equals("view") || status.equals("editUser") || status.equals("deleteUser") || status.equals("addUser") || status.equals("addPhone") || status.equals("deletePhone"))
    {%>     
    <div id="titolo"><b>Utenti Registrati</b></div>
    <div id="testo"> 
        <table cellspacing="0"> 
            <tr class="alternate">                
                <td width="200">
                    <b>Utente</b>
                </td>
                <td width="200">
                    <b>Telefono</b>
                </td>
                <td width="200">
                    <b>Mail</b>
                </td>
                <td width="80">
                    <b>Modifica</b>
                </td>
                <td width="80">
                    <b>Aggiungi/Elimina numero</b>
                </td>
                <td width="80">
                    <b>Cancella</b>
                </td>
                <td width="80" align="center">
                    <b>Storico Mail</b>
                </td>
                <td width="100" >
                    <b>Dispositivo</b>
                </td>                
            </tr>   
            <% if(userManagement.getUtenti() != null)
            {   
                for(User u : userManagement.getUtenti())
                {
                    if((userManagement.getUtenti().indexOf(u) % 2) == 0)
                    {%>
                    <tr>                    
                        <td width="200">
                             <%=u.Cognome%> <%=u.Nome%> 
                        </td>
                        <td width="200">                        
                            <%for(Telefono t : userManagement.getTelefoni())
                            {
                                if(t.Email.equals(u.Email))
                                {%>
                                <%=t.Numero%><br/>
                                <%}
                            }%>
                        </td>
                        <td width="200">
                            <%=u.Email%>
                        </td>
                        <td width="80">
                            <form name="edit" method="post" action="Utenti.jsp">
                                <input type="hidden" name="email" value="<%=u.Email%>">
                                <input type="hidden" name="status" value="editUserView">
                                <center><input type="image" name="submit" src="images/edit.jpg"></center>
                            </form>
                        </td>
                        <td width="80">
                            <form name="number" method="post" action="Utenti.jsp">
                                <input type="hidden" name="email" value="<%=u.Email%>">
                                <input type="hidden" name="status" value="addPhoneView">
                                <center><input type="image" name="submit" src="images/phone.png"></center>
                            </form>
                        </td>
                        <td width="80">
                            <form name="delete" method="post" action="Utenti.jsp">
                                <input type="hidden" name="email" value="<%=u.Email%>">
                                <input type="hidden" name="status" value="deleteUser">
                                <center><input type="image" name="submit" src="images/delete.jpg"></center>
                            </form>
                        </td>
                        <td width="80">
                                <form name="mail" method="post" action="Mail.jsp">
                                    <input type="hidden" name="email" value="<%=u.Email%>">
                                    <input type="hidden" name="status" value="viewMail">
                                    <center><input type="image" name="submit" src="images/mail.jpg"></center>
                                </form>
                            </td>
                        <td width="100">
                            <form name="disp" method="post" action="Dispositivi.jsp">
                                <input type="hidden" name="email" value="<%=u.Email%>">
                                <input type="hidden" name="status" value="viewDevices">
                                <center><input type="image" name="submit" src="images/mobile.jpg"></center>
                            </form>
                        </td>
                    </tr>  
                
                <% }else{%>
                    <tr class="alternate"> 
                        <td width="200">
                             <%=u.Cognome%> <%=u.Nome%> 
                        </td>
                        <td width="200">                        
                            <%for(Telefono t : userManagement.getTelefoni())
                            {
                                if(t.Email.equals(u.Email))
                                {%>
                                <%=t.Numero%><br/>
                                <%}
                            }%>
                        </td>
                        <td width="200">
                            <%=u.Email%>
                        </td>
                        <td width="80">
                            <form name="edit" method="post" action="Utenti.jsp">
                                <input type="hidden" name="email" value="<%=u.Email%>">
                                <input type="hidden" name="status" value="editUserView">
                                <center><input type="image" name="submit" src="images/edit.jpg"></center>
                            </form>
                        </td>
                        <td width="80">
                            <form name="number" method="post" action="Utenti.jsp">
                                <input type="hidden" name="email" value="<%=u.Email%>">
                                <input type="hidden" name="status" value="addPhoneView">
                                <center><input type="image" name="submit" src="images/phone.png"></center>
                            </form>
                        </td>
                        <td width="80">
                            <form name="delete" method="post" action="Utenti.jsp">
                                <input type="hidden" name="email" value="<%=u.Email%>">
                                <input type="hidden" name="status" value="deleteUser">
                                <center><input type="image" name="submit" src="images/delete.jpg"></center>
                            </form>
                        </td>
                        <td width="80">
                                <form name="mail" method="post" action="Mail.jsp">
                                    <input type="hidden" name="email" value="<%=u.Email%>">
                                    <input type="hidden" name="status" value="view">
                                    <center><input type="image" name="submit" src="images/mail.jpg"></center>
                                </form>
                            </td>
                        <td width="100">
                            <form name="disp" method="post" action="Dispositivi.jsp">
                                <input type="hidden" name="email" value="<%=u.Email%>">
                                <input type="hidden" name="status" value="view">
                                <center><input type="image" name="submit" src="images/mobile.jpg"></center>
                            </form>
                        </td>
                    </tr>  
                    <%}
                }
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
<%}else if(status.equals("editUserView"))
{%>
    <div id="titolo"><b>Modifica Utente</b></div>          
    <div id="testo">
        <form name="edit" method="post" action="Utenti.jsp">
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
<%}else if(status.equals("addPhoneView"))
{%>
    <div id="titolo"><b>Elimina un numero per l'utente <%=userManagement.getSelectedUser().Cognome%> <%=userManagement.getSelectedUser().Nome%></b></div>
    <div id="testo">
        <form name="deleteNumber" method="post" action="Utenti.jsp">
            <input type="hidden" name="status" value="deletePhone"/>
            <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
            <select name="numero">
                <%for(Telefono t : userManagement.getTelefoni())
                {
                    if(t.Email.equals(userManagement.getSelectedUser().Email))
                    {%>
                    <option><%=t.Numero%></option>
                    <%}
                }%>
            </select>
            <input type="submit" value="Elimina"/>
        </form>
    </div>    

    <div id="titolo"><b>Aggiungi un numero per l'utente <%=userManagement.getSelectedUser().Cognome%> <%=userManagement.getSelectedUser().Nome%></b></div>
    <div id="testo">
        <form name="addNumber" method="post" action="Utenti.jsp">
            <input type="hidden" name="status" value="addPhone"/>
            <input type="hidden" name="email" value="<%=userManagement.getSelectedUser().Email%>"/>
            <input type="text" name="numero" maxlength="11"/>
            <input type="submit" value="Aggiungi"/>
        </form>
    </div>
<%}%>
 <%@include file="Footer.jsp" %>
