package in.ispirt.pushpaka.authorisation;

public enum SubjectType {
    USER,
    PILOT,
    OPERATOR,
    PLATFORM;

    public String getSubjectType() {
        switch (this) {
        case USER:
            return "user";
        case PILOT:
            return "pilot";
        case OPERATOR:
            return "operator";
         case PLATFORM:
            return "platform";
        default:
            return null;
        }
    }
}
