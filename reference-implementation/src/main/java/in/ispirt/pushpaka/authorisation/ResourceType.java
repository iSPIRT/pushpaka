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
  PLATFORM_RESOURCETYPE,
  FLIGHTOPERATIONS_RESOURCETYPE,
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
      case PLATFORM_RESOURCETYPE:
        return "platform_resource_type";
      case FLIGHTOPERATIONS_RESOURCETYPE:
        return "flightoperations_resource_type";
      case OPERATOR:
        return "operator";
      default:
        return null;
    }
  }
}
