package in.ispirt.pushpaka.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Logging {
  public static Logger logger = Logger.getLogger("in.ispirt.pushpaka.utils.Logger");

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
