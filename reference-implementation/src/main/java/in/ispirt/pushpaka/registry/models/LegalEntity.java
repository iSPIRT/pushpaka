package in.ispirt.pushpaka.registry.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.ispirt.pushpaka.registry.dao.Dao;
import in.ispirt.pushpaka.registry.models.Address;
import in.ispirt.pushpaka.registry.models.ObjectTimestamps;
import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Generated;
import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * LegalEntity
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
public class LegalEntity {
  private UUID id;

  private String cin;

  private String name;

  private Address regdAddress;

  private String gstin;

  private ObjectTimestamps timestamps;

  /**
   * Default constructor
   * @deprecated Use {@link LegalEntity#LegalEntity(UUID, String, String, Address, ObjectTimestamps)}
   */
  @Deprecated
  public LegalEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LegalEntity(
    UUID id,
    String cin,
    String name,
    Address regdAddress,
    ObjectTimestamps timestamps,
    String gstin
  ) {
    this.id = id;
    this.cin = cin;
    this.name = name;
    this.regdAddress = regdAddress;
    this.timestamps = timestamps;
    this.gstin = gstin;
  }

  public LegalEntity id(UUID id) {
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

  public LegalEntity cin(String cin) {
    this.cin = cin;
    return this;
  }

  /**
   * Get cin
   * @return cin
   */
  @NotNull
  @Schema(name = "cin", example = "CIN00000", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cin")
  public String getCin() {
    return cin;
  }

  public void setCin(String cin) {
    this.cin = cin;
  }

  public LegalEntity name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull
  @Schema(
    name = "name",
    example = "Test Company Pvt Ltd",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LegalEntity regdAddress(Address regdAddress) {
    this.regdAddress = regdAddress;
    return this;
  }

  /**
   * Get regdAddress
   * @return regdAddress
   */
  @NotNull
  @Valid
  @Schema(name = "regdAddress", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("regdAddress")
  public Address getRegdAddress() {
    return regdAddress;
  }

  public void setRegdAddress(Address regdAddress) {
    this.regdAddress = regdAddress;
  }

  public LegalEntity gstin(String gstin) {
    this.gstin = gstin;
    return this;
  }

  /**
   * Get gstin
   * @return gstin
   */

  @Schema(
    name = "gstin",
    example = "GSTIN00000",
    requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  @JsonProperty("gstin")
  public String getGstin() {
    return gstin;
  }

  public void setGstin(String gstin) {
    this.gstin = gstin;
  }

  public LegalEntity timestamps(ObjectTimestamps timestamps) {
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
    LegalEntity legalEntity = (LegalEntity) o;
    return (
      Objects.equals(this.id, legalEntity.id) &&
      Objects.equals(this.cin, legalEntity.cin) &&
      Objects.equals(this.name, legalEntity.name) &&
      Objects.equals(this.regdAddress, legalEntity.regdAddress) &&
      Objects.equals(this.gstin, legalEntity.gstin) &&
      Objects.equals(this.timestamps, legalEntity.timestamps)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, cin, name, regdAddress, gstin, timestamps);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LegalEntity {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    cin: ").append(toIndentedString(cin)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    regdAddress: ").append(toIndentedString(regdAddress)).append("\n");
    sb.append("    gstin: ").append(toIndentedString(gstin)).append("\n");
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

  public static Dao.LegalEntity fromOa(LegalEntity le) {
    OffsetDateTime n = OffsetDateTime.now();
    Dao.LegalEntity u = new Dao.LegalEntity(
      le.getId(),
      le.getName(),
      Address.fromOa(le.getRegdAddress()),
      le.getCin(),
      le.getGstin(),
      le.getTimestamps().getCreated(),
      le.getTimestamps().getUpdated()
    );
    return u;
  }

  public static LegalEntity toOa(Dao.LegalEntity x) {
    ObjectTimestamps timestamps = new ObjectTimestamps(
      x.getTimestampCreated(),
      x.getTimestampUpdated()
    );
    LegalEntity le = new LegalEntity(
      x.getId(),
      x.getCin(),
      x.getName(),
      Address.toOa(x.getAddress()),
      timestamps,
      x.getGstin()
    );
    return le;
  }
}
