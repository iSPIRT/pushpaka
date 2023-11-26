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
    System.out.println("UUID" + id);
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
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
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
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  @Test
  public void testUasCreate() throws ClientProtocolException, IOException {
    // create legal entity
    UUID leid = legalEntityCreate();
    // create manufacturer
    UUID mid = manufacturerCreate(leid);
    // create uas type
    UUID utid = uasTypeCreate(mid);
    // create uas
    UUID uid = uasCreate(utid);
  }
}
