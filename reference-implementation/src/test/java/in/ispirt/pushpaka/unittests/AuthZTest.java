package in.ispirt.pushpaka.unittests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import in.ispirt.pushpaka.authorisation.ResourceType;
import in.ispirt.pushpaka.authorisation.utils.AuthZUtils;
import in.ispirt.pushpaka.authorisation.utils.SpicedbClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
//import org.junit.Test;
import org.junit.jupiter.api.Test;

public class AuthZTest {
  public static SpicedbClient spicedbClient;

  @BeforeClass
  public static void setup() {
    spicedbClient = SpicedbClient.getInstance(
        SpicedbClient.SPICEDDB_TARGET,
        SpicedbClient.SPICEDB_TOKEN);
  }

  @AfterClass
  public static void tearDown() {
    try {
      spicedbClient.shutdownChannel();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWriteSchema() {
    spicedbClient.writeSchema(SpicedbClient.SPICEDDB_PERMISSION_FILE);
    String schemaText = spicedbClient.readSchema();

    if (schemaText == null) {
      assertTrue(false);
    } else {
      assertTrue(schemaText.indexOf("platform_resource_type") > 0);
    }
  }

  @Test
  public void testCreatePlatformUser() {
    // platform:digital-sky-platform#administrator@user:platform-user
    // platform_resource_type:caa#owner@platform:digital-sky-platform

    boolean isSuccess = AuthZUtils.createPlatformAdmin("platform-user");
    assertTrue(isSuccess);
  }

  @Test
  public void testCreateCAAAdministrator() {
    // caa:caa-authority#administrator@user:caa-user
    String CAAResourceID = "caa-authority";
    String CAAResourceAdminID = AuthZUtils.getCAAResourceID();
    String platformAdminId = "platform-user";

    boolean isSuccess = AuthZUtils.createResoureTypeAdminByPlatformUser(
        ResourceType.CAA,
        CAAResourceID,
        CAAResourceAdminID,
        platformAdminId);

    assertTrue(isSuccess);
  }

  @Test
  public void testCreateManufacturerAdministrator() {
    // manufacturer:manufacturer-1#administrator@user:manufacturer-user
    String manufacturerResourceID = "manufacturer-1";
    String manufacturerAdminUserID = "manufacturer-user";

    boolean isSuccess = AuthZUtils.createResoureTypeAdmin(
        ResourceType.MANUFACTURER,
        manufacturerResourceID,
        manufacturerAdminUserID);

    assertTrue(isSuccess);
  }

  @Test
  public void testCreateOperatorAdministrator() {
    // operator:operator-1#administrator@user:operator-user
    // operator:operator-1#regulator@caa:caa-authority

    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user";

    boolean isSuccess = AuthZUtils.createResoureTypeAdmin(
        ResourceType.OPERATOR,
        operatorResourceID,
        operatorAdminUserID);

    assertTrue(isSuccess);
  }


  @Test
  public void testIsResourceAdministrator() {
    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user";

    boolean isSuccess = AuthZUtils.checkIsResourceAdmin(
        ResourceType.OPERATOR,
        operatorResourceID,
        operatorAdminUserID);

    assertTrue(isSuccess);
  }

  @Test
  @Test
  public void testIsResourceAdministratorNegative() {
    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user-1";

    boolean isSuccess = AuthZUtils.checkIsResourceAdmin(
        ResourceType.OPERATOR,
        operatorResourceID,
        operatorAdminUserID);

    assertTrue(isSuccess);
  }

  @Test
  public void testAddPilotToOperator() {
    // pilot:default-pilot-group#member@user:pilot-user-2
    String pilotUserID = "pilot-user-1";
    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user";

    boolean isSuccess = AuthZUtils.addPilotToOperator(
        pilotUserID,
        operatorResourceID,
        operatorAdminUserID);

    assertTrue(isSuccess);
  }

  @Test
  public void testAddPilotToOperatoNegative() {
    // pilot:default-pilot-group#member@user:pilot-user-2
    String pilotUserID = "pilot-user-1";
    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user-1";

    boolean isSuccess = AuthZUtils.addPilotToOperator(
        pilotUserID,
        operatorResourceID,
        operatorAdminUserID);

    assertFalse(isSuccess);
  }

  @Test
  public void testFlightOperationsAdmin() {
    String pilotUserID = "pilot-user-1";
    String operatorResourceID = "operator-1";


    boolean isSuccess = AuthZUtils.isFlightOperationsAdmin(
        pilotUserID,
        operatorResourceID);

    assertTrue(isSuccess);
  }

  @Test
  public void testFlightOperationsAdminNegative() {
    String pilotUserID = "pilot-user-2";
    String operatorResourceID = "operator-1";


    boolean isSuccess = AuthZUtils.isFlightOperationsAdmin(
        pilotUserID,
        operatorResourceID);

    assertFalse(isSuccess);
  }

  @Test
  public void testCreateUASRelationships() {
    String UASID = "uas-1";
    String UASID = "uas-1";
    String manufacturerResourceID = "manufacturer-1";
    String manufacturerAdminUserID = "manufacturer-user";

    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user-1";


    boolean isSuccess = AuthZUtils.createUASManufacturerRelationships(
        UASID,
        manufacturerResourceID,
        manufacturerAdminUserID);

    isSuccess = AuthZUtils.createUASOperatorRelationships(
        UASID,
        operatorResourceID,
        operatorAdminUserID);

    assertTrue(isSuccess);
  }

  @Test
  public void testCreateUASTypeRelationships() {
    String UASTypeID = "uastype-1";
    String UASTypeID = "uastype-1";
    String manufacturerResourceID = "manufacturer-1";
    String manufacturerAdminUserID = "manufacturer-user";


    boolean isSuccess = AuthZUtils.createUASTypeRelationships(
        UASTypeID,
        manufacturerResourceID,
        manufacturerAdminUserID);

    assertTrue(isSuccess);
  }
}
