package servlet.action.stanfordToLists;

public class StanfordToListsBean {

  private String message = "none";
  private String[] paths = null;
  private String[] names = null;
  
  public StanfordToListsBean () {}

  public String getMessage () {
    return message;
  }

  public void setMessage (String m) {
    message = m;
  }

  public String[] getPaths () {
    return paths;
  }

  public void setPaths (String[] p) {
    paths = p;
  }

  public String[] getNames () {
    return names;
  }

  public void setNames (String[] n) {
    names = n;
  }
}
