package in.ispirt.pushpaka.flightauthorisation.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.ispirt.pushpaka.flightauthorisation.dao.Dao;
import in.ispirt.pushpaka.registry.models.OperationCategory;
import in.ispirt.pushpaka.registry.models.Pilot;
import in.ispirt.pushpaka.registry.models.Uas;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Generated;
import javax.validation.constraints.NotNull;

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
public class AirspaceUsageToken {
  private UUID id;
  private Uas uas;
  private Pilot pilot;
  private FlightPlan flightPlan;
  private String issuerID;
  private OffsetDateTime startTime;
  private OffsetDateTime endTime;
  private AirspaceUsageTokenState state;
  private OperationCategory operationType;
  private AirspaceUsageTokenAttenuations attenuations;

  @NotNull
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  @NotNull
  @Schema(name = "uas", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uas")
  public Uas getUas() {
    return uas;
  }

  public void setUas(Uas uas) {
    this.uas = uas;
  }

  @NotNull
  @Schema(name = "pilot", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pilot")
  public Pilot getPilot() {
    return pilot;
  }

  public void setPilot(Pilot pilot) {
    this.pilot = pilot;
  }

  @NotNull
  @Schema(name = "flight_plan", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flight_plan")
  public FlightPlan getFlightPlan() {
    return flightPlan;
  }

  public void setFlightPlan(FlightPlan flightPlan) {
    this.flightPlan = flightPlan;
  }

  public String getIssuerID() {
    return issuerID;
  }

  public void setIssuerID(String issuerID) {
    this.issuerID = issuerID;
  }

  public OffsetDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(OffsetDateTime startTime) {
    this.startTime = startTime;
  }

  public OffsetDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(OffsetDateTime endTime) {
    this.endTime = endTime;
  }

  public AirspaceUsageTokenState getState() {
    return state;
  }

  public void setState(AirspaceUsageTokenState state) {
    this.state = state;
  }

  public OperationCategory getOperationCategory() {
    return operationType;
  }

  public void setOperationCategory(OperationCategory operationType) {
    this.operationType = operationType;
  }

  public AirspaceUsageTokenAttenuations getAttenuations() {
    return attenuations;
  }

  public void setAttenuations(AirspaceUsageTokenAttenuations attenuations) {
    this.attenuations = attenuations;
  }

  public String toJsonString() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonElement jsonElement = gson.toJsonTree(this);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("payload", jsonElement);
    String airspaceUsageTokenJson = gson.toJson(this);

    return airspaceUsageTokenJson;
  }

  public Map toJsonObject() {
    String jsonString = this.toJsonString();
    Gson gson = new Gson();
    Map map = gson.fromJson(jsonString, Map.class);

    return map;
  }

  @Deprecated
  public AirspaceUsageToken() {
    super();
  }

  public AirspaceUsageToken(UUID id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AirspaceUsageToken x = (AirspaceUsageToken) o;
    return (Objects.equals(this.id, x.id));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public static Dao.AirspaceUsageToken fromOa(AirspaceUsageToken a) {
    Dao.AirspaceUsageToken u = new Dao.AirspaceUsageToken(a.getId());
    u.setUas(Uas.fromOa(a.getUas()));
    u.setPilot(Pilot.fromOa(a.getPilot()));
    u.setFlightPlan(FlightPlan.fromOa(a.getFlightPlan()));
    return u;
  }

  public static AirspaceUsageToken toOa(Dao.AirspaceUsageToken x) {
    AirspaceUsageToken le = new AirspaceUsageToken(x.getId());
    le.setId(x.getId());
    le.setPilot(Pilot.toOa(x.getPilot()));
    le.setUas(Uas.toOa(x.getUas()));
    le.setFlightPlan(FlightPlan.toOa(x.getFlightPlan()));
    return le;
  }
}
