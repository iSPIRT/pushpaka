package in.ispirt.pushpaka.unittests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import in.ispirt.pushpaka.authorisation.ResourceType;
import in.ispirt.pushpaka.authorisation.utils.AuthZ;
import in.ispirt.pushpaka.authorisation.utils.SpicedbClient;
import in.ispirt.pushpaka.dao.seeds.Seeds;
import java.util.ArrayList;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthZTest {
  public static AuthZ authZ;
  public static SpicedbClient spicedbClient;

  @BeforeAll
  public static void setup() {
    authZ = new AuthZ();
    spicedbClient = authZ.getSpicedbClient();
    authZ.setCaaResourceID(Seeds.SPICEDB_CAA_RESOURCE_ID);
    // Schema must be loaded before any permission checks can succeed.
    // testWriteSchema verifies this succeeded; we load here to guarantee ordering.
    spicedbClient.writeSchema(SpicedbClient.SPICEDDB_PERMISSION_FILE);
  }

  @AfterAll
  public static void tearDown() {
    authZ.shutdownChannel();
  }

  @Test
  @Order(1)
  public void testWriteSchema() {
    boolean isSuccess = spicedbClient.writeSchema(SpicedbClient.SPICEDDB_PERMISSION_FILE);
    String schemaText = spicedbClient.readSchema();

    if (schemaText == null || !(isSuccess)) {
      assertTrue(false);
    } else {
      assertTrue(schemaText.indexOf("platform") > 0);
    }
  }

  @Test
  @Order(2)
  public void testCreatePlatformUser() {
    // platform:digital-sky-platform#administrator@user:platform-user

    boolean isSuccess = authZ.createPlatformAdmin(Seeds.SPICEDB_PLATFORM_USER_ID);
    assertTrue(isSuccess);
  }

  @Test
  @Order(3)
  public void testAssociateCAAToPlatform() {
    // caa:caa-authority#platform@platform:digital-sky-platform

    boolean isSuccess = authZ.associateCAAToPlatform(authZ.getCaaResourceID());

    assertTrue(isSuccess);
  }

  @Test
  @Order(4)
  public void testCreateCAAAdministrator() {
    // caa:caa-authority#administrator@user:caa-user

    String caaResourceAdminID = Seeds.SPICEDB_CAA_ADMIN_USER_ID;
    String platformAdminId = Seeds.SPICEDB_PLATFORM_USER_ID;

    boolean isSuccess = authZ.createCAAAdmin(
      authZ.getCaaResourceID(),
      caaResourceAdminID,
      platformAdminId
    );

    assertTrue(isSuccess);
  }

  @Test
  @Order(5)
  public void testCreateManufacturerAdministrator() {
    // manufacturer:manufacturer-1#administrator@user:manufacturer-user
    String manufacturerResourceID = Seeds.SPICEDB_MANUFACTURER_RESOURCE_ID;
    String manufacturerAdminUserID = Seeds.SPICEDB_MANUFACTURER_ADMIN_USER_ID;

    boolean isSuccess = authZ.createResoureTypeAdmin(
      ResourceType.MANUFACTURER,
      manufacturerResourceID,
      manufacturerAdminUserID,
      authZ.getCaaResourceID()
    );

    assertTrue(isSuccess);
  }

  @Test
  @Order(6)
  public void testCreateTraderAdministrator() {
    String traderResourceID = Seeds.SPICEDB_TRADER_RESOURCE_ID;
    String traderAdminUserID = Seeds.SPICEDB_TRADER_ADMIN_USER_ID;

    boolean isSuccess = authZ.createResoureTypeAdmin(
      ResourceType.TRADER,
      traderResourceID,
      traderAdminUserID,
      authZ.getCaaResourceID()
    );

    assertTrue(isSuccess);
  }

  @Test
  @Order(7)
  public void testCreateDSSPAdministrator() {
    String dsspResourceID = Seeds.SPICEDB_DSSP_RESOURCE_ID;
    String dsspAdminUserID = Seeds.SPICEDB_DSSP_ADMIN_USER_ID;

    boolean isSuccess = authZ.createResoureTypeAdmin(
      ResourceType.DSSP,
      dsspResourceID,
      dsspAdminUserID,
      authZ.getCaaResourceID()
    );

    assertTrue(isSuccess);
  }

  @Test
  @Order(8)
  public void testCreateRepairAgencyAdministrator() {
    String repairAgencyResourceID = Seeds.SPICEDB_REPAIR_AGENCY_RESOURCE_ID;
    String repairAgencyAdminUserID = Seeds.SPICEDB_REPAIR_AGENCY_ADMIN_USER_ID;

    boolean isSuccess = authZ.createResoureTypeAdmin(
      ResourceType.REPAIRAGENCY,
      repairAgencyResourceID,
      repairAgencyAdminUserID,
      authZ.getCaaResourceID()
    );

    assertTrue(isSuccess);
  }

  @Test
  @Order(9)
  public void testCreateOperatorAdministrator() {
    // operator:operator-1#administrator@user:operator-user
    // operator:operator-1#regulator@caa:caa-authority

    String operatorResourceID = Seeds.SPICEDB_OPERATOR_RESOURCE_ID;
    String operatorAdminUserID = Seeds.SPICEDB_OPERATOR_ADMIN_USER_ID;

    boolean isSuccess = authZ.createResoureTypeAdmin(
      ResourceType.OPERATOR,
      operatorResourceID,
      operatorAdminUserID,
      authZ.getCaaResourceID()
    );

    assertTrue(isSuccess);
  }

  @Test
  @Order(10)
  public void testIsResourceAdministrator() {
    String operatorResourceID = Seeds.SPICEDB_OPERATOR_RESOURCE_ID;
    String operatorAdminUserID = Seeds.SPICEDB_OPERATOR_ADMIN_USER_ID;

    boolean isSuccess = authZ.checkIsResourceAdmin(
      ResourceType.OPERATOR,
      operatorResourceID,
      operatorAdminUserID
    );

    assertTrue(isSuccess);
  }

  @Test
  @Order(11)
  public void testIsResourceAdministratorNegative() {
    String operatorResourceID = Seeds.SPICEDB_OPERATOR_RESOURCE_ID;
    String operatorAdminUserID = "operator-user-1"; // intentionally wrong user for negative test

    boolean isSuccess = authZ.checkIsResourceAdmin(
      ResourceType.OPERATOR,
      operatorResourceID,
      operatorAdminUserID
    );

    assertFalse(isSuccess);
  }

  @Test
  @Order(12)
  public void testAddFirstPilotToOperator() {
    // pilot:default-pilot-group#member@user:pilot-user-2
    String pilotResourceID = Seeds.SPICEDB_PILOT_RESOURCE_1_ID;
    String pilotUserID = Seeds.SPICEDB_PILOT_USER_1_ID;
    String operatorResourceID = Seeds.SPICEDB_OPERATOR_RESOURCE_ID;
    String operatorAdminUserID = Seeds.SPICEDB_OPERATOR_ADMIN_USER_ID;

    boolean addPilot = authZ.addPilot(
      pilotResourceID,
      pilotUserID,
      authZ.getCaaResourceID()
    );

    boolean isSuccess = authZ.addPilotToOperator(
      pilotResourceID,
      operatorResourceID,
      operatorAdminUserID
    );

    boolean isPilotflightOperationsAdmin = authZ.isFlightOperationsAdmin(
      pilotUserID,
      operatorResourceID
    );

    assertTrue(addPilot && isSuccess && isPilotflightOperationsAdmin);
  }

  @Test
  @Order(13)
  public void testAddSecondPilotToOperator() {
    // pilot:default-pilot-group#member@user:pilot-user-2
    String pilotResourceID = Seeds.SPICEDB_PILOT_RESOURCE_2_ID;
    String pilotUserID = Seeds.SPICEDB_PILOT_USER_2_ID;
    String operatorResourceID = Seeds.SPICEDB_OPERATOR_RESOURCE_ID;
    String operatorAdminUserID = Seeds.SPICEDB_OPERATOR_ADMIN_USER_ID;

    boolean addPilot = authZ.addPilot(
      pilotResourceID,
      pilotUserID,
      authZ.getCaaResourceID()
    );

    boolean isSuccess = authZ.addPilotToOperator(
      pilotResourceID,
      operatorResourceID,
      operatorAdminUserID
    );

    boolean isPilotflightOperationsAdmin = authZ.isFlightOperationsAdmin(
      pilotUserID,
      operatorResourceID
    );

    assertTrue(addPilot && isSuccess && isPilotflightOperationsAdmin);
  }

  @Test
  @Order(14)
  public void testRemoveFirstPilotToOperator() {
    String pilotResourceID = Seeds.SPICEDB_PILOT_RESOURCE_1_ID;
    String operatorResourceID = Seeds.SPICEDB_OPERATOR_RESOURCE_ID;
    String operatorAdminUserID = Seeds.SPICEDB_OPERATOR_ADMIN_USER_ID;

    boolean isSuccess = authZ.removePilotFromOperator(
      pilotResourceID,
      operatorResourceID,
      operatorAdminUserID
    );

    assertTrue(isSuccess);
  }

  @Test
  @Order(15)
  public void testAddPilotToOperatoNegative() {
    String pilotResourceID = "pilot-resource"; // intentionally unknown resource
    String operatorResourceID = Seeds.SPICEDB_OPERATOR_RESOURCE_ID;
    String operatorAdminUserID = "operator-user-1"; // intentionally wrong admin

    boolean isSuccess = authZ.addPilotToOperator(
      pilotResourceID,
      operatorResourceID,
      operatorAdminUserID
    );

    assertFalse(isSuccess);
  }

  @Test
  @Order(16)
  public void testFlightOperationsAdminNegative() {
    String pilotUserID = "pilot-user-3"; // intentionally unknown pilot
    String operatorResourceID = Seeds.SPICEDB_OPERATOR_RESOURCE_ID;

    boolean isSuccess = authZ.isFlightOperationsAdmin(pilotUserID, operatorResourceID);

    assertFalse(isSuccess);
  }

  @Test
  @Order(17)
  public void testCreateUASRelationships() {
    String UASID = Seeds.SPICEDB_UAS_RESOURCE_ID;
    String manufacturerResourceID = Seeds.SPICEDB_MANUFACTURER_RESOURCE_ID;
    String manufacturerAdminUserID = Seeds.SPICEDB_MANUFACTURER_ADMIN_USER_ID;

    String operatorResourceID = Seeds.SPICEDB_OPERATOR_RESOURCE_ID;
    String operatorAdminUserID = Seeds.SPICEDB_OPERATOR_ADMIN_USER_ID;

    boolean isSuccess = authZ.createUASManufacturerRelationships(
      UASID,
      manufacturerResourceID,
      manufacturerAdminUserID,
      authZ.getCaaResourceID()
    );

    isSuccess =
      authZ.createUASOperatorRelationships(
        UASID,
        operatorResourceID,
        operatorAdminUserID
      );

    assertTrue(isSuccess);
  }

  @Test
  @Order(18)
  public void testCreateUASTypeRelationships() {
    String UASTypeID = Seeds.SPICEDB_UAS_TYPE_RESOURCE_ID;
    String manufacturerResourceID = Seeds.SPICEDB_MANUFACTURER_RESOURCE_ID;
    String manufacturerAdminUserID = Seeds.SPICEDB_MANUFACTURER_ADMIN_USER_ID;

    boolean isSuccess = authZ.createUASTypeRelationships(
      UASTypeID,
      manufacturerResourceID,
      manufacturerAdminUserID,
      authZ.getCaaResourceID()
    );

    assertTrue(isSuccess);
  }

  @Test
  @Order(19)
  public void testApproveManufacturer() {
    String manufacturerResourceID = Seeds.SPICEDB_MANUFACTURER_RESOURCE_ID;
    String caaResourceAdminID = Seeds.SPICEDB_CAA_ADMIN_USER_ID;

    boolean isApprover = authZ.approveResourceByRegulator(
      ResourceType.MANUFACTURER,
      manufacturerResourceID,
      caaResourceAdminID
    );

    assertTrue(isApprover);
  }

  @Test
  @Order(20)
  public void testApproveOperator() {
    String operatorResourceID = Seeds.SPICEDB_OPERATOR_RESOURCE_ID;
    String caaResourceAdminID = Seeds.SPICEDB_CAA_ADMIN_USER_ID;

    boolean isApprover = authZ.approveResourceByRegulator(
      ResourceType.OPERATOR,
      operatorResourceID,
      caaResourceAdminID
    );

    assertTrue(isApprover);
  }

  @Test
  @Order(21)
  public void testApproveDSSP() {
    String dsspResourceID = Seeds.SPICEDB_DSSP_RESOURCE_ID;
    String caaResourceAdminID = Seeds.SPICEDB_CAA_ADMIN_USER_ID;

    boolean isApprover = authZ.approveResourceByRegulator(
      ResourceType.DSSP,
      dsspResourceID,
      caaResourceAdminID
    );

    assertTrue(isApprover);
  }

  @Test
  @Order(22)
  public void testApproveTrader() {
    String traderResourceID = Seeds.SPICEDB_TRADER_RESOURCE_ID;
    String caaResourceAdminID = Seeds.SPICEDB_CAA_ADMIN_USER_ID;

    boolean isApprover = authZ.approveResourceByRegulator(
      ResourceType.TRADER,
      traderResourceID,
      caaResourceAdminID
    );

    assertTrue(isApprover);
  }

  @Test
  @Order(23)
  public void testApproveRepairAgency() {
    String repairAgencyResourceID = Seeds.SPICEDB_REPAIR_AGENCY_RESOURCE_ID;
    String caaResourceAdminID = Seeds.SPICEDB_CAA_ADMIN_USER_ID;

    boolean isApprover = authZ.approveResourceByRegulator(
      ResourceType.REPAIRAGENCY,
      repairAgencyResourceID,
      caaResourceAdminID
    );

    assertTrue(isApprover);
  }

  @Test
  @Order(24)
  public void testLookupUASResourceOwnership() {
    String UASID = Seeds.SPICEDB_UAS_RESOURCE_ID;
    String manufacturerResourceID = Seeds.SPICEDB_MANUFACTURER_RESOURCE_ID;

    boolean isSuccess = authZ.lookupUASResourceManufacturerOwnership(
      UASID,
      manufacturerResourceID
    );

    assertTrue(isSuccess);
  }

  @Test
  @Order(25)
  public void testLookupUASResourceOwnershipNegative() {
    String UASID = "uas"; // intentionally unknown UAS
    String manufacturerResourceID = Seeds.SPICEDB_MANUFACTURER_RESOURCE_ID;

    boolean isSuccess = authZ.lookupUASResourceManufacturerOwnership(
      UASID,
      manufacturerResourceID
    );

    assertFalse(isSuccess);
  }

  @Test
  @Order(26)
  public void testLookupResourcesForRegulatorApprovals() {
    String caaResourceAdminID = Seeds.SPICEDB_CAA_ADMIN_USER_ID;

    Set<String> resourceIdSetForApproval = authZ.lookupResourcesForRegulatorApproval(
      caaResourceAdminID
    );

    assertTrue(resourceIdSetForApproval.size() > 0);
  }

  @Test
  @Order(27)
  public void testPilotToOperators() {
    String pilotResourceID = Seeds.SPICEDB_PILOT_RESOURCE_2_ID;

    Set<String> pilotToOperators = authZ.lookupPilotResource(pilotResourceID);
    System.out.println(pilotToOperators);
    assertTrue(pilotToOperators.size() > 0);
  }

  @Test
  @Order(28)
  public void testLookupRegulator() {
    boolean isSuccess = authZ.lookupRegulator(authZ.getCaaResourceID());

    assertTrue(isSuccess);
  }

  @Test
  @Order(29)
  public void testBulkExportRelationshipsCount() {
    int relationshipCount = authZ.lookupRelationshipsCount();

    assert (relationshipCount > 0);
  }

  @Test
  @Order(30)
  public void testBulkExportRelationships() {
    ArrayList<String> relationships = authZ.lookupRelationship();
    int relationshipsCount = relationships.size();

    assertTrue(relationshipsCount > 0);
  }
}
