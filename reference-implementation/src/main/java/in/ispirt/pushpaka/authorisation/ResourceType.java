package in.ispirt.pushpaka.authorisation;

public enum ResourceType {
    UAS,
    UASTYPE,
    FLIGHTPLAN,
    FLIGHTLOG,
    INCIDENTREPORT,
    CAA,
    MANUFACTURER,
    PLATFORM,
    RESOURCE;
  
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
        case RESOURCE:
          return "resource";
        default:
          return null;
      }
    }
}
