package in.ispirt.pushpaka.registry.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import in.ispirt.pushpaka.registry.models.LegalEntity;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * Operator
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-09-07T16:15:58.556735+05:30[Asia/Kolkata]")
public class Operator {

  private UUID id;

  private LegalEntity legalEntity;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime validFrom;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime validTill;

  /**
   * Default constructor
   * @deprecated Use {@link Operator#Operator(UUID, LegalEntity)}
   */
  @Deprecated
  public Operator() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Operator(UUID id, LegalEntity legalEntity) {
    this.id = id;
    this.legalEntity = legalEntity;
  }

  public Operator id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  @NotNull @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Operator legalEntity(LegalEntity legalEntity) {
    this.legalEntity = legalEntity;
    return this;
  }

  /**
   * Get legalEntity
   * @return legalEntity
  */
  @NotNull @Valid 
  @Schema(name = "legalEntity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("legalEntity")
  public LegalEntity getLegalEntity() {
    return legalEntity;
  }

  public void setLegalEntity(LegalEntity legalEntity) {
    this.legalEntity = legalEntity;
  }

  public Operator validFrom(OffsetDateTime validFrom) {
    this.validFrom = validFrom;
    return this;
  }

  /**
   * Get validFrom
   * @return validFrom
  */
  @Valid 
  @Schema(name = "validFrom", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("validFrom")
  public OffsetDateTime getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(OffsetDateTime validFrom) {
    this.validFrom = validFrom;
  }

  public Operator validTill(OffsetDateTime validTill) {
    this.validTill = validTill;
    return this;
  }

  /**
   * Get validTill
   * @return validTill
  */
  @Valid 
  @Schema(name = "validTill", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    Operator operator = (Operator) o;
    return Objects.equals(this.id, operator.id) &&
        Objects.equals(this.legalEntity, operator.legalEntity) &&
        Objects.equals(this.validFrom, operator.validFrom) &&
        Objects.equals(this.validTill, operator.validTill);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, legalEntity, validFrom, validTill);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Operator {\n");
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

