package com.example.aut;

public enum AirspaceUsageOperationType {
    VLOS("VLOS"),
    EVLOS("EVLOS"),
    BVLOS("BVLOS");

    private final String operationType;

    AirspaceUsageOperationType(String operationType){
        this.operationType = operationType;
    }

    public String getAirspaceOperationType(){
        return this.operationType;
    }

}