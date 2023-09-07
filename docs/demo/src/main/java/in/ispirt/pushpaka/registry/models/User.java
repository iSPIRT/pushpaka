package in.ispirt.pushpaka.registry.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
  date = "2023-09-07T16:15:58.556735+05:30[Asia/Kolkata]"
)
public class User {
  private UUID id;

  private String username;

  private String firstName;

  private String lastName;

  private String email;

  private String phone;

  private Integer userStatus;

  /**
   * Default constructor
   * @deprecated Use {@link User#User(UUID, String, String, String, String)}
   */
  @Deprecated
  public User() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public User(UUID id, String username, String firstName, String lastName, String email) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  public User id(UUID id) {
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

  public User username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
   */
  @NotNull
  @Schema(
    name = "username",
    example = "theUser",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public User firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
   */
  @NotNull
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
  @NotNull
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
  @NotNull
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

  public User phone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * Get phone
   * @return phone
   */

  @Schema(
    name = "phone",
    example = "12345",
    requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  @JsonProperty("phone")
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public User userStatus(Integer userStatus) {
    this.userStatus = userStatus;
    return this;
  }

  /**
   * User Status
   * @return userStatus
   */

  @Schema(
    name = "userStatus",
    example = "1",
    description = "User Status",
    requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  @JsonProperty("userStatus")
  public Integer getUserStatus() {
    return userStatus;
  }

  public void setUserStatus(Integer userStatus) {
    this.userStatus = userStatus;
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
      Objects.equals(this.username, user.username) &&
      Objects.equals(this.firstName, user.firstName) &&
      Objects.equals(this.lastName, user.lastName) &&
      Objects.equals(this.email, user.email) &&
      Objects.equals(this.phone, user.phone) &&
      Objects.equals(this.userStatus, user.userStatus)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, firstName, lastName, email, phone, userStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    userStatus: ").append(toIndentedString(userStatus)).append("\n");
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
