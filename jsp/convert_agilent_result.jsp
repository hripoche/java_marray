<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>Conversion du format Agilent vers le format Agilent: r&eacute;sultat</title>
</head>

<body>

<jsp:include page="/jsp/check_connection.jsp" />

<h1>Conversion du format Agilent vers le format Agilent: r&eacute;sultat</h1>

<jsp:useBean id="bean" class="servlet.action.convertAgilent.ConvertAgilentBean" scope="session" />

<%
  String message = bean.getMessage();
  if (message == null) {
%>
  <h2>format incorrect</h2>
<%
  } else {
%>
<pre>
<%= message %>
</pre>
<%
  }
%>
<br>
<br>
<%
  String agilentPath = bean.getAgilentPath();
  if (agilentPath != null) {
%>
<a href="<%= response.encodeURL(agilentPath) %>"> fichier Agilent </a>
<%
  }
%>

<jsp:include page="/jsp/bottom.jsp" />

</body>
</html>
