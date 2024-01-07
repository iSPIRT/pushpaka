package in.ispirt.pushpaka.authorisation.utils;

import in.ispirt.pushpaka.authorisation.Permission;
import in.ispirt.pushpaka.authorisation.RelationshipType;
import in.ispirt.pushpaka.authorisation.ResourceType;
import in.ispirt.pushpaka.authorisation.SubjectType;

public class AuthZUtils {
 
  public static final SpicedbClient spicedbClient;

  static{
    spicedbClient = SpicedbClient.getInstance(SpicedbClient.SPICEDDB_TARGET, SpicedbClient.SPICEDB_TOKEN);
  }
 
  /**This method is used to create a platform admin with a buil-in seed for the platform
   * This method also has a built-in seed for a resource type CAA that implies that
   * the platform admin has access to resource type CAA and administer it
   */
  public static boolean createPlatformAdmin(String subjectID) {
    //create relation of administrator
    //create relation with resource type

    String tokenValue_1 = spicedbClient.writeRelationship(
      RelationshipType.ADMINISTRATOR,
      AuthZConstants.PLATFORM_ID,
      ResourceType.PLATFORM,
      subjectID,
      SubjectType.USER
    );

    String tokenValue_2 = spicedbClient.writeRelationship(
      RelationshipType.OWNER,
      ResourceType.CAA.getResourceType(),
      ResourceType.PLATFORM_RESOURCETYPE,
      AuthZConstants.PLATFORM_ID,
      SubjectType.PLATFORM
    );

    if (tokenValue_1 != null && tokenValue_2 != null) {
      return true;
    } else {
      return false;
    }
    //platform digital-sky administrator user<input/>
    //resource caa platform digital-sky
    //platform:digital-sky-platform#administrator@user:platform-user
    //platform_resource_type:caa#owner@platform:digital-sky-platform
  }

  /**This method allows for any resource type admin user to be created by a platform
   * admin
   */
  public static boolean createResoureTypeAdminByPlatformUser(
    ResourceType resourceType,
    String resourceID,
    String resourceAdminID,
    String platformAdminID
  ) {
    String tokenValue = null;

    //check permission and then create admin for the given resourceID
    boolean isPlatformAdmin = spicedbClient.checkPermission(
      Permission.SUPER_ADMIN,
      ResourceType.PLATFORM_RESOURCETYPE,
      resourceType.getResourceType(),
      SubjectType.USER,
      platformAdminID
    );

    //create admin for the provided resource
    if (isPlatformAdmin) {
      tokenValue = spicedbClient.writeRelationship(
        RelationshipType.ADMINISTRATOR,
        resourceID,
        resourceType,
        resourceAdminID,
        SubjectType.USER
      );
    }

    if(tokenValue != null){
      return true;
    } else {
      return false;
    }
    //caa:caa-authority#administrator@user:caa-user
  }

  /** Thois method is used to get CAA resource ID*/
  public static String getCAAResourceID() {
    //return the ID of the CCA resource in the system
    return AuthZConstants.CAA_RESOURCE_ID;
  }

  /**This method is used to create resource type admin when creating the resource */
  public static boolean createResoureTypeAdmin(
    ResourceType resourceType,
    String resourceID,
    String resourceAdminID
  ) {
    boolean isSuccess = false;
    String tokenValue = null;
    String operatorTokenValue = null;
   
    tokenValue = spicedbClient.writeRelationship(
      RelationshipType.ADMINISTRATOR,
      resourceID,
      resourceType,
      resourceAdminID,
      SubjectType.USER
    );

    if(tokenValue != null ){
      isSuccess = true;
    }

    if (ResourceType.OPERATOR.equals(resourceType)) {
      operatorTokenValue =spicedbClient.writeRelationship(
        RelationshipType.REGULATOR,
        resourceID,
        resourceType,
        getCAAResourceID(),
        SubjectType.CAA
      );

      if(tokenValue != null && operatorTokenValue !=null ){
        isSuccess = true;
      }
    }
    //operator:operator-1#administrator@user:operator-user
    //manufacturer:manufacturer-1#administrator@user:manufacturer-user
   
    return isSuccess;
  }

  public static boolean checkIsResourceAdmin(
  ResourceType resourceType,
  String resourceAdminID,
  String userID
  ){
    boolean isResourceAdmin = spicedbClient.checkPermission(
      Permission.SUPER_ADMIN,
      resourceType,
      resourceAdminID,
      SubjectType.USER,
      userID
    );

    return isResourceAdmin;
  }

  /**This methods created more than one relationships for UAS */
  public static void createUASRelationships(
    String UASID,
    String manufacturerID,
    String operatorID
  ) {
    /**Put additional checks for pre-condition on UAS*/

    spicedbClient.writeRelationship(
      RelationshipType.MANUFACTURER,
      UASID,
      ResourceType.UAS,
      manufacturerID,
      SubjectType.MANUFACTURER
    );

    spicedbClient.writeRelationship(
      RelationshipType.OWNER,
      UASID,
      ResourceType.UAS,
      operatorID,
      SubjectType.OPERATOR
    );

    spicedbClient.writeRelationship(
      RelationshipType.REGULATOR,
      UASID,
      ResourceType.UAS,
      getCAAResourceID(),
      SubjectType.CAA
    );
    //uas:uas-1#owner@operator:operator-1
    //uas:uas-1#manufacturer@manufacturer:manufacturer-1
    //uas:uas-1#regulator@caa:caa-authority

  }

  /** This method creates more than one realtionships for UASType */
  public static void createUASTypeRelationships(String UASTypeID, String manufacturerID) {
    spicedbClient.writeRelationship(
      RelationshipType.MANUFACTURER,
      UASTypeID,
      ResourceType.UASTYPE,
      manufacturerID,
      SubjectType.MANUFACTURER
    );

    spicedbClient.writeRelationship(
      RelationshipType.REGULATOR,
      UASTypeID,
      ResourceType.UASTYPE,
      getCAAResourceID(),
      SubjectType.CAA
    );
    //uastype:uastype-1#manufacturer@manufacturer:manufacturer-1
    //uastype:uastype-1#regulator@caa:caa-authority

  }

  /**This method is used to create relationsip of pilot on what resource type they own to
   * adminsiter. We are only providing for resource type defintion that prevent each entry in Auth
   * system and still helps us get auth restrictions by looking up the resource type  */
  public static void createPilotFlightOperationsResourceTypeRelationships(
    String pilotGroupID
  ) {
    spicedbClient.writeRelationship(
      RelationshipType.OWNER,
      ResourceType.FLIGHTPLAN.getResourceType(),
      ResourceType.FLIGHTOPERATIONS_RESOURCETYPE,
      pilotGroupID + "#member",
      SubjectType.PILOT
    );

    spicedbClient.writeRelationship(
      RelationshipType.OWNER,
      ResourceType.FLIGHTLOG.getResourceType(),
      ResourceType.FLIGHTOPERATIONS_RESOURCETYPE,
      pilotGroupID + "#member",
      SubjectType.PILOT
    );

    spicedbClient.writeRelationship(
      RelationshipType.OWNER,
      ResourceType.INCIDENTREPORT.getResourceType(),
      ResourceType.FLIGHTOPERATIONS_RESOURCETYPE,
      pilotGroupID + "#member",
      SubjectType.PILOT
    );
    //flightoperations_resource_type:flightplan#owner@pilot:operator-1-pilot-group#member
    //flightoperations_resource_type:flightlog#owner@pilot:operator-1-pilot-group#member
    //flightoperations_resource_type:incidentreport#owner@pilot:operator-1-pilot-group#member
    //flightoperations_resource_type:flightplan#owner@pilot:default-pilot-group#member
    //flightoperations_resource_type:flightlog#owner@pilot:operator-1-pilot-group#member
    //flightoperations_resource_type:incidentreport#owner@pilot:operator-1-pilot-group#member

  }

  /** This function allows us to check the resource type permission before creation
   * of the resource in the system
   */
  public static boolean checkPilotFlightOperationsResourceTypePermission(
    String pilotGroupID,
    Permission permission,
    ResourceType resourceType
  ) {
    boolean hasPermission = spicedbClient.checkPermission(
      permission,
      resourceType,
      resourceType.getResourceType(),
      SubjectType.PILOT,
      pilotGroupID
    );

    return hasPermission;
  }

  /** This function is used to add pilot user to a pilot group */
  public static boolean addPilotUserToDefaultGroup(String pilotUserID) {
    String pilotGroupID = AuthZConstants.DEFAULT_PILOT_GROUP;
    boolean isSuccess = false;

    String tokenValue = spicedbClient.writeRelationship(
      RelationshipType.MEMBER,
      pilotGroupID,
      ResourceType.PILOT,
      pilotUserID,
      SubjectType.USER
    );

    if(tokenValue != null){
      isSuccess = true;
    }

    return isSuccess;
    //pilot:default-pilot-group#member@user:pilot-user-2
  }

  /** This function is used to add pilot user to a pilot group */
  public static boolean addPilotUserToPilotOperatorGroup(
  String pilotUserID, 
  String operatorResourceID,
  String operatorUserID) {
    String operatorPilotGroupID = operatorResourceID+"-pilot-group";
    String tokenValue = null;
    boolean isSuccess = false;

    boolean addPilotUserToPilotGroup = spicedbClient.checkPermission(
      Permission.ADD_PILOT,
      ResourceType.OPERATOR,
      operatorResourceID,
      SubjectType.USER,
      operatorUserID
    );

    if(addPilotUserToPilotGroup){
      tokenValue = spicedbClient.writeRelationship(
      RelationshipType.MEMBER,
      operatorPilotGroupID,
      ResourceType.PILOT,
      pilotUserID,
      SubjectType.USER
      );
    }

    if(tokenValue != null){
      isSuccess = true;
    }
  
    return isSuccess;
    //pilot:operator-1-pilot-group#member@user:pilot-user
    
  }

  /** this function will be used to lookup the groups to which a pilot belongs to */
  public static void lookupPilotUserGroups(String pilotUserID) {
    /** this function will help in looking up pilot
     * across multiple groups */
  }

  /** This function will be used to lookup the UAS resource association with operators */
  public static void lookupUASResource(String UASResourceID) {}
}
