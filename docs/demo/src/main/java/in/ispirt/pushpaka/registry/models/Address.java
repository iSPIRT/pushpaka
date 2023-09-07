package in.ispirt.pushpaka.registry.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import in.ispirt.pushpaka.registry.models.State;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.Objects;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Address
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T16:15:58.556735+05:30[Asia/Kolkata]"
)
public class Address {
  private String line1;

  private String line2;

  private String line3;

  private String city;

  private State state;

  private BigDecimal pinCode;

  /**
   * Default constructor
   * @deprecated Use {@link Address#Address(String, String, String, String, State, BigDecimal)}
   */
  @Deprecated
  public Address() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Address(
    String line1,
    String line2,
    String line3,
    String city,
    State state,
    BigDecimal pinCode
  ) {
    this.line1 = line1;
    this.line2 = line2;
    this.line3 = line3;
    this.city = city;
    this.state = state;
    this.pinCode = pinCode;
  }

  public Address line1(String line1) {
    this.line1 = line1;
    return this;
  }

  /**
   * Get line1
   * @return line1
   */
  @NotNull
  @Schema(
    name = "line1",
    example = "123 ABC Housing Society",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @JsonProperty("line1")
  public String getLine1() {
    return line1;
  }

  public void setLine1(String line1) {
    this.line1 = line1;
  }

  public Address line2(String line2) {
    this.line2 = line2;
    return this;
  }

  /**
   * Get line2
   * @return line2
   */
  @NotNull
  @Schema(
    name = "line2",
    example = "Landmark",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @JsonProperty("line2")
  public String getLine2() {
    return line2;
  }

  public void setLine2(String line2) {
    this.line2 = line2;
  }

  public Address line3(String line3) {
    this.line3 = line3;
    return this;
  }

  /**
   * Get line3
   * @return line3
   */
  @NotNull
  @Schema(
    name = "line3",
    example = "Bandra West",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @JsonProperty("line3")
  public String getLine3() {
    return line3;
  }

  public void setLine3(String line3) {
    this.line3 = line3;
  }

  public Address city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Get city
   * @return city
   */
  @NotNull
  @Schema(name = "city", example = "Mumbai", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Address state(State state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  @NotNull
  @Valid
  @Schema(name = "state", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("state")
  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public Address pinCode(BigDecimal pinCode) {
    this.pinCode = pinCode;
    return this;
  }

  /**
   * Get pinCode
   * minimum: 100000
   * maximum: 999999
   * @return pinCode
   */
  @NotNull
  @Valid
  @DecimalMin("100000")
  @DecimalMax("999999")
  @Schema(name = "pinCode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("pinCode")
  public BigDecimal getPinCode() {
    return pinCode;
  }

  public void setPinCode(BigDecimal pinCode) {
    this.pinCode = pinCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Address address = (Address) o;
    return (
      Objects.equals(this.line1, address.line1) &&
      Objects.equals(this.line2, address.line2) &&
      Objects.equals(this.line3, address.line3) &&
      Objects.equals(this.city, address.city) &&
      Objects.equals(this.state, address.state) &&
      Objects.equals(this.pinCode, address.pinCode)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(line1, line2, line3, city, state, pinCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Address {\n");
    sb.append("    line1: ").append(toIndentedString(line1)).append("\n");
    sb.append("    line2: ").append(toIndentedString(line2)).append("\n");
    sb.append("    line3: ").append(toIndentedString(line3)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    pinCode: ").append(toIndentedString(pinCode)).append("\n");
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
