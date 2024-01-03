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
class DemoScenario1 {

  @BeforeEach
  void cleanup() {
    System.out.println("cleanup entities started");
    // in.ispirt.pushpaka.registry.dao.Dao.deleteAll(in.ispirt.pushpaka.registry.dao.DaoInstance.getInstance().getSession());
    System.out.println("cleanup entities completed");
  }

  public UUID legalEntityCreate(String jwt)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/legalEntity");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return TestUtils.extractUuid(reb);
  }

  public UUID manufacturerCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"legalEntity\": { \"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:08:22.985Z\", \"till\": \"2023-11-26T12:08:22.985Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/manufacturer");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return TestUtils.extractUuid(reb);
  }

  public void manufacturerCreate(String jwt)
    throws ClientProtocolException, IOException, JsonProcessingException {
    UUID x = UUID.randomUUID();
    StringEntity e = new StringEntity(
      "{ \"legalEntity\": { \"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:08:22.985Z\", \"till\": \"2023-11-26T12:08:22.985Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/manufacturer");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 400);
    return;
  }

  public UUID civilAviationAuthorityCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"legalEntity\": { \"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:08:22.985Z\", \"till\": \"2023-11-26T12:08:22.985Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost(
      "http://localhost:8084/api/v1/civilAviationAuthority"
    );
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return TestUtils.extractUuid(reb);
  }

  public void civilAviationAuthorityCreate(String jwt)
    throws ClientProtocolException, IOException, JsonProcessingException {
    UUID x = UUID.randomUUID();
    StringEntity e = new StringEntity(
      "{ \"legalEntity\": { \"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:08:22.985Z\", \"till\": \"2023-11-26T12:08:22.985Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost(
      "http://localhost:8084/api/v1/civilAviationAuthority"
    );
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 400);
    return;
  }

  public UUID operatorCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{\"legalEntity\": {\"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": {\"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": {\"from\": \"2023-11-27T07:48:03.686Z\", \"till\": \"2023-11-27T07:48:03.686Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/operator");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return TestUtils.extractUuid(reb);
  }

  public void operatorCreate(String jwt)
    throws ClientProtocolException, IOException, JsonProcessingException {
    UUID x = UUID.randomUUID();
    StringEntity e = new StringEntity(
      "{\"legalEntity\": {\"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": {\"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": {\"from\": \"2023-11-27T07:48:03.686Z\", \"till\": \"2023-11-27T07:48:03.686Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/operator");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 400);
    return;
  }

  public UUID dsspCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{\"legalEntity\": {\"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": {\"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": {\"from\": \"2023-11-27T07:48:03.686Z\", \"till\": \"2023-11-27T07:48:03.686Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/dssp");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return TestUtils.extractUuid(reb);
  }

  public void dsspCreate(String jwt)
    throws ClientProtocolException, IOException, JsonProcessingException {
    UUID x = UUID.randomUUID();
    StringEntity e = new StringEntity(
      "{\"legalEntity\": {\"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": {\"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": {\"from\": \"2023-11-27T07:48:03.686Z\", \"till\": \"2023-11-27T07:48:03.686Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/dssp");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 400);
    return;
  }

  public UUID uasTypeCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"modelNumber\": \"string\", \"manufacturer\": { \"id\": \"" +
      x.toString() +
      "\", \"legalEntity\": { \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:12:08.481Z\", \"till\": \"2023-11-26T12:12:08.481Z\" }, \"timestamps\": {} }, \"propulsionCategory\": \"VTOL\", \"weightCategory\": \"NANO\", \"mtow\": 0, \"photoUrl\": \"https://ispirt.github.io/pushpaka/\", \"supportedOperationCategories\": [ \"C1\" ], \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/uasType");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return TestUtils.extractUuid(reb);
  }

  public void uasTypeCreate(String jwt)
    throws ClientProtocolException, IOException, JsonProcessingException {
    UUID x = UUID.randomUUID();
    StringEntity e = new StringEntity(
      "{ \"modelNumber\": \"string\", \"manufacturer\": { \"id\": \"" +
      x.toString() +
      "\", \"legalEntity\": { \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:12:08.481Z\", \"till\": \"2023-11-26T12:12:08.481Z\" }, \"timestamps\": {} }, \"propulsionCategory\": \"VTOL\", \"weightCategory\": \"NANO\", \"mtow\": 0, \"photoUrl\": \"https://ispirt.github.io/pushpaka/\", \"supportedOperationCategories\": [ \"C1\" ], \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/uasType");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 400);
    return;
  }

  public UUID uasCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"type\": { \"id\": \"" +
      x.toString() +
      "\", \"modelNumber\": \"string\", \"manufacturer\": { \"legalEntity\": { \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:14:38.563Z\", \"till\": \"2023-11-26T12:14:38.563Z\" }, \"timestamps\": {} }, \"propulsionCategory\": \"VTOL\", \"weightCategory\": \"NANO\", \"mtow\": 0, \"photoUrl\": \"https://ispirt.github.io/pushpaka/\", \"supportedOperationCategories\": [ \"C1\" ], \"timestamps\": {} }, \"oemSerialNumber\": \"string\", \"timestamps\": {}, \"status\": \"REGISTERED\" }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/uas");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return TestUtils.extractUuid(reb);
  }

  public void uasCreate(String jwt)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"type\": { \"id\": \"" +
      UUID.randomUUID().toString() +
      "\", \"modelNumber\": \"string\", \"manufacturer\": { \"legalEntity\": { \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:14:38.563Z\", \"till\": \"2023-11-26T12:14:38.563Z\" }, \"timestamps\": {} }, \"propulsionCategory\": \"VTOL\", \"weightCategory\": \"NANO\", \"mtow\": 0, \"photoUrl\": \"https://ispirt.github.io/pushpaka/\", \"supportedOperationCategories\": [ \"C1\" ], \"timestamps\": {} }, \"oemSerialNumber\": \"string\", \"timestamps\": {}, \"status\": \"REGISTERED\" }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/uas");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 400);
    return;
  }

  public UUID pilotCreate(String jwt)
    throws ClientProtocolException, IOException, JsonProcessingException {
    assertEquals(1, 2);
    return UUID.randomUUID();
  }

  public UUID repairAgencyCreate(String jwt, UUID leid)
    throws ClientProtocolException, IOException, JsonProcessingException {
    assertEquals(1, 2);
    return UUID.randomUUID();
  }

  public UUID traderCreate(String jwt, UUID leid)
    throws ClientProtocolException, IOException, JsonProcessingException {
    assertEquals(1, 2);
    return UUID.randomUUID();
  }

  public void grantCaaAdmin(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  public void manufacturerApprove(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  public void traderApprove(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  public void uasTypeApprove(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  public void transferCreate(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  public void saleApprove(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  public void assertJwt(SignedJWT t) throws java.text.ParseException {
    assertEquals(
      "http://localhost:8080/realms/pushpaka",
      t.getJWTClaimsSet().getIssuer()
    );
    assertTrue(new Date().before(t.getJWTClaimsSet().getExpirationTime()));
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
      assertJwt(jwtsCaaAdmin);
      UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());
      Logging.info("id Caa Admin: " + idCaaAdmin.toString());
      // grant caa admin rights to caa admin user
      grantCaaAdmin(jwtPlatformAdmin, idCaaAdmin); // TODO: spicedb call

      // create legal entity
      UUID leid = legalEntityCreate(jwtCaaAdmin);
      // create civilAviationAuthority
      UUID mid = civilAviationAuthorityCreate(jwtCaaAdmin, leid);
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
