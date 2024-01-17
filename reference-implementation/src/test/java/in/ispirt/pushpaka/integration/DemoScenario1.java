package in.ispirt.pushpaka.integration;

import com.nimbusds.jwt.SignedJWT;
import in.ispirt.pushpaka.authorisation.utils.AuthZ;
import in.ispirt.pushpaka.authorisation.utils.SpicedbClient;
import in.ispirt.pushpaka.utils.Logging;
import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;
import org.apache.http.client.ClientProtocolException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Scenario 1: End to end Registration service workflow
 * a. Explain that there are different personas (or user types) in Digital Sky
 * and
 * their inter-relationships
 * 1. Civil Aviation Authority
 * 2. Manufacturers
 * 3. Operators
 * 4. Pilots
 * 5. DSSPs
 * 6. Repair Agencies
 * 7. Trader
 * 8. UAS
 * b. Demonstrate the VLoS drone registration process at point of sale where
 * manufacturerID, TraderID, OperatorID, DroneID come together and register a
 * drone.
 */
public class DemoScenario1 {
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

  @BeforeEach
  void cleanup() {
    // Logging.info("cleanup entities started");
    // in.ispirt.pushpaka.registry.dao.Dao.deleteAll(in.ispirt.pushpaka.registry.dao.DaoInstance.getInstance().getSession());
    // Logging.info("cleanup entities completed");
  }

  // Scenario 1.a.1 Civil Aviation Authority
  @Test
  public void testScenario_1_a_1()
    throws ClientProtocolException, IOException, ParseException {
    String jwtPlatformAdmin = TestUtils.loginPlatformAdminUser();

    UUID uidPlatformAdmin = TestUtils.userCreate(jwtPlatformAdmin);
    boolean platformGrantCreated = TestUtils.grantPlatformAdmin(authZ, uidPlatformAdmin);
    Logging.info("platform grant created : " + platformGrantCreated);

    String jwtCaaAdmin = TestUtils.loginCaaAdminUser();

    try {
      SignedJWT jwtsCaaAdmin = TestUtils.parseJwt(jwtCaaAdmin);
      TestUtils.assertJwt(jwtsCaaAdmin);
      UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());
      Logging.info("id Caa Admin: " + idCaaAdmin.toString());

      UUID leid = TestUtils.legalEntityCreate(jwtCaaAdmin, UUID.randomUUID());
      UUID mid = TestUtils.civilAviationAuthorityCreate(
        jwtCaaAdmin,
        UUID.randomUUID(),
        leid
      );

      boolean associateCAAToPlatform = TestUtils.associateCAAToPlatform(authZ, leid);
      boolean caaAdminGrantCreated = TestUtils.grantCAAAdmin(
        authZ,
        mid,
        uidPlatformAdmin,
        idCaaAdmin
      );

      Logging.info("association with platform created : " + associateCAAToPlatform);
      Logging.info("CAA admin grant created : " + caaAdminGrantCreated);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // Scenario 1.a.2 Manufacturers
  @Test
  public void testScenario_1_a_2()
    throws ClientProtocolException, IOException, ParseException {
    String jwtCaaAdmin = TestUtils.loginCaaAdminUser();
    String jwtManufacturerAdmin = TestUtils.loginManufacturerAdminUser();
    UUID uidManufacturerAdmin = TestUtils.userCreate(jwtManufacturerAdmin);
    try {
      SignedJWT jwtsCaaAdmin = TestUtils.parseJwt(jwtCaaAdmin);
      TestUtils.assertJwt(jwtsCaaAdmin);
      UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());
      UUID leid = TestUtils.legalEntityCreate(jwtManufacturerAdmin, UUID.randomUUID());
      UUID mid = TestUtils.manufacturerCreate(jwtManufacturerAdmin, leid);

      boolean isManufacturerAdminGranted = TestUtils.grantManufacturerAdmin(
        authZ,
        mid,
        uidManufacturerAdmin
      );
      boolean isManufacturerApproved = TestUtils.approveManufacturer(
        authZ,
        uidManufacturerAdmin,
        idCaaAdmin
      );

      Logging.info("manufacturer admin granted : " + isManufacturerAdminGranted);
      Logging.info("manufacturer approved : " + isManufacturerApproved);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // Scenario 1.a.3 Operators
  @Test
  public void testScenario_1_a_3()
    throws ClientProtocolException, IOException, ParseException {
    String jwtCaaAdmin = TestUtils.loginCaaAdminUser();
    String jwtOperatorAdmin = TestUtils.loginOperatorAdminUser();
    UUID uidOperatorAdmin = TestUtils.userCreate(jwtOperatorAdmin);
    try {
      SignedJWT jwtsCaaAdmin = TestUtils.parseJwt(jwtCaaAdmin);
      TestUtils.assertJwt(jwtsCaaAdmin);
      UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());
      UUID leid = TestUtils.legalEntityCreate(jwtOperatorAdmin, UUID.randomUUID());
      UUID oid = TestUtils.operatorCreate(jwtOperatorAdmin, leid);

      boolean isOperatorAdminGranted = TestUtils.grantOperatorAdmin(
        authZ,
        oid,
        uidOperatorAdmin
      );
      boolean isOperatorApproved = TestUtils.approveOperator(authZ, oid, idCaaAdmin);

      Logging.info("operator admin granted : " + isOperatorAdminGranted);
      Logging.info("operator approved : " + isOperatorApproved);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // // Scenario 1.a.4 Pilots
  @Test
  public void testScenario_1_a_4()
    throws ClientProtocolException, IOException, ParseException {
    String jwtCaaAdmin = TestUtils.loginCaaAdminUser();
    String jwtPilot = TestUtils.loginPilotUser();
    try {
      SignedJWT jwtsCaaAdmin = TestUtils.parseJwt(jwtCaaAdmin);
      TestUtils.assertJwt(jwtsCaaAdmin);
      UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());
      SignedJWT jwtsPilot = TestUtils.parseJwt(jwtPilot);
      UUID idPilot = UUID.fromString(jwtsPilot.getJWTClaimsSet().getSubject());

      //To-Do Need to get the Civial Aviation Authority
      UUID regulatorUUID = null;

      boolean associatePilotToRegulator = TestUtils.associatePilotToRegulator(
        authZ,
        idPilot,
        regulatorUUID
      );

      boolean isPilotApproved = TestUtils.approvePilot(authZ, idCaaAdmin, idPilot);

      Logging.info("pilot association crated : " + associatePilotToRegulator);
      Logging.info("pilot approved : " + isPilotApproved);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // // Scenario 1.a.5 DSSPs
  @Test
  public void testScenario_1_a_5()
    throws ClientProtocolException, IOException, ParseException {
    String jwtCaaAdmin = TestUtils.loginCaaAdminUser();
    String jwtDsspAdmin = TestUtils.loginDsspAdminUser();
    UUID uidDsspAdmin = TestUtils.userCreate(jwtDsspAdmin); // TODO: skip insertion
    try {
      SignedJWT jwtsCaaAdmin = TestUtils.parseJwt(jwtCaaAdmin);
      TestUtils.assertJwt(jwtsCaaAdmin);
      UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());
      UUID leid = TestUtils.legalEntityCreate(jwtDsspAdmin, UUID.randomUUID());
      UUID dsspid = TestUtils.dsspCreate(jwtDsspAdmin, leid);

      boolean isDSSPAdminGranted = TestUtils.grantOperatorAdmin(
        authZ,
        dsspid,
        uidDsspAdmin
      );
      boolean isDSSPApproved = TestUtils.approveOperator(authZ, dsspid, idCaaAdmin);

      Logging.info("dssp admin granted : " + isDSSPAdminGranted);
      Logging.info("dssp approved : " + isDSSPApproved);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // Scenario 1.a.6 Repair Agencies
  @Test
  public void testScenario_1_a_6()
    throws ClientProtocolException, IOException, ParseException {
    String jwtCaaAdmin = TestUtils.loginCaaAdminUser();
    String jwtRepairAgencyAdmin = TestUtils.loginRepairAgencyAdminUser();
    UUID uidRepairAgencyAdmin = TestUtils.userCreate(jwtRepairAgencyAdmin); // TODO: skip insertion
    try {
      SignedJWT jwtsCaaAdmin = TestUtils.parseJwt(jwtCaaAdmin);
      TestUtils.assertJwt(jwtsCaaAdmin);
      UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());
      UUID leid = TestUtils.legalEntityCreate(jwtRepairAgencyAdmin, UUID.randomUUID());
      UUID repairAgencyid = TestUtils.repairAgencyCreate(jwtRepairAgencyAdmin, leid);

      boolean isRepairAgencyAdminGranted = TestUtils.grantRepairAgencyAdmin(
        authZ,
        repairAgencyid,
        uidRepairAgencyAdmin
      );
      boolean isRepairAgencyApproved = TestUtils.approveOperator(
        authZ,
        repairAgencyid,
        idCaaAdmin
      );

      Logging.info("repair agency admin granted : " + isRepairAgencyAdminGranted);
      Logging.info("repair agency approved : " + isRepairAgencyApproved);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // Scenario 1.a.7 Trader
  @Test
  public void testScenario_1_a_7()
    throws ClientProtocolException, IOException, ParseException {
    String jwtCaaAdmin = TestUtils.loginCaaAdminUser();
    String jwtTraderAdmin = TestUtils.loginTraderAdminUser();
    UUID uidTraderAdmin = TestUtils.userCreate(jwtTraderAdmin); // TODO: skip insertion
    try {
      SignedJWT jwtsCaaAdmin = TestUtils.parseJwt(jwtCaaAdmin);
      TestUtils.assertJwt(jwtsCaaAdmin);
      UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());
      UUID leid = TestUtils.legalEntityCreate(jwtTraderAdmin, UUID.randomUUID());
      UUID traderid = TestUtils.traderCreate(jwtTraderAdmin, leid);

      boolean isTraderAgencyAdminGranted = TestUtils.grantTraderAdmin(
        authZ,
        traderid,
        uidTraderAdmin
      );
      boolean isTraderAgencyApproved = TestUtils.approveOperator(
        authZ,
        traderid,
        idCaaAdmin
      );

      Logging.info("trader admin granted : " + isTraderAgencyAdminGranted);
      Logging.info("trader agency approved : " + isTraderAgencyApproved);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // Scenario 1.a.8 UAS
  @Test
  public void testScenario_1_a_8()
    throws ClientProtocolException, IOException, ParseException {
    String jwtPlatformAdmin = TestUtils.loginPlatformAdminUser();
    UUID uidPlatformAdmin = TestUtils.userCreate(jwtPlatformAdmin); // TODO: skip insertion
    String jwtCaaAdmin = TestUtils.loginCaaAdminUser();
    UUID uidCaaAdmin = TestUtils.userCreate(jwtCaaAdmin); // TODO: skip insertion
    String jwtManufacturerAdmin = TestUtils.loginManufacturerAdminUser();
    UUID uidManufacturerAdmin = TestUtils.userCreate(jwtManufacturerAdmin); // TODO: skip insertion
    try {
      SignedJWT jwtsCaaAdmin = TestUtils.parseJwt(jwtCaaAdmin);
      TestUtils.assertJwt(jwtsCaaAdmin);
      UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());
      UUID leid = TestUtils.legalEntityCreate(jwtManufacturerAdmin, UUID.randomUUID());
      UUID manufacturerid = TestUtils.manufacturerCreate(jwtManufacturerAdmin, leid);
      TestUtils.approveManufacturer(jwtCaaAdmin, manufacturerid);
      UUID uasTypeId = TestUtils.uasTypeCreate(jwtCaaAdmin, manufacturerid);
      TestUtils.approveManufacturer(jwtCaaAdmin, manufacturerid);
      UUID uasId = TestUtils.uasCreate(jwtCaaAdmin, uasTypeId, leid);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // Scenario 2 UAS Sale
  @Test
  public void testScenario_2()
    throws ClientProtocolException, IOException, ParseException {
    String jwtPlatformAdmin = TestUtils.loginPlatformAdminUser();
    UUID uidPlatformAdmin = TestUtils.userCreate(jwtPlatformAdmin); // TODO: skip insertion
    String jwtCaaAdmin = TestUtils.loginCaaAdminUser();
    UUID uidCaaAdmin = TestUtils.userCreate(jwtCaaAdmin); // TODO: skip insertion
    String jwtManufacturerAdmin = TestUtils.loginManufacturerAdminUser();
    UUID uidManufacturerAdmin = TestUtils.userCreate(jwtManufacturerAdmin); // TODO: skip insertion
    String jwtOwner = TestUtils.loginOwnerUser();
    UUID uidOwner = TestUtils.userCreate(jwtOwner); // TODO: skip insertion
    String jwtTraderAdmin = TestUtils.loginTraderAdminUser();
    UUID uidTraderAdmin = TestUtils.userCreate(jwtTraderAdmin); // TODO: skip insertion
    String jwtOperatorAdmin = TestUtils.loginOperatorAdminUser();
    UUID uidOperatorAdmin = TestUtils.userCreate(jwtOperatorAdmin); // TODO: skip insertion
    try {
      SignedJWT jwtsCaaAdmin = TestUtils.parseJwt(jwtCaaAdmin);
      TestUtils.assertJwt(jwtsCaaAdmin);
      UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());
      UUID leid = TestUtils.legalEntityCreate(jwtManufacturerAdmin, UUID.randomUUID());
      UUID manufacturerid = TestUtils.manufacturerCreate(jwtManufacturerAdmin, leid);
      TestUtils.approveManufacturer(jwtCaaAdmin, manufacturerid);
      UUID uasTypeId = TestUtils.uasTypeCreate(jwtCaaAdmin, manufacturerid);
      TestUtils.approveManufacturer(jwtCaaAdmin, manufacturerid);
      UUID uasId = TestUtils.uasCreate(jwtCaaAdmin, uasTypeId, leid);
      UUID traderLeid = TestUtils.legalEntityCreate(jwtTraderAdmin, UUID.randomUUID());
      UUID traderId = TestUtils.manufacturerCreate(jwtTraderAdmin, traderLeid);
      TestUtils.approveTrader(jwtCaaAdmin, traderId);
      UUID operatorLeid = TestUtils.legalEntityCreate(jwtTraderAdmin, UUID.randomUUID());
      UUID operatorId = TestUtils.operatorCreate(jwtTraderAdmin, operatorLeid);
      TestUtils.approveOperator(jwtCaaAdmin, operatorId);
      UUID saleId0 = TestUtils.saleCreate(
        jwtCaaAdmin,
        UUID.randomUUID(),
        uasId,
        true,
        null,
        null,
        manufacturerid,
        traderId
      );
      UUID saleId1 = TestUtils.saleCreate(
        jwtCaaAdmin,
        UUID.randomUUID(),
        uasId,
        false,
        null,
        null,
        traderId,
        operatorId
      );
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }
}
