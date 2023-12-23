package in.ispirt.pushpaka.authorisation.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import com.authzed.api.v1.Core;
import com.authzed.api.v1.Core.ObjectReference;
import com.authzed.api.v1.Core.Relationship;
import com.authzed.api.v1.Core.RelationshipUpdate;
import com.authzed.api.v1.Core.RelationshipUpdate.Operation;
import com.authzed.api.v1.Core.SubjectReference;
import com.authzed.api.v1.PermissionService;
import com.authzed.api.v1.PermissionService.CheckPermissionResponse;
import com.authzed.api.v1.PermissionsServiceGrpc;
import com.authzed.api.v1.SchemaServiceGrpc;
import com.authzed.api.v1.SchemaServiceOuterClass.WriteSchemaRequest;
import com.authzed.grpcutil.BearerToken;

import in.ispirt.pushpaka.authorisation.Permission;
import in.ispirt.pushpaka.authorisation.RelationshipType;
import in.ispirt.pushpaka.authorisation.ResourceType;
import in.ispirt.pushpaka.authorisation.SubjectType;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class SpicedbUtils {
    final static Operation OPERATION_CREATE = RelationshipUpdate.Operation.OPERATION_CREATE;
    final static Operation OPERATION_TOUCH = RelationshipUpdate.Operation.OPERATION_TOUCH;
    final static Operation OPERATION_DELETE = RelationshipUpdate.Operation.OPERATION_DELETE;
    final static String target = "localhost:50051";
    final static String token = "somerandomkeyhere";

    private static ManagedChannel channel = ManagedChannelBuilder
    .forTarget(target)
    .usePlaintext() // if not using TLS, replace with .usePlaintext()
    .build();
  
    private static final PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsService = PermissionsServiceGrpc
    .newBlockingStub(channel)
    .withCallCredentials(new BearerToken(token));

  
    private static final SchemaServiceGrpc.SchemaServiceBlockingStub schemaService = SchemaServiceGrpc
    .newBlockingStub(channel)
    .withCallCredentials(new BearerToken(token));


    public static String writeRelationship(RelationshipType relationType,
    String resourceID,
    ResourceType resourceType,
    String subjectID,
    SubjectType subjectType){
        // Write relationship
        PermissionService.WriteRelationshipsRequest relRequest = PermissionService
        .WriteRelationshipsRequest.newBuilder()
        .addUpdates(
            RelationshipUpdate
            .newBuilder()
            .setOperation(OPERATION_CREATE)
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
            response = permissionsService.writeRelationships(relRequest);
        } catch (Exception e) {
            return "";
        }
        
        return response.getWrittenAt().getToken();

    }

    public static boolean checkPermission(Permission permission,
    ResourceType resourceType,
    String resourceID,
    SubjectType subjectType,
    String subjectID){
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
            PermissionService.CheckPermissionResponse response = permissionsService.checkPermission(
                request
            );
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

    public static void writeSchema(String filename) {
        try {
        Path p = Path.of(filename);
        String schema = Files.readString(p);
        WriteSchemaRequest wsr = WriteSchemaRequest.newBuilder().setSchema(schema).build();
        schemaService.writeSchema(wsr);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    public static void shutdownChannel() {
        try {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            // Uh oh!
        }
    }
}