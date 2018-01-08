<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>Classification kmeans: r&eacute;sultat</title>
</head>

<body>

<jsp:include page="/jsp/check_connection.jsp" />

<h1>Classification kmeans: r&eacute;sultat</h1>

<jsp:useBean id="bean" class="servlet.action.kmeans.KMeansBean" scope="session" />

<%
  String message = bean.getMessage();
  if (message == null) {
%>
  <h2>format incorrect</h2>
<%
  } else {
%>
<%= message %>
<%
  }
%>

<jsp:include page="/jsp/bottom.jsp" />

</body>
</html>
