/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.6.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package in.ispirt.pushpaka.registry.api;

import in.ispirt.pushpaka.registry.dao.Dao;
import in.ispirt.pushpaka.registry.dao.DaoInstance;
import in.ispirt.pushpaka.registry.models.LegalEntity;
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
import java.time.OffsetDateTime;
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
@Tag(name = "legal_entities", description = "Legal Entity")
public interface LegalEntityApi {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  /**
   * POST /legalEntity : Add a new legalEntity to the store
   * Add a new legalEntity to the store
   *
   * @param legalEntity Create a new legalEntity in the store (required)
   * @return Successful operation (status code 200)
   *         or Invalid input (status code 405)
   */
  @Operation(
    operationId = "addLegalEntity",
    summary = "Add a new legalEntity to the store",
    description = "Add a new legalEntity to the store",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = LegalEntity.class)
          )
        }
      ),
      @ApiResponse(responseCode = "405", description = "Invalid input")
    },
    security = {
      @SecurityRequirement(
        name = "registry_auth",
        scopes = { "write:legalEntitys", "read:legalEntitys" }
      )
    }
  )
  @RequestMapping(
    method = RequestMethod.POST,
    value = "/legalEntity",
    produces = { "application/json" },
    consumes = { "application/json" }
  )
  default ResponseEntity<LegalEntity> addLegalEntity(
    @Parameter(
      name = "LegalEntity",
      description = "Create a new legalEntity in the store",
      required = true
    ) @Valid @RequestBody LegalEntity legalEntity
  ) {
    // getRequest()
    //   .ifPresent(
    //     request -> {
    //       for (MediaType mediaType : MediaType.parseMediaTypes(
    //         request.getHeader("Accept")
    //       )) {
    //         if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
    //           String exampleString =
    //             "{ \"oemSerialNumber\" : \"oemSerialNumber\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"type\" : { \"photoUrl\" : \"https://openapi-generator.tech\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"modelNumber\" : \"modelNumber\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"mtow\" : 6.0274563, \"manufacturer\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"validity\" : { \"till\" : \"2000-01-23T04:56:07.000+00:00\", \"from\" : \"2000-01-23T04:56:07.000+00:00\" }, \"legalEntity\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"name\" : \"name\", \"cin\" : \"cin\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"regdAddress\" : { \"city\" : \"Mumbai\", \"pinCode\" : 172074.45705867198, \"line3\" : \"Bandra West\", \"line2\" : \"Landmark\", \"line1\" : \"123 ABC Housing Society\" }, \"gstin\" : \"gstin\" } }, \"supportedOperationCategories\" : [ null, null ] } }";
    //           ApiUtil.setExampleResponse(request, "application/json", exampleString);

    //           break;
    //         }
    //       }
    //     }
    //   );
    try {
      Dao.LegalEntity le = LegalEntity.fromOa(legalEntity);
      Dao.LegalEntity lec = Dao.LegalEntity.create(
        DaoInstance.getInstance().getSession(),
        le
      );
      return ResponseEntity.ok(LegalEntity.toOa(lec));
    } catch (Exception e) {
      System.err.println("Exception: " + e.toString());
      e.printStackTrace(System.err);
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  /**
   * DELETE /legalEntity/{legalEntityId} : Deletes a legalEntity
   *
   *
   * @param legalEntityId LegalEntity id to delete (required)
   * @param apiKey  (optional)
   * @return Invalid legalEntity value (status code 400)
   */
  @Operation(
    operationId = "deleteLegalEntity",
    summary = "Deletes a legalEntity",
    description = "",
    responses = {
      @ApiResponse(responseCode = "400", description = "Invalid legalEntity value")
    },
    security = {
      @SecurityRequirement(
        name = "registry_auth",
        scopes = { "write:legalEntitys", "read:legalEntitys" }
      )
    }
  )
  @RequestMapping(method = RequestMethod.DELETE, value = "/legalEntity/{legalEntityId}")
  default ResponseEntity<Void> deleteLegalEntity(
    @Parameter(
      name = "legalEntityId",
      description = "LegalEntity id to delete",
      required = true,
      in = ParameterIn.PATH
    ) @PathVariable("legalEntityId") UUID legalEntityId
  ) {
    Dao.LegalEntity.delete(DaoInstance.getInstance().getSession(), legalEntityId);
    return ResponseEntity.ok().build();
    // return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  /**
   * GET /legalEntity/find : Finds LegalEntitys
   *
   * @return successful operation (status code 200)
   *         or Invalid value (status code 400)
   */
  @Operation(
    operationId = "findLegalEntitys",
    summary = "Finds LegalEntitys",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = LegalEntity.class))
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid value")
    },
    security = {
      @SecurityRequirement(
        name = "registry_auth",
        scopes = { "write:legalEntitys", "read:legalEntitys" }
      )
    }
  )
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/legalEntity/find",
    produces = { "application/json" }
  )
  default ResponseEntity<List<LegalEntity>> findLegalEntitys() {
    // getRequest()
    //   .ifPresent(
    //     request -> {
    //       for (MediaType mediaType : MediaType.parseMediaTypes(
    //         request.getHeader("Accept")
    //       )) {
    //         if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
    //           String exampleString =
    //             "[ { \"oemSerialNumber\" : \"oemSerialNumber\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"type\" : { \"photoUrl\" : \"https://openapi-generator.tech\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"modelNumber\" : \"modelNumber\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"mtow\" : 6.0274563, \"manufacturer\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"validity\" : { \"till\" : \"2000-01-23T04:56:07.000+00:00\", \"from\" : \"2000-01-23T04:56:07.000+00:00\" }, \"legalEntity\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"name\" : \"name\", \"cin\" : \"cin\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"regdAddress\" : { \"city\" : \"Mumbai\", \"pinCode\" : 172074.45705867198, \"line3\" : \"Bandra West\", \"line2\" : \"Landmark\", \"line1\" : \"123 ABC Housing Society\" }, \"gstin\" : \"gstin\" } }, \"supportedOperationCategories\" : [ null, null ] } }, { \"oemSerialNumber\" : \"oemSerialNumber\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"type\" : { \"photoUrl\" : \"https://openapi-generator.tech\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"modelNumber\" : \"modelNumber\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"mtow\" : 6.0274563, \"manufacturer\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"validity\" : { \"till\" : \"2000-01-23T04:56:07.000+00:00\", \"from\" : \"2000-01-23T04:56:07.000+00:00\" }, \"legalEntity\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"name\" : \"name\", \"cin\" : \"cin\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"regdAddress\" : { \"city\" : \"Mumbai\", \"pinCode\" : 172074.45705867198, \"line3\" : \"Bandra West\", \"line2\" : \"Landmark\", \"line1\" : \"123 ABC Housing Society\" }, \"gstin\" : \"gstin\" } }, \"supportedOperationCategories\" : [ null, null ] } } ]";
    //           ApiUtil.setExampleResponse(request, "application/json", exampleString);
    //           break;
    //         }
    //       }
    //     }
    //   );
    List<Dao.LegalEntity> les = Dao.LegalEntity.getAll(
      DaoInstance.getInstance().getSession()
    );
    List<LegalEntity> leso = les
      .stream()
      .map(x -> in.ispirt.pushpaka.registry.models.LegalEntity.toOa(x))
      .collect(Collectors.toList());
    return ResponseEntity.ok(leso);
  }

  /**
   * GET /legalEntity/{legalEntityId} : Find legalEntity by ID
   * Returns a single legalEntity
   *
   * @param legalEntityId ID of legalEntity to return (required)
   * @return successful operation (status code 200)
   *         or Invalid ID supplied (status code 400)
   *         or LegalEntity not found (status code 404)
   */
  @Operation(
    operationId = "getLegalEntityById",
    summary = "Find legalEntity by ID",
    description = "Returns a single legalEntity",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = LegalEntity.class)
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
      @ApiResponse(responseCode = "404", description = "LegalEntity not found")
    },
    security = {
      @SecurityRequirement(
        name = "registry_auth",
        scopes = { "write:legalEntitys", "read:legalEntitys" }
      )
    }
  )
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/legalEntity/{legalEntityId}",
    produces = { "application/json" }
  )
  default ResponseEntity<LegalEntity> getLegalEntityById(
    @Parameter(
      name = "legalEntityId",
      description = "ID of legalEntity to return",
      required = true,
      in = ParameterIn.PATH
    ) @PathVariable("legalEntityId") UUID legalEntityId
  ) {
    // getRequest()
    //   .ifPresent(
    //     request -> {
    //       for (MediaType mediaType : MediaType.parseMediaTypes(
    //         request.getHeader("Accept")
    //       )) {
    //         if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
    //           String exampleString =
    //             "{ \"oemSerialNumber\" : \"oemSerialNumber\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"type\" : { \"photoUrl\" : \"https://openapi-generator.tech\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"modelNumber\" : \"modelNumber\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"mtow\" : 6.0274563, \"manufacturer\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"validity\" : { \"till\" : \"2000-01-23T04:56:07.000+00:00\", \"from\" : \"2000-01-23T04:56:07.000+00:00\" }, \"legalEntity\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"name\" : \"name\", \"cin\" : \"cin\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"regdAddress\" : { \"city\" : \"Mumbai\", \"pinCode\" : 172074.45705867198, \"line3\" : \"Bandra West\", \"line2\" : \"Landmark\", \"line1\" : \"123 ABC Housing Society\" }, \"gstin\" : \"gstin\" } }, \"supportedOperationCategories\" : [ null, null ] } }";
    //           ApiUtil.setExampleResponse(request, "application/json", exampleString);
    //           break;
    //         }
    //       }
    //     }
    //   );
    Dao.LegalEntity le = Dao.LegalEntity.get(
      DaoInstance.getInstance().getSession(),
      legalEntityId
    );
    return ResponseEntity.ok(in.ispirt.pushpaka.registry.models.LegalEntity.toOa(le));
  }

  /**
   * PUT /legalEntity/{legalEntityId} : Updates a legalEntity in the store with form data
   *
   *
   * @param legalEntityId ID of legalEntity that needs to be updated (required)
   * @param name Name of legalEntity that needs to be updated (optional)
   * @param status Status of legalEntity that needs to be updated (optional)
   * @return Invalid input (status code 405)
   */
  @Operation(
    operationId = "updateLegalEntity",
    summary = "Updates a legalEntity in the store",
    description = "",
    responses = { @ApiResponse(responseCode = "405", description = "Invalid input") },
    security = {
      @SecurityRequirement(
        name = "registry_auth",
        scopes = { "write:legalEntitys", "read:legalEntitys" }
      )
    }
  )
  @RequestMapping(method = RequestMethod.PUT, value = "/legalEntity/{legalEntityId}")
  default ResponseEntity<LegalEntity> updateLegalEntity(
    @Parameter(
      name = "legalEntityId",
      description = "ID of legalEntity that needs to be updated",
      required = true,
      in = ParameterIn.PATH
    ) @PathVariable("legalEntityId") UUID legalEntityId,
    @Parameter(
      name = "LegalEntity",
      description = "Create a new legalEntity in the store",
      required = true
    ) @Valid @RequestBody LegalEntity legalEntity
  ) {
    Dao.LegalEntity le = Dao.LegalEntity.update(
      DaoInstance.getInstance().getSession(),
      legalEntityId,
      LegalEntity.fromOa(legalEntity)
    );
    return ResponseEntity.ok(LegalEntity.toOa(le));
    // return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
