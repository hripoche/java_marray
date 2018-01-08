<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>Liste de g&egrave;nes tri&eacute;s: r&eacute;sultat</title>
</head>

<body>

<jsp:include page="/jsp/check_connection.jsp" />

<h1>Liste de g&egrave;nes tri&eacute;s: r&eacute;sultat</h1>

<jsp:useBean id="bean" class="servlet.action.stanfordToLists.StanfordToListsBean" scope="session" />

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

<ul>
<%
  String[] paths = bean.getPaths();
  String[] names = bean.getNames();
  if (paths != null && names != null && paths.length == names.length) {
    for (int i=0; i<paths.length; i++) {
%>
  <li> <a href="<%= response.encodeURL(paths[i]) %>"> <%= names[i] %> </a>
<%
    }
  } else {
%>
  <h2>pb nommage de fichiers</h2>
<%
  }
%>
</ul>
<br>

<jsp:include page="/jsp/bottom.jsp" />

</body>
</html>
