package com.example;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.authzed.api.v1.Core;
import com.authzed.api.v1.Core.ObjectReference;
import com.authzed.api.v1.Core.Relationship;
import com.authzed.api.v1.Core.RelationshipUpdate;
import com.authzed.api.v1.Core.SubjectReference;
import com.authzed.api.v1.Core.ZedToken;
import com.authzed.api.v1.PermissionService;
import com.authzed.api.v1.PermissionService.CheckPermissionResponse;
import com.authzed.api.v1.PermissionsServiceGrpc;
import com.authzed.grpcutil.BearerToken;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class App 
{
        private static final Logger logger = Logger.getLogger(App.class.getName());
        private static final String target = "localhost:50051";
        private static final String token = "somerandomkeyhere";
    
        private final PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsService;

        public App(Channel channel) {
                permissionsService = PermissionsServiceGrpc.newBlockingStub(channel)
                        .withCallCredentials(new BearerToken(token));
        }
        
        public String manageObjectRelations(String paramRelationType , String paramOperationType , String paramObjectType , 
        String paramObjectId ,String paramSubjectType,String paramSubjectId){
                 // Write relationship
                PermissionService.WriteRelationshipsRequest relRequest = PermissionService.WriteRelationshipsRequest
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

                PermissionService.WriteRelationshipsResponse response;
                try {
                    response = permissionsService.writeRelationships(relRequest);
                } catch (Exception e) {
                    logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
                    return "";
                }
                logger.info("Response: " + response.toString());
                return response.getWrittenAt().getToken();
        }    
        
        public boolean checkObjectPermission(String paramPermissionType , String paramEntityType , String paramEntityId 
        , String paramObjectType, String paramSubjectId){

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
                        PermissionService.CheckPermissionResponse response = permissionsService.checkPermission(request);
                        System.out.println("result: " + response.getPermissionship().getValueDescriptor().getName());

                        if(CheckPermissionResponse.Permissionship.PERMISSIONSHIP_HAS_PERMISSION.getNumber() == (response.getPermissionship().getNumber())){
                                return true;
                        } else {
                                return false;
                        }
                } catch (Exception e) {
                        logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
                        return false;
                }
        }    
        
        public static void main( String[] args ) 
        {
                ManagedChannel channel = ManagedChannelBuilder
                .forTarget(target)
                .usePlaintext() // if not using TLS, replace with .usePlaintext()
                .build();
                try {
                        App client = new App(channel);

                        // Write relationships
                        String result = client.manageObjectRelations(Relation.ADMINISTRATOR.getRelation(),
                        RelationOperation.CREATE.getRelationOperation(),
                        ObjectType.CAA.getObjectType(),
                        "DGCA",
                        ObjectType.USER.getObjectType(),
                        "dgca_user");

                        System.out.println(result);

                        result = client.manageObjectRelations(Relation.ADMINISTRATOR.getRelation(),
                        RelationOperation.CREATE.getRelationOperation(),
                        ObjectType.MANUFACTURER.getObjectType(),
                        "man_1",
                        ObjectType.USER.getObjectType(),
                        "man_1_user");

                        System.out.println(result);

                        result = client.manageObjectRelations(Relation.ADMINISTRATOR.getRelation(),
                        RelationOperation.CREATE.getRelationOperation(),
                        ObjectType.MANUFACTURER.getObjectType(),
                        "man_1",
                        ObjectType.USER.getObjectType(),
                        "man_1_user");

                        System.out.println(result);

                        result = client.manageObjectRelations(Relation.REGULATOR.getRelation(),
                        RelationOperation.CREATE.getRelationOperation(),
                        ObjectType.UAS.getObjectType(),
                        "uas_1",
                        ObjectType.CAA.getObjectType(),
                        "dgca");

                        System.out.println(result);

                        result = client.manageObjectRelations(Relation.MANUFACTURER.getRelation(),
                        RelationOperation.CREATE.getRelationOperation(),
                        ObjectType.UAS.getObjectType(),
                        "uas_1",
                        ObjectType.MANUFACTURER.getObjectType(),
                        "man_1");

                        System.out.println(result);

                        Thread.sleep(5000);

                        boolean permissionResult  = client.checkObjectPermission(Permission.COMMISION_UAS.getPermission(), 
                        ObjectType.MANUFACTURER.getObjectType(), 
                        "man_1", 
                        ObjectType.USER.getObjectType(), 
                        "man_1_user");

                        System.out.println(permissionResult);
                        
                        permissionResult  = client.checkObjectPermission(Permission.DECOMMISION_UAS.getPermission(), 
                        ObjectType.UAS.getObjectType(), 
                        "uas_1", 
                        ObjectType.USER.getObjectType(), 
                        "man_1_user");

                        System.out.println(permissionResult);

                        permissionResult  = client.checkObjectPermission(Permission.COMMISION_UAS.getPermission(), 
                        ObjectType.CAA.getObjectType(), 
                        "DGCA", 
                        ObjectType.USER.getObjectType(), 
                        "dgca_user");

                        System.out.println(permissionResult);

                        permissionResult  = client.checkObjectPermission(Permission.DECOMMISION_UAS.getPermission(), 
                        ObjectType.UAS.getObjectType(), 
                        "uas_1", 
                        ObjectType.USER.getObjectType(), 
                        "dgca_user");

                        System.out.println(permissionResult);
                        
                        permissionResult  = client.checkObjectPermission(Permission.DECOMMISION_UAS.getPermission(), 
                        ObjectType.UAS.getObjectType(), 
                        "uas_1", 
                        ObjectType.USER.getObjectType(), 
                        "man_user_1");

                        System.out.println(permissionResult);
               
                
                } catch(InterruptedException exception) {
                        // Uh oh!
                }finally {
                        try {
                                channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                        } catch (InterruptedException exception) {
                                // Uh oh!
                        }
                }   
               
        } 
}
