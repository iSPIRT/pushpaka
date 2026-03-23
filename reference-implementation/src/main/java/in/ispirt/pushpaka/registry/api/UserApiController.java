package in.ispirt.pushpaka.registry.api;

import in.ispirt.pushpaka.models.User;
import in.ispirt.pushpaka.registry.service.UserService;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.Optional;
import javax.annotation.Generated;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
@Controller
@RequestMapping("${openapi.pushpakaRegistry.base-path:/api/v1}")
public class UserApiController implements UserApi {

  private final NativeWebRequest request;
  private final UserService userService;

  @Autowired
  public UserApiController(NativeWebRequest request, UserService userService) {
    this.request = request;
    this.userService = userService;
  }

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  @Override
  public ResponseEntity<User> createUser(
    @Valid @RequestBody(required = true) User user,
    @AuthenticationPrincipal Jwt authentication
  ) {
    try {
      return ResponseEntity.ok(userService.create(user));
    } catch (DaoException e) {
      System.err.println("Exception: " + e.toString());
      e.printStackTrace(System.err);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      System.err.println("Exception: " + e.toString());
      e.printStackTrace(System.err);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Void> deleteUser(@PathVariable("username") String username) {
    try {
      userService.delete(username);
      return ResponseEntity.ok().build();
    } catch (DaoException e) {
      System.err.println("Exception: " + e.toString());
      e.printStackTrace(System.err);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      System.err.println("Exception: " + e.toString());
      e.printStackTrace(System.err);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Void> updateUser(
    @PathVariable("username") String username,
    @Valid @RequestBody(required = false) User user
  ) {
    try {
      userService.update(username, user);
      return ResponseEntity.ok().build();
    } catch (DaoException e) {
      System.err.println("Exception: " + e.toString());
      e.printStackTrace(System.err);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      System.err.println("Exception: " + e.toString());
      e.printStackTrace(System.err);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
