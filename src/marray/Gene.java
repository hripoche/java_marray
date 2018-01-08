// Gene.java

package marray;

public class Gene  {
  
  String name;
  double dist;
  String description;

  public Gene (String name, double dist, String description) {
    this.name = name;
    this.dist = dist;
    this.description = description;
  }

  public String getName () {
    return name;
  }

  public double getDist () {
    return dist;
  }

  public String getDescription () {
    return description;
  }

  public String toString () {
    return name+"\t"+dist+"\t"+description;
  }
}
