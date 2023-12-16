package com.example.aut;

import org.jose4j.jwt.JwtClaims;

public class App {

    public static void main(String[] args) {
    //generate & patch attenuations VLOS
    AirspaceUsageToken airspaceUsageToken = AirspaceUsageTokenUtils.createAirspaceUsageTokenObject(
    "DRONE12345",
    "PILOT98765",
    AirspaceUsageOperationType.VLOS,
    "2023-11-19T14:28:15Z",
    "2023-11-19T15:28:15Z"
    );

    AirspaceUsageTokenAttenuations airspaceUsageTokenAttenuations_VLOS = new AirspaceUsageTokenAttenuations();

    GeocageData geocageData = new GeocageData();
    geocageData.setRadius(150);

    airspaceUsageTokenAttenuations_VLOS.setGeocage(geocageData);
    airspaceUsageTokenAttenuations_VLOS.setEmergencyTermination(false);

    AirspaceUsageTokenUtils.updateAirspaceUsageTokenAttenuationsObject(
    airspaceUsageToken,
    airspaceUsageTokenAttenuations_VLOS
    );

    System.out.println(airspaceUsageToken.toJson());

    //signing the claim
    try{  
      String signedToken = AirspaceUsageTokenUtils.signAirspaceUsageTokenObjectJWT(
      AirspaceUsageTokenUtils.getDigitalSkyPrivateKey("digitalsky.jks"),
      "digitalsky",
      airspaceUsageToken,
      "Issuer",
      "Audience",
      "subject",
      10,
      2
      );

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

    //generate and patch attenuations for BVLOS
    airspaceUsageToken =
    AirspaceUsageTokenUtils.createAirspaceUsageTokenObject(
      "DRONE12345",
      "PILOT98765",
      AirspaceUsageOperationType.BVLOS,
      "2023-11-19T14:28:15Z",
      "2023-11-19T15:28:15Z"
    );

    AirspaceUsageTokenAttenuations airspaceUsageTokenAttenuations_BVLOS = new AirspaceUsageTokenAttenuations();

    GeospatialData geospatialData = new GeospatialData();
    geospatialData.setLatitude(123.45);
    geospatialData.setLongitude(67.89);

    airspaceUsageTokenAttenuations_BVLOS.setWaypoints(geospatialData);
    airspaceUsageTokenAttenuations_BVLOS.setEmergencyTermination(false);

    AirspaceUsageTokenUtils.updateAirspaceUsageTokenAttenuationsObject(
    airspaceUsageToken,
    airspaceUsageTokenAttenuations_BVLOS
    );

    System.out.println(airspaceUsageToken.toJson());

    try {
      String signedToken = AirspaceUsageTokenUtils.signAirspaceUsageTokenObjectJWT(
      AirspaceUsageTokenUtils.getDigitalSkyPrivateKey("digitalsky.jks"),
      "digitalsky",
      airspaceUsageToken,
      "Issuer",
      "Audience",
      "subject",
      10,
      2
      );

      System.out.println(signedToken);

      JwtClaims jwtClaims = AirspaceUsageTokenUtils.validateAirspaceUsageTokenObjectJWT(
      AirspaceUsageTokenUtils.getDigitalSkyPublicKey("digitalsky.cer"), 
      signedToken, 
      "Issuer", 
      "Audience", 
      30);

      System.out.println(jwtClaims);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}