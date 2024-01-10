package in.ispirt.pushpaka.authorisation;

public enum SubjectType {
  USER,
  OPERATOR,
  PLATFORM,
  MANUFACTURER,
  CAA;

  public String getSubjectType() {
    switch (this) {
      case USER:
        return "user";
      case OPERATOR:
        return "operator";
      case PLATFORM:
        return "platform";
      case MANUFACTURER:
        return "manufacturer";
      case CAA:
        return "caa";
      default:
        return null;
    }
  }
}
