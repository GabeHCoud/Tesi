<%@page import="java.io.Writer"%>
<%@page import="java.io.BufferedWriter"%>
<%@page import="java.io.OutputStreamWriter"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Calendar"%>
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
    
    String path= getServletContext().getRealPath("/");
    mailManagement.setAbsolutePath(path);
    
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
    }else if(status.equals("saveDefaultMail"))
    {
        mailManagement.saveDefaultMail();
    }
%>

<%@include file="Header.jsp" %>
<%  if(mailManagement.getErrorMessage() != null)
    {%>     
    <div class="titolo">
        Si è verificato un Errore!
    </div>
    <div class="testo">
    <%=mailManagement.getErrorMessage()%>
        <br/><br/><br/>
        <a href="Index.jsp">
            <div class="button goHome-button">Torna all'Home Page</div>
        </a>
        <br/>
    </div>
<%  }else if(status.equals("view") || status.equals("saveDefaultMail")){%>    
    <div class="titolo">
        <b>Seleziona una fattura per inviare le email</b>
    </div>
    <div class="testo">
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
    <div class="titolo">
        <b>Modifica messaggio</b>
    </div>
    <div class="testo">
        <form name="updateDefaultMessage" method="post" action="Mail.jsp">
    <%if(mailManagement.getMessaggio() == null || mailManagement.getMessaggio().isEmpty())
        {%>
        <textarea id="messaggio" name="messaggio" rows="20" cols="100">Gentile &lt;nome&gt; &lt;cognome&gt;,&#010;&#010;la sua quota per la fattura TIM del &lt;data&gt; (relativa al bimestre &lt;bimestre&gt;) &egrave; complessivamente di &euro; &lt;totale&gt; (Iva inclusa).&#010;La spesa verr&agrave; addebitata sul fondo &lt;fondo&gt; come da lei indicato precedentemente.&#010;Se desidera cambiare il fondo la prego di comunicarmelo, rispondendo a questa mail, entro una settimana.&#010;&#010;Le inviamo di seguito i dettagli:&#010;&#010;Telefono: &lt;telefono&gt;&#010;&#010;Dispositivo &lt;dispositivoNome&gt;: &euro; &lt;dispositivoCosto&gt;&#010;&lt;contributiNome&gt;  (Accesso Internet): &euro; &lt;contributi&gt;&#010;Altri addebiti e accrediti (Ricariche effettuate): &euro; &lt;aaa&gt;&#010;Abbonamenti: &euro; &lt;abb&gt;&#010;Totale: &euro; &lt;totale_i&gt;&#010;&#010;Ringrazio e saluto cordialmente.</textarea>
        <%}else{%>
        <textarea id="messaggio" name="messaggio" rows="20" cols="100"><%=mailManagement.getMessaggio()%></textarea>
        <%}%>
        <br/>
        <input type="hidden" name="status" value="saveDefaultMail"/>
        <input type="submit" value="Salva"/>
        </form>
    </div>
    
<%}else if(status.equals("viewFattura")){%>
    <div class='titolo'>Fattura del <%=mailManagement.getSelctedFattura().Data%></div>
    <div class="testo">
        <%
        String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"}; 
        ArrayList<Character> done = new ArrayList<Character>();
        
        for(String s : alphabet)
        {
            %>
            <a href="#<%=s%>" ><%=s%></a>
            <%
        }  
        %>
        <form id="name" name="send" action="Mail.jsp" method="post" onsubmit="return sendOnSubmit(this)">
            <table class="table-classic" cellspacing="0">                
                <tr>
                    <td class="w2"></td>
                    <td class="w13">
                        <center><b>Utente</b></center>
                    </td>                            
                    <td class="w77"><center><b>Descrizione</b></center></td>
                    <td class="w8"><center><b>Totale<br/>(Iva inc.)</b></center></td>
                </tr> 
              <%
                DecimalFormat dbf = new DecimalFormat("0.00");
                for(User u : mailManagement.getUtenti())
                {
                    ArrayList<Consumo> cs = new ArrayList<Consumo>();

                    for(Consumo c : mailManagement.getSelectedConsumi())
                    {      
                        if(c.Email != null && c.Email.equals(u.Email))
                            cs.add(c);
                    }

                    //if(!cs.isEmpty())
                    //{
                    %>                    
                    <tr>
                        <td class="w2"> 
                            <input type="checkbox" name="selectedUsers" value="<%=u.Email%>">
                        </td>
                        <td class="w13"> 
                            <%if(!done.contains(u.Cognome.charAt(0))){
                                Character letter = u.Cognome.charAt(0);
                                done.add(letter);                   

                                %><a id="<%=letter%>" ></a>
                            <%}%>
                            <%=u.Cognome%> <%=u.Nome%><br/>
                            <span class="f12"><%=u.Email%></span><br/>
                            <span class="f12">Fondo Attivo: 
                            <%
                            for(Fondo f : mailManagement.getFondi())
                            {
                                if(u.Email.equals(f.Email) && f.Attivo)
                                {%>
                                    <%=f.Nome%>
                                <%}
                            }
                            %></span>
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
                            <span class="email-counter f10">Email già inviate per questa fattura: <%=count%></span>
                            <%}%>
                        </td>
                        <td class="w77">
                            <%
                            Double Totale = 0.0;
                            if(!cs.isEmpty()){ //se ci sono consumi in bolletta
                                for(Consumo c : cs)
                                {
                                    Totale+= c.Totale;
                                %>
                                <div class="full-col">
                                    <div class="mail-col consumi">                                
                                        <b>Numero: <%=c.Telefono%></b><br/>                            
                                        Contributi Ricaricabile Business: <%=c.CRB%><br/>
                                        Altri addebiti e accrediti: <%=c.AAA%><br/>
                                        Abbonamenti: <%=c.ABB%><br/>
                                        Totale: <%=c.Totale%><br/>
                                    </div>
                                    <div class="mail-col dispositivi">                                              
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
                                                        Totale+= d.Costo;
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
                                    <div class="mail-col contributi">  
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
                                <%}
                            }else{//altrimenti verifico solo i dispositivi a noleggio
                            %>
                            <div class="full-col">
                                <%if(mailManagement.getTelefoni() != null && !mailManagement.getTelefoni().isEmpty())
                                {
                                    for(Telefono t : mailManagement.getTelefoni())
                                    {
                                        if(t.Email.equals(u.Email))
                                        {
                                            %>
                                            <div class="mail-row consumi">                                
                                                <b>Numero: <%=t.Numero%></b><br/>                            
                                                Contributi Ricaricabile Business: 0.0<br/>
                                                Altri addebiti e accrediti: 0.0<br/>
                                                Abbonamenti: 0.0<br/>
                                                Totale: 0.0<br/>
                                            </div>
                                            <div class="mail-row dispositivi">                                              
                                        <%
                                        boolean found = false;                                        
                                        if(t.IdDispositivo > 0 && !mailManagement.getDispositivi().isEmpty())
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
                                                    Totale+= d.Costo;
                                                }
                                            }
                                        }
                                        if(!found)
                                        {%>
                                            Nessun dispositivo<br/><br/><br/><br/><br/>
                                        <%}%>                                
                                    </div>
                                    <div class="mail-row contributi">  
                                            Nessun contributo o abbonamento <br/><br/><br/><br/><br/>
                                    </div>
                                        <%}
                                    }
                                }%> 
                                </div>
                            <%}%>
                        </td>
                        <td class="w8">  
                            <% Totale = Totale + (Totale * 22/100);%>
                            <center><%=dbf.format(Totale)%> &euro; </center>
                        </td>
                    </tr>
                    <%//}
                }%>
                
            </table>
            <button type="button" onclick="selectAll();">Seleziona tutti</button>   <button type="button" onclick="deselectAll();">Deseleziona tutti</button>
            <br/> 
            <br/> 
            <div class="titolo"><b>Invia Mail</b></div>
            <!--<div class="titolo"><b>Anteprima Messaggio</b></div>
           
            <%if(false || mailManagement.getMessaggio() == null || mailManagement.getMessaggio().isEmpty())
            {%>
            <div class="testo">Gentile &lt;nome&gt; &lt;cognome&gt;,&#010;&#010;la sua quota per la fattura TIM del &lt;data&gt; (relativa al bimestre &lt;bimestre&gt;) &egrave; complessivamente di &euro; &lt;totale&gt;.&#010;La spesa verr&agrave; addebitata sul fondo &lt;fondo&gt; come da lei indicato precedentemente.&#010;Se desidera cambiare il fondo la prego di comunicarmelo, rispondendo a questa mail, entro una settimana.&#010;&#010;Le inviamo di seguito i dettagli:&#010;&#010;Telefono: &lt;telefono&gt;&#010;&#010;Dispositivo &lt;dispositivoNome&gt;: &euro; &lt;dispositivoCosto&gt;&#010;&lt;contributiNome&gt;  (Accesso Internet): &euro; &lt;contributi&gt;&#010;Altri addebiti e accrediti (Ricariche effettuate): &euro; &lt;aaa&gt;&#010;Abbonamenti: &euro; &lt;abb&gt;&#010;Totale: &euro; &lt;totale_i&gt;&#010;&#010;Ringrazio e saluto cordialmente.</div>
            <%}else if(false){%>
            <div class="testo"><%=mailManagement.getMessaggio().replaceAll("&#010;","<br/>")%></div>
            <%}%>
            <br/>-->
            <input type="hidden" name="idFattura" value="<%=mailManagement.getSelctedFattura().IdFattura%>"/>
            Indirizzo email: <input type="text" name="sender" placeholder="esempio: nome.cognome@unife.it" size="25" maxlength="50" /></br>
            Password email: <input type="password" name="password" />
            <input type="hidden" name="status" value="send"/>
            <input type="submit" value="Invia"/>
        </form>
    </div>
    <br/><br/><br/><br/><br/><br/>
<%}else if(status.equals("viewUser")){%>
    
    <%if(!mailManagement.getEmails().isEmpty())
    {%>
    <div class="titolo">
        <b>Cronologia email di <%=mailManagement.getSelectedUser().Cognome%> <%=mailManagement.getSelectedUser().Nome%></b>
    </div>
    <div class="testo">
        <table class="table-classic">
            <tbody>
            <tr>
                <td class="w10"><b>Data Invio</b></td>
                <td class="w10"><b>Data Fattura</b></td>
                <td class="w90"><b>Testo</b></td>
            </tr>
        <%for(Mail m : mailManagement.getEmails())
        {%>
            <tr>
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
                    <%=m.Testo.replace("?","&euro;")%>
                </td>
            </tr>
        <%}%>
            </tbody>
        </table>
    </div>
    <%}else{%>
    <div class="titolo">
        <b>Cronologia email di <%=mailManagement.getSelectedUser().Cognome%> <%=mailManagement.getSelectedUser().Nome%></b>
    </div>
    <div class="testo">
        Nessuna email inviata 
    </div>
    <%}%>
<%}else if(status.equals("send")){%>  

    <%if(mailManagement.getErrorMessage() != null)
    {%>     
        <div class="titolo">
            Si è verificato un Errore!
        </div>
        <div class="testo">
        <%=mailManagement.getErrorMessage()%>
            <br/><br/><br/>
            <a href="Index.jsp">
                <div class="button goHome-button">Torna all'Home Page</div>
            </a>
            <br/>
        </div>
    <%}else{%>
    <div class="titolo"><b>Email inviata con successo a: </b></div>
    <div class="testo">
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
    <%}
}%>









<%@include file="Footer.jsp" %>


