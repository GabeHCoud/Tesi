<%@page import="prevblogics.*"%>
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

<jsp:useBean id="b" scope="page" class="prevbflows.FieldBF"/>
<jsp:setProperty name="b" property="*"/>

<% 
    String status;
    status=request.getParameter("status");
    String telefono=null,tel=null,message=null;
    ArrayList<Integer> ok= new ArrayList<Integer>();
    
            if(status==null)
            {
               status="view"; 
               System.out.println("sono in view");
            }
    if(status.equals("edit1"))
    {
        b.edit();        
        status="view";
    }
    if(status.equals("continua"))
    {
        telefono=request.getParameter("telefono");
        System.out.println("continua");
        b.Register(telefono);
        status="view";
    }
    
    if(status.equals("view"))
    {
        b.View();
        System.out.println(b.getField1().length);
        for(int n=0;n<b.getField1().length;n++)
        {
            b.SearchUser(b.getField1(n).telefono);
            
            
            if(b.getUser().length==0)
            {
                ok.add(0);
                System.out.println(ok.get(n));
            }
            else {
                ok.add(1);
                System.out.println(ok.get(n));
            }
        }
    }
    if(status.equals("edit"))
    {
        tel=request.getParameter("tel");
        b.SearchF1(tel);
    }
    if(status.equals("add"))
    {
        telefono=request.getParameter("telefono");  
    }
        
%>

<%@include file="/preversion/Header.jsp" %>
 
<%if (message!=null) {%>
      <script>alert("<%=message%>");</script>
    <%}%> 
    <% if(status.equals("view")){%>

<div id="titolo">Inserimento effettuato</div>
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
                <td width="100"><b>Modifica</b>
                </td>
                <td width="100"><b>Utente</b>
                </td>
            </tr>   
    <%
       if(b.getField1().length!=0) {
           int j=0;
           while(j<b.getField1().length)
           {
%>


    <tr>
                    
                    <td width="200"> 
                        <%=b.getField1(j).telefono %>
                    </td>
                    
                    <td width="300">
                      Contributi Ricaricabile Business<br/>
                      Altri addebiti e accrediti<br/>
                      Abbonamenti<br/><br/>
                      Totale<br/>
                      
                    </td>
                    
                    
                    <td width="200"><%=b.getField1(j).contributi%><br/>
                        <%=b.getField1(j).aaa%><br/>
                        <%=b.getField1(j).abb%><br/><br/>
                        <%=b.getField1(j).totale%><br/>
                        
                    </td>
                    <td width="100">
                        <form name="edit" method="post" action="Fattura.jsp">
                            <input type="hidden" name="tel" value="<%=b.getField1(j).telefono %>">
                            <input type="hidden" name="status" value="edit">
                            <center><input type="image" name="submit" src="../images/edit.jpg"></center>
                        </form>
                    </td>
                    <td with="100">
                        <form name="name"method="post" action="Fattura.jsp">
                            <input type="hidden" name="telefono" value="<%=b.getField1(j).telefono %>"/>
                            <input type="hidden" name="status" value="add"/>
                            <% if(ok.get(j)==0){%>
                            <center><input type="image" name="submit" src="../images/nouser.jpg">
                             <%}else{%>
                             <center><img src="../images/siuser.jpg">
                            <%}%>
                            
                        </form>
                    </td>
    </tr>
    
                 <%j++;
                       if(j<b.getField1().length){%>
       <tr class="alternate">                
                       <td width="200"> 
                        <%=b.getField1(j).telefono %>
                    </td>
                    
                    <td width="300">
                      Contributi Ricaricabile Business<br/>
                      Altri addebiti e accrediti<br/>
                      Abbonamenti<br/><br/>
                      Totale<br/>
                      
                    </td>
                    
                    
                    <td width="200"><%=b.getField1(j).contributi%><br/>
                        <%=b.getField1(j).aaa%><br/>
                        <%=b.getField1(j).abb%><br/><br/>
                        <%=b.getField1(j).totale%><br/>
                        
                    </td>
                    <td width="100">
                        <form name="edit" method="post" action="Fattura.jsp">
                            <input type="hidden" name="tel" value="<%=b.getField1(j).telefono %>">
                            <input type="hidden" name="status" value="edit">
                            <center><input type="image" name="submit" src="../images/edit.jpg"></center>
                        </form>
                    </td>
                    <td with="100">
                        <form name="name"method="post" action="Fattura.jsp">
                            <input type="hidden" name="telefono" value="<%=b.getField1(j).telefono %>">
                            <input type="hidden" name="status" value="add"/>
                            <% if(ok.get(j)==0){%>
                            <center><input type="image" name="submit" src="../images/nouser.jpg">
                             <%}else{%>
                             <center><img src="../images/siuser.jpg">
                            <%}%>
                        </form>
                    </td>
       </tr> 
                       
                       
                        <% }j++;}}%>  

        </table></div>
<%}if(status.equals("edit")){%>
<form name="edit" method="post" action="Fattura.jsp">
<div id="titolo"><b>Modifica <%=b.getField1(0).telefono%></b></div>
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
                <%=b.getField1(0).contributi%>
            </td>
            <td>
                <input type="text" name="contributi" size="10" maxlength="20" value="<%=b.getField1(0).contributi%>">
        </tr>
        <tr style="background-color:#F0F8FF">
            <td>
                Altri addebiti e accrediti
            </td>
            <td>
                <%=b.getField1(0).aaa%>
            </td>
            <td>
             <input type="text" name="aaa" size="10" maxlength="20" value="<%=b.getField1(0).aaa%>">
            </td>
        </tr>
        <tr style="background-color:#F0F8FF">
            <td>
                Abbonamenti
            </td>
            <td>
                <%=b.getField1(0).abb%>
            </td>
            <td>
                <input type="text" name="abb" size="10" maxlength="20" value="<%=b.getField1(0).abb%>">
            </td>
        </tr>
        <tr style="background-color:#F0F8FF">
        <td>
            Totale
        </td>
        <td>
            <%=b.getField1(0).totale%>
        </td>
        <td>
            <input type="text" name="totale" size="10" maxlength="20" value="<%=b.getField1(0).totale%>">
        </td>
        </td>
        </tr>
    </table>
        <input type="hidden" name="status" value="edit1"/>
        <input type="hidden" name="telefono" value="<%=b.getField1(0).telefono%>"/>
    <input type="submit" value="Modifica"/>
    
    
</div>
</form>

<%}if(status.equals("add")){%>
<form name="fregister" method="post">
            <div id="titolo"><b> Registrazione <%=telefono%></b></div>
            <div id="testo"> 
                <input type="hidden" name="telefono" value="<%=telefono%>">
                
                <table style="background-color:#F0F8FF;width:auto;padding-top:0px;">
                <tr style="background-color:#F0F8FF;">
                    <td width="150">Nome</td>
                    <td width="250">
                        <input type="text" name="nome" size="25" maxlength="50"/>
                    </td>
                </tr>
                <tr style="background-color:#F0F8FF;">
                    <td width="150">Cognome</td>
                    <td width="250">
                        <input type="text" name="cognome" size="25" maxlength="50"/>
                    </td>
                </tr>
                
               
                <tr style="background-color:#F0F8FF;">
                    <td width="150">Mail</td>
                    <td width="250">
                        <input type="text" name="mail" size="25" maxlength="50" />
                    </td>
                </tr>
                
                        </table>
            
            <input type="hidden" name="status" value="continua"/>
         
      
        <span class="tab2"><a href="javascript:controlloReg2()"> Inserisci</a></span>
            </div></form>  
<%}%>
<%@include file="/preversion/Footer.jsp" %>
