package servlet.action.convertArp;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Enumeration;

import servlet.core.*;
import marray.*;
import util.*;
import application.Computation;
import application.Constants;

import com.oreilly.servlet.MultipartRequest;

public class ConvertArpAction extends Action implements ProcessMultipartAction {

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
          ff1 = File.createTempFile("marray_convert_arp_in_","");
        } catch (IOException e) {
	  e.printStackTrace();
        }
System.err.println("name="+ff1.getName()+" path="+ff1.getAbsolutePath());
        f.renameTo(ff1);
        ff1.deleteOnExit();
      }
    }

    try {
      ff2 = File.createTempFile("marray_convert_arp_out_",".arp",new File(Constants.TEMP_DIRECTORY_VISIBLE));
    } catch (IOException e) {
      e.printStackTrace();
    }
    ff2.deleteOnExit();

    String message=null, arpPath=null;
    boolean result;
    Report report = new Report(Report.SERVLET_MODE);
    try {
      String[] args = new String[3];
      args[1] = ff1.getAbsolutePath();
      args[2] = ff2.getAbsolutePath();
      result = Computation.agilentToArp(args,report);
    } catch (Exception e) {
      e.printStackTrace();
      result = false;
    }

    String tempName = new File(Constants.TEMP_DIRECTORY_VISIBLE).getName();
    if (result && !fileWasNull) {
      message = report.getText();
      arpPath = "/"+Constants.WEBAPP_NAME+"/"+tempName+"/"+ff2.getName();
    } else {
      message = null;
      arpPath = null;
    }

    // suppression fichiers temporaires inutiles
    if (ff1 != null) ff1.delete();

    ConvertArpBean convertArpBean = new ConvertArpBean();
    convertArpBean.setMessage(message);
    convertArpBean.setArpPath(arpPath);

    session.setAttribute("bean", convertArpBean);
    forward("/jsp/convert_arp_result.jsp",request,response);
  }
}
