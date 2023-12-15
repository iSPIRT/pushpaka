package in.ispirt.pushpaka.flightauthorisation.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.ispirt.pushpaka.flightauthorisation.dao.Dao;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.Objects;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.openapitools.jackson.nullable.JsonNullable;

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
public class AirspaceUsageToken {
  private UUID id;
  private UUID droneID;
  private UUID pilotID;
  private String issuerID;
  private String startTime;
  private String endTime;
  private AirspaceUsageTokenState state;
  private AirspaceUsageOperationType operationType;
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

  public UUID getDroneID() {
    return droneID;
  }

  public void setDroneID(UUID droneID) {
    this.droneID = droneID;
  }

  public UUID getPilotID() {
    return pilotID;
  }

  public void setPilotID(UUID pilotID) {
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
    String airspaceUsageTokenJson = gson.toJson(this);

    return airspaceUsageTokenJson;
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

  public static Dao.AirspaceUsageToken fromOa(AirspaceUsageToken a) {
    Dao.AirspaceUsageToken u = new Dao.AirspaceUsageToken(a.getId());
    return u;
  }

  public static AirspaceUsageToken toOa(Dao.AirspaceUsageToken x) {
    AirspaceUsageToken le = new AirspaceUsageToken(x.getId());
    return le;
  }
}
