package in.ispirt.pushpaka.integration;

import static org.junit.Assert.assertTrue;

import com.nimbusds.jwt.SignedJWT;
import in.ispirt.pushpaka.authorisation.utils.AuthZ;
import in.ispirt.pushpaka.utils.Logging;
import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
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

  @BeforeAll
  public static void setup() {
    authZ = new AuthZ();
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
  public void testRegisterCivilAviationAuthority()
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

      boolean caaAdminGrantCreated = TestUtils.grantCAAAdmin(
        authZ,
        mid,
        uidPlatformAdmin,
        idCaaAdmin
      );

      Logging.info("CAA admin grant created : " + caaAdminGrantCreated);

      assertTrue(platformGrantCreated && caaAdminGrantCreated);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // Scenario 1.a.2 Manufacturers
  @Test
  public void testRegisterManufacturer()
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

      UUID civilAviationAuthorityUUID = UUID.fromString(
        TestUtils.listCivilAviationAuthorities(authZ)
      );

      boolean isManufacturerAdminGranted = TestUtils.grantManufacturerAdmin(
        authZ,
        mid,
        uidManufacturerAdmin,
        civilAviationAuthorityUUID
      );
      boolean isManufacturerApproved = TestUtils.approveManufacturer(
        authZ,
        mid,
        idCaaAdmin
      );

      Logging.info("manufacturer admin granted : " + isManufacturerAdminGranted);
      Logging.info("manufacturer approved : " + isManufacturerApproved);

      assertTrue(isManufacturerAdminGranted && isManufacturerApproved);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // Scenario 1.a.3 Operators
  @Test
  public void testRegisterOperator()
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

      UUID civilAviationAuthorityUUID = UUID.fromString(
        TestUtils.listCivilAviationAuthorities(authZ)
      );

      boolean isOperatorAdminGranted = TestUtils.grantOperatorAdmin(
        authZ,
        oid,
        uidOperatorAdmin,
        civilAviationAuthorityUUID
      );
      boolean isOperatorApproved = TestUtils.approveOperator(authZ, oid, idCaaAdmin);

      Logging.info("operator admin granted : " + isOperatorAdminGranted);
      Logging.info("operator approved : " + isOperatorApproved);

      assertTrue(isOperatorAdminGranted && isOperatorApproved);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // // Scenario 1.a.4 Pilots
  @Test
  public void testRegisterPilot()
    throws ClientProtocolException, IOException, ParseException {
    String jwtCaaAdmin = TestUtils.loginCaaAdminUser();
    String jwtPilot = TestUtils.loginPilotUser();
    try {
      SignedJWT jwtsCaaAdmin = TestUtils.parseJwt(jwtCaaAdmin);
      TestUtils.assertJwt(jwtsCaaAdmin);
      UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());
      SignedJWT jwtsPilot = TestUtils.parseJwt(jwtPilot);
      UUID idPilot = UUID.fromString(jwtsPilot.getJWTClaimsSet().getSubject());

      //To-Do - need legal entity definition
      UUID legalPilotID = UUID.fromString("aca69ef3-0a6e-4e1a-ae71-986289bdaaa0");

      UUID civilAviationAuthorityUUID = UUID.fromString(
        TestUtils.listCivilAviationAuthorities(authZ)
      );

      boolean associatePilotToRegulator = TestUtils.associatePilotToRegulator(
        authZ,
        legalPilotID,
        idPilot,
        civilAviationAuthorityUUID
      );

      boolean isPilotApproved = TestUtils.approvePilot(authZ, legalPilotID, idCaaAdmin);

      Logging.info("pilot association crated : " + associatePilotToRegulator);
      Logging.info("pilot approved : " + isPilotApproved);

      assertTrue(associatePilotToRegulator && isPilotApproved);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // // Scenario 1.a.5 DSSPs
  @Test
  public void testRegisterDigitalSkyServiceProvider()
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

      UUID civilAviationAuthorityUUID = UUID.fromString(
        TestUtils.listCivilAviationAuthorities(authZ)
      );

      boolean isDSSPAdminGranted = TestUtils.grantDSSPAdmin(
        authZ,
        dsspid,
        uidDsspAdmin,
        civilAviationAuthorityUUID
      );
      boolean isDSSPApproved = TestUtils.approveDssp(authZ, dsspid, idCaaAdmin);

      Logging.info("dssp admin granted : " + isDSSPAdminGranted);
      Logging.info("dssp approved : " + isDSSPApproved);

      assertTrue(isDSSPAdminGranted && isDSSPApproved);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // Scenario 1.a.6 Repair Agencies
  @Test
  public void testRegisterRepairAgency()
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

      UUID civilAviationAuthorityUUID = UUID.fromString(
        TestUtils.listCivilAviationAuthorities(authZ)
      );

      boolean isRepairAgencyAdminGranted = TestUtils.grantRepairAgencyAdmin(
        authZ,
        repairAgencyid,
        uidRepairAgencyAdmin,
        civilAviationAuthorityUUID
      );
      boolean isRepairAgencyApproved = TestUtils.approveRepairAgency(
        authZ,
        repairAgencyid,
        idCaaAdmin
      );

      Logging.info("repair agency admin granted : " + isRepairAgencyAdminGranted);
      Logging.info("repair agency approved : " + isRepairAgencyApproved);

      assertTrue(isRepairAgencyAdminGranted && isRepairAgencyApproved);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // Scenario 1.a.7 Trader
  @Test
  public void testRegisterTrader()
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

      UUID civilAviationAuthorityUUID = UUID.fromString(
        TestUtils.listCivilAviationAuthorities(authZ)
      );

      boolean isTraderAdminGranted = TestUtils.grantTraderAdmin(
        authZ,
        traderid,
        uidTraderAdmin,
        civilAviationAuthorityUUID
      );

      boolean isTraderApproved = TestUtils.approveTrader(authZ, traderid, idCaaAdmin);

      Logging.info("trader admin granted : " + isTraderAdminGranted);
      Logging.info("trader agency approved : " + isTraderApproved);

      assertTrue(isTraderAdminGranted && isTraderApproved);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // Scenario 1.a.8 UAS
  @Test
  public void testRegisterUas()
    throws ClientProtocolException, IOException, ParseException {
    String jwtCaaAdmin = TestUtils.loginCaaAdminUser();
    String jwtManufacturerAdmin = TestUtils.loginManufacturerAdminUser();
    UUID uidManufacturerAdmin = TestUtils.userCreate(jwtManufacturerAdmin); // TODO: skip insertion
    try {
      SignedJWT jwtsCaaAdmin = TestUtils.parseJwt(jwtCaaAdmin);
      TestUtils.assertJwt(jwtsCaaAdmin);
      UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());
      UUID leid = TestUtils.legalEntityCreate(jwtManufacturerAdmin, UUID.randomUUID());
      UUID manufacturerid = TestUtils.manufacturerCreate(jwtManufacturerAdmin, leid);

      UUID civilAviationAuthorityUUID = UUID.fromString(
        TestUtils.listCivilAviationAuthorities(authZ)
      );

      boolean isManufacturerAdminGranted = TestUtils.grantManufacturerAdmin(
        authZ,
        manufacturerid,
        uidManufacturerAdmin,
        civilAviationAuthorityUUID
      );
      boolean isManufacturerApproved = TestUtils.approveManufacturer(
        authZ,
        manufacturerid,
        idCaaAdmin
      );

      Logging.info("manufacturer admin granted : " + isManufacturerAdminGranted);
      Logging.info("manufacturer approved : " + isManufacturerApproved);

      UUID uasTypeId = TestUtils.uasTypeCreate(jwtCaaAdmin, manufacturerid);

      boolean isUASTypeAssociationSuccess = TestUtils.associateUASTypeToManufacturer(
        authZ,
        uasTypeId,
        manufacturerid,
        uidManufacturerAdmin,
        civilAviationAuthorityUUID
      );

      boolean isUASTypeApproved = TestUtils.approveUASType(authZ, uasTypeId, idCaaAdmin);

      Logging.info("UAS Type approved : " + isUASTypeApproved);

      TestUtils.approveUasType(jwtCaaAdmin, uasTypeId);

      UUID uasId = TestUtils.uasCreate(
        jwtCaaAdmin,
        uasTypeId,
        leid,
        ThreadLocalRandom.current().nextInt(0, 65535)
      );

      boolean isUASAssociationSuccess = TestUtils.associateUASToManufacturer(
        authZ,
        uasId,
        manufacturerid,
        uidManufacturerAdmin,
        civilAviationAuthorityUUID
      );

      Logging.info("uas association created : " + isUASAssociationSuccess);

      assertTrue(
        isManufacturerAdminGranted &&
        isManufacturerApproved &&
        isUASTypeAssociationSuccess &&
        isUASTypeApproved &&
        isUASAssociationSuccess
      );
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }

  // Scenario 2 UAS Sale
  @Test
  public void testRegisterSale()
    throws ClientProtocolException, IOException, ParseException {
    String jwtCaaAdmin = TestUtils.loginCaaAdminUser();
    String jwtManufacturerAdmin = TestUtils.loginManufacturerAdminUser();
    UUID uidManufacturerAdmin = TestUtils.userCreate(jwtManufacturerAdmin); // TODO: skip insertion
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
      UUID civilAviationAuthorityUUID = UUID.fromString(
        TestUtils.listCivilAviationAuthorities(authZ)
      );

      boolean isManufacturerAdminGranted = TestUtils.grantManufacturerAdmin(
        authZ,
        manufacturerid,
        uidManufacturerAdmin,
        civilAviationAuthorityUUID
      );
      boolean isManufacturerApproved = TestUtils.approveManufacturer(
        authZ,
        manufacturerid,
        idCaaAdmin
      );

      Logging.info("manufacturer admin granted : " + isManufacturerAdminGranted);
      Logging.info("manufacturer approved : " + isManufacturerApproved);

      UUID uasTypeId = TestUtils.uasTypeCreate(jwtCaaAdmin, manufacturerid);
      TestUtils.approveManufacturer(jwtCaaAdmin, manufacturerid);
      boolean isUASTypeAssociationSuccess = TestUtils.associateUASTypeToManufacturer(
        authZ,
        uasTypeId,
        manufacturerid,
        uidManufacturerAdmin,
        civilAviationAuthorityUUID
      );

      Logging.info("UAS Type associated : " + isUASTypeAssociationSuccess);

      boolean isUASTypeApproved = TestUtils.approveUASType(authZ, uasTypeId, idCaaAdmin);

      Logging.info("UAS Type approved : " + isUASTypeApproved);

      TestUtils.approveUasType(jwtCaaAdmin, uasTypeId);

      UUID uasId = TestUtils.uasCreate(
        jwtCaaAdmin,
        uasTypeId,
        leid,
        ThreadLocalRandom.current().nextInt(0, 65535)
      );

      boolean isUASAssociationSuccess = TestUtils.associateUASToManufacturer(
        authZ,
        uasId,
        manufacturerid,
        uidManufacturerAdmin,
        civilAviationAuthorityUUID
      );

      Logging.info("uas association created : " + isUASAssociationSuccess);

      UUID traderLeid = TestUtils.legalEntityCreate(jwtTraderAdmin, UUID.randomUUID());
      UUID traderId = TestUtils.manufacturerCreate(jwtTraderAdmin, traderLeid);
      TestUtils.approveTrader(jwtCaaAdmin, traderId);

      boolean isTraderAdminGranted = TestUtils.grantTraderAdmin(
        authZ,
        traderId,
        uidTraderAdmin,
        civilAviationAuthorityUUID
      );

      boolean isTraderApproved = TestUtils.approveTrader(authZ, traderId, idCaaAdmin);

      Logging.info("trader admin granted : " + isTraderAdminGranted);
      Logging.info("trader agency approved : " + isTraderApproved);

      UUID operatorLeid = TestUtils.legalEntityCreate(jwtTraderAdmin, UUID.randomUUID());
      UUID operatorId = TestUtils.operatorCreate(jwtTraderAdmin, operatorLeid);

      boolean isOperatorAdminGranted = TestUtils.grantOperatorAdmin(
        authZ,
        operatorId,
        uidOperatorAdmin,
        civilAviationAuthorityUUID
      );
      boolean isOperatorApproved = TestUtils.approveOperator(
        authZ,
        operatorId,
        idCaaAdmin
      );

      Logging.info("operator admin granted : " + isOperatorAdminGranted);
      Logging.info("operator approved : " + isOperatorApproved);

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

      assertTrue(
        isManufacturerAdminGranted &&
        isManufacturerApproved &&
        isUASTypeAssociationSuccess &&
        isUASAssociationSuccess &&
        isUASTypeApproved &&
        isTraderAdminGranted &&
        isTraderApproved &&
        isOperatorAdminGranted &&
        isOperatorApproved &&
        saleId0.toString().length() > 0 &&
        saleId1.toString().length() > 0
      );
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }
}
