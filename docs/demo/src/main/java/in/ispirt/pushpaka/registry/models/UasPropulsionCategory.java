package in.ispirt.pushpaka.registry.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets UasPropulsionCategory
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-09-07T16:15:58.556735+05:30[Asia/Kolkata]")
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

