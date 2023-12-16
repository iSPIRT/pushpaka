/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.6.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package in.ispirt.pushpaka.registry.api;

import in.ispirt.pushpaka.registry.dao.Dao;
import in.ispirt.pushpaka.registry.dao.DaoInstance;
import in.ispirt.pushpaka.registry.models.ModelApiResponse;
import in.ispirt.pushpaka.registry.models.UasType;
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
@Tag(name = "uas_types", description = "the uas_types API")
public interface UasTypeApi {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  /**
   * POST /uasType : Add a new uasType to the store
   * Add a new uasType to the store
   *
   * @param uasType Create a new uasType in the store (required)
   * @return Successful operation (status code 200)
   *         or Invalid input (status code 405)
   */
  @Operation(
    operationId = "addUasType",
    summary = "Add a new uasType to the store",
    description = "Add a new uasType to the store",
    tags = { "uas_types" },
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UasType.class)
          )
        }
      ),
      @ApiResponse(responseCode = "405", description = "Invalid input")
    },
    security = {
      @SecurityRequirement(
        name = "registry_auth",
        scopes = { "write:uasTypes", "read:uasTypes" }
      )
    }
  )
  @RequestMapping(
    method = RequestMethod.POST,
    value = "/uasType",
    produces = { "application/json" },
    consumes = { "application/json" }
  )
  default ResponseEntity<UasType> addUasType(
    @Parameter(
      name = "UasType",
      description = "Create a new uasType in the store",
      required = true
    ) @Valid @RequestBody UasType uasType
  ) {
    try {
      Dao.UasType le = UasType.fromOa(uasType);
      Dao.UasType lec = Dao.UasType.create(DaoInstance.getInstance().getSession(), le);
      return ResponseEntity.ok(UasType.toOa(lec));
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
   * DELETE /uasType/{uasTypeId} : Deletes a uasType
   *
   *
   * @param uasTypeId UasType id to delete (required)
   * @return Invalid uasType value (status code 400)
   */
  @Operation(
    operationId = "deleteUasType",
    summary = "Deletes a uasType",
    description = "",
    tags = { "uas_types" },
    responses = {
      @ApiResponse(responseCode = "400", description = "Invalid uasType value")
    },
    security = {
      @SecurityRequirement(
        name = "registry_auth",
        scopes = { "write:uasTypes", "read:uasTypes" }
      )
    }
  )
  @RequestMapping(method = RequestMethod.DELETE, value = "/uasType/{uasTypeId}")
  default ResponseEntity<Void> deleteUasType(
    @Parameter(
      name = "uasTypeId",
      description = "UasType id to delete",
      required = true,
      in = ParameterIn.PATH
    ) @PathVariable("uasTypeId") UUID uasTypeId
  ) {
    Dao.UasType.delete(DaoInstance.getInstance().getSession(), uasTypeId);
    return ResponseEntity.ok().build();
  }

  /**
   * GET /uasType/find : Finds UasTypes
   *
   * @return successful operation (status code 200)
   *         or Invalid value (status code 400)
   */
  @Operation(
    operationId = "findUasTypes",
    summary = "Finds UasTypes",
    tags = { "uas_types" },
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = UasType.class))
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid value")
    },
    security = {
      @SecurityRequirement(
        name = "registry_auth",
        scopes = { "write:uasTypes", "read:uasTypes" }
      )
    }
  )
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/uasType/find",
    produces = { "application/json" }
  )
  default ResponseEntity<List<UasType>> findUasTypes() {
    List<Dao.UasType> les = Dao.UasType.getAll(DaoInstance.getInstance().getSession());
    List<UasType> leso = les
      .stream()
      .map(x -> in.ispirt.pushpaka.registry.models.UasType.toOa(x))
      .collect(Collectors.toList());
    return ResponseEntity.ok(leso);
  }

  /**
   * GET /uasType/{uasTypeId} : Find uasType by ID
   * Returns a single uasType
   *
   * @param uasTypeId ID of uasType to return (required)
   * @return successful operation (status code 200)
   *         or Invalid ID supplied (status code 400)
   *         or UasType not found (status code 404)
   */
  @Operation(
    operationId = "getUasTypeById",
    summary = "Find uasType by ID",
    description = "Returns a single uasType",
    tags = { "uas_types" },
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UasType.class)
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
      @ApiResponse(responseCode = "404", description = "UasType not found")
    },
    security = {
      @SecurityRequirement(name = "api_key"),
      @SecurityRequirement(
        name = "registry_auth",
        scopes = { "write:uasTypes", "read:uasTypes" }
      )
    }
  )
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/uasType/{uasTypeId}",
    produces = { "application/json" }
  )
  default ResponseEntity<UasType> getUasTypeById(
    @Parameter(
      name = "uasTypeId",
      description = "ID of uasType to return",
      required = true,
      in = ParameterIn.PATH
    ) @PathVariable("uasTypeId") UUID uasTypeId
  ) {
    Dao.UasType le = Dao.UasType.get(DaoInstance.getInstance().getSession(), uasTypeId);
    return ResponseEntity.ok(in.ispirt.pushpaka.registry.models.UasType.toOa(le));
  }

  /**
   * PUT /uasType : Update an existing uasType
   * Update an existing uasType by Id
   *
   * @param uasType Update an existent uasType in the store (required)
   * @return Successful operation (status code 200)
   *         or Invalid ID supplied (status code 400)
   *         or UasType not found (status code 404)
   *         or Validation exception (status code 405)
   */
  @Operation(
    operationId = "updateUasType",
    summary = "Update an existing uasType",
    description = "Update an existing uasType by Id",
    tags = { "uas_types" },
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UasType.class)
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
      @ApiResponse(responseCode = "404", description = "UasType not found"),
      @ApiResponse(responseCode = "405", description = "Validation exception")
    },
    security = {
      @SecurityRequirement(
        name = "registry_auth",
        scopes = { "write:uasTypes", "read:uasTypes" }
      )
    }
  )
  @RequestMapping(
    method = RequestMethod.PUT,
    value = "/uasType",
    produces = { "application/json" },
    consumes = { "application/json" }
  )
  default ResponseEntity<UasType> updateUasType(
    @Parameter(
      name = "UasType",
      description = "Update an existent uasType in the store",
      required = true
    ) @Valid @RequestBody UasType uasType
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
