package in.ispirt.pushpaka.authorisation.utils;

import com.authzed.api.v1.Core;
import com.authzed.api.v1.Core.ObjectReference;
import com.authzed.api.v1.Core.Relationship;
import com.authzed.api.v1.Core.RelationshipUpdate;
import com.authzed.api.v1.Core.RelationshipUpdate.Operation;
import com.authzed.api.v1.Core.SubjectReference;
import com.authzed.api.v1.PermissionService;
import com.authzed.api.v1.PermissionService.CheckPermissionResponse;
import com.authzed.api.v1.PermissionService.LookupResourcesResponse;
import com.authzed.api.v1.PermissionsServiceGrpc;
import com.authzed.api.v1.SchemaServiceGrpc;
import com.authzed.api.v1.SchemaServiceOuterClass.ReadSchemaRequest;
import com.authzed.api.v1.SchemaServiceOuterClass.ReadSchemaResponse;
import com.authzed.api.v1.SchemaServiceOuterClass.WriteSchemaRequest;
import com.authzed.grpcutil.BearerToken;
import com.google.common.collect.Iterables;
import in.ispirt.pushpaka.authorisation.Permission;
import in.ispirt.pushpaka.authorisation.RelationshipType;
import in.ispirt.pushpaka.authorisation.ResourceType;
import in.ispirt.pushpaka.authorisation.SubjectType;
import io.grpc.ManagedChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class SpicedbClient {
  static final Operation OPERATION_CREATE = RelationshipUpdate.Operation.OPERATION_CREATE;
  static final Operation OPERATION_TOUCH = RelationshipUpdate.Operation.OPERATION_TOUCH;
  static final Operation OPERATION_DELETE = RelationshipUpdate.Operation.OPERATION_DELETE;

  public static final String SPICEDDB_TARGET = "localhost:50051";
  public static final String SPICEDB_TOKEN = "somerandomkeyhere";
  public static final String SPICEDDB_PERMISSION_FILE =
    "src/main/resources/spicedb_permissions.txt";

  ManagedChannel channel;

  public ManagedChannel getChannel() {
    return channel;
  }

  public void setChannel(ManagedChannel channel) {
    this.channel = channel;
  }

  PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsService;
  SchemaServiceGrpc.SchemaServiceBlockingStub schemaService;

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

  private static SpicedbClient instance;

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

  public void writeSchema(String filename) {
    try {
      Path p = Path.of(filename);
      String schema = Files.readString(p);
      WriteSchemaRequest wsr = WriteSchemaRequest.newBuilder().setSchema(schema).build();
      this.getSchemaService().writeSchema(wsr);
    } catch (Exception e) {
      e.printStackTrace();
    }
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

  public int lookupResources(
    Permission permission,
    ResourceType resourceType,
    SubjectType subjectType,
    String subjectID
  ) {
    int size = 0;
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
      size = Iterables.size((Iterable<?>) response);
      System.out.println("result: " + size);
    } catch (Exception exception) {
      exception.printStackTrace();
    }

    return size;
  }
}
