package in.ispirt.pushpaka.integration;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import in.ispirt.pushpaka.utils.Logging;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Scenario 1: End to end Registration service workflow
 *   a. Explain that there are different personas (or user types) in Digital Sky and
 * their inter-relationships
 *      1. Civil Aviation Authority
 *      2. Manufacturers
 *      3. Operators
 *      4. Pilots
 *      5. DSSPs
 *      6. Repair Agencies
 *      7. Trader
 *      8. UAS
 *   b. Demonstrate the VLoS drone registration process at point of sale where
 * manufacturerID, TraderID, OperatorID, DroneID come together and register a drone .
 */
public class DemoScenario1 {

  @BeforeEach
  void cleanup() {
    System.out.println("cleanup entities started");
    // in.ispirt.pushpaka.registry.dao.Dao.deleteAll(in.ispirt.pushpaka.registry.dao.DaoInstance.getInstance().getSession());
    System.out.println("cleanup entities completed");
  }

  // Scenario 1.a.1 Civil Aviation Authority
  @Test
  public void testScenario_1_a_1()
    throws ClientProtocolException, IOException, java.text.ParseException {
    String jwtPlatformAdmin = TestUtils.loginPlatformAdminUser();
    UUID uidPlatformAdmin = TestUtils.userCreate(jwtPlatformAdmin); // TODO: skip insertion
    String jwtCaaAdmin = TestUtils.loginCaaAdminUser();
    try {
      SignedJWT jwtsCaaAdmin = TestUtils.parseJwt(jwtCaaAdmin);
      TestUtils.assertJwt(jwtsCaaAdmin);
      UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());
      Logging.info("id Caa Admin: " + idCaaAdmin.toString());
      // grant caa admin rights to caa admin user
      TestUtils.grantCaaAdmin(jwtPlatformAdmin, idCaaAdmin); // TODO: spicedb call

      // create legal entity
      UUID leid = TestUtils.legalEntityCreate(jwtCaaAdmin);
      // create civilAviationAuthority
      UUID mid = TestUtils.civilAviationAuthorityCreate(jwtCaaAdmin, leid);
    } catch (java.text.ParseException e) {
      Logging.severe("JWT ParseException");
    }
  }
  // // Scenario 1.a.2 Manufacturers
  // @Ignore
  // @Test
  // public void testScenario_1_a_2() throws ClientProtocolException, IOException {
  //   String jwt = loginUser();
  //   // create legal entity
  //   UUID leid = legalEntityCreate(jwt);
  //   // create manufacturer
  //   UUID mid = manufacturerCreate(jwt, leid);
  // }

  // // Scenario 1.a.3 Operators
  // @Ignore
  // @Test
  // public void testScenario_1_a_3() throws ClientProtocolException, IOException {
  //   String jwt = loginUser();
  //   // create legal entity
  //   UUID leid = legalEntityCreate(jwt);
  //   // create manufacturer
  //   UUID oid = operatorCreate(jwt, leid);
  // }

  // // Scenario 1.a.4 Pilots
  // @Ignore
  // @Test
  // public void testScenario_1_a_4() throws ClientProtocolException, IOException {
  //   String jwt = loginUser();
  //   UUID pid = pilotCreate(jwt);
  // }

  // // Scenario 1.a.5 DSSPs
  // @Ignore
  // @Test
  // public void testScenario_1_a_5() throws ClientProtocolException, IOException {
  //   String jwt = loginUser();
  //   // create legal entity
  //   UUID leid = legalEntityCreate(jwt);
  //   // create dssp
  //   UUID dsspid = dsspCreate(jwt, leid);
  // }

  // // Scenario 1.a.6 Repair Agencies
  // @Ignore
  // @Test
  // public void testScenario_1_a_6() throws ClientProtocolException, IOException {
  //   String jwt = loginUser();
  //   // create legal entity
  //   UUID leid = legalEntityCreate(jwt);
  //   // create repair agency
  //   UUID mid = repairAgencyCreate(jwt, leid);
  // }

  // // Scenario 1.a.7 Trader
  // @Ignore
  // @Test
  // public void testScenario_1_a_7() throws ClientProtocolException, IOException {
  //   String jwt = loginUser();
  //   // create legal entity
  //   UUID leid = legalEntityCreate(jwt);
  //   // create repair agency
  //   UUID mid = traderCreate(jwt, leid);
  // }

  // // Scenario 1.a.8 UAS
  // @Ignore
  // @Test
  // public void testScenario_1_a_8() throws ClientProtocolException, IOException {
  //   String jwt = loginUser();
  //   // create legal entity
  //   UUID leid = legalEntityCreate(jwt);
  //   // create manufacturer
  //   UUID mid = manufacturerCreate(jwt, leid);
  //   // create uas type
  //   UUID utid = uasTypeCreate(jwt, mid);
  //   // create uas
  //   UUID uid = uasCreate(jwt, utid);
  // }
}
