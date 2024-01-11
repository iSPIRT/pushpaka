package in.ispirt.pushpaka.unittests;

import in.ispirt.pushpaka.flightauthorisation.aut.AirspaceUsageTokenUtils;
import in.ispirt.pushpaka.flightauthorisation.models.AirspaceUsageToken;
import in.ispirt.pushpaka.flightauthorisation.models.AirspaceUsageTokenAttenuations;
import in.ispirt.pushpaka.flightauthorisation.models.GeocageData;
import in.ispirt.pushpaka.flightauthorisation.models.GeospatialData;
import in.ispirt.pushpaka.registry.models.OperationCategory;
import in.ispirt.pushpaka.registry.models.Pilot;
import in.ispirt.pushpaka.registry.models.Uas;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.jose4j.jwt.JwtClaims;
import org.junit.jupiter.api.Test;

public class Aut {

  @Test
  public void testAutGeneration() {
    // generate & patch attenuations VLOS
    AirspaceUsageToken airspaceUsageToken = AirspaceUsageTokenUtils.createAirspaceUsageTokenObject(
        new Uas(UUID.randomUUID()),
        new Pilot(UUID.randomUUID()),
        null,
        OperationCategory.C3,
        OffsetDateTime.parse(
            "2023-11-19T14:28:15+05:30",
            DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        OffsetDateTime.parse(
            "2023-11-19T15:28:15+05:30",
            DateTimeFormatter.ISO_OFFSET_DATE_TIME));

    AirspaceUsageTokenAttenuations airspaceUsageTokenAttenuations_VLOS = new AirspaceUsageTokenAttenuations();

    GeocageData geocageData = new GeocageData();
    geocageData.setRadius(150);

    airspaceUsageTokenAttenuations_VLOS.setGeocage(geocageData);
    airspaceUsageTokenAttenuations_VLOS.setEmergencyTermination(false);

    AirspaceUsageTokenUtils.updateAirspaceUsageTokenAttenuationsObject(
        airspaceUsageToken,
        airspaceUsageTokenAttenuations_VLOS);

    System.out.println(airspaceUsageToken.toJsonString());

    // signing the claim
    try {
      String signedToken = AirspaceUsageTokenUtils.signAirspaceUsageTokenObjectJWT(
          AirspaceUsageTokenUtils.getDigitalSkyPrivateKey("digitalsky.jks"),
          "digitalsky",
          airspaceUsageToken,
          "Issuer",
          "Audience",
          "subject",
          10,
          2);

      System.out.println("===signedToken===");
      System.out.println(signedToken);
      System.out.println("===signedToken===");

      JwtClaims jwtClaims = AirspaceUsageTokenUtils.validateAirspaceUsageTokenObjectJWT(
          AirspaceUsageTokenUtils.getDigitalSkyPublicKey("digitalsky.cer"),
          signedToken,
          "Issuer",
          "Audience",
          30);

      System.out.println("===jwtClaims===");
      System.out.println(jwtClaims);
      System.out.println("===jwtClaims===");
      System.out.println("===jwk===");
      System.out.println(AirspaceUsageTokenUtils.getDigitalSkyJwk("digitalsky.cer"));
      System.out.println("===jwk===");
    } catch (Exception e) {
      e.printStackTrace();
    }

    // generate and patch attenuations for BVLOS
    airspaceUsageToken = AirspaceUsageTokenUtils.createAirspaceUsageTokenObject(
        new Uas(UUID.randomUUID()),
        new Pilot(UUID.randomUUID()),
        null,
        OperationCategory.C1,
        OffsetDateTime.parse(
            "2023-11-19T14:28:15+05:30",
            DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        OffsetDateTime.parse(
            "2023-11-19T15:28:15+05:30",
            DateTimeFormatter.ISO_OFFSET_DATE_TIME));

    AirspaceUsageTokenAttenuations airspaceUsageTokenAttenuations_BVLOS = new AirspaceUsageTokenAttenuations();

    GeospatialData geospatialData = new GeospatialData();
    geospatialData.setLatitude(123.45);
    geospatialData.setLongitude(67.89);

    airspaceUsageTokenAttenuations_BVLOS.setWaypoints(geospatialData);
    airspaceUsageTokenAttenuations_BVLOS.setEmergencyTermination(false);

    AirspaceUsageTokenUtils.updateAirspaceUsageTokenAttenuationsObject(
        airspaceUsageToken,
        airspaceUsageTokenAttenuations_BVLOS);

    System.out.println(airspaceUsageToken.toJsonString());

    try {
      String signedToken = AirspaceUsageTokenUtils.signAirspaceUsageTokenObjectJWT(
          AirspaceUsageTokenUtils.getDigitalSkyPrivateKey("digitalsky.jks"),
          "digitalsky",
          airspaceUsageToken,
          "Issuer",
          "Audience",
          "subject",
          10,
          2);

      System.out.println(signedToken);

      JwtClaims jwtClaims = AirspaceUsageTokenUtils.validateAirspaceUsageTokenObjectJWT(
          AirspaceUsageTokenUtils.getDigitalSkyPublicKey("digitalsky.cer"),
          signedToken,
          "Issuer",
          "Audience",
          30);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
