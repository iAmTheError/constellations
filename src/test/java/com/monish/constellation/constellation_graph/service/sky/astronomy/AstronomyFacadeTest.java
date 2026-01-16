package com.monish.constellation.constellation_graph.service.sky.astronomy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;
public class AstronomyFacadeTest {
    private final AstronomyFacade astronomy = new AstronomyFacade();
    
    @Test
    void sunAltIsPositiveAtNoonEquator(){
        Instant timeUtc = Instant.parse("2025-01-01T12:00:00Z"); // around equinox
        double altDeg = astronomy.sunAltitudeDeg(0.0, 0.0, timeUtc);
        assertTrue(altDeg > 30.0, "Sun altitude should be positive and high at noon on equator");
    }

    @Test
    void sunAltIsNegativeAtMidnight(){
        Instant timeUtc = Instant.parse("2025-01-01T00:00:00Z");
         double altDeg = astronomy.sunAltitudeDeg(0.0, 0.0, timeUtc);
        assertTrue(altDeg < 0.0, "Sun altitude should be negative at midnight");
    }

    @Test
    void raDecConversionReturnsValuesInBound(){
        double raDeg = 101.287;   // degrees
        double decDeg = -16.716;
        double latDeg = -27.47;   // Brisbane-ish
        double lonDeg = 153.02;
        Instant timeUtc = Instant.parse("2025-01-01T12:00:00Z");
        AstronomyFacade.Horizontal horiz = astronomy.raDecToAltAz(
                raDeg, decDeg, // RA/Dec
                latDeg, lonDeg,   // lat/lon
                timeUtc
        );
        assertTrue(horiz.altDeg() >= -90.0 && horiz.altDeg() <= 90.0, "Altitude should be within -90 to +90 degrees");
        assertTrue(horiz.azDeg() >= 0.0 && horiz.azDeg() < 360.0, "Azimuth should be within 0 to <360 degrees");
    }
}