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

<jsp:useBean id="b" scope="page" class="bflows.UserBF"/>
<jsp:setProperty name="b" property="*"/>


        
        <% 
            String status=null;
            ArrayList<User> u= new ArrayList<User>();
            ArrayList<Field1> f= new ArrayList<Field1>();
            String nome=null,cognome=null,telefono=null,mail=null,messaggio=null,nome_d=null;
            String tel=null;
            int num;
            double costo_d=0;

            status=request.getParameter("status");
            if(status==null)
            {
               status="view"; 
               System.out.println("sono in view");
            }
            if(status.equals("delete1"))
            {
                b.delete(tel);
                status="view";
            }
            if(status.equals("edit1"))
            {
                telefono=request.getParameter("telefono");
                System.out.println("edit tel"+telefono);
                b.edit();
                status="view";
            }
            if(status.equals("del"))
            {
                tel=request.getParameter("telefono");
                System.out.println("#"+tel);
                b.delete(tel);
                status="view";
            }
            
            if(status.equals("view"))
            {
                b.ViewUser();
            }
            if(status.equals("edit"))
            {
                tel=request.getParameter("tel");
                System.out.println("edit"+tel);
                b.SearchUser(tel);
            }
            if(status.equals("delete"))
            {
                tel=request.getParameter("tel");
                System.out.println("delete"+tel);
                b.SearchUser(tel);
            }
            if(status.equals("mail"))
            {
                mail=request.getParameter("mail");
                telefono=request.getParameter("tel");
                b.SearchMail(telefono);
            }
            if(status.equals("add_d"))
            {
                mail=request.getParameter("mail");
                costo_d=Double.parseDouble(request.getParameter("costo_d"));
                nome_d=request.getParameter("nome_d");
                System.out.println(mail+"#"+nome_d+" "+costo_d);
                b.insert_disp(nome_d,costo_d,mail);
                status="disp";
                
            }
            if(status.equals("del_disp"))
            {
                Long code=Long.parseLong(request.getParameter("code"));
                b.delete_disp(code);
                status="disp";
            }
            if(status.equals("disp"))
            {
                mail=request.getParameter("mail");
                b.SearchDisp(mail);
            }
            if(status.equals("add"))
            {
                mail=request.getParameter("mail");
            }
            
            %>
 <%@include file="Header.jsp" %>

    
<% if(status.equals("view")){%>           
       
            
    <form name="mail" method="post" action="Utenti.jsp"><div id="titolo"><b>Elenco utenti</b></div>
       <div id="testo"> <table cellspacing="0"> 
            <tr class="alternate">
                
                <td width="200">
                    <b>Utente</b>
                </td>
                <td width="200">
                    <b>Telefono</b>
                </td>
                <td width="200">
                    <b>Mail</b>
                </td>
                <td width="80"><b>Modifica</b>
                </td>
                <td width="80"><b>Cancella</b></td>
                <td width="80" align="center"><b>Storico Mail</b></td>
                <td width="100" ><b>Dispositivo</b></td>
                
            </tr>   
            <% if(b.getUser().length!=0)
            {   int i=0;
                while(i<b.getUser().length){%>
                <tr>
                    
                    <td width="200">
                         <%=b.getUser(i).cognome%> <%=b.getUser(i).nome%> 
                    </td>
                    <td width="200">
                        <%=b.getUser(i).telefono%>
                    </td>
                    <td width="200"><%=b.getUser(i).mail%>
                    </td>
                    <td width="80">
                        <form name="edit" method="post" action="Utenti.jsp">
                            <input type="hidden" name="tel" value="<%=b.getUser(i).telefono%>">
                            <input type="hidden" name="status" value="edit">
                            <center><input type="image" name="submit" src="images/edit.jpg"></center>
                        </form>
                    </td>
                            
                    <td width="80">
                        <form name="delete" method="post" action="Utenti.jsp">
                            <input type="hidden" name="tel" value="<%=b.getUser(i).telefono%>">
                            <input type="hidden" name="status" value="delete">
                            <center><input type="image" name="submit" src="images/delete.jpg"></center>
                        </form>
                    </td>
                <td width="80">
                        <form name="mail" method="post" action="Utenti.jsp">
                            <input type="hidden" name="mail" value="<%=b.getUser(i).mail%>">
                            <input type="hidden" name="tel" value="<%=b.getUser(i).telefono%>">
                            <input type="hidden" name="status" value="mail">
                            <center><input type="image" name="submit" src="images/mail.jpg"></center>
                        </form>
                    </td>
                <td width="100">
                        <form name="disp" method="post" action="Utenti.jsp">
                            <input type="hidden" name="mail" value="<%=b.getUser(i).mail%>">
                            <input type="hidden" name="status" value="disp">
                            <center><input type="image" name="submit" src="images/mobile.jpg"></center>
                        </form>
                    </td>
                </tr>  
                
                 <%i++;
                       if(i<b.getUser().length){%>
                   <tr class="alternate"> <td width="200"> 
                      <%=b.getUser(i).cognome%> <%=b.getUser(i).nome%>  
                    </td>
                    <td width="200">
                        <%=b.getUser(i).telefono%>
                    </td>
                    <td width="200"><%=b.getUser(i).mail%>
                    </td>
                    <td width="80">
                        <form name="edit" method="post" action="Utenti.jsp">
                            <input type="hidden" name="tel" value="<%=b.getUser(i).telefono%>">
                            <input type="hidden" name="status" value="edit">
                            <center><input type="image" name="submit" src="images/edit.jpg"></center>
                        </form>
                    </td>
                            
                    <td width="80">
                        <form name="delete" method="post" action="Utenti.jsp">
                            <input type="hidden" name="tel" value="<%=b.getUser(i).telefono%>">
                            <input type="hidden" name="status" value="delete">
                            <center><input type="image" name="submit" src="images/delete.jpg"></center>
                        </form>
                    </td>
                   <td width="80">
                        <form name="mail" method="post" action="Utenti.jsp">
                            <input type="hidden" name="mail" value="<%=b.getUser(i).mail%>">
                            <input type="hidden" name="tel" value="<%=b.getUser(i).telefono%>">
                            <input type="hidden" name="status" value="mail">
                            <center><input type="image" name="submit" src="images/mail.jpg"></center>
                        </form>
                    </td>
                   <td width="100">
                        <form name="disp" method="post" action="Utenti.jsp">
                            <input type="hidden" name="mail" value="<%=b.getUser(i).mail%>">
                            <input type="hidden" name="status" value="disp">
                            <center><input type="image" name="submit" src="images/mobile.jpg"></center>
                        </form>
                    </td></tr>  
           <% }i++;}}%>  
        </table>
        
        
        
       </div></form>
        <%} if(status.equals("edit")){%>
        <form name="edit" method="post" action="Utenti.jsp">
        <div id="titolo"><b>Modifica Utente</b> </div>
        <div id="testo">
        <table cellspacing="0" >
            <tr style="background-color:#F0F8FF">
                <td>
                    <%=b.getUser(0).nome%>
                </td>
                <td>
                <input type="text" name="nome" size="30" maxlength="20" value="<%=b.getUser(0).nome%>">
                </td>
            </tr>
            <tr style="background-color:#F0F8FF">            
                <td>
                    <%=b.getUser(0).cognome%>
                </td>
                <td>
                    <input type="text" name="cognome" size="30" maxlength="20" value="<%=b.getUser(0).cognome%>">
                </td>
            </tr>
            <tr style="background-color:#F0F8FF">
                <td>
                    <%=b.getUser(0).mail%><br/>
                </td>
                <td>
                    <input type="text" name="mail" size="50" maxlength="50" value="<%=b.getUser(0).mail%>">
                </td>
            </tr>
        </table>
                <input type="hidden" name="status" value="edit1"/>
                <input type="hidden" name="telefono" value="<%=b.getUser(0).telefono%>"/>
                <input type="submit" value="Modifica"/>
        </div>
         
        </form>
        <%}if(status.equals("delete")){ %>
        <form name="del" method="post" action="Utenti.jsp">
        <div id="titolo"><b>Cancella Utente</b> </div>
        <div id="testo" align="left">
            <span class="tab2"><%=b.getUser(0).nome%> <%=b.getUser(0).cognome%><br/></span>
            <span class="tab2"><%=b.getUser(0).telefono%><br/></span>
            <span class="tab2"><%=b.getUser(0).mail%></span><br/><br/>
            <input type="hidden" name="status" value="del"/>
            <input type="hidden" name="telefono" value="<%=b.getUser(0).telefono%>">
            <span class="tab2"><input type="submit" value="Cancella"></span>
        </div>
        </form>
        <%}if(status.equals("mail")){%>
        <div id="titolo"><b>Mail inviate</b></div>
            <div id="testo" align="left">
            <% if(b.getText().length!=0){
                for(int i=0;i<b.getText().length;i++){%>
               
            Data:<b> <%=b.getText(i).data%></b><br/><br/>
            <div class="tab2"><%=b.getText(i).testo%></div><br/><br/><br/>
            
         <%}}else{%>   
         <div id="testo" align="left">Nessuna mail inviata</div>
        <%}%>
        </div>
        <%}if(status.equals("disp")){%>
        <div id="titolo"><b>Dispositivi</b></div>
        <div id="testo" align="left">
            <% if(b.getDispositivo().length==0){%>
            <center>L'utente non possiede dispositivi</center>   
        <%}if(b.getDispositivo().length>0){%>
        <table cellspacing="0"> 
            <tr class="alternate">
                <td width="50%"><b>Nome</b></td>
                <td width="25%"><center><b>Costo</b></center></td>
                <td width="25%"><center><b>Elimina</b></center></td>
            </tr>
            <%for(int i=0;i<b.getDispositivo().length;i++)
            {%>   
            <tr>
                <td width="50%"> 
                         <%=b.getDispositivo(i).nome_d%>
                    </td>
                    <td width="25%">
            <center><%=b.getDispositivo(i).costo_d%></center>
                    </td>
                    <td width="25%">
                        <form name="del" method="post" action="Utenti.jsp">
                            <input type="hidden" name="mail" value="<%=b.getDispositivo(i).mail%>">
                            <input type="hidden" name="code" value="<%=b.getDispositivo(i).code%>">
                            <input type="hidden" name="status" value="del_disp">
                            <center><input type="image" name="submit" src="images/delete.jpg"></center>
                        </form>
                    </td>
            </tr>
         <%}%>
        </table>
        <%}%>
         <br> 
         <form name="add" method="post" action="Utenti.jsp">
         <input type="hidden" name="mail" value="<%=mail%>">
         <input type="hidden" name="status" value="add">
         <center>Inserisci un nuovo dispositivo  <input type="image" name="submit" src="images/add.jpg"></center>
         </form>   
          
        </div>
        
        <%}if(status.equals("add")){%>
        <form name="fregister" method="post">
            <div id="titolo"><b> Nuovo dispositivo </b></div>
            <div id="testo" align="left"> 
                <table style="background-color:#F0F8FF; width: auto;padding-bottom:0px;padding-top:0px">
                     <tr style="background-color:#F0F8FF;">
                    <td width="150">Nome Dispositivo</td>
                    <td width="250">
                        <input type="text" name="nome_d" size="50" maxlength="50"/>
                    </td>
                </tr>
                <tr style="background-color:#F0F8FF;">
                    <td width="150">Costo</td>
                    <td width="250">
                        <input type="text" name="costo_d" size="25" maxlength="50"/>
                    </td>
                </tr>
                </table>
                
                <input type="hidden" name="status" value="add_d"/>
                <input type="hidden" name="mail" value="<%=mail%>"/>
                <center><input type="submit" value="Aggiungi"/></center>
                
            </div>
        
        <%}%>