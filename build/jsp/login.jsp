<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>Login</title>
</head>

<body
  bgcolor="#FFFFFF"
  text="#000000"
  link="#0000FF"
  vlink="#000080"
  alink="#FF0000"
 >

<center>
<h1>Login</h1>

<form method="post" action="<%= response.encodeURL("/marray/controller") %>">
<input type="hidden" name="action" value="login">

<table border=1 bgcolor="#eeffee">

<tr align="left">
 <td align="left" bgcolor="#eeeeff">
  <b>Nom:</b>
 </td>
 <td align="left">
  <input type="text" name="user" size="10">
 </td>
</tr>

<tr align="left">
 <td align="left" bgcolor="#eeeeff">
  <b>Mot de passe:</b>
 </td>
 <td align="left">
  <input type="password" name="password" size="10">
 </td>
</tr>

<tr>
  <th align="right" bgcolor="#eeeeff">Submit</TH>
  <td>
   <input type="submit" name="submitted" value="Proceed" />
   <input type="reset" value="Reset fields" />
  </td>
</tr>

</table>
</center>

</form>

</body>
</html>
