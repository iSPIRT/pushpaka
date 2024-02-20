package in.ispirt.pushpaka.utils;

import java.util.logging.Logger;

public class Logging {
  private static Logger logger = Logger.getLogger("pushpaka");

  public static void info(String m) {
    logger.info(m);
  }

  public static void severe(String m) {
    logger.severe(m);
  }

  public static void warning(String m) {
    logger.warning(m);
  }
}
