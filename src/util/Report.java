// Report.java

package util;

import util.SmallTextFile;

public class Report {

  final public static int APPLICATION_MODE = 0;
  final public static int SERVLET_MODE = 1;

  private int mode;
  private SmallTextFile stf;

  public Report (int m) {
    mode = m;
    stf = new SmallTextFile();
  }

  public int getMode () {
    return mode;
  }

  public void setMode (int m) {
    mode = m;
  }

  public void appendLine (String s) {
    if (mode == APPLICATION_MODE) {
      System.out.println(s);
    }
    if (mode == SERVLET_MODE) {
      stf.appendLine(s);
    }
  }

  public String getText () {
    if (mode == SERVLET_MODE) { 
      return stf.getText();
    }
    return null;
  }
}
