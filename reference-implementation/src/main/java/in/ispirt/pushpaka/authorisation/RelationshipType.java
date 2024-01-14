package in.ispirt.pushpaka.authorisation;

public enum RelationshipType {
  MEMBER,
  ADMINISTRATOR,
  REGULATOR,
  PILOT,
  MANUFACTURER,
  PLATFORM,
  OWNER;

  public String getRelationshipType() {
    switch (this) {
      case MEMBER:
        return "member";
      case ADMINISTRATOR:
        return "administrator";
      case REGULATOR:
        return "regulator";
      case PILOT:
        return "pilot";
      case MANUFACTURER:
        return "manufacturer";
      case OWNER:
        return "owner";
      case PLATFORM:
        return "platform";
      default:
        return null;
    }
  }
}
