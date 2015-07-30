
<%@page import="java.util.Calendar"%>
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
            String status=null,message=null,telefono=null;
            ArrayList<String> elementi= new ArrayList<String>();
            ArrayList<Integer> ok=new ArrayList<Integer>();
            ArrayList<Field1> field= new ArrayList<Field1>();
            String s=null,file=null,data=null,tel=null;
            
            
            status=request.getParameter("status");
            if(status==null)
            {
               status="view"; 
               System.out.println("sono in view");
            }
            
            if(status.equals("view"))
            {
                b.delete(); 
                DataInputStream in = new DataInputStream(request.getInputStream());
                while(true)
                {
                    s=in.readLine();
                    if(s==null)
                        break;
                    file=file+"\n"+s;
                }
              System.out.println("mando a capo il file");
                
                    String []d= file.split("\n"); //separo le righe
                    String [] d1;
                    int n=d.length,m=0,el=0;
                    System.out.println("split a capo"+n);
                    
                    for(int i=0;i<n;i++)
                    {
                       d1=d[i].split(";"); //separo i campi
                       m=d1.length;
                       for(int j=0;j<m;j++)
                       {
                            elementi.add(d1[j]);       //salvo nell'arraylist                     
                       }
                    }
                   
                    for(int i=0;i<elementi.size();i++)//scorro l'array stringhe
                    {
                        //System.out.println("i++"+elementi.get(i)+"\n ");
                        String a1="300-0000000";
                        String a2="400-0000000";
                        
            
                        int f=0;
                        if((elementi.get(i).compareTo(a1)>0) && (elementi.get(i).compareTo(a2)<0) && (elementi.get(i).contains("-"))) //seleziono i numeri di telefono
                        {
                            
                            double aaa=0,totale=0,contributi=0,abb=0;
                            String conv;
                            
                            int j=i+1;
                            while((elementi.get(j).contains("-"))==false)
                            {
                               
                                
                                if(elementi.get(j).contains("Contributi"))
                                {
                                    conv=elementi.get(j+1).replaceAll(",",".");
                                    contributi=Double.parseDouble(conv);
                                }
                                if(elementi.get(j).contains("Altri"))
                                {
                                    conv=elementi.get(j+1).replaceAll(",",".");
                                    aaa=Double.parseDouble(conv);
                                }
                                if(elementi.get(j).contains("Totale"))
                                {
                                    conv=elementi.get(j+1).replaceAll(",",".");
                                    totale=Double.parseDouble(conv);                                    
                                }
                                if(elementi.get(j).contains("Abbonamenti"))
                                {
                                    conv=elementi.get(j+1).replaceAll(",",".");
                                    abb=Double.parseDouble(conv);
                                }
                                j++;
                            }
                            b.insert(elementi.get(i),contributi,aaa,abb,totale,data);
                            j++;
                            f++;
                            System.out.println(f+" #"+elementi.get(i)+" contributi:"+contributi+" aaa:"+aaa+" totale:"+totale);
                        }
                    }
            } 
    if(status.equals("ins"))
    {
        int giorno=Integer.parseInt(request.getParameter("giorno"));
        int mese=Integer.parseInt(request.getParameter("mese"));
        int anno=Integer.parseInt(request.getParameter("anno"));
        data=giorno+"/"+mese+"/"+anno;
                
        b.DataFattura(data);
        response.sendRedirect("Fattura.jsp");        
    } 
%>

<%@include file="/preversion/Header.jsp" %>
 
<%if (message!=null) {%>
      <script>alert("<%=message%>");</script>
    <%}%> 
    
<%  if(status.equals("view")){%>
<form name="ins" method="post">
<div id="titolo" > <br> Inserire data emissione fattura</div>
    
        <div id="testo">
             <table>
               <tr style="background-color:#F0F8FF;">
                    <td>
                    <select name="giorno" style="width: 50px;">
                        <option value="01" selected="selected">1</option>
                        <option value="02">2</option>
                        <option value="03">3</option>
                        <option value="04">4</option>
                        <option value="05">5</option>
                        <option value="06">6</option>
                        <option value="07">7</option>
                        <option value="08">8</option>
                        <option value="09">9</option>
                        <% for(int i=10;i<=31;i++){%>
                        <option value="<%=i%>"><%=i%></option>
                        <%}%>
                        
                    </select>
                    <select name="mese" style="width: 100px;">
                        <option value="01" selected="selected">Gennaio</option>
                        <option value="02">Febbraio</option>
                        <option value="03">Marzo</option>
                        <option value="04">Aprile</option>
                        <option value="05">Maggio</option>
                        <option value="06">Giugno</option>
                        <option value="07">Luglio</option>
                        <option value="08">Agosto</option>
                        <option value="09">Settembre</option>
                        <option value="10">Ottobre</option>
                        <option value="11">Novembre</option>
                        <option value="12">Dicembre</option>
                    </select>
            
                    
                    <select name="anno" style="width:100px;">
                        <% for (int i=b.getYear(); i<=b.getYear()+1; i++) {%>
                            <option value="<%=i%>"><%=i%></option><%}%>
                    </select>
                    
                    </td><td>
     
    <a  href="javascript:insert()">Procedi</a></td></tr></table>
    </div><input type="hidden" name="status" value="ins"/>  </form>
<%}%>

<%@include file="/preversion/Footer.jsp" %>
