package in.ispirt.pushpaka.registry.utils;

public class DaoException extends Exception {

  public enum Code {
    UNKNOWN("UNKNOWN"),
    NOT_FOUND("NOT FOUND"),
    CONSTRAINT_VIOLATION("CONSTRAINT VIOLATION");

    private String value;

    Code(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  public DaoException(Code c, String m) {
    super("[" + c.toString() + "] " + m);
  }
}
