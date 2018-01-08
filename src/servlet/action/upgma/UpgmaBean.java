package servlet.action.upgma;

public class UpgmaBean {

  private String message = "none";
  private String image = "";
  
  public UpgmaBean () {}

  public String getMessage () {
    return message;
  }

  public void setMessage (String m) {
    message = m;
  }

  public String getImage () {
    return image;
  }

  public void setImage (String i) {
    image = i;
  }
}
