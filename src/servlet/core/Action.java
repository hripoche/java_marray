package servlet.core;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Action {

  static ControllerServlet servlet;

  static ControllerServlet getServlet () {
    return servlet;
  }

  static void setServlet (ControllerServlet s) {
    servlet = s;
  }
 
  static protected void forward (String address, HttpServletRequest request, HttpServletResponse response) {
    servlet.forward(address,request,response);
  }

}
