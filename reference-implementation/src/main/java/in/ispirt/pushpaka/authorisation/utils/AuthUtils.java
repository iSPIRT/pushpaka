package in.ispirt.pushpaka.authorisation.utils;

import in.ispirt.pushpaka.authorisation.Permission;
import in.ispirt.pushpaka.authorisation.RelationshipType;
import in.ispirt.pushpaka.authorisation.ResourceType;
import in.ispirt.pushpaka.authorisation.SubjectType;

public class AuthUtils {
    /**This method is used to create a platform admin with a buil-in seed for the platform
     * This method also has a built-in seed for a resource type CAA that implies that 
     * the platform admin has access to resource type CAA and administer it
     */
    public static void createPlatformAdmin(String subjectID){
        //create relation of administrator
        //create relation with resource type

        SpicedbUtils.writeRelationship(RelationshipType.ADMINISTRATOR, 
        "digital-sky-platform", ResourceType.PLATFORM, subjectID, SubjectType.USER);

        SpicedbUtils.writeRelationship(RelationshipType.OWNER,
        ResourceType.CAA.getResourceType(), 
        ResourceType.RESOURCETYPE, 
        "digital-sky-platform", 
        SubjectType.PLATFORM);

        //platform digital-sky administrator user<input/>
        //resource caa platform digital-sky
    }

    /**This method allows for any resource type admin user to be created by a platform 
     * admin
     */
    public static void createResoureTypeAdminByPlatformUser(ResourceType resourceType,
    String resourceID,
    String resourceAdminID,
    String platformAdminID){

        //check permission and then create admin for the given resourceID
        boolean isPlatformAdmin = SpicedbUtils.checkPermission(Permission.SUPER_ADMIN, 
        ResourceType.RESOURCETYPE, resourceType.getResourceType(), SubjectType.USER, platformAdminID);

        //create admin for the provided resource 
        if(isPlatformAdmin){
            SpicedbUtils.writeRelationship(RelationshipType.ADMINISTRATOR, 
            resourceID, resourceType, resourceAdminID, SubjectType.USER);
        }
    }

    /** Thois method is used to get CAA resource ID*/
    public static String getCAAResourceID(){
        //return the ID of the CCA resource in the system 
        return null;
    }

    /**This method is used to created resource type admin when creating the resource */
    public static void createResoureTypeAdmin(ResourceType resourceType,
    String resourceID,
    String resourceAdminID){
           
        SpicedbUtils.writeRelationship(RelationshipType.ADMINISTRATOR, 
        resourceID, resourceType, resourceAdminID, SubjectType.USER);

        if(ResourceType.OPERATOR.equals(resourceType)){
            SpicedbUtils.writeRelationship(RelationshipType.REGULATOR, 
            getCAAResourceID(), ResourceType.CAA, resourceID, SubjectType.OPERATOR);
        } 

    }

    /**This methods created more than one relationships for UAS */
    public static void createUASRelationships(String UASID,
    String manufacturerID,
    String operatorID) {

        SpicedbUtils.writeRelationship(RelationshipType.MANUFACTURER, 
        UASID, ResourceType.UAS, manufacturerID, SubjectType.MANUFACTURER);

        SpicedbUtils.writeRelationship(RelationshipType.OWNER, 
        UASID, ResourceType.UAS, operatorID, SubjectType.OPERATOR);

        SpicedbUtils.writeRelationship(RelationshipType.REGULATOR, 
        UASID, ResourceType.UAS, getCAAResourceID(), SubjectType.CAA);

    }

    /** This method creates more than one realtionships for UASType */
    public static void createUASTypeRelationships(String UASTypeID,
    String manufacturerID) {
        SpicedbUtils.writeRelationship(RelationshipType.MANUFACTURER, 
        UASTypeID, ResourceType.UASTYPE, manufacturerID, SubjectType.MANUFACTURER);

         SpicedbUtils.writeRelationship(RelationshipType.REGULATOR, 
        UASTypeID, ResourceType.UASTYPE, getCAAResourceID(), SubjectType.CAA);

    }

    /**This method is used to create relationsip of pilot on what resource type they own to 
     * adminsiter. We are only providing for resource type defintion that prevent each entry in Auth 
     * system and still helps us get auth restrictions by looking up the resource type  */
    public static void createPilotFlightOperationsResourceTypeRelationships(String pilotID){

        SpicedbUtils.writeRelationship(RelationshipType.OWNER,
        ResourceType.FLIGHTPLAN.getResourceType(), 
        ResourceType.FLIGHTOPERATIONS_RESOURCETYPE, 
        pilotID, 
        SubjectType.PILOT);

        SpicedbUtils.writeRelationship(RelationshipType.OWNER,
        ResourceType.FLIGHTLOG.getResourceType(), 
        ResourceType.FLIGHTOPERATIONS_RESOURCETYPE, 
        pilotID, 
        SubjectType.PILOT);

        SpicedbUtils.writeRelationship(RelationshipType.OWNER,
        ResourceType.INCIDENTREPORT.getResourceType(), 
        ResourceType.FLIGHTOPERATIONS_RESOURCETYPE, 
        pilotID, 
        SubjectType.PILOT);
    }

    /** This function allows us to check the resource type permission before creation
     * of the resource in the system 
     */
    public static boolean checkPilotFlightOperationsResourceTypePermission(String pilotID,
    Permission permission,
    ResourceType resourceType){

        boolean hasPermission = SpicedbUtils.checkPermission(permission, 
        resourceType, resourceType.getResourceType() , SubjectType.PILOT, pilotID);

        return hasPermission;

    }
}
