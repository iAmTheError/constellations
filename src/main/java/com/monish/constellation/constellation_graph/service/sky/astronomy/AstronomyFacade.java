package com.monish.constellation.constellation_graph.service.sky.astronomy;

import net.e175.klaus.solarpositioning.DeltaT;
import net.e175.klaus.solarpositioning.SPA;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class AstronomyFacade {

    public record Horizontal(double azDeg, double altDeg) {}

    /**
     * Sun altitude at observer location/time (degrees).
     * Positive = above horizon, negative = below horizon.
     */
    public double sunAltitudeDeg(double latDeg, double lonDeg, Instant timeUtc) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(timeUtc, ZoneOffset.UTC);

        // elevation/pressure/temp can be constants for now; you can pass real values later
        double elevationM = 0;
        double pressureHpa = 1013.25;
        double tempC = 15;

        var pos = SPA.calculateSolarPosition(
                zdt,
                latDeg,
                lonDeg,
                elevationM,
                DeltaT.estimate(zdt.toLocalDate()),
                pressureHpa,
                tempC
        );

        // "elevation" from library is the altitude above horizon (in degrees)
        return 90.0 - pos.zenithAngle();
    }

    /**
     * Convert equatorial coords (RA/Dec) to horizontal (Az/Alt) for an observer.
     *
     * Inputs:
     *  - raDeg: Right ascension in DEGREES (if you have hours, multiply by 15)
     *  - decDeg: Declination in degrees
     *  - latDeg/lonDeg: observer geodetic lat/lon in degrees (lon east-positive)
     *  - timeUtc: instant in UTC
     */
    public Horizontal raDecToAltAz(double raDeg, double decDeg,
                                  double latDeg, double lonDeg,
                                  Instant timeUtc) {

        double lstDeg = localSiderealTimeDeg(lonDeg, timeUtc); // degrees
        double haDeg = normalizeDeg(lstDeg - raDeg);           // hour angle in degrees

        // Convert to radians for trig
        double ha = Math.toRadians(haDeg);
        double dec = Math.toRadians(decDeg);
        double lat = Math.toRadians(latDeg);

        // Altitude
        double sinAlt = Math.sin(dec) * Math.sin(lat) + Math.cos(dec) * Math.cos(lat) * Math.cos(ha);
        sinAlt = clamp(sinAlt, -1.0, 1.0);
        double alt = Math.asin(sinAlt);

        // Azimuth (measured from North, increasing towards East)
        double cosAz = (Math.sin(dec) - Math.sin(alt) * Math.sin(lat)) / (Math.cos(alt) * Math.cos(lat));
        cosAz = clamp(cosAz, -1.0, 1.0);
        double az = Math.acos(cosAz);

        // Determine quadrant using sin(HA)
        if (Math.sin(ha) > 0) {
            az = 2 * Math.PI - az;
        }

        return new Horizontal(Math.toDegrees(az), Math.toDegrees(alt));
    }

    /**
     * True if "night enough" for stargazing.
     * Suggested thresholds:
     *  - Civil twilight: sun alt <= -6°
     *  - Nautical: <= -12°
     *  - Astronomical: <= -18°
     */
    public boolean isDark(double latDeg, double lonDeg, Instant timeUtc, double thresholdSunAltDeg) {
        return sunAltitudeDeg(latDeg, lonDeg, timeUtc) <= thresholdSunAltDeg;
    }

    // -------------------------
    // Sidereal time helpers
    // -------------------------

    /**
     * Local Sidereal Time (degrees) using a standard approximation.
     * Good enough for constellation visibility filtering.
     */
    static double localSiderealTimeDeg(double lonDeg, Instant timeUtc) {
        // Convert to Julian Date
        double jd = toJulianDate(timeUtc);
        double d = jd - 2451545.0; // days since J2000.0

        // Greenwich Mean Sidereal Time in degrees (approx)
        double gmst = 280.46061837
                + 360.98564736629 * d;

        // Local sidereal time
        double lst = gmst + lonDeg; // lon east-positive
        return normalizeDeg(lst);
    }

    static double toJulianDate(Instant t) {
        // JD at Unix epoch = 2440587.5
        double seconds = t.getEpochSecond() + t.getNano() / 1e9;
        return 2440587.5 + seconds / 86400.0;
    }

    static double normalizeDeg(double deg) {
        double x = deg % 360.0;
        return (x < 0) ? x + 360.0 : x;
    }

    static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}
