/**
 * Cluster.java
 * @author H. Ripoche
 * 14/02/2002
 */

package classifier.kmeans;

import classifier.*;

import java.util.Vector;

public class Cluster extends Vector {
  
  public Cluster () {
    super();
  };
  
  Instance createCentroid() {
    Instance inst;
    int len = 0;
    try {
      len = ((Item)(this.elementAt(0))).getArray().length;
    } catch (Exception e) {
      return null;
    }
    double[] array = new double[len];
    for (int k=0; k<len; k++) {
      array[k] = 0;
    }
    Item p;
    for (int j=0; j<this.size(); j++) {
      inst = (Instance) this.elementAt(j);
      p = (Item) inst;
      for (int k=0; k<len; k++) {
      	array[k] += p.getArray()[k];
      }
    }
    for (int k=0; k<len; k++) {
      array[k] /= this.size();
    }
    Item result = new Item(-1,"","",array);
    return result;
  }
  
  public String asString () {
    StringBuffer result = new StringBuffer();
    result.append("<pre>\n");
    for (int i=0; i<size(); i++) {
      result.append(elementAt(i).toString());
      result.append('\n');
    }
    result.append("</pre>\n");
    return result.toString();
  }
}
