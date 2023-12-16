package in.ispirt.pushpaka.registry.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * Validity
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
public class Validity {
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime from;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime till;

  /**
   * Default constructor
   * @deprecated Use {@link Validity#Validity(OffsetDateTime)}
   */
  @Deprecated
  public Validity() {
    super();
  }

  public Validity(OffsetDateTime s, OffsetDateTime e) {
    this.from = s;
    this.till = e;
  }

  /**
   * Constructor with only required parameters
   */
  public Validity(OffsetDateTime from) {
    this.from = from;
  }

  public Validity from(OffsetDateTime from) {
    this.from = from;
    return this;
  }

  /**
   * Get from
   * @return from
   */
  @NotNull
  @Valid
  @Schema(name = "from", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("from")
  public OffsetDateTime getFrom() {
    return from;
  }

  public void setFrom(OffsetDateTime from) {
    this.from = from;
  }

  public Validity till(OffsetDateTime till) {
    this.till = till;
    return this;
  }

  /**
   * Get till
   * @return till
   */
  @Valid
  @Schema(name = "till", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("till")
  public OffsetDateTime getTill() {
    return till;
  }

  public void setTill(OffsetDateTime till) {
    this.till = till;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Validity validity = (Validity) o;
    return (
      Objects.equals(this.from, validity.from) && Objects.equals(this.till, validity.till)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, till);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Validity {\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    till: ").append(toIndentedString(till)).append("\n");
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
}
