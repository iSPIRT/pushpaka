/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.6.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package in.ispirt.pushpaka.registry.api;

import in.ispirt.pushpaka.registry.dao.Dao;
import in.ispirt.pushpaka.registry.dao.DaoInstance;
import in.ispirt.pushpaka.registry.models.Manufacturer;
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
    date = "2023-09-07T22:13:29.143496+05:30[Asia/Kolkata]")
@Validated
@Tag(name = "manufacturers", description = "Manufacturers")
public interface ManufacturerApi {
  default Optional
    <NativeWebRequest> getRequest() {
      return Optional.empty();
    }

    /**
   * POST /manufacturer : Add a new manufacturer to the store
   * Add a new manufacturer to the store
   *
   * @param manufacturer Create a new manufacturer in the store (required)
   * @return Successful operation (status code 200)
   *         or Invalid input (status code 405)
   */
    @Operation(
        operationId = "addManufacturer",
        summary = "Add a new manufacturer to the store",
        description = "Add a new manufacturer to the store",
        tags = {"manufacturer"},
        responses = { @ApiResponse(
                          responseCode = "200",
                          description = "Successful operation",
                          content = {
                            @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = Manufacturer.class)
                            )
                          })
                      ,
                          @ApiResponse(responseCode = "405", description = "Invalid input") },
        security = { @SecurityRequirement(
                         name = "registry_auth",
                         scopes = {"write:manufacturers", "read:manufacturers"}) })
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/manufacturer",
        produces = {"application/json"},
        consumes = {"application/json"})
    default ResponseEntity<Manufacturer>
    addManufacturer(
        @Parameter(
            name = "Manufacturer",
            description = "Create a new manufacturer in the store",
            required = true) @Valid @RequestBody Manufacturer manufacturer) {
      //       getRequest()
      //           .ifPresent(
      //               request -> {
      //                 for (MediaType mediaType : MediaType.parseMediaTypes(
      //                          request.getHeader("Accept"))) {
      //                   if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
      //                     String exampleString =
      //                         "{ \"oemSerialNumber\" : \"oemSerialNumber\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"type\" : { \"photoUrl\" : \"https://openapi-generator.tech\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"modelNumber\" : \"modelNumber\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"mtow\" : 6.0274563, \"manufacturer\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"validity\" : { \"till\" : \"2000-01-23T04:56:07.000+00:00\", \"from\" : \"2000-01-23T04:56:07.000+00:00\" }, \"manufacturer\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"name\" : \"name\", \"cin\" : \"cin\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"regdAddress\" : { \"city\" : \"Mumbai\", \"pinCode\" : 172074.45705867198, \"line3\" : \"Bandra West\", \"line2\" : \"Landmark\", \"line1\" : \"123 ABC Housing Society\" }, \"gstin\" : \"gstin\" } }, \"supportedOperationCategories\" : [ null, null ] } }";
      //                     ApiUtil.setExampleResponse(request, "application/json", exampleString);
      //
      //   }
      // });
      // break;
      // }
      try {
        System.out.println("Create Manufacturer " + manufacturer.toString());
        Dao.Manufacturer mm = Dao.Manufacturer.create(
            DaoInstance.getInstance().getSession(),
            Manufacturer.fromOa(manufacturer));
        return ResponseEntity.ok(Manufacturer.toOa(mm));
      } catch (Exception e) {
        System.err.println("Exception: " + e.toString());
        e.printStackTrace(System.err);
      }
      return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
   * DELETE /manufacturer/{manufacturerId} : Deletes a manufacturer
   *
   *
   * @param manufacturerId Manufacturer id to delete (required)
   * @return Invalid manufacturer value (status code 400)
   */
    @Operation(
        operationId = "deleteManufacturer",
        summary = "Deletes a manufacturer",
        description = "",
        tags = {"manufacturer"},
        responses = { @ApiResponse(responseCode = "400", description = "Invalid manufacturer value") },
        security = { @SecurityRequirement(
                         name = "registry_auth",
                         scopes = {"write:manufacturers", "read:manufacturers"}) })
    @RequestMapping(method = RequestMethod.DELETE, value = "/manufacturer/{manufacturerId}")
    default ResponseEntity<Void>
    deleteManufacturer(
        @Parameter(
            name = "manufacturerId",
            description = "Manufacturer id to delete",
            required = true,
            in = ParameterIn.PATH) @PathVariable("manufacturerId") UUID manufacturerId) {
      Dao.Manufacturer.delete(DaoInstance.getInstance().getSession(), manufacturerId);
      return ResponseEntity.ok().build();
      // return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
   * GET /manufacturer/find : Finds Manufacturers
   *
   * @return successful operation (status code 200)
   *         or Invalid value (status code 400)
   */
    @Operation(
        operationId = "findManufacturers",
        summary = "Finds Manufacturers",
        tags = {"manufacturer"},
        responses = { @ApiResponse(
                          responseCode = "200",
                          description = "successful operation",
                          content = {
                            @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = Manufacturer.class))
                            )
                          })
                      ,
                          @ApiResponse(responseCode = "400", description = "Invalid value") },
        security = { @SecurityRequirement(
                         name = "registry_auth",
                         scopes = {"write:manufacturers", "read:manufacturers"}) })
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/manufacturer/find",
        produces = {"application/json"})
    default ResponseEntity<List<Manufacturer>>
    findManufacturers() {
      // getRequest()
      //   .ifPresent(
      //     request -> {
      //       for (MediaType mediaType : MediaType.parseMediaTypes(
      //         request.getHeader("Accept")
      //       )) {
      //         if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
      //           String exampleString =
      //             "[ { \"oemSerialNumber\" : \"oemSerialNumber\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"type\" : { \"photoUrl\" : \"https://openapi-generator.tech\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"modelNumber\" : \"modelNumber\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"mtow\" : 6.0274563, \"manufacturer\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"validity\" : { \"till\" : \"2000-01-23T04:56:07.000+00:00\", \"from\" : \"2000-01-23T04:56:07.000+00:00\" }, \"manufacturer\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"name\" : \"name\", \"cin\" : \"cin\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"regdAddress\" : { \"city\" : \"Mumbai\", \"pinCode\" : 172074.45705867198, \"line3\" : \"Bandra West\", \"line2\" : \"Landmark\", \"line1\" : \"123 ABC Housing Society\" }, \"gstin\" : \"gstin\" } }, \"supportedOperationCategories\" : [ null, null ] } }, { \"oemSerialNumber\" : \"oemSerialNumber\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"type\" : { \"photoUrl\" : \"https://openapi-generator.tech\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"modelNumber\" : \"modelNumber\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"mtow\" : 6.0274563, \"manufacturer\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"validity\" : { \"till\" : \"2000-01-23T04:56:07.000+00:00\", \"from\" : \"2000-01-23T04:56:07.000+00:00\" }, \"manufacturer\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"name\" : \"name\", \"cin\" : \"cin\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"regdAddress\" : { \"city\" : \"Mumbai\", \"pinCode\" : 172074.45705867198, \"line3\" : \"Bandra West\", \"line2\" : \"Landmark\", \"line1\" : \"123 ABC Housing Society\" }, \"gstin\" : \"gstin\" } }, \"supportedOperationCategories\" : [ null, null ] } } ]";
      //           ApiUtil.setExampleResponse(request, "application/json", exampleString);
      //           break;
      //         }
      //       }
      //     }
      //   );
      List<Dao.Manufacturer> les = Dao.Manufacturer.getAll(
          DaoInstance.getInstance().getSession());
      List<Manufacturer> leso = les
                                    .stream()
                                    .map(x -> in.ispirt.pushpaka.registry.models.Manufacturer.toOa(x))
                                    .collect(Collectors.toList());
      return ResponseEntity.ok(leso);
    }

    /**
   * GET /manufacturer/{manufacturerId} : Find manufacturer by ID
   * Returns a single manufacturer
   *
   * @param manufacturerId ID of manufacturer to return (required)
   * @return successful operation (status code 200)
   *         or Invalid ID supplied (status code 400)
   *         or Manufacturer not found (status code 404)
   */
    @Operation(
        operationId = "getManufacturerById",
        summary = "Find manufacturer by ID",
        description = "Returns a single manufacturer",
        tags = {"manufacturer"},
        responses = { @ApiResponse(
                          responseCode = "200",
                          description = "successful operation",
                          content = {
                            @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = Manufacturer.class)
                            )
                          })
                      ,
                          @ApiResponse(responseCode = "400", description = "Invalid ID supplied"), @ApiResponse(responseCode = "404", description = "Manufacturer not found") },
        security = { @SecurityRequirement(
                         name = "registry_auth",
                         scopes = {"write:manufacturers", "read:manufacturers"}) })
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/manufacturer/{manufacturerId}",
        produces = {"application/json"})
    default ResponseEntity<Manufacturer>
    getManufacturerById(
        @Parameter(
            name = "manufacturerId",
            description = "ID of manufacturer to return",
            required = true,
            in = ParameterIn.PATH) @PathVariable("manufacturerId") UUID manufacturerId) {
      // getRequest()
      //   .ifPresent(
      //     request -> {
      //       for (MediaType mediaType : MediaType.parseMediaTypes(
      //         request.getHeader("Accept")
      //       )) {
      //         if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
      //           String exampleString =
      //             "{ \"oemSerialNumber\" : \"oemSerialNumber\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"type\" : { \"photoUrl\" : \"https://openapi-generator.tech\", \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"modelNumber\" : \"modelNumber\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"mtow\" : 6.0274563, \"manufacturer\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"validity\" : { \"till\" : \"2000-01-23T04:56:07.000+00:00\", \"from\" : \"2000-01-23T04:56:07.000+00:00\" }, \"manufacturer\" : { \"timestamps\" : { \"created\" : \"2000-01-23T04:56:07.000+00:00\", \"updated\" : \"2000-01-23T04:56:07.000+00:00\" }, \"name\" : \"name\", \"cin\" : \"cin\", \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"regdAddress\" : { \"city\" : \"Mumbai\", \"pinCode\" : 172074.45705867198, \"line3\" : \"Bandra West\", \"line2\" : \"Landmark\", \"line1\" : \"123 ABC Housing Society\" }, \"gstin\" : \"gstin\" } }, \"supportedOperationCategories\" : [ null, null ] } }";
      //           ApiUtil.setExampleResponse(request, "application/json", exampleString);
      //           break;
      //         }
      //       }
      //     }
      //   );
      Dao.Manufacturer le = Dao.Manufacturer.get(
          DaoInstance.getInstance().getSession(),
          manufacturerId);
      return ResponseEntity.ok(in.ispirt.pushpaka.registry.models.Manufacturer.toOa(le));
    }

    /**
   * PUT /manufacturer/{manufacturerId} : Updates a manufacturer in the store with form data
   *
   *
   * @param manufacturerId ID of manufacturer that needs to be updated (required)
   * @param name Name of manufacturer that needs to be updated (optional)
   * @param status Status of manufacturer that needs to be updated (optional)
   * @return Invalid input (status code 405)
   */
    @Operation(
        operationId = "updateManufacturer",
        summary = "Updates a manufacturer in the store",
        description = "",
        tags = {"manufacturer"},
        responses = { @ApiResponse(responseCode = "405", description = "Invalid input") },
        security = { @SecurityRequirement(
                         name = "registry_auth",
                         scopes = {"write:manufacturers", "read:manufacturers"}) })
    @RequestMapping(method = RequestMethod.PUT, value = "/manufacturer/{manufacturerId}")
    default ResponseEntity<Manufacturer>
    updateManufacturer(
        @Parameter(
            name = "manufacturerId",
            description = "ID of manufacturer that needs to be updated",
            required = true,
            in = ParameterIn.PATH) @PathVariable("manufacturerId") UUID manufacturerId,
        @Parameter(
            name = "Manufacturer",
            description = "Create a new manufacturer in the store",
            required = true) @Valid @RequestBody Manufacturer manufacturer) {
      Dao.Manufacturer le = Dao.Manufacturer.update(
          DaoInstance.getInstance().getSession(),
          manufacturerId,
          Manufacturer.fromOa(manufacturer));
      return ResponseEntity.ok(in.ispirt.pushpaka.registry.models.Manufacturer.toOa(le));
      // return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
