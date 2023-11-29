package in.ispirt.pushpaka.registry;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.junit.jupiter.api.Test;

class EntityTests {
  ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  private UUID extractUuid(String s) throws JsonProcessingException {
    Map<String, Object> mm = objectMapper.readValue(
      s,
      new TypeReference<Map<String, Object>>() {}
    );
    String id = String.valueOf(mm.get("id"));
    return UUID.fromString(id);
  }

  public UUID legalEntityCreate()
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/legalEntity");
    request.setEntity(e);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public UUID manufacturerCreate(UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"legalEntity\": { \"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:08:22.985Z\", \"till\": \"2023-11-26T12:08:22.985Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/manufacturer");
    request.setEntity(e);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public void manufacturerCreate()
    throws ClientProtocolException, IOException, JsonProcessingException {
    UUID x = UUID.randomUUID();
    StringEntity e = new StringEntity(
      "{ \"legalEntity\": { \"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:08:22.985Z\", \"till\": \"2023-11-26T12:08:22.985Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/manufacturer");
    request.setEntity(e);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 400);
    return;
  }

  public UUID operatorCreate(UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{\"legalEntity\": {\"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": {\"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": {\"from\": \"2023-11-27T07:48:03.686Z\", \"till\": \"2023-11-27T07:48:03.686Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/operator");
    request.setEntity(e);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public void operatorCreate()
    throws ClientProtocolException, IOException, JsonProcessingException {
    UUID x = UUID.randomUUID();
    StringEntity e = new StringEntity(
      "{\"legalEntity\": {\"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": {\"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": {\"from\": \"2023-11-27T07:48:03.686Z\", \"till\": \"2023-11-27T07:48:03.686Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/operator");
    request.setEntity(e);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 400);
    return;
  }

  public UUID utmspCreate(UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{\"legalEntity\": {\"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": {\"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": {\"from\": \"2023-11-27T07:48:03.686Z\", \"till\": \"2023-11-27T07:48:03.686Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/utmsp");
    request.setEntity(e);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public void utmspCreate()
    throws ClientProtocolException, IOException, JsonProcessingException {
    UUID x = UUID.randomUUID();
    StringEntity e = new StringEntity(
      "{\"legalEntity\": {\"id\": \"" +
      x.toString() +
      "\", \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": {\"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": {\"from\": \"2023-11-27T07:48:03.686Z\", \"till\": \"2023-11-27T07:48:03.686Z\" }, \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/utmsp");
    request.setEntity(e);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 400);
    return;
  }

  public UUID uasTypeCreate(UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"modelNumber\": \"string\", \"manufacturer\": { \"id\": \"" +
      x.toString() +
      "\", \"legalEntity\": { \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:12:08.481Z\", \"till\": \"2023-11-26T12:12:08.481Z\" }, \"timestamps\": {} }, \"propulsionCategory\": \"VTOL\", \"weightCategory\": \"NANO\", \"mtow\": 0, \"photoUrl\": \"https://ispirt.github.io/pushpaka/\", \"supportedOperationCategories\": [ \"C1\" ], \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/uasType");
    request.setEntity(e);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public void uasTypeCreate()
    throws ClientProtocolException, IOException, JsonProcessingException {
    UUID x = UUID.randomUUID();
    StringEntity e = new StringEntity(
      "{ \"modelNumber\": \"string\", \"manufacturer\": { \"id\": \"" +
      x.toString() +
      "\", \"legalEntity\": { \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:12:08.481Z\", \"till\": \"2023-11-26T12:12:08.481Z\" }, \"timestamps\": {} }, \"propulsionCategory\": \"VTOL\", \"weightCategory\": \"NANO\", \"mtow\": 0, \"photoUrl\": \"https://ispirt.github.io/pushpaka/\", \"supportedOperationCategories\": [ \"C1\" ], \"timestamps\": {} }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/uasType");
    request.setEntity(e);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 400);
    return;
  }

  public UUID userCreate(UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"id\": \"" +
      x.toString() +
      "\", \"aadharId\": \"string\", \"phone\": \"string\", \"address\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"timestamps\": {}  }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/user");
    request.setEntity(e);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public UUID uasCreate(UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"type\": { \"id\": \"" +
      x.toString() +
      "\", \"modelNumber\": \"string\", \"manufacturer\": { \"legalEntity\": { \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:14:38.563Z\", \"till\": \"2023-11-26T12:14:38.563Z\" }, \"timestamps\": {} }, \"propulsionCategory\": \"VTOL\", \"weightCategory\": \"NANO\", \"mtow\": 0, \"photoUrl\": \"https://ispirt.github.io/pushpaka/\", \"supportedOperationCategories\": [ \"C1\" ], \"timestamps\": {} }, \"oemSerialNumber\": \"string\", \"timestamps\": {}, \"status\": \"REGISTERED\" }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/uas");
    request.setEntity(e);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public void uasCreate()
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"type\": { \"id\": \"" +
      UUID.randomUUID().toString() +
      "\", \"modelNumber\": \"string\", \"manufacturer\": { \"legalEntity\": { \"cin\": \"CIN00000\", \"name\": \"Test Company Pvt Ltd\", \"regdAddress\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"gstin\": \"GSTIN00000\", \"timestamps\": {} }, \"validity\": { \"from\": \"2023-11-26T12:14:38.563Z\", \"till\": \"2023-11-26T12:14:38.563Z\" }, \"timestamps\": {} }, \"propulsionCategory\": \"VTOL\", \"weightCategory\": \"NANO\", \"mtow\": 0, \"photoUrl\": \"https://ispirt.github.io/pushpaka/\", \"supportedOperationCategories\": [ \"C1\" ], \"timestamps\": {} }, \"oemSerialNumber\": \"string\", \"timestamps\": {}, \"status\": \"REGISTERED\" }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/uas");
    request.setEntity(e);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 400);
    return;
  }

  public UUID pilotCreate(UUID uid)
    throws ClientProtocolException, IOException, JsonProcessingException {
    StringEntity e = new StringEntity(
      "{ \"user\": { \"id\": \"" +
      uid.toString() +
      "\", \"firstName\": \"John\", \"lastName\": \"James\", \"email\": \"john@email.com\", \"phone\": \"+919999999999\", \"aadharId\": \"+919999999999\", \"address\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"timestamps\": {}, \"status\": \"ACTIVE\" }, \"timestamps\": {}, \"validity\": { \"from\": \"2023-11-29T06:20:07.699Z\", \"till\": \"2023-11-29T06:20:07.699Z\" } }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/pilot");
    request.setEntity(e);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public void pilotCreate()
    throws ClientProtocolException, IOException, JsonProcessingException {
    UUID uid = UUID.randomUUID();
    StringEntity e = new StringEntity(
      "{ \"user\": { \"id\": \"" +
      uid.toString() +
      "\", \"firstName\": \"John\", \"lastName\": \"James\", \"email\": \"john@email.com\", \"phone\": \"+919999999999\", \"aadharId\": \"+919999999999\", \"address\": { \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"line1\": \"123 ABC Housing Society\", \"line2\": \"Landmark\", \"line3\": \"Bandra West\", \"city\": \"Mumbai\", \"state\": \"ANDHRA_PRADESH\", \"pinCode\": \"400000\", \"country\": \"IND\" }, \"timestamps\": {}, \"status\": \"ACTIVE\" }, \"timestamps\": {}, \"validity\": { \"from\": \"2023-11-29T06:20:07.699Z\", \"till\": \"2023-11-29T06:20:07.699Z\" } }",
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost("http://localhost:8083/api/v1/pilot");
    request.setEntity(e);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 400);
    return;
  }

  @Test
  public void testLegalEntityCreate() throws ClientProtocolException, IOException {
    // create legal entity
    UUID leid = legalEntityCreate();
  }

  @Test
  public void testManufacturerCreate() throws ClientProtocolException, IOException {
    // create legal entity
    UUID leid = legalEntityCreate();
    // create manufacturer
    UUID mid = manufacturerCreate(leid);
    // create manufacturer without legal entity
    manufacturerCreate();
  }

  @Test
  public void testOperatorCreate() throws ClientProtocolException, IOException {
    // create legal entity
    UUID leid = legalEntityCreate();
    // create manufacturer
    UUID mid = operatorCreate(leid);
    // create manufacturer without legal entity
    operatorCreate();
  }

  @Test
  public void testUtmspCreate() throws ClientProtocolException, IOException {
    // create legal entity
    UUID leid = legalEntityCreate();
    // create manufacturer
    UUID mid = utmspCreate(leid);
    // create manufacturer without legal entity
    utmspCreate();
  }

  @Test
  public void testUasTypeCreate() throws ClientProtocolException, IOException {
    // create legal entity
    UUID leid = legalEntityCreate();
    // create manufacturer
    UUID mid = manufacturerCreate(leid);
    // create manufacturer without legal entity
    manufacturerCreate();
    // create uas type
    UUID utid = uasTypeCreate(mid);
    // create uas type without manufacturer
    uasTypeCreate();
  }

  @Test
  public void testUasCreate() throws ClientProtocolException, IOException {
    // create legal entity
    UUID leid = legalEntityCreate();
    // create manufacturer
    UUID mid = manufacturerCreate(leid);
    // create manufacturer without legal entity
    manufacturerCreate();
    // create uas type
    UUID utid = uasTypeCreate(mid);
    // create uas type without manufacturer
    uasTypeCreate();
    // create uas
    UUID uid = uasCreate(utid);
    // create uas without uas type
    uasCreate();
  }

  @Test
  public void testUserCreate() throws ClientProtocolException, IOException {
    UUID uid = UUID.randomUUID();
    userCreate(uid);
  }

  @Test
  public void testPilotCreate() throws ClientProtocolException, IOException {
    UUID uid = UUID.randomUUID();
    userCreate(uid);

    UUID pid = pilotCreate(uid);

    pilotCreate();
  }
}
