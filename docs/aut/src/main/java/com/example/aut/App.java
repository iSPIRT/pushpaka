package com.example.aut;

public class App {

  public static void main(String[] args) {
    //generate AUT for VLOS
    String airspaceUsageTokenPayload = AirspaceUsageTokenUtils.generateAirspaceUsageToken(
      "DRONE12345",
      "PILOT98765",
      AirspaceUsageOperationType.VLOS,
      "2023-11-19T14:28:15Z",
      "2023-11-19T15:28:15Z"
    );

    System.out.println(airspaceUsageTokenPayload);

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
  }
}
