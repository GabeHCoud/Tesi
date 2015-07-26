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
    status=request.getParameter("status");
    
    
    if(status==null)
    {
       status="view";        
    }
    
    switch(status)
    {
        case "view":
            userManagement.viewUsers();
            break;
        case "associate":
            userManagement.associate();
            
            if(userManagement.getResult() >= 0)
            {
                Integer id = userManagement.getIdFattura();
                Cookie cookie = new Cookie("status","details");
                response.addCookie(cookie);
                cookie = new Cookie("idFattura",id.toString());
                response.addCookie(cookie);
                response.sendRedirect("Fatture.jsp");
            }
            break;
        case "register":
            userManagement.register();
            
            if(userManagement.getResult() >= 0)
            {
                Integer id = userManagement.getIdFattura();
                Cookie cookie = new Cookie("status","details");
                response.addCookie(cookie);
                cookie = new Cookie("idFattura",id.toString());
                response.addCookie(cookie);
                response.sendRedirect("Fatture.jsp");
            }
            break;      
    }  
  %>        
  <%@include file="Header.jsp" %>   
    <%if(userManagement.getErrorMessage() != null){%>
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
    <%}else if(status.equals("view")) 
    {%>
        <div>
            <div id="titolo">
                Seleziona un utente a cui associare il numero
            </div>
            <div id="testo">
                <form name="fassociate" method="post" action="AssociaUtente.jsp">                
                    <select name="email">
                        <%for(User u : userManagement.getUtenti())
                        {%>
                        <option value="<%=u.Email%>">
                            <%=u.Nome%> <%=u.Cognome%>
                        </option>
                        <%}%>
                    </select>
                    <input type="submit" value="Associa"/>
                    
                    <input type="hidden" name="status" value="associate"/>    
                    <input type="hidden" name="numero" value="<%=userManagement.getNumero()%>"/>
                    <input type="hidden" name="idFattura" value="<%=userManagement.getIdFattura()%>"/>
                </form>
            </div>
        </div>  
                
                
        <form name="fregister" method="post" action="AssociaUtente.jsp">
            <div id="titolo"><b> Registra un nuovo utente per il numero <%=userManagement.getNumero()%></b></div>
            <div id="testo">
                <table style="background-color:#F0F8FF;width:auto;padding-top:0px;">
                <tr style="background-color:#F0F8FF;">
                    <td width="150">Nome</td>
                    <td width="250">
                        <input type="text" name="nome" size="25" maxlength="50"/>
                    </td>
                </tr>
                <tr style="background-color:#F0F8FF;">
                    <td width="150">Cognome</td>
                    <td width="250">
                        <input type="text" name="cognome" size="25" maxlength="50"/>
                    </td>
                </tr>           
                <tr style="background-color:#F0F8FF;">
                    <td width="150">Mail</td>
                    <td width="250">
                        <input type="text" name="email" size="25" maxlength="50" />
                    </td>
                </tr>                
            </table>
            
            <input type="hidden" name="status" value="register"/>    
            <input type="hidden" name="numero" value="<%=userManagement.getNumero()%>"/>
            <input type="hidden" name="idFattura" value="<%=userManagement.getIdFattura()%>"/>
            <input type="submit" value="Inserisci"/>
            </div>
        </form>  
    <%}%>
        
  <%@include file="Footer.jsp" %>