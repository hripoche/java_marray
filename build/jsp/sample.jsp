<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
  <title>Sample Demo</title>
</head>

<body>

<jsp:include page="/jsp/check_connection.jsp" />

<h1>Sample Demo</h1>
<center>
<jsp:useBean id="bean" class="servlet.action.sample.SampleBean" scope="session" />
<br>
<form action="<%= response.encodeURL("/marray/controller") %>">
<input type="hidden" name="action" value="sample">
<p>
<input type="text" name="textfield" value="<%= bean.getProperty() %>">
<p>
<input type="submit" value="Submit">
<input type="reset" value="Reset">
</form>
</center>

<jsp:include page="/jsp/bottom.jsp" />

</body>
</html>
