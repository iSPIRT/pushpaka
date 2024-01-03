package in.ispirt.pushpaka.integration;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import java.io.IOException;
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
 * Scenario 2: Scenario 2: AUT issuance for VLoS
 *   b. Show that Post registration, VLoS drone has long-term AUT assigned that
 *   has expiry date 1 year from date of registration and status = “Created”.
 */
class DemoScenario2 {

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

  public UUID autCreate(String jwt, UUID leid)
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

  public UUID transferCreate(String jwt, UUID id) {
    assertEquals(1, 2);
    return UUID.randomUUID();
  }

  public void saleApprove(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  // Scenario 2.b.1
  @Test
  public void testScenario_2_b_1()
    throws ClientProtocolException, IOException, java.text.ParseException {
    // login platform admin user
    String jwtPlatformAdmin = TestUtils.loginPlatformAdminUser();

    // login caa-admin user
    String jwtCaaAdmin = TestUtils.loginCaaAdminUser();
    SignedJWT jwtsCaaAdmin = TestUtils.parseJwt(jwtCaaAdmin);
    UUID idCaaAdmin = UUID.fromString(jwtsCaaAdmin.getJWTClaimsSet().getSubject());

    // grant caa admin rights to caa admin user
    grantCaaAdmin(jwtPlatformAdmin, idCaaAdmin);

    // create legal entity
    UUID leid = legalEntityCreate(jwtCaaAdmin);

    // create civilAviationAuthority
    UUID caaid = civilAviationAuthorityCreate(jwtCaaAdmin, leid);

    // login manufacturer admin user
    String jwtMfgAdmin = TestUtils.loginManufacturerAdminUser();

    // create legal entity
    UUID lemid = legalEntityCreate(jwtMfgAdmin);

    // create manufacturer
    UUID mid = manufacturerCreate(jwtMfgAdmin, lemid);

    // approve manufacturer profild
    manufacturerApprove(jwtCaaAdmin, mid);

    // login trader admin user
    String jwtSellerAdmin = TestUtils.loginTraderAdminUser();

    UUID lesid = legalEntityCreate(jwtSellerAdmin);

    // create trader
    UUID sid = traderCreate(jwtSellerAdmin, lesid);

    // approve trader profild
    traderApprove(jwtCaaAdmin, sid);

    // create uas type
    UUID utid = uasTypeCreate(jwtMfgAdmin, mid);

    // approve uas type profild
    uasTypeApprove(jwtCaaAdmin, utid);

    // create uas
    UUID uid = uasCreate(jwtMfgAdmin, utid);

    // create AUT
    UUID x1 = autCreate(jwtMfgAdmin, uid);
    // or
    UUID transferId = transferCreate(jwtSellerAdmin, uid);
    UUID x2 = autCreate(jwtSellerAdmin, uid);
  }
}
