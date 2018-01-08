<%
  String url = "/jsp/login.jsp";
  session = request.getSession(false);

  //out.println("session="+session);
  //if (session != null) {
  //  out.println("login="+session.getAttribute("login"));
  //}

  if (session == null || session.getAttribute("login") == null) {
    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
    dispatcher.forward(request, response);
  }
%>
