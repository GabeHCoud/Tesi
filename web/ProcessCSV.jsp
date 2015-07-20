<%@page import="blogics.Consumo"%>
<%@page import="blogics.Fattura"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
<%@page import="util.*"%>

<%@page buffer="40kb" %>
<%@page errorPage="/ErrorPage.jsp" %>

<jsp:useBean id="fattureManagement" scope="page" class="bflows.FattureManagement"/>
<jsp:setProperty name="fattureManagement" property="*"/>

<% 
    String status=request.getParameter("status");
    if(status==null)
    {
       status="process"; 
    }  

    switch(status)
    {
        case "view":
            break;
        case "process":
            InputStream is = request.getInputStream();            
            fattureManagement.setInputStream(is);            
            fattureManagement.processCSV();  
            fattureManagement.insert();
            break;
        case "setDate":
            fattureManagement.setDate();
            break;
    }   
%>

<%@include file="Header.jsp" %>
<%if(fattureManagement.getErrorMessage() != null)
{%>     
    <div id="titolo">
        Si è verificato un Errore!
    </div>
    <div id="testo">
    <%=fattureManagement.getErrorMessage()%>
        <br/><br/><br/>
        <a href="Index.jsp">
            <div class="button" style="width: 150px; margin: 0 auto;">Torna all'Home Page</div>
        </a>
        <br/>
    </div>
<%}else{%>
    <%if(fattureManagement.getData() == null)
    {%>
    <div>
        <br/>
        <span>
            <b>Data Fattura:</b>   
        </span>       
        <form name="dateForm" action="ProcessCSV.jsp" method="post" >
            <input type="hidden" name="status" value="setDate"/>
            <input type="date" name="data"/>
            <input type="submit" />
        </form>        
    </div>
    <%}else{%>
    <div>
        <span>
        <b>Fattura del:</b> <%=fattureManagement.getData()%>
        </span>
    </div>
    <%}
    ArrayList<Consumo> consumi = fattureManagement.getConsumi();
    if(consumi != null)
    {%>
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
                <form name="edit" method="post" action="Fattura.jsp">
                    <input type="hidden" name="idFattura" value="<%=fattureManagement.getFattura().IdFattura%>"/>
                    <input type="hidden" name="telefono" value="<%=c.Telefono %>">
                    <input type="hidden" name="status" value="edit">
                    <center><input type="image" name="submit" src="images/edit.jpg"></center>
                </form>
            </td>
            <td with="100">
                <form name="name" method="post" action="Fattura.jsp">
                    <input type="hidden" name="telefono" value="<%=c.Telefono%>"/>
                    <input type="hidden" name="status" value="add"/>
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
                <form name="edit" method="post" action="Fattura.jsp">
                    <input type="hidden" name="idFattura" value="<%=fattureManagement.getFattura().IdFattura%>"/>
                    <input type="hidden" name="telefono" value="<%=c.Telefono %>">
                    <input type="hidden" name="status" value="edit">
                    <center><input type="image" name="submit" src="images/edit.jpg"></center>
                </form>
            </td>
            <td with="100">
                <form name="name" method="post" action="Fattura.jsp">
                    <input type="hidden" name="telefono" value="<%=c.Telefono%>"/>
                    <input type="hidden" name="status" value="add"/>
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
    <%}
}%>

<%@include file="Footer.jsp" %>