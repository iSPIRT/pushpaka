package in.ispirt.pushpaka.authorisation.utils;

import in.ispirt.pushpaka.authorisation.Permission;
import in.ispirt.pushpaka.authorisation.RelationshipType;
import in.ispirt.pushpaka.authorisation.ResourceType;
import in.ispirt.pushpaka.authorisation.SubjectType;

public class AuthZUtils {
  public static final SpicedbClient spicedbClient;

  static {
    spicedbClient = SpicedbClient.getInstance(
        SpicedbClient.SPICEDDB_TARGET,
        SpicedbClient.SPICEDB_TOKEN);
  }

  /**
   * This method is used to create a platform admin with a buil-in seed for the
   * platform
   * This method also has a built-in seed for a resource type CAA that implies
   * that
   * the platform admin has access to resource type CAA and administer it
   */
  public static boolean createPlatformAdmin(String subjectID) {
    // create relation of administrator
    // create relation with resource type

    String tokenValue_1 = spicedbClient.writeRelationship(
        RelationshipType.ADMINISTRATOR,
        AuthZConstants.PLATFORM_ID,
        ResourceType.PLATFORM,
        subjectID,
        SubjectType.USER);

    String tokenValue_2 = spicedbClient.writeRelationship(
        RelationshipType.OWNER,
        ResourceType.CAA.getResourceType(),
        ResourceType.PLATFORM_RESOURCETYPE,
        AuthZConstants.PLATFORM_ID,
        SubjectType.PLATFORM);

    if (tokenValue_1 != null && tokenValue_2 != null) {
      return true;
    } else {
      return false;
    }
    // platform digital-sky administrator user<input/>
    // resource caa platform digital-sky
    // platform:digital-sky-platform#administrator@user:platform-user
    // platform_resource_type:caa#owner@platform:digital-sky-platform
  }

  /**
   * This method allows for any resource type admin user to be created by a
   * platform
   * admin
   */
  public static boolean createResoureTypeAdminByPlatformUser(
      ResourceType resourceType,
      String resourceID,
      String resourceAdminID,
      String platformAdminID) {
    String tokenValue = null;

    // check permission and then create admin for the given resourceID
    boolean isPlatformAdmin = spicedbClient.checkPermission(
        Permission.SUPER_ADMIN,
        ResourceType.PLATFORM_RESOURCETYPE,
        resourceType.getResourceType(),
        SubjectType.USER,
        platformAdminID);

    // create admin for the provided resource
    if (isPlatformAdmin) {
      tokenValue = spicedbClient.writeRelationship(
          RelationshipType.ADMINISTRATOR,
          resourceID,
          resourceType,
          resourceAdminID,
          SubjectType.USER);
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
    return AuthZConstants.CAA_RESOURCE_ID;
  }

  /**
   * This method is used to create resource type admin when creating the resource
   */
  public static boolean createResoureTypeAdmin(
      ResourceType resourceType,
      String resourceID,
      String resourceAdminID) {
    boolean isSuccess = false;
    String tokenValue = null;
    String operatorTokenValue = null;

    tokenValue = spicedbClient.writeRelationship(
        RelationshipType.ADMINISTRATOR,
        resourceID,
        resourceType,
        resourceAdminID,
        SubjectType.USER);

    if (tokenValue != null) {
      isSuccess = true;
    }

    if (ResourceType.OPERATOR.equals(resourceType)) {
      operatorTokenValue = spicedbClient.writeRelationship(
          RelationshipType.REGULATOR,
          resourceID,
          resourceType,
          getCAAResourceID(),
          SubjectType.CAA);

      if (tokenValue != null && operatorTokenValue != null) {
        isSuccess = true;
      }
    }
    // operator:operator-1#administrator@user:operator-user
    // manufacturer:manufacturer-1#administrator@user:manufacturer-user

    return isSuccess;
  }

  public static boolean checkIsResourceAdmin(
      ResourceType resourceType,
      String resourceID,
      String userID) {
    boolean isResourceAdmin = spicedbClient.checkPermission(
        Permission.SUPER_ADMIN,
        resourceType,
        resourceID,
        SubjectType.USER,
        userID);

    return isResourceAdmin;
  }

  /** This methods creates more than one relationships for UAS */
  public static boolean createUASManufacturerRelationships(
      String UASID,
      String manufacturerID,
      String manufacturerUserID) {
    /** Put additional checks for pre-condition on UAS */
    boolean isSuccess = false;
    String tokenValueManufacturer = null;
    String tokenValueRegulator = null;

    boolean checkIsManufacturerAdmin = checkIsResourceAdmin(
        ResourceType.MANUFACTURER, manufacturerID, manufacturerUserID);

    if (!checkIsManufacturerAdmin) {
      return isSuccess;
    }

    tokenValueManufacturer = spicedbClient.writeRelationship(
        RelationshipType.MANUFACTURER,
        UASID,
        ResourceType.UAS,
        manufacturerID,
        SubjectType.MANUFACTURER);

    tokenValueRegulator = spicedbClient.writeRelationship(
        RelationshipType.REGULATOR,
        UASID,
        ResourceType.UAS,
        getCAAResourceID(),
        SubjectType.CAA);

    if (tokenValueManufacturer != null && tokenValueRegulator != null) {
      isSuccess = true;
    }

    return isSuccess;
    // uas:uas-1#manufacturer@manufacturer:manufacturer-1
    // uas:uas-1#regulator@caa:caa-authority

  }

  /** This methods creates more than one relationships for UAS */
  public static boolean createUASOperatorRelationships(
      String UASID,
      String operatorID,
      String operatorUserID) {
    /** Put additional checks for pre-condition on UAS */
    boolean isSuccess = false;
    String tokenValueOperator = null;

    boolean checkIsOperatorAdmin = checkIsResourceAdmin(
        ResourceType.MANUFACTURER, operatorID, operatorUserID);

    if (!checkIsOperatorAdmin) {
      return isSuccess;
    }

    tokenValueOperator = spicedbClient.writeRelationship(
        RelationshipType.OWNER,
        UASID,
        ResourceType.UAS,
        operatorID,
        SubjectType.OPERATOR);

    if (tokenValueOperator != null) {
      isSuccess = true;
    }

    return isSuccess;
    // uas:uas-1#owner@operator:operator-1
  }

  /** This method creates more than one realtionships for UASType */
  public static boolean createUASTypeRelationships(
      String UASTypeID,
      String manufacturerID,
      String manufacturerUserID) {
    boolean isSuccess = false;

    boolean checkIsManufacturerAdmin = checkIsResourceAdmin(
        ResourceType.MANUFACTURER, manufacturerID, manufacturerUserID);

    if (!checkIsManufacturerAdmin) {
      return isSuccess;
    }

    String tokenValueManufacturer = spicedbClient.writeRelationship(
        RelationshipType.MANUFACTURER,
        UASTypeID,
        ResourceType.UASTYPE,
        manufacturerID,
        SubjectType.MANUFACTURER);

    String tokenValueRgulator = spicedbClient.writeRelationship(
        RelationshipType.REGULATOR,
        UASTypeID,
        ResourceType.UASTYPE,
        getCAAResourceID(),
        SubjectType.CAA);

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
  public static boolean isFlightOperationsAdmin(
      String pilotUserID,
      String operatorResourceID) {
    boolean hasPermission = spicedbClient.checkPermission(
        Permission.FLIGHT_OPERATIONS_ADMIN,
        ResourceType.OPERATOR,
        operatorResourceID,
        SubjectType.USER,
        pilotUserID);

    return hasPermission;
  }

  /** This function is used to add pilot user to operator */
  public static boolean addPilotToOperator(
      String pilotUserID,
      String operatorResourceID,
      String operatorUserID) {
    String tokenValue = null;
    boolean isSuccess = false;

    boolean addPilotToOperator = spicedbClient.checkPermission(
        Permission.ADD_PILOT,
        ResourceType.OPERATOR,
        operatorResourceID,
        SubjectType.USER,
        operatorUserID);

    if (addPilotToOperator) {
      tokenValue = spicedbClient.writeRelationship(
          RelationshipType.PILOT,
          operatorResourceID,
          ResourceType.OPERATOR,
          pilotUserID,
          SubjectType.USER);
    }

    if (tokenValue != null) {
      isSuccess = true;
    }

    return isSuccess;
    // operator:operator-1#pilot@user:pilot-1

  }

  /**
   * this function will be used to lookup the groups to which a pilot belongs to
   */
  public static void lookupPilotToOperators(String pilotUserID) {
    /**
     * this function will help in looking up pilot
     * across multiple groups
     */
  }

  /**
   * This function will be used to lookup the UAS resource association with
   * operators
   */
  public static void lookupUASResource(String UASResourceID) {
  }
}
