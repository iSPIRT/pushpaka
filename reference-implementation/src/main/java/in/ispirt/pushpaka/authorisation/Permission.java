package in.ispirt.pushpaka.authorisation;

public enum Permission {
  FILE_FLIGHTPLAN,
  SUBMIT_INCIDENTREPORT,
  SUBMIT_FLIGHTLOG,
  COMMISION_UAS,
  REGISTER_UASTYPE,
  FLIGHT_OPERATIONS_ADMIN,
  APPROVE_OPERATOR,
  READ_UAS,
  DECOMMISION_UAS,
  READ_UASTYPE,
  DISCONTINUE_UASTYPE,
  APPROVE_UASTYPE,
  UPDATE_FLIGHTPLAN,
  DELETE_FLIGHTPLAN,
  EXECUTE_FLIGHTPLAN,
  SUPER_ADMIN;

  public String getPermission() {
    switch (this) {
      case FILE_FLIGHTPLAN:
        return "file_flightplan";
      case SUBMIT_INCIDENTREPORT:
        return "submit_incidentreport";
      case SUBMIT_FLIGHTLOG:
        return "submit_flightlog";
      case COMMISION_UAS:
        return "commission_uas";
      case REGISTER_UASTYPE:
        return "register_uastype";
      case FLIGHT_OPERATIONS_ADMIN:
        return "flight_operations_admin";
      case APPROVE_OPERATOR:
        return "approve_operator";
      case READ_UAS:
        return "read_uas";
      case DECOMMISION_UAS:
        return "decommision_uas";
      case READ_UASTYPE:
        return "read_usatype";
      case DISCONTINUE_UASTYPE:
        return "discontinue_uastype";
      case APPROVE_UASTYPE:
        return "approve_usatype";
      case UPDATE_FLIGHTPLAN:
        return "update_flightplan";
      case DELETE_FLIGHTPLAN:
        return "delete_flightplan";
      case EXECUTE_FLIGHTPLAN:
        return "execute_flightplan";
      case SUPER_ADMIN:
        return "super_Admin";
      default:
        return null;
    }
  }
}

