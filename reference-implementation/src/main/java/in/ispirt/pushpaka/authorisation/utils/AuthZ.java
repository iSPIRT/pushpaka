package in.ispirt.pushpaka.authorisation.utils;

import in.ispirt.pushpaka.authorisation.Permission;
import in.ispirt.pushpaka.authorisation.RelationshipType;
import in.ispirt.pushpaka.authorisation.ResourceType;
import in.ispirt.pushpaka.authorisation.SubjectType;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthZ {
  public SpicedbClient spicedbClient;
  public String caaResourceID;

  public String getCaaResourceID() {
    return caaResourceID;
  }

  public void setCaaResourceID(String caaResourceID) {
    this.caaResourceID = caaResourceID;
  }

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

    boolean removeRegulatorCheck = this.removeRegulator();

    if (removeRegulatorCheck) {
      tokenValue =
        spicedbClient.writeRelationship(
          RelationshipType.PLATFORM,
          caaResourceID,
          ResourceType.CAA,
          AuthZConstants.PLATFORM_ID,
          SubjectType.PLATFORM
        );
    }

    if (tokenValue != null && tokenValue.length() > 0) {
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

    boolean isAssociateCAAToPlatformSuccess = associateCAAToPlatform(caaResourceID);

    boolean isCAAAdmin = spicedbClient.checkPermission(
      Permission.SUPER_ADMIN,
      ResourceType.CAA,
      caaResourceID,
      SubjectType.USER,
      platformUserID
    );

    if (isAssociateCAAToPlatformSuccess && isCAAAdmin) {
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

  /**
   * This method is used to create resource type admin when creating the resource
   */
  public boolean createResoureTypeAdmin(
    ResourceType resourceType,
    String resourceID,
    String resourceAdminID,
    String caaResourceID
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
          caaResourceID,
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
    String manufacturerUserID,
    String caaResourceID
  ) {
    boolean isSuccess = false;
    String tokenValueManufacturer = null;
    String tokenValueRegulator = null;

    boolean isUASExist = lookupUASResourceManufacturerOwnership(
      UASID,
      manufacturerUserID
    );

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
        caaResourceID,
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

    boolean isUASExist = lookupUASResourceManufacturerOwnership(UASID, operatorUserID);

    if (isUASExist) {
      return isSuccess;
    }

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
    String manufacturerUserID,
    String caaResourceID
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
      caaResourceID,
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
    String pilotResourceID,
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
          pilotResourceID,
          SubjectType.PILOT
        );
    }

    if (tokenValue != null && tokenValue.length() > 0) {
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

  public boolean addPilot(
    String pilotResourceID,
    String pilotUserID,
    String caaResourceID
  ) {
    boolean isSuccess = false;
    String tokenValue = null;
    String tokenValueAddRegulator = null;

    tokenValue =
      spicedbClient.writeRelationship(
        RelationshipType.FLIGHTPLAN_OPERATOR,
        pilotResourceID,
        ResourceType.PILOT,
        pilotUserID,
        SubjectType.USER
      );

    tokenValueAddRegulator =
      spicedbClient.writeRelationship(
        RelationshipType.REGULATOR,
        pilotResourceID,
        ResourceType.PILOT,
        caaResourceID,
        SubjectType.CAA
      );

    if (
      tokenValue != null &&
      tokenValue.length() > 0 &&
      tokenValueAddRegulator != null &&
      tokenValueAddRegulator.length() > 0
    ) {
      isSuccess = true;
    }

    return isSuccess;
  }

  /**
   * this function will be used to lookup the groups to which a pilot belongs to
   */
  public Set<String> lookupPilotResource(String pilotUserID) {
    /**
     * this function will help in looking up pilot
     * across multiple groups
     */
    Set<String> pilotToOperators = spicedbClient.lookupResources(
      RelationshipType.PILOT,
      ResourceType.OPERATOR,
      SubjectType.PILOT,
      pilotUserID
    );

    return pilotToOperators;
  }

  /**
   * This function will be used to lookup the UAS resource association with
   * operators
   */
  public boolean lookupUASResourceManufacturerOwnership(
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

  public boolean lookupUASResourceOperatorOwnership(
    String UASResourceID,
    String operatorResourceID
  ) {
    Set<String> uasResources = spicedbClient.lookupResources(
      RelationshipType.OWNER,
      ResourceType.UAS,
      SubjectType.OPERATOR,
      operatorResourceID
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

  public List<ResourceType> lookupResourceTypeForRegulatorApproval() {
    List<ResourceType> resourceTypeList = new ArrayList<ResourceType>();

    resourceTypeList.add(ResourceType.OPERATOR);
    resourceTypeList.add(ResourceType.MANUFACTURER);
    resourceTypeList.add(ResourceType.DSSP);
    resourceTypeList.add(ResourceType.TRADER);
    resourceTypeList.add(ResourceType.REPAIRAGENCY);
    resourceTypeList.add(ResourceType.PILOT);
    resourceTypeList.add(ResourceType.UASTYPE);

    return resourceTypeList;
  }

  public Set<String> lookupResourcesForRegulatorApproval(String caaAdminsUserID) {
    List<ResourceType> resourceTypeList = lookupResourceTypeForRegulatorApproval();
    Set<String> resourceIDSetForApproval = new HashSet<String>();
    for (int counter = 0; counter < resourceTypeList.size(); counter++) {
      Set<String> resourceSet = spicedbClient.lookupResources(
        Permission.APPROVE,
        resourceTypeList.get(counter),
        SubjectType.USER,
        caaAdminsUserID
      );
      resourceIDSetForApproval.addAll(resourceSet);
    }

    return resourceIDSetForApproval;
  }

  public boolean lookupRegulator(String resourceID) {
    Set<String> resourceSet = spicedbClient.lookupResources(
      RelationshipType.PLATFORM,
      ResourceType.CAA,
      SubjectType.PLATFORM,
      AuthZConstants.PLATFORM_ID
    );

    System.out.println(resourceSet);

    return resourceSet.contains(resourceID);
  }

  public Set<String> lookupRegulator() {
    Set<String> resourceSet = spicedbClient.lookupResources(
      RelationshipType.PLATFORM,
      ResourceType.CAA,
      SubjectType.PLATFORM,
      AuthZConstants.PLATFORM_ID
    );

    System.out.println(resourceSet);

    return resourceSet;
  }

  public boolean removeRegulator() {
    boolean isSuccess = false;

    Set<String> regulatorSet = (HashSet<String>) this.lookupRegulator();
    List<String> regulatorList = new ArrayList<String>(regulatorSet);

    for (int counter = 0; counter < regulatorList.size(); counter++) {
      System.out.print(regulatorList.get(counter));
      String tokenValue = spicedbClient.deleteRelationship(
        RelationshipType.PLATFORM,
        regulatorList.get(counter),
        ResourceType.CAA,
        AuthZConstants.PLATFORM_ID,
        SubjectType.PLATFORM
      );
      if (tokenValue != null) {
        isSuccess = true;
      } else {
        isSuccess = false;
      }
    }

    int modifiedRegulatorCount = ((HashSet<String>) this.lookupRegulator()).size();

    if (modifiedRegulatorCount == 0) {
      isSuccess = true;
    }

    return isSuccess;
  }
}
