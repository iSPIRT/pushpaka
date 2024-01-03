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

  public static SignedJWT parseJwt(String jwt) throws java.text.ParseException {
    SignedJWT signedJWT = SignedJWT.parse(jwt);
    return signedJWT;
  }

  public static UUID userCreate(String jwt)
    throws ClientProtocolException, IOException, JsonProcessingException, java.text.ParseException {
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

  public static UUID pilotCreate(String jwt) {
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
}
