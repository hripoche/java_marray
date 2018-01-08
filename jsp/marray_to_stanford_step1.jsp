<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>Conversion vers le format Stanford</title>
</head>

<body>

<jsp:include page="/jsp/check_connection.jsp" />

<h1>Conversion vers le format Stanford</h1>

<h2>Etape 1: Saisie du nombre de microarrays</h2>

<form method="post" action="<%= response.encodeURL("/marray/controller") %>">
<input type="hidden" name="action" value="marray_to_stanford_step2">

<table border=1 bgcolor="#eeffee">

<tr align="left">
 <th rowspan="1" align="right" bgcolor="#eeeeff">
 Donn&eacute;es:
 </th>

 <td align="left">
  Nombre de fichiers microarray<br>
  <input type="text" name="nb" size="20">
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
