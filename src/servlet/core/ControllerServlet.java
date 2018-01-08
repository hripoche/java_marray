package servlet.core;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.oreilly.servlet.MultipartRequest;
import application.Constants;

public class ControllerServlet extends HttpServlet {

  final static private String ACTION_PROPERTIES_FILE = "actions.txt";
  final static private String ACTION_NAME = "action";
  final static private int multipartMaxSize = Constants.MULTIPART_MAX_SIZE;
  static private String multipartTmpDirectory = Constants.TEMP_DIRECTORY_HIDDEN;


  Properties properties;
  Action action;
	
  private final static Properties getPropertiesFromInputStream (InputStream is) {
    Properties props = new Properties();
    try {
      props.load(is);
    } catch (FileNotFoundException e) {
      return null;
    } catch (IOException e) {
      return null;
    }
    return props;
  }
	
  public void init() throws ServletException {
    super.init();
    properties = getPropertiesFromInputStream(getClass().getResourceAsStream(ACTION_PROPERTIES_FILE));
System.err.println("properties="+properties);
    Action.setServlet(this);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request,response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request,response);
  }

  public void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    if (session == null) {
      session = request.getSession(true);
      forward("/jsp/login.jsp",request,response);
    }

    Object obj = null;
    String action = request.getParameter(ACTION_NAME);

    // test if action is embedded within multipart request
    MultipartRequest multipartRequest = null;
    boolean multipart = false;
    if (action == null) {
      try {
	multipartRequest = new MultipartRequest(request,multipartTmpDirectory,multipartMaxSize);
      } catch (IOException e) {
	e.printStackTrace();
      }
      action = multipartRequest.getParameter(ACTION_NAME);
      multipart = (action != null);
    }

System.err.println("action="+action);
    if (action != null) {
      String className = (String) properties.get(action);
System.err.println("className="+className);
      try {
        obj = Class.forName(className).newInstance();
      } catch (Exception e) {
	  e.printStackTrace();
        obj = null;	
      }
    }
    
    if (obj == null) {
      forward("/jsp/illegal_request.jsp",request,response);
    } else {
      if (multipart) {
        ((ProcessMultipartAction) obj).processMultipartAction(request,response,multipartRequest);
      } else {
        ((ProcessAction) obj).processAction(request,response);
      }
    }
  }
  
  public void destroy() {
    super.destroy();
    action = null;
    properties = null;
  }
  
  public String getServletInfo() {
    return "ControllerServlet";
  }
  
  public void forward(String address, HttpServletRequest request, HttpServletResponse response) {
    try {
      RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(address);
      dispatcher.forward(request, response);
    } catch (Exception e) {
      System.err.println("forward failed: "+address);
      e.printStackTrace();
    }
  }
}
