/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.6.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package in.ispirt.pushpaka.registry.api;

import in.ispirt.pushpaka.registry.dao.Dao;
import in.ispirt.pushpaka.registry.dao.DaoInstance;
import in.ispirt.pushpaka.registry.models.CivilAviationAuthority;
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
@Tag(name = "civilAviationAuthorities", description = "CivilAviationAuthorities")
public interface CivilAviationAuthorityApi {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  /**
   * POST /civilAviationAuthority : Add a new civilAviationAuthority to the store
   * Add a new civilAviationAuthority to the store
   *
   * @param civilAviationAuthority Create a new civilAviationAuthority in the store (required)
   * @return Successful operation (status code 200)
   *         or Invalid input (status code 405)
   */
  @Operation(
    operationId = "addCivilAviationAuthority",
    summary = "Add a new civilAviationAuthority to the store",
    description = "Add a new civilAviationAuthority to the store",
    tags = { "civilAviationAuthority" },
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CivilAviationAuthority.class)
          )
        }
      ),
      @ApiResponse(responseCode = "405", description = "Invalid input")
    },
    security = {
      @SecurityRequirement(
        name = "registry_auth",
        scopes = { "write:civilAviationAuthorities", "read:civilAviationAuthorities" }
      )
    }
  )
  @RequestMapping(
    method = RequestMethod.POST,
    value = "/civilAviationAuthority",
    produces = { "application/json" },
    consumes = { "application/json" }
  )
  default ResponseEntity<CivilAviationAuthority> addCivilAviationAuthority(
    @Parameter(
      name = "CivilAviationAuthority",
      description = "Create a new civilAviationAuthority in the store",
      required = true
    ) @Valid @RequestBody CivilAviationAuthority civilAviationAuthority
  ) {
    try {
      System.out.println(
        "Create CivilAviationAuthority " + civilAviationAuthority.toString()
      );
      Dao.CivilAviationAuthority mm = Dao.CivilAviationAuthority.create(
        DaoInstance.getInstance().getSession(),
        CivilAviationAuthority.fromOa(civilAviationAuthority)
      );
      return ResponseEntity.ok(CivilAviationAuthority.toOa(mm));
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

  /**
   * DELETE /civilAviationAuthority/{civilAviationAuthorityId} : Deletes a civilAviationAuthority
   *
   *
   * @param civilAviationAuthorityId CivilAviationAuthority id to delete (required)
   * @return Invalid civilAviationAuthority value (status code 400)
   */
  @Operation(
    operationId = "deleteCivilAviationAuthority",
    summary = "Deletes a civilAviationAuthority",
    description = "",
    tags = { "civilAviationAuthority" },
    responses = {
      @ApiResponse(
        responseCode = "400",
        description = "Invalid civilAviationAuthority value"
      )
    },
    security = {
      @SecurityRequirement(
        name = "registry_auth",
        scopes = { "write:civilAviationAuthorities", "read:civilAviationAuthorities" }
      )
    }
  )
  @RequestMapping(
    method = RequestMethod.DELETE,
    value = "/civilAviationAuthority/{civilAviationAuthorityId}"
  )
  default ResponseEntity<Void> deleteCivilAviationAuthority(
    @Parameter(
      name = "civilAviationAuthorityId",
      description = "CivilAviationAuthority id to delete",
      required = true,
      in = ParameterIn.PATH
    ) @PathVariable("civilAviationAuthorityId") UUID civilAviationAuthorityId
  ) {
    Dao.CivilAviationAuthority.delete(
      DaoInstance.getInstance().getSession(),
      civilAviationAuthorityId
    );
    return ResponseEntity.ok().build();
    // return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  /**
   * GET /civilAviationAuthority/find : Finds CivilAviationAuthorities
   *
   * @return successful operation (status code 200)
   *         or Invalid value (status code 400)
   */
  @Operation(
    operationId = "findCivilAviationAuthorities",
    summary = "Finds CivilAviationAuthorities",
    tags = { "civilAviationAuthority" },
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            array = @ArraySchema(
              schema = @Schema(implementation = CivilAviationAuthority.class)
            )
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid value")
    },
    security = {
      @SecurityRequirement(
        name = "registry_auth",
        scopes = { "write:civilAviationAuthorities", "read:civilAviationAuthorities" }
      )
    }
  )
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/civilAviationAuthority/find",
    produces = { "application/json" }
  )
  default ResponseEntity<List<CivilAviationAuthority>> findCivilAviationAuthorities() {
    // getRequest()
    //   .ifPresent(
    //     request -> {
    //       for (MediaType mediaType : MediaType.parseMediaTypes(
    //         request.getHeader("Accept")
    //       )) {
    //         if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
    //           String exampleString =
    //             "[ { \"oemSerialNumber\" : \"oemSerialNumber\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"type\" : { \"photoUrl\" : \"https://openapi-generator.tech\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"modelNumber\" : \"modelNumber\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"mtow\" : 6.0274563, \"civilAviationAuthority\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"validity\" : { \"till\" : \"2000-01-23T04:56:07.000+00:00\", \"from\" : \"2000-01-23T04:56:07.000+00:00\" }, \"civilAviationAuthority\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"name\" : \"name\", \"cin\" : \"cin\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"regdAddress\" : { \"city\" : \"Mumbai\", \"pinCode\" : 172074.45705867198, \"line3\" : \"Bandra West\", \"line2\" : \"Landmark\", \"line1\" : \"123 ABC Housing Society\" }, \"gstin\" : \"gstin\" } }, \"supportedOperationCategories\" : [ null, null ] } }, { \"oemSerialNumber\" : \"oemSerialNumber\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"type\" : { \"photoUrl\" : \"https://openapi-generator.tech\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"modelNumber\" : \"modelNumber\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"mtow\" : 6.0274563, \"civilAviationAuthority\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"validity\" : { \"till\" : \"2000-01-23T04:56:07.000+00:00\", \"from\" : \"2000-01-23T04:56:07.000+00:00\" }, \"civilAviationAuthority\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"name\" : \"name\", \"cin\" : \"cin\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"regdAddress\" : { \"city\" : \"Mumbai\", \"pinCode\" : 172074.45705867198, \"line3\" : \"Bandra West\", \"line2\" : \"Landmark\", \"line1\" : \"123 ABC Housing Society\" }, \"gstin\" : \"gstin\" } }, \"supportedOperationCategories\" : [ null, null ] } } ]";
    //           ApiUtil.setExampleResponse(request, "application/json", exampleString);
    //           break;
    //         }
    //       }
    //     }
    //   );
    List<Dao.CivilAviationAuthority> les = Dao.CivilAviationAuthority.getAll(
      DaoInstance.getInstance().getSession()
    );
    List<CivilAviationAuthority> leso = les
      .stream()
      .map(x -> in.ispirt.pushpaka.registry.models.CivilAviationAuthority.toOa(x))
      .collect(Collectors.toList());
    return ResponseEntity.ok(leso);
  }

  /**
   * GET /civilAviationAuthority/{civilAviationAuthorityId} : Find civilAviationAuthority by ID
   * Returns a single civilAviationAuthority
   *
   * @param civilAviationAuthorityId ID of civilAviationAuthority to return (required)
   * @return successful operation (status code 200)
   *         or Invalid ID supplied (status code 400)
   *         or CivilAviationAuthority not found (status code 404)
   */
  @Operation(
    operationId = "getCivilAviationAuthorityById",
    summary = "Find civilAviationAuthority by ID",
    description = "Returns a single civilAviationAuthority",
    tags = { "civilAviationAuthority" },
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CivilAviationAuthority.class)
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
      @ApiResponse(responseCode = "404", description = "CivilAviationAuthority not found")
    },
    security = {
      @SecurityRequirement(
        name = "registry_auth",
        scopes = { "write:civilAviationAuthorities", "read:civilAviationAuthorities" }
      )
    }
  )
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/civilAviationAuthority/{civilAviationAuthorityId}",
    produces = { "application/json" }
  )
  default ResponseEntity<CivilAviationAuthority> getCivilAviationAuthorityById(
    @Parameter(
      name = "civilAviationAuthorityId",
      description = "ID of civilAviationAuthority to return",
      required = true,
      in = ParameterIn.PATH
    ) @PathVariable("civilAviationAuthorityId") UUID civilAviationAuthorityId
  ) {
    // getRequest()
    //   .ifPresent(
    //     request -> {
    //       for (MediaType mediaType : MediaType.parseMediaTypes(
    //         request.getHeader("Accept")
    //       )) {
    //         if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
    //           String exampleString =
    //             "{ \"oemSerialNumber\" : \"oemSerialNumber\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"type\" : { \"photoUrl\" : \"https://openapi-generator.tech\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"modelNumber\" : \"modelNumber\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"mtow\" : 6.0274563, \"civilAviationAuthority\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"validity\" : { \"till\" : \"2000-01-23T04:56:07.000+00:00\", \"from\" : \"2000-01-23T04:56:07.000+00:00\" }, \"civilAviationAuthority\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"name\" : \"name\", \"cin\" : \"cin\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"regdAddress\" : { \"city\" : \"Mumbai\", \"pinCode\" : 172074.45705867198, \"line3\" : \"Bandra West\", \"line2\" : \"Landmark\", \"line1\" : \"123 ABC Housing Society\" }, \"gstin\" : \"gstin\" } }, \"supportedOperationCategories\" : [ null, null ] } }";
    //           ApiUtil.setExampleResponse(request, "application/json", exampleString);
    //           break;
    //         }
    //       }
    //     }
    //   );
    Dao.CivilAviationAuthority le = Dao.CivilAviationAuthority.get(
      DaoInstance.getInstance().getSession(),
      civilAviationAuthorityId
    );
    return ResponseEntity.ok(
      in.ispirt.pushpaka.registry.models.CivilAviationAuthority.toOa(le)
    );
  }

  /**
   * PUT /civilAviationAuthority/{civilAviationAuthorityId} : Updates a civilAviationAuthority in the store with form data
   *
   *
   * @param civilAviationAuthorityId ID of civilAviationAuthority that needs to be updated (required)
   * @param name Name of civilAviationAuthority that needs to be updated (optional)
   * @param status Status of civilAviationAuthority that needs to be updated (optional)
   * @return Invalid input (status code 405)
   */
  @Operation(
    operationId = "updateCivilAviationAuthority",
    summary = "Updates a civilAviationAuthority in the store",
    description = "",
    tags = { "civilAviationAuthority" },
    responses = { @ApiResponse(responseCode = "405", description = "Invalid input") },
    security = {
      @SecurityRequirement(
        name = "registry_auth",
        scopes = { "write:civilAviationAuthorities", "read:civilAviationAuthorities" }
      )
    }
  )
  @RequestMapping(
    method = RequestMethod.PUT,
    value = "/civilAviationAuthority/{civilAviationAuthorityId}"
  )
  default ResponseEntity<CivilAviationAuthority> updateCivilAviationAuthority(
    @Parameter(
      name = "civilAviationAuthorityId",
      description = "ID of civilAviationAuthority that needs to be updated",
      required = true,
      in = ParameterIn.PATH
    ) @PathVariable("civilAviationAuthorityId") UUID civilAviationAuthorityId,
    @Parameter(
      name = "CivilAviationAuthority",
      description = "Create a new civilAviationAuthority in the store",
      required = true
    ) @Valid @RequestBody CivilAviationAuthority civilAviationAuthority
  ) {
    Dao.CivilAviationAuthority le = Dao.CivilAviationAuthority.update(
      DaoInstance.getInstance().getSession(),
      civilAviationAuthorityId,
      CivilAviationAuthority.fromOa(civilAviationAuthority)
    );
    return ResponseEntity.ok(
      in.ispirt.pushpaka.registry.models.CivilAviationAuthority.toOa(le)
    );
    // return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
