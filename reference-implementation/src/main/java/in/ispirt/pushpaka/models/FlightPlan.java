package in.ispirt.pushpaka.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.ispirt.pushpaka.dao.Dao;
import in.ispirt.pushpaka.models.OperationCategory;
import in.ispirt.pushpaka.models.Pilot;
import in.ispirt.pushpaka.models.Uas;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Generated;
import javax.validation.constraints.NotNull;

/**
 * FlightPlan
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
public class FlightPlan {
  private UUID id;
  private Uas uas;
  private Pilot pilot;
  private OperationCategory operationCategory;
  private OffsetDateTime startTime;
  private OffsetDateTime endTime;

  /**
   * Default constructor
   * @deprecated Use {@link FlightPlan#FlightPlan(String, String, String, String, State, BigDecimal)}
   */
  @Deprecated
  public FlightPlan() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FlightPlan(UUID id) {
    this.id = id;
  }

  public FlightPlan setId(UUID id) {
    this.id = id;
    return this;
  }

  @NotNull
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  @NotNull
  @Schema(name = "uas", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("uas")
  public Uas getUas() {
    return uas;
  }

  public void setUas(Uas u) {
    this.uas = u;
  }

  @NotNull
  @Schema(name = "start_time", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("start_time")
  public OffsetDateTime getStartTime() {
    return this.startTime;
  }

  public void setStartTime(OffsetDateTime u) {
    this.startTime = u;
  }

  @NotNull
  @Schema(name = "end_time", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("end_time")
  public OffsetDateTime getEndTime() {
    return this.endTime;
  }

  public void setEndTime(OffsetDateTime u) {
    this.endTime = u;
  }

  @NotNull
  @Schema(name = "pilot", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("pilot")
  public Pilot getPilot() {
    return this.pilot;
  }

  public void setPilot(Pilot u) {
    this.pilot = u;
  }

  @NotNull
  @Schema(name = "operation_category", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("operation_category")
  public OperationCategory getOperationCategory() {
    return this.operationCategory;
  }

  public void setOperationCategory(OperationCategory u) {
    this.operationCategory = u;
  }

  public FlightPlan(
    UUID id,
    Uas u,
    Pilot p,
    OffsetDateTime st,
    OffsetDateTime et,
    OperationCategory c
  ) {
    this.id = id;
    this.uas = u;
    this.pilot = p;
    this.startTime = st;
    this.endTime = et;
    this.operationCategory = c;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FlightPlan fp = (FlightPlan) o;
    return (Objects.equals(this.id, fp.id));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FlightPlan {\n");
    sb.append("    id: ").append(toIndentedString(id.toString())).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  public static Dao.FlightPlan fromOa(FlightPlan a) {
    Dao.FlightPlan u = new Dao.FlightPlan(
      a.getId(),
      Uas.fromOa(a.getUas()),
      Pilot.fromOa(a.getPilot()),
      a.getStartTime(),
      a.getEndTime(),
      a.getOperationCategory()
    );
    return u;
  }

  public static FlightPlan toOa(Dao.FlightPlan x) {
    FlightPlan le = new FlightPlan(
      x.getId(),
      Uas.toOa(x.getUas()),
      Pilot.toOa(x.getPilot()),
      x.getStartTime(),
      x.getEndTime(),
      x.getOperationCategory()
    );
    return le;
  }
}
