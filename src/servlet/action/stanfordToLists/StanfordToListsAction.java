package servlet.action.stanfordToLists;

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

public class StanfordToListsAction extends Action implements ProcessMultipartAction {

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
          ff1 = File.createTempFile("marray_stanford_to_lists_in_","");
        } catch (IOException e) {
	  e.printStackTrace();
        }
System.err.println("name="+ff1.getName()+" path="+ff1.getAbsolutePath());
        f.renameTo(ff1);
        ff1.deleteOnExit();
      }
    }

    String aDistance = aDistance = multipartRequest.getParameter("distance");

    boolean result;
    String message=null;
    Report report = new Report(Report.SERVLET_MODE);
    SmallTextFile experimentNames = new SmallTextFile();
    Vector vfiles = new Vector();
    try {
      String[] args = new String[3];
      args[1] = ff1.getAbsolutePath();
      args[2] = aDistance;
      result = Computation.stanfordToLists(args,Constants.TEMP_DIRECTORY_VISIBLE,"marray_stanford_to_lists_",".txt",experimentNames,vfiles,report);
    } catch (Exception e) {
      e.printStackTrace();
      result = false;
    }

    String[] paths = new String[vfiles.size()];
    String tempName = new File(Constants.TEMP_DIRECTORY_VISIBLE).getName();
    if (result && !fileWasNull) {
      for (int i=0; i<paths.length; i++) {
        paths[i] = "/"+Constants.WEBAPP_NAME+"/"+tempName+"/"+((File) vfiles.elementAt(i)).getName();
      }
      message = report.getText();
    } else {
      message = null;
      paths = null;
    }

    // suppression fichiers temporaires inutiles
    if (ff1 != null) ff1.delete();

    StanfordToListsBean stanfordToListsBean = new StanfordToListsBean();
    stanfordToListsBean.setMessage(message);
    stanfordToListsBean.setPaths(paths);
    stanfordToListsBean.setNames(experimentNames.getLines());

    session.setAttribute("bean", stanfordToListsBean);
    forward("/jsp/stanford_to_lists_result.jsp",request,response);
  }
}
