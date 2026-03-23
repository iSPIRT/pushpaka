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
 * Scenario 4: Category change from VLoS to EVLoS
 * a. Show that the drone from scenario 3 is registered as VLoS. Now re-register it
 * as EVLoS and demonstrate that the geocage height restriction has changed
 * and it can now fly to higher altitude
 * b. here height restriction remains same and diameter changes and we can show
 * that it is now able to move further out within larger EVLoS geocage
 */
class DemoScenario4 {

  @BeforeEach
  void cleanup() {}

  // Scenario 4.a.1
  @Test
  public void testGenerateAutForEvlos()
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
      UUID uasTypeId = TestUtils.uasTypeBCreate(jwtCaaAdmin, mid);
      UUID uasId = TestUtils.uasCreate(jwtCaaAdmin, uasTypeId, leid, 15);
      // Flight Authorisation
      UUID flightPlanId = TestUtils.flightPlanCreate(
        jwtPilot,
        UUID.randomUUID(),
        uasId,
        uasTypeId,
        mid,
        leid,
        idPilot
      );
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
