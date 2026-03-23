package in.ispirt.pushpaka.registry.api;

import in.ispirt.pushpaka.models.RepairAgency;
import in.ispirt.pushpaka.registry.service.RepairAgencyService;
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
public class RepairAgencyApiController implements RepairAgencyApi {

  private final NativeWebRequest request;
  private final RepairAgencyService repairAgencyService;

  @Autowired
  public RepairAgencyApiController(NativeWebRequest request, RepairAgencyService repairAgencyService) {
    this.request = request;
    this.repairAgencyService = repairAgencyService;
  }

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  @Override
  public ResponseEntity<RepairAgency> addRepairAgency(@Valid @RequestBody RepairAgency repairAgency) {
    try {
      return ResponseEntity.ok(repairAgencyService.create(repairAgency));
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
  public ResponseEntity<Void> deleteRepairAgency(
    @PathVariable("repairAgencyId") UUID repairAgencyId
  ) {
    try {
      repairAgencyService.delete(repairAgencyId);
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
  public ResponseEntity<List<RepairAgency>> findRepairAgencies() {
    try {
      return ResponseEntity.ok(repairAgencyService.getAll());
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
  public ResponseEntity<RepairAgency> getRepairAgencyById(
    @PathVariable("repairAgencyId") UUID repairAgencyId
  ) {
    try {
      return ResponseEntity.ok(repairAgencyService.getById(repairAgencyId));
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
  public ResponseEntity<RepairAgency> updateRepairAgency(
    @PathVariable("repairAgencyId") UUID repairAgencyId,
    @Valid @RequestBody RepairAgency repairAgency
  ) {
    try {
      return ResponseEntity.ok(repairAgencyService.update(repairAgencyId, repairAgency));
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
