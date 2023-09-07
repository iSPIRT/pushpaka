package in.ispirt.pushpaka.registry.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.Objects;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Gets or Sets State
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T16:15:58.556735+05:30[Asia/Kolkata]"
)
public enum State {
  ANDHRA_PRADESH("ANDHRA_PRADESH"),

  ARUNACHAL_PRADESH("ARUNACHAL_PRADESH"),

  ASSAM("ASSAM"),

  BIHAR("BIHAR"),

  CHHATTISGARH("CHHATTISGARH"),

  GOA("GOA"),

  GUJARAT("GUJARAT"),

  HARYANA("HARYANA"),

  HIMACHAL_PRADESH("HIMACHAL_PRADESH"),

  JHARKHAND("JHARKHAND"),

  KARNATAKA("KARNATAKA"),

  KERALA("KERALA"),

  MADHYA_PRADESH("MADHYA_PRADESH"),

  MAHARASHTRA("MAHARASHTRA"),

  MANIPUR("MANIPUR"),

  MEGHALAYA("MEGHALAYA"),

  MIZORAM("MIZORAM"),

  NAGALAND("NAGALAND"),

  ODISHA("ODISHA"),

  PUNJAB("PUNJAB"),

  RAJASTHAN("RAJASTHAN"),

  SIKKIM("SIKKIM"),

  TAMIL_NADU("TAMIL_NADU"),

  TELANGANA("TELANGANA"),

  TRIPURA("TRIPURA"),

  UTTARAKHAND("UTTARAKHAND"),

  UTTAR_PRADESH("UTTAR_PRADESH"),

  WEST_BENGAL("WEST_BENGAL"),

  ANDAMAN_AND_NICOBAR_ISLANDS("ANDAMAN_AND_NICOBAR_ISLANDS"),

  CHANDIGARH("CHANDIGARH"),

  DADRA_NAGAR_HAVELI_DAMAN_DIU("DADRA_NAGAR_HAVELI_DAMAN_DIU"),

  NCT("NCT"),

  JAMMU_KASHMIR("JAMMU_KASHMIR"),

  LADAKH("LADAKH"),

  LAKSHADWEEP("LAKSHADWEEP"),

  PUDUCHERRY("PUDUCHERRY");

  private String value;

  State(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static State fromValue(String value) {
    for (State b : State.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}
