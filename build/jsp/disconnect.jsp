<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>D&eacute;connexion</title>
</head>

<body
  bgcolor="#FFFFFF"
  text="#000000"
  link="#0000FF"
  vlink="#000080"
  alink="#FF0000"
 >

<%
   session = request.getSession(false);
   if (session != null) {
     session.removeAttribute("login");
     session.invalidate();
   }
%>

<h1>Vous avez &eacute;t&eacute; d&eacute;connect&eacute;</h1>
<br>
Vous pouvez vous reconnecter.
<br>

<center>
<table border=1>
<tr align="left">
 <td> <a href="<%= response.encodeURL("/marray/jsp/login.jsp") %>"> login </a> </td>
</tr>
</table>
</center>

</body>
</html>
