package servlet.action.correlation;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Enumeration;

import servlet.core.*;
import marray.*;
import util.*;
import application.Computation;

import com.oreilly.servlet.MultipartRequest;

public class CorrelationAction extends Action implements ProcessMultipartAction {

  public void processMultipartAction (HttpServletRequest request, HttpServletResponse response, MultipartRequest multipartRequest) {
    HttpSession session = request.getSession(false);

    Enumeration files = multipartRequest.getFileNames();
    String name, filename;
    File f=null, ff0=null, ff1=null;
    String filename0=null, filename1=null;
    boolean fileWasNull=false;
    while (files.hasMoreElements()) {
      name = (String)files.nextElement();
      filename = multipartRequest.getFilesystemName(name);
System.err.println("name="+name+" filename="+filename);
      f = multipartRequest.getFile(name);
      if (f == null) {
	fileWasNull = true;
        continue;
      }
      
      if (name.equals("data_file0")) {
	filename0 = filename;
        try {
          ff0 = File.createTempFile("marray_correlation1_","");
        } catch (IOException e) {
	  e.printStackTrace();
        }
System.err.println("name="+ff0.getName()+" path="+ff0.getAbsolutePath());
        f.renameTo(ff0);
        ff0.deleteOnExit();
      }

      if (name.equals("data_file1")) {
        filename1 = filename;
        try {
          ff1 = File.createTempFile("marray_correlation2_","");
        } catch (IOException e) {
	  e.printStackTrace();
        }
System.err.println("name="+ff1.getName()+" path="+ff1.getAbsolutePath());
        f.renameTo(ff1);
        ff1.deleteOnExit();
      }
    }

    boolean checkbox0 = multipartRequest.getParameter("checkbox0") != null && multipartRequest.getParameter("checkbox0").equals("on");
    boolean checkbox1 = multipartRequest.getParameter("checkbox1") != null && multipartRequest.getParameter("checkbox1").equals("on");

    String message;
    Report report = new Report(Report.SERVLET_MODE);
    try {
      String[] args = new String[3];
      args[1] = ff0.getAbsolutePath();
      args[2] = ff1.getAbsolutePath();
      args[1] = (checkbox0 ? "-" : "+") + "\t" + args[1] + "\t" + filename0;
      args[2] = (checkbox1 ? "-" : "+") + "\t" + args[2] + "\t" + filename1;
      Computation.correlation(args,report);
      message = report.getText();
    } catch (Exception e) {
      e.printStackTrace();
      message = null;
    }
    if (fileWasNull) {
      message = null;
    }

    // suppression fichiers temporaires inutiles
    if (ff0 != null) ff0.delete();
    if (ff1 != null) ff1.delete();

    CorrelationBean correlationBean = new CorrelationBean();
    correlationBean.setMessage(message);

    session.setAttribute("bean", correlationBean);
    forward("/jsp/correlation_result.jsp",request,response);
  }
}
