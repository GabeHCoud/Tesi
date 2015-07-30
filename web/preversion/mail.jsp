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

<jsp:useBean id="b" scope="page" class="prevbflows.MailBF"/>
<jsp:setProperty name="b" property="*"/>


        
        <% 
            String status=null,message=null;
            ArrayList<User> u= new ArrayList<User>();
            ArrayList<Field1> f= new ArrayList<Field1>();
            String nome=null,cognome=null,telefono=null,mail=null,messaggio=null,temp=null,contributi=null,aaa=null,abb=null,totale=null,data=null;
            String[] tel=null;
            int num;
            ArrayList<Integer> ok= new ArrayList<Integer>();

            status=request.getParameter("status");
            if(status==null)
            {
               status="view"; 
               System.out.println("sono in view");
            }
            if(status.equals("view"))
            {
                int ut=0;                
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
                        u.add(b.getUser(0));
                        ut++;
                        System.out.println(ok.get(n));
                    }
                }
                messaggio="Gentile <nome> <cognome> \n\nTelefono: <telefono>\nContributi Ricaricabile Business: <contributi> \nAltri addebiti e accrediti: <aaa> \nAbbonamenti: <abb>\n\nTotale: <totale>";
                     
            }
            if(status.equals("inviomail"))
            {
                System.out.println("sono in inviomail");
                messaggio=request.getParameter("messaggio");
                messaggio=messaggio.replaceAll("\n","<br/>");
                String msg =messaggio;
                num=Integer.parseInt(request.getParameter("num"));
                tel=request.getParameterValues("tel");
                data=request.getParameter("data");
                for(int i=0;i<num;i++)
                {
                    System.out.println(tel[i]);
                    b.SearchUser(tel[i]);
                    System.out.println(b.getUser(0).nome+"#"+b.getUser(0).cognome);
                    b.SearchF1(tel[i]);
                    messaggio=messaggio.replaceAll("<nome>",b.getUser(0).nome);
                    messaggio=messaggio.replaceAll("<cognome>",b.getUser(0).cognome);
                    messaggio=messaggio.replaceAll("<telefono>",tel[i]);
                    contributi=""+b.getField1(0).contributi;
                    aaa=""+b.getField1(0).aaa;
                    abb=""+b.getField1(0).abb;
                    totale=""+b.getField1(0).totale;
                    messaggio=messaggio.replaceAll("<contributi>",contributi);
                    messaggio=messaggio.replaceAll("<aaa>",aaa);
                    messaggio=messaggio.replaceAll("<abb>",abb);
                    messaggio=messaggio.replaceAll("<totale>",totale);
                    
                    System.out.println(messaggio+"\n");
                    b.confirm(b.getUser(0).mail, messaggio,data);
                    messaggio=msg;
                }
                
            }
            if (b.getResult()==-1) 
        {
            throw new Exception("Errore nell'applicazion consultare i log!");
        } 
   else if (b.getResult()==-2)
   {
       message = b.getErrorMessage();
       status = "view";
   }

%>

<%@include file="/preversion/Header.jsp" %>
<%if (message!=null) {%>
      <script>alert("<%=message%>");</script>
    <%}%> 
<% if(status.equals("view")){%>

<form name="mail" method="post" action="mail.jsp">
    <div id="titolo"><b>Data Fattura <%=b.getField1(0).data%></b></div>
    <div id="testo"> 
        <table cellspacing="0"> 
            <tr >
                <td></td>
                <td>
                    <b>Utente</b>
                </td>
                <td width="150">
                    <b>Telefono</b>
                </td>
               
                <td><b>Descrizione</b></td>
                <td><b>Totale</b></td>
            </tr>   
            <% if(b.getField1().length!=0)
            {
                int i=0;
                int ut=0;
                for(i=0;i<b.getField1().length;i++)
                    { 
                        if(ok.get(i)!=0)
                        {%>
                <tr class="alternate">
                    <td> 
                        <input type="checkbox" name="numero" value="<%=b.getField1(i).telefono%>" onclick="Quanti(this)">
                    </td>
                    <td> 
                        <%=u.get(ut).nome%> <%=u.get(ut).cognome%>
                    </td>
                    <td>
                        <%=u.get(ut).telefono%>
                    </td>
                    
                    <td>
                        Contributi Ricaricabile Business: <%=b.getField1(i).contributi%><br/>
                        Altri addebiti e accrediti: <%=b.getField1(i).aaa%><br/>
                        Abbonamenti: <%=b.getField1(i).abb%><br/>
                    </td>
                    <td>         
                        <b><%=b.getField1(i).totale%></b>
                    </td>
                </tr>        
           <%  ut++;}}}%>
        </table>
 <input type="hidden" name="status" value="inviomail"/>
<input type="button" value="Seleziona tutto" onclick="SelezTT()">
<input type="hidden" id="num" name="num" value=""/>
<input type="button" value="Conta" onclick="alert('Selezionati: ' + quanti)"> <br/><br/>      
<script type="text/javascript">
var quanti = 0;
function SelezTT()
{
    var i = 0;
    var modulo = document.mail.elements;
    quanti=0;
    for (i=0; i<modulo.length; i++)
    {
        if(modulo[i].type == "checkbox")
        {
            modulo[i].checked = true;
        }
        if(modulo[i].checked)
            quanti++;
    }
    
    document.quanti=quanti;
    this.document.getElementById("num").value=quanti;
}


function Quanti(elemento)
{
  if (elemento.checked)
  {
      quanti += 1;
  }
  else
  {
      quanti -= 1;
  }
  document.quanti=quanti;
  this.document.getElementById("num").value=quanti;
}
</script>
<br/> <b>Messaggio<b>
 <br/>       
<textarea id="messaggio" name="messaggio" rows="20" cols="100"  ><%=messaggio%></textarea>
<br/> 
<input type="hidden" name="data" value="<%=b.getField1(0).data%>"/>
<input type="submit" name="invio" value="Invia"/>
       </div>
</form> 
           
           
           
           
<%} if(status.equals("inviomail")){%>
<div id="titolo"><b>Mail inviata con successo</b></div>
<%}%>
<%@include file="/preversion/Footer.jsp" %>


