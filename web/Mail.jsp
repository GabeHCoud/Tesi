<%@page import="java.util.List"%>
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

<jsp:useBean id="mailManagement" scope="page" class="bflows.MailManagement"/>
<jsp:setProperty name="mailManagement" property="*"/>
        
<% 
    String status=null;            
    status=request.getParameter("status");

    if(status==null)
    {
       status="view"; 
       System.out.println("sono in view");
    }
            
    switch(status)
    {
        case "view":
            mailManagement.view();
            break;
        case "viewFattura":
            mailManagement.viewFattura();
            break;        
    }
%>

<%@include file="Header.jsp" %>
<%  if(mailManagement.getErrorMessage() != null)
    {%>     
    <div id="titolo">
        Si è verificato un Errore!
    </div>
    <div id="testo">
    <%=mailManagement.getErrorMessage()%>
        <br/><br/><br/>
        <a href="Index.jsp">
            <div class="button" style="width: 150px; margin: 0 auto;">Torna all'Home Page</div>
        </a>
        <br/>
    </div>
<%  }else if(status.equals("view")){%>
    <div id="titolo">
        <b>Seleziona un utente per visualizzare la cronologia delle mail</b>
    </div>
    <div id="testo">
        <form method="post" action="Mail.jsp">
            <select name="userId">
                <%for(User u : mailManagement.getUtenti())
                {%>
                    <option value="<%=u.Email%>">
                        <%=u.Nome%> <%=u.Cognome%>
                    </option>
                <%}%>
            </select>
            <input type="hidden" name="status" value="view"/>
            <input type="submit" value="Visualizza"/>
        </form>
    </div>
                
    <%if(mailManagement.getUserId() != null)//utente selezionato
    {%>
    <div id="titolo">
        <b>Cronologia Email di <%=mailManagement.getSelectedUser().Cognome%> <%=mailManagement.getSelectedUser().Nome%></b>
    </div>
    <div id="testo">
        <table>
            <tr>
                <td>Data</td>
                <td>Messaggio</td>
            </tr>
            <%for(Mail m : mailManagement.getEmails())
            {
                if((mailManagement.getEmails().indexOf(m) % 2) ==0)
                {%>
            <tr>
                <td><%=m.Data%></td>
                <td><%=m.Testo%></td>
            </tr>
            <%}else{%>
            <tr class="alternate">
                <td><%=m.Data%></td>
                <td><%=m.Testo%></td>
            </tr>  
                <%}
            }%>
        </table>
    </div>     
    <%}%>
<%}else if(status.equals("viewFattura")){%>
    <div id='titolo'>Fattura del <%=mailManagement.getSelctedFattura().Data%></div>
    <div id="testo">
        <form name="send" action="Mail.jsp" method="post">
            <table cellspacing="0"> 
                <tr >
                    <td></td>
                    <td>
                        <b>Utente</b>
                    </td>                            
                    <td><b>Descrizione</b></td>
                </tr> 
              <%for(User u : mailManagement.getUtenti())
                {
                    ArrayList<Consumo> cs = new ArrayList<>();

                    for(Consumo c : mailManagement.getSelectedConsumi())
                    {      
                        if(c.Email != null && c.Email.equals(u.Email))
                            cs.add(c);
                    }

                    if(!cs.isEmpty())
                    {%>
                    <tr class='alternate'>
                        <td> 
                            <input type="checkbox" name="selectedUsers" value="<%=u.Email%>" onclick="Quanti(this)">
                        </td>
                        <td> 
                            <%=u.Nome%> <%=u.Cognome%>
                        </td>
                        <td>
                            <%for(Consumo c : cs)
                            {%>
                            <b>Linea: <%=c.Telefono%></b><br/>                            
                            Contributi Ricaricabile Business: <%=c.CRB%><br/>
                            Altri addebiti e accrediti: <%=c.AAA%><br/>
                            Abbonamenti: <%=c.ABB%><br/>
                            Totale: <%=c.Totale%><br/><br/>
                            <%}%>
                        </td>
                        </tr>
                    <%}
                }%>
            </table>
            
        <input type="hidden" name="status" value="send"/>
        <input type="submit" value="Invia"/>
        </form>
    </div>
<%}else if(status.equals("send")){%>

    
    <%for(String s: mailManagement.getSelectedUsers())
    {
    %>
    Inviata a: <%=s%><br/>
    <%}%>
    
<%}%>
<%@include file="Footer.jsp" %>


