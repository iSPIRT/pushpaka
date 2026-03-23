package in.ispirt.pushpaka.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nimbusds.jwt.SignedJWT;
import in.ispirt.pushpaka.authorisation.ResourceType;
import in.ispirt.pushpaka.authorisation.utils.AuthZ;
import in.ispirt.pushpaka.utils.Logging;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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

  // ── Base URLs (override via env vars in CI or devcontainer) ───────────────
  static final String REGISTRY_BASE_URL = System
    .getenv()
    .getOrDefault("REGISTRY_BASE_URL", "http://localhost:8082");
  static final String FLIGHT_AUTH_BASE_URL = System
    .getenv()
    .getOrDefault("FLIGHT_AUTH_BASE_URL", "http://localhost:8083");
  static final String KEYCLOAK_URL = System
    .getenv()
    .getOrDefault("KEYCLOAK_URL", "http://localhost:18080");

  private static final ObjectMapper objectMapper = new ObjectMapper()
    .findAndRegisterModules();

  // ── Fixture helpers ───────────────────────────────────────────────────────

  /**
   * Loads a JSON fixture file from the test classpath under /fixtures/.
   */
  static String loadFixture(String name) throws IOException {
    try (
      InputStream is = TestUtils.class.getResourceAsStream("/fixtures/" + name)
    ) {
      if (is == null) throw new IOException("Fixture not found: /fixtures/" + name);
      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  /**
   * Replaces {{key}} placeholders in a template string.
   * Pass key-value pairs as alternating arguments: fill(template, "id", uuid, "cin", "CIN001")
   */
  static String fill(String template, String... keyValues) {
    String result = template;
    for (int i = 0; i + 1 < keyValues.length; i += 2) {
      result = result.replace("{{" + keyValues[i] + "}}", keyValues[i + 1]);
    }
    return result;
  }

  // ── JSON / JWT helpers ────────────────────────────────────────────────────

  public static Map<String, Object> extractJsonMap(String s)
    throws JsonProcessingException {
    return objectMapper.readValue(s, new TypeReference<Map<String, Object>>() {});
  }

  public static UUID extractUuid(String s) throws JsonProcessingException {
    return UUID.fromString(String.valueOf(extractJsonMap(s).get("id")));
  }

  public static SignedJWT parseJwt(String jwt) throws ParseException {
    return SignedJWT.parse(jwt);
  }

  public static void assertJwt(SignedJWT t) throws ParseException {
    assertEquals(
      KEYCLOAK_URL + "/realms/pushpaka",
      t.getJWTClaimsSet().getIssuer()
    );
    assertTrue(new Date().before(t.getJWTClaimsSet().getExpirationTime()));
  }

  // ── Login helpers ─────────────────────────────────────────────────────────

  private static String loginUser(java.util.Map.Entry<String, String> user)
    throws ClientProtocolException, IOException, JsonProcessingException {
    HttpPost request = new HttpPost(
      KEYCLOAK_URL + "/realms/pushpaka/protocol/openid-connect/token"
    );
    List<NameValuePair> formparams = Arrays.asList(
      new BasicNameValuePair("client_id", "backend"),
      new BasicNameValuePair("grant_type", "password"),
      new BasicNameValuePair("client_secret", "qV6lTdv59FyBL1kn2bRnp6LQF4HVxOkk"),
      new BasicNameValuePair("scope", "openid"),
      new BasicNameValuePair("username", user.getKey()),
      new BasicNameValuePair("password", user.getValue())
    );
    request.setEntity(new UrlEncodedFormEntity(formparams));
    request.addHeader("Content-Type", "application/x-www-form-urlencoded");

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return String.valueOf(extractJsonMap(reb).get("access_token"));
  }

  public static String loginPlatformAdminUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    return loginUser(
      new java.util.AbstractMap.SimpleEntry<>("test.platform.admin@test.com", "test")
    );
  }

  public static String loginCaaAdminUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    return loginUser(
      new java.util.AbstractMap.SimpleEntry<>("test.caa.admin@test.com", "test")
    );
  }

  public static String loginManufacturerAdminUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    return loginUser(
      new java.util.AbstractMap.SimpleEntry<>(
        "test.manufacturer.0.admin@test.com",
        "test"
      )
    );
  }

  public static String loginRepairAgencyAdminUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    return loginUser(
      new java.util.AbstractMap.SimpleEntry<>(
        "test.repair.agency.0.admin@test.com",
        "test"
      )
    );
  }

  public static String loginTraderAdminUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    return loginUser(
      new java.util.AbstractMap.SimpleEntry<>("test.trader.0.admin@test.com", "test")
    );
  }

  public static String loginOperatorAdminUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    return loginUser(
      new java.util.AbstractMap.SimpleEntry<>("test.operator.0.admin@test.com", "test")
    );
  }

  public static String loginPilotUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    return loginUser(
      new java.util.AbstractMap.SimpleEntry<>("test.pilot.0@test.com", "test")
    );
  }

  public static String loginDsspAdminUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    return loginUser(
      new java.util.AbstractMap.SimpleEntry<>("test.dssp.0.admin@test.com", "test")
    );
  }

  public static String loginOwnerUser()
    throws ClientProtocolException, IOException, JsonProcessingException {
    return loginUser(
      new java.util.AbstractMap.SimpleEntry<>("test.uas.0.owner@test.com", "test")
    );
  }

  // ── Entity create helpers ─────────────────────────────────────────────────

  public static UUID userCreate(String jwt)
    throws ClientProtocolException, IOException, JsonProcessingException, ParseException {
    UUID uid = UUID.fromString(parseJwt(jwt).getJWTClaimsSet().getSubject());
    String body = fill(loadFixture("user.json"), "id", uid.toString());

    HttpPost request = new HttpPost(REGISTRY_BASE_URL + "/api/v1/user");
    request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
    request.addHeader("Authorization", "Bearer " + jwt);

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    Logging.info("USER CREATE RESPONSE: " + reb);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public static UUID pilotCreate(String jwt)
    throws ClientProtocolException, IOException, JsonProcessingException, ParseException {
    UUID uid = UUID.fromString(parseJwt(jwt).getJWTClaimsSet().getSubject());
    String body = fill(loadFixture("pilot.json"), "userId", uid.toString());

    HttpPost request = new HttpPost(REGISTRY_BASE_URL + "/api/v1/pilot");
    request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
    request.addHeader("Authorization", "Bearer " + jwt);

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    Logging.info("pilot create response: " + reb);
    return extractUuid(reb);
  }

  public static UUID legalEntityCreate(String jwt, UUID id)
    throws ClientProtocolException, IOException, JsonProcessingException {
    String suffix = id.toString().substring(0, 8);
    String body = fill(
      loadFixture("legal-entity.json"),
      "id", id.toString(),
      "cin", "CIN" + suffix,
      "gstin", "GSTIN" + suffix
    );

    HttpPost request = new HttpPost(REGISTRY_BASE_URL + "/api/v1/legalEntity");
    request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
    request.addHeader("Authorization", "Bearer " + jwt);

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public static UUID manufacturerCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    String suffix = x.toString().substring(0, 8);
    String body = fill(
      loadFixture("manufacturer.json"),
      "legalEntityId", x.toString(),
      "cin", "CIN" + suffix,
      "gstin", "GSTIN" + suffix
    );

    HttpPost request = new HttpPost(REGISTRY_BASE_URL + "/api/v1/manufacturer");
    request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
    request.addHeader("Authorization", "Bearer " + jwt);

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public static UUID civilAviationAuthorityCreate(String jwt, UUID x, UUID leid)
    throws ClientProtocolException, IOException, JsonProcessingException {
    String suffix = x.toString().substring(0, 8);
    String body = fill(
      loadFixture("civil-aviation-authority.json"),
      "id", x.toString(),
      "legalEntityId", leid.toString(),
      "cin", "CIN" + suffix,
      "gstin", "GSTIN" + suffix
    );

    HttpPost request = new HttpPost(
      REGISTRY_BASE_URL + "/api/v1/civilAviationAuthority"
    );
    request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
    request.addHeader("Authorization", "Bearer " + jwt);

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public static String listCivilAviationAuthorities(AuthZ authz) {
    List<String> regulatorList = new ArrayList<String>(authz.lookupRegulator());
    return regulatorList.get(0);
  }

  public static UUID operatorCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    String suffix = x.toString().substring(0, 8);
    String body = fill(
      loadFixture("operator.json"),
      "legalEntityId", x.toString(),
      "cin", "CIN" + suffix,
      "gstin", "GSTIN" + suffix
    );

    HttpPost request = new HttpPost(REGISTRY_BASE_URL + "/api/v1/operator");
    request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
    request.addHeader("Authorization", "Bearer " + jwt);

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public static UUID dsspCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    String suffix = x.toString().substring(0, 8);
    String body = fill(
      loadFixture("dssp.json"),
      "legalEntityId", x.toString(),
      "cin", "CIN" + suffix,
      "gstin", "GSTIN" + suffix
    );

    HttpPost request = new HttpPost(
      REGISTRY_BASE_URL + "/api/v1/digitalSkyServiceProvider"
    );
    request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
    request.addHeader("Authorization", "Bearer " + jwt);

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public static UUID uasTypeCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    return uasTypeCreateWithCategory(jwt, x, "A");
  }

  public static UUID uasTypeBCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    return uasTypeCreateWithCategory(jwt, x, "B");
  }

  public static UUID uasTypeCCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    return uasTypeCreateWithCategory(jwt, x, "C");
  }

  private static UUID uasTypeCreateWithCategory(String jwt, UUID x, String category)
    throws ClientProtocolException, IOException, JsonProcessingException {
    String suffix = x.toString().substring(0, 8);
    String body = fill(
      loadFixture("uas-type.json"),
      "manufacturerId", x.toString(),
      "gstin", "GSTIN" + suffix,
      "operationCategory", category
    );

    HttpPost request = new HttpPost(REGISTRY_BASE_URL + "/api/v1/uasType");
    request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
    request.addHeader("Authorization", "Bearer " + jwt);

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    Logging.info("UAS TYPE CREATE RESPONSE " + reb);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    return extractUuid(reb);
  }

  public static UUID uasCreate(String jwt, UUID uasTypeId, UUID leid, Integer oemSerialNumber)
    throws ClientProtocolException, IOException, JsonProcessingException, ParseException {
    String suffix = leid.toString().substring(0, 8);
    String body = fill(
      loadFixture("uas.json"),
      "uasTypeId", uasTypeId.toString(),
      "legalEntityId", leid.toString(),
      "cin", "CIN" + suffix,
      "gstin", "GSTIN" + suffix,
      "oemSerialNumber", String.valueOf(oemSerialNumber)
    );

    HttpPost request = new HttpPost(REGISTRY_BASE_URL + "/api/v1/uas");
    request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
    request.addHeader("Authorization", "Bearer " + jwt);

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    Logging.info("UAS CREATE RESPONSE: " + reb);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    return extractUuid(reb);
  }

  public static UUID repairAgencyCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    String suffix = x.toString().substring(0, 8);
    String body = fill(
      loadFixture("repair-agency.json"),
      "legalEntityId", x.toString(),
      "cin", "CIN" + suffix,
      "gstin", "GSTIN" + suffix
    );

    HttpPost request = new HttpPost(REGISTRY_BASE_URL + "/api/v1/repairAgency");
    request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
    request.addHeader("Authorization", "Bearer " + jwt);

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public static UUID traderCreate(String jwt, UUID x)
    throws ClientProtocolException, IOException, JsonProcessingException {
    String suffix = x.toString().substring(0, 8);
    String body = fill(
      loadFixture("trader.json"),
      "legalEntityId", x.toString(),
      "cin", "CIN" + suffix,
      "gstin", "GSTIN" + suffix
    );

    HttpPost request = new HttpPost(REGISTRY_BASE_URL + "/api/v1/trader");
    request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
    request.addHeader("Authorization", "Bearer " + jwt);

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    return extractUuid(reb);
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
    String body = fill(
      loadFixture("flight-plan.json"),
      "id", id.toString(),
      "uasId", uasId.toString(),
      "uasTypeId", uasTypeId.toString(),
      "manufacturerId", mId.toString(),
      "legalEntityId", leId.toString(),
      "pilotId", pilotId.toString()
    );

    HttpPost request = new HttpPost(FLIGHT_AUTH_BASE_URL + "/api/v1/flightPlan");
    request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
    request.addHeader("Authorization", "Bearer " + jwt);

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    Logging.info("flight plan create: " + reb);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    EntityUtils.consume(re);
    return extractUuid(reb);
  }

  public static UUID flightAuthorisationCreate(
    String jwt,
    UUID id,
    UUID fp,
    UUID uas,
    UUID pilot
  )
    throws ClientProtocolException, IOException, JsonProcessingException {
    String body = fill(
      loadFixture("airspace-usage-token.json"),
      "id", id.toString(),
      "uasId", uas.toString(),
      "pilotId", pilot.toString()
    );

    HttpPost request = new HttpPost(
      FLIGHT_AUTH_BASE_URL + "/api/v1/airspaceUsageToken"
    );
    request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
    request.addHeader("Authorization", "Bearer " + jwt);

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    Logging.info("aut create: " + reb);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    return extractUuid(reb);
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
    ObjectNode body = objectMapper.createObjectNode();
    body.set(
      "timestamps",
      objectMapper.readTree(
        "{\"updated\": \"2023-11-27T07:48:03.686Z\", \"created\": \"2023-11-27T07:48:03.686Z\"}"
      )
    );
    body.set(
      "validity",
      objectMapper.readTree(
        "{\"from\": \"2024-01-11T11:01:56.896Z\", \"till\": \"2024-01-11T11:01:56.896Z\"}"
      )
    );
    body.put("holding", holding != null ? holding : true);

    ObjectNode uasNode = (ObjectNode) objectMapper.readTree(
      fill(loadFixture("sale-uas.json"), "uasId", uasId.toString())
    );
    body.set("uas", uasNode);

    if (su != null) {
      body.set(
        "seller_user",
        objectMapper.readTree(fill(loadFixture("person-ref.json"), "id", su.toString()))
      );
    }
    if (bu != null) {
      body.set(
        "buyer_user",
        objectMapper.readTree(fill(loadFixture("person-ref.json"), "id", bu.toString()))
      );
    }
    if (sle != null) {
      body.set(
        "seller_legal_entity",
        objectMapper.readTree(
          fill(loadFixture("legal-entity-ref.json"), "id", sle.toString())
        )
      );
    }
    if (ble != null) {
      body.set(
        "buyer_legal_entity",
        objectMapper.readTree(
          fill(loadFixture("legal-entity-ref.json"), "id", ble.toString())
        )
      );
    }

    HttpPost request = new HttpPost(REGISTRY_BASE_URL + "/api/v1/sale");
    request.setEntity(
      new StringEntity(body.toString(), ContentType.APPLICATION_JSON)
    );
    request.addHeader("Authorization", "Bearer " + jwt);

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    EntityUtils.consume(re);
    Logging.info("sale create response: " + reb);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    return extractUuid(reb);
  }

  public static void approveUasType(String jwt, UUID id)
    throws ClientProtocolException, IOException, JsonProcessingException {
    int modelNumber = (int) (Math.random() * 4096);
    StringEntity e = new StringEntity(
      String.valueOf(modelNumber),
      ContentType.APPLICATION_JSON
    );
    HttpPost request = new HttpPost(
      REGISTRY_BASE_URL + "/api/v1/uasType/approve/" + id.toString()
    );
    request.setEntity(e);
    request.addHeader("Authorization", "Bearer " + jwt);

    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    HttpEntity re = httpResponse.getEntity();
    String reb = EntityUtils.toString(re);
    Logging.info("UasType Approve Response: " + reb);
    assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    EntityUtils.consume(re);
  }

  // ── Stub approval methods (not yet implemented server-side) ──────────────

  public static void approveManufacturer(String jwt, UUID id) {}

  public static void approveOperator(String jwt, UUID id) {}

  public static void approvePilot(String jwt, UUID id) {}

  public static void approveDssp(String jwt, UUID id) {}

  public static void approveTrader(String jwt, UUID id) {}

  public static void approveRepairAgency(String jwt, UUID id) {}

  public static void approveTransferCreate(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  public static void approveSale(String jwt, UUID id) {
    assertEquals(1, 2);
  }

  // ── SpiceDB / AuthZ grant helpers ─────────────────────────────────────────

  public static boolean grantPlatformAdmin(AuthZ authZ, UUID platformAdminUserUUID) {
    return authZ.createPlatformAdmin(platformAdminUserUUID.toString());
  }

  public static boolean associateCAAToPlatform(AuthZ authZ, UUID CAAResourceUUID) {
    return authZ.associateCAAToPlatform(CAAResourceUUID.toString());
  }

  public static boolean grantCAAAdmin(
    AuthZ authZ,
    UUID CAAResourceUUID,
    UUID platformAdminUserUUID,
    UUID CAAAdminUserUUID
  ) {
    return authZ.createCAAAdmin(
      CAAResourceUUID.toString(),
      CAAAdminUserUUID.toString(),
      platformAdminUserUUID.toString()
    );
  }

  public static boolean grantManufacturerAdmin(
    AuthZ authZ,
    UUID manufacturerUUID,
    UUID manufacturerAdminUUID,
    UUID caaResourceUUID
  ) {
    return authZ.createResoureTypeAdmin(
      ResourceType.MANUFACTURER,
      manufacturerUUID.toString(),
      manufacturerAdminUUID.toString(),
      caaResourceUUID.toString()
    );
  }

  public static boolean grantOperatorAdmin(
    AuthZ authZ,
    UUID operatorUUID,
    UUID operatorAdminUUID,
    UUID caaResourceUUID
  ) {
    return authZ.createResoureTypeAdmin(
      ResourceType.OPERATOR,
      operatorUUID.toString(),
      operatorAdminUUID.toString(),
      caaResourceUUID.toString()
    );
  }

  public static boolean grantDSSPAdmin(
    AuthZ authZ,
    UUID dsspUUID,
    UUID dsspAdminUUID,
    UUID caaResourceUUID
  ) {
    return authZ.createResoureTypeAdmin(
      ResourceType.DSSP,
      dsspUUID.toString(),
      dsspAdminUUID.toString(),
      caaResourceUUID.toString()
    );
  }

  public static boolean grantRepairAgencyAdmin(
    AuthZ authZ,
    UUID repairAgencyUUID,
    UUID repairAgencyAdminUUID,
    UUID caaResourceUUID
  ) {
    return authZ.createResoureTypeAdmin(
      ResourceType.REPAIRAGENCY,
      repairAgencyUUID.toString(),
      repairAgencyAdminUUID.toString(),
      caaResourceUUID.toString()
    );
  }

  public static boolean grantTraderAdmin(
    AuthZ authZ,
    UUID traderUUID,
    UUID traderAdminUUID,
    UUID caaResourceUUID
  ) {
    return authZ.createResoureTypeAdmin(
      ResourceType.TRADER,
      traderUUID.toString(),
      traderAdminUUID.toString(),
      caaResourceUUID.toString()
    );
  }

  public static boolean associatePilotToRegulator(
    AuthZ authZ,
    UUID pilotResourceUUID,
    UUID pilotUUID,
    UUID regulatorUUID
  ) {
    return authZ.addPilot(
      pilotResourceUUID.toString(),
      pilotUUID.toString(),
      regulatorUUID.toString()
    );
  }

  public static boolean associateUASTypeToManufacturer(
    AuthZ authZ,
    UUID uasTypeUUID,
    UUID manufacturerUUID,
    UUID manufacturerAdminUserUUID,
    UUID caaResourceUUID
  ) {
    return authZ.createUASTypeRelationships(
      uasTypeUUID.toString(),
      manufacturerUUID.toString(),
      manufacturerAdminUserUUID.toString(),
      caaResourceUUID.toString()
    );
  }

  public static boolean associateUASToManufacturer(
    AuthZ authZ,
    UUID uasUUID,
    UUID manufacturerUUID,
    UUID manufacturerAdminUserUUID,
    UUID caaResourceUUID
  ) {
    return authZ.createUASManufacturerRelationships(
      uasUUID.toString(),
      manufacturerUUID.toString(),
      manufacturerAdminUserUUID.toString(),
      caaResourceUUID.toString()
    );
  }

  public static boolean approveManufacturer(
    AuthZ authZ,
    UUID manufacturerUUID,
    UUID CAAAdminUserID
  ) {
    return authZ.approveResourceByRegulator(
      ResourceType.MANUFACTURER,
      manufacturerUUID.toString(),
      CAAAdminUserID.toString()
    );
  }

  public static boolean approveUASType(
    AuthZ authZ,
    UUID uasTypeUUID,
    UUID CAAAdminUserID
  ) {
    return authZ.approveResourceByRegulator(
      ResourceType.UASTYPE,
      uasTypeUUID.toString(),
      CAAAdminUserID.toString()
    );
  }

  public static boolean approveOperator(
    AuthZ authZ,
    UUID operatorUUID,
    UUID CAAAdminUserID
  ) {
    return authZ.approveResourceByRegulator(
      ResourceType.OPERATOR,
      operatorUUID.toString(),
      CAAAdminUserID.toString()
    );
  }

  public static boolean approveDssp(AuthZ authZ, UUID dsspUUID, UUID CAAAdminUserID) {
    return authZ.approveResourceByRegulator(
      ResourceType.DSSP,
      dsspUUID.toString(),
      CAAAdminUserID.toString()
    );
  }

  public static boolean approveTrader(
    AuthZ authZ,
    UUID traderUUID,
    UUID CAAAdminUserID
  ) {
    return authZ.approveResourceByRegulator(
      ResourceType.TRADER,
      traderUUID.toString(),
      CAAAdminUserID.toString()
    );
  }

  public static boolean approveRepairAgency(
    AuthZ authZ,
    UUID repairAgencyUUID,
    UUID CAAAdminUserID
  ) {
    return authZ.approveResourceByRegulator(
      ResourceType.REPAIRAGENCY,
      repairAgencyUUID.toString(),
      CAAAdminUserID.toString()
    );
  }

  public static boolean approvePilot(
    AuthZ authZ,
    UUID pilotResourceUUID,
    UUID CAAAdminUserID
  ) {
    return authZ.approveResourceByRegulator(
      ResourceType.PILOT,
      pilotResourceUUID.toString(),
      CAAAdminUserID.toString()
    );
  }
}
