package in.ispirt.pushpaka.registry.api;

import in.ispirt.pushpaka.models.DigitalSkyServiceProvider;
import in.ispirt.pushpaka.registry.service.DigitalSkyServiceProviderService;
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
public class DigitalSkyServiceProviderApiController implements DigitalSkyServiceProviderApi {

  private final NativeWebRequest request;
  private final DigitalSkyServiceProviderService digitalSkyServiceProviderService;

  @Autowired
  public DigitalSkyServiceProviderApiController(
    NativeWebRequest request,
    DigitalSkyServiceProviderService digitalSkyServiceProviderService
  ) {
    this.request = request;
    this.digitalSkyServiceProviderService = digitalSkyServiceProviderService;
  }

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  @Override
  public ResponseEntity<DigitalSkyServiceProvider> addDigitalSkyServiceProvider(
    @Valid @RequestBody DigitalSkyServiceProvider digitalSkyServiceProvider
  ) {
    try {
      return ResponseEntity.ok(
        digitalSkyServiceProviderService.create(digitalSkyServiceProvider)
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

  @Override
  public ResponseEntity<Void> deleteDigitalSkyServiceProvider(
    @PathVariable("digitalSkyServiceProviderId") UUID digitalSkyServiceProviderId
  ) {
    try {
      digitalSkyServiceProviderService.delete(digitalSkyServiceProviderId);
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
  public ResponseEntity<List<DigitalSkyServiceProvider>> findDigitalSkyServiceProviders() {
    try {
      return ResponseEntity.ok(digitalSkyServiceProviderService.getAll());
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
  public ResponseEntity<DigitalSkyServiceProvider> getDigitalSkyServiceProviderById(
    @PathVariable("digitalSkyServiceProviderId") UUID digitalSkyServiceProviderId
  ) {
    try {
      return ResponseEntity.ok(
        digitalSkyServiceProviderService.getById(digitalSkyServiceProviderId)
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

  @Override
  public ResponseEntity<DigitalSkyServiceProvider> updateDigitalSkyServiceProvider(
    @PathVariable("digitalSkyServiceProviderId") UUID digitalSkyServiceProviderId,
    @Valid @RequestBody DigitalSkyServiceProvider digitalSkyServiceProvider
  ) {
    try {
      return ResponseEntity.ok(
        digitalSkyServiceProviderService.update(
          digitalSkyServiceProviderId,
          digitalSkyServiceProvider
        )
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
