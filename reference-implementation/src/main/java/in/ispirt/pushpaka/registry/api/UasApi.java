/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.6.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package in.ispirt.pushpaka.registry.api;

import in.ispirt.pushpaka.dao.Dao;
import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.models.Uas;
import in.ispirt.pushpaka.registry.utils.DaoException;
import in.ispirt.pushpaka.utils.Logging;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

@Generated(
  value = "org.openapitools.codegen.languages.SpringCodegen",
  date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]"
)
@Validated
@Tag(name = "uas", description = "UAS")
public interface UasApi {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  /**
   * POST /uas : Add a new uas to the store
   * Add a new uas to the store
   *
   * @param uas Create a new uas in the store (required)
   * @return Successful operation (status code 200)
   *         or Invalid input (status code 405)
   */
  @Operation(
    operationId = "addUas",
    summary = "Add a new uas to the store",
    description = "Add a new uas to the store",
    tags = { "uas" },
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Uas.class)
          )
        }
      ),
      @ApiResponse(responseCode = "405", description = "Invalid input")
    },
    security = { @SecurityRequirement(name = "jwt") }
  )
  @RequestMapping(
    method = RequestMethod.POST,
    value = "/uas",
    produces = { "application/json" },
    consumes = { "application/json" }
  )
  default ResponseEntity<Uas> addUas(
    @Parameter(
      name = "Uas",
      description = "Create a new uas in the store",
      required = true
    ) @Valid @RequestBody Uas uas
  ) {
    try {
      Dao.Uas le = Uas.fromOa(uas);
      Dao.Uas lec = Dao.Uas.create(DaoInstance.getInstance().getSession(), le);
      return ResponseEntity.ok(Uas.toOa(lec));
    } catch (ConstraintViolationException e) {
      System.err.println("Exception: " + e.toString());
      e.printStackTrace(System.err);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
   * DELETE /uas/{uasId} : Deletes a uas
   *
   *
   * @param uasId Uas id to delete (required)
   * @param apiKey  (optional)
   * @return Invalid uas value (status code 400)
   */
  @Operation(
    operationId = "deleteUas",
    summary = "Deletes a uas",
    description = "",
    tags = { "uas" },
    responses = { @ApiResponse(responseCode = "400", description = "Invalid uas value") },
    security = { @SecurityRequirement(name = "jwt") }
  )
  @RequestMapping(method = RequestMethod.DELETE, value = "/uas/{uasId}")
  default ResponseEntity<Void> deleteUas(
    @Parameter(
      name = "uasId",
      description = "Uas id to delete",
      required = true,
      in = ParameterIn.PATH
    ) @PathVariable("uasId") UUID uasId,
    @Parameter(
      name = "api_key",
      description = "",
      in = ParameterIn.HEADER
    ) @RequestHeader(value = "api_key", required = false) String apiKey
  ) {
    Dao.Uas.delete(DaoInstance.getInstance().getSession(), uasId);
    return ResponseEntity.ok().build();
  }

  /**
   * GET /uas/find : Finds Uass
   *
   * @return successful operation (status code 200)
   *         or Invalid value (status code 400)
   */
  @Operation(
    operationId = "findUass",
    summary = "Finds Uass",
    tags = { "uas" },
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Uas.class))
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid value")
    },
    security = { @SecurityRequirement(name = "jwt") }
  )
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/uas/find",
    produces = { "application/json" }
  )
  default ResponseEntity<List<Uas>> findUass() {
    List<Dao.Uas> les = Dao.Uas.getAll(DaoInstance.getInstance().getSession());
    List<Uas> leso = les
      .stream()
      .map(x -> in.ispirt.pushpaka.models.Uas.toOa(x))
      .collect(Collectors.toList());
    return ResponseEntity.ok(leso);
  }

  /**
   * GET /uas/{uasId} : Find uas by ID
   * Returns a single uas
   *
   * @param uasId ID of uas to return (required)
   * @return successful operation (status code 200)
   *         or Invalid ID supplied (status code 400)
   *         or Uas not found (status code 404)
   */
  @Operation(
    operationId = "getUasById",
    summary = "Find uas by ID",
    description = "Returns a single uas",
    tags = { "uas" },
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Uas.class)
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
      @ApiResponse(responseCode = "404", description = "Uas not found")
    },
    security = { @SecurityRequirement(name = "jwt") }
  )
  @RequestMapping(
    method = RequestMethod.GET,
    value = "/uas/{uasId}",
    produces = { "application/json" }
  )
  default ResponseEntity<Uas> getUasById(
    @Parameter(
      name = "uasId",
      description = "ID of uas to return",
      required = true,
      in = ParameterIn.PATH
    ) @PathVariable("uasId") UUID uasId
  ) {
    Dao.Uas le = Dao.Uas.get(DaoInstance.getInstance().getSession(), uasId);
    return ResponseEntity.ok(in.ispirt.pushpaka.models.Uas.toOa(le));
  }

  /**
   * PUT /uas : Update an existing uas
   * Update an existing uas by Id
   *
   * @param uas Update an existent uas in the store (required)
   * @return Successful operation (status code 200)
   *         or Invalid ID supplied (status code 400)
   *         or Uas not found (status code 404)
   *         or Validation exception (status code 405)
   */
  @Operation(
    operationId = "updateUas",
    summary = "Update an existing uas",
    description = "Update an existing uas by Id",
    tags = { "uas" },
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = {
          @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Uas.class)
          )
        }
      ),
      @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
      @ApiResponse(responseCode = "404", description = "Uas not found"),
      @ApiResponse(responseCode = "405", description = "Validation exception")
    },
    security = { @SecurityRequirement(name = "jwt") }
  )
  @RequestMapping(
    method = RequestMethod.PUT,
    value = "/uas",
    produces = { "application/json" },
    consumes = { "application/json" }
  )
  default ResponseEntity<Uas> updateUas(
    @Parameter(
      name = "Uas",
      description = "Update an existent uas in the store",
      required = true
    ) @Valid @RequestBody Uas uas
  ) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
