/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.6.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package in.ispirt.pushpaka.registry.api;

import in.ispirt.pushpaka.registry.dao.Dao;
import in.ispirt.pushpaka.registry.dao.DaoInstance;
import in.ispirt.pushpaka.registry.models.Lease;
import in.ispirt.pushpaka.registry.models.ModelApiResponse;
import in.ispirt.pushpaka.registry.models.Sale;
import in.ispirt.pushpaka.registry.utils.DaoException;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
@Validated
@Tag(name = "ownership", description = "the ownership API")
public interface OwnershipApi {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  /**
   * POST /lease : Add a new lease to the store
   * Add a new lease to the store
   *
   * @param lease Create a new lease in the store (required)
   * @return Successful operation (status code 200)
   *         or Invalid input (status code 405)
   */
  @Operation(
    operationId = "addLease",
    summary = "Add a new lease to the store",
    description = "Add a new lease to the store",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Lease.class)
          )
        }
      ),
      @ApiResponse(responseCode = "405", description = "Invalid input")
    },
    security = { @SecurityRequirement(name = "jwt") }
  )
  @RequestMapping(
    method = RequestMethod.POST,
    value = "/lease",
    produces = { "application/json" },
    consumes = { "application/json" }
  )
  default ResponseEntity<Lease> addLease(
    @Parameter(
      name = "Lease",
      description = "Create a new lease in the store",
      required = true
    ) @Valid @RequestBody Lease lease
  ) {
    try {
      Dao.Lease le = Lease.fromOa(lease);
      Dao.Lease lec = Dao.Lease.create(DaoInstance.getInstance().getSession(), le);
      return ResponseEntity.ok(Lease.toOa(lec));
    } catch (DaoException e) {
      System.err.println("Exception: " + e.toString());
      e.printStackTrace(System.err);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      System.err.println("Exception: " + e.toString());
      e.printStackTrace(System.err);
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  /**
   * DELETE /lease/{leaseId} : Deletes a lease
   *
   *
   * @param leaseId Lease id to delete (required)
   * @return Invalid lease value (status code 400)
   */
  @Operation(
    operationId = "deleteLease",
    summary = "Deletes a lease",
    description = "",
    responses = {
      @ApiResponse(responseCode = "400", description = "Invalid lease value")
    },
    security = { @SecurityRequirement(name = "jwt") }
  )
  @RequestMapping(method = RequestMethod.DELETE, value = "/lease/{leaseId}")
  default ResponseEntity<Void> deleteLease(
    @Parameter(
      name = "leaseId",
      description = "Lease id to delete",
      required = true,
      in = ParameterIn.PATH
    ) @PathVariable("leaseId") UUID leaseId
  ) {
    Dao.Lease.delete(DaoInstance.getInstance().getSession(), leaseId);
    return ResponseEntity.ok().build();
  }

  /**
   * GET /lease/find : Finds Leases
   *
   * @return successful operation (status code 200)
   *         or Invalid value (status code 400)
   */
  @Operation(
    operationId = "findLeases",
    summary = "Finds Leases",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Lease.class))
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid value")
    },
    security = { @SecurityRequirement(name = "jwt") }
  )
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/lease/find",
    produces = { "application/json" }
  )
  default ResponseEntity<List<Lease>> findLeases() {
    List<Dao.Lease> les = Dao.Lease.getAll(DaoInstance.getInstance().getSession());
    List<Lease> leso = les
      .stream()
      .map(x -> in.ispirt.pushpaka.registry.models.Lease.toOa(x))
      .collect(Collectors.toList());
    return ResponseEntity.ok(leso);
  }

  /**
   * GET /lease/{leaseId} : Find lease by ID
   * Returns a single lease
   *
   * @param leaseId ID of lease to return (required)
   * @return successful operation (status code 200)
   *         or Invalid ID supplied (status code 400)
   *         or Lease not found (status code 404)
   */
  @Operation(
    operationId = "getLeaseById",
    summary = "Find lease by ID",
    description = "Returns a single lease",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Lease.class)
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
      @ApiResponse(responseCode = "404", description = "Lease not found")
    },
    security = { @SecurityRequirement(name = "jwt") }
  )
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/lease/{leaseId}",
    produces = { "application/json" }
  )
  default ResponseEntity<Lease> getLeaseById(
    @Parameter(
      name = "leaseId",
      description = "ID of lease to return",
      required = true,
      in = ParameterIn.PATH
    ) @PathVariable("leaseId") UUID leaseId
  ) {
    Dao.Lease le = Dao.Lease.get(DaoInstance.getInstance().getSession(), leaseId);
    return ResponseEntity.ok(in.ispirt.pushpaka.registry.models.Lease.toOa(le));
  }

  /**
   * PUT /lease : Update an existing lease
   * Update an existing lease by Id
   *
   * @param lease Update an existent lease in the store (required)
   * @return Successful operation (status code 200)
   *         or Invalid ID supplied (status code 400)
   *         or Lease not found (status code 404)
   *         or Validation exception (status code 405)
   */
  @Operation(
    operationId = "updateLease",
    summary = "Update an existing lease",
    description = "Update an existing lease by Id",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Lease.class)
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
      @ApiResponse(responseCode = "404", description = "Lease not found"),
      @ApiResponse(responseCode = "405", description = "Validation exception")
    },
    security = { @SecurityRequirement(name = "jwt") }
  )
  @RequestMapping(
    method = RequestMethod.PUT,
    value = "/lease",
    produces = { "application/json" },
    consumes = { "application/json" }
  )
  default ResponseEntity<Lease> updateLease(
    @Parameter(
      name = "Lease",
      description = "Update an existent lease in the store",
      required = true
    ) @Valid @RequestBody Lease lease
  ) {
    getRequest()
      .ifPresent(
        request -> {
          for (MediaType mediaType : MediaType.parseMediaTypes(
            request.getHeader("Accept")
          )) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
              String exampleString =
                "{ \"photoUrl\" : \"https://openapi-generator.tech\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"modelNumber\" : \"modelNumber\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"mtow\" : 6.0274563, \"manufacturer\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"validity\" : { \"till\" : \"2000-01-23T04:56:07.000+00:00\", \"from\" : \"2000-01-23T04:56:07.000+00:00\" }, \"legalEntity\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"name\" : \"name\", \"cin\" : \"cin\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"regdAddress\" : { \"city\" : \"Mumbai\", \"pinCode\" : 172074.45705867198, \"line3\" : \"Bandra West\", \"line2\" : \"Landmark\", \"line1\" : \"123 ABC Housing Society\" }, \"gstin\" : \"gstin\" } }, \"supportedOperationCategories\" : [ null, null ] }";
              ApiUtil.setExampleResponse(request, "application/json", exampleString);
              break;
            }
          }
        }
      );
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  /**
   * POST /ownership : Add a new ownership to the store
   * Add a new ownership to the store
   *
   * @param ownership Create a new ownership in the store (required)
   * @return Successful operation (status code 200)
   *         or Invalid input (status code 405)
   */
  @Operation(
    operationId = "addSale",
    summary = "Add a new ownership to the store",
    description = "Add a new ownership to the store",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Sale.class)
          )
        }
      ),
      @ApiResponse(responseCode = "405", description = "Invalid input")
    },
    security = { @SecurityRequirement(name = "jwt") }
  )
  @RequestMapping(
    method = RequestMethod.POST,
    value = "/ownership",
    produces = { "application/json" },
    consumes = { "application/json" }
  )
  default ResponseEntity<Sale> addSale(
    @Parameter(
      name = "Sale",
      description = "Create a new ownership in the store",
      required = true
    ) @Valid @RequestBody Sale ownership
  ) {
    try {
      Dao.Sale le = Sale.fromOa(ownership);
      Dao.Sale lec = Dao.Sale.create(DaoInstance.getInstance().getSession(), le);
      return ResponseEntity.ok(Sale.toOa(lec));
    } catch (DaoException e) {
      System.err.println("Exception: " + e.toString());
      e.printStackTrace(System.err);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      System.err.println("Exception: " + e.toString());
      e.printStackTrace(System.err);
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  /**
   * DELETE /ownership/{ownershipId} : Deletes a ownership
   *
   *
   * @param ownershipId Sale id to delete (required)
   * @return Invalid ownership value (status code 400)
   */
  @Operation(
    operationId = "deleteSale",
    summary = "Deletes a ownership",
    description = "",
    responses = {
      @ApiResponse(responseCode = "400", description = "Invalid ownership value")
    },
    security = { @SecurityRequirement(name = "jwt") }
  )
  @RequestMapping(method = RequestMethod.DELETE, value = "/ownership/{ownershipId}")
  default ResponseEntity<Void> deleteSale(
    @Parameter(
      name = "ownershipId",
      description = "Sale id to delete",
      required = true,
      in = ParameterIn.PATH
    ) @PathVariable("ownershipId") UUID ownershipId
  ) {
    Dao.Sale.delete(DaoInstance.getInstance().getSession(), ownershipId);
    return ResponseEntity.ok().build();
  }

  /**
   * GET /ownership/find : Finds Sales
   *
   * @return successful operation (status code 200)
   *         or Invalid value (status code 400)
   */
  @Operation(
    operationId = "findSales",
    summary = "Finds Sales",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Sale.class))
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid value")
    },
    security = { @SecurityRequirement(name = "jwt") }
  )
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/ownership/find",
    produces = { "application/json" }
  )
  default ResponseEntity<List<Sale>> findSales() {
    List<Dao.Sale> les = Dao.Sale.getAll(DaoInstance.getInstance().getSession());
    List<Sale> leso = les
      .stream()
      .map(x -> in.ispirt.pushpaka.registry.models.Sale.toOa(x))
      .collect(Collectors.toList());
    return ResponseEntity.ok(leso);
  }

  /**
   * GET /ownership/{ownershipId} : Find ownership by ID
   * Returns a single ownership
   *
   * @param ownershipId ID of ownership to return (required)
   * @return successful operation (status code 200)
   *         or Invalid ID supplied (status code 400)
   *         or Sale not found (status code 404)
   */
  @Operation(
    operationId = "getSaleById",
    summary = "Find ownership by ID",
    description = "Returns a single ownership",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Sale.class)
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
      @ApiResponse(responseCode = "404", description = "Sale not found")
    },
    security = { @SecurityRequirement(name = "jwt") }
  )
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/ownership/{ownershipId}",
    produces = { "application/json" }
  )
  default ResponseEntity<Sale> getSaleById(
    @Parameter(
      name = "ownershipId",
      description = "ID of ownership to return",
      required = true,
      in = ParameterIn.PATH
    ) @PathVariable("ownershipId") UUID ownershipId
  ) {
    Dao.Sale le = Dao.Sale.get(DaoInstance.getInstance().getSession(), ownershipId);
    return ResponseEntity.ok(in.ispirt.pushpaka.registry.models.Sale.toOa(le));
  }

  /**
   * PUT /ownership : Update an existing ownership
   * Update an existing ownership by Id
   *
   * @param ownership Update an existent ownership in the store (required)
   * @return Successful operation (status code 200)
   *         or Invalid ID supplied (status code 400)
   *         or Sale not found (status code 404)
   *         or Validation exception (status code 405)
   */
  @Operation(
    operationId = "updateSale",
    summary = "Update an existing ownership",
    description = "Update an existing ownership by Id",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Sale.class)
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
      @ApiResponse(responseCode = "404", description = "Sale not found"),
      @ApiResponse(responseCode = "405", description = "Validation exception")
    },
    security = { @SecurityRequirement(name = "jwt") }
  )
  @RequestMapping(
    method = RequestMethod.PUT,
    value = "/ownership",
    produces = { "application/json" },
    consumes = { "application/json" }
  )
  default ResponseEntity<Sale> updateSale(
    @Parameter(
      name = "Sale",
      description = "Update an existent ownership in the store",
      required = true
    ) @Valid @RequestBody Sale ownership
  ) {
    getRequest()
      .ifPresent(
        request -> {
          for (MediaType mediaType : MediaType.parseMediaTypes(
            request.getHeader("Accept")
          )) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
              String exampleString =
                "{ \"photoUrl\" : \"https://openapi-generator.tech\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"modelNumber\" : \"modelNumber\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"mtow\" : 6.0274563, \"manufacturer\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"validity\" : { \"till\" : \"2000-01-23T04:56:07.000+00:00\", \"from\" : \"2000-01-23T04:56:07.000+00:00\" }, \"legalEntity\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"name\" : \"name\", \"cin\" : \"cin\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"regdAddress\" : { \"city\" : \"Mumbai\", \"pinCode\" : 172074.45705867198, \"line3\" : \"Bandra West\", \"line2\" : \"Landmark\", \"line1\" : \"123 ABC Housing Society\" }, \"gstin\" : \"gstin\" } }, \"supportedOperationCategories\" : [ null, null ] }";
              ApiUtil.setExampleResponse(request, "application/json", exampleString);
              break;
            }
          }
        }
      );
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
