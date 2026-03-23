package in.ispirt.pushpaka.unittests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import in.ispirt.pushpaka.authorisation.ResourceType;
import in.ispirt.pushpaka.authorisation.utils.AuthZ;
import in.ispirt.pushpaka.authorisation.utils.SpicedbClient;
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
    authZ.setCaaResourceID("caa-authority");
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

    boolean isSuccess = authZ.createPlatformAdmin("platform-user");
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

    String caaResourceAdminID = "caa-user";
    String platformAdminId = "platform-user";

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
    String manufacturerResourceID = "manufacturer-1";
    String manufacturerAdminUserID = "manufacturer-user";

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
    String traderResourceID = "trader-1";
    String traderAdminUserID = "trader-user";

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
    String dsspResourceID = "dssp-1";
    String dsspAdminUserID = "dssp-user";

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
    String repairAgencyResourceID = "repairagency-1";
    String repairAgencyAdminUserID = "repairagency-user";

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

    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user";

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
    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user";

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
    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user-1";

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
    String pilotResourceID = "pilot-resource-1";
    String pilotUserID = "pilot-user-1";
    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user";

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
    String pilotResourceID = "pilot-resource-2";
    String pilotUserID = "pilot-user-2";
    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user";

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
    // pilot:default-pilot-group#member@user:pilot-user-
    String pilotResourceID = "pilot-resource-1";
    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user";

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
    // pilot:default-pilot-group#member@user:pilot-user-2
    String pilotResourceID = "pilot-resource";
    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user-1";

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
    String pilotUserID = "pilot-user-3";
    String operatorResourceID = "operator-1";

    boolean isSuccess = authZ.isFlightOperationsAdmin(pilotUserID, operatorResourceID);

    assertFalse(isSuccess);
  }

  @Test
  @Order(17)
  public void testCreateUASRelationships() {
    String UASID = "uas-1";
    String manufacturerResourceID = "manufacturer-1";
    String manufacturerAdminUserID = "manufacturer-user";

    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user";

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
    String UASTypeID = "uastype-1";
    String manufacturerResourceID = "manufacturer-1";
    String manufacturerAdminUserID = "manufacturer-user";

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
    String manufacturerResourceID = "manufacturer-1";
    String caaResourceAdminID = "caa-user";

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
    String operatorResourceID = "operator-1";
    String caaResourceAdminID = "caa-user";

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
    String dsspResourceID = "dssp-1";
    String caaResourceAdminID = "caa-user";

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
    String traderResourceID = "trader-1";
    String caaResourceAdminID = "caa-user";

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
    String repairAgencyResourceID = "repairagency-1";
    String caaResourceAdminID = "caa-user";

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
    String UASID = "uas-1";
    String manufacturerResourceID = "manufacturer-1";

    boolean isSuccess = authZ.lookupUASResourceManufacturerOwnership(
      UASID,
      manufacturerResourceID
    );

    assertTrue(isSuccess);
  }

  @Test
  @Order(25)
  public void testLookupUASResourceOwnershipNegative() {
    String UASID = "uas";
    String manufacturerResourceID = "manufacturer-1";

    boolean isSuccess = authZ.lookupUASResourceManufacturerOwnership(
      UASID,
      manufacturerResourceID
    );

    assertFalse(isSuccess);
  }

  @Test
  @Order(26)
  public void testLookupResourcesForRegulatorApprovals() {
    String caaResourceAdminID = "caa-user";

    Set<String> resourceIdSetForApproval = authZ.lookupResourcesForRegulatorApproval(
      caaResourceAdminID
    );

    assertTrue(resourceIdSetForApproval.size() > 0);
  }

  @Test
  @Order(27)
  public void testPilotToOperators() {
    String pilotResourceID = "pilot-resource-2";

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
