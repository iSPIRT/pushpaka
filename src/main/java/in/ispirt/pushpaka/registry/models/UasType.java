package in.ispirt.pushpaka.registry.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import in.ispirt.pushpaka.registry.dao.Dao;
import in.ispirt.pushpaka.registry.models.Manufacturer;
import in.ispirt.pushpaka.registry.models.ObjectTimestamps;
import in.ispirt.pushpaka.registry.models.OperationCategory;
import in.ispirt.pushpaka.registry.models.UasPropulsionCategory;
import in.ispirt.pushpaka.registry.models.UasWeightCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.net.MalformedURLException;
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
    date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]")
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

  private ObjectTimestamps timestamps;

  /**
   * Default constructor
   * @deprecated Use {@link UasType#UasType(UUID, String, Manufacturer, UasPropulsionCategory, UasWeightCategory, Float, List<OperationCategory>, ObjectTimestamps)}
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
      List<OperationCategory> supportedOperationCategories,
      ObjectTimestamps timestamps) {
    this.id = id;
    this.modelNumber = modelNumber;
    this.manufacturer = manufacturer;
    this.propulsionCategory = propulsionCategory;
    this.weightCategory = weightCategory;
    this.mtow = mtow;
    this.supportedOperationCategories = supportedOperationCategories;
    this.timestamps = timestamps;
  }

  public UasType id(UUID id) {
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
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID
  getId() {
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
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mtow")
  public Float
  getMtow() {
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
  // @Size(max = 512)
  @Schema(
      name = "photoUrl",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED,
      example = "https://ispirt.github.io/pushpaka/")
  @JsonProperty("photoUrl")
  public URI
  getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(URI photoUrl) {
    this.photoUrl = photoUrl;
  }

  public UasType supportedOperationCategories(
      List<OperationCategory> supportedOperationCategories) {
    this.supportedOperationCategories = supportedOperationCategories;
    return this;
  }

  public UasType addSupportedOperationCategoriesItem(
      OperationCategory supportedOperationCategoriesItem) {
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
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("supportedOperationCategories")
  public List<OperationCategory>
  getSupportedOperationCategories() {
    return supportedOperationCategories;
  }

  public void setSupportedOperationCategories(
      List<OperationCategory> supportedOperationCategories) {
    this.supportedOperationCategories = supportedOperationCategories;
  }

  public UasType timestamps(ObjectTimestamps timestamps) {
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
    UasType uasType = (UasType) o;
    return (
        Objects.equals(this.id, uasType.id) && Objects.equals(this.modelNumber, uasType.modelNumber) && Objects.equals(this.manufacturer, uasType.manufacturer) && Objects.equals(this.propulsionCategory, uasType.propulsionCategory) && Objects.equals(this.weightCategory, uasType.weightCategory) && Objects.equals(this.mtow, uasType.mtow) && Objects.equals(this.photoUrl, uasType.photoUrl) && Objects.equals(this.supportedOperationCategories, uasType.supportedOperationCategories) && Objects.equals(this.timestamps, uasType.timestamps));
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
        supportedOperationCategories,
        timestamps);
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

  public static Dao.UasType fromOa(UasType u) {
    try {
      OffsetDateTime n = OffsetDateTime.now();
      Dao.UasType uu = new Dao.UasType(
          u.id,
          Manufacturer.fromOa(u.manufacturer),
          u.modelNumber,
          u.photoUrl.toURL(),
          u.mtow,
          n,
          n,
          u.propulsionCategory);
      return uu;
    } catch (MalformedURLException e) {
      OffsetDateTime n = OffsetDateTime.now();
      Dao.UasType uu = new Dao.UasType(
          u.id,
          Manufacturer.fromOa(u.manufacturer),
          u.modelNumber,
          null,
          u.mtow,
          n,
          n,
          u.propulsionCategory);
      return uu;
    }
  }

  public static UasType toOa(Dao.UasType u) {
    ObjectTimestamps timestamps = new ObjectTimestamps(
        u.getTimestampCreated(),
        u.getTimestampUpdated());
    UasType uu = new UasType(
        u.getId(),
        u.getModelNumber(),
        Manufacturer.toOa(u.getManufacturer()),
        u.getPropulsionCategory(),
        u.getWeightCategory(),
        u.getMtow(),
        u.getSupportedOperationCategories(),
        timestamps);
    return uu;
  }
}
