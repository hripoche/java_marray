package servlet.action.marrayToStanford;

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

public class MarrayToStanfordStep3Action extends Action implements ProcessMultipartAction {

  public void processMultipartAction (HttpServletRequest request, HttpServletResponse response, MultipartRequest multipartRequest) {
    HttpSession session = request.getSession(false);

    int nb = Integer.parseInt((String)multipartRequest.getParameter("nb"));

    boolean[] checkbox = new boolean[nb];
    for (int i=0; i<nb; i++) {
      checkbox[i] = multipartRequest.getParameter("checkbox"+i) != null && multipartRequest.getParameter("checkbox"+i).equals("on");
    }

    boolean result = true; // false in case of error

    String type = multipartRequest.getParameter("type");
    int mode=Computation.MARRAY_AGILENT;
    try {
      mode = Integer.parseInt(type);
    } catch (Exception e) {
      e.printStackTrace();
      result=false;
    }

    String[] filenames = new String[nb];

    Enumeration files = multipartRequest.getFileNames();
    SmallTextFile stf = new SmallTextFile();
    String name, filename;
    File f=null;
    File[] ff = new File[nb];
    File ffOut=null;
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

      for (int i=0; i<nb; i++) {
        if (name.equals("data_file"+i)) {
          filenames[i] = filename;
	  ff[i] = null;
          try {
            ff[i] = File.createTempFile("marray_to_stanford_"+i+"_","");
          } catch (IOException e) {
	    e.printStackTrace();
            result = false;
          }
System.err.println("name="+ff[i].getName()+" path="+ff[i].getAbsolutePath());
          f.renameTo(ff[i]);
          if (ff[i] != null) {
            ff[i].deleteOnExit();
          }
          if (mode == Computation.MARRAY_AFFYMETRIX) {
            stf.appendLine("0\t"+ff[i].getAbsolutePath()+"\t"+filenames[i]);
          } else {
            stf.appendLine((checkbox[i] ? "-" : "+")+"\t"+ff[i].getAbsolutePath()+"\t"+filenames[i]);
          }
        }
      }
    }

    File ffIn = null;
    try {
      ffIn = File.createTempFile("marray_to_stanford_in_","");
      stf.write(ffIn.getAbsolutePath());
      System.err.println("in: "+ffIn.getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
      result = false;
    }
    if (ffIn != null) {
      ffIn.deleteOnExit();
    }

    try {
      ffOut = File.createTempFile("marray_to_stanford_out_",".txt",new File(Constants.TEMP_DIRECTORY_VISIBLE));
    } catch (IOException e) {
      e.printStackTrace();
      result = false;
    }
    if (ffOut != null) {
      ffOut.deleteOnExit();
    }

    double pValueLogRatio = 1;
    try {
      pValueLogRatio = Double.parseDouble(multipartRequest.getParameter("pValueLogRatio"));
    } catch (Exception e) {
      e.printStackTrace();
      result = false;
    }

    double logRatio = 0;
    try {
      logRatio = Double.parseDouble(multipartRequest.getParameter("logRatio"));
    } catch (Exception e) {
      e.printStackTrace();
      result = false;
    }

    double signalNoiseRatio = 1;
    try {
	signalNoiseRatio = Double.parseDouble(multipartRequest.getParameter("signalNoiseRatio"));
    } catch (Exception e) {
      e.printStackTrace();
      result = false;
    }

    int n = 1;
    try {
      n = Integer.parseInt(multipartRequest.getParameter("n"));
    } catch (Exception e) {
      e.printStackTrace();
      result = false;
    }

    double x = 0;
    try {
      x = Double.parseDouble(multipartRequest.getParameter("x"));
    } catch (Exception e) {
      e.printStackTrace();
      result = false;
    }

    double sd = 0;
    try {
      sd = Double.parseDouble(multipartRequest.getParameter("sd"));
    } catch (Exception e) {
      e.printStackTrace();
      result = false;
    }

    boolean pValueLogRatioCheckbox = multipartRequest.getParameter("pValueLogRatioCheckbox") != null && multipartRequest.getParameter("pValueLogRatioCheckbox").equals("on");
    boolean logRatioCheckbox = multipartRequest.getParameter("logRatioCheckbox") != null && multipartRequest.getParameter("logRatioCheckbox").equals("on");
    boolean signalNoiseRatioCheckbox = multipartRequest.getParameter("signalNoiseRatioCheckbox") != null && multipartRequest.getParameter("signalNoiseRatioCheckbox").equals("on");
    boolean nCheckbox = multipartRequest.getParameter("nCheckbox") != null && multipartRequest.getParameter("nCheckbox").equals("on");
    boolean xCheckbox = multipartRequest.getParameter("xCheckbox") != null && multipartRequest.getParameter("xCheckbox").equals("on");
    boolean sdCheckbox = multipartRequest.getParameter("sdCheckbox") != null && multipartRequest.getParameter("sdCheckbox").equals("on");

    ParameterFilter parameterFilter = new ParameterFilter();
    parameterFilter.setPValueLogRatioFilter(pValueLogRatio);
    parameterFilter.setPValueLogRatioApply(pValueLogRatioCheckbox);
    parameterFilter.setLogRatioFilter(logRatio);
    parameterFilter.setLogRatioApply(logRatioCheckbox);
    parameterFilter.setSignalNoiseRatioFilter(signalNoiseRatio);
    parameterFilter.setSignalNoiseRatioApply(signalNoiseRatioCheckbox);
    parameterFilter.setMinimalNumberOfExperimentsFilter(n);
    parameterFilter.setMinimalNumberOfExperimentsApply(nCheckbox);
    parameterFilter.setMinimalVariationBetweenExperimentsFilter(x);
    parameterFilter.setMinimalVariationBetweenExperimentsApply(xCheckbox);
    parameterFilter.setStandardDeviationFilter(sd);
    parameterFilter.setStandardDeviationApply(sdCheckbox);

    //session.setMaxInactiveInterval(Constants.SESSION_MAX_INACTIVE_INTERVAL);

    String message=null, path=null;
    Report report = new Report(Report.SERVLET_MODE);
    if (result) { // pas de calcul si result==false
      try {
        String[] args = new String[3];
        args[1] = ffIn.getAbsolutePath();
        args[2] = ffOut.getAbsolutePath();
        result = Computation.toStanford(args,mode,parameterFilter,report);
      } catch (Exception e) {
        e.printStackTrace();
        result = false;
      }
    }

    String tempName = new File(Constants.TEMP_DIRECTORY_VISIBLE).getName();
    if (result && !fileWasNull) {
      message = report.getText();
      path = "/"+Constants.WEBAPP_NAME+"/"+tempName+"/"+ffOut.getName();
    } else {
      message = null;
      path = null;
    }
System.err.println("out: "+ffOut.getAbsolutePath());

    // suppression des fichiers temporaires inutiles
    if (ffIn != null) ffIn.delete();
    for (int i=0; i<nb; i++) {
      if (ff[i] != null) ff[i].delete();
    }

    MarrayToStanfordStep3Bean marrayToStanfordStep3Bean = new MarrayToStanfordStep3Bean();
    marrayToStanfordStep3Bean.setMessage(message);
    marrayToStanfordStep3Bean.setPath(path);

    session.setAttribute("bean", marrayToStanfordStep3Bean);
    forward("/jsp/marray_to_stanford_step3.jsp",request,response);
  }
}
