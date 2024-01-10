package in.ispirt.pushpaka.registry.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.ispirt.pushpaka.registry.dao.Dao;
import in.ispirt.pushpaka.registry.models.ObjectTimestamps;
import in.ispirt.pushpaka.registry.models.User;
import in.ispirt.pushpaka.registry.models.Validity;
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
 * Pilot
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
public class Pilot {
  private UUID id;

  private User user;

  private ObjectTimestamps timestamps;

  private Validity validity;

  /**
   * Default constructor
   * @deprecated Use {@link Pilot#Pilot(UUID, User, ObjectTimestamps, Validity)}
   */
  @Deprecated
  public Pilot() {
    super();
  }

  public Pilot(UUID id) {
    this.id = id;
  }

  /**
   * Constructor with only required parameters
   */
  public Pilot(UUID id, User user, ObjectTimestamps timestamps, Validity validity) {
    this.id = id;
    this.user = user;
    this.timestamps = timestamps;
    this.validity = validity;
  }

  public Pilot id(UUID id) {
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
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Pilot user(User user) {
    this.user = user;
    return this;
  }

  /**
   * Get user
   * @return user
   */
  @NotNull
  @Valid
  @Schema(name = "user", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("user")
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Pilot timestamps(ObjectTimestamps timestamps) {
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

  public Pilot validity(Validity validity) {
    this.validity = validity;
    return this;
  }

  /**
   * Get validity
   * @return validity
   */
  @NotNull
  @Valid
  @Schema(name = "validity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("validity")
  public Validity getValidity() {
    return validity;
  }

  public void setValidity(Validity validity) {
    this.validity = validity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pilot pilot = (Pilot) o;
    return (
      Objects.equals(this.id, pilot.id) &&
      Objects.equals(this.user, pilot.user) &&
      Objects.equals(this.timestamps, pilot.timestamps) &&
      Objects.equals(this.validity, pilot.validity)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user, timestamps, validity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Pilot {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    timestamps: ").append(toIndentedString(timestamps)).append("\n");
    sb.append("    validity: ").append(toIndentedString(validity)).append("\n");
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

  public static Pilot toOa(Dao.Pilot u) {
    Validity vt = new Validity(u.getValidityStart(), u.getValidityEnd());
    ObjectTimestamps ot = new ObjectTimestamps(
      u.getTimestampCreated(),
      u.getTimestampUpdated()
    );
    Pilot uu = new Pilot(u.getId(), User.toOa(u.getUser()), ot, vt);
    return uu;
  }

  public static Dao.Pilot fromOa(Pilot u) {
    Dao.Pilot uu = new Dao.Pilot(
      u.getId(),
      User.fromOa(u.getUser()),
      u.getTimestamps().getCreated(),
      u.getTimestamps().getUpdated(),
      u.getValidity().getFrom(),
      u.getValidity().getTill()
    );
    return uu;
  }
}
