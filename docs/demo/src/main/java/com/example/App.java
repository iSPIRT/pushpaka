package com.example;

import com.authzed.api.v1.Core;
import com.authzed.api.v1.Core.ObjectReference;
import com.authzed.api.v1.Core.Relationship;
import com.authzed.api.v1.Core.RelationshipUpdate;
import com.authzed.api.v1.Core.SubjectReference;
import com.authzed.api.v1.PermissionService;
import com.authzed.api.v1.PermissionService.WriteRelationshipsRequest;
import com.authzed.api.v1.PermissionService.WriteRelationshipsResponse;
import com.authzed.api.v1.PermissionsServiceGrpc;
import com.authzed.grpcutil.BearerToken;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class App 
{
    public static void main( String[] args )
    {
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget("localhost:50051")
                .usePlaintext()
                .build();

        BearerToken bearerToken = new BearerToken("somerandomkeyhere");

        PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsService = PermissionsServiceGrpc
        .newBlockingStub(channel)
        .withCallCredentials(bearerToken);

        // Write relationship
        WriteRelationshipsRequest relRequest = PermissionService.WriteRelationshipsRequest
        .newBuilder()
        .addUpdates(
                RelationshipUpdate.newBuilder()
                .setOperation(RelationshipUpdate.Operation.OPERATION_TOUCH)
                .setRelationship(
                        Relationship.newBuilder()
                        .setResource(
                                ObjectReference.newBuilder()
                                .setObjectType("caa")
                                .setObjectId("DGCA")
                        .build())
                        .setRelation("administrator")
                        .setSubject(
                                SubjectReference.newBuilder()
                                .setObject(
                                ObjectReference.newBuilder()
                                .setObjectType("user")
                                .setObjectId("dgca_user")
                                .build())
                        .build())
                .build())
        .build())
        .build();

        WriteRelationshipsResponse relResponse = permissionsService.writeRelationships(relRequest);
        String tokenVal = relResponse.getWrittenAt().getToken();
        System.out.println(tokenVal);

        // Write relationship
        relRequest = PermissionService.WriteRelationshipsRequest
        .newBuilder()
        .addUpdates(
                RelationshipUpdate.newBuilder()
                .setOperation(RelationshipUpdate.Operation.OPERATION_TOUCH)
                .setRelationship(
                        Relationship.newBuilder()
                        .setResource(
                                ObjectReference.newBuilder()
                                .setObjectType("manufacturer")
                                .setObjectId("man_1")
                        .build())
                        .setRelation("administrator")
                        .setSubject(
                                SubjectReference.newBuilder()
                                .setObject(
                                ObjectReference.newBuilder()
                                .setObjectType("user")
                                .setObjectId("man_1_user")
                                .build())
                        .build())
                .build())
        .build())
        .build();

        relResponse = permissionsService.writeRelationships(relRequest);
        tokenVal = relResponse.getWrittenAt().getToken();
        System.out.println(tokenVal);

        // Write relationship
        relRequest = PermissionService.WriteRelationshipsRequest
        .newBuilder()
        .addUpdates(
                RelationshipUpdate.newBuilder()
                .setOperation(RelationshipUpdate.Operation.OPERATION_TOUCH)
                .setRelationship(
                        Relationship.newBuilder()
                        .setResource(
                                ObjectReference.newBuilder()
                                .setObjectType("uas")
                                .setObjectId("uas_1")
                        .build())
                        .setRelation("regulator")
                        .setSubject(
                                SubjectReference.newBuilder()
                                .setObject(
                                ObjectReference.newBuilder()
                                .setObjectType("caa")
                                .setObjectId("DGCA")
                                .build())
                        .build())
                .build())
        .build())
        .build();

        relResponse = permissionsService.writeRelationships(relRequest);
        tokenVal = relResponse.getWrittenAt().getToken();
        System.out.println(tokenVal);


        // Write relationship
        relRequest = PermissionService.WriteRelationshipsRequest
        .newBuilder()
        .addUpdates(
                RelationshipUpdate.newBuilder()
                .setOperation(RelationshipUpdate.Operation.OPERATION_TOUCH)
                .setRelationship(
                        Relationship.newBuilder()
                        .setResource(
                                ObjectReference.newBuilder()
                                .setObjectType("uas")
                                .setObjectId("uas_1")
                        .build())
                        .setRelation("manufacturer")
                        .setSubject(
                                SubjectReference.newBuilder()
                                .setObject(
                                ObjectReference.newBuilder()
                                .setObjectType("manufacturer")
                                .setObjectId("man_1")
                                .build())
                        .build())
                .build())
        .build())
        .build();

        relResponse = permissionsService.writeRelationships(relRequest);
        tokenVal = relResponse.getWrittenAt().getToken();
        System.out.println(tokenVal);

        PermissionService.CheckPermissionRequest request = PermissionService.CheckPermissionRequest.newBuilder()
        .setConsistency(
                PermissionService.Consistency.newBuilder()
                        .setMinimizeLatency(true)
                        .build())
        .setResource(
                Core.ObjectReference.newBuilder()
                        .setObjectType("manufacturer")
                        .setObjectId("man_1")
                        .build())
        .setSubject(
                Core.SubjectReference.newBuilder()
                        .setObject(
                                Core.ObjectReference.newBuilder()
                                        .setObjectType("user")
                                        .setObjectId("man_1_user")
                                        .build())
                        .build())
        .setPermission("commission_uas")
        .build();

         try {
            PermissionService.CheckPermissionResponse response = permissionsService.checkPermission(request);
            System.out.println("man_1_user_commission_uas_result: " + response.getPermissionship().getValueDescriptor().getName());
        } catch (Exception e) {
            System.out.println("Failed to check permission: " + e.getMessage());
        }

        request = PermissionService.CheckPermissionRequest.newBuilder()
        .setConsistency(
                PermissionService.Consistency.newBuilder()
                        .setMinimizeLatency(true)
                        .build())
        .setResource(
                Core.ObjectReference.newBuilder()
                        .setObjectType("caa")
                        .setObjectId("DGCA")
                        .build())
        .setSubject(
                Core.SubjectReference.newBuilder()
                        .setObject(
                                Core.ObjectReference.newBuilder()
                                        .setObjectType("user")
                                        .setObjectId("dgca_user")
                                        .build())
                        .build())
        .setPermission("commission_uas")
        .build();

         try {
            PermissionService.CheckPermissionResponse response = permissionsService.checkPermission(request);
            System.out.println("dgca_user_commission_uas_result: " + response.getPermissionship().getValueDescriptor().getName());
        } catch (Exception e) {
            System.out.println("Failed to check permission: " + e.getMessage());
        }
        request = PermissionService.CheckPermissionRequest.newBuilder()
        .setConsistency(
                PermissionService.Consistency.newBuilder()
                        .setMinimizeLatency(true)
                        .build())
        .setResource(
                Core.ObjectReference.newBuilder()
                        .setObjectType("uas")
                        .setObjectId("uas_1")
                        .build())
        .setSubject(
                Core.SubjectReference.newBuilder()
                        .setObject(
                                Core.ObjectReference.newBuilder()
                                        .setObjectType("user")
                                        .setObjectId("dgca_user")
                                        .build())
                        .build())
        .setPermission("decommision_uas")
        .build();

        try {
            PermissionService.CheckPermissionResponse response = permissionsService.checkPermission(request);
            System.out.println("dgca_user_decommision_uas_result: " + response.getPermissionship().getValueDescriptor().getName());
        } catch (Exception e) {
            System.out.println("Failed to check permission: " + e.getMessage());
        }

        request = PermissionService.CheckPermissionRequest.newBuilder()
        .setConsistency(
                PermissionService.Consistency.newBuilder()
                        .setMinimizeLatency(true)
                        .build())
        .setResource(
                Core.ObjectReference.newBuilder()
                        .setObjectType("uas")
                        .setObjectId("uas_1")
                        .build())
        .setSubject(
                Core.SubjectReference.newBuilder()
                        .setObject(
                                Core.ObjectReference.newBuilder()
                                        .setObjectType("user")
                                        .setObjectId("man_user_1")
                                        .build())
                        .build())
        .setPermission("decommision_uas")
        .build();

         try {
            PermissionService.CheckPermissionResponse response = permissionsService.checkPermission(request);
            System.out.println("man_user_1_decommision_uas_result: " + response.getPermissionship().getValueDescriptor().getName());
        } catch (Exception e) {
            System.out.println("Failed to check permission: " + e.getMessage());
        }

        request = PermissionService.CheckPermissionRequest.newBuilder()
        .setConsistency(
                PermissionService.Consistency.newBuilder()
                        .setMinimizeLatency(true)
                        .build())
        .setResource(
                Core.ObjectReference.newBuilder()
                        .setObjectType("uas")
                        .setObjectId("uas_1")
                        .build())
        .setSubject(
                Core.SubjectReference.newBuilder()
                        .setObject(
                                Core.ObjectReference.newBuilder()
                                        .setObjectType("user")
                                        .setObjectId("man_1_user")
                                        .build())
                        .build())
        .setPermission("decommision_uas")
        .build();


        try {
            PermissionService.CheckPermissionResponse response = permissionsService.checkPermission(request);
            System.out.println("man_1_user_decommision_uas_result: " + response.getPermissionship().getValueDescriptor().getName());
        } catch (Exception e) {
            System.out.println("Failed to check permission: " + e.getMessage());
        }
    } 
}
