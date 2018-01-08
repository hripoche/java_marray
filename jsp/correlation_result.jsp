<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>Corr&eacute;lation entre deux microarrays Agilent: r&eacute;sultat</title>
</head>

<body>

<jsp:include page="/jsp/check_connection.jsp" />

<h1>Corr&eacute;lation entre deux microarrays Agilent: r&eacute;sultat</h1>

<jsp:useBean id="bean" class="servlet.action.correlation.CorrelationBean" scope="session" />

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

<jsp:include page="/jsp/bottom.jsp" />

</body>
</html>
