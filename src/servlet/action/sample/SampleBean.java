package servlet.action.sample;

public class SampleBean {

  private String property = "default value";
  
  public SampleBean () {}

  public String getProperty () {
    return property;
  }

  public void setProperty (String p) {
    property = p;
  }
}
