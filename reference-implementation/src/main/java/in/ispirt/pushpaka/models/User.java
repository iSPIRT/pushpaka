package in.ispirt.pushpaka.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.ispirt.pushpaka.dao.Dao;
import in.ispirt.pushpaka.models.ObjectTimestamps;
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
 * User
 */

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
public class User {
  private UUID id;

  private String firstName;

  private String lastName;

  private String email;

  private String phone;

  private String aadharId;

  private Address address;

  private Integer userStatus;

  private ObjectTimestamps timestamps;

  private UserStatus status;

  /**
   * Default constructor
   * @deprecated Use {@link User#User(UUID, String, String, String, String, ObjectTimestamps)}
   */
  @Deprecated
  public User() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public User(
    UUID id,
    String firstName,
    String lastName,
    String email,
    String phone,
    String aadharId,
    Address address,
    ObjectTimestamps timestamps,
    UserStatus status
  ) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.aadharId = aadharId;
    this.address = address;
    this.timestamps = timestamps;
    this.status = status;
  }

  public User id(UUID id) {
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
    example = "e66b7c9e-79f5-44b0-9642-59ca20b7af63",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public User firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
   */
  @Schema(
    name = "firstName",
    example = "John",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @JsonProperty("firstName")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public User lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
   */
  @Schema(
    name = "lastName",
    example = "James",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public User email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  @Schema(
    name = "email",
    example = "john@email.com",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Get phone
   * @return phone
   */

  @Schema(
    name = "phone",
    example = "+919999999999",
    requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  @JsonProperty("phone")
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public User phone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * Get aadharId
   * @return aadharId
   */

  @Schema(
    name = "aadharId",
    example = "+919999999999",
    requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  @JsonProperty("aadharId")
  public String getAadharId() {
    return aadharId;
  }

  public void setAadharId(String aadharId) {
    this.aadharId = aadharId;
  }

  public User status(UserStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get address
   * @return address
   */
  @Schema(name = "", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("address")
  public Address getAddress() {
    return address;
  }

  public void setAddress(Address a) {
    this.address = a;
  }

  /**
   * User Status
   * @return userStatus
   */

  @Schema(
    name = "status",
    example = "ACTIVE",
    description = "User Status",
    requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  @JsonProperty("status")
  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public User timestamps(ObjectTimestamps timestamps) {
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
    User user = (User) o;
    return (
      Objects.equals(this.id, user.id) &&
      Objects.equals(this.firstName, user.firstName) &&
      Objects.equals(this.lastName, user.lastName) &&
      Objects.equals(this.email, user.email) &&
      Objects.equals(this.phone, user.phone) &&
      Objects.equals(this.status, user.status) &&
      Objects.equals(this.timestamps, user.timestamps)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, email, phone, status, timestamps);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

  public static User toOa(Dao.Person u) {
    ObjectTimestamps ot = new ObjectTimestamps(
      u.getTimestampCreated(),
      u.getTimestampUpdated()
    );
    User uu = new User(
      u.getId(),
      "firstName",
      "lastName",
      "",
      u.getPhone(),
      u.getAadharId(),
      Address.toOa(u.getAddress()),
      ot,
      u.getStatus()
    );
    return uu;
  }

  public static Dao.Person fromOa(User u) {
    Dao.Person uu = new Dao.Person(
      u.getId(),
      u.getPhone(),
      u.getAadharId(),
      Address.fromOa(u.getAddress()),
      u.getTimestamps().getCreated(),
      u.getTimestamps().getUpdated(),
      u.getStatus()
    );
    return uu;
  }
}
