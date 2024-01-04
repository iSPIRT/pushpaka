package in.ispirt.pushpaka.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Date;
import java.text.ParseException;

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

  // curl -L -X POST 'http://localhost:8080/realms/pushpaka/protocol/openid-connect/token' \
  // -H 'Content-Type: application/x-www-form-urlencoded' \
  // --data-urlencode 'client_id=frontend' \
  // --data-urlencode 'grant_type=password' \
  // --data-urlencode 'client_secret=zEWiaDIDVPLsKVoGHc1uWIeKrv7rzzBe' \
  // --data-urlencode 'scope=openid' \
  // --data-urlencode 'username=test@test.com' \
  // --data-urlencode 'password=test'

  private static String loginUser(java.util.Map.Entry<String, String> user)
    throws ClientProtocolException, IOException, JsonProcessingException {
    HttpPost request = new HttpPost(
      "http://localhost:8080/realms/pushpaka/protocol/openid-connect/token"
    );
    List<NameValuePair> formparams = Arrays.asList(
      new BasicNameValuePair("client_id", "frontend"),
      new BasicNameValuePair("grant_type", "password"),
      new BasicNameValuePair("client_secret", "FH598McXo0GugVaKqAZYuiM6RDm99QY3"),
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

  public static String loginTraderAdminUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    java.util.Map.Entry<String, String> u = new java.util.AbstractMap.SimpleEntry<>(
      "test.trader.0.admin@test.com",
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

  public static UUID pilotCreate(String jwt) throws ClientProtocolException, IOException, JsonProcessingException, ParseException {
    SignedJWT jwts = TestUtils.parseJwt(jwt);
    UUID uid = UUID.fromString(jwts.getJWTClaimsSet().getSubject());
    StringEntity e = new StringEntity(
      "{\"user\": {\"id\": \"" +
      uid.toString() +
      "\", \"firstName\": \"John\", \"lastName\": \"James\", \"email\": \"john@email.com\", \"phone\": \"+919999999999\", \"aadharId\": \"+919999999999\", \"address\": {\"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"timestamps\": {}, \"status\": \"ACTIVE\" }, \"timestamps\": {}, \"validity\": {\"from\": \"2024-01-03T13:41:00.461Z\", \"till\": \"2024-01-03T13:41:00.461Z\" } }"
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

  public static UUID legalEntityCreate(String jwt)
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

  public static UUID manufacturerCreate(String jwt, UUID x)
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

  public static void manufacturerCreate(String jwt)
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

  public static UUID civilAviationAuthorityCreate(String jwt, UUID x)
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

  public static void civilAviationAuthorityCreate(String jwt)
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

  public static UUID operatorCreate(String jwt, UUID x)
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

  public static void operatorCreate(String jwt)
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

  public static UUID dsspCreate(String jwt, UUID x)
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

  public static void dsspCreate(String jwt)
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

  public static UUID uasTypeCreate(String jwt, UUID x)
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

  public static void uasTypeCreate(String jwt)
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

  public static UUID uasCreate(String jwt, UUID x)
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

  public static void uasCreate(String jwt)
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

  public static UUID repairAgencyCreate(String jwt, UUID leid)
    throws ClientProtocolException, IOException, JsonProcessingException {
    assertEquals(1, 2);
    return UUID.randomUUID();
  }

  public static UUID traderCreate(String jwt, UUID leid)
    throws ClientProtocolException, IOException, JsonProcessingException {
    assertEquals(1, 2);
    return UUID.randomUUID();
  }

  public static void grantCaaAdmin(String jwt, UUID id) {
    
    assertEquals(1, 2);
  }

  public static void manufacturerApprove(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  public static void traderApprove(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  public static void uasTypeApprove(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  public static void transferCreate(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  public static void saleApprove(String jwt, UUID id) {
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
