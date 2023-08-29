package com.example;

public enum  RelationOperation{
    CREATE,
    TOUCH;

    public String getRelationOperation() {

        switch(this) {
            case CREATE:
            return "Create";
    
            case TOUCH:
            return "Touch";
    
            default:
            return null;
        }
    }
}