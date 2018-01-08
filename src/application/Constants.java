// Constants.java

package application;

import java.io.File;

/**
 * @author H. Ripoche
 */
public class Constants {
  public static final String JWSDP_HOME = "/usr/jwsdp-1_0";
  public static final String WEBAPP_HOME = JWSDP_HOME + File.separator + "webapps" + File.separator + "marray";
  public static final String WEBAPP_NAME = "marray";
  public static final String TEMP_DIRECTORY_HIDDEN = JWSDP_HOME + File.separator + "temp";
  public static final String TEMP_DIRECTORY_VISIBLE = WEBAPP_HOME + File.separator + "temp";
  public static final int MULTIPART_MAX_SIZE = 1024 * 1024 * 1024; // 1 Go
  public static final int SESSION_MAX_INACTIVE_INTERVAL = 3600; // 60mn
  public static final String USER_NAME = "ugf";
  public static final String USER_PASSWORD = "igr2002";
}
