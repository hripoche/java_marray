<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page import="util.Lib" %>
<html>
<head>
  <title>Liste de g&egrave;nes tri&eacute;s</title>
</head>

<body>

<jsp:include page="/jsp/check_connection.jsp" />

<h1>Liste de g&egrave;nes tri&eacute;s</h1>

<form method="post" action="<%= response.encodeURL("/marray/controller") %>" enctype="multipart/form-data">
<input type="hidden" name="action" value="stanford_to_lists">

<table border=1 bgcolor="#eeffee">

<tr align="left">
 <th rowspan="1" align="right" bgcolor="#eeeeff">
 Donn&eacute;es:
 </th>

 <td align="left">
  Emplacement du fichier<br>
  <input type="file" name="data_file" size="40">
 </td>
</tr>

<tr align="left">
 <th rowspan="1" align="right" bgcolor="#eeeeff">
  Distance
 </th>

 <td align="left">
  <select name="distance">
  <option value="<%= Lib.DISTANCE_EUCLIDIAN %>" selected>Distance euclidienne
  <option value="<%= Lib.DISTANCE_CORRELATION %>">Distance bas&eacute;e sur corr&eacute;lation
  <option value="<%= Lib.DISTANCE_CORRELATION_GOLUB %>">Distance bas&eacute;e sur corr&eacute;lation Golub
  </select>
 </td>
</tr>

<tr>
  <th align="right" bgcolor="#eeeeff">Submit</TH>
  <TD>
  <input type="submit" name="submitted" value="Proceed" />
  <input type="reset" value="Reset fields" />
  </td>
</tr>

</table>
</form>

<jsp:include page="/jsp/bottom.jsp" />

</body>
</html>
