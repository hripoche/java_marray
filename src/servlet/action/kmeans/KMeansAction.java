package servlet.action.kmeans;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Enumeration;
import java.util.Vector;

import servlet.core.*;
import marray.*;
import util.*;
import application.Computation;
import application.Constants;

import com.oreilly.servlet.MultipartRequest;

public class KMeansAction extends Action implements ProcessMultipartAction {

  public void processMultipartAction (HttpServletRequest request, HttpServletResponse response, MultipartRequest multipartRequest) {
    HttpSession session = request.getSession(false);

    Enumeration files = multipartRequest.getFileNames();
    String name, filename;
    File f=null, ff1=null;
    boolean fileWasNull = false;
    while (files.hasMoreElements()) {
      name = (String)files.nextElement();
      filename = multipartRequest.getFilesystemName(name);
System.err.println("name="+name+" filename="+filename);
      f = multipartRequest.getFile(name);
      if (f == null) {
	fileWasNull = true;
        continue;
      }
      
      if (name.equals("data_file")) {
        try {
          ff1 = File.createTempFile("marray_kmeans_in_","");
        } catch (IOException e) {
	  e.printStackTrace();
        }
System.err.println("name="+ff1.getName()+" path="+ff1.getAbsolutePath());
        f.renameTo(ff1);
        ff1.deleteOnExit();
      }
    }

    boolean result;
    Report report = new Report(Report.SERVLET_MODE);

    try {
      String[] args = new String[4];
      args[1] = ff1.getAbsolutePath();
      args[2] = multipartRequest.getParameter("nbClusters");
      args[3] = multipartRequest.getParameter("maxIter");
      result = Computation.kmeans(args,report);
    } catch (Exception e) {
      e.printStackTrace();
      report.appendLine(""+e);
      result = false;
    }

    // suppression fichiers temporaires inutiles
    if (ff1 != null) ff1.delete();

    KMeansBean kMeansBean = new KMeansBean();
    kMeansBean.setMessage(report.getText());

    session.setAttribute("bean", kMeansBean);
    forward("/jsp/kmeans_result.jsp",request,response);
  }
}
