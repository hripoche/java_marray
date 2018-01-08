package servlet.core;

import javax.servlet.*;
import javax.servlet.http.*;

import com.oreilly.servlet.MultipartRequest;

public interface ProcessMultipartAction {
  void processMultipartAction (HttpServletRequest request, HttpServletResponse response, MultipartRequest multi);
}
