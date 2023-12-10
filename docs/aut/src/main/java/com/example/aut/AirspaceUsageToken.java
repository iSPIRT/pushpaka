package com.example.aut;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AirspaceUsageToken {
  private String id;
  private String droneID;
  private String pilotID;
  private String issuerID;
  private String startTime;
  private String endTime;
  private AirspaceUsageTokenState state;
  private AirspaceUsageOperationType operationType;
  private AirspaceUsageTokenAttenuations attenuations;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDroneID() {
    return droneID;
  }

  public void setDroneID(String droneID) {
    this.droneID = droneID;
  }

  public String getPilotID() {
    return pilotID;
  }

  public void setPilotID(String pilotID) {
    this.pilotID = pilotID;
  }

  public String getIssuerID() {
    return issuerID;
  }

  public void setIssuerID(String issuerID) {
    this.issuerID = issuerID;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public AirspaceUsageTokenState getState() {
    return state;
  }

  public void setState(AirspaceUsageTokenState state) {
    this.state = state;
  }

  public AirspaceUsageOperationType getOperationType() {
    return operationType;
  }

  public void setOperationType(AirspaceUsageOperationType operationType) {
    this.operationType = operationType;
  }

  public AirspaceUsageTokenAttenuations getAttenuations() {
    return attenuations;
  }

  public void setAttenuations(AirspaceUsageTokenAttenuations attenuations) {
    this.attenuations = attenuations;
  }

  public String toJson() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonElement jsonElement = gson.toJsonTree(this);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("payload", jsonElement);
    String airspaceUsageTokenJson = gson.toJson(jsonObject);

    return airspaceUsageTokenJson;
  }
}
