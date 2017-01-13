package com.example.kamranali.geofenceprojectbroadcast;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by kamranali on 06/01/2017.
 */

public class Constants {
    public static final String CATEGORY_LOCATION_SERVICES = "com.example.kamranali.geofenceprojectbroadcast.ACTION_RECEIVE_GEOFENCE";

    private Constants() {
    }
    public static class SharedPrefs {
        public static String Geofences = "SHARED_PREFS_GEOFENCES";
    }
    public static final String PACKAGE_NAME = "com.example.kamranali.geofenceprojectbroadcast";

    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";
    public static final String ACTIVITY_EXTRA = PACKAGE_NAME + ".ACTIVITY_EXTRA";

    public static final String BROADCAST_ACTION = PACKAGE_NAME + ".BROADCAST_ACTION";

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;

    /**
     *  geofences expire after twelve hours.
     */
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;

    public static final float GEOFENCE_RADIUS_IN_METERS = 60; // 1 mile, 1.6 km

    public static final HashMap<String, LatLng> PanacloudLatLong = new HashMap<String, LatLng>();
    static {
        // OFFICE
        PanacloudLatLong.put("BCast PANACLOUD ", new LatLng(24.813486, 67.048381));

        // HOME
        PanacloudLatLong.put("BCast HOME ", new LatLng(24.923880,67001465));

        //HOTEL
        PanacloudLatLong.put("BCast Hotel ", new LatLng(24.920905,66.992984));

    }

}
