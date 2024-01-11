package in.ispirt.pushpaka.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.Generated;

/**
 * Gets or Sets State
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
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

  ANDAMAN_NICOBAR_ISLANDS("ANDAMAN_NICOBAR_ISLANDS"),

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
