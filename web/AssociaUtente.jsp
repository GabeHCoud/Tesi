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
    status=request.getParameter("status");
    
    
    if(status==null)
    {
       status="view";        
    }
    
    if(status.equals("view")){
        userManagement.viewUsers();
    }else if(status.equals("associate")){
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

    }else if(status.equals("register"))
    {
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
    }  
  %>        
  <%@include file="Header.jsp" %>  
    <%if(userManagement.getErrorMessage() != null){%>
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
    <%}else if(status.equals("view")) 
    {%>
        <div>
            <form class="back-button" name="back" method="post" action="Fatture.jsp">
                <input type="hidden" name="idFattura" value="<%=userManagement.getIdFattura()%>"/>
                <input type="hidden" name="status" value="details"/>
                <input type="image" name="submit" src="images/back.png">        
                <br/>
                <span class="f10">indietro</span>
            </form>
            <div class="titolo">
                <b>Seleziona un utente a cui associare il numero</b>
            </div>
            <div class="testo">
                <form name="fassociate" method="post" >                
                    <select name="email">
                        <%for(User u : userManagement.getUtenti())
                        {%>
                        <option value="<%=u.Email%>">
                            <%=u.Cognome%> <%=u.Nome%>
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
                
                
        <form name="fregister" method="post" action="AssociaUtente.jsp" onsubmit="return registerUserOnSubmit(this)">
            <div class="titolo"><b> Registra un nuovo utente per il numero <%=userManagement.getNumero()%></b></div>
            <div class="testo">
                <table class="invisible-table">
                    <tr>
                        <td class="w150p">Nome</td>
                        <td class="w250p">
                            <input type="text" name="nome" size="25" maxlength="50"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="w150p">Cognome</td>
                        <td class="w250p">
                            <input type="text" name="cognome" size="25" maxlength="50"/>
                        </td>
                    </tr>           
                    <tr>
                        <td width="150">Mail</td>
                        <td width="250">
                            <input type="text" name="email" placeholder="esempio: nome.cognome@unife.it" size="25" maxlength="50" />
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