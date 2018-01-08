// Lib.java

package util;

import java.text.*;
import java.util.*;
import java.io.*;

/**
 * @author H. Ripoche
 */
public final class Lib {

  // Prevent instantiation
  private Lib() {}

  public final static int DISTANCE_EUCLIDIAN = 0;
  public final static int DISTANCE_CORRELATION = 1;
  public final static int DISTANCE_CORRELATION_GOLUB = 2;

  public final static String getNormalDate () {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    return formatter.format(new Date());
  }

  public final static String getNormalTime () {
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    return formatter.format(new Date());
  }

  public final static String formatDouble (double d) {
    String s="";
    Locale locale = Locale.US;
    NumberFormat nf = NumberFormat.getPercentInstance(locale);// 2 chiffres après la virgule
    //s = ""+nf.format(d);
    try {
      s = ""+nf.parse(nf.format(d));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return s;
  }

  public final static String[] asStringArray (String s, String separators) {
    StringTokenizer st = new StringTokenizer(s,separators);
    String[] result = new String[st.countTokens()];
    for (int i=0; i<result.length; i++) {
        result[i] = st.nextToken();
    }
    return result;
  }

  public final static String firstToken (String s, String separators) {
    StringTokenizer st = new StringTokenizer(s,separators);
    return st.nextToken();
  }

  public final static String lastToken (String s, String separators) {
    StringTokenizer st = new StringTokenizer(s,separators);
    String[] result = new String[st.countTokens()];
    for (int i=0; i<result.length; i++) {
        result[i] = st.nextToken();
    }
    return result[result.length-1];
  }

  public final static double[] asDoubleArray (String s, String separators) {
    StringTokenizer st = new StringTokenizer(s,separators);
    double[] result = new double[st.countTokens()];
    for (int i=0; i<result.length; i++) {
        result[i] = Double.parseDouble(st.nextToken());
    }
    return result;
  }

  /**
   * @param s La chaine € parser
   * @param separators Les s‰parateurs
   */
  public final static String[] asStringArrayWithNulls (String s, String separators) {
    String[] stringArray;
    Vector stringVector;
    StringBuffer sb;
    char separator;

    if (separators == null) {
      return null;
    }

    stringVector = new Vector();
    sb = new StringBuffer();
    boolean inWord = false;
    char c;
    int len = s.length();
    for (int i=0; i<len; i++) {
      c = s.charAt(i);
      if (separators.indexOf(c) != -1) { // c est un s‰parateur
        if (inWord) {
           stringVector.addElement(sb.toString());
           inWord = false;
        } else {
           stringVector.addElement(null);
        }
      } else if (i == len-1) { // traitement du dernier caractˆre
        if (inWord) {
          sb.append(c);
        } else {
          sb = new StringBuffer();
          sb.append(c);
        }
        stringVector.addElement(sb.toString());
      } else { // cas g‰n‰ral
        if (inWord) {
          sb.append(c);
        } else {
          sb = new StringBuffer();
          sb.append(c);
          inWord = true;
        }
      }
    }
    stringArray = new String[stringVector.size()];
    stringVector.copyInto(stringArray);
    return stringArray;
  }

  public final static int parseInt (String s, int defaultValue) {
    if (s == null) return defaultValue;
    int x;
    try {
      x = Integer.parseInt(s);
    } catch (NumberFormatException ex) {
      //System.err.println(ex);
      x = defaultValue;
    }
    return x;
  }

  public final static boolean saveToFile (String filename, String text) {
    boolean result;
    SmallTextFile textFile = new SmallTextFile();
    textFile.setText(text);
    try {
      textFile.write(filename);
      result = true;
    } catch (IOException ex) {
      System.err.println(ex);
      result = false;
    }
    return result;
  }

  public final static Properties getPropertiesFromFile (String filename) {
    File f = new File(filename);
    FileInputStream fis;
    Properties props = new Properties();
    try {
      fis = new FileInputStream(f);
      props.load(fis);
    } catch (FileNotFoundException e) {
      return null;
    } catch (IOException e) {
      return null;
    }
    return props;
  }

  public final static boolean member (String s, Vector v) {
    for (int i=0; i<v.size(); i++) {
      if (s.equals((String) v.elementAt(i))) return true;
    }
    return false;
  }

  public final static boolean member (String s, String[] array) {
    for (int i=0; i<array.length; i++) {
      if (s.equals(array[i])) return true;
    }
    return false;
  }

  public final static int memberInt (String s, String[] array) {
    for (int i=0; i<array.length; i++) {
      if (s.equals(array[i])) return i;
    }
    return -1;
  }

  /**
   * Diff‰rence ensembliste: <code>array1 - array2</code>
   */
  public final static String[] diff (String[] array1, String[] array2) {
    Vector resultVector = new Vector();
    String[] resultArray;
    for (int i=0; i<array1.length; i++) {
        if (!member(array1[i],array2)) {
           resultVector.addElement(array1[i]);
        }
    }
    resultArray = new String[resultVector.size()];
    resultVector.copyInto(resultArray);
    return resultArray;
  }
  
  public final static int doubleToInt (double d) {
    //return (new Double(d)).intValue();
    return (int)d;
  }

  public final static double mean (double[] array) {
    double sum=0;
    int countNaN=0;
    for (int i=0; i<array.length; i++) {
      if (Double.isNaN(array[i])) {
        countNaN++;
      } else {
        sum += array[i];
      }
    }
    return (sum / (array.length - countNaN));
  }
  
  public final static double standardDeviation (double[] array) {
    double mean = mean(array);
    double sum=0;
    for (int i=0; i<array.length; i++) {
      sum += array[i] * array[i];
    }
    sum = sum / array.length - mean * mean;
    return Math.sqrt(sum);
  }
  
  public final static double correlation (double[] x, double[] y) {
    if (x.length != y.length) System.err.println("Bad array length: x="+x.length+" y="+y.length);
    double mean_x = mean(x);
    double mean_y = mean(y);
    double sum=0;
    for (int i=0; i<x.length; i++) {
      sum += x[i]*y[i];
    }
    sum = (sum / x.length) - mean_x * mean_y;
    return (sum / (standardDeviation(x) * standardDeviation(y)));
  }
  
  public final static double euclidian (double[] x, double[] y) {
    if (x.length != y.length) {
      System.err.println("Bad array length: x="+x.length+" y="+y.length);
    }
    double sum = 0;
    double delta;
    for (int i=0; i<x.length; i++) {
    	delta = x[i] - y[i];
    	sum += delta * delta;
    }
    return Math.sqrt(sum);
  }
  
  /**
   * In: Molecular classification of cancer:
   *     class discovery and class prediction by gene expression monitoring.
   *     T.R.Golub et al.
   *     Science, vol. 286, p. 531
   */
  static double correlationGolub (double[] x, double[] y) {
    if (x.length != y.length) System.err.println("Bad array length: x="+x.length+" y="+y.length);
    return ((mean(x) - mean(y))/(standardDeviation(x) + standardDeviation(y)));
  }

  public final static double distance (double[] x, double[] y, int aDistance) {
    switch (aDistance) {
      case DISTANCE_EUCLIDIAN : return euclidian(x,y);
      case DISTANCE_CORRELATION : return (1-correlation(x,y));
      case DISTANCE_CORRELATION_GOLUB : return (1-correlationGolub(x,y));
      default: return 0;
    }
  }

  /**
   * Pour permettre la comparaison inter-arrays affymetrix, on normalise
   * aprˆs avoir enlev‰ les 2% min et les 2% max.
   */
/*
  static void affymetrixNormalize (double[] array) {
    int len = array.length;
    double[] sortedArray = new double[len];
    for (int i=0; i<len; i++) {
    	sortedArray[i] = array[i];
    }
    Arrays.sort(sortedArray);
    int min = (int)Math.floor(0.02 * len);
    int max = (int)Math.floor(0.98 * len);
    double[] reducedArray = new double[max - min + 1];
    for (int i=min; i<max+1; i++) {
    	reducedArray[i-min] = sortedArray[i];
    }
    double mean = mean(reducedArray);
    double sd = standardDeviation(reducedArray);
    for (int i=0; i<len; i++) {
    	array[i] = (array[i] - mean) / Math.max(Math.abs(sortedArray[0]),Math.abs(sortedArray[len-1])); //sd;
    }
  }
*/
  public final static void normalize (double[] array) {
    int len = array.length;
    double mean = mean(array);
    double min = min(array);
    double max = max(array);
    for (int i=0; i<len; i++) {
    	array[i] = (array[i] - mean) / Math.max(Math.abs(min),Math.abs(max)); //sd;
    }
  }

  public final static double max (double[] x) {
    double result = x[0];
    for (int i=0; i<x.length; i++) {
      if (x[i] > result) result = x[i];
    }
    return result;
  }

  public final static double min (double[] x) {
    double result = x[0];
    for (int i=0; i<x.length; i++) {
      if (x[i] < result) result = x[i];
    }
    return result;
  }

  /**
   * Coeff Spearman cf. site Vassar Stats: rank order correlation
   */
  public final static double spearman (int[] x) {
    long sum=0, diff;
    long n= (long)x.length;
    for (int i=0; i<n; i++) {
      diff = i - x[i]; // == (i+1) - (x[i] + 1);
      sum += diff * diff;
    }
    return (1 - ((double)(6 * sum) / (double)(n * (n*n - 1))));
  }
}
