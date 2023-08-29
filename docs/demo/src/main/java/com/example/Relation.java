package com.example;

public enum Relation {
    MEMBER,
    ADMINISTRATOR,
    REGULATOR,
    PILOT,
    MANUFACTURER,
    OWNER;

    public String getRelation() {

        switch(this) {
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
            
            default:
            return null;
        }
    }
}