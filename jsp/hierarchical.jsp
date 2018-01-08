<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>Classification hi&eacute;rarchique</title>
</head>

<body>

<jsp:include page="/jsp/check_connection.jsp" />

<h1>Classification hi&eacute;rarchique</h1>

<form method="post" action="<%= response.encodeURL("/marray/controller") %>" enctype="multipart/form-data">
<input type="hidden" name="action" value="hierarchical">

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
   Mode
 </th>

 <td align="left">
  <select name="mode">
  <option selected>Single linkage
  </select>
 </td>
</tr>

<tr align="left">
 <th rowspan="1" align="right" bgcolor="#eeeeff">
  Distance
 </th>

 <td align="left">
  <select name="distance">
  <option selected>Distance euclidienne
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
