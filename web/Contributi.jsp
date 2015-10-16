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

<jsp:useBean id="contributiManagement" scope="page" class="bflows.ContributiManagement"/>
<jsp:setProperty name="contributiManagement" property="*"/>

<% 
    String status;    
    status = request.getParameter("status");
    
    if(status == null)
        status = "view";
    
    if(status.equals("view")){
        contributiManagement.view();
    } else if(status.equals("addSubscription")){
        contributiManagement.addSubscription();
    }else if (status.equals("viewEditSubscription")){
        contributiManagement.viewEditSubscription();
    }else if (status.equals("editSubscription")){
        contributiManagement.editSubscription();
    }else if (status.equals("deleteSubscription")){
        contributiManagement.deleteSubscription();
    }
    %>
 
<%@include file="Header.jsp" %>
<%  if(contributiManagement.getErrorMessage() != null)
    {%>     
    <div class="titolo">
        Si è verificato un Errore!
    </div>
    <div class="testo">
    <%=contributiManagement.getErrorMessage()%>
        <br/><br/><br/>
        <a href="Index.jsp">
            <div class="button goHome-button" >Torna all'Home Page</div>
        </a>
        <br/>
    </div>
<%  }else if(status.equals("view") || status.equals("addSubscription") || status.equals("deleteSubscription") || status.equals("editSubscription"))
    {%>     
    <div class="titolo"><b>Contributi ed Abbonamenti</b></div>
    <div class="testo" >        
        <table class="table-classic" cellspacing="0"> 
            <tr>
                <td class="w50"><b>Nome</b></td>
                <td class="w17"><center><b>Costo</b></center></td>
                <td class="w17"><center><b>Modifica</b></center></td>
                <td class="w17"><center><b>Elimina</b></center></td>
            </tr>
            <%for(Contributo c : contributiManagement.getContributi())
            {%>
                <tr>
                    <td class="w50"> 
                        <%=c.Nome%>
                    </td>
                    <td class="w17">
                    <center><%=c.Costo%></center>
                    </td>
                    <td class="w17">
                        <form name="edit" method="post" action="Contributi.jsp">
                            <input type="hidden" name="idContributo" value="<%=c.IdContributo%>">
                            <input type="hidden" name="status" value="viewEditSubscription">
                            <center><input type="image" name="submit" src="images/edit.jpg"></center>
                        </form>
                    </td>
                    <td class="w17">
                        <form name="del" method="post" action="Contributi.jsp" onsubmit="return deleteContributoOnSubmit()">
                            <input type="hidden" name="idContributo" value="<%=c.IdContributo%>">
                            <input type="hidden" name="status" value="deleteSubscription">
                            <center><input type="image" name="submit" src="images/delete.jpg"></center>
                        </form>
                    </td>
                </tr>
            <%}%>
        </table>        
    </div>
    <div class="titolo"><b>Aggiungi un contributo o abbonamento </b></div>
    <div class="testo">
        <form name="fregister" method="post" action="Contributi.jsp" onsubmit="return addContributoOnSubmit(this)">            
                <table class="invisible-table">
                    <tr>
                        <td class="w150p">Nome</td>
                        <td class="w250p">
                            <input type="text" name="nome" size="50" maxlength="50"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="w150p">Costo</td>
                        <td class="w250p">
                            <input type="text" name="costo" placeholder="esempio: 12.34" size="25" maxlength="50"/>
                        </td>
                    </tr>
                </table>
                
                <input type="hidden" name="status" value="addSubscription"/>
                <center><input type="submit" value="Aggiungi"/></center>
        </form>
    </div>  
<%}else if(status.equals("viewEditSubscription")){%>

    <form class="back-button" name="back" method="post" action="Contributi.jsp">
        <input type="hidden" name="status" value="view">
        <input type="image" alt="indietro" name="submit" src="images/back.png">        
        <br/>
        <span class="f10">indietro</span>
    </form>
    <div class="titolo"><b>Modifica contributo o abbonamento</b></div>
    <div class="testo" >
        <form name="fregister" method="post" action="Contributi.jsp" onsubmit="return editContributoOnSubmit(this)">            
            <table class="invisible-table">
                <tr>
                    <td width="150">Nome Contributo o Abbonamento</td>
                    <td width="250">
                        <input type="text" name="nome" size="50" maxlength="50" value="<%=contributiManagement.getSelectedContributo().Nome%>"/>
                    </td>
                </tr>
                <tr>
                    <td width="150">Costo</td>
                    <td width="250">
                        <input type="text" name="costo" size="25" maxlength="50"  value="<%=contributiManagement.getSelectedContributo().Costo%>"/>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="idContributo" value="<%=contributiManagement.getSelectedContributo().IdContributo%>">                                        
            <input type="hidden" name="status" value="editSubscription"/>
            <center><input type="submit" value="Modifica"/></center>
        </form>
    </div>
<%}%>
 <%@include file="Footer.jsp" %>