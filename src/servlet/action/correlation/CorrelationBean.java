package servlet.action.correlation;

public class CorrelationBean {

  private String message = "none";
  
  public CorrelationBean () {}

  public String getMessage () {
    return message;
  }

  public void setMessage (String p) {
    message = p;
  }
}
