package in.ispirt.pushpaka.authorisation;

public enum ResourceType {
  UAS,
  UASTYPE,
  FLIGHTPLAN,
  FLIGHTLOG,
  INCIDENTREPORT,
  CAA,
  MANUFACTURER,
  PILOT,
  PLATFORM,
  OPERATOR;

  public String getResourceType() {
    switch (this) {
      case UAS:
        return "uas";
      case UASTYPE:
        return "uastype";
      case FLIGHTPLAN:
        return "flightplan";
      case FLIGHTLOG:
        return "flightlog";
      case INCIDENTREPORT:
        return "incidentreport";
      case CAA:
        return "caa";
      case MANUFACTURER:
        return "manufacturer";
      case PLATFORM:
        return "platform";
      case OPERATOR:
        return "operator";
      case PILOT:
        return "pilot";
      default:
        return null;
    }
  }
}
