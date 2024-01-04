package in.ispirt.pushpaka.registry.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.ispirt.pushpaka.registry.dao.Dao;
import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.Objects;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Sale
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
public class Sale {
  private UUID id;

  private ObjectTimestamps timestamps;

  private Validity validity;

  /**
   * Default constructor
   * @deprecated Use {@link Sale#Sale(OffsetDateTime)}
   */
  @Deprecated
  public Sale() {
    super();
  }

  public Sale(UUID id, ObjectTimestamps ts, Validity v) {
    this.id = id;
    this.timestamps = ts;
    this.validity = v;
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

  public Sale validity(Validity validity) {
    this.validity = validity;
    return this;
  }

  /**
   * Get validity
   * @return validity
   */
  @NotNull
  @Valid
  @Schema(name = "validity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("validity")
  public Validity getValidity() {
    return validity;
  }

  public void setValidity(Validity validity) {
    this.validity = validity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Sale sale = (Sale) o;
    return (
      Objects.equals(this.timestamps, sale.timestamps) &&
      Objects.equals(this.validity, sale.validity)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamps, validity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Sale {\n");
    // sb.append("    from: ").append(toIndentedString(from)).append("\n");
    // sb.append("    till: ").append(toIndentedString(till)).append("\n");
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

  public static Sale toOa(Dao.Sale u) {
    Validity vt = new Validity(u.getValidityStart(), u.getValidityEnd());
    ObjectTimestamps ot = new ObjectTimestamps(
      u.getTimestampCreated(),
      u.getTimestampUpdated()
    );
    Sale uu = new Sale(u.getId(), ot, vt);
    return uu;
  }

  public static Dao.Sale fromOa(Sale u) {
    Dao.Sale uu = new Dao.Sale(
      u.getId(),
      u.getTimestamps().getCreated(),
      u.getTimestamps().getUpdated(),
      u.getValidity().getFrom(),
      u.getValidity().getTill()
    );
    return uu;
  }
}
