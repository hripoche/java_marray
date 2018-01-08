<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>Conversion vers le format Stanford: r&eacute;sultat</title>
</head>

<body>

<jsp:include page="/jsp/check_connection.jsp" />

<h1>Conversion vers le format Stanford: r&eacute;sultat</h1>

<h2>Etape 3: Fichier au format Stanford</h2>

<jsp:useBean id="bean" class="servlet.action.marrayToStanford.MarrayToStanfordStep3Bean" scope="session" />

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

<%
  String path = bean.getPath();
  if (path != null) {
%>
<a href="<%= response.encodeURL(bean.getPath()) %>"> fichier Stanford </a>
<%
  }
%>

<jsp:include page="/jsp/bottom.jsp" />

</body>
</html>
