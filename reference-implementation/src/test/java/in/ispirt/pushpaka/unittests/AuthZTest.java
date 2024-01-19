package in.ispirt.pushpaka.unittests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import in.ispirt.pushpaka.authorisation.ResourceType;
import in.ispirt.pushpaka.authorisation.utils.AuthZ;
import in.ispirt.pushpaka.authorisation.utils.SpicedbClient;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AuthZTest {
  public static AuthZ authZ;
  public static SpicedbClient spicedbClient;

  @BeforeAll
  public static void setup() {
    authZ = new AuthZ();
    spicedbClient = authZ.getSpicedbClient();
  }

  @AfterAll
  public static void tearDown() {
    authZ.shutdownChannel();
  }

  @Test
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
  public void testCreatePlatformUser() {
    // platform:digital-sky-platform#administrator@user:platform-user

    boolean isSuccess = authZ.createPlatformAdmin("platform-user");
    assertTrue(isSuccess);
  }

  @Test
  public void testAssociateCAAToPlatform() {
    // caa:caa-authority#platform@platform:digital-sky-platform

    String CAAResourceID = "caa-authority";
    boolean isSuccess = authZ.associateCAAToPlatform(CAAResourceID);

    assertTrue(isSuccess);
  }

  @Test
  public void testCreateCAAAdministrator() {
    // caa:caa-authority#administrator@user:caa-user

    String caaResourceID = "caa-authority";
    String caaResourceAdminID = "caa-user";
    String platformAdminId = "platform-user";

    boolean isSuccess = authZ.createCAAAdmin(
      caaResourceID,
      caaResourceAdminID,
      platformAdminId
    );

    assertTrue(isSuccess);
  }

  @Test
  public void testCreateManufacturerAdministrator() {
    // manufacturer:manufacturer-1#administrator@user:manufacturer-user
    String manufacturerResourceID = "manufacturer-1";
    String manufacturerAdminUserID = "manufacturer-user";

    boolean isSuccess = authZ.createResoureTypeAdmin(
      ResourceType.MANUFACTURER,
      manufacturerResourceID,
      manufacturerAdminUserID
    );

    assertTrue(isSuccess);
  }

  @Test
  public void testCreateTraderAdministrator() {
    // manufacturer:manufacturer-1#administrator@user:manufacturer-user
    String traderResourceID = "trader-1";
    String traderAdminUserID = "trader-user";

    boolean isSuccess = authZ.createResoureTypeAdmin(
      ResourceType.TRADER,
      traderResourceID,
      traderAdminUserID
    );

    assertTrue(isSuccess);
  }

  @Test
  public void testCreateDSSPAdministrator() {
    // manufacturer:manufacturer-1#administrator@user:manufacturer-user
    String dsspResourceID = "dssp-1";
    String dsspAdminUserID = "dssp-user";

    boolean isSuccess = authZ.createResoureTypeAdmin(
      ResourceType.DSSP,
      dsspResourceID,
      dsspAdminUserID
    );

    assertTrue(isSuccess);
  }

  @Test
  public void testCreateRepairAgencyAdministrator() {
    // manufacturer:manufacturer-1#administrator@user:manufacturer-user
    String repairAgencyResourceID = "repairagency-1";
    String repairAgencyAdminUserID = "repairagency-user";

    boolean isSuccess = authZ.createResoureTypeAdmin(
      ResourceType.REPAIRAGENCY,
      repairAgencyResourceID,
      repairAgencyAdminUserID
    );

    assertTrue(isSuccess);
  }

  @Test
  public void testCreateOperatorAdministrator() {
    // operator:operator-1#administrator@user:operator-user
    // operator:operator-1#regulator@caa:caa-authority

    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user";

    boolean isSuccess = authZ.createResoureTypeAdmin(
      ResourceType.OPERATOR,
      operatorResourceID,
      operatorAdminUserID
    );

    assertTrue(isSuccess);
  }

  @Test
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
  public void testAddPilotToOperator() {
    // pilot:default-pilot-group#member@user:pilot-user-2
    String pilotUserID = "pilot-user-1";
    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user";

    boolean isSuccess = authZ.addPilotToOperator(
      pilotUserID,
      operatorResourceID,
      operatorAdminUserID
    );

    assertTrue(isSuccess);
  }

  @Test
  public void testRemovePilotToOperator() {
    // pilot:default-pilot-group#member@user:pilot-user-2
    String pilotUserID = "pilot-user-1";
    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user";

    boolean isSuccess = authZ.removePilotFromOperator(
      pilotUserID,
      operatorResourceID,
      operatorAdminUserID
    );

    assertTrue(isSuccess);
  }

  @Test
  public void testAddPilotToOperatoNegative() {
    // pilot:default-pilot-group#member@user:pilot-user-2
    String pilotUserID = "pilot-user-1";
    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user-1";

    boolean isSuccess = authZ.addPilotToOperator(
      pilotUserID,
      operatorResourceID,
      operatorAdminUserID
    );

    assertFalse(isSuccess);
  }

  @Test
  public void testFlightOperationsAdmin() {
    String pilotUserID = "pilot-user-1";
    String operatorResourceID = "operator-1";

    boolean isSuccess = authZ.isFlightOperationsAdmin(pilotUserID, operatorResourceID);

    assertTrue(isSuccess);
  }

  @Test
  public void testFlightOperationsAdminNegative() {
    String pilotUserID = "pilot-user-2";
    String operatorResourceID = "operator-1";

    boolean isSuccess = authZ.isFlightOperationsAdmin(pilotUserID, operatorResourceID);

    assertFalse(isSuccess);
  }

  @Test
  public void testCreateUASRelationships() {
    String UASID = "uas-1";
    String manufacturerResourceID = "manufacturer-1";
    String manufacturerAdminUserID = "manufacturer-user";

    String operatorResourceID = "operator-1";
    String operatorAdminUserID = "operator-user";

    boolean isSuccess = authZ.createUASManufacturerRelationships(
      UASID,
      manufacturerResourceID,
      manufacturerAdminUserID
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
  public void testCreateUASTypeRelationships() {
    String UASTypeID = "uastype-1";
    String manufacturerResourceID = "manufacturer-1";
    String manufacturerAdminUserID = "manufacturer-user";

    boolean isSuccess = authZ.createUASTypeRelationships(
      UASTypeID,
      manufacturerResourceID,
      manufacturerAdminUserID
    );

    assertTrue(isSuccess);
  }

  @Test
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
  public void testLookupUASResourceOwnership() {
    String UASID = "uas-1";
    String manufacturerResourceID = "manufacturer-1";

    boolean isSuccess = authZ.lookupUASResourceOwnership(UASID, manufacturerResourceID);

    assertTrue(isSuccess);
  }

  @Test
  public void testLookupUASResourceOwnershipNegative() {
    String UASID = "uas";
    String manufacturerResourceID = "manufacturer-1";

    boolean isSuccess = authZ.lookupUASResourceOwnership(UASID, manufacturerResourceID);

    assertFalse(isSuccess);
  }

  @Test
  public void testLookupResourcesForRegulatorApprovals() {
    String caaResourceAdminID = "caa-user";

    Set<String> resourceIdSetForApproval = authZ.lookupResourcesForRegulatorApproval(
      caaResourceAdminID
    );

    assertTrue(resourceIdSetForApproval.size() > 0);
  }

  @Test
  public void testPilotToOperators() {
    String pilotUserID = "pilot-user-1";

    Set<String> pilotToOperators = authZ.lookupPilotResource(pilotUserID);

    assertTrue(pilotToOperators.size() > 0);
  }
}
