<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>Corr&eacute;lation entre deux microarrays Agilent</title>
</head>

<body>

<jsp:include page="/jsp/check_connection.jsp" />

<h1>Corr&eacute;lation entre deux microarrays Agilent</h1>

<form method="post" action="<%= response.encodeURL("/marray/controller") %>" enctype="multipart/form-data">
<input type="hidden" name="action" value="correlation">

<table border=1 bgcolor="#eeffee">

<tr align="left">
 <th rowspan="2" align="right" bgcolor="#eeeeff">
 Donn&eacute;es:
 </th>

 <td align="left">
  Emplacement du fichier 1:
  <br>
  <input type="file" name="data_file0" size="40">
  <br>
  Inverser Cy5/Cy3: <input type="checkbox" name="checkbox0" value="on">
 </td>
</tr>

<tr align="left">
 <td align="left">
  Emplacement du fichier 2:
  <br>
  <input type="file" name="data_file1" size="40">
  <br>
  Inverser Cy5/Cy3: <input type="checkbox" name="checkbox1" value="on">
 </td>
</tr>

<tr>
  <th align="right" bgcolor="#eeeeff">Submit</TH>
  <TD>
  <input type="submit" name="submitted" value="Proceed" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <input type="reset" value="Reset fields" />
  </td>
</tr>

</table>
</form>

<jsp:include page="/jsp/bottom.jsp" />

</body>
</html>
