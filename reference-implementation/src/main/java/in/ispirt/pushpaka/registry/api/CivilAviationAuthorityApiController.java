package in.ispirt.pushpaka.registry.api;

import in.ispirt.pushpaka.models.CivilAviationAuthority;
import in.ispirt.pushpaka.registry.service.CivilAviationAuthorityService;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Generated;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class CivilAviationAuthorityApiController implements CivilAviationAuthorityApi {

  private final NativeWebRequest request;
  private final CivilAviationAuthorityService civilAviationAuthorityService;

  @Autowired
  public CivilAviationAuthorityApiController(
    NativeWebRequest request,
    CivilAviationAuthorityService civilAviationAuthorityService
  ) {
    this.request = request;
    this.civilAviationAuthorityService = civilAviationAuthorityService;
  }

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  @Override
  public ResponseEntity<CivilAviationAuthority> addCivilAviationAuthority(
    @Valid @RequestBody CivilAviationAuthority civilAviationAuthority
  ) {
    try {
      return ResponseEntity.ok(civilAviationAuthorityService.create(civilAviationAuthority));
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
  public ResponseEntity<Void> deleteCivilAviationAuthority(
    @PathVariable("civilAviationAuthorityId") UUID civilAviationAuthorityId
  ) {
    try {
      civilAviationAuthorityService.delete(civilAviationAuthorityId);
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

  @Override
  public ResponseEntity<List<CivilAviationAuthority>> findCivilAviationAuthorities() {
    try {
      return ResponseEntity.ok(civilAviationAuthorityService.getAll());
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
  public ResponseEntity<CivilAviationAuthority> getCivilAviationAuthorityById(
    @PathVariable("civilAviationAuthorityId") UUID civilAviationAuthorityId
  ) {
    try {
      return ResponseEntity.ok(civilAviationAuthorityService.getById(civilAviationAuthorityId));
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
  public ResponseEntity<CivilAviationAuthority> updateCivilAviationAuthority(
    @PathVariable("civilAviationAuthorityId") UUID civilAviationAuthorityId,
    @Valid @RequestBody CivilAviationAuthority civilAviationAuthority
  ) {
    try {
      return ResponseEntity.ok(
        civilAviationAuthorityService.update(civilAviationAuthorityId, civilAviationAuthority)
      );
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
