package servlet.action.hierarchical;

public class HierarchicalBean {

  private String message = "none";
  private String image = "";
  
  public HierarchicalBean () {}

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
