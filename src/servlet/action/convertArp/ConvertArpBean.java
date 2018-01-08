package servlet.action.convertArp;

public class ConvertArpBean {

  private String message = "none";
  private String arpPath = "/";
  
  public ConvertArpBean () {}

  public String getMessage () {
    return message;
  }

  public void setMessage (String p) {
    message = p;
  }

  public String getArpPath () {
    return arpPath;
  }

  public void setArpPath (String p) {
    arpPath = p;
  }
}
