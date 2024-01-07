package in.ispirt.pushpaka.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nimbusds.jwt.SignedJWT;
import in.ispirt.pushpaka.utils.Logging;
import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;
import org.apache.http.client.ClientProtocolException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Scenario 5: BVLoS simulation
 * a. Demonstrate that a manufacturerID submitting a flight plan gets rejected (as this is not permitted and only operatorID with valid PilotID can do so)
 * b. For BVLoS drone: first flight plan submitted will be rejected by Flight planning service because there is a conflict with an existing flight plan (strategic deconfliction) → pilot submits second flight plan → DSSP approves it → Flight auth service helps generate AUT and pushes it to the GCS of drone (show that AUT state = Created)
 * c. As part of arming process, BVLoS drone performs pre-flight check and verifies that the certificate is valid and signed by verifiable authority (show that AUT state = In-use)
 * d. mid-flight a kill switch command is issued by Disaster response team to vacate all business drones so that relief drones can come in to help. Demonstrate that AUT state now transitions to Terminated state and the drone proceeds to land immediately at nearest safe location
 * e. Post issuance of kill switch, a TFR comes into effect for 2 hours. Within this window, drone pilots submit new flight plan and get new AUT issued. Show that new AUT is assigned to this drone with status = Created
 * f. Drone begins flight → AUT state changes to “Active” → drone goes airborne → lands at preset destination according to flight plan (show that AUT state = Terminated)
 **/
class DemoScenario5 {

  @BeforeEach
  void cleanup() {}

  // Scenario 5.a.1
  @Test
  public void testScenario_5_a_1()
    throws ClientProtocolException, IOException, java.text.ParseException {
    String jwtPlatformAdmin = TestUtils.loginPlatformAdminUser();
    UUID uidPlatformAdmin = TestUtils.userCreate(jwtPlatformAdmin); // TODO: skip insertion
    assertNotNull(uidPlatformAdmin);
    String jwtCaaAdmin = TestUtils.loginCaaAdminUser();
    UUID uidCaaAdmin = TestUtils.userCreate(jwtCaaAdmin); // TODO: skip insertion
    assertNotNull(uidCaaAdmin);
    String jwtManufacturerAdmin = TestUtils.loginManufacturerAdminUser();
    UUID uidManufacturerAdmin = TestUtils.userCreate(jwtManufacturerAdmin); // TODO: skip insertion
    assertNotNull(uidManufacturerAdmin);
    String jwtPilot = TestUtils.loginPilotUser();
    UUID uidPilot = TestUtils.userCreate(jwtPilot); // TODO: skip insertion
    assertNotNull(uidPilot);
    try {
      SignedJWT jwtsCaaAdmin = TestUtils.parseJwt(jwtCaaAdmin);
      TestUtils.assertJwt(jwtsCaaAdmin);
      UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());
      Logging.info("id Caa Admin: " + idCaaAdmin.toString());
      TestUtils.grantCaaAdmin(jwtPlatformAdmin, idCaaAdmin); // TODO: spicedb call
      UUID leidCaa = TestUtils.legalEntityCreate(jwtCaaAdmin, UUID.randomUUID());
      UUID caaid = TestUtils.civilAviationAuthorityCreate(
        jwtCaaAdmin,
        UUID.randomUUID(),
        leidCaa
      );
      assertNotNull(caaid);
      UUID leid = TestUtils.legalEntityCreate(jwtManufacturerAdmin, UUID.randomUUID());
      UUID mid = TestUtils.manufacturerCreate(jwtManufacturerAdmin, leid);
      TestUtils.approveManufacturer(jwtCaaAdmin, mid);
      SignedJWT jwtsPilot = TestUtils.parseJwt(jwtPilot);
      UUID idPilot = UUID.fromString(jwtsPilot.getJWTClaimsSet().getSubject());
      TestUtils.approvePilot(jwtCaaAdmin, idPilot);
      UUID uasTypeId = TestUtils.uasTypeC3Create(jwtCaaAdmin, mid);
      UUID uasId = TestUtils.uasCreate(jwtCaaAdmin, uasTypeId, leid);
      // Flight Authorisation
      UUID flightPlanId = TestUtils.flightPlanCreate(jwtPilot, uasTypeId);
      UUID flightAuthorisationId = TestUtils.flightAuthorisationCreate(
        jwtPilot,
        UUID.randomUUID(),
        flightPlanId,
        uasId,
        idPilot
      );
      assertNotNull(flightAuthorisationId);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }
}
