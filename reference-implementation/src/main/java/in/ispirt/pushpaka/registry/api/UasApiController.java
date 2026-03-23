package in.ispirt.pushpaka.registry.api;

import in.ispirt.pushpaka.models.Uas;
import in.ispirt.pushpaka.registry.service.UasService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
@Controller
@RequestMapping("${openapi.pushpakaRegistry.base-path:/api/v1}")
public class UasApiController implements UasApi {

  private final NativeWebRequest request;
  private final UasService uasService;

  @Autowired
  public UasApiController(NativeWebRequest request, UasService uasService) {
    this.request = request;
    this.uasService = uasService;
  }

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  @Override
  public ResponseEntity<Uas> addUas(@Valid @RequestBody Uas uas) {
    try {
      return ResponseEntity.ok(uasService.create(uas));
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
  public ResponseEntity<Void> deleteUas(
    @PathVariable("uasId") UUID uasId,
    @RequestHeader(value = "api_key", required = false) String apiKey
  ) {
    try {
      uasService.delete(uasId);
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
  public ResponseEntity<List<Uas>> findUass() {
    try {
      return ResponseEntity.ok(uasService.getAll());
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
  public ResponseEntity<Uas> getUasById(@PathVariable("uasId") UUID uasId) {
    try {
      return ResponseEntity.ok(uasService.getById(uasId));
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
  public ResponseEntity<Uas> updateUas(@Valid @RequestBody Uas uas) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
