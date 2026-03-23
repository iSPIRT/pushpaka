package in.ispirt.pushpaka.authorisation.utils;

import com.authzed.api.v1.Core;
import com.authzed.api.v1.Core.ObjectReference;
import com.authzed.api.v1.Core.Relationship;
import com.authzed.api.v1.Core.RelationshipUpdate;
import com.authzed.api.v1.Core.RelationshipUpdate.Operation;
import com.authzed.api.v1.Core.SubjectReference;
import com.authzed.api.v1.ExperimentalServiceGrpc;
import com.authzed.api.v1.ExperimentalServiceOuterClass;
import com.authzed.api.v1.ExperimentalServiceOuterClass.BulkExportRelationshipsResponse;
import com.authzed.api.v1.PermissionService;
import com.authzed.api.v1.PermissionService.CheckPermissionResponse;
import com.authzed.api.v1.PermissionService.LookupResourcesResponse;
import com.authzed.api.v1.PermissionsServiceGrpc;
import com.authzed.api.v1.SchemaServiceGrpc;
import com.authzed.api.v1.SchemaServiceOuterClass.ReadSchemaRequest;
import com.authzed.api.v1.SchemaServiceOuterClass.ReadSchemaResponse;
import com.authzed.api.v1.SchemaServiceOuterClass.WriteSchemaRequest;
import com.authzed.grpcutil.BearerToken;
import com.google.protobuf.Descriptors;
import in.ispirt.pushpaka.authorisation.Permission;
import in.ispirt.pushpaka.authorisation.RelationshipType;
import in.ispirt.pushpaka.authorisation.ResourceType;
import in.ispirt.pushpaka.authorisation.SubjectType;
import io.grpc.ManagedChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SpicedbClient {
  static final Operation OPERATION_CREATE = RelationshipUpdate.Operation.OPERATION_CREATE;
  static final Operation OPERATION_TOUCH = RelationshipUpdate.Operation.OPERATION_TOUCH;
  static final Operation OPERATION_DELETE = RelationshipUpdate.Operation.OPERATION_DELETE;

  public static final String SPICEDDB_TARGET = "localhost:50051";
  public static final String SPICEDB_TOKEN = "somerandomkeyhere";
  public static final String SPICEDDB_PERMISSION_FILE =
    "src/main/resources/spicedb_permissions.txt";

  private static SpicedbClient instance;
  ManagedChannel channel;
  PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsService;
  SchemaServiceGrpc.SchemaServiceBlockingStub schemaService;
  ExperimentalServiceGrpc.ExperimentalServiceBlockingStub experimentalService;

  public ExperimentalServiceGrpc.ExperimentalServiceBlockingStub getExperimentalService() {
    return experimentalService;
  }

  public void setExperimentalService(
    ExperimentalServiceGrpc.ExperimentalServiceBlockingStub experimentalService
  ) {
    this.experimentalService = experimentalService;
  }

  public ManagedChannel getChannel() {
    return channel;
  }

  public void setChannel(ManagedChannel channel) {
    this.channel = channel;
  }

  public PermissionsServiceGrpc.PermissionsServiceBlockingStub getPermissionsService() {
    return permissionsService;
  }

  public void setPermissionsService(
    PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsService
  ) {
    this.permissionsService = permissionsService;
  }

  public SchemaServiceGrpc.SchemaServiceBlockingStub getSchemaService() {
    return schemaService;
  }

  public void setSchemaService(
    SchemaServiceGrpc.SchemaServiceBlockingStub schemaService
  ) {
    this.schemaService = schemaService;
  }

  private SpicedbClient(ManagedChannel channel, String token) {
    setChannel(channel);

    permissionsService =
      PermissionsServiceGrpc
        .newBlockingStub(channel)
        .withCallCredentials(new BearerToken(token));

    setPermissionsService(permissionsService);

    schemaService =
      SchemaServiceGrpc
        .newBlockingStub(channel)
        .withCallCredentials(new BearerToken(token));

    setSchemaService(schemaService);

    experimentalService =
      ExperimentalServiceGrpc
        .newBlockingStub(channel)
        .withCallCredentials(new BearerToken(token));

    setExperimentalService(experimentalService);
  }

  public static synchronized SpicedbClient getInstance(
    ManagedChannel channel,
    String token
  ) {
    if (instance == null) {
      instance = new SpicedbClient(channel, token);
    }
    return instance;
  }

  public String writeRelationship(
    RelationshipType relationType,
    String resourceID,
    ResourceType resourceType,
    String subjectID,
    SubjectType subjectType
  ) {
    // Write relationship
    PermissionService.WriteRelationshipsRequest relRequest = PermissionService
      .WriteRelationshipsRequest.newBuilder()
      .addUpdates(
        RelationshipUpdate
          .newBuilder()
          .setOperation(OPERATION_TOUCH)
          .setRelationship(
            Relationship
              .newBuilder()
              .setResource(
                ObjectReference
                  .newBuilder()
                  .setObjectType(resourceType.getResourceType())
                  .setObjectId(resourceID)
                  .build()
              )
              .setRelation(relationType.getRelationshipType())
              .setSubject(
                SubjectReference
                  .newBuilder()
                  .setObject(
                    ObjectReference
                      .newBuilder()
                      .setObjectType(subjectType.getSubjectType())
                      .setObjectId(subjectID)
                      .build()
                  )
                  .build()
              )
              .build()
          )
          .build()
      )
      .build();

    PermissionService.WriteRelationshipsResponse response;

    try {
      response = this.getPermissionsService().writeRelationships(relRequest);
      Thread.sleep(6000);
    } catch (Exception e) {
      return "";
    }

    return response.getWrittenAt().getToken();
  }

  public String deleteRelationship(
    RelationshipType relationType,
    String resourceID,
    ResourceType resourceType,
    String subjectID,
    SubjectType subjectType
  ) {
    // Write relationship
    PermissionService.WriteRelationshipsRequest relRequest = PermissionService
      .WriteRelationshipsRequest.newBuilder()
      .addUpdates(
        RelationshipUpdate
          .newBuilder()
          .setOperation(OPERATION_DELETE)
          .setRelationship(
            Relationship
              .newBuilder()
              .setResource(
                ObjectReference
                  .newBuilder()
                  .setObjectType(resourceType.getResourceType())
                  .setObjectId(resourceID)
                  .build()
              )
              .setRelation(relationType.getRelationshipType())
              .setSubject(
                SubjectReference
                  .newBuilder()
                  .setObject(
                    ObjectReference
                      .newBuilder()
                      .setObjectType(subjectType.getSubjectType())
                      .setObjectId(subjectID)
                      .build()
                  )
                  .build()
              )
              .build()
          )
          .build()
      )
      .build();

    PermissionService.WriteRelationshipsResponse response;

    try {
      response = this.getPermissionsService().writeRelationships(relRequest);
      Thread.sleep(6000);
    } catch (Exception e) {
      return "";
    }

    return response.getWrittenAt().getToken();
  }

  public boolean checkPermission(
    Permission permission,
    ResourceType resourceType,
    String resourceID,
    SubjectType subjectType,
    String subjectID
  ) {
    PermissionService.CheckPermissionRequest request = PermissionService
      .CheckPermissionRequest.newBuilder()
      .setConsistency(
        PermissionService.Consistency.newBuilder().setMinimizeLatency(true).build()
      )
      .setResource(
        Core
          .ObjectReference.newBuilder()
          .setObjectType(resourceType.getResourceType())
          .setObjectId(resourceID)
          .build()
      )
      .setSubject(
        Core
          .SubjectReference.newBuilder()
          .setObject(
            Core
              .ObjectReference.newBuilder()
              .setObjectType(subjectType.getSubjectType())
              .setObjectId(subjectID)
              .build()
          )
          .build()
      )
      .setPermission(permission.getPermission())
      .build();

    try {
      PermissionService.CheckPermissionResponse response =
        this.getPermissionsService().checkPermission(request);
      System.out.println(
        "result: " + response.getPermissionship().getValueDescriptor().getName()
      );

      if (
        CheckPermissionResponse.Permissionship.PERMISSIONSHIP_HAS_PERMISSION.getNumber() ==
        (response.getPermissionship().getNumber())
      ) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
  }

  public boolean writeSchema(String filename) {
    boolean isSuccess = false;
    try {
      Path p = Path.of(filename);
      String schema = Files.readString(p);
      WriteSchemaRequest wsr = WriteSchemaRequest.newBuilder().setSchema(schema).build();
      this.getSchemaService().writeSchema(wsr);

      isSuccess = true;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return isSuccess;
  }

  public void shutdownChannel() throws InterruptedException {
    try {
      this.getChannel().shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    } catch (InterruptedException exception) {
      exception.printStackTrace();
    }
  }

  public void shutdownChannel(ManagedChannel channel) throws InterruptedException {
    try {
      channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    } catch (InterruptedException exception) {
      exception.printStackTrace();
    }
  }

  public String readSchema() {
    String schemaText = null;

    ReadSchemaRequest readRequest = ReadSchemaRequest.newBuilder().build();

    ReadSchemaResponse readResponse = this.getSchemaService().readSchema(readRequest);

    if (readResponse != null) {
      schemaText = readResponse.getSchemaText();
    }

    return schemaText;
  }

  public Set<String> lookupResources(
    Permission permission,
    ResourceType resourceType,
    SubjectType subjectType,
    String subjectID
  ) {
    Set<String> resources = new HashSet<>();
    PermissionService.LookupResourcesRequest request = PermissionService
      .LookupResourcesRequest.newBuilder()
      .setConsistency(
        PermissionService.Consistency.newBuilder().setMinimizeLatency(true).build()
      )
      .setResourceObjectType(resourceType.getResourceType())
      .setPermission(permission.getPermission())
      .setSubject(
        Core
          .SubjectReference.newBuilder()
          .setObject(
            Core
              .ObjectReference.newBuilder()
              .setObjectType(subjectType.getSubjectType())
              .setObjectId(subjectID)
              .build()
          )
          .build()
      )
      .build();

    try {
      Iterator<LookupResourcesResponse> response =
        this.getPermissionsService().lookupResources(request);
      response.forEachRemaining(
        lookupResourcesResponse -> {
          resources.add(lookupResourcesResponse.getResourceObjectId());
        }
      );
    } catch (Exception exception) {
      exception.printStackTrace();
    }

    return resources;
  }

  public Set<String> lookupResources(
    RelationshipType relationshipType,
    ResourceType resourceType,
    SubjectType subjectType,
    String subjectID
  ) {
    Set<String> resources = new HashSet<>();
    PermissionService.LookupResourcesRequest request = PermissionService
      .LookupResourcesRequest.newBuilder()
      .setConsistency(
        PermissionService.Consistency.newBuilder().setMinimizeLatency(true).build()
      )
      .setResourceObjectType(resourceType.getResourceType())
      .setPermission(relationshipType.getRelationshipType())
      .setSubject(
        Core
          .SubjectReference.newBuilder()
          .setObject(
            Core
              .ObjectReference.newBuilder()
              .setObjectType(subjectType.getSubjectType())
              .setObjectId(subjectID)
              .build()
          )
          .build()
      )
      .build();

    try {
      Iterator<LookupResourcesResponse> response = (Iterator<LookupResourcesResponse>) (
        this.getPermissionsService().lookupResources(request)
      );

      response.forEachRemaining(
        lookupResourcesResponse -> {
          resources.add(lookupResourcesResponse.getResourceObjectId());
        }
      );
    } catch (Exception exception) {
      exception.printStackTrace();
    }

    return resources;
  }

  public Set<List<Core.Relationship>> exportRelationships() {
    Set<List<Core.Relationship>> resources = new HashSet<>();

    ExperimentalServiceOuterClass.BulkExportRelationshipsRequest request = ExperimentalServiceOuterClass
      .BulkExportRelationshipsRequest.newBuilder()
      .build();

    try {
      Iterator<BulkExportRelationshipsResponse> response =
        this.experimentalService.bulkExportRelationships(request);

      response.forEachRemaining(
        bulkExportRelationshipsResponse -> {
          resources.add(bulkExportRelationshipsResponse.getRelationshipsList());
        }
      );
    } catch (Exception exception) {
      exception.printStackTrace();
    }

    return resources;
  }
}
