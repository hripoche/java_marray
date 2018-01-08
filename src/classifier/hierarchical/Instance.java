// Instance.java

package classifier.hierarchical;

/**
 * @author H. Ripoche
 * 20/02/2002
 */
public class Instance {

  static private DataMatrix dataMatrix;

  private String name;
  private int index;

  public static String[] getNames () {
    return dataMatrix.getNames();
  }

  public static void setDataMatrix (DataMatrix dm) {
    dataMatrix = dm;
  }

  public static double distance (Instance i1, Instance i2) {
    return dataMatrix.getMatrixValue(i1.index,i2.index);
  }  

  public Instance () {
  }

  public Instance (String name, int index) {
    this.name = name;
    this.index = index;
  }

  public String getName () {
    return name;
  }

  public int getIndex () {
    return index;
  }

  public String toString () {
    return "["+name+":"+index+"]";
  }
}
