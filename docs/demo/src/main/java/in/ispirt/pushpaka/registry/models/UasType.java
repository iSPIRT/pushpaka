package in.ispirt.pushpaka.registry.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import in.ispirt.pushpaka.registry.models.Manufacturer;
import in.ispirt.pushpaka.registry.models.OperationCategory;
import in.ispirt.pushpaka.registry.models.UasPropulsionCategory;
import in.ispirt.pushpaka.registry.models.UasWeightCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URI;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
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
  date = "2023-09-07T18:36:04.490622+05:30[Asia/Kolkata]"
)
public class UasType {
  private UUID id;

  private String modelNumber;

  private Manufacturer manufacturer;

  private UasPropulsionCategory propulsionCategory;

  private UasWeightCategory weightCategory;

  private Float mtow;

  private URI photoUrl;

  @Valid
  private List<OperationCategory> supportedOperationCategories = new ArrayList<>();

  /**
   * Default constructor
   * @deprecated Use {@link UasType#UasType(UUID, String, Manufacturer, UasPropulsionCategory, UasWeightCategory, Float, List<OperationCategory>)}
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
    UasWeightCategory weightCategory,
    Float mtow,
    List<OperationCategory> supportedOperationCategories
  ) {
    this.id = id;
    this.modelNumber = modelNumber;
    this.manufacturer = manufacturer;
    this.propulsionCategory = propulsionCategory;
    this.weightCategory = weightCategory;
    this.mtow = mtow;
    this.supportedOperationCategories = supportedOperationCategories;
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

  public UasType weightCategory(UasWeightCategory weightCategory) {
    this.weightCategory = weightCategory;
    return this;
  }

  /**
   * Get weightCategory
   * @return weightCategory
   */
  @NotNull
  @Valid
  @Schema(name = "weightCategory", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("weightCategory")
  public UasWeightCategory getWeightCategory() {
    return weightCategory;
  }

  public void setWeightCategory(UasWeightCategory weightCategory) {
    this.weightCategory = weightCategory;
  }

  public UasType mtow(Float mtow) {
    this.mtow = mtow;
    return this;
  }

  /**
   * maximum take off weight in kilograms
   * @return mtow
   */
  @NotNull
  @Schema(
    name = "mtow",
    description = "maximum take off weight in kilograms",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @JsonProperty("mtow")
  public Float getMtow() {
    return mtow;
  }

  public void setMtow(Float mtow) {
    this.mtow = mtow;
  }

  public UasType photoUrl(URI photoUrl) {
    this.photoUrl = photoUrl;
    return this;
  }

  /**
   * Get photoUrl
   * @return photoUrl
   */
  @Valid
  @Size(max = 512)
  @Schema(name = "photoUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("photoUrl")
  public URI getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(URI photoUrl) {
    this.photoUrl = photoUrl;
  }

  public UasType supportedOperationCategories(
    List<OperationCategory> supportedOperationCategories
  ) {
    this.supportedOperationCategories = supportedOperationCategories;
    return this;
  }

  public UasType addSupportedOperationCategoriesItem(
    OperationCategory supportedOperationCategoriesItem
  ) {
    if (this.supportedOperationCategories == null) {
      this.supportedOperationCategories = new ArrayList<>();
    }
    this.supportedOperationCategories.add(supportedOperationCategoriesItem);
    return this;
  }

  /**
   * Get supportedOperationCategories
   * @return supportedOperationCategories
   */
  @NotNull
  @Valid
  @Schema(
    name = "supportedOperationCategories",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @JsonProperty("supportedOperationCategories")
  public List<OperationCategory> getSupportedOperationCategories() {
    return supportedOperationCategories;
  }

  public void setSupportedOperationCategories(
    List<OperationCategory> supportedOperationCategories
  ) {
    this.supportedOperationCategories = supportedOperationCategories;
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
      Objects.equals(this.weightCategory, uasType.weightCategory) &&
      Objects.equals(this.mtow, uasType.mtow) &&
      Objects.equals(this.photoUrl, uasType.photoUrl) &&
      Objects.equals(
        this.supportedOperationCategories,
        uasType.supportedOperationCategories
      )
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(
      id,
      modelNumber,
      manufacturer,
      propulsionCategory,
      weightCategory,
      mtow,
      photoUrl,
      supportedOperationCategories
    );
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
    sb
      .append("    weightCategory: ")
      .append(toIndentedString(weightCategory))
      .append("\n");
    sb.append("    mtow: ").append(toIndentedString(mtow)).append("\n");
    sb.append("    photoUrl: ").append(toIndentedString(photoUrl)).append("\n");
    sb
      .append("    supportedOperationCategories: ")
      .append(toIndentedString(supportedOperationCategories))
      .append("\n");
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
