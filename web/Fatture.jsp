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
    <div class="titolo">
        Si è verificato un Errore!
    </div>
    <div class="testo">
    <%=consumiManagement.getErrorMessage()%>
        <br/><br/><br/>
        <a href="Index.jsp">
            <div class="button goHome-button">Torna all'Home Page</div>
        </a>
        <br/>
    </div>
<%}else{%>
    <%
    if(status.equals("editView"))
    {%>
            
    <form class="back-button" name="back" method="post" action="Fatture.jsp">
        <input type="hidden" name="idFattura" value="<%=consumiManagement.getSelectedFattura().IdFattura%>"/>
        <input type="hidden" name="status" value="details"/>
        <input type="image" name="submit" src="images/back.png">        
        <br/>
        <span class="f10">indietro</span>
    </form>
    <div class="titolo"> 
        <b>Modifica consumi di <%=consumiManagement.getSelectedConsumo().Telefono%> in fattura del <%=consumiManagement.getSelectedFattura().Data%></b>
    </div>
    <div class="testo">
        <form name="edit" method="post" action="Fatture.jsp" onSubmit="return editConsumoOnSubmit(this)">  
        
            <table class="invisible-table" cellspacing="0" >
                <tr>
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
                <tr>
                    <td>
                        Contributi Ricaricabile Business
                    </td>
                    <td>
                        <%=consumiManagement.getSelectedConsumo().CRB%>
                    </td>
                    <td>
                        <input type="text" name="contributi" size="10" maxlength="20" value="<%=consumiManagement.getSelectedConsumo().CRB%>">
                    </td>
                </tr>
                <tr>
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
                <tr>
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
                <tr>
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
    <div class="titolo">
        <b>Fatture</b>
    </div>
    <div class="testo">
        <%  ArrayList<Fattura> fatture = consumiManagement.getFatture();
            if(fatture != null && !fatture.isEmpty())
            {%>
            <table class="table-classic" cellspacing="0"> 
                <tr>
                    <td class="w200p">
                        <b>Data</b>
                    </td>
                    <td class="w100p">
                        <b>Importo Totale</b>
                    </td> 
                    <td class="w100p">
                    <center>
                        <b>Dettagli</b>
                    </center>
                    </td> 
                    <td class="w100p">
                    <center>
                        <b>Elimina</b>
                    </center>                        
                    </td>
                    <!--<td class="w100p">
                    <center>
                        <b>Invia Mail</b>
                    </center>
                    </td>-->
                </tr>  

                <%
                for(Fattura f : fatture)
                {%>
                    <tr>
                        <td class="w200p">
                            Fattura del: <%=f.Data%>
                        </td>            
                        <td class="w100p">
                            <%=f.Totale%> &euro;
                        </td>
                        <td class="w100p">
                            <form name="details" method="post" action="Fatture.jsp">
                                <input type="hidden" name="idFattura" value="<%=f.IdFattura%>"/>
                                <input type="hidden" name="status" value="details">
                                <center><input type="image" name="submit" src="images/details.png"></center>
                            </form>
                        </td>
                        <td class="w100p">
                            <form name="delete" method="post" action="Fatture.jsp" onSubmit="return deleteFatturaOnSubmit();">
                                <input type="hidden" name="idFattura" value="<%=f.IdFattura%>"/>
                                <input type="hidden" name="status" value="delete"/>
                                <center><input type="image" name="submit" src="images/delete.jpg"></center>
                            </form>
                        </td>
                        <!--<td class="w100p">
                            <form name="viewMail" method="post" action="Mail.jsp">
                                <input type="hidden" name="idFattura" value="<%--f.IdFattura--%>"/>
                                <input type="hidden" name="status" value="viewFattura"/>
                                <center><input type="image" name="submit" src="images/mail.jpg"></center>
                            </form>
                        </td>-->
                    </tr>
                <%}%>  
            </table>
        <%}else{%>
            Nessuna fattura caricata
        <%}            
            if(consumiManagement.getConsumi() != null && !consumiManagement.getConsumi().isEmpty())
            {%>
            <div class="titolo">
                <b>Fattura del: <%=consumiManagement.getSelectedFattura().Data%></b>
            </div>
            <div class="testo">
                <b>Importi Nominali</b><br/>
                Importo Totale: <%=consumiManagement.getSelectedFattura().Totale%> &euro;<br/>
                <u>Totale Contributi e Abbonamenti: <%=consumiManagement.getSelectedFattura().CA%> &euro;</u><br/>
                <u>Totale Altri Addebiti e Accrediti: <%=consumiManagement.getSelectedFattura().AAA%> &euro;</u><br/>
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
                <u>Totale Contributi e Abbonamenti: <%=df.format(contributi + noleggi)%> &euro;</u><br/>
                <u>Altri Addebiti e Accrediti Calcolati: <%=df.format(altri)%> &euro;</u><br/><br/><br/>
            </div>
            <div class="testo">
                <b>Consumi</b>
                <table class="table-classic" cellspacing="0"> 
                    <tr>
                        <td class="w100p">
                            <b>Telefono</b>
                        </td>
                        <td class="w200p">
                            <b>Descrizione</b>
                        </td>
                        <td class="w100p">
                            <b>Importo</b>
                        </td>
                        <td class="w50p">
                        <center>
                            <b>Modifica</b>
                        </center>
                        </td>
                        <td class="w50p">
                        <center>
                            <b>Utente</b>
                        </center>
                        </td>
                    </tr>   
                    <%
                    for(Consumo c : consumiManagement.getConsumi())
                    {%>
                    <tr>
                        <td class="w100p">
                            <%=c.Telefono%>
                        </td>
                        <td class="w200p">
                          Contributi Ricaricabile Business<br/>
                          Altri addebiti e accrediti<br/>
                          Abbonamenti<br/><br/>
                          Totale<br/>
                        </td>
                        <td class="w100p">
                            <%=c.CRB%><br/>
                            <%=c.AAA%><br/>
                            <%=c.ABB%><br/><br/>
                            <%=c.Totale%><br/>
                        </td>
                        <td class="w50p">
                            <form name="edit" method="post" action="Fatture.jsp">
                                <input type="hidden" name="idFattura" value="<%=consumiManagement.getIdFattura()%>"/>
                                <input type="hidden" name="numero" value="<%=c.Telefono %>">
                                <input type="hidden" name="status" value="editView">
                                <center><input type="image" name="submit" src="images/edit.jpg"></center>
                            </form>
                        </td>
                        <td class="w50p">
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
