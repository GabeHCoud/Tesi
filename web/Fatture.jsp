<%@page import="java.text.DecimalFormat"%>
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

<jsp:useBean id="consumiManagement" scope="page" class="bflows.ConsumiManagement"/>
<jsp:setProperty name="consumiManagement" property="*"/>

<% 
    Cookie[] cookies;
    String status=request.getParameter("status");    
    String id = null;
    
    if(status==null)   
    {    
        cookies = request.getCookies();
        if(cookies != null && cookies.length == 2)
        {
            status = cookies[0].getValue();
            id=cookies[1].getValue();
        
            for(Cookie c : cookies)
            {
                c.setMaxAge(0);   
                response.addCookie(c);
            }   
        }
    }
    if(status==null)
       status="view";      
    
   
    if(status.equals("view"))
    {
        consumiManagement.viewFatture();
    }if(status.equals("details"))
    {
        if(id != null)
            consumiManagement.setIdFattura(Integer.parseInt(id));     

        consumiManagement.viewFatture();
        consumiManagement.viewConsumi();
    } if(status.equals("editView"))
    {
        consumiManagement.viewEdit();
    }
    if(status.equals("edit"))
    {
        consumiManagement.edit();   
    }if(status.equals("delete")){

        consumiManagement.delete();        
    }   
%>

<%@include file="Header.jsp" %>
 
<%if(consumiManagement.getErrorMessage() != null)
{%>     
    <div id="titolo">
        Si è verificato un Errore!
    </div>
    <div id="testo">
    <%=consumiManagement.getErrorMessage()%>
        <br/><br/><br/>
        <a href="Index.jsp">
            <div class="button" style="width: 150px; margin: 0 auto;">Torna all'Home Page</div>
        </a>
        <br/>
    </div>
<%}else{%>
    <%
    if(status.equals("editView"))
    {%>
            
    <form style="float: left; margin: 10px 20px;" name="back" method="post" action="Fatture.jsp">
        <input type="hidden" name="idFattura" value="<%=consumiManagement.getSelectedFattura().IdFattura%>"/>
        <input type="hidden" name="status" value="details"/>
        <input type="image" name="submit" src="images/back.png">        
        <br/>
        <span style="font-size: 10px;">indietro</span>
    </form>
    <div id="titolo"> 
        <b>Modifica consumi di <%=consumiManagement.getSelectedConsumo().Telefono%> in fattura del <%=consumiManagement.getSelectedFattura().Data%></b>
    </div>
    <div id="testo">
        <form name="edit" method="post" action="Fatture.jsp" onSubmit="return editConsumoOnSubmit(this)">  
        
            <table cellspacing="0" >
                <tr style="background-color:#F0F8FF">
                    <td>
                        <b>Descrizione</b>
                    </td>
                    <td>
                        <b>Importo</b>
                    </td>
                    <td>
                        <b>Modifica</b>
                    </td>
                </tr>
                <tr style="background-color:#F0F8FF">
                    <td>
                        Contributi Ricaricabile Business
                    </td>
                    <td>
                        <%=consumiManagement.getSelectedConsumo().CRB%>
                    </td>
                    <td>
                        <input type="text" name="contributi" value="<%=consumiManagement.getSelectedConsumo().CRB%>">
                    </td>
                </tr>
                <tr style="background-color:#F0F8FF">
                    <td>
                        Altri addebiti e accrediti
                    </td>
                    <td>
                        <%=consumiManagement.getSelectedConsumo().AAA%>
                    </td>
                    <td>
                     <input type="text" name="altri" size="10" maxlength="20" value="<%=consumiManagement.getSelectedConsumo().AAA%>">
                    </td>
                </tr>
                <tr style="background-color:#F0F8FF">
                    <td>
                        Abbonamenti
                    </td>
                    <td>
                        <%=consumiManagement.getSelectedConsumo().ABB%>
                    </td>
                    <td>
                        <input type="text" name="abbonamenti" size="10" maxlength="20" value="<%=consumiManagement.getSelectedConsumo().ABB%>">
                    </td>
                </tr>
                <tr style="background-color:#F0F8FF">
                    <td>
                        Totale
                    </td>
                    <td>
                        <%=consumiManagement.getSelectedConsumo().Totale%>
                    </td>
                    <td>
                        <input type="text" name="totale" size="10" maxlength="20" value="<%=consumiManagement.getSelectedConsumo().Totale%>">
                    </td>            
                </tr>
            </table>
            <input type="hidden" name="status" value="edit"/>
            <input type="hidden" name="idFattura" value="<%=consumiManagement.getSelectedFattura().IdFattura%>"/>
            <input type="hidden" name="numero" value="<%=consumiManagement.getSelectedConsumo().Telefono%>"/>
            <input type="submit" value="Modifica"/>    
        </form> 
    </div>
     
    <%}else{%>
    <div id="titolo">
        <b>Fatture</b>
    </div>
    <div id="testo">
        <%  ArrayList<Fattura> fatture = consumiManagement.getFatture();
            if(fatture != null && !fatture.isEmpty())
            {%>
            <table cellspacing="0"> 
                <tr class="alternate">
                    <td width="200">
                        <b>Data</b>
                    </td>
                    <td width="100">
                        <b>Importo Totale</b>
                    </td> 
                    <td width="100">
                    <center>
                        <b>Dettagli</b>
                    </center>
                    </td> 
                    <td width="100">
                    <center>
                        <b>Elimina</b>
                    </center>                        
                    </td>
                    <td width="100">
                    <center>
                        <b>Invia Mail</b>
                    </center>
                    </td>
                </tr>  

                <%
                for(Fattura f : fatture)
                {
                    if((fatture.indexOf(f) % 2) == 1)//dispari
                    {%>
                    <tr class="alternate">  
                    <%
                    }else //pari
                    {                
                    %>
                    <tr >
                    <%}%>
                        <td width="200"> 
                            Fattura del: <%=f.Data%>
                        </td>            
                        <td width="100">
                            <%=f.Totale%> &euro;
                        </td>
                        <td width="100">
                            <form name="details" method="post" action="Fatture.jsp">
                                <input type="hidden" name="idFattura" value="<%=f.IdFattura%>"/>
                                <input type="hidden" name="status" value="details">
                                <center><input type="image" name="submit" src="images/details.png"></center>
                            </form>
                        </td>
                        <td width="100">
                            <form name="delete" method="post" action="Fatture.jsp" onSubmit="return deleteFatturaOnSubmit();">
                                <input type="hidden" name="idFattura" value="<%=f.IdFattura%>"/>
                                <input type="hidden" name="status" value="delete"/>
                                <center><input type="image" name="submit" src="images/delete.jpg"></center>
                            </form>
                        </td>
                        <td width="100">
                            <form name="viewMail" method="post" action="Mail.jsp">
                                <input type="hidden" name="idFattura" value="<%=f.IdFattura%>"/>
                                <input type="hidden" name="status" value="viewFattura"/>
                                <center><input type="image" name="submit" src="images/mail.jpg"></center>
                            </form>
                        </td>
                    </tr>
                <%}%>  
            </table>
        <%}else{%>
            Nessuna fattura caricata
        <%}            
            if(consumiManagement.getConsumi() != null && !consumiManagement.getConsumi().isEmpty())
            {%>
            <div id="titolo">
                <b>Fattura del: <%=consumiManagement.getSelectedFattura().Data%></b>
            </div>
            <div id="testo">
                <b>Importi Nominali</b><br/>
                Importo Totale: <%=consumiManagement.getSelectedFattura().Totale%> &euro;<br/>
                Contributi e Abbonamenti: <%=consumiManagement.getSelectedFattura().CA%> &euro;<br/>
                Altri Addebiti e Accrediti: <%=consumiManagement.getSelectedFattura().AAA%> &euro;<br/>
                <br/>
                <%
                double contributi = 0;
                double noleggi = 0;
                double altri = 0;
                
                for(Consumo c : consumiManagement.getConsumi())
                {
                    contributi += c.CRB + c.ABB;
                    altri += c.AAA;    
                }               
                //recupero costo del dispositivo associato(se esiste)
                for(Telefono t : consumiManagement.getTelefoni())
                {                    
                    if(t.Numero.equals("329-7506529"))
                        System.out.println("trovato");

                    if(t.IdDispositivo > 0) //c'è un dispositivo associato al numero
                    {
                        for(Dispositivo d : consumiManagement.getDispositivi())
                        {
                            if(d.IdDispositivo == t.IdDispositivo)
                            {
                                noleggi += d.Costo;
                            }
                        }
                    }                    
                }
                
                DecimalFormat df = new DecimalFormat("####.####");   
                %>
                <b>Importi Calcolati</b><br/>
                Contributi e Abbonamenti Calcolati: <%=df.format(contributi)%> &euro;<br/>
                Noleggi Calcolati: <%=df.format(noleggi)%> &euro;<br/>
                Totale Contributi e Abbonamenti: <%=df.format(contributi + noleggi)%> &euro;<br/>
                Altri Addebiti e Accrediti Calcolati: <%=df.format(altri)%> &euro;<br/><br/><br/>
            </div>
            <div id="testo">
                <b>Consumi</b>
                <table cellspacing="0"> 
                    <tr class="alternate">
                        <td width="100">
                            <b>Telefono</b>
                        </td>
                        <td width="200">
                            <b>Descrizione</b>
                        </td>
                        <td width="100">
                            <b>Importo</b>
                        </td>
                        <td width="50">
                        <center>
                            <b>Modifica</b>
                        </center>
                        </td>
                        <td width="50">
                        <center>
                            <b>Utente</b>
                        </center>
                        </td>
                    </tr>   
                    <%
                    for(Consumo c : consumiManagement.getConsumi())
                    {
                        if((consumiManagement.getConsumi().indexOf(c) % 2) == 1)//dispari
                        {%>
                        <tr class="alternate">  
                        <%}else{%>
                        <tr >
                        <%}%>
                        <td width="100">
                            <%=c.Telefono%>
                        </td>
                        <td width="200">
                          Contributi Ricaricabile Business<br/>
                          Altri addebiti e accrediti<br/>
                          Abbonamenti<br/><br/>
                          Totale<br/>
                        </td>
                        <td width="100"><%=c.CRB%><br/>
                            <%=c.AAA%><br/>
                            <%=c.ABB%><br/><br/>
                            <%=c.Totale%><br/>
                        </td>
                        <td width="50">
                            <form name="edit" method="post" action="Fatture.jsp">
                                <input type="hidden" name="idFattura" value="<%=consumiManagement.getIdFattura()%>"/>
                                <input type="hidden" name="numero" value="<%=c.Telefono %>">
                                <input type="hidden" name="status" value="editView">
                                <center><input type="image" name="submit" src="images/edit.jpg"></center>
                            </form>
                        </td>
                        <td width="50">
                            <form name="name" method="post" action="AssociaUtente.jsp">                                
                                <input type="hidden" name="idFattura" value="<%=consumiManagement.getIdFattura()%>"/>
                                <input type="hidden" name="numero" value="<%=c.Telefono%>"/>
                                <input type="hidden" name="status" value="view"/>
                                <% if(c.Email == null){
                                %>
                                <center><input type="image" name="submit" src="images/nouser.jpg"></center>                                
                                <%}else{
                                    for(User u : consumiManagement.getUtenti())
                                    {
                                        if(c.Email.equals(u.Email))
                                        {%>
                                        <center>
                                            <div>
                                                <img src="images/siuser.jpg"><br/>
                                                <b><%=u.Cognome%> <%=u.Nome%></b>
                                            </div>
                                        </center>
                                        <%}
                                    }
                                }%>
                            </form>
                        </td>
                    </tr>                          
                    <%}%>  
                </table>
            </div>
            
        <%}%>
    </div>
    <%}%>
    <br/><br/><br/><br/><br/>
<%}%>
<%@include file="Footer.jsp" %>
