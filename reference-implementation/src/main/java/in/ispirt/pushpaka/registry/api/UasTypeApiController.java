package in.ispirt.pushpaka.registry.api;

import in.ispirt.pushpaka.models.UasType;
import in.ispirt.pushpaka.registry.service.UasTypeService;
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
public class UasTypeApiController implements UasTypeApi {

  private final NativeWebRequest request;
  private final UasTypeService uasTypeService;

  @Autowired
  public UasTypeApiController(NativeWebRequest request, UasTypeService uasTypeService) {
    this.request = request;
    this.uasTypeService = uasTypeService;
  }

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  @Override
  public ResponseEntity<UasType> approveUasType(
    @Valid @RequestBody Integer modelNumber,
    @PathVariable("uasTypeId") UUID uasTypeId
  ) {
    try {
      return ResponseEntity.ok(uasTypeService.approve(uasTypeId, modelNumber));
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
  public ResponseEntity<UasType> addUasType(@Valid @RequestBody UasType uasType) {
    try {
      return ResponseEntity.ok(uasTypeService.create(uasType));
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
  public ResponseEntity<Void> deleteUasType(@PathVariable("uasTypeId") UUID uasTypeId) {
    try {
      uasTypeService.delete(uasTypeId);
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
  public ResponseEntity<List<UasType>> findUasTypes() {
    try {
      return ResponseEntity.ok(uasTypeService.getAll());
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
  public ResponseEntity<UasType> getUasTypeById(@PathVariable("uasTypeId") UUID uasTypeId) {
    try {
      return ResponseEntity.ok(uasTypeService.getById(uasTypeId));
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
  public ResponseEntity<UasType> updateUasType(@Valid @RequestBody UasType uasType) {
    try {
      return ResponseEntity.ok(uasTypeService.update(uasType));
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
