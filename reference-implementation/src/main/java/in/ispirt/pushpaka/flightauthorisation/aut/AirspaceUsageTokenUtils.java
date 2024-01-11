package in.ispirt.pushpaka.flightauthorisation.aut;

import com.google.gson.JsonObject;
import in.ispirt.pushpaka.flightauthorisation.models.AirspaceUsageOperationType;
import in.ispirt.pushpaka.flightauthorisation.models.AirspaceUsageToken;
import in.ispirt.pushpaka.flightauthorisation.models.AirspaceUsageTokenAttenuations;
import in.ispirt.pushpaka.flightauthorisation.models.AirspaceUsageTokenState;
import in.ispirt.pushpaka.flightauthorisation.models.FlightPlan;
import in.ispirt.pushpaka.flightauthorisation.models.GeocageData;
import in.ispirt.pushpaka.flightauthorisation.models.GeospatialData;
import in.ispirt.pushpaka.registry.models.OperationCategory;
import in.ispirt.pushpaka.registry.models.Pilot;
import in.ispirt.pushpaka.registry.models.Uas;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

public class AirspaceUsageTokenUtils {

  //methods below are representative of manipulation of AUT at object level in SDK
  public static AirspaceUsageToken createAirspaceUsageTokenObject(
    Uas uas,
    Pilot pilot,
    FlightPlan flightPlan,
    OperationCategory operationCategory,
    OffsetDateTime startTime,
    OffsetDateTime endTime
  ) {
    //Create AUT for VLOS
    AirspaceUsageToken airspaceUsageToken = new AirspaceUsageToken();
    UUID uuid = UUID.randomUUID();

    airspaceUsageToken.setId(uuid);
    airspaceUsageToken.setUas(uas);
    airspaceUsageToken.setPilot(pilot);
    airspaceUsageToken.setFlightPlan(flightPlan);
    airspaceUsageToken.setOperationCategory(operationCategory);
    airspaceUsageToken.setStartTime(startTime);
    airspaceUsageToken.setEndTime(endTime);
    airspaceUsageToken.setState(AirspaceUsageTokenState.CREATED);

    return airspaceUsageToken;
  }

  public static void updateAirspaceUsageTokenAttenuationsObject(
    AirspaceUsageToken airspaceUsageToken,
    AirspaceUsageTokenAttenuations airspaceUsageTokenAttenuations
  ) {
    airspaceUsageToken.setAttenuations(airspaceUsageTokenAttenuations);
  }

  public static PublicKey getDigitalSkyPublicKey(String filename) throws Exception {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    X509Certificate cert = (X509Certificate) cf.generateCertificate(
      new FileInputStream(filename)
    );

    PublicKey retVal = cert.getPublicKey();

    return retVal;
  }

  public static String getDigitalSkyJwk(String filename) throws Exception {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    X509Certificate cert = (X509Certificate) cf.generateCertificate(
      new FileInputStream(filename)
    );

    PublicKey retVal = cert.getPublicKey();

    com.nimbusds.jose.jwk.RSAKey jwk = new com.nimbusds.jose.jwk.RSAKey.Builder(
      (RSAPublicKey) retVal
    )
    .build();

    return jwk.toJSONString();
  }

  public static PrivateKey getDigitalSkyPrivateKey(String filename) throws Exception {
    //keytool -genkey -alias digitalsky -keyalg RSA -keystore digitalsky.jks -keysize 2048
    //keytool -export -keystore digitalsky.jks -alias digitalsky -file digitalsky.cer

    String jksPassword = "digitalsky";

    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    ks.load(new FileInputStream("digitalsky.jks"), jksPassword.toCharArray());
    PrivateKey privateKey = (PrivateKey) ks.getKey(
      jksPassword,
      jksPassword.toCharArray()
    );

    return privateKey;
  }

  public static String signAirspaceUsageTokenObjectJWT(
    PrivateKey privateKey,
    String keyID,
    AirspaceUsageToken airspaceUsageToken,
    String issuer,
    String audience,
    String subject,
    int expirationTimePeriod,
    int validtionTimePeriod
  ) {
    String jwt = null;
    //String additionalClaimAsString = airspaceUsageToken.toJsonString();
    Map additionalClaimAsJsonObject = airspaceUsageToken.toJsonObject();

    try {
      RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
      rsaJsonWebKey.setKeyId("k1");

      JwtClaims claims = new JwtClaims();
      claims.setIssuer(issuer); // who creates the token and signs it
      claims.setAudience(audience); // to whom the token is intended to be sent
      claims.setExpirationTimeMinutesInTheFuture(expirationTimePeriod); // time when the token will expire (10 minutes from now)
      claims.setGeneratedJwtId(); // a unique identifier for the token
      claims.setIssuedAtToNow(); // when the token was issued/created (now)
      claims.setNotBeforeMinutesInThePast(validtionTimePeriod); // time before which the token is not yet valid (2 minutes ago)
      claims.setSubject(subject); // the subject/principal is whom the token is about
      claims.setClaim("payload", additionalClaimAsJsonObject); // additional claims/attributes about the subject can be added

      JsonWebSignature jws = new JsonWebSignature();
      jws.setPayload(claims.toJson());
      jws.setKey(privateKey);
      jws.setKeyIdHeaderValue(keyID);
      jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
      jwt = jws.getCompactSerialization();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return jwt;
  }

  public static JwtClaims validateAirspaceUsageTokenObjectJWT(
    Key publicKey,
    String signedAirspaceUsageToken,
    String issuer,
    String audience,
    int expirationTimePeriod
  ) {
    JwtClaims jwtClaims = null;

    try {
      JwtConsumer jwtConsumer = new JwtConsumerBuilder()
        .setRequireExpirationTime() // the JWT must have an expiration time
        .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
        .setRequireSubject() // the JWT must have a subject claim
        .setExpectedIssuer(issuer) // whom the JWT needs to have been issued by
        .setExpectedAudience(audience) // to whom the JWT is intended for
        .setVerificationKey(publicKey) // verify the signature with the public key
        .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
          ConstraintType.PERMIT,
          AlgorithmIdentifiers.RSA_USING_SHA256
        ) // which is only RS256 here
        .build(); // create the JwtConsumer instance

      try {
        //  Validate the JWT and process it to the Claims
        jwtClaims = jwtConsumer.processToClaims(signedAirspaceUsageToken);
      } catch (InvalidJwtException e) {
        // InvalidJwtException will be thrown, if the JWT failed processing or validation in anyway.
        // Hopefully with meaningful explanations(s) about what went wrong.
        System.out.println("Invalid JWT! " + e);

        // Programmatic access to (some) specific reasons for JWT invalidity is also possible
        // should you want different error handling behavior for certain conditions.

        // Whether or not the JWT has expired being one common reason for invalidity
        if (e.hasExpired()) {
          System.out.println(
            "JWT expired at " + e.getJwtContext().getJwtClaims().getExpirationTime()
          );
        }

        // Or maybe the audience was invalid
        if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID)) {
          System.out.println(
            "JWT had wrong audience: " + e.getJwtContext().getJwtClaims().getAudience()
          );
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return jwtClaims;
  }

  //methods below are representative of service end points for AUT
  public static String generateAirspaceUsageToken(
    Uas uas,
    Pilot pilot,
    OperationCategory operationCategory,
    OffsetDateTime startTime,
    OffsetDateTime endTime
  ) {
    //Create AUT for VLOS
    AirspaceUsageToken airspaceUsageToken = new AirspaceUsageToken();
    UUID uuid = UUID.randomUUID();

    airspaceUsageToken.setId(uuid);
    airspaceUsageToken.setUas(uas);
    airspaceUsageToken.setPilot(pilot);
    airspaceUsageToken.setOperationCategory(operationCategory);
    airspaceUsageToken.setStartTime(startTime);
    airspaceUsageToken.setEndTime(endTime);
    airspaceUsageToken.setState(AirspaceUsageTokenState.CREATED);

    return airspaceUsageToken.toJsonString();
  }

  public static void updateAirspaceUsageTokenAttenuations(
    String airspaceUsageTokenId,
    AirspaceUsageTokenAttenuations airspaceUsageTokenAttenuations
  ) {
    //update the attentuation and return the payload
  }

  public static void signAirspaceUsageToken(String airspaceUsageTokenId) {
    //get the JSON representation of the AUT
  }

  public static void validateAirspaceUsageToken(String airspaceUsageTokenId) {
    //get the JSON representation of the AUT
  }

  public static void activateAirspaceUsageToken(String airspaceUsageTokenId) {
    //use the in-use state and update the AUT state
  }

  public static void expireAirspaceUsageToken(String airspaceUsageTokenId) {
    //use the expiration state and update the end time of the AUT
  }
}
