package in.ispirt.pushpaka.flightauthorisation.api;

import in.ispirt.pushpaka.flightauthorisation.service.AirspaceUsageTokenService;
import in.ispirt.pushpaka.models.AirspaceUsageToken;
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
public class AirspaceUsageTokenApiController implements AirspaceUsageTokenApi {

  private final NativeWebRequest request;
  private final AirspaceUsageTokenService airspaceUsageTokenService;

  @Autowired
  public AirspaceUsageTokenApiController(
    NativeWebRequest request,
    AirspaceUsageTokenService airspaceUsageTokenService
  ) {
    this.request = request;
    this.airspaceUsageTokenService = airspaceUsageTokenService;
  }

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  @Override
  public ResponseEntity<AirspaceUsageToken> addAirspaceUsageToken(
    @Valid @RequestBody AirspaceUsageToken aut
  ) {
    try {
      return ResponseEntity.ok(airspaceUsageTokenService.create(aut));
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
  public ResponseEntity<Void> deleteAirspaceUsageToken(
    @PathVariable("AirspaceUsageTokenId") UUID AirspaceUsageTokenId
  ) {
    try {
      airspaceUsageTokenService.delete(AirspaceUsageTokenId);
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
  public ResponseEntity<List<AirspaceUsageToken>> findAirspaceUsageTokens() {
    try {
      return ResponseEntity.ok(airspaceUsageTokenService.getAll());
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
  public ResponseEntity<AirspaceUsageToken> getAirspaceUsageTokenById(
    @PathVariable("AirspaceUsageTokenId") UUID AirspaceUsageTokenId
  ) {
    try {
      return ResponseEntity.ok(airspaceUsageTokenService.getById(AirspaceUsageTokenId));
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
  public ResponseEntity<AirspaceUsageToken> updateAirspaceUsageToken(
    @PathVariable("AirspaceUsageTokenId") UUID AirspaceUsageTokenId,
    @Valid @RequestBody AirspaceUsageToken AirspaceUsageToken
  ) {
    try {
      return ResponseEntity.ok(
        airspaceUsageTokenService.update(AirspaceUsageTokenId, AirspaceUsageToken)
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
