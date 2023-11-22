package com.example;

public enum ObjectType {
  USER,
  PILOT,
  CAA,
  MANUFACTURER,
  OPERATOR,
  UAS,
  UASTYPE,
  FLIGHTPLAN,
  FLIGHTLOG,
  INCIDENTREPORT;

  public String getObjectType() {
    switch (this) {
      case USER:
        return "user";
      case PILOT:
        return "pilot";
      case CAA:
        return "caa";
      case MANUFACTURER:
        return "manufacturer";
      case OPERATOR:
        return "operator";
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
