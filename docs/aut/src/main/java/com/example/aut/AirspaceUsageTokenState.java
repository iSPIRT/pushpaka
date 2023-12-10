package com.example.aut;

public enum AirspaceUsageTokenState {
  CREATED("Created"),
  INUSE("In-Use"),
  TERMINATED("Terminated");

  private final String state;

  AirspaceUsageTokenState(String state) {
    this.state = state;
  }

  public String getState() {
    return this.state;
  }
}
