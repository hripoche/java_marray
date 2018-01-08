// Affymetrix.java

package marray;

import java.io.*;
import java.util.*;

import util.*;

/**
 * @author H. Ripoche
 */
public class Affymetrix extends MicroArray {
	
  private String name;

  private ParameterFilter parameterFilter = new ParameterFilter();
  private boolean[] selected;

  private String[] systematicNames; // GenBank ID
  private double[] avgDiffs;
  private char[] absCalls;
  private String[] geneNames; // description
  private int index;
  
  public static String parse (String s) {
    int index = s.indexOf("/GEN=");
    if (index != -1) {
      s = s.substring(index+5);
    }
    /*
    index = s.indexOf(' ');
    if (index != -1) {
      s = s.substring(0,index);
    }
    */
    return s;
  }
  
  public Affymetrix (int length) {
    selected = new boolean[length];
    systematicNames = new String[length];
    avgDiffs = new double[length];
    absCalls = new char[length];
    geneNames = new String[length];
    index = 0;
  }

  public static Affymetrix read (String inputLine) {

    String inputFilename, logicalName=null;
    String[] str = Lib.asStringArray(inputLine," \t#");
    if (str.length == 1) { // soit le fichier seul (nom physique)
      inputFilename = str[0];
    } else if (str.length == 3) { // soit marqueur_dye_swap | nom_physique | nom_logique
      String item = str[0];
      if (!item.equals("0")) { // dummy dye swap item
      	System.err.println("Unknown line beginning: should be: 0");
      	return null;
      }
      inputFilename = str[1];
      logicalName = str[2];
    } else {
      System.err.println("Bad file naming");
      return null;
    }

    LargeTextFile ltfIn = new LargeTextFile();
    try {
      ltfIn.openRead(inputFilename);
    } catch (IOException e) {
      System.err.println("Can't read: "+inputFilename);
      return null;
    }

    String[] stringArray;
    String line=null;
    Affymetrix affymetrix = null;
    int i=0, len=0;
    try {
      while ((line = ltfIn.getLine()) != null) {
        if (!line.trim().equals("")) len++;
      }
      ltfIn.close();
      ltfIn.openRead(inputFilename);
      affymetrix = new Affymetrix(len-2); // len-4
      affymetrix.setName(logicalName);
      while ((line = ltfIn.getLine()) != null) {
      	stringArray = Lib.asStringArrayWithNulls(line,"\t");
      	if (i == 3) {
      	  if (!Affymetrix.checkHeaderOK(stringArray)) {
      	    System.err.println("\nBad format file - check column names\n");
       	    for (int k=0; k<stringArray.length; k++) {
      	      System.err.println(""+k+"\t"+stringArray[k]);
      	    }
      	    return null;
      	  }
      	}
      	if (i > 3) {
      	  affymetrix.add(stringArray);
      	}
      	i++;
      }
      ltfIn.close();
    } catch (IOException e) {
      System.err.println(""+e);
    }
    return affymetrix;
  }
    
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public int getLength() {
    return index;
  }

  public String[] getGeneNames() {
    return geneNames;
  }

  public String[] getSystematicNames() {
    return systematicNames;
  }

  /**
   * Affymetrix: on prend avgDiff € la place du logRatio
   */
  public double[] getLogRatios() {
    return avgDiffs;
  }
 
  /**
   * Affymetrix: on prend absCall == 'P'
   */
  public void initialFilter () {
    int count=0;
    for (int i=0; i<this.getLength(); i++) {
      if (absCalls[i] == 'P') {
      	selected[i] = true;
        count++;
      } else {
        selected[i] = false;
      }
    }
    parameterFilter.setInitialSelected(count);
  }

  public void filterByLogRatio (double logRatio) {
  }

  public void filterBySignalNoiseRatio (double ratio) {
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

  /**
   * Affymetrix: we consider avgDiff instead of logRatio
   * ---
   * Seeks avgDiff according to systematicName
   * @return avgDiff if gene was selected or Double.NaN
   * if gene was not selected
   */
  public double getLogRatioBySystematicName (String systematicName) {
    for (int i=0; i<index; i++) {
      if (systematicName.equals(systematicNames[i])) {
      	//if (selected[i]) {
          return avgDiffs[i];
        //} else {
        //  return Double.NaN;
        //}
      }
    }
    return Double.NaN;
  }

  public String getGeneNameBySystematicName (String systematicName) {
    for (int i=0; i<index; i++) {
      if (systematicName.equals(systematicNames[i])) {
      	return geneNames[i];
      }
    }
    return null;
  }
    
  public static boolean checkHeaderOK (String[] stringArray) {
    boolean result = true;
    String[] headerNames = {"ProbeSet Name",
                            "Avg Diff",
                            "Abs Call",
                            "Descriptions"};
    int[] headerIndexes = {0,9,10,11};
    String headerName;
    int headerIndex;
    
    if (headerNames.length != headerIndexes.length) {
      System.err.println("headerNames and headerIndexes do not have same length");
      return false;
    }
    
    for (int i=0; i<headerIndexes.length; i++) {
      headerIndex = headerIndexes[i];
      headerName = headerNames[i];
      result = result && stringArray[headerIndex].equals(headerName);
      if (!result) { System.err.println(""+headerIndex); return false;}	
    }
    return result;
  }

  public void add (String[] stringArray) {
    char absCall = stringArray[10].charAt(0);
    systematicNames[index] = stringArray[0];
    avgDiffs[index] = Double.parseDouble(stringArray[9]);
    absCalls[index] = absCall;
    geneNames[index] = stringArray[11];

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
  
  public void normalize () {
    Lib.normalize(avgDiffs);
  }
}
