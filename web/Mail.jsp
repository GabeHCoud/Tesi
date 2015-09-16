<%@page import="java.util.List"%>
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
    if(status.equals("view"))
    {
        mailManagement.view();
    }else if(status.equals("viewFattura"))
    {
        mailManagement.viewFattura();
    }else if(status.equals("viewUser"))
    {
        mailManagement.viewUser();
    }else if(status.equals("send"))
    {
        mailManagement.send();
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
        <b>Seleziona una fattura per inviare le email</b>
    </div>
    <div id="testo">
        <%if( mailManagement.getFatture() != null && ! mailManagement.getFatture().isEmpty())
        {%>
        <form method="post" action="Mail.jsp">
            <select name="idFattura">
                <%for(Fattura f : mailManagement.getFatture())
                {%>
                    <option value="<%=f.IdFattura %>">
                        Fattura del <%=f.Data%>
                    </option>
                <%}%>
            </select>
            <input type="hidden" name="status" value="viewFattura"/>
            <input type="submit" value="Visualizza"/>
        </form>
        <%}else{%>
        Nessuna fattura nel database.
        <%}%>
    </div>               
    
<%}else if(status.equals("viewFattura")){%>
    <div id='titolo'>Fattura del <%=mailManagement.getSelctedFattura().Data%></div>
    <div id="testo">
        <form id="name" name="send" action="Mail.jsp" method="post" onsubmit="return sendOnSubmit(this)">
            <table cellspacing="0"> 
                <tr >
                    <td width='2%'></td>
                    <td width='15%'>
                        <b>Utente</b>
                    </td>                            
                    <td width='83%'><b>Descrizione</b></td>
                </tr> 
              <%for(User u : mailManagement.getUtenti())
                {
                    ArrayList<Consumo> cs = new ArrayList<Consumo>();

                    for(Consumo c : mailManagement.getSelectedConsumi())
                    {      
                        if(c.Email != null && c.Email.equals(u.Email))
                            cs.add(c);
                    }

                    if(!cs.isEmpty())
                    {%>
                    
                    <%if((mailManagement.getUtenti().indexOf(u) % 2) == 0){%>
                    <tr>
                    <%}else{%>
                    <tr class='alternate'>
                    <%}%>
                        <td width='2%'> 
                            <input type="checkbox" name="selectedUsers" value="<%=u.Email%>">
                        </td>
                        <td width='15%'> 
                            <%=u.Cognome%> <%=u.Nome%><br/>
                            <span style="font-size: 12px;"><%=u.Email%></span>    
                            <%
                            int count = 0;
                            for(Mail m : mailManagement.getEmails())
                            {
                                if(m.Email.equals(u.Email) && m.IdFattura == mailManagement.getSelctedFattura().IdFattura)
                                {
                                    count++;
                                }                         
                            }
                            if(count > 0)
                            {%>
                            <br/>
                            <span style="font-size: 10px; font-style: italic; color: red;">Email già inviate per questa fattura: <%=count%></span>
                            <%}%>
                        </td>
                        <td width='83%'>
                            <%for(Consumo c : cs)
                            {%>
                            <div style="width: 100%; display: inline-block;">
                            <div style="float: left; margin-right: 30px; min-width: 300px;">                                
                                <b>Numero: <%=c.Telefono%></b><br/>                            
                                Contributi Ricaricabile Business: <%=c.CRB%><br/>
                                Altri addebiti e accrediti: <%=c.AAA%><br/>
                                Abbonamenti: <%=c.ABB%><br/>
                                Totale: <%=c.Totale%><br/>
                            </div>
                            <div style="float: left; margin-right: 30px; min-width: 300px;">                                              
                                <%
                                boolean found = false;
                                if(mailManagement.getTelefoni() != null && !mailManagement.getTelefoni().isEmpty())
                                {
                                    for(Telefono t : mailManagement.getTelefoni())
                                    {
                                        if(t.Numero.equals(c.Telefono) && t.IdDispositivo > 0 && !mailManagement.getDispositivi().isEmpty())
                                        {
                                            for(Dispositivo d: mailManagement.getDispositivi())
                                            {
                                                if(d.IdDispositivo == t.IdDispositivo)
                                                {
                                                    found = true;
                                                %>
                                                    <b>Dispositivo</b><br/>
                                                    Nome: <%=d.Nome%><br/>
                                                    Costo: <%=d.Costo%><br/><br/>
                                                <%
                                                }
                                            }
                                        }
                                    }
                                }%>
                                <%if(!found)
                                {%>
                                    Nessun dispositivo<br/><br/><br/><br/><br/>
                                <%}%>                                
                            </div>
                            <div style="float: left; margin-right: 30px; min-width: 300px; min-height: 130px;">  
                                <%
                                found = false;
                                if(mailManagement.getTelefoni() != null && !mailManagement.getTelefoni().isEmpty())
                                {
                                    for(Telefono t : mailManagement.getTelefoni())
                                    {
                                        if(t.Numero.equals(c.Telefono) && t.IdContributo > 0 && !mailManagement.getContributi().isEmpty())
                                        {
                                            for(Contributo ca : mailManagement.getContributi())
                                            {
                                                if(ca.IdContributo == t.IdContributo)
                                                {
                                                    found = true;
                                                %>
                                                    <b>Contributo o Abbonamento:</b><br/>
                                                    Nome: <%=ca.Nome%><br/>
                                                    Costo: <%=ca.Costo%>
                                                <%
                                                }
                                            }
                                        }
                                    }
                                }%>
                                <%if(!found)
                                {%>
                                    Nessun contributo o abbonamento <br/><br/><br/><br/><br/>
                                <%}%>  
                            </div>
                            </div>
                            <%}%>
                        </td>
                        </tr>
                    <%}
                }%>
            </table>
            
            <button type="button" onclick="selectAll();">Seleziona tutti</button><button type="button" onclick="deselectAll();">Deseleziona tutti</button>
            <br/> 
            <b>Messaggio<b>
            <br/>       
            <textarea id="messaggio" name="messaggio" rows="20" cols="100">Gentile &lt;nome&gt; &lt;cognome&gt;,&NewLine;&NewLine;Telefono: &lt;telefono&gt;&NewLine;Contributi Ricaricabile Business: &lt;contributi&gt; (&lt;contributiI&gt; con IVA)&NewLine;Altri addebiti e accrediti: &lt;aaa&gt; (&lt;aaaI&gt; con IVA)&NewLine;Abbonamenti: &lt;abb&gt; (&lt;abbI&gt; con IVA)&NewLine;Totale: &lt;totale&gt; (&lt;totaleI&gt; con IVA)&NewLine;&NewLine;Contributi e Abbonamenti: &lt;contributo&gt;&NewLine;Costo: &lt;costo&gt; (&lt;costoI&gt; con IVA)&NewLine;&NewLine;Dispositivo: &lt;dispositivo&gt;&NewLine;Costo: &lt;costo&gt; (&lt;costoI&gt; con IVA)</textarea>
            <br/>
            <input type="hidden" name="idFattura" value="<%=mailManagement.getSelctedFattura().IdFattura%>"/>
            
            Password email: <input type="password" name="password" />
            <input type="hidden" name="status" value="send"/>
            <input type="submit" value="Invia"/>
        </form>
    </div>
    <br/><br/><br/><br/><br/><br/>
<%}else if(status.equals("viewUser")){%>
    
    <%if(!mailManagement.getEmails().isEmpty())
    {%>
    <div id="titolo">
        <b>Cronologia email di <%=mailManagement.getSelectedUser().Cognome%> <%=mailManagement.getSelectedUser().Nome%></b>
    </div>
    <div id="testo">
        <table>
            <tr class="alternate">
                <td width="10%"><b>Data Invio</b></td>
                <td width="10%"><b>Data Fattura</b></td>
                <td width="90%"><b>Testo</b></td>
            </tr>
        <%for(Mail m : mailManagement.getEmails())
        {%>
        <%if((mailManagement.getEmails().indexOf(m) %2) == 0)
        {%>
            <tr>
        <%}else{%>
            <tr class="alternate">
        <%}%>
                <td>
                    <%for(User u : mailManagement.getUtenti())
                    {
                        if(m.Email.equals(u.Email))
                        {%>
                        <%=m.Data%>
                        <%}
                    }%>
                </td>
                <td>
                    <%for(Fattura f : mailManagement.getFatture())
                    {
                        if(m.IdFattura == f.IdFattura)
                        {%>
                        <%=f.Data%>
                        <%}
                    }%>
                </td>
                <td>
                    <%=m.Testo%>
                </td>
            </tr>
        <%}%>
    </div>
    <%}else{%>
    <div id="titolo">
        <b>Cronologia email di <%=mailManagement.getSelectedUser().Cognome%> <%=mailManagement.getSelectedUser().Nome%></b>
    </div>
    <div id="testo">
        Nessuna email inviata 
    </div>
    <%}%>
<%}else if(status.equals("send")){%>  

<div id="titolo"><b>Email inviata con successo a: </b></div>
<div id="testo">
    <%for(String s: mailManagement.getSelectedUsers())
    {
        for(User u : mailManagement.getUtenti())
        {
            if(s.equals(u.Email))
            {
        %>
            <%=u.Cognome%> <%=u.Nome%><br/>
        <%}
        }
    }%>
</div>
<%}%>









<%@include file="Footer.jsp" %>


