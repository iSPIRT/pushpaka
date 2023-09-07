package in.ispirt.pushpaka.registry.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.ispirt.pushpaka.registry.models.Address;
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
 * LegalEntity
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T16:15:58.556735+05:30[Asia/Kolkata]"
)
public class LegalEntity {
  private UUID id;

  private String cin;

  private String name;

  private Address regdAddress;

  private String gstin;

  /**
   * Default constructor
   * @deprecated Use {@link LegalEntity#LegalEntity(UUID, String, String, Address)}
   */
  @Deprecated
  public LegalEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LegalEntity(UUID id, String cin, String name, Address regdAddress) {
    this.id = id;
    this.cin = cin;
    this.name = name;
    this.regdAddress = regdAddress;
  }

  public LegalEntity id(UUID id) {
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

  public LegalEntity cin(String cin) {
    this.cin = cin;
    return this;
  }

  /**
   * Get cin
   * @return cin
   */
  @NotNull
  @Schema(name = "cin", requiredMode = Schema.RequiredMode.REQUIRED)
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
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
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

  @Schema(name = "gstin", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gstin")
  public String getGstin() {
    return gstin;
  }

  public void setGstin(String gstin) {
    this.gstin = gstin;
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
      Objects.equals(this.gstin, legalEntity.gstin)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, cin, name, regdAddress, gstin);
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
