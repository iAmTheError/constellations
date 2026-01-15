package com.monish.constellation.constellation_graph.service.sky.astronomy;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class sunPositionService {

    /**
     * Returns the Sun altitude in degrees for a given observer and UTC time.
     * Positive = above horizon, negative = below horizon.
     *
     * Based on NOAA Solar Position Algorithm.
     */
    public static double sunAltitudeDeg(double latDeg, double lonDeg, Instant timeUtc) {

        ZonedDateTime utc = timeUtc.atZone(ZoneOffset.UTC);

        int dayOfYear = utc.getDayOfYear();
        double hour = utc.getHour()
                + utc.getMinute() / 60.0
                + utc.getSecond() / 3600.0;

        // Fractional year (radians)
        double gamma = 2.0 * Math.PI / 365.0 * (dayOfYear - 1 + (hour - 12) / 24.0);

        // Solar declination (radians)
        double decl = 0.006918
                - 0.399912 * Math.cos(gamma)
                + 0.070257 * Math.sin(gamma)
                - 0.006758 * Math.cos(2 * gamma)
                + 0.000907 * Math.sin(2 * gamma)
                - 0.002697 * Math.cos(3 * gamma)
                + 0.00148  * Math.sin(3 * gamma);

        // Equation of time (minutes)
        double eqTime = 229.18 * (
                0.000075
                + 0.001868 * Math.cos(gamma)
                - 0.032077 * Math.sin(gamma)
                - 0.014615 * Math.cos(2 * gamma)
                - 0.040849 * Math.sin(2 * gamma)
        );

        // True solar time (minutes)
        double timeOffset = eqTime + 4 * lonDeg;
        double trueSolarTime = (hour * 60 + timeOffset) % 1440;

        // Hour angle (degrees)
        double hourAngleDeg = (trueSolarTime / 4.0 < 0)
                ? trueSolarTime / 4.0 + 180
                : trueSolarTime / 4.0 - 180;

        double hourAngleRad = Math.toRadians(hourAngleDeg);

        // Convert latitude to radians
        double latRad = Math.toRadians(latDeg);

        // Solar zenith angle (radians)
        double cosZenith =
                Math.sin(latRad) * Math.sin(decl)
              + Math.cos(latRad) * Math.cos(decl) * Math.cos(hourAngleRad);

        // Clamp for numerical safety
        cosZenith = Math.max(-1.0, Math.min(1.0, cosZenith));

        double zenithRad = Math.acos(cosZenith);

        // Altitude = 90° - zenith
        return 90.0 - Math.toDegrees(zenithRad);
    }

    // --- Convenience helpers ---

    public static boolean isCivilNight(double lat, double lon, Instant t) {
        return sunAltitudeDeg(lat, lon, t) < -6.0;
    }

    public static boolean isNauticalNight(double lat, double lon, Instant t) {
        return sunAltitudeDeg(lat, lon, t) < -12.0;
    }

    public static boolean isAstronomicalNight(double lat, double lon, Instant t) {
        return sunAltitudeDeg(lat, lon, t) < -18.0;
    }
}
