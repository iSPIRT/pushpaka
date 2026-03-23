package in.ispirt.pushpaka.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.Generated;

/**
 * Gets or Sets OperationCategory
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
public enum OperationCategory {
  A("A"),

  B("B"),

  C("C");

  private String value;

  OperationCategory(String value) {
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
  public static OperationCategory fromValue(String value) {
    for (OperationCategory b : OperationCategory.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}
