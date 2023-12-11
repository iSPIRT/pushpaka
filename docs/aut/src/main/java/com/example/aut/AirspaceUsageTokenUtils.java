package com.example.aut;

import java.util.UUID;

public class AirspaceUsageTokenUtils {

  //methods below are representative of manipulation of AUT at object level in SDK
  public static AirspaceUsageToken createAirspaceUsageTokenObject(
    String droneID,
    String pilotID,
    AirspaceUsageOperationType airspaceUsageOperationType,
    String startTime,
    String endTime
  ) {
    //Create AUT for VLOS
    AirspaceUsageToken airspaceUsageToken = new AirspaceUsageToken();
    UUID uuid = UUID.randomUUID();

    airspaceUsageToken.setId(uuid.toString());
    airspaceUsageToken.setDroneID(droneID);
    airspaceUsageToken.setPilotID(pilotID);
    airspaceUsageToken.setOperationType(airspaceUsageOperationType);
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

  public static String signAirspaceUsageTokenObjectJWT(
    AirspaceUsageToken airspaceUsageToken
  ) {
    return null;
  }
  
  public static String validateAirspaceUsageTokenObjectJWT(
   String signedAirspaceUsageToken
  ) {
    return null;
  }

  //methods below are representative of service end points for AUT
  public static String generateAirspaceUsageToken(
    String droneID,
    String pilotID,
    AirspaceUsageOperationType airspaceUsageOperationType,
    String startTime,
    String endTime
  ) {
    //Create AUT for VLOS
    AirspaceUsageToken airspaceUsageToken = new AirspaceUsageToken();
    UUID uuid = UUID.randomUUID();

    airspaceUsageToken.setId(uuid.toString());
    airspaceUsageToken.setDroneID(droneID);
    airspaceUsageToken.setPilotID(pilotID);
    airspaceUsageToken.setOperationType(airspaceUsageOperationType);
    airspaceUsageToken.setStartTime(startTime);
    airspaceUsageToken.setEndTime(endTime);
    airspaceUsageToken.setState(AirspaceUsageTokenState.CREATED);

    return airspaceUsageToken.toJson();
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
