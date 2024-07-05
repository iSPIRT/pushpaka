package in.ispirt.pushpaka.flightauthorisation.aut;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.*;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import in.ispirt.pushpaka.models.AirspaceUsageToken;
import in.ispirt.pushpaka.models.AirspaceUsageTokenAttenuations;
import in.ispirt.pushpaka.models.AirspaceUsageTokenState;
import in.ispirt.pushpaka.models.FlightPlan;
import in.ispirt.pushpaka.models.Pilot;
import in.ispirt.pushpaka.models.Uas;

import java.io.*;
import java.security.Key;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AirspaceUsageTokenUtils {
 private static ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();


  //methods below are representative of manipulation of AUT at object level in SDK
  public static AirspaceUsageToken createAirspaceUsageTokenObject(
    Uas uas,
    Pilot pilot,
    FlightPlan flightPlan,
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

  public static Key getDigitalSkyPublicKey() throws CertificateException, JOSEException {
    return getDigitalSkyJwk().toRSAKey().toPublicKey();
  }

  public static PrivateKey getDigitalSkyPrivateKey() throws JOSEException {
    return getDigitalSkyJwk().toRSAKey().toPrivateKey();
  }

  public static RSAKey getDigitalSkyRsaKey() throws JOSEException {
    return getDigitalSkyJwk().toRSAKey();
  }

  public static JWK getDigitalSkyJwk() throws JOSEException {
    InputStream is = AirspaceUsageTokenUtils.class.getClassLoader().getResourceAsStream("certs/private.pem");
    String privateKeyString = new BufferedReader(new InputStreamReader(is))
            .lines().collect(Collectors.joining("\n"));
      return JWK.parseFromPEMEncodedObjects(privateKeyString);
  }

  public static JWKSet getDigitalSkyJwks() throws JOSEException {
     return new JWKSet(getDigitalSkyJwk());
  }

  public static String signAirspaceUsageTokenObjectJWT(
          RSAKey rsaKey,
          String keyID,
          AirspaceUsageToken airspaceUsageToken,
          String issuer,
          String audience,
          String subject,
          int expirationTimeInMinutes,
          int validationTimePeriodInMinutes
  ) {
    String jwt = null;
    //String additionalClaimAsString = airspaceUsageToken.toJsonString();
    Map additionalClaimAsJsonObject = airspaceUsageToken.toJsonObject();

    try {
      // Create RSA-signer with the private key
      JWSSigner signer = new RSASSASigner(rsaKey);
      Date currentTime = new Date();
      JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
              .issuer(issuer)
              .audience(audience)
              .expirationTime(new Date(currentTime.getTime() + expirationTimeInMinutes * 60 * 1000))
              .issueTime(currentTime)
//              .notBeforeTime(new Date(currentTime.getTime() - validationTimePeriodInMinutes * 60 * 1000))
              .subject(subject)
              .claim("payload", additionalClaimAsJsonObject)
              .build();

      SignedJWT signedJWT = new SignedJWT(
              new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaKey.getKeyID()).type(JOSEObjectType.JWT).build(),
              claimsSet);

      signedJWT.sign(signer);
      return signedJWT.serialize();
    } catch (JOSEException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }


  public static JWTClaimsSet validateAirspaceUsageTokenObjectJWT(
    Key publicKey,
    String signedAirspaceUsageToken,
    String issuer,
    String audience,
    int expirationTimePeriod
  ) throws JOSEException {
    JWTClaimsSet jwtClaims = null;

    jwtProcessor.setJWSTypeVerifier(
            new DefaultJOSEObjectTypeVerifier<>(new JOSEObjectType(JOSEObjectType.JWT.getType())));
    // The expected JWS algorithm of the access tokens (agreed out-of-band)
    JWSAlgorithm expectedJWSAlg = JWSAlgorithm.RS256;
    JWKSource<SecurityContext> keySource = new ImmutableJWKSet(getDigitalSkyJwks());
    // Configure the JWT processor with a key selector to feed matching public
    // RSA keys sourced from the JWK set URL
    JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(
            expectedJWSAlg,
            keySource);
    jwtProcessor.setJWSKeySelector(keySelector);

    // Set the required JWT claims for access tokens
    jwtProcessor.setJWTClaimsSetVerifier(new DefaultJWTClaimsVerifier<>(
            new JWTClaimsSet.Builder().issuer(issuer).build(),
            new HashSet<>(Arrays.asList(
                    JWTClaimNames.SUBJECT,
                    JWTClaimNames.ISSUED_AT,
                    JWTClaimNames.EXPIRATION_TIME,
                    JWTClaimNames.ISSUER,
                    JWTClaimNames.AUDIENCE
            ))));

    // Process the token
    SecurityContext ctx = null; // optional context parameter, not required here
    JWTClaimsSet claimsSet;
    try {
      claimsSet = jwtProcessor.process(signedAirspaceUsageToken, ctx);
      return claimsSet;
    } catch (ParseException | BadJOSEException e) {
      // Invalid token
      System.err.println(e.getMessage());
      return jwtClaims;
    } catch (JOSEException e) {
      // Key sourcing failed or another internal exception
      System.err.println(e.getMessage());
      return jwtClaims;
    }
  }

  //methods below are representative of service end points for AUT
  public static String generateAirspaceUsageToken(
    Uas uas,
    Pilot pilot,
    OffsetDateTime startTime,
    OffsetDateTime endTime
  ) {
    //Create AUT for VLOS
    AirspaceUsageToken airspaceUsageToken = new AirspaceUsageToken();
    UUID uuid = UUID.randomUUID();

    airspaceUsageToken.setId(uuid);
    airspaceUsageToken.setUas(uas);
    airspaceUsageToken.setPilot(pilot);
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
