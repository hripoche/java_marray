package servlet.action.sample;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import servlet.core.*;

public class SampleAction extends Action implements ProcessAction {

  public void processAction (HttpServletRequest request, HttpServletResponse response) {

    HttpSession session = request.getSession(false);

    String textfield = request.getParameter("textfield");
    SampleBean sampleBean = new SampleBean();
    if (textfield != null) {
      textfield = textfield.toUpperCase();
      sampleBean.setProperty(textfield);
    }
      
    session.setAttribute("bean", sampleBean);
    forward("/jsp/sample.jsp",request,response);
  }

}
