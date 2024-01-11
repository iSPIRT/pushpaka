package in.ispirt.pushpaka.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.ispirt.pushpaka.dao.Dao;
import in.ispirt.pushpaka.models.LegalEntity;
import in.ispirt.pushpaka.models.ObjectTimestamps;
import in.ispirt.pushpaka.models.Validity;
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
 * Operator
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
public class Operator {
  private UUID id;

  private LegalEntity legalEntity;

  private Validity validity;

  private ObjectTimestamps timestamps;

  /**
   * Default constructor
   * @deprecated Use {@link Operator#Operator(UUID, LegalEntity, Validity, ObjectTimestamps)}
   */
  @Deprecated
  public Operator() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Operator(
    UUID id,
    LegalEntity legalEntity,
    Validity validity,
    ObjectTimestamps timestamps
  ) {
    this.id = id;
    this.legalEntity = legalEntity;
    this.validity = validity;
    this.timestamps = timestamps;
  }

  public Operator id(UUID id) {
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

  public Operator legalEntity(LegalEntity legalEntity) {
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

  public Operator validity(Validity validity) {
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

  public Operator timestamps(ObjectTimestamps timestamps) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Operator operator = (Operator) o;
    return (
      Objects.equals(this.id, operator.id) &&
      Objects.equals(this.legalEntity, operator.legalEntity) &&
      Objects.equals(this.validity, operator.validity) &&
      Objects.equals(this.timestamps, operator.timestamps)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, legalEntity, validity, timestamps);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Operator {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    legalEntity: ").append(toIndentedString(legalEntity)).append("\n");
    sb.append("    validity: ").append(toIndentedString(validity)).append("\n");
    sb.append("    timestamps: ").append(toIndentedString(timestamps)).append("\n");
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

  public static Operator toOa(Dao.Operator x) {
    Validity vtimestamps = new Validity();
    vtimestamps.setFrom(x.getValidityStart());
    vtimestamps.setTill(x.getValidityEnd());
    ObjectTimestamps timestamps = new ObjectTimestamps(
      x.getTimestampCreated(),
      x.getTimestampUpdated()
    );
    Operator le = new Operator(
      x.getId(),
      LegalEntity.toOa(x.getLegalEntity()),
      vtimestamps,
      timestamps
    );
    return le;
  }

  public static Dao.Operator fromOa(Operator m) {
    OffsetDateTime n = OffsetDateTime.now();
    Dao.Operator u = new Dao.Operator(
      m.id,
      LegalEntity.fromOa(m.getLegalEntity()),
      n,
      n,
      n,
      n
    );
    return u;
  }
}
