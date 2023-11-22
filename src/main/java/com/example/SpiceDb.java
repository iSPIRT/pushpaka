package com.example;

import com.authzed.api.v1.Core;
import com.authzed.api.v1.Core.ObjectReference;
import com.authzed.api.v1.Core.Relationship;
import com.authzed.api.v1.Core.RelationshipUpdate;
import com.authzed.api.v1.Core.SubjectReference;
import com.authzed.api.v1.Core.ZedToken;
import com.authzed.api.v1.PermissionService;
import com.authzed.api.v1.PermissionService.CheckPermissionResponse;
import com.authzed.api.v1.PermissionsServiceGrpc;
import com.authzed.api.v1.SchemaServiceGrpc;
import com.authzed.api.v1.SchemaServiceOuterClass.WriteSchemaRequest;
import com.authzed.grpcutil.BearerToken;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpiceDb {
  private static final String target = "localhost:50051";
  private static final String token = "somerandomkeyhere";
  private static final Logger logger = Logger.getLogger(App.class.getName());

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

  public static String manageObjectRelations(
    Relation relation,
    RelationOperation relationOperation,
    ObjectType objectType,
    String paramObjectId,
    ObjectType subjectType,
    String paramSubjectId
  ) {
    // Write relationship
    PermissionService.WriteRelationshipsRequest relRequest = PermissionService
      .WriteRelationshipsRequest.newBuilder()
      .addUpdates(
        RelationshipUpdate
          .newBuilder()
          .setOperation(relationOperation.getRelationOperation())
          .setRelationship(
            Relationship
              .newBuilder()
              .setResource(
                ObjectReference
                  .newBuilder()
                  .setObjectType(objectType.getObjectType())
                  .setObjectId(paramObjectId)
                  .build()
              )
              .setRelation(relation.getRelation())
              .setSubject(
                SubjectReference
                  .newBuilder()
                  .setObject(
                    ObjectReference
                      .newBuilder()
                      .setObjectType(subjectType.getObjectType())
                      .setObjectId(paramSubjectId)
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
      logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
      return "";
    }
    logger.info("Response: " + response.toString());
    return response.getWrittenAt().getToken();
  }

  public static boolean checkObjectPermission(
    Permission permission,
    ObjectType entityType,
    String paramEntityId,
    ObjectType objectType,
    String paramSubjectId
  ) {
    PermissionService.CheckPermissionRequest request = PermissionService
      .CheckPermissionRequest.newBuilder()
      .setConsistency(
        PermissionService.Consistency.newBuilder().setMinimizeLatency(true).build()
      )
      .setResource(
        Core
          .ObjectReference.newBuilder()
          .setObjectType(entityType.getObjectType())
          .setObjectId(paramEntityId)
          .build()
      )
      .setSubject(
        Core
          .SubjectReference.newBuilder()
          .setObject(
            Core
              .ObjectReference.newBuilder()
              .setObjectType(objectType.getObjectType())
              .setObjectId(paramSubjectId)
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
      logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
      return false;
    }
  }

  public static void writeSchema(String filename) {
    try {
      Path p = Path.of(filename);
      String schema = Files.readString(p);
      logger.info("SCHEMA::\n" + schema);
      WriteSchemaRequest wsr = WriteSchemaRequest.newBuilder().setSchema(schema).build();
      schemaService.writeSchema(wsr);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Error writing Schema: " + e.toString());
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
