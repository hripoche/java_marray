// Computation.java

package application;

import java.util.*;
import java.io.*;

import util.*;
import marray.*;
import classifier.*;
import classifier.kmeans.*;
import classifier.hierarchical.*;
import classifier.upgma.*;

/**
 * @author H. Ripoche
 */
public class Computation {

  final static public int MARRAY_AGILENT = 0;
  final static public int MARRAY_AFFYMETRIX = 1;
  final static public int MARRAY_GENEPIX = 2;

  final static public int GENE_MODE_INTERSECTION = 0;
  final static public int GENE_MODE_PRESENT_IN_AT_LEAST_N_MARRAY = 1;
  final static public int GENE_MODE_PRESENT_IN_AT_LEAST_N_MARRAY_AND_VARIATION_GREATER_THAN_X = 2;

  public static boolean agilentToArp (String[] args, Report report) {
    String inputFilename, outputFilename;
    LargeTextFile ltfIn, ltfOut;
          
    try {
      inputFilename = args[1];
      outputFilename = args[2];
    } catch (Exception e) {
      System.err.println(""+e);
      Application.usage();
      return false;
    }
    
    Agilent agilent = Agilent.read(inputFilename);
    if (agilent == null) return false; // message should have been provided
    agilent.initialFilter();
    agilent.printStats(report);

    ltfOut = new LargeTextFile();
    try {
      ltfOut.openWrite(outputFilename);
    } catch (IOException e) {
      System.err.println(""+e);
      System.err.println("Can't write: "+outputFilename);
    }

    String[] systematicNames = agilent.getSystematicNamesWithFilter();
    try {
      for (int k=0; k<systematicNames.length; k++) {
	//ltfOut.putLine(agilent.toArp(systematicNames[k]));
        ltfOut.put(agilent.toArp(systematicNames[k])+"\r\n"); // saut de ligne windows
      }
      ltfOut.flush();
      ltfOut.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  public static boolean agilentToAgilent (String[] args, Report report) {
    String inputFilename, outputFilename;
    LargeTextFile ltfIn, ltfOut;
          
    try {
      inputFilename = args[1];
      outputFilename = args[2];
    } catch (Exception e) {
      System.err.println(""+e);
      Application.usage();
      return false;
    }
    
    Agilent agilent = Agilent.read(inputFilename);
    if (agilent == null) return false; // message should have been provided
    agilent.initialFilter();
    agilent.printStats(report);

    ltfOut = new LargeTextFile();
    try {
      ltfOut.openWrite(outputFilename);
    } catch (IOException e) {
      System.err.println(""+e);
      System.err.println("Can't write: "+outputFilename);
    }

    String[] systematicNames = agilent.getSystematicNamesWithFilter();
    try {
      ltfOut.putLine("gBGSubSignal\trBGSubSignal\tlogRatio\tpValueLogRatio\tsystematicName\tgeneName");
      for (int k=0; k<systematicNames.length; k++) {
        ltfOut.putLine(agilent.toAgilent(systematicNames[k]));
      }
      ltfOut.flush();
      ltfOut.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  public static void correlation (String[] args, Report report) {
    Agilent agilent1 = Agilent.read(args[1]);
    Agilent agilent2 = Agilent.read(args[2]);
    agilent1.initialFilter();
    agilent2.initialFilter();
    agilent1.printStats(report);
    agilent2.printStats(report);
    correlationAux(agilent1,agilent2,report);
  }

  static void correlationAux(Agilent agilent1, Agilent agilent2, Report report) {

    String[] systematicNames1 = agilent1.getSystematicNamesWithFilter();
    String[] systematicNames2 = agilent2.getSystematicNamesWithFilter();

    Vector commonGenes = new Vector();
    for (int i=0; i<systematicNames1.length; i++) {
      if (Lib.member(systematicNames1[i],systematicNames2)) {
      	commonGenes.add(systematicNames1[i]);
      }
    }

    report.appendLine("nb genes retenus pour le calcul de correlation: "+commonGenes.size()+"\n");
    
    String systematicName;
    double[] logRatios1 = new double[commonGenes.size()];
    double[] logRatios2 = new double[commonGenes.size()];
    double[] gBGSubSignals1 = new double[commonGenes.size()];
    double[] gBGSubSignals2 = new double[commonGenes.size()];
    double[] rBGSubSignals1 = new double[commonGenes.size()];
    double[] rBGSubSignals2 = new double[commonGenes.size()];
    for (int i=0; i<commonGenes.size(); i++) {
      systematicName = (String) commonGenes.elementAt(i);
      logRatios1[i] = agilent1.getLogRatioBySystematicName(systematicName);
      logRatios2[i] = agilent2.getLogRatioBySystematicName(systematicName);
      gBGSubSignals1[i] = agilent1.getGBGSubSignalBySystematicName(systematicName);
      gBGSubSignals2[i] = agilent2.getGBGSubSignalBySystematicName(systematicName);
      rBGSubSignals1[i] = agilent1.getRBGSubSignalBySystematicName(systematicName);
      rBGSubSignals2[i] = agilent2.getRBGSubSignalBySystematicName(systematicName);
    }

    correlationPrintStats("logRatio:",logRatios1,logRatios2,report);
    correlationPrintStats("gBGSubSignal:",gBGSubSignals1,gBGSubSignals2,report);
    correlationPrintStats("rBGSubSignal:",rBGSubSignals1,rBGSubSignals2,report);

    report.appendLine("Tri par PValueLogRatio croissant:\n");

    String description;
    double dist;

    Gene[] geneArray1 = new Gene[systematicNames1.length]; 
    for (int i=0; i<systematicNames1.length; i++) {
      systematicName = systematicNames1[i];
      description = agilent1.getGeneNameBySystematicName(systematicName);
      dist = agilent1.getPValueLogRatioBySystematicName(systematicName);
      geneArray1[i] = new Gene(systematicName,dist,description);
    }
    Arrays.sort(geneArray1,new GeneComparator(GeneComparator.ASCENDING));

    Gene[] geneArray2 = new Gene[systematicNames2.length]; 
    for (int i=0; i<systematicNames2.length; i++) {
      systematicName = systematicNames2[i];
      description = agilent2.getGeneNameBySystematicName(systematicName);
      dist = agilent2.getPValueLogRatioBySystematicName(systematicName);
      geneArray2[i] = new Gene(systematicName,dist,description);
    }
    Arrays.sort(geneArray2,new GeneComparator(GeneComparator.ASCENDING));

    int len;
    if (systematicNames1.length != systematicNames2.length) {
      report.appendLine("Spearman: les tailles des series sont differentes");
      return;
    } else {
      len = systematicNames1.length;
    }
    int[] x = new int[len];
    String systematicName1;
    boolean spearmanOk = true;
    for (int i=0; i<len; i++) {
      systematicName1 = geneArray1[i].getName();
      x[i] = -1;
      for (int j=0; j<len; j++) {
	if (systematicName1.equals(geneArray2[j].getName())) {
	  x[i] = j;
	}
      }
      if (x[i] == -1) {
        spearmanOk = false;
        break;
      }
    }

    if (spearmanOk) {
      report.appendLine("coeff. de Spearman: "+Lib.spearman(x)+"\n");
    } else {
      report.appendLine("Spearman: series non comparables");
    }

    int limit=0;
    report.appendLine("genes pris en compte:");
    report.appendLine("% \t les n premiers \t pourcentage d'identite");
    for (int i=0; i<10; i++) {
      percentageIdentity(i,len,geneArray1,geneArray2,report);
    }
    report.appendLine("");
  }

  static void percentageIdentity (int i, int len, Gene[] geneArray1, Gene[] geneArray2, Report report) {
    int percent = (i+1)*10;
    int limit = (int)(len/100.0*percent);

    String[] sn1 = new String[limit];
    for (int j=0; j<limit; j++) {
      sn1[j] = geneArray1[j].getName();
    }

    String[] sn2 = new String[limit];
    for (int j=0; j<limit; j++) {
      sn2[j] = geneArray2[j].getName();
    }

    int count=0;
    for (int j=0; j<limit; j++) {
      if (Lib.member(sn1[j],sn2)) {
	count++;
      }
    }

    report.appendLine(percent+"\t"+limit+"\t"+(((double)count)/((double)limit)*100.0));
  }

  static void correlationPrintStats(String header, double[] x1, double[] x2, Report report) {
    report.appendLine(header);
    report.appendLine("");
    report.appendLine("moyenne1: "+Lib.formatDouble(Lib.mean(x1)));
    report.appendLine("moyenne2: "+Lib.formatDouble(Lib.mean(x2)));
    report.appendLine("ecart-type1: "+Lib.formatDouble(Lib.standardDeviation(x1)));
    report.appendLine("ecart-type2: "+Lib.formatDouble(Lib.standardDeviation(x2)));
    report.appendLine("correlation: "+Lib.formatDouble(Lib.correlation(x1,x2))+"\n");
    report.appendLine("");
  }

  public static boolean toStanford (String[] args, int marrayMode, ParameterFilter parameterFilter, Report report) {

    SmallTextFile stf;
    try {
      stf = new SmallTextFile();
      stf.read(args[1]);
    } catch (Exception e) {
      System.err.println(""+e);
      Application.usage();
      return false;
    }
    String[] lines = stf.getLinesWithoutComments();
    MicroArrays microArrays = new MicroArrays();
    MicroArray m;
    for (int i=0; i<lines.length; i++) {
      switch (marrayMode) {
        case MARRAY_AGILENT:
          m = Agilent.read(lines[i]);
          m.initialFilter();
          if (parameterFilter.getPValueLogRatioApply()) {
            ((Agilent)m).filterByPValueLogRatio(parameterFilter.getPValueLogRatioFilter());
          }
          break;
        case MARRAY_AFFYMETRIX:
          m = Affymetrix.read(lines[i]);
          m.initialFilter();
          break;
        case MARRAY_GENEPIX:
          m = Genepix.read(lines[i]);
          m.initialFilter();
          break;
        default: { m = null;
	           System.err.println("Bad marray mode");
                   return false;
	         }
      }
      if (parameterFilter.getLogRatioApply()) {
        m.filterByLogRatio(parameterFilter.getLogRatioFilter());
      }
      if (parameterFilter.getSignalNoiseRatioApply()) {
        m.filterBySignalNoiseRatio(parameterFilter.getSignalNoiseRatioFilter());
      }
      //m.normalize();
      m.printStats(report);
      if (m == null) return false; // message should have been provided
      microArrays.add(m);
    }

    int n=1;
    double x=0; // do not use nFoldChange
    double sd=0; // do not use standardDeviationFilter
    if (parameterFilter.getMinimalNumberOfExperimentsApply()) {
	n = parameterFilter.getMinimalNumberOfExperimentsFilter();
    }
    if (parameterFilter.getMinimalVariationBetweenExperimentsApply()) {
	x = parameterFilter.getMinimalVariationBetweenExperimentsFilter();
    }
    if (parameterFilter.getStandardDeviationApply()) {
	sd = parameterFilter.getStandardDeviationFilter();
    }
    microArrays.computeCommonGenesThresholdWithNFoldChangeAndStandardDeviation(n,x,sd);

    String outputFilename;
    try {
      outputFilename = args[2];
    } catch (Exception e) {
      System.err.println(""+e);
      Application.usage();
      return false;
    }
    LargeTextFile ltfOut = new LargeTextFile();
    try {
      ltfOut.openWrite(outputFilename);
    } catch (IOException e) {
      System.err.println(""+e);
      System.err.println("Can't write: "+outputFilename);
      return false;
    }

    String gene;
    try {
      report.appendLine("\ngenes communs="+microArrays.getCommonGenes().length);
      ltfOut.putLine(microArrays.toStanfordHeader());
      for (int k=0; k<microArrays.getCommonGenes().length; k++) {
      	gene = (String) microArrays.getCommonGenes()[k];
        ltfOut.putLine(microArrays.toStanford(gene));
      }
      ltfOut.flush();
      ltfOut.close();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public static boolean stanfordToLists (String[] args, String directory, String prefix, String suffix, SmallTextFile stf, Vector v, Report report) {
    Stanford stanford = null;
    int aDistance;
    try {
      stanford = Stanford.read(args[1],report);
      aDistance = Integer.parseInt(args[2]);
    } catch (Exception e) {
      report.appendLine(""+e);
      Application.usage();
      return false;
    }

    if (stanford == null) {
      return false;
    }

    return stanford.toGeneLists(aDistance,directory,prefix,suffix,stf,v,report);
  }

  public static boolean kmeans (String[] args, Report report) {

    String inputFilename;
    int nbClusters, maxIter;

    if (args.length != 4) {
      Application.usage();
      return false;
    }

    try {
      inputFilename = args[1];
      nbClusters = Integer.parseInt(args[2]);
      maxIter = Integer.parseInt(args[3]);
    } catch (Exception e) {
      report.appendLine(""+e);
      Application.usage();
      return false;
    }

    System.out.println("inputFile="+inputFilename+"\n\n");
    report.appendLine("nbClusters="+nbClusters+"<br>\n");
    report.appendLine("maxIter="+maxIter+"<br>\n");

    Stanford stanford = null;
    stanford = Stanford.read(inputFilename,report);

    if (stanford == null) {
      return false;
    }

    Item[] items = stanford.getItems();

    KMeans kMeans = new KMeans(items,nbClusters,maxIter);
    kMeans.classify();
    report.appendLine("iter="+kMeans.getIter()+"<br>\n");
    report.appendLine("clusters=<br>\n"+kMeans.getClusters().asString());
    return true;
  }

  public static boolean hierarchical (String[] args, Report report) {

    String inputFilename;
    String mode;
    String distance;
    String image;

    if (args.length != 5) {
      Application.usage();
      return false;
    }

    try {
      inputFilename = args[1];
      mode = args[2];
      distance = args[3];
      image = args[4];
    } catch (Exception e) {
      report.appendLine(""+e);
      Application.usage();
      return false;
    }

    System.out.println("inputFile="+inputFilename);

    Stanford stanford = null;
    stanford = Stanford.read(inputFilename,report);

    if (stanford == null) {
      return false;
    }

    classifier.hierarchical.DataMatrix dm = new classifier.hierarchical.DataMatrix(stanford);

    //Hierarchical hierarchical = new Hierarchical(SampleDataMatrix.getMatrix(),SampleDataMatrix.getNames());
    Hierarchical hierarchical = new Hierarchical(dm);
    hierarchical.classify();
    hierarchical.display(new File(image));

    return true;
  }

  public static boolean upgma (String[] args, Report report) {

    String inputFilename;
    String mode;
    String distance;
    String image;

    if (args.length != 5) {
      Application.usage();
      return false;
    }

    try {
      inputFilename = args[1];
      mode = args[2];
      distance = args[3];
      image = args[4];
    } catch (Exception e) {
      report.appendLine(""+e);
      Application.usage();
      return false;
    }

    System.out.println("inputFile="+inputFilename);

    Stanford stanford = null;
    stanford = Stanford.read(inputFilename,report);

    if (stanford == null) {
      return false;
    }

    classifier.upgma.DataMatrix dm = new classifier.upgma.DataMatrix(stanford);
    UPGMA upclu = new UPGMA(dm);
    upclu.display(new File(image));

    return true;
  }
}
