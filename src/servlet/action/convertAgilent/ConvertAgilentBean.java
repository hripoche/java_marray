package servlet.action.convertAgilent;

public class ConvertAgilentBean {

  private String message = "none";
  private String agilentPath = "/";
  
  public ConvertAgilentBean () {}

  public String getMessage () {
    return message;
  }

  public void setMessage (String p) {
    message = p;
  }

  public String getAgilentPath () {
    return agilentPath;
  }

  public void setAgilentPath (String p) {
    agilentPath = p;
  }
}
