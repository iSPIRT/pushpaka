package in.ispirt.pushpaka.authorisation.utils;

import in.ispirt.pushpaka.authorisation.Permission;
import in.ispirt.pushpaka.authorisation.RelationshipType;
import in.ispirt.pushpaka.authorisation.ResourceType;
import in.ispirt.pushpaka.authorisation.SubjectType;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Set;

public class AuthZ {
  public SpicedbClient spicedbClient;

  public SpicedbClient getSpicedbClient() {
    return spicedbClient;
  }

  public AuthZ() {
    ManagedChannel channel = ManagedChannelBuilder
      .forTarget(SpicedbClient.SPICEDDB_TARGET)
      .usePlaintext() // if not using TLS, replace with .usePlaintext()
      .build();
    spicedbClient = SpicedbClient.getInstance(channel, SpicedbClient.SPICEDB_TOKEN);
  }

  /**
   * This method is used to create a platform admin with a buil-in seed for the
   * platform
   *
   */
  public boolean createPlatformAdmin(String platformUserID) {
    String tokenValue = null;

    tokenValue =
      spicedbClient.writeRelationship(
        RelationshipType.ADMINISTRATOR,
        AuthZConstants.PLATFORM_ID,
        ResourceType.PLATFORM,
        platformUserID,
        SubjectType.USER
      );

    if (tokenValue != null) {
      return true;
    } else {
      return false;
    }
    // platform:digital-sky-platform#administrator@user:platform-user
  }

  public boolean associateCAAToPlatform(String caaResourceID) {
    String tokenValue = null;

    tokenValue =
      spicedbClient.writeRelationship(
        RelationshipType.PLATFORM,
        caaResourceID,
        ResourceType.CAA,
        AuthZConstants.PLATFORM_ID,
        SubjectType.PLATFORM
      );

    if (tokenValue != null) {
      return true;
    } else {
      return false;
    }
    // caa:caa-authority#platform@platform:digital-sky-platform
  }

  /**
   * This method is used to create a platform admin with a buil-in seed for the
   * platform
   *
   */
  public boolean createCAAAdmin(
    String caaResourceID,
    String caaUserAdminID,
    String platformUserID
  ) {
    String tokenValue = null;

    boolean isCAAAdmin = spicedbClient.checkPermission(
      Permission.SUPER_ADMIN,
      ResourceType.CAA,
      caaResourceID,
      SubjectType.USER,
      platformUserID
    );

    if (isCAAAdmin) {
      tokenValue =
        spicedbClient.writeRelationship(
          RelationshipType.ADMINISTRATOR,
          caaResourceID,
          ResourceType.CAA,
          caaUserAdminID,
          SubjectType.USER
        );
    }

    if (tokenValue != null) {
      return true;
    } else {
      return false;
    }
    // caa:caa-authority#administrator@user:caa-user
  }

  /** Thois method is used to get CAA resource ID */
  public static String getCAAResourceID() {
    // return the ID of the CCA resource in the system
    return AuthZConstants.TEST_CAA_RESOURCE_ID;
  }

  /**
   * This method is used to create resource type admin when creating the resource
   */
  public boolean createResoureTypeAdmin(
    ResourceType resourceType,
    String resourceID,
    String resourceAdminID
  ) {
    boolean isSuccess = false;
    String tokenValue = null;
    String regulatorTokenValue = null;

    tokenValue =
      spicedbClient.writeRelationship(
        RelationshipType.ADMINISTRATOR,
        resourceID,
        resourceType,
        resourceAdminID,
        SubjectType.USER
      );

    if (tokenValue != null) {
      isSuccess = true;
    }

    if (
      ResourceType.OPERATOR.equals(resourceType) ||
      ResourceType.MANUFACTURER.equals(resourceType) ||
      ResourceType.DSSP.equals(resourceType) ||
      ResourceType.TRADER.equals(resourceType) ||
      ResourceType.REPAIRAGENCY.equals(resourceType)
    ) {
      regulatorTokenValue =
        spicedbClient.writeRelationship(
          RelationshipType.REGULATOR,
          resourceID,
          resourceType,
          getCAAResourceID(),
          SubjectType.CAA
        );

      if (tokenValue != null && regulatorTokenValue != null) {
        isSuccess = true;
      }
    }
    // operator:operator-1#administrator@user:operator-user
    // manufacturer:manufacturer-1#administrator@user:manufacturer-user

    return isSuccess;
  }

  public boolean checkIsResourceAdmin(
    ResourceType resourceType,
    String resourceID,
    String userID
  ) {
    boolean isResourceAdmin = spicedbClient.checkPermission(
      Permission.SUPER_ADMIN,
      resourceType,
      resourceID,
      SubjectType.USER,
      userID
    );

    return isResourceAdmin;
  }

  /** This methods creates more than one relationships for UAS */
  public boolean createUASManufacturerRelationships(
    String UASID,
    String manufacturerID,
    String manufacturerUserID
  ) {
    boolean isSuccess = false;
    String tokenValueManufacturer = null;
    String tokenValueRegulator = null;

    boolean isUASExist = lookupUASResourceOwnership(UASID, manufacturerUserID);

    if (isUASExist) {
      return isSuccess;
    }

    boolean checkIsManufacturerAdmin = checkIsResourceAdmin(
      ResourceType.MANUFACTURER,
      manufacturerID,
      manufacturerUserID
    );

    if (!checkIsManufacturerAdmin) {
      return isSuccess;
    }

    tokenValueManufacturer =
      spicedbClient.writeRelationship(
        RelationshipType.MANUFACTURER,
        UASID,
        ResourceType.UAS,
        manufacturerID,
        SubjectType.MANUFACTURER
      );

    tokenValueRegulator =
      spicedbClient.writeRelationship(
        RelationshipType.REGULATOR,
        UASID,
        ResourceType.UAS,
        getCAAResourceID(),
        SubjectType.CAA
      );

    if (tokenValueManufacturer != null && tokenValueRegulator != null) {
      isSuccess = true;
    }

    return isSuccess;
    // uas:uas-1#manufacturer@manufacturer:manufacturer-1
    // uas:uas-1#regulator@caa:caa-authority

  }

  /** This methods creates more than one relationships for UAS */
  public boolean createUASOperatorRelationships(
    String UASID,
    String operatorID,
    String operatorUserID
  ) {
    /** Put additional checks for pre-condition on UAS */
    boolean isSuccess = false;
    String tokenValueOperator = null;

    boolean checkIsOperatorAdmin = checkIsResourceAdmin(
      ResourceType.OPERATOR,
      operatorID,
      operatorUserID
    );

    if (!checkIsOperatorAdmin) {
      return isSuccess;
    }

    tokenValueOperator =
      spicedbClient.writeRelationship(
        RelationshipType.OWNER,
        UASID,
        ResourceType.UAS,
        operatorID,
        SubjectType.OPERATOR
      );

    if (tokenValueOperator != null) {
      isSuccess = true;
    }

    return isSuccess;
    // uas:uas-1#owner@operator:operator-1
  }

  /** This method creates more than one realtionships for UASType */
  public boolean createUASTypeRelationships(
    String UASTypeID,
    String manufacturerID,
    String manufacturerUserID
  ) {
    boolean isSuccess = false;

    boolean checkIsManufacturerAdmin = checkIsResourceAdmin(
      ResourceType.MANUFACTURER,
      manufacturerID,
      manufacturerUserID
    );

    if (!checkIsManufacturerAdmin) {
      return isSuccess;
    }

    String tokenValueManufacturer = spicedbClient.writeRelationship(
      RelationshipType.MANUFACTURER,
      UASTypeID,
      ResourceType.UASTYPE,
      manufacturerID,
      SubjectType.MANUFACTURER
    );

    String tokenValueRgulator = spicedbClient.writeRelationship(
      RelationshipType.REGULATOR,
      UASTypeID,
      ResourceType.UASTYPE,
      getCAAResourceID(),
      SubjectType.CAA
    );

    if (tokenValueManufacturer != null && tokenValueRgulator != null) {
      isSuccess = true;
    }

    return isSuccess;
    // uastype:uastype-1#manufacturer@manufacturer:manufacturer-1
    // uastype:uastype-1#regulator@caa:caa-authority

  }

  /**
   * Check if the pilot is a flight operations admin
   */
  public boolean isFlightOperationsAdmin(String pilotUserID, String operatorResourceID) {
    boolean hasPermission = spicedbClient.checkPermission(
      Permission.FLIGHT_OPERATIONS_ADMIN,
      ResourceType.OPERATOR,
      operatorResourceID,
      SubjectType.USER,
      pilotUserID
    );

    return hasPermission;
  }

  /** This function is used to add pilot user to operator */
  public boolean addPilotToOperator(
    String pilotUserID,
    String operatorResourceID,
    String operatorUserID
  ) {
    String tokenValue = null;
    boolean isSuccess = false;

    boolean addPilotToOperator = spicedbClient.checkPermission(
      Permission.ADD_PILOT,
      ResourceType.OPERATOR,
      operatorResourceID,
      SubjectType.USER,
      operatorUserID
    );

    if (addPilotToOperator) {
      tokenValue =
        spicedbClient.writeRelationship(
          RelationshipType.PILOT,
          operatorResourceID,
          ResourceType.OPERATOR,
          pilotUserID,
          SubjectType.USER
        );
    }

    if (tokenValue != null) {
      isSuccess = true;
    }

    return isSuccess;
    // operator:operator-1#pilot@user:pilot-1

  }

  /** This function is used to add pilot user to operator */
  public boolean removePilotFromOperator(
    String pilotUserID,
    String operatorResourceID,
    String operatorUserID
  ) {
    String tokenValue = null;
    boolean isSuccess = false;
    boolean removePilotToOperator = true;

    removePilotToOperator =
      spicedbClient.checkPermission(
        Permission.REMOVE_PILOT,
        ResourceType.OPERATOR,
        operatorResourceID,
        SubjectType.USER,
        operatorUserID
      );

    if (removePilotToOperator) {
      tokenValue =
        spicedbClient.deleteRelationship(
          RelationshipType.PILOT,
          operatorResourceID,
          ResourceType.OPERATOR,
          pilotUserID,
          SubjectType.USER
        );
    }

    if (tokenValue != null) {
      isSuccess = true;
    }

    return isSuccess;
  }

  public boolean addPilot(String pilotUserID, String caaResourceID) {
    boolean isSuccess = false;
    String tokenValue = null;

    tokenValue =
      spicedbClient.writeRelationship(
        RelationshipType.REGULATOR,
        caaResourceID,
        ResourceType.CAA,
        pilotUserID,
        SubjectType.PILOT
      );

    if (tokenValue != null) {
      isSuccess = true;
    }

    return isSuccess;
  }

  /**
   * this function will be used to lookup the groups to which a pilot belongs to
   */
  public void lookupPilotResource(String pilotUserID) {
    /**
     * this function will help in looking up pilot
     * across multiple groups
     */
  }

  /**
   * This function will be used to lookup the UAS resource association with
   * operators
   */
  public boolean lookupUASResourceOwnership(
    String UASResourceID,
    String manufacturerResourceID
  ) {
    Set<String> uasResources = spicedbClient.lookupResources(
      RelationshipType.MANUFACTURER,
      ResourceType.UAS,
      SubjectType.MANUFACTURER,
      manufacturerResourceID
    );

    boolean isSuccess = uasResources.contains(UASResourceID);

    return isSuccess;
  }

  public void shutdownChannel() {
    try {
      this.spicedbClient.shutdownChannel();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public boolean approveResourceByRegulator(
    ResourceType resourceType,
    String resourceID,
    String caaAdminUserID
  ) {
    boolean isApprover = spicedbClient.checkPermission(
      Permission.APPROVE,
      resourceType,
      resourceID,
      SubjectType.USER,
      caaAdminUserID
    );

    return isApprover;
  }
}
