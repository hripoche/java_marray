package servlet.action.marrayToStanford;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import servlet.core.*;

public class MarrayToStanfordStep2Action extends Action implements ProcessAction {

  public void processAction (HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession(false);

    MarrayToStanfordStep2Bean marrayToStanfordStep2Bean = new MarrayToStanfordStep2Bean();
    int n=0;
    try {
      n = Integer.parseInt((String)request.getParameter("nb"));
    } catch (Exception e) { // retour step1
      forward("/jsp/marray_to_stanford_step1.jsp",request,response);
    }
    if (n <= 0) { // retour step1
      forward("/jsp/marray_to_stanford_step1.jsp",request,response);
    }

    marrayToStanfordStep2Bean.setNb(n);

    session.setAttribute("bean", marrayToStanfordStep2Bean);
    forward("/jsp/marray_to_stanford_step2.jsp",request,response);
  }
}
