package in.ispirt.pushpaka.registry.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import in.ispirt.pushpaka.registry.models.Manufacturer;
import in.ispirt.pushpaka.registry.models.UasPropulsionCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
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
 * UasType
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T16:15:58.556735+05:30[Asia/Kolkata]"
)
public class UasType {
  private UUID id;

  private String modelNumber;

  private Manufacturer manufacturer;

  private UasPropulsionCategory propulsionCategory;

  private BigDecimal mass;

  /**
   * Default constructor
   * @deprecated Use {@link UasType#UasType(UUID, String, Manufacturer, UasPropulsionCategory, BigDecimal)}
   */
  @Deprecated
  public UasType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UasType(
    UUID id,
    String modelNumber,
    Manufacturer manufacturer,
    UasPropulsionCategory propulsionCategory,
    BigDecimal mass
  ) {
    this.id = id;
    this.modelNumber = modelNumber;
    this.manufacturer = manufacturer;
    this.propulsionCategory = propulsionCategory;
    this.mass = mass;
  }

  public UasType id(UUID id) {
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

  public UasType modelNumber(String modelNumber) {
    this.modelNumber = modelNumber;
    return this;
  }

  /**
   * Get modelNumber
   * @return modelNumber
   */
  @NotNull
  @Schema(name = "modelNumber", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("modelNumber")
  public String getModelNumber() {
    return modelNumber;
  }

  public void setModelNumber(String modelNumber) {
    this.modelNumber = modelNumber;
  }

  public UasType manufacturer(Manufacturer manufacturer) {
    this.manufacturer = manufacturer;
    return this;
  }

  /**
   * Get manufacturer
   * @return manufacturer
   */
  @NotNull
  @Valid
  @Schema(name = "manufacturer", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("manufacturer")
  public Manufacturer getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(Manufacturer manufacturer) {
    this.manufacturer = manufacturer;
  }

  public UasType propulsionCategory(UasPropulsionCategory propulsionCategory) {
    this.propulsionCategory = propulsionCategory;
    return this;
  }

  /**
   * Get propulsionCategory
   * @return propulsionCategory
   */
  @NotNull
  @Valid
  @Schema(name = "propulsionCategory", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("propulsionCategory")
  public UasPropulsionCategory getPropulsionCategory() {
    return propulsionCategory;
  }

  public void setPropulsionCategory(UasPropulsionCategory propulsionCategory) {
    this.propulsionCategory = propulsionCategory;
  }

  public UasType mass(BigDecimal mass) {
    this.mass = mass;
    return this;
  }

  /**
   * maximum take off mass in grams
   * @return mass
   */
  @NotNull
  @Valid
  @Schema(
    name = "mass",
    description = "maximum take off mass in grams",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @JsonProperty("mass")
  public BigDecimal getMass() {
    return mass;
  }

  public void setMass(BigDecimal mass) {
    this.mass = mass;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UasType uasType = (UasType) o;
    return (
      Objects.equals(this.id, uasType.id) &&
      Objects.equals(this.modelNumber, uasType.modelNumber) &&
      Objects.equals(this.manufacturer, uasType.manufacturer) &&
      Objects.equals(this.propulsionCategory, uasType.propulsionCategory) &&
      Objects.equals(this.mass, uasType.mass)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, modelNumber, manufacturer, propulsionCategory, mass);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UasType {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    modelNumber: ").append(toIndentedString(modelNumber)).append("\n");
    sb.append("    manufacturer: ").append(toIndentedString(manufacturer)).append("\n");
    sb
      .append("    propulsionCategory: ")
      .append(toIndentedString(propulsionCategory))
      .append("\n");
    sb.append("    mass: ").append(toIndentedString(mass)).append("\n");
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
