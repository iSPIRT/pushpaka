package in.ispirt.pushpaka.registry.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import in.ispirt.pushpaka.registry.dao.Dao;
import in.ispirt.pushpaka.registry.models.ObjectTimestamps;
import in.ispirt.pushpaka.registry.models.UasStatus;
import in.ispirt.pushpaka.registry.models.UasType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Uas
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
public class Uas {
  private UUID id;

  private UasType type;

  private String oemSerialNumber;

  private ObjectTimestamps timestamps;

  private UasStatus status;

  /**
   * Default constructor
   * @deprecated Use {@link Uas#Uas(UUID, UasType, String, ObjectTimestamps, UasStatus)}
   */
  @Deprecated
  public Uas() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Uas(
    UUID id,
    UasType type,
    String oemSerialNumber,
    ObjectTimestamps timestamps,
    UasStatus status
  ) {
    this.id = id;
    this.type = type;
    this.oemSerialNumber = oemSerialNumber;
    this.timestamps = timestamps;
    this.status = status;
  }

  public Uas id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @Valid
  @Schema(
    name = "id",
    accessMode = Schema.AccessMode.READ_ONLY,
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Uas type(UasType type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull
  @Valid
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public UasType getType() {
    return type;
  }

  public void setType(UasType type) {
    this.type = type;
  }

  public Uas oemSerialNumber(String oemSerialNumber) {
    this.oemSerialNumber = oemSerialNumber;
    return this;
  }

  /**
   * Get oemSerialNumber
   * @return oemSerialNumber
   */
  @NotNull
  @Size(min = 6, max = 12)
  @Schema(name = "oemSerialNumber", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("oemSerialNumber")
  public String getOemSerialNumber() {
    return oemSerialNumber;
  }

  public void setOemSerialNumber(String oemSerialNumber) {
    this.oemSerialNumber = oemSerialNumber;
  }

  public Uas timestamps(ObjectTimestamps timestamps) {
    this.timestamps = timestamps;
    return this;
  }

  /**
   * Get timestamps
   * @return timestamps
   */
  @NotNull
  @Valid
  @Schema(name = "timestamps", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamps")
  public ObjectTimestamps getTimestamps() {
    return timestamps;
  }

  public void setTimestamps(ObjectTimestamps timestamps) {
    this.timestamps = timestamps;
  }

  public Uas status(UasStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull
  @Valid
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public UasStatus getStatus() {
    return status;
  }

  public void setStatus(UasStatus status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Uas uas = (Uas) o;
    return (
      Objects.equals(this.id, uas.id) &&
      Objects.equals(this.type, uas.type) &&
      Objects.equals(this.oemSerialNumber, uas.oemSerialNumber) &&
      Objects.equals(this.timestamps, uas.timestamps) &&
      Objects.equals(this.status, uas.status)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, oemSerialNumber, timestamps, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Uas {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb
      .append("    oemSerialNumber: ")
      .append(toIndentedString(oemSerialNumber))
      .append("\n");
    sb.append("    timestamps: ").append(toIndentedString(timestamps)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

  public static Uas toOa(Dao.Uas u) {
    ObjectTimestamps ot = new ObjectTimestamps(
      u.getTimestampCreated(),
      u.getTimestampUpdated()
    );
    Uas uu = new Uas(
      u.id,
      UasType.toOa(u.getUasType()),
      u.getOemSerialNo(),
      ot,
      u.getStatus()
    );
    return uu;
  }

  public static Dao.Uas fromOa(Uas u) {
    Dao.Uas uu = new Dao.Uas(
      u.id,
      UasType.fromOa(u.type),
      u.oemSerialNumber,
      u.status,
      u.getTimestamps().getCreated(),
      u.getTimestamps().getUpdated()
    );
    return uu;
  }
}
