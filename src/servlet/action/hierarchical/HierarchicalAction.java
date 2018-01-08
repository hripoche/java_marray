package servlet.action.hierarchical;

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

public class HierarchicalAction extends Action implements ProcessMultipartAction {

  public void processMultipartAction (HttpServletRequest request, HttpServletResponse response, MultipartRequest multipartRequest) {
    HttpSession session = request.getSession(false);

    Enumeration files = multipartRequest.getFileNames();
    String name, filename;
    File f=null, ff1=null, ff2=null;
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
          ff1 = File.createTempFile("marray_hierarchical_in_","");
        } catch (IOException e) {
	  e.printStackTrace();
        }
System.err.println("name="+ff1.getName()+" path="+ff1.getAbsolutePath());
        f.renameTo(ff1);
        ff1.deleteOnExit();
      }
    }

    try {
      ff2 = File.createTempFile("marray_hierarchical_image_",".png",new File(Constants.TEMP_DIRECTORY_VISIBLE));
    } catch (IOException e) {
      e.printStackTrace();
    }
    ff2.deleteOnExit();

    boolean result;
    Report report = new Report(Report.SERVLET_MODE);

    String mode = multipartRequest.getParameter("mode");
    String distance = multipartRequest.getParameter("distance");

    report.appendLine("mode: "+mode);
    report.appendLine("distance: "+distance);

    try {
      String[] args = new String[5];
      args[1] = ff1.getAbsolutePath();
      args[2] = mode;
      args[3] = distance;
      args[4] = ff2.getAbsolutePath();
      result = Computation.hierarchical(args,report);
    } catch (Exception e) {
      e.printStackTrace();
      report.appendLine(""+e);
      result = false;
    }

    String tempName = new File(Constants.TEMP_DIRECTORY_VISIBLE).getName();
    String message, image;
    if (result && !fileWasNull) {
      message = report.getText();
      image = "/"+Constants.WEBAPP_NAME+"/"+tempName+"/"+ff2.getName();
    } else {
      message = null;
      image = null;
    }

    // suppression fichiers temporaires inutiles
    if (ff1 != null) ff1.delete();

    HierarchicalBean hierarchicalBean = new HierarchicalBean();
    hierarchicalBean.setMessage(message);
    hierarchicalBean.setImage(image);

    session.setAttribute("bean", hierarchicalBean);
    forward("/jsp/hierarchical_result.jsp",request,response);
  }
}
