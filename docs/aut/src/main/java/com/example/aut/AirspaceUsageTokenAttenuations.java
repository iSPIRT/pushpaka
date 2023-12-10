package com.example.aut;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AirspaceUsageTokenAttenuations {
    private GeocageData geocage;
    private GeospatialData waypoints;
    private boolean emergencyTermination;
    
    public GeocageData getGeocage() {
        return geocage;
    }
    public void setGeocage(GeocageData geocage) {
        this.geocage = geocage;
    }
    public GeospatialData getWaypoints() {
        return waypoints;
    }
    public void setWaypoints(GeospatialData waypoints) {
        this.waypoints = waypoints;
    }
    public boolean isEmergencyTermination() {
        return emergencyTermination;
    }
    public void setEmergencyTermination(boolean emergencyTermination) {
        this.emergencyTermination = emergencyTermination;
    }

    public String toJson(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = gson.toJsonTree(this);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("attenuations", jsonElement);
        String airspaceUsageTokenAttenuationsJson = gson.toJson(jsonObject);
        
        return airspaceUsageTokenAttenuationsJson;
    }

}
