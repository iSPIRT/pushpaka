package in.ispirt.pushpaka.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  public static String loginUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    HttpPost request = new HttpPost(
      "http://localhost:8080/realms/pushpaka/protocol/openid-connect/token"
    );
    List<NameValuePair> formparams = Arrays.asList(
      new BasicNameValuePair("client_id", "frontend"),
      new BasicNameValuePair("grant_type", "password"),
      new BasicNameValuePair("client_secret", "FH598McXo0GugVaKqAZYuiM6RDm99QY3"),
      new BasicNameValuePair("scope", "openid"),
      new BasicNameValuePair("username", "test@test.com"),
      new BasicNameValuePair("password", "test")
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
}
