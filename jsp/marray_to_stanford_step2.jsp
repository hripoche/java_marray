<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page import="application.Computation" %>
<html>
<head>
  <title>Conversion vers le format Stanford</title>
<script language="javascript">
function validate (f,max) {

  if (f.sd.value < 0) {
    alert("L'ecart-type doit etre positif");
    f.sd.focus();
    return false;
  } else if (f.n.value < 1 || f.n.value > max) {
    alert("Le nombre d'expérience doit etre compris entre 1 et "+max);
    f.n.focus();
    return false;
  } else if (f.x.value < 1) {
    alert("La variation de l'expression du gène doit etre supérieure ou égale à 1");
    f.x.focus();
    return false;
  } else if (f.pValueLogRatio.value < 0 || f.pValueLogRatio.value > 1) {
    alert("La pValue doit etre comprise entre 0 et 1");
    f.pValueLogRatio.focus();
    return false;
  } else if (f.signalNoiseRatio.value < 0) {
    alert("Le rapport signal/bruit doit etre positif");
    f.signalNoiseRatio.focus();
    return false;
  } else {
    return true;
  }
}
</script>
</head>

<body>

<jsp:include page="/jsp/check_connection.jsp" />

<h1>Conversion vers le format Stanford</h1>

<h2>Etape 2: Saisie des fichiers microarrays</h2>

<jsp:useBean id="bean" class="servlet.action.marrayToStanford.MarrayToStanfordStep2Bean" scope="session" />

<% int nb = bean.getNb();%>

<form method="post" action="<%= response.encodeURL("/marray/controller") %>" enctype="multipart/form-data" onsubmit="javascript:return validate(this,<%= nb %>)">
<input type="hidden" name="action" value="marray_to_stanford_step3">
<input type="hidden" name="nb" value="<%= nb %>">

<table border=1 bgcolor="#eeffee">

<% for (int i=0; i<nb; i++) { %>

<% if (i == 0) { %>
<tr align="left">
 <th rowspan="<%= nb %>" align="right" bgcolor="#eeeeff">
 Donn&eacute;es:
 </th>

 <td align="left">
  Emplacement du fichier <%= (i+1) %><br>
  <input type="file" name="data_file<%= i %>" size="40">
  <br>
  Inverser Cy5/Cy3: <input type="checkbox" name="checkbox<%= i %>" value="on">
 </td>
</tr>
<% } // end if %>

<% if (i != 0) { %>
<tr align="left">
 <td align="left">
  Emplacement du fichier <%= (i+1) %><br>
  <input type="file" name="data_file<%= i %>" size="40">
  <br>
  Inverser Cy5/Cy3: <input type="checkbox" name="checkbox<%= i %>" value="on">
 </td>
</tr>
<% } // end if %>

<% } // end for %>

<tr align="left">
 <th rowspan="1" align="right" bgcolor="#eeeeff">
 Type
 </th>

 <td align="left">
  <select name="type">
  <option value="<%= Computation.MARRAY_AGILENT %>" selected>Agilent
  <option value="<%= Computation.MARRAY_AFFYMETRIX %>">Affymetrix
  <option value="<%= Computation.MARRAY_GENEPIX %>">Genepix
  </select>
 </td>
</tr>

<tr align="left">
 <th rowspan="1" align="right" bgcolor="#eeeeff">
 (Agilent) pValueLogRatio <=
 </th>

 <td align="left">
  <input name="pValueLogRatio" value="1" size="10">
  Appliquer le filtre: <input type="checkbox" name="pValueLogRatioCheckbox" value="on">
 </td>
</tr>

<tr align="left">
 <th rowspan="1" align="right" bgcolor="#eeeeff">
 (Agilent, Genepix) |logRatio| >=
 </th>

 <td align="left">
  <input name="logRatio" value="0" size="10">
  Appliquer le filtre: <input type="checkbox" name="logRatioCheckbox" value="on">
 </td>
</tr>

<tr align="left">
 <th rowspan="1" align="right" bgcolor="#eeeeff">
  (Agilent, Genepix) Rapport signal/bruit >=
 </th>

 <td align="left">
  <input name="signalNoiseRatio" value="1" size="10">
  Appliquer le filtre: <input type="checkbox" name="signalNoiseRatioCheckbox" value="on">
 </td>
</tr>

<tr align="left">
 <th rowspan="1" align="right" bgcolor="#eeeeff">
 Nombre minimal d'exp&eacute;riences dans lesquels le g&egrave;ne doit &ecirc;tre pr&eacute;sent
 </th>

 <td align="left">
   <input name="n" value="1" size="10">
   Appliquer le filtre: <input type="checkbox" name="nCheckbox" value="on">
 </td>
</tr>

<tr align="left">
 <th rowspan="1" align="right" bgcolor="#eeeeff">
 Variation minimale de l'expression du g&egrave;ne dans les exp&eacute;riences
 </th>

 <td align="left">
   <input name="x" value="1" size="10">
   Appliquer le filtre: <input type="checkbox" name="xCheckbox" value="on">
 </td>
</tr>

<tr align="left">
 <th rowspan="1" align="right" bgcolor="#eeeeff">
 Ecart-type minimal
 </th>

 <td align="left">
   <input name="sd" value="0" size="10">
   Appliquer le filtre: <input type="checkbox" name="sdCheckbox" value="on">
 </td>
</tr>

<tr>
  <th align="right" bgcolor="#eeeeff">Submit</th>
  <td>
  <input type="submit" name="submitted" value="Proceed" />
  <input type="reset" value="Reset fields" />
  </td>
</tr>

</table>
</form>

<jsp:include page="/jsp/bottom.jsp" />

</body>
</html>
