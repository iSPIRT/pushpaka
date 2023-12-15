package in.ispirt.pushpaka.flightauthorisation.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
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

/**
 * FlightPlan
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
public class FlightPlan {
  private UUID id;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FlightPlan FlightPlan = (FlightPlan) o;
    return (Objects.equals(this.id, FlightPlan.id));
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
    Dao.FlightPlan u = new Dao.FlightPlan(a.getId());
    return u;
  }

  public static FlightPlan toOa(Dao.FlightPlan x) {
    FlightPlan le = new FlightPlan(x.getId());
    return le;
  }
}
