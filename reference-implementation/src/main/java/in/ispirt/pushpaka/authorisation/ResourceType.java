package in.ispirt.pushpaka.authorisation;

public enum ResourceType {
    UAS,
    UASTYPE,
    FLIGHTPLAN,
    FLIGHTLOG,
    INCIDENTREPORT;
  
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
        default:
          return null;
      }
    }
}
