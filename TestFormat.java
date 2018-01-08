// TestFormat.java

import java.util.*;
import java.text.*;

public class TestFormat {

  public static void main (String[] args) {
    // Print out a number using the localized number, integer, currency,
    // and percent format for each locale
    Locale locale = Locale.US;
    double myNumber = -1234.56786877868686;
    NumberFormat form;
    for (int j=0; j<4; ++j) {
        System.out.println("FORMAT");
        System.out.print(locale.getDisplayName());
        switch (j) {
        case 0:
            form = NumberFormat.getInstance(locale); break;
        case 1:
            form = NumberFormat.getIntegerInstance(locale); break;
        case 2:
            form = NumberFormat.getCurrencyInstance(locale); break;
        default:
            form = NumberFormat.getPercentInstance(locale); break;
        }
        if (form instanceof DecimalFormat) {
            System.out.print(": " + ((DecimalFormat) form).toPattern());
        }
        System.out.print(" -> " + form.format(myNumber));
        try {
            System.out.println(" -> " + form.parse(form.format(myNumber)));
        } catch (ParseException e) {}
    }
  }
}
