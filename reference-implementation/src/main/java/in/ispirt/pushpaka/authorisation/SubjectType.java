package in.ispirt.pushpaka.authorisation;

public enum SubjectType {
    USER,
    PILOT,
    CAA,
    MANUFACTURER,
    OPERATOR;

    public String getSubjectType() {
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
        default:
            return null;
        }
    }
}
