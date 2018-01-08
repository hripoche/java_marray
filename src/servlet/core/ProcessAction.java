package servlet.core;

import javax.servlet.*;
import javax.servlet.http.*;

public interface ProcessAction {
  void processAction (HttpServletRequest request, HttpServletResponse response);
}
