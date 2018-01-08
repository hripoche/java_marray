package servlet.action.login;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import servlet.core.*;
import application.Constants;

public class LoginAction extends Action implements ProcessAction {

  public void processAction (HttpServletRequest request, HttpServletResponse response) {

    HttpSession session = request.getSession(false);

    String user = request.getParameter("user");
    String password = request.getParameter("password");
    
    if (user != null &&
        password != null &&
        user.equals(Constants.USER_NAME) &&
        password.equals(Constants.USER_PASSWORD)) {

      session.setAttribute("login","true");
      forward("/jsp/welcome.jsp",request,response);
    } else {
      forward("/jsp/login.jsp",request,response);
    }
  }

}
