<%@page import="blogics.Consumo"%>
<%@page import="blogics.Fattura"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="ISO-8859-1" session="false"%>
<%@page import="util.*"%>

<%@page buffer="40kb" %>
<%@page errorPage="/ErrorPage.jsp" %>

<jsp:useBean id="consumiManagement" scope="page" class="bflows.ConsumiManagement"/>
<jsp:setProperty name="consumiManagement" property="*"/>

<% 
    String status=request.getParameter("status");
    String r=request.getHeader("referer");
    String previous = "";
    if(r.contains("ProcessPDF"))
        previous="ProcessPDF.jsp";
    else if(r.contains("ProcessCSV"))
        previous="ProcessCSV.jsp";
    else if(r.contains("Fatture"))
        previous="Fatture.jsp";
    
    if(status==null)
    {
       status="view"; 
    }  

    switch(status)
    {
        case "view":
            consumiManagement.viewEdit();
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
    <form name="edit" method="post" action="<%=previous%>">
        <div id="titolo">
            <b>Modifica <%=consumiManagement.getSelectedConsumo().Telefono%></b></div>
        <div id="testo">
            <table cellspacing="0" >
                <tr style="background-color:#F0F8FF">
                    <td>
                        <b>Descrizione</b>
                    </td>
                    <td>
                        <b>Importo ?</b>
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
                        <input type="text" name="crb" size="10" maxlength="20" value="<%=consumiManagement.getSelectedConsumo().CRB%>">
                </tr>
                <tr style="background-color:#F0F8FF">
                    <td>
                        Altri addebiti e accrediti
                    </td>
                    <td>
                        <%=consumiManagement.getSelectedConsumo().AAA%>
                    </td>
                    <td>
                     <input type="text" name="aaa" size="10" maxlength="20" value="<%=consumiManagement.getSelectedConsumo().AAA%>">
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
                        <input type="text" name="abb" size="10" maxlength="20" value="<%=consumiManagement.getSelectedConsumo().ABB%>">
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
            <input type="hidden" name="telefono" value="<%=consumiManagement.getSelectedConsumo().Telefono%>"/>
            <input type="submit" value="Modifica"/>    
        </div>
    </form>
<%}%>
        
<%@include file="Footer.jsp" %>