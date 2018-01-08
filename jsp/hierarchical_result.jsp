<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>Classification hi&eacute;rarchique: r&eacute;sultat</title>
</head>

<body>

<jsp:include page="/jsp/check_connection.jsp" />

<h1>Classification hi&eacute;rarchique: r&eacute;sultat</h1>

<jsp:useBean id="bean" class="servlet.action.hierarchical.HierarchicalBean" scope="session" />

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
  String image = bean.getImage();

  if (image != null && !image.equals("")) {
%>
<img src="<%= image %>">
<%
  }
%>

<jsp:include page="/jsp/bottom.jsp" />

</body>
</html>
