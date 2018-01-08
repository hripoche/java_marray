// Genepix.java

package marray;

import java.io.*;
import java.util.*;

import util.*;

/**
 * @author H. Ripoche
 */
public class Genepix extends MicroArray {
	
  private String name;
  private Hashtable params;

  private ParameterFilter parameterFilter = new ParameterFilter();
  private boolean[] selected;

  private String[] systematicNames; // GenBank ID
  private String[] ids;
  private double[] f635medians;
  private double[] f635means;
  private double[] b635medians;
  private double[] b635means;
  private double[] b635sds;
  private double[] f532medians;
  private double[] f532means;
  private double[] b532medians;
  private double[] b532means;
  private double[] b532sds;
  private double[] medianOfRatios;
  private double[] logRatios;
  private int[] flags;
  private String[] geneNames; // description
  private int index;

  public Genepix (int length) {
    selected = new boolean[length];
    systematicNames = new String[length];
    ids = new String[length];
    f635medians = new double[length];
    f635means = new double[length];
    b635medians = new double[length];
    b635means = new double[length];
    b635sds = new double[length];
    f532medians = new double[length];
    f532means = new double[length];
    b532medians = new double[length];
    b532means = new double[length];
    b532sds = new double[length];
    medianOfRatios = new double[length];
    logRatios = new double[length];
    flags = new int[length];
    geneNames = new String[length];
    index = 0;
  }

  public static Genepix read (String inputLine) {

    String inputFilename, logicalName=null;
    String[] str = Lib.asStringArray(inputLine," \t#");
    boolean dyeSwap = false;
    if (str.length == 1) { // soit le fichier seul (nom physique)
      inputFilename = str[0];
    } else if (str.length == 3) { // soit marqueur_dye_swap | nom_physique | nom_logique
      String item = str[0];
      if (item.equals("+")) {
      	dyeSwap = false;
      } else if (item.equals("-")) {
      	dyeSwap = true;
      } else {
      	System.err.println("Unknown line beginning");
      	return null;
      }
      inputFilename = str[1];	
      logicalName = str[2];
    } else {
      System.err.println("Bad file naming");
      return null;
    }

    logicalName = (logicalName != null ? logicalName : Lib.lastToken(inputFilename,"/\\"));
    LargeTextFile ltfIn = new LargeTextFile();
    try {
      ltfIn.openRead(inputFilename);
    } catch (IOException e) {
      System.err.println("Can't read: "+inputFilename);
      return null;
    }

    String line;
    String[] stringArray=null;
    Genepix genepix = null;
    int i=0, len=0, block=-1;
    try {
      while ((line = ltfIn.getLine()) != null) {
        if (line.startsWith("\"Block\"") || line.startsWith("Block")) {
	  block = len;
        }
        if (!line.trim().equals("")) len++;
      }
      ltfIn.close();
      ltfIn.openRead(inputFilename);
      genepix = new Genepix(len-block-1);
      genepix.setName(logicalName);
//System.err.println("len="+len);
//System.err.println("block="+block);
      while ((line = ltfIn.getLine()) != null) {
//System.err.println("i="+i);
//System.err.println("line="+line);
       	Hashtable h = new Hashtable();
      	if (i > 1 && i < block) {
          stringArray = Lib.asStringArray(line,"\"=");
          if (stringArray.length > 1) {
	    h.put(stringArray[0],stringArray[1]);
          } else if (stringArray.length == 1) {
            h.put(stringArray[0],"");
          }
      	}
      	if (i == block) {
      	  genepix.setParams(h);
          stringArray = Lib.asStringArrayWithNulls(line,"\t");
      	  if (!Genepix.checkHeaderOK(stringArray)) {
      	    System.err.println("\nBad format file - check column names\n");
       	    for (int k=0; k<stringArray.length; k++) {
      	      System.err.println(""+k+"\t"+stringArray[k]);
      	    }
      	    return null;
      	  }
      	}
      	if (i > block) {
          stringArray = Lib.asStringArrayWithNulls(line,"\t");
      	  genepix.add(stringArray,dyeSwap);
      	}
      	i++;
      }
      ltfIn.close();
    } catch (IOException e) {
      System.err.println(""+e);
    }
    return genepix;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public Hashtable getParams() {
    return params;
  }
  
  public void setParams (Hashtable h) {
    params = h;
  }
  
  public int getLength() {
    return index;
  }
  
  public String[] getSystematicNames() {
    return systematicNames;
  }

  public double[] getLogRatios() {
    return logRatios;
  }

  public void initialFilter () {
    int count=0;
    for (int i=0; i<this.getLength(); i++) {
      int flag = flags[i];
      String systematicName = systematicNames[i];
      if (!systematicName.equals("TEDMSO") && (flag == 0 || flag == 100)) {
      	this.selected[i] = true;
        count++;
      } else {
        this.selected[i] = false;
      }
    }
    parameterFilter.setInitialSelected(count);
  }

  public void filterByLogRatio (double logRatio) {
    int count=0;
    for (int i=0; i<this.getLength(); i++) {
      if (selected[i] && Math.abs(logRatios[i]) >= logRatio) {
        count++;
      } else {
        selected[i] = false;
      }
    }
    parameterFilter.setLogRatioFilter(logRatio);
    parameterFilter.setLogRatioSelected(count);
    parameterFilter.setLogRatioApply(true);
  }

  public void filterBySignalNoiseRatio (double ratio) {
    int count=0;
    for (int i=0; i<this.getLength(); i++) {
      if (selected[i] &&
          (((f635medians[i] - b635medians[i]) > ratio * b635sds[i]) ||
           ((f532medians[i] - b532medians[i]) > ratio * b532sds[i]))) {
        count++;
      } else {
        selected[i] = false;
      }
    }
    parameterFilter.setSignalNoiseRatioFilter(ratio);
    parameterFilter.setSignalNoiseRatioSelected(count);
    parameterFilter.setSignalNoiseRatioApply(true);
  }

  public String[] getSystematicNamesWithFilter () {
    Vector names = new Vector();
    for (int i=0; i<this.getLength(); i++) {
      if (this.selected[i]) {
      	names.add(this.systematicNames[i]);
      }
    }
    String[] result = new String[names.size()];
    names.copyInto(result);
    return result;
  }

  public String getGeneNameBySystematicName (String systematicName) {
    for (int i=0; i<index; i++) {
      if (systematicName.equals(systematicNames[i])) {
      	return geneNames[i];
      }
    }
    return null;
  }

  /**
   * Seeks logRatio according to systematicName
   * @return logRatio if gene was selected or Double.NaN
   * if gene was not selected
   */
  public double getLogRatioBySystematicName (String systematicName) {
    for (int i=0; i<index; i++) {
      if (systematicName.equals(systematicNames[i])) {
      	//if (selected[i]) {
          return logRatios[i];
        //} else {
	//  return Double.NaN;
        //}
      }
    }
    return Double.NaN;
  }

  /**
   * lecture des headerNames avec ou sans \"
   * (les \" sont supprimés par ouverture/sauvegarde sous excel)
   */
  public static boolean checkHeaderOK (String[] stringArray) {
    boolean result = true;

    String[] headerNames = {"Block",
                            "Name",
                            "ID",
                            "F635 Median",
                            "F635 Mean",
                            "B635 Median",
                            "B635 Mean",
                            "B635 SD",
                            "F532 Median",
                            "F532 Mean",
                            "B532 Median",
                            "B532 Mean",
                            "B532 SD",
                            "Median of Ratios (635/532)",
                            "Log Ratio (635/532)",
                            "Flags"
                            //,"nom_gene"
                            };

    int[] headerIndexes = {0,3,4,8,9,11,12,13,17,18,20,21,22,46,69,80}; //,82};
    String headerName;
    int headerIndex;
    
    if (headerNames.length != headerIndexes.length) {
      System.err.println("headerNames and headerIndexes do not have same length");
      return false;
    }
    
    if (stringArray.length < headerIndexes[headerIndexes.length-1]) {
      return false;
    }

    for (int i=0; i<headerIndexes.length; i++) {
      headerIndex = headerIndexes[i];
      headerName = headerNames[i];
      result = result && (stringArray[headerIndex].equals(headerName)
                          ||
                          stringArray[headerIndex].equals("\""+headerName+"\""));
      if (!result) { System.err.println(""+headerIndex); return false;}	
    }
    return result;
  }

  public void add (String[] stringArray, boolean dyeSwap) {

    double logRatio = 0.0;

    if (stringArray[69].equals("Error")) {
      logRatio = Double.NaN;
    } else {
      logRatio = Double.parseDouble(stringArray[69]);
    }
/*
    if (stringArray[46].equals("Error")) {
      logRatio = Double.NaN;
    } else {
      logRatio = Math.log(Double.parseDouble(stringArray[46]))/Math.log(2);
    }
*/
    selected[index] = true;
    systematicNames[index] = stringArray[3];
    ids[index] = stringArray[4];
    f635medians[index] = Double.parseDouble(stringArray[8]);
    f635means[index] = Double.parseDouble(stringArray[9]);
    b635medians[index] = Double.parseDouble(stringArray[11]);
    b635means[index] = Double.parseDouble(stringArray[12]);
    b635sds[index] = Double.parseDouble(stringArray[13]);
    f532medians[index] = Double.parseDouble(stringArray[17]);
    f532means[index] = Double.parseDouble(stringArray[18]);
    b532medians[index] = Double.parseDouble(stringArray[20]);
    b532means[index] = Double.parseDouble(stringArray[21]);
    b532sds[index] = Double.parseDouble(stringArray[22]);
    logRatios[index] = (dyeSwap ? -logRatio : logRatio);
    flags[index] = Integer.parseInt(stringArray[80]);
    try {
      geneNames[index] = stringArray[82];
    } catch (Exception e) {
      //System.err.println(""+e);
      geneNames[index] = "";
    }
    index++;
  }

  public void printStats (Report report) {
    System.out.println("nom: "+getName()); // only in application mode
    report.appendLine("nombre de genes dans le microarray: "+systematicNames.length);
    report.appendLine("nombre de genes retenus initialement: "+parameterFilter.getInitialSelected());
    if (parameterFilter.getPValueLogRatioApply()) {
      report.appendLine("filtrage pValueLogRatio="+parameterFilter.getPValueLogRatioFilter()+" -> "+parameterFilter.getPValueLogRatioSelected()+" retenus");
    }
    if (parameterFilter.getLogRatioApply()) {
      report.appendLine("filtrage |logRatio| >= "+parameterFilter.getLogRatioFilter()+" -> "+parameterFilter.getLogRatioSelected()+" retenus");
    }
    if (parameterFilter.getSignalNoiseRatioApply()) {
      report.appendLine("filtrage rapport signal/bruit >= "+parameterFilter.getSignalNoiseRatioFilter()+" -> "+parameterFilter.getSignalNoiseRatioSelected()+" retenus");
    }
    report.appendLine("nombre de genes retenus au final: "+getSystematicNamesWithFilter().length);
    report.appendLine("");
  }

  public void printParams(Report report) {
    report.appendLine(""+params);
  }
/*  
  public String toArp (String systematicName) {
    return ""+Lib.doubleToInt(getGMeanSignalBySystematicName(systematicName))+"\t"+
              Lib.doubleToInt(getRMeanSignalBySystematicName(systematicName))+"\t"+
              systematicName+"\t"+
              getGeneNameBySystematicName(systematicName)+"\t\r";
  }
*/

  public void normalize () {
    Lib.normalize(logRatios);
  }
}
