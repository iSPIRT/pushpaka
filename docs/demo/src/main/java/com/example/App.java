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
        public static String manageObjectRelations(PermissionsServiceGrpc.PermissionsServiceBlockingStub paramPermissionService , 
        String paramRelationType ,  
        String paramOperationType , 
        String paramObjectType , 
        String paramObjectId ,String paramSubjectType,String paramSubjectId){
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
                                        .setObjectType(paramObjectType)
                                        .setObjectId(paramObjectId)
                                .build())
                                .setRelation(paramRelationType)
                                .setSubject(
                                        SubjectReference.newBuilder()
                                        .setObject(
                                        ObjectReference.newBuilder()
                                        .setObjectType(paramSubjectType)
                                        .setObjectId(paramSubjectId)
                                        .build())
                                .build())
                        .build())
                .build())
                .build();

                WriteRelationshipsResponse relResponse = paramPermissionService.writeRelationships(relRequest);
                String tokenVal = relResponse.getWrittenAt().getToken();
        
                return tokenVal;
        }    
        
        public static boolean checkObjectPermission(PermissionsServiceGrpc.PermissionsServiceBlockingStub paramPermissionService,
        String paramPermissionType ,  
        String paramEntityType , 
        String paramEntityId , String paramObjectType, String paramSubjectId){

                boolean result = false;

                PermissionService.CheckPermissionRequest request = PermissionService.CheckPermissionRequest.newBuilder()
                .setConsistency(
                        PermissionService.Consistency.newBuilder()
                                .setMinimizeLatency(true)
                                .build())
                .setResource(
                        Core.ObjectReference.newBuilder()
                                .setObjectType(paramEntityType)
                                .setObjectId(paramEntityId)
                                .build())
                .setSubject(
                        Core.SubjectReference.newBuilder()
                                .setObject(
                                        Core.ObjectReference.newBuilder()
                                                .setObjectType(paramObjectType)
                                                .setObjectId(paramSubjectId)
                                                .build())
                                .build())
                .setPermission(paramPermissionType)
                .build();

                try {
                        PermissionService.CheckPermissionResponse response = paramPermissionService.checkPermission(request);
                        System.out.println("result: " + response.getPermissionship().getValueDescriptor().getName());
                        return true;
                } catch (Exception e) {
                       System.out.println("Failed to check permission: " + e.getMessage());
                }

                return false;
               
        }    
        
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

                // Write relationships
                String result = manageObjectRelations(permissionsService,
                Relation.ADMINISTRATOR.getRelation(),
                RelationOperation.CREATE.getRelationOperation(),
                ObjectType.CAA.getObjectType(),
                "DGCA",
                ObjectType.USER.getObjectType(),
                "dgca_user");

                System.out.println(result);

                result = manageObjectRelations(permissionsService,
                Relation.ADMINISTRATOR.getRelation(),
                RelationOperation.CREATE.getRelationOperation(),
                ObjectType.MANUFACTURER.getObjectType(),
                "man_1",
                 ObjectType.USER.getObjectType(),
                "man_1_user");

                System.out.println(result);

                result = manageObjectRelations(permissionsService,
                Relation.ADMINISTRATOR.getRelation(),
                RelationOperation.CREATE.getRelationOperation(),
                ObjectType.MANUFACTURER.getObjectType(),
                "man_1",
                 ObjectType.USER.getObjectType(),
                "man_1_user");

                System.out.println(result);

                result = manageObjectRelations(permissionsService,
                Relation.REGULATOR.getRelation(),
                RelationOperation.CREATE.getRelationOperation(),
                ObjectType.UAS.getObjectType(),
                "uas_1",
                 ObjectType.CAA.getObjectType(),
                "dgca");

                System.out.println(result);

                result = manageObjectRelations(permissionsService,
                Relation.MANUFACTURER.getRelation(),
                RelationOperation.CREATE.getRelationOperation(),
                ObjectType.UAS.getObjectType(),
                "uas_1",
                 ObjectType.MANUFACTURER.getObjectType(),
                "man_1");

                System.out.println(result);

                boolean permissionResult  = checkObjectPermission(permissionsService, 
                Permission.COMMISION_UAS.getPermission(), 
                ObjectType.MANUFACTURER.getObjectType(), 
                "man_1", 
                ObjectType.USER.getObjectType(), 
                "man_1_user");

                System.out.println(permissionResult);
                
                permissionResult  = checkObjectPermission(permissionsService, 
                Permission.DECOMMISION_UAS.getPermission(), 
                ObjectType.UAS.getObjectType(), 
                "uas_1", 
                ObjectType.USER.getObjectType(), 
                "man_1_user");

                System.out.println(permissionResult);

                permissionResult  = checkObjectPermission(permissionsService, 
                Permission.COMMISION_UAS.getPermission(), 
                ObjectType.CAA.getObjectType(), 
                "DGCA", 
                ObjectType.USER.getObjectType(), 
                "dgca_user");

                System.out.println(permissionResult);

                permissionResult  = checkObjectPermission(permissionsService, 
                Permission.DECOMMISION_UAS.getPermission(), 
                ObjectType.UAS.getObjectType(), 
                "uas_1", 
                ObjectType.USER.getObjectType(), 
                "dgca_user");

                System.out.println(permissionResult);
                
                permissionResult  = checkObjectPermission(permissionsService, 
                Permission.DECOMMISION_UAS.getPermission(), 
                ObjectType.UAS.getObjectType(), 
                "uas_1", 
                ObjectType.USER.getObjectType(), 
                "man_user_1");

                System.out.println(permissionResult);
               
           
               
        } 
}
