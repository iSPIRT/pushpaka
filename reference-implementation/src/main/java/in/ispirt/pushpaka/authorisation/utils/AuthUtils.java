package in.ispirt.pushpaka.authorisation.utils;

import in.ispirt.pushpaka.authorisation.Permission;
import in.ispirt.pushpaka.authorisation.RelationshipType;
import in.ispirt.pushpaka.authorisation.ResourceType;
import in.ispirt.pushpaka.authorisation.SubjectType;

public class AuthUtils {
    public static void createPlatformAdmin(String subjectID){
        //create relation of administrator
        //create relation with resources 

        SpicedbUtils.writeRelationship(RelationshipType.ADMINISTRATOR, 
        "digital-sky-platform", ResourceType.PLATFORM, subjectID, SubjectType.USER);

        SpicedbUtils.writeRelationship(RelationshipType.OWNER,
        ResourceType.CAA.getResourceType(), 
        ResourceType.RESOURCE, 
        "digital-sky-platform", 
        SubjectType.PLATFORM);

        //platform digital-sky administrator user<input/>
        //resource caa platform digital-sky
    }

    public static void createResoureTypeAdminByPlatformUser(ResourceType resourceType,
    String resourceID,
    String resourceAdminID,
    String platformAdminID){

        //check permission and then create admin for the given resourceID
        boolean isPlatformAdmin = SpicedbUtils.checkPermission(Permission.SUPER_ADMIN, 
        ResourceType.RESOURCE, resourceType.getResourceType(), SubjectType.USER, platformAdminID);

        //create admin for the provided resource 
        if(isPlatformAdmin){
            SpicedbUtils.writeRelationship(RelationshipType.ADMINISTRATOR, 
            resourceID, resourceType, resourceAdminID, SubjectType.USER);
        }
    }

    public static void createResoureTypeAdmin(ResourceType resourceType,
    String resourceID,
    String resourceAdminID){
    
        SpicedbUtils.writeRelationship(RelationshipType.ADMINISTRATOR, 
        resourceID, resourceType, resourceAdminID, SubjectType.USER);

        //extend relationship block
    }

    private static void extendUASRelationships() {

    }

    private static void extendcreateUASTypeRelationships() {

    }

    private static void extendOperatorRelationships() {

    }
}
