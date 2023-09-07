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
 * Gets or Sets UasPropulsionCategory
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T18:36:04.490622+05:30[Asia/Kolkata]"
)
public enum UasPropulsionCategory {
  VTOL("VTOL"),

  FIXED_WING("FIXED_WING"),

  HYBRID("HYBRID");

  private String value;

  UasPropulsionCategory(String value) {
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
  public static UasPropulsionCategory fromValue(String value) {
    for (UasPropulsionCategory b : UasPropulsionCategory.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}
