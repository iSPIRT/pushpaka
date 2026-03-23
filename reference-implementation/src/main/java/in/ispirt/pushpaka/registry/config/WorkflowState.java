package in.ispirt.pushpaka.registry.config;

public enum WorkflowState {
  PROCESSING("Processing"),
  APPROVED("Approved"),
  REJECTED("Rejected");

  private final String state;

  WorkflowState(String state) {
    this.state = state;
  }

  public String getState() {
    return this.state;
  }
}
