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
            fattureManagement.processPDF();  
            break;
    }   
    
%>

<%@include file="Header.jsp" %>

<%
    ArrayList<Fattura> fatture = fattureManagement.getFatture();
    if(fatture != null)
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
        for(Fattura f : fatture)
        {
            if((fatture.indexOf(f) % 2) == 1)//dispari
            {
        %>
        <tr>                    
            <td width="200"> 
                <%=f.Telefono%>
            </td>

            <td width="300">
              Contributi Ricaricabile Business<br/>
              Altri addebiti e accrediti<br/>
              Abbonamenti<br/><br/>
              Totale<br/>

            </td>


            <td width="200"><%=f.CRB%><br/>
                <%=f.AAA%><br/>
                <%=f.ABB%><br/><br/>
                <%=f.Totale%><br/>

            </td>
            <td width="100">
                <form name="edit" method="post" action="Fattura.jsp">
                    <input type="hidden" name="tel" value="<%=f.Telefono %>">
                    <input type="hidden" name="status" value="edit">
                    <center><input type="image" name="submit" src="images/edit.jpg"></center>
                </form>
            </td>
            <td with="100">
                <form name="name" method="post" action="Fattura.jsp">
                    <input type="hidden" name="telefono" value="<%=f.Telefono%>"/>
                    <input type="hidden" name="status" value="add"/>
                    <% //if(ok.get(j)==0){%>
                    <center><input type="image" name="submit" src="images/nouser.jpg"></center>
                    <%//}else{%>
                    <!--<center><img src="images/siuser.jpg"></center>-->
                    <%//}%>
                </form>
            </td>
        </tr>
    
        <%
            }else if((fatture.indexOf(f) % 2) == 0)//pari
            {                
        %>
        <tr class="alternate">                
            <td width="200"> 
                <%=f.Telefono%>
            </td>

            <td width="300">
              Contributi Ricaricabile Business<br/>
              Altri addebiti e accrediti<br/>
              Abbonamenti<br/><br/>
              Totale<br/>

            </td>


            <td width="200"><%=f.CRB%><br/>
                <%=f.AAA%><br/>
                <%=f.ABB%><br/><br/>
                <%=f.Totale%><br/>

            </td>
            <td width="100">
                <form name="edit" method="post" action="Fattura.jsp">
                    <input type="hidden" name="tel" value="<%=f.Telefono %>">
                    <input type="hidden" name="status" value="edit">
                    <center><input type="image" name="submit" src="images/edit.jpg"></center>
                </form>
            </td>
            <td with="100">
                <form name="name" method="post" action="Fattura.jsp">
                    <input type="hidden" name="telefono" value="<%=f.Telefono%>"/>
                    <input type="hidden" name="status" value="add"/>
                    <% //if(ok.get(j)==0){%>
                    <center><input type="image" name="submit" src="images/nouser.jpg"></center>
                    <%//}else{%>
                    <!--<center><img src="images/siuser.jpg"></center>-->
                    <%//}%>
                </form>
            </td>
       </tr>           
        <% }
        }%>  
    </table>
<%}%>
        
<%@include file="Footer.jsp" %>