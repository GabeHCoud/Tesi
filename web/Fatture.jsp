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
    
    switch(status)
    {
        case "view":
            consumiManagement.viewFatture();
            break;
        case "details":
            if(id != null)
                consumiManagement.setIdFattura(Integer.parseInt(id));     
            
            consumiManagement.viewFatture();
            consumiManagement.viewConsumi();
            break;
        case "editView":
            consumiManagement.viewEdit();
            break;                
        case "edit":
            consumiManagement.edit();            
            break;
        case "delete":
            consumiManagement.delete();
            break;
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
    <form name="edit" method="post" action="Fatture.jsp">
        <div id="titolo">
            <b>Modifica consumi di <%=consumiManagement.getSelectedConsumo().Telefono%> in fattura del <%=consumiManagement.getSelectedFattura().Data%></b></div>
        <div id="testo">
            <table cellspacing="0" >
                <tr style="background-color:#F0F8FF">
                    <td>
                        <b>Descrizione</b>
                    </td>
                    <td>
                        <b>Importo €</b>
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
            <input type="hidden" name="telefono" value="<%=consumiManagement.getSelectedConsumo().Telefono%>"/>
            <input type="submit" value="Modifica"/>    
        </div>
    </form>  
    <%}else{%>
    <div id="titolo">
        Fatture
    </div>
    <div id="testo">
        <%  ArrayList<Fattura> fatture = consumiManagement.getFatture();
            if(fatture != null)
            {%>
            <table cellspacing="0"> 
                <tr class="alternate">
                    <td width="200">
                        <b>Data</b>
                    </td>
                    <td width="100">
                        <b>Dettagli</b>
                    </td>               
                    <td width="100">
                        <b>Elimina</b>
                    </td>
                    <td width="100">
                        <b>Invia Mail</b>
                    </td>
                </tr>  

                <%
                for(Fattura f : fatture)
                {
                    if((fatture.indexOf(f) % 2) == 1)//dispari
                    {
                %>
                <tr>                    
                    <td width="200"> 
                        Fattura del: <%=f.Data%>
                    </td>                
                    <td width="100">
                        <form name="details" method="post" action="Fatture.jsp">
                            <input type="hidden" name="idFattura" value="<%=f.IdFattura%>"/>
                            <input type="hidden" name="status" value="details">
                            <center><input type="image" name="submit" src="images/details.png"></center>
                        </form>
                    </td>
                    <td width="100">
                        <form name="delete" method="post" action="Fatture.jsp">
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
                 <%
                    }else if((fatture.indexOf(f) % 2) == 0)//pari
                    {                
                %>
                <tr class="alternate">
                    <td width="200"> 
                        Fattura del: <%=f.Data%>
                    </td>                
                    <td width="100">
                        <form name="details" method="post" action="Fatture.jsp">
                            <input type="hidden" name="idFattura" value="<%=f.IdFattura%>"/>
                            <input type="hidden" name="status" value="details">
                            <center><input type="image" name="submit" src="images/details.png"></center>
                        </form>
                    </td>
                    <td width="100">
                        <form name="delete" method="post" action="Fatture.jsp">
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
                <%  }
                }%>  
            </table>
        <%  }

            ArrayList<Consumo> consumi = consumiManagement.getConsumi();
            if(consumi != null)
            {%>
            <div id="titolo">
                Fattura del: <%=consumiManagement.getSelectedFattura().Data%>
            </div>
            <div id="testo">
                <table cellspacing="0"> 
                    <tr class="alternate">
                        <td width="200">
                            <b>Telefono</b>
                        </td>
                        <td width="300">
                            <b>Descrizione</b>
                        </td>
                        <td width="200">
                            <b>Importo (€)</b>
                        </td>
                        <td width="100">
                            <b>Modifica</b>
                        </td>
                        <td width="100">
                            <b>Utente</b>
                        </td>
                    </tr>   
                    <%
                    for(Consumo c : consumi)
                    {
                        if((consumi.indexOf(c) % 2) == 1)//dispari
                        {
                    %>
                    <tr>                    
                        <td width="200"> 
                            <%=c.Telefono%>
                        </td>

                        <td width="300">
                          Contributi Ricaricabile Business<br/>
                          Altri addebiti e accrediti<br/>
                          Abbonamenti<br/><br/>
                          Totale<br/>

                        </td>


                        <td width="200"><%=c.CRB%><br/>
                            <%=c.AAA%><br/>
                            <%=c.ABB%><br/><br/>
                            <%=c.Totale%><br/>

                        </td>
                        <td width="100">
                            <form name="edit" method="post" action="Fatture.jsp">
                                <input type="hidden" name="idFattura" value="<%=consumiManagement.getIdFattura()%>"/>
                                <input type="hidden" name="telefono" value="<%=c.Telefono %>">
                                <input type="hidden" name="status" value="editView">
                                <center><input type="image" name="submit" src="images/edit.jpg"></center>
                            </form>
                        </td>
                        <td with="100">
                            <form name="name" method="post" action="AssociaUtente.jsp">                                
                                <input type="hidden" name="idFattura" value="<%=consumiManagement.getIdFattura()%>"/>
                                <input type="hidden" name="numero" value="<%=c.Telefono%>"/>
                                <input type="hidden" name="status" value="view"/>
                                <% if(c.Email == null){%>
                                <center><input type="image" name="submit" src="images/nouser.jpg"></center>
                                <%}else{%>
                                <center><img src="images/siuser.jpg"></center>
                                <%}%>
                            </form>
                        </td>
                    </tr>

                    <%
                        }else if((consumi.indexOf(c) % 2) == 0)//pari
                        {                
                    %>
                    <tr class="alternate">                
                        <td width="200"> 
                            <%=c.Telefono%>
                        </td>

                        <td width="300">
                          Contributi Ricaricabile Business<br/>
                          Altri addebiti e accrediti<br/>
                          Abbonamenti<br/><br/>
                          Totale<br/>

                        </td>


                        <td width="200"><%=c.CRB%><br/>
                            <%=c.AAA%><br/>
                            <%=c.ABB%><br/><br/>
                            <%=c.Totale%><br/>

                        </td>
                        <td width="100">
                            <form name="edit" method="post" action="Fatture.jsp">
                                <input type="hidden" name="idFattura" value="<%=consumiManagement.getIdFattura()%>"/>
                                <input type="hidden" name="telefono" value="<%=c.Telefono %>">
                                <input type="hidden" name="status" value="editView">
                                <center><input type="image" name="submit" src="images/edit.jpg"></center>
                            </form>
                        </td>
                        <td with="100">
                            <form name="name" method="post" action="AssociaUtente.jsp">                               
                                <input type="hidden" name="idFattura" value="<%=consumiManagement.getIdFattura()%>"/>
                                <input type="hidden" name="numero" value="<%=c.Telefono%>"/>
                                <input type="hidden" name="status" value="view"/>
                                <% if(c.Email == null){%>
                                <center><input type="image" name="submit" src="images/nouser.jpg"></center>
                                <%}else{%>
                                <center><img src="images/siuser.jpg"></center>
                                <%}%>
                            </form>
                        </td>
                   </tr>           
                    <% }
                    }%>  
                </table>
            </div>
        <%}%>
    </div>
    <%}%>
<%}%>
<%@include file="Footer.jsp" %>
