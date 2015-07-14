<%@ page info="Error Page" %>
<%@ page contentType="text/html" %>
<%@ page session="false" %>
<%@ page buffer="10kb" %>
<%@ page isErrorPage="true" %>
<%//@ page import="services.errorservice.*"%>

<%int i;%>
<%@include file="Header.jsp" %>
<div id="titolo">

    Si è verificato un Errore!
</div>
<div id="testo">
Ci scusiamo per l'errore ma si è cercato di accedere ad una pagina non più raggiungibile o vi è stato qualche errore interno al sito di carattere sconosciuto. 
<br/><br/><br/><a href="Index.jsp"><div class="button" style="width: 150px; margin: 0 auto;">Torna all'Home Page</div></a>
<br/>
</div>
 