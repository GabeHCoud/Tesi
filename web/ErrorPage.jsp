<%@ page info="Error Page" %>
<%@ page contentType="text/html" %>
<%@ page session="false" %>
<%@ page buffer="10kb" %>
<%@ page isErrorPage="true" %>
<%//@ page import="services.errorservice.*"%>

<%int i;%>
<%@include file="Header.jsp" %>
<div class="titolo">

    Si � verificato un Errore!
</div>
<div class="testo">
Ci scusiamo per l'errore ma si � cercato di accedere ad una pagina non pi� raggiungibile o vi � stato qualche errore interno al sito di carattere sconosciuto. 
<br/><br/><br/><a href="Index.jsp"><div class="button back-button">Torna all'Home Page</div></a>
<br/>
</div>
 