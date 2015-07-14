<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
<%@page import="util.*"%>

<%@page buffer="40kb" %>
<%@page errorPage="/ErrorPage.jsp" %>

<jsp:useBean id="testBean" scope="page" class="bflows.TestBean"/>
<jsp:setProperty name="testBean" property="*"/>

<% 
    String status=request.getParameter("status");
    if(status==null)
    {
       status="process"; 
    }

    if(status.equals("process"))
    {
        try{
            InputStream is = request.getInputStream();            
            testBean.setInputStream(is);            
            testBean.process();  
            testBean.readFile();
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
%>

<%@include file="Header.jsp" %>

<h1>RIEPILOGO PER UTENZA</h1>
<%if(testBean.getTextualPdf() != null)
{%>
<table>
    <tr>
        <td style="width:300px">Numero di Telefono</td>
        <td style="width:300px">Descrizione</td>
        <td style="width:300px">Importo</td>                                
    </tr>
    <%
    boolean startPointFound = false;
    for(int i=0;i<testBean.getTextualPdf().size();i++)
    {                               
        for(String line : testBean.getTextualPdf().get(i).getLines())
        {
            //RIEPILOGO UTENZA marks the start point of data
            if(line.contains("RIEPILOGO PER UTENZA"))
                startPointFound = !startPointFound;

            if(!startPointFound) continue;                    

            //SERVIZI OPZIONALI marks the end point of data
            if(line.matches("SERVIZI OPZIONALI")) return;

            if(line.matches("(\\d{3}-\\d{7})((?:\\s+)(?:\\w+\\s+)+)(\\d+,\\d+)")) //(3 digits)-(7 digits)(any number of whitespaces)(any number of word followed by whitespace)(1+ digits),(1+digits)
            //if(line.matches("((\\d{3})-(\\d{7}))(\\s+)(\\w+\\s+)+(\\d+),(\\d+)")) 
            {                        
                ArrayList<String> splitted = testBean.splitLine1(line);        
            %>                        
            <tr>
                <%for(int j=0;j<splitted.size();j++)
                {%>
                    <td><%=splitted.get(j)%></td>
                <%}%>
            </tr>
            <%}   

            if(line.matches("((?:\\w+\\s+)+)(\\d+,\\d+)"))//(any number of words followed by whitespaces)(1+ digits),(1+ digits)
            //if(line.matches("(\\w+\\s+)+(\\d+),(\\d+)")) 
            {                        
                ArrayList<String> splitted = testBean.splitLine2(line);        
            %>                    
            <tr>
                <td></td>
                <%for(int j=0;j<splitted.size();j++)
                {%>
                    <td><%=splitted.get(j)%></td>
                <%}%>
            </tr>
            <%}
        }                
    }                 
    %>
</table>
<%}%>
        
<%@include file="Footer.jsp" %>