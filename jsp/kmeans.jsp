<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>Classification kmeans</title>
</head>

<body>

<jsp:include page="/jsp/check_connection.jsp" />

<h1>Classification kmeans</h1>

<form method="post" action="<%= response.encodeURL("/marray/controller") %>" enctype="multipart/form-data">
<input type="hidden" name="action" value="kmeans">

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
   Nombre de classes
 </th>

 <td align="left">
  <input type="text" name="nbClusters" value="10" size="20">
 </td>
</tr>

<tr align="left">
 <th rowspan="1" align="right" bgcolor="#eeeeff">
   Nombre d'it&eacute;rations maximum
 </th>

 <td align="left">
  <input type="text" name="maxIter" value="100" size="20">
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
