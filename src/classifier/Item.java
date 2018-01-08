/**
 * Item.java
 * @author H. Ripoche
 * 14/02/2002
 */

package classifier;

import java.util.*;

import util.*;

public class Item implements Instance {

  private int index;
  private String name;
  private String description;
  private double[] array;
  
  public int getIndex() {
    return index;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public double[] getArray() {
    return array;
  }
  
  public double distance (Instance inst) {
    return Lib.euclidian(this.array,((Item)inst).array);
  }
  
  public Item(int index, String name, String description, double[] array) {
    this.index = index;
    this.name = name;
    this.description = description;
    this.array = array;
  }

  public String toString () {
    return name+"  \t"+description;
  }
}
