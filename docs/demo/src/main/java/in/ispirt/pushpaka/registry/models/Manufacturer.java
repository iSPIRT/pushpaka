package in.ispirt.pushpaka.registry.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.ispirt.pushpaka.registry.models.LegalEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Manufacturer
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T18:36:04.490622+05:30[Asia/Kolkata]"
)
public class Manufacturer {
  private UUID id;

  private LegalEntity legalEntity;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime validFrom;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime validTill;

  /**
   * Default constructor
   * @deprecated Use {@link Manufacturer#Manufacturer(UUID, LegalEntity, OffsetDateTime, OffsetDateTime)}
   */
  @Deprecated
  public Manufacturer() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Manufacturer(
    UUID id,
    LegalEntity legalEntity,
    OffsetDateTime validFrom,
    OffsetDateTime validTill
  ) {
    this.id = id;
    this.legalEntity = legalEntity;
    this.validFrom = validFrom;
    this.validTill = validTill;
  }

  public Manufacturer id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull
  @Valid
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Manufacturer legalEntity(LegalEntity legalEntity) {
    this.legalEntity = legalEntity;
    return this;
  }

  /**
   * Get legalEntity
   * @return legalEntity
   */
  @NotNull
  @Valid
  @Schema(name = "legalEntity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("legalEntity")
  public LegalEntity getLegalEntity() {
    return legalEntity;
  }

  public void setLegalEntity(LegalEntity legalEntity) {
    this.legalEntity = legalEntity;
  }

  public Manufacturer validFrom(OffsetDateTime validFrom) {
    this.validFrom = validFrom;
    return this;
  }

  /**
   * Get validFrom
   * @return validFrom
   */
  @NotNull
  @Valid
  @Schema(name = "validFrom", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("validFrom")
  public OffsetDateTime getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(OffsetDateTime validFrom) {
    this.validFrom = validFrom;
  }

  public Manufacturer validTill(OffsetDateTime validTill) {
    this.validTill = validTill;
    return this;
  }

  /**
   * Get validTill
   * @return validTill
   */
  @NotNull
  @Valid
  @Schema(name = "validTill", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("validTill")
  public OffsetDateTime getValidTill() {
    return validTill;
  }

  public void setValidTill(OffsetDateTime validTill) {
    this.validTill = validTill;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Manufacturer manufacturer = (Manufacturer) o;
    return (
      Objects.equals(this.id, manufacturer.id) &&
      Objects.equals(this.legalEntity, manufacturer.legalEntity) &&
      Objects.equals(this.validFrom, manufacturer.validFrom) &&
      Objects.equals(this.validTill, manufacturer.validTill)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, legalEntity, validFrom, validTill);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Manufacturer {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    legalEntity: ").append(toIndentedString(legalEntity)).append("\n");
    sb.append("    validFrom: ").append(toIndentedString(validFrom)).append("\n");
    sb.append("    validTill: ").append(toIndentedString(validTill)).append("\n");
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
