package in.ispirt.pushpaka.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import in.ispirt.pushpaka.authorisation.RelationshipType;
import in.ispirt.pushpaka.authorisation.ResourceType;
import in.ispirt.pushpaka.authorisation.SubjectType;
import in.ispirt.pushpaka.authorisation.utils.AuthZ;
import in.ispirt.pushpaka.utils.Logging;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class TestUtils {
  private static ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  public static Map<String, Object> extractJsonMap(String s)
    throws JsonProcessingException {
    Map<String, Object> mm = objectMapper.readValue(
      s,
      new TypeReference<Map<String, Object>>() {}
    );
    return mm;
  }

  public static UUID extractUuid(String s) throws JsonProcessingException {
    Map<String, Object> mm = extractJsonMap(s);
    String id = String.valueOf(mm.get("id"));
    return UUID.fromString(id);
  }

  private static String loginUser(java.util.Map.Entry<String, String> user)
    throws ClientProtocolException, IOException, JsonProcessingException {
    // curl -L -X POST
    // 'http://localhost:8080/realms/pushpaka/protocol/openid-connect/token' \
    // -H 'Content-Type: application/x-www-form-urlencoded' \
    // --data-urlencode 'client_id=frontend' \
    // --data-urlencode 'grant_type=password' \
    // --data-urlencode 'client_secret=zEWiaDIDVPLsKVoGHc1uWIeKrv7rzzBe' \
    // --data-urlencode 'scope=openid' \
    // --data-urlencode 'username=test@test.com' \
    // --data-urlencode 'password=test'

    HttpPost request = new HttpPost(
      "http://localhost:8080/realms/pushpaka/protocol/openid-connect/token"
    );
    List<NameValuePair> formparams = Arrays.asList(
      new BasicNameValuePair("client_id", "frontend"),
      new BasicNameValuePair("grant_type", "password"),
      new BasicNameValuePair("client_secret", "Gm236XNRzKTG04hOiXjhRIgZ59krCOFG"),
      new BasicNameValuePair("scope", "openid"),
      new BasicNameValuePair("username", user.getKey()),
      new BasicNameValuePair("password", user.getValue())
    );
    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);
    request.setEntity(entity);
    request.addHeader("Content-Type", "application/x-www-form-urlencoded");

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    // assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    // System.out.println("response: " + reb);
    EntityUtils.consume(re);
    Map<String, Object> mm = extractJsonMap(reb);
    String jwt = String.valueOf(mm.get("access_token"));
    return jwt;
  }

  public static String loginPlatformAdminUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    java.util.Map.Entry<String, String> u = new java.util.AbstractMap.SimpleEntry<>(
      "test.platform.admin@test.com",
      "test"
    );
    return loginUser(u);
  }

  public static String loginCaaAdminUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    java.util.Map.Entry<String, String> u = new java.util.AbstractMap.SimpleEntry<>(
      "test.caa.admin@test.com",
      "test"
    );
    return loginUser(u);
  }

  public static String loginManufacturerAdminUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    java.util.Map.Entry<String, String> u = new java.util.AbstractMap.SimpleEntry<>(
      "test.manufacturer.0.admin@test.com",
      "test"
    );
    return loginUser(u);
  }

  public static String loginRepairAgencyAdminUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    java.util.Map.Entry<String, String> u = new java.util.AbstractMap.SimpleEntry<>(
      "test.repair.agency.0.admin@test.com",
      "test"
    );
    return loginUser(u);
  }

  public static String loginTraderAdminUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    java.util.Map.Entry<String, String> u = new java.util.AbstractMap.SimpleEntry<>(
      "test.trader.0.admin@test.com",
      "test"
    );
    return loginUser(u);
  }

  public static String loginOperatorAdminUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    java.util.Map.Entry<String, String> u = new java.util.AbstractMap.SimpleEntry<>(
      "test.operator.0.admin@test.com",
      "test"
    );
    return loginUser(u);
  }

  public static String loginPilotUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    java.util.Map.Entry<String, String> u = new java.util.AbstractMap.SimpleEntry<>(
      "test.pilot.0@test.com",
      "test"
    );
    return loginUser(u);
  }

  public static String loginDsspAdminUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    java.util.Map.Entry<String, String> u = new java.util.AbstractMap.SimpleEntry<>(
      "test.dssp.0.admin@test.com",
      "test"
    );
    return loginUser(u);
  }

  public static String loginOwnerUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    java.util.Map.Entry<String, String> u = new java.util.AbstractMap.SimpleEntry<>(
      "test.uas.0.owner@test.com",
      "test"
    );
    return loginUser(u);
  }

  public static SignedJWT parseJwt(String jwt) throws ParseException {
    SignedJWT signedJWT = SignedJWT.parse(jwt);
    return signedJWT;
  }

  public static UUID userCreate(String jwt)
    throws ClientProtocolException, IOException, JsonProcessingException, ParseException {
    SignedJWT jwts = TestUtils.parseJwt(jwt);
    UUID uid = UUID.fromString(jwts.getJWTClaimsSet().getSubject());

    StringEntity e = new StringEntity(
      "{\"id\": \"" +
      uid.toString() +
      "\", \"firstName\": \"John\", \"lastName\": \"James\", \"email\": \"john@email.com\", \"phone\": \"+919999999999\", \"aadharId\": \"+919999999999\", \"address\": {\"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"timestamps\": {}, \"status\": \"ACTIVE\" }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/user");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    // assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public static UUID pilotCreate(String jwt)
    throws ClientProtocolException, IOException, JsonProcessingException, ParseException {
    SignedJWT jwts = TestUtils.parseJwt(jwt);
    UUID uid = UUID.fromString(jwts.getJWTClaimsSet().getSubject());
    StringEntity e = new StringEntity(
      "{\"user\": {\"id\": \"" +
      uid.toString() +
      "\", \"firstName\": \"John\", \"lastName\": \"James\", \"email\": \"john@email.com\", \"phone\": \"+919999999999\", \"aadharId\": \"+919999999999\", \"address\": {\"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"timestamps\": {}, \"status\": \"ACTIVE\" }, \"timestamps\": {}, \"validity\": {\"from\": \"2024-01-05T08:06:44.023Z\", \"till\": \"2024-01-05T08:06:44.023Z\" } }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/pilot");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    // assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    Logging.info("pilot create response: " + reb);
    return extractUuid(reb);
  }

  public static UUID legalEntityCreate(String jwt, UUID id)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"id\": \"" +
      id.toString() +
      "\", \"cin\": \"CIN" +
      id.toString().substring(0, 8) +
      "\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN" +
      id.toString().substring(0, 8) +
      "\", \"timestamps\": {} }",
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

  public static UUID manufacturerCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"legalEntity\": { \"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN" +
      x.toString().substring(0, 8) +
      "\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN" +
      x.toString().substring(0, 8) +
      "\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:08:22.985Z\", \"till\": \"2023-11-26T12:08:22.985Z\" }, \"timestamps\": {} }",
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

  public static UUID civilAviationAuthorityCreate(String jwt, UUID x, UUID leid)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"id\": \"" +
      x.toString() +
      "\", \"legalEntity\": { \"id\": \"" +
      leid.toString() +
      "\", \"cin\": \"CIN" +
      x.toString().substring(0, 8) +
      "\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN" +
      x.toString().substring(0, 8) +
      "\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:08:22.985Z\", \"till\": \"2023-11-26T12:08:22.985Z\" }, \"timestamps\": {} }",
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

  public static String listCivilAviationAuthorities(AuthZ authz) {
    List<String> regulatorList = new ArrayList<String>(authz.lookupRegulator());

    return regulatorList.get(0);
  }

  public static UUID operatorCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{\"legalEntity\": {\"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN" +
      x.toString().substring(0, 8) +
      "\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": {\"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN" +
      x.toString().substring(0, 8) +
      "\", \"timestamps\": {} }, \"validity\": {\"from\": \"2023-11-27T07:48:03.686Z\", \"till\": \"2023-11-27T07:48:03.686Z\" }, \"timestamps\": {} }",
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

  public static UUID dsspCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"legalEntity\": { \"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN" +
      x.toString().substring(0, 8) +
      "\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN" +
      x.toString().substring(0, 8) +
      "\", \"timestamps\": {} }, \"validity\": { \"from\": \"2024-01-05T08:42:05.989Z\", \"till\": \"2024-01-05T08:42:05.989Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost(
      "http://localhost:8084/api/v1/digitalSkyServiceProvider"
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

  public static UUID uasTypeCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"modelNumber\": \"string\", \"manufacturer\": { \"id\": \"" +
      x.toString() +
      "\", \"legalEntity\": { \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN" +
      x.toString().substring(0, 8) +
      "\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:12:08.481Z\", \"till\": \"2023-11-26T12:12:08.481Z\" }, \"timestamps\": {} }, \"propulsionCategory\": \"VTOL\", \"weightCategory\": \"NANO\", \"mtow\": 0, \"photoUrl\": \"https://ispirt.github.io/pushpaka/\", \"supportedOperationCategories\": [ \"C1\" ], \"timestamps\": {} }",
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

  public static UUID uasTypeC2Create(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"modelNumber\": \"string\", \"manufacturer\": { \"id\": \"" +
      x.toString() +
      "\", \"legalEntity\": { \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN" +
      x.toString().substring(0, 8) +
      "\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:12:08.481Z\", \"till\": \"2023-11-26T12:12:08.481Z\" }, \"timestamps\": {} }, \"propulsionCategory\": \"VTOL\", \"weightCategory\": \"NANO\", \"mtow\": 0, \"photoUrl\": \"https://ispirt.github.io/pushpaka/\", \"supportedOperationCategories\": [ \"C1\", \"C2\" ], \"timestamps\": {} }",
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

  public static UUID uasTypeC3Create(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"modelNumber\": \"string\", \"manufacturer\": { \"id\": \"" +
      x.toString() +
      "\", \"legalEntity\": { \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN" +
      x.toString().substring(0, 8) +
      "\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:12:08.481Z\", \"till\": \"2023-11-26T12:12:08.481Z\" }, \"timestamps\": {} }, \"propulsionCategory\": \"VTOL\", \"weightCategory\": \"NANO\", \"mtow\": 0, \"photoUrl\": \"https://ispirt.github.io/pushpaka/\", \"supportedOperationCategories\": [ \"C1\", \"C2\", \"C3\" ], \"timestamps\": {} }",
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

  public static UUID uasCreate(String jwt, UUID uasTypeId, UUID leid)
    throws ClientProtocolException, IOException, JsonProcessingException, ParseException {
    StringEntity e = new StringEntity(
      "{ \"type\": { \"id\": \"" +
      uasTypeId.toString() +
      "\", \"modelNumber\": \"string\", \"manufacturer\": { \"legalEntity\": { \"id\": \"" +
      leid.toString() +
      "\", \"cin\": \"CIN" +
      leid.toString().substring(0, 8) +
      "\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN" +
      leid.toString().substring(0, 8) +
      "\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:14:38.563Z\", \"till\": \"2023-11-26T12:14:38.563Z\" }, \"timestamps\": {} }, \"propulsionCategory\": \"VTOL\", \"weightCategory\": \"NANO\", \"mtow\": 0, \"photoUrl\": \"https://ispirt.github.io/pushpaka/\", \"supportedOperationCategories\": [ \"C1\" ], \"timestamps\": {} }, \"oemSerialNumber\": \"string\", \"timestamps\": {}, \"status\": \"REGISTERED\" }",
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

  public static UUID repairAgencyCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"legalEntity\": { \"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN" +
      x.toString().substring(0, 8) +
      "\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN" +
      x.toString().substring(0, 8) +
      "\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:08:22.985Z\", \"till\": \"2023-11-26T12:08:22.985Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/repairAgency");
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

  public static UUID flightPlanCreate(
    String jwt,
    UUID id,
    UUID uasId,
    UUID uasTypeId,
    UUID mId,
    UUID leId,
    UUID pilotId
  )
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"id\": \"" +
      id.toString() +
      "\", \"uas\": { \"id\": \"" +
      uasId.toString() +
      "\", \"type\": { \"id\": \"" +
      uasTypeId.toString() +
      "\", \"modelNumber\": \"string\", \"manufacturer\": { \"id\": \"" +
      mId.toString() +
      "\", \"legalEntity\": { \"id\": \"" +
      leId.toString() +
      "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2024-01-10T07:32:43.564Z\", \"till\": \"2024-01-10T07:32:43.564Z\" }, \"timestamps\": {} }, \"propulsionCategory\": \"VTOL\", \"weightCategory\": \"NANO\", \"mtow\": 0, \"photoUrl\": \"https://ispirt.github.io/pushpaka/\", \"supportedOperationCategories\": [ \"C1\" ], \"timestamps\": {} }, \"oemSerialNumber\": \"string\", \"timestamps\": {}, \"status\": \"REGISTERED\" }, \"pilot\": { \"id\": \"" +
      pilotId.toString() +
      "\", \"user\": { \"firstName\": \"John\", \"lastName\": \"James\", \"email\": \"john@email.com\", \"phone\": \"+919999999999\", \"aadharId\": \"+919999999999\", \"address\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"timestamps\": {}, \"status\": \"ACTIVE\" }, \"timestamps\": {}, \"validity\": { \"from\": \"2024-01-10T07:32:43.564Z\", \"till\": \"2024-01-10T07:32:43.564Z\" } }, \"operation_category\": \"C1\", \"start_time\": \"2024-01-10T07:32:43.564Z\", \"end_time\": \"2024-01-10T07:32:43.564Z\" }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/flightPlan");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    Logging.info("aut create: " + reb);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    EntityUtils.consume(re);
    return TestUtils.extractUuid(reb);
  }

  public static UUID flightAuthorisationCreate(
    String jwt,
    UUID id,
    UUID fp,
    UUID uas,
    UUID pilot
  )
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"issuerID\": \"string\", \"start_time\": \"2024-01-11T07:23:23.397Z\", \"end_time\": \"2024-01-11T07:23:23.397Z\", \"state\": \"CREATED\", \"attenuations\": { \"geocage\": { \"radius\": 0 }, \"waypoints\": { \"latitude\": 0, \"longitude\": 0 }, \"emergencyTermination\": true }, \"operation_category\": \"C1\", \"id\": \"" +
      id.toString() +
      "\", \"uas\": { \"id\": \"" +
      uas.toString() +
      "\", \"type\": { \"modelNumber\": \"string\", \"manufacturer\": { \"legalEntity\": { \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2024-01-11T07:23:23.397Z\", \"till\": \"2024-01-11T07:23:23.397Z\" }, \"timestamps\": {} }, \"propulsionCategory\": \"VTOL\", \"weightCategory\": \"NANO\", \"mtow\": 0, \"photoUrl\": \"https://ispirt.github.io/pushpaka/\", \"supportedOperationCategories\": [ \"C1\" ], \"timestamps\": {} }, \"oemSerialNumber\": \"string\", \"timestamps\": {}, \"status\": \"REGISTERED\" }, \"pilot\": { \"id\": \"" +
      pilot.toString() +
      "\", \"user\": { \"id\": \"\", \"firstName\": \"John\", \"lastName\": \"James\", \"email\": \"john@email.com\", \"phone\": \"+919999999999\", \"aadharId\": \"+919999999999\", \"address\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"timestamps\": {}, \"status\": \"ACTIVE\" }, \"timestamps\": {}, \"validity\": { \"from\": \"2024-01-11T07:23:23.397Z\", \"till\": \"2024-01-11T07:23:23.397Z\" } } }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/airspaceUsageToken");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    Logging.info("aut create: " + reb);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    return TestUtils.extractUuid(reb);
  }

  public static UUID traderCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"legalEntity\": { \"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN" +
      x.toString().substring(0, 8) +
      "\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN" +
      x.toString().substring(0, 8) +
      "\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:08:22.985Z\", \"till\": \"2023-11-26T12:08:22.985Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/trader");
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

  public static UUID saleCreate(
    String jwt,
    UUID id,
    UUID uasId,
    Boolean holding,
    UUID su,
    UUID bu,
    UUID sle,
    UUID ble
  )
    throws ClientProtocolException, IOException, JsonProcessingException {
    String sus = "";
    String bus = "";
    String sles = "";
    String bles = "";
    if (su != null) {
      sus =
        sus +
        "\"seller_user\": { \"id\": \"" +
        su.toString() +
        "\", \"firstName\": \"John\", \"lastName\": \"James\", \"email\": \"john@email.com\", \"phone\": \"+919999999999\", \"aadharId\": \"+919999999999\", \"address\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"timestamps\": {}, \"status\": \"ACTIVE\" }, ";
    }
    if (bu != null) {
      bus =
        bus +
        "\"buyer_user\": { \"id\": \"" +
        bu.toString() +
        "\", \"firstName\": \"John\", \"lastName\": \"James\", \"email\": \"john@email.com\", \"phone\": \"+919999999999\", \"aadharId\": \"+919999999999\", \"address\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"timestamps\": {}, \"status\": \"ACTIVE\" }, ";
    }
    if (sle != null) {
      sles =
        sles +
        "\"seller_legal_entity\": { \"id\": \"" +
        sle.toString() +
        "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, ";
    }
    if (ble != null) {
      bles =
        bles +
        "\"buyer_legal_entity\": { \"id\": \"" +
        ble.toString() +
        "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, ";
    }
    StringEntity e = new StringEntity(
      "{ \"timestamps\": {\"updated\": \"2023-11-27T07:48:03.686Z\", \"created\": \"2023-11-27T07:48:03.686Z\" }, \"validity\": { \"from\": \"2024-01-11T11:01:56.896Z\", \"till\": \"2024-01-11T11:01:56.896Z\" }, " +
      sus +
      sles +
      bus +
      bles +
      "\"uas\": { \"id\": \"" +
      uasId.toString() +
      "\", \"type\": { \"modelNumber\": \"string\", \"manufacturer\": { \"legalEntity\": { \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2024-01-11T11:01:56.896Z\", \"till\": \"2024-01-11T11:01:56.896Z\" }, \"timestamps\": {} }, \"propulsionCategory\": \"VTOL\", \"weightCategory\": \"NANO\", \"mtow\": 0, \"photoUrl\": \"https://ispirt.github.io/pushpaka/\", \"supportedOperationCategories\": [ \"C1\" ], \"timestamps\": {} }, \"oemSerialNumber\": \"string\", \"timestamps\": {}, \"status\": \"REGISTERED\" }, \"holding\": true }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8084/api/v1/sale");
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    Logging.info("sale create response: " + reb);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    return TestUtils.extractUuid(reb);
  }

  public static boolean grantPlatformAdmin(AuthZ authZ, UUID platformAdminUserUUID) {
    boolean isSuccess = false;
    isSuccess = authZ.createPlatformAdmin(platformAdminUserUUID.toString());

    return isSuccess;
  }

  public static boolean associateCAAToPlatform(AuthZ authZ, UUID CAAResourceUUID) {
    boolean isSuccess = false;
    isSuccess = authZ.associateCAAToPlatform(CAAResourceUUID.toString());

    return isSuccess;
  }

  public static boolean grantCAAAdmin(
    AuthZ authZ,
    UUID CAAResourceUUID,
    UUID platformAdminUserUUID,
    UUID CAAAdminUserUUID
  ) {
    boolean isSuccess = false;
    isSuccess =
      authZ.createCAAAdmin(
        CAAResourceUUID.toString(),
        CAAAdminUserUUID.toString(),
        platformAdminUserUUID.toString()
      );
    return isSuccess;
  }

  public static boolean grantManufacturerAdmin(
    AuthZ authZ,
    UUID manufacturerUUID,
    UUID manufacturerAdminUUID,
    UUID caaResourceUUID
  ) {
    boolean isSuccess = false;

    isSuccess =
      authZ.createResoureTypeAdmin(
        ResourceType.MANUFACTURER,
        manufacturerUUID.toString(),
        manufacturerAdminUUID.toString(),
        caaResourceUUID.toString()
      );

    return isSuccess;
  }

  public static boolean grantOperatorAdmin(
    AuthZ authZ,
    UUID operatorUUID,
    UUID opertorAdminUUID,
    UUID caaResourceUUID
  ) {
    boolean isSuccess = false;

    isSuccess =
      authZ.createResoureTypeAdmin(
        ResourceType.OPERATOR,
        operatorUUID.toString(),
        opertorAdminUUID.toString(),
        caaResourceUUID.toString()
      );

    return isSuccess;
  }

  public static boolean grantDSSPAdmin(
    AuthZ authZ,
    UUID dsspUUID,
    UUID dsspAdminUUID,
    UUID caaResourceUUID
  ) {
    boolean isSuccess = false;

    isSuccess =
      authZ.createResoureTypeAdmin(
        ResourceType.DSSP,
        dsspUUID.toString(),
        dsspAdminUUID.toString(),
        caaResourceUUID.toString()
      );

    return isSuccess;
  }

  public static boolean grantRepairAgencyAdmin(
    AuthZ authZ,
    UUID repairAgencyUUID,
    UUID repairAgencyAdminUUID,
    UUID caaResourceUUID
  ) {
    boolean isSuccess = false;

    isSuccess =
      authZ.createResoureTypeAdmin(
        ResourceType.REPAIRAGENCY,
        repairAgencyUUID.toString(),
        repairAgencyAdminUUID.toString(),
        caaResourceUUID.toString()
      );

    return isSuccess;
  }

  public static boolean grantTraderAdmin(
    AuthZ authZ,
    UUID traderUUID,
    UUID traderdminUUID,
    UUID caaResourceUUID
  ) {
    boolean isSuccess = false;

    isSuccess =
      authZ.createResoureTypeAdmin(
        ResourceType.TRADER,
        traderUUID.toString(),
        traderdminUUID.toString(),
        caaResourceUUID.toString()
      );

    return isSuccess;
  }

  public static boolean associatePilotToRegulator(
    AuthZ authZ,
    UUID pilotResourceUUID,
    UUID pilotUUID,
    UUID regulatorUUID
  ) {
    boolean isSuccess = false;

    isSuccess =
      authZ.addPilot(
        pilotResourceUUID.toString(),
        pilotUUID.toString(),
        regulatorUUID.toString()
      );

    return isSuccess;
  }

  public static boolean associateUASTypeToManufacturer(
    AuthZ authZ,
    UUID uasTypeUUID,
    UUID manufacturerUUID,
    UUID manufacturerAdminUserUUID,
    UUID caaResourceUUID
  ) {
    boolean isSuccess = authZ.createUASTypeRelationships(
      uasTypeUUID.toString(),
      manufacturerUUID.toString(),
      manufacturerAdminUserUUID.toString(),
      caaResourceUUID.toString()
    );

    return isSuccess;
  }

  public static boolean associateUASToManufacturer(
    AuthZ authZ,
    UUID uasUUID,
    UUID manufacturerUUID,
    UUID manufacturerAdminUserUUID,
    UUID caaResourceUUID
  ) {
    boolean isSuccess = authZ.createUASManufacturerRelationships(
      uasUUID.toString(),
      manufacturerUUID.toString(),
      manufacturerAdminUserUUID.toString(),
      caaResourceUUID.toString()
    );

    return isSuccess;
  }

  public static boolean approveManufacturer(
    AuthZ authZ,
    UUID manufacturerUUID,
    UUID CAAAdminUserID
  ) {
    boolean isSuccess = false;

    isSuccess =
      authZ.approveResourceByRegulator(
        ResourceType.MANUFACTURER,
        manufacturerUUID.toString(),
        CAAAdminUserID.toString()
      );

    return isSuccess;
  }

  public static boolean approveUASType(
    AuthZ authZ,
    UUID uasTypeUUID,
    UUID CAAAdminUserID
  ) {
    boolean isSuccess = false;

    isSuccess =
      authZ.approveResourceByRegulator(
        ResourceType.UASTYPE,
        uasTypeUUID.toString(),
        CAAAdminUserID.toString()
      );

    return isSuccess;
  }

  public static boolean approveOperator(
    AuthZ authZ,
    UUID operatorUUID,
    UUID CAAAdminUserID
  ) {
    boolean isSuccess = false;

    isSuccess =
      authZ.approveResourceByRegulator(
        ResourceType.OPERATOR,
        operatorUUID.toString(),
        CAAAdminUserID.toString()
      );

    return isSuccess;
  }

  public static boolean approveDssp(AuthZ authZ, UUID dsspUUID, UUID CAAAdminUserID) {
    boolean isSuccess = false;

    isSuccess =
      authZ.approveResourceByRegulator(
        ResourceType.DSSP,
        dsspUUID.toString(),
        CAAAdminUserID.toString()
      );

    return isSuccess;
  }

  public static boolean approveTrader(AuthZ authZ, UUID traderUUID, UUID CAAAdminUserID) {
    boolean isSuccess = false;

    isSuccess =
      authZ.approveResourceByRegulator(
        ResourceType.TRADER,
        traderUUID.toString(),
        CAAAdminUserID.toString()
      );

    return isSuccess;
  }

  public static boolean approveRepairAgency(
    AuthZ authZ,
    UUID repairAgencyUUID,
    UUID CAAAdminUserID
  ) {
    boolean isSuccess = false;

    isSuccess =
      authZ.approveResourceByRegulator(
        ResourceType.REPAIRAGENCY,
        repairAgencyUUID.toString(),
        CAAAdminUserID.toString()
      );

    return isSuccess;
  }

  public static boolean approvePilot(
    AuthZ authZ,
    UUID pilotResourceUUID,
    UUID CAAAdminUserID
  ) {
    boolean isSuccess = false;

    isSuccess =
      authZ.approveResourceByRegulator(
        ResourceType.PILOT,
        pilotResourceUUID.toString(),
        CAAAdminUserID.toString()
      );

    return isSuccess;
  }

  public static void approveManufacturer(String jwt, UUID id) {
    // assertEquals(1, 2);
  }

  public static void approveOperator(String jwt, UUID id) {
    // assertEquals(1, 2);
  }

  public static void approvePilot(String jwt, UUID id) {
    // assertEquals(1, 2);
  }

  public static void approveDssp(String jwt, UUID id) {
    // assertEquals(1, 2);
  }

  public static void approveTrader(String jwt, UUID id) {
    // assertEquals(1, 2);
  }

  public static void approveRepairAgency(String jwt, UUID id) {
    // assertEquals(1, 2);
  }

  public static void approveUasType(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  public static void approveTransferCreate(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  public static void approveSale(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  public static void assertJwt(SignedJWT t) throws ParseException {
    assertEquals(
      "http://localhost:8080/realms/pushpaka",
      t.getJWTClaimsSet().getIssuer()
    );
    assertTrue(new Date().before(t.getJWTClaimsSet().getExpirationTime()));
  }
}
