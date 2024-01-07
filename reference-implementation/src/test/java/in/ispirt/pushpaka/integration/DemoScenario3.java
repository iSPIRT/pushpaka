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
 * Scenario 3: VLoS auto geocage demonstration
 * a. Even if pilot gives command to exceed the permitted height, it cannot go beyond the 30 m height based on operation type (VLoS in this case)
 * b. it will also send a warning message to both GCS and pilot (as SMS) that the drone cannot exceed the height restriction
 */
class DemoScenario3 {

  @BeforeEach
  void cleanup() {}

  // Scenario 3.a.1
  @Test
  public void testScenario_3_a_1()
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
      UUID uasTypeId = TestUtils.uasTypeCreate(jwtCaaAdmin, mid);
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
