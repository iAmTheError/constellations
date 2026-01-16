package com.monish.constellation.constellation_graph.service.sky.astronomy;

import java.time.Instant;

public class TwilightService {
    private final AstronomyFacade astronomy;

    private static final double CIVIL_TWILIGHT_ANGLE_DEG = -6.0;

    public TwilightService(AstronomyFacade astronomy){
        this.astronomy = astronomy;
    }

    public boolean isDarkEnough(double latDeg, double lonDeg, Instant timeUtc){
        return astronomy.isDark(latDeg, lonDeg, timeUtc, CIVIL_TWILIGHT_ANGLE_DEG);
    }
}
