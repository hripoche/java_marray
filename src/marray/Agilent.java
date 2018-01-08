// Agilent.java

package marray;

import java.io.*;
import java.util.*;

import util.*;

/**
 * @author H. Ripoche
 */
public class Agilent extends MicroArray {

  static final private int MODE_UNKNOWN=-1;
  static final private int MODE_CDNA=0;
  static final private int MODE_OLIGO=1;

  private String name;
  private Hashtable params, comp;

  private ParameterFilter parameterFilter = new ParameterFilter();
  private boolean[] selected;
  private int mode = MODE_UNKNOWN;

  private int[] featureNums;
  private int[] controlTypes;
  private String[] geneNames; // description
  private String[] systematicNames; // GenBankID.FeatureNum
  private double[] logRatios;
  private double[] logRatioErrors;
  private double[] pValueLogRatios;
  private double[] gMeanSignals;
  private double[] rMeanSignals;
  private double[] gMedianSignals;
  private double[] rMedianSignals;
  private double[] gBGMeanSignals;
  private double[] rBGMeanSignals;
  private double[] gBGMedianSignals;
  private double[] rBGMedianSignals;
  private double[] gBGPixSDevs;
  private double[] rBGPixSDevs;
  private int[] gIsSaturateds; // boolean
  private int[] rIsSaturateds; // boolean
  private int[] gIsFeatNonUnifOLs; // boolean
  private int[] rIsFeatNonUnifOLs; // boolean
  private double[] gBGSubSignals;
  private double[] rBGSubSignals;
  private int[] gIsWellAboveBGs; // boolean
  private int[] rIsWellAboveBGs; // boolean
  private int index;
  
  public Agilent (int length) {
    selected = new boolean[length];
    featureNums = new int[length];
    controlTypes = new int[length];
    geneNames = new String[length];
    systematicNames = new String[length];
    logRatios = new double[length];
    logRatioErrors = new double[length];
    pValueLogRatios = new double[length];
    gMeanSignals = new double[length];
    rMeanSignals = new double[length];
    gMedianSignals = new double[length];
    rMedianSignals = new double[length];
    gBGMeanSignals = new double[length];
    rBGMeanSignals = new double[length];
    gBGMedianSignals = new double[length];
    rBGMedianSignals = new double[length];
    gBGPixSDevs = new double[length];
    rBGPixSDevs = new double[length];
    gIsSaturateds = new int[length];
    rIsSaturateds = new int[length];
    gIsFeatNonUnifOLs = new int[length];
    rIsFeatNonUnifOLs = new int[length];
    gBGSubSignals = new double[length];
    rBGSubSignals = new double[length];
    gIsWellAboveBGs = new int[length];
    rIsWellAboveBGs = new int[length];
    index = 0;
  }
  /*
  class Item {
    String geneName;
    double logRatio;
    double logRatioError;
    double gMeanSignal;
    double rMeanSignal;
    
    public Item (String geneName,
                 double logRatio,
                 double logRatioError,
                 double gMeanSignal,
                 double rMeanSignal) {
      this.geneName = geneName;
      this.logRatio = logRatio;
      this.logRatioError = logRatioError;
      this.gMeanSignal = gMeanSignal;
      this.rMeanSignal = rMeanSignal;
    }
    
    public double getLogRatio () {
      return logRatio;
    }
  }
  
  public void compile () {
    comp = new Hashtable(10000);
    for (int i=0; i<getLength(); i++) {
      comp.put(systematicNames[i],new Item(geneNames[i],
                                           logRatios[i],
                                           logRatioErrors[i],
                                           gMeanSignals[i],
                                           rMeanSignals[i]));
    }
  }
  */
  public static Agilent read (String inputLine) {

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
    String[] stringArray;
    Agilent agilent = null;
    int i=0, len=0;
    try {
      while ((line = ltfIn.getLine()) != null) {
        if (!line.trim().equals("")) len++;
      }
      ltfIn.close();
      ltfIn.openRead(inputFilename);
      agilent = new Agilent(len-10);
      agilent.setName(logicalName);
      while ((line = ltfIn.getLine()) != null) {
      	stringArray = Lib.asStringArrayWithNulls(line,"\t");
      	if (i == 9) {
      	  agilent.setParams(null);
      	  if (!agilent.checkHeaderOK(stringArray)) {
      	    System.err.println("\nBad format file - check column names\n");
       	    for (int k=0; k<stringArray.length; k++) {
      	      System.err.println(""+k+"\t"+stringArray[k]);
      	    }
      	    return null;
      	  }
      	}
      	if (i > 9 && line.startsWith("DATA")) {
      	  agilent.add(stringArray,dyeSwap);
      	}
      	i++;
      }
      ltfIn.close();
    } catch (IOException e) {
      System.err.println(""+e);
    }
    return agilent;
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
  
  public int[] getFeatureNums() {
    return featureNums;
  }
  
  public int[] getControlTypes() {
    return controlTypes;
  }
  
  public String[] getGeneNames() {
    return geneNames;
  }

  public String[] getSystematicNames() {
    return systematicNames;
  }

  public double[] getLogRatios() {
    return logRatios;
  }

  public double[] getLogRatioErrors() {
    return logRatioErrors;
  }
  
  public double[] getPValueLogRatios() {
    return pValueLogRatios;
  }

  public double[] getGMeanSignals() {
    return gMeanSignals;
  }

  public double[] getRMeanSignals() {
    return rMeanSignals;
  }

  public double[] getGMedianSignals() {
    return gMedianSignals;
  }

  public double[] getRMedianSignals() {
    return rMedianSignals;
  }
 
  public double[] getGBGMeanSignals() {
    return gBGMeanSignals;
  }
  
  public double[] getRBGMeanSignals() {
    return rBGMeanSignals;
  }
  
  public double[] getGBGMedianSignals() {
    return gBGMedianSignals;
  }
  
  public double[] getRBGMedianSignals() {
    return rBGMedianSignals;
  }
  
  public double[] getGBGPixSDevs() {
    return gBGPixSDevs;
  }
  
  public double[] getRBGPixSDevs() {
    return rBGPixSDevs;
  }
 
  public void initialFilter () {
    int count=0;
    String systematicName;
    boolean featureUniformity,
            wellAboveBackground,
            featureSaturation;
    for (int i=0; i<getLength(); i++) {
      int controlType = controlTypes[i];
      systematicName = systematicNames[i];
      featureUniformity = gIsFeatNonUnifOLs[i] == 0 || rIsFeatNonUnifOLs[i] == 0;
      wellAboveBackground = gIsWellAboveBGs[i] == 1 && rIsWellAboveBGs[i] == 1;
      featureSaturation = gIsSaturateds[i] == 0 && rIsSaturateds[i] == 0;
      if (controlType == 0 &&
          !systematicName.startsWith("0.") &&
          featureUniformity &&
          wellAboveBackground &&
          featureSaturation) {
      	selected[i] = true;
        count++;
      } else {
        selected[i] = false;
      }
    }
    parameterFilter.setInitialSelected(count);
  }

  public void filterByPValueLogRatio (double pValueLogRatio) {
    int count=0;
    for (int i=0; i<this.getLength(); i++) {
      if (selected[i] && pValueLogRatios[i] <= pValueLogRatio) {
        count++;
      } else {
        selected[i] = false;
      }
    }
    parameterFilter.setPValueLogRatioFilter(pValueLogRatio);
    parameterFilter.setPValueLogRatioSelected(count);
    parameterFilter.setPValueLogRatioApply(true);
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
          (((rMeanSignals[i] - rBGMeanSignals[i]) > ratio * rBGPixSDevs[i]) ||
           ((gMeanSignals[i] - gBGMeanSignals[i]) > ratio * gBGPixSDevs[i]))) {
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

  public double getPValueLogRatioBySystematicName (String systematicName) {
    for (int i=0; i<index; i++) {
      if (systematicName.equals(systematicNames[i])) {
      	return pValueLogRatios[i];
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

  public double getRMeanSignalBySystematicName (String systematicName) {
    for (int i=0; i<index; i++) {
      if (systematicName.equals(systematicNames[i])) {
      	return rMeanSignals[i];
      }
    }
    return Double.NaN;
  }

  public double getGMeanSignalBySystematicName (String systematicName) {
    for (int i=0; i<index; i++) {
      if (systematicName.equals(systematicNames[i])) {
      	return gMeanSignals[i];
      }
    }
    return Double.NaN;
  }

  public double getGBGSubSignalBySystematicName (String systematicName) {
    for (int i=0; i<index; i++) {
      if (systematicName.equals(systematicNames[i])) {
      	return gBGSubSignals[i];
      }
    }
    return Double.NaN;
  }

  public double getRBGSubSignalBySystematicName (String systematicName) {
    for (int i=0; i<index; i++) {
      if (systematicName.equals(systematicNames[i])) {
      	return rBGSubSignals[i];
      }
    }
    return Double.NaN;
  }

  public boolean checkHeaderOK (String[] stringArray) {
    boolean result = true;

    String[] headerNamesCDNA = {"FeatureNum",
                            "ControlType",
                            "GeneName",
                            "SystematicName", // only for CDNA
                            "LogRatio",
                            "LogRatioError",
                            "PValueLogRatio",
                            "gMeanSignal",
                            "rMeanSignal",
                            "gMedianSignal",
                            "rMedianSignal",
                            "gBGMeanSignal",
                            "rBGMeanSignal",
                            "gBGMedianSignal",
                            "rBGMedianSignal",
                            "gBGPixSDev",
                            "rBGPixSDev",
                            "gIsSaturated",
                            "rIsSaturated",
                            "gIsFeatNonUnifOL",
                            "rIsFeatNonUnifOL",
                            "gBGSubSignal",
                            "rBGSubSignal",
                            "gIsWellAboveBG",
                            "rIsWellAboveBG"};
    int[] headerIndexesCDNA = {1,14,16,17,21,22,23,38,39,40,41,46,47,48,49,50,51,54,55,58,59,67,68,78,79};

    String[] headerNamesOLIGO = {"FeatureNum",
                            "ControlType",
                            "GeneName",
                            "LogRatio",
                            "LogRatioError",
                            "PValueLogRatio",
                            "gMeanSignal",
                            "rMeanSignal",
                            "gMedianSignal",
                            "rMedianSignal",
                            "gBGMeanSignal",
                            "rBGMeanSignal",
                            "gBGMedianSignal",
                            "rBGMedianSignal",
                            "gBGPixSDev",
                            "rBGPixSDev",
                            "gIsSaturated",
                            "rIsSaturated",
                            "gIsFeatNonUnifOL",
                            "rIsFeatNonUnifOL",
                            "gBGSubSignal",
                            "rBGSubSignal",
                            "gIsWellAboveBG",
                            "rIsWellAboveBG"};
    // puce IGR22K
/*
    int[] headerIndexesOLIGO = {1,7,9,12,13,14,29,30,31,32,37,38,39,40,41,42,45,46,49,50,58,59,69,70};
*/
    // puce agilent22K
    int[] headerIndexesOLIGO = {1,8,10,15,16,17,32,33,34,35,40,41,42,43,44,45,48,49,52,53,61,62,72,73};

    if (headerNamesCDNA.length != headerIndexesCDNA.length) {
      System.err.println("CDNA: headerNames and headerIndexes do not have same length");
      return false;
    }
    
    if (headerNamesOLIGO.length != headerIndexesOLIGO.length) {
      System.err.println("OLIGO: headerNames and headerIndexes do not have same length");
      return false;
    }
    
    if (stringArray.length < headerIndexesCDNA[headerIndexesCDNA.length-1] &&
        stringArray.length < headerIndexesOLIGO[headerIndexesOLIGO.length-1]) {
      return false;
    }

    String headerName;
    int headerIndex;

    for (int i=0; i<headerIndexesCDNA.length; i++) {
      headerIndex = headerIndexesCDNA[i];
      headerName = headerNamesCDNA[i];
      result = result && stringArray[headerIndex].equals(headerName);
    }

    if (result) {
      mode = MODE_CDNA;
      //System.err.println("MODE_CDNA");
    } else { // try MODE_OLIGO
      result = true;
      for (int i=0; i<headerIndexesOLIGO.length; i++) {
        headerIndex = headerIndexesOLIGO[i];
        headerName = headerNamesOLIGO[i];
        result = result && stringArray[headerIndex].equals(headerName);
      }
      if (result) {
        mode = MODE_OLIGO;
        //System.err.println("MODE_OLIGO");
      }
    }

    return result;
  }
    
  public void add (String[] stringArray, boolean dyeSwap) {
    if (mode == MODE_CDNA) {
      addCDNA(stringArray,dyeSwap);
    } else if (mode == MODE_OLIGO) {
      addOLIGO(stringArray,dyeSwap);
    }
  }

  public void addCDNA (String[] stringArray, boolean dyeSwap) {
    
    int featureNum = Integer.parseInt(stringArray[1]);
    int controlType = Integer.parseInt(stringArray[14]);
    double pValueLogRatio = Double.parseDouble(stringArray[23]);
    String systematicName = stringArray[17];
    double logRatio = Double.parseDouble(stringArray[21]);
    double ratio = Math.pow(10,logRatio);
    logRatio = Math.log(ratio)/Math.log(2);
    logRatio = (dyeSwap ? -logRatio : logRatio);

    selected[index] = true;
    featureNums[index] = featureNum;
    controlTypes[index] = controlType;
    geneNames[index] = stringArray[16];
    systematicNames[index] = systematicName+"."+featureNum;
    logRatios[index] = logRatio;
    logRatioErrors[index] = Double.parseDouble(stringArray[22]);
    pValueLogRatios[index] = pValueLogRatio;
    gMeanSignals[index] = Double.parseDouble(stringArray[38]);
    rMeanSignals[index] = Double.parseDouble(stringArray[39]);
    gMedianSignals[index] = Double.parseDouble(stringArray[40]);
    rMedianSignals[index] = Double.parseDouble(stringArray[41]);
    gBGMeanSignals[index] = Double.parseDouble(stringArray[46]);
    rBGMeanSignals[index] = Double.parseDouble(stringArray[47]);
    gBGMedianSignals[index] = Double.parseDouble(stringArray[48]);
    rBGMedianSignals[index] = Double.parseDouble(stringArray[49]);
    gBGPixSDevs[index] = Double.parseDouble(stringArray[50]);
    rBGPixSDevs[index] = Double.parseDouble(stringArray[51]);
    gIsSaturateds[index] = Integer.parseInt(stringArray[54]);
    rIsSaturateds[index] = Integer.parseInt(stringArray[55]);
    gIsFeatNonUnifOLs[index] = Integer.parseInt(stringArray[58]);
    rIsFeatNonUnifOLs[index] = Integer.parseInt(stringArray[59]);
    gBGSubSignals[index] = Double.parseDouble(stringArray[67]);
    rBGSubSignals[index] = Double.parseDouble(stringArray[68]);
    gIsWellAboveBGs[index] = Integer.parseInt(stringArray[78]);
    rIsWellAboveBGs[index] = Integer.parseInt(stringArray[79]);
    index++;
  }

  // puce IGR22K
/*
  public void addOLIGO (String[] stringArray, boolean dyeSwap) {
    
    int featureNum = Integer.parseInt(stringArray[1]);
    int controlType = Integer.parseInt(stringArray[7]);
    double pValueLogRatio = Double.parseDouble(stringArray[14]);
    String systematicName = stringArray[8]+"."+featureNum;
    double logRatio = Double.parseDouble(stringArray[12]);
    double ratio = Math.pow(10,logRatio);
    logRatio = Math.log(ratio)/Math.log(2);
    logRatio = (dyeSwap ? -logRatio : logRatio);

    selected[index] = true;
    featureNums[index] = featureNum;
    controlTypes[index] = controlType;
    geneNames[index] = ""+featureNum; //stringArray[9];
    systematicNames[index] = systematicName;
    logRatios[index] = logRatio;
    logRatioErrors[index] = Double.parseDouble(stringArray[13]);
    pValueLogRatios[index] = pValueLogRatio;
    gMeanSignals[index] = Double.parseDouble(stringArray[29]);
    rMeanSignals[index] = Double.parseDouble(stringArray[30]);
    gMedianSignals[index] = Double.parseDouble(stringArray[31]);
    rMedianSignals[index] = Double.parseDouble(stringArray[32]);
    gBGMeanSignals[index] = Double.parseDouble(stringArray[37]);
    rBGMeanSignals[index] = Double.parseDouble(stringArray[38]);
    gBGMedianSignals[index] = Double.parseDouble(stringArray[39]);
    rBGMedianSignals[index] = Double.parseDouble(stringArray[40]);
    gBGPixSDevs[index] = Double.parseDouble(stringArray[41]);
    rBGPixSDevs[index] = Double.parseDouble(stringArray[42]);
    gIsSaturateds[index] = Integer.parseInt(stringArray[45]);
    rIsSaturateds[index] = Integer.parseInt(stringArray[46]);
    gIsFeatNonUnifOLs[index] = Integer.parseInt(stringArray[49]);
    rIsFeatNonUnifOLs[index] = Integer.parseInt(stringArray[50]);
    
    gBGSubSignals[index] = Double.parseDouble(stringArray[58]);
    rBGSubSignals[index] = Double.parseDouble(stringArray[59]);
    //gBGSubSignals[index] = Double.parseDouble(stringArray[77]);
    //rBGSubSignals[index] = Double.parseDouble(stringArray[78]);
    
    gIsWellAboveBGs[index] = Integer.parseInt(stringArray[69]);
    rIsWellAboveBGs[index] = Integer.parseInt(stringArray[70]);
    index++;
  }
*/

  // puce agilent22K
  public void addOLIGO (String[] stringArray, boolean dyeSwap) {
    
    int featureNum = Integer.parseInt(stringArray[1]);
    int controlType = Integer.parseInt(stringArray[8]);
    double pValueLogRatio = Double.parseDouble(stringArray[17]);
    String systematicName = stringArray[11]+"."+featureNum;
    double logRatio = Double.parseDouble(stringArray[15]);
    double ratio = Math.pow(10,logRatio);
    logRatio = Math.log(ratio)/Math.log(2);
    logRatio = (dyeSwap ? -logRatio : logRatio);

    selected[index] = true;
    featureNums[index] = featureNum;
    controlTypes[index] = controlType;
    geneNames[index] = ""+featureNum;
    systematicNames[index] = systematicName;
    logRatios[index] = logRatio;
    logRatioErrors[index] = Double.parseDouble(stringArray[16]);
    pValueLogRatios[index] = pValueLogRatio;
    gMeanSignals[index] = Double.parseDouble(stringArray[32]);
    rMeanSignals[index] = Double.parseDouble(stringArray[33]);
    gMedianSignals[index] = Double.parseDouble(stringArray[34]);
    rMedianSignals[index] = Double.parseDouble(stringArray[35]);
    gBGMeanSignals[index] = Double.parseDouble(stringArray[40]);
    rBGMeanSignals[index] = Double.parseDouble(stringArray[41]);
    gBGMedianSignals[index] = Double.parseDouble(stringArray[42]);
    rBGMedianSignals[index] = Double.parseDouble(stringArray[43]);
    gBGPixSDevs[index] = Double.parseDouble(stringArray[44]);
    rBGPixSDevs[index] = Double.parseDouble(stringArray[45]);
    gIsSaturateds[index] = Integer.parseInt(stringArray[48]);
    rIsSaturateds[index] = Integer.parseInt(stringArray[49]);
    gIsFeatNonUnifOLs[index] = Integer.parseInt(stringArray[52]);
    rIsFeatNonUnifOLs[index] = Integer.parseInt(stringArray[53]);
    
    gBGSubSignals[index] = Double.parseDouble(stringArray[61]);
    rBGSubSignals[index] = Double.parseDouble(stringArray[62]);
    
    gIsWellAboveBGs[index] = Integer.parseInt(stringArray[72]);
    rIsWellAboveBGs[index] = Integer.parseInt(stringArray[73]);
    index++;
  }

  public static Agilent median (MicroArrays microArrays) {
    int length;
    Agilent result;
    Agilent agilent0;
    try {
      agilent0 = (Agilent) microArrays.elementAt(0);
      length = agilent0.getLength();
    } catch (Exception e) {
      System.err.println(""+e);
      return null;
    }
    result = new Agilent(length);
    double sum;
    for (int i=0; i<length; i++) {
      result.geneNames[i] = agilent0.geneNames[i];
      result.systematicNames[i] = agilent0.systematicNames[i];
      result.index = agilent0.index;
      
      sum=0;
      for (int j=0; j<microArrays.size(); j++) {
      	sum += ((Agilent) microArrays.elementAt(j)).logRatios[i];
      }
      result.logRatios[i] = sum / microArrays.size();

      sum=0;
      for (int j=0; j<microArrays.size(); j++) {
      	sum += ((Agilent) microArrays.elementAt(j)).logRatioErrors[i];
      }
      result.logRatioErrors[i] = sum / microArrays.size();

      sum=0;
      for (int j=0; j<microArrays.size(); j++) {
      	sum += ((Agilent) microArrays.elementAt(j)).pValueLogRatios[i];
      }
      result.pValueLogRatios[i] = sum / microArrays.size();

      sum=0;
      for (int j=0; j<microArrays.size(); j++) {
      	sum += ((Agilent) microArrays.elementAt(j)).gMeanSignals[i];
      }
      result.gMeanSignals[i] = sum / microArrays.size();

      sum=0;
      for (int j=0; j<microArrays.size(); j++) {
      	sum += ((Agilent) microArrays.elementAt(j)).rMeanSignals[i];
      }
      result.rMeanSignals[i] = sum / microArrays.size();

      sum=0;
      for (int j=0; j<microArrays.size(); j++) {
      	sum += ((Agilent) microArrays.elementAt(j)).gMedianSignals[i];
      }
      result.gMedianSignals[i] = sum / microArrays.size();

      sum=0;
      for (int j=0; j<microArrays.size(); j++) {
      	sum += ((Agilent) microArrays.elementAt(j)).rMedianSignals[i];
      }
      result.rMedianSignals[i] = sum / microArrays.size();

      sum=0;
      for (int j=0; j<microArrays.size(); j++) {
      	sum += ((Agilent) microArrays.elementAt(j)).gBGMeanSignals[i];
      }
      result.gBGMeanSignals[i] = sum / microArrays.size();

      sum=0;
      for (int j=0; j<microArrays.size(); j++) {
      	sum += ((Agilent) microArrays.elementAt(j)).rBGMeanSignals[i];
      }
      result.rBGMeanSignals[i] = sum / microArrays.size();

      sum=0;
      for (int j=0; j<microArrays.size(); j++) {
      	sum += ((Agilent) microArrays.elementAt(j)).gBGMedianSignals[i];
      }
      result.gBGMedianSignals[i] = sum / microArrays.size();

      sum=0;
      for (int j=0; j<microArrays.size(); j++) {
      	sum += ((Agilent) microArrays.elementAt(j)).rBGMedianSignals[i];
      }
      result.rBGMedianSignals[i] = sum / microArrays.size();

      sum=0;
      for (int j=0; j<microArrays.size(); j++) {
      	sum += ((Agilent) microArrays.elementAt(j)).gBGPixSDevs[i];
      }
      result.gBGPixSDevs[i] = sum / microArrays.size();

      sum=0;
      for (int j=0; j<microArrays.size(); j++) {
      	sum += ((Agilent) microArrays.elementAt(j)).rBGPixSDevs[i];
      }
      result.rBGPixSDevs[i] = sum / microArrays.size();
    }
    return result;
  }
  
  public void printStats (Report report) {
    int countP10=0, countP100=0, countP1000=0, countRSNR=0, countGSNR=0;
    String name;
    for (int i=0; i<systematicNames.length; i++) {
      name = systematicNames[i];
      double pValueLogRatio = this.getPValueLogRatioBySystematicName(name);
      if (pValueLogRatio < 0.1) {
    	countP10++;
      }
      if (pValueLogRatio < 0.01) {
    	countP100++;
      }
      if (pValueLogRatio < 0.001) {
    	countP1000++;
      }
    }

    report.appendLine("nom: "+getName());
    report.appendLine("nombre de genes dans le microarray: "+systematicNames.length);
    report.appendLine("nombre de genes retenus initialement: "+parameterFilter.getInitialSelected());
    report.appendLine("nombre de genes tels que pValueLogRatio < 0.1 : "+countP10);
    report.appendLine("nombre de genes tels que pValueLogRatio < 0.01 : "+countP100);
    report.appendLine("nombre de genes tels que pValueLogRatio < 0.001 : "+countP1000);
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

  public String toArp (String systematicName) {
    return ""+Lib.doubleToInt(getGBGSubSignalBySystematicName(systematicName))+"\t"+
              Lib.doubleToInt(getRBGSubSignalBySystematicName(systematicName))+"\t"+
              systematicName+"\t"+
              getGeneNameBySystematicName(systematicName);
  }

  public String toAgilent (String systematicName) {
    return ""+getGBGSubSignalBySystematicName(systematicName)+"\t"+
              getRBGSubSignalBySystematicName(systematicName)+"\t"+
              getLogRatioBySystematicName(systematicName)+"\t"+
              getPValueLogRatioBySystematicName(systematicName)+"\t"+
              systematicName+"\t"+
              getGeneNameBySystematicName(systematicName)+"\t";
  }

  public void normalize () {
    Lib.normalize(logRatios);
  }
}
