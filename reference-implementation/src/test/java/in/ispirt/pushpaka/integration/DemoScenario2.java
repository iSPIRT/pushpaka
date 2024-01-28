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
 * Scenario 2: Scenario 2: AUT issuance for VLoS
 *   b. Show that Post registration, VLoS drone has long-term AUT assigned that
 *   has expiry date 1 year from date of registration and status = “Created”.
 */
class DemoScenario2 {

  @BeforeEach
  void cleanup() {}

  // Scenario 2.b.1
  @Test
  public void testGenerateAutForBvlos()
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
    Logging.info("id Pilot : " + uidPilot.toString());
    UUID uidPilot2 = TestUtils.pilotCreate(jwtPilot);
    Logging.info("id Pilot : " + uidPilot2.toString());
    assertNotNull(uidPilot);
    try {
      SignedJWT jwtsCaaAdmin = TestUtils.parseJwt(jwtCaaAdmin);
      TestUtils.assertJwt(jwtsCaaAdmin);
      UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());
      Logging.info("id Caa Admin: " + idCaaAdmin.toString());
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
      UUID uasTypeId = TestUtils.uasTypeCreate(jwtCaaAdmin, mid);
      UUID uasId = TestUtils.uasCreate(jwtCaaAdmin, uasTypeId, leid, 12);
      // Flight Authorisation
      UUID flightPlanId = TestUtils.flightPlanCreate(
        jwtPilot,
        UUID.randomUUID(),
        uasId,
        uasTypeId,
        mid,
        leid,
        uidPilot2
      );
      UUID flightAuthorisationId = TestUtils.flightAuthorisationCreate(
        jwtPilot,
        UUID.randomUUID(),
        flightPlanId,
        uasId,
        uidPilot2
      );
      assertNotNull(flightAuthorisationId);
    } catch (ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }
}
