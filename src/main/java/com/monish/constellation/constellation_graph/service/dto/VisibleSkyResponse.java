package com.monish.constellation.constellation_graph.service.dto;

import java.time.Instant;
import java.util.List;

public record VisibleSkyResponse(
    double lat,
    double lon,
    int bortle,
    double minAltDeg,
    Instant timeUtc,
    boolean isNight,
    double limitingMag,
    List<VisibleStar> stars
)
{
    public record VisibleStar(
        long sourceId,
        double raDeg,
        double decDeg,
        double gMag,
        double altDeg,
        double azDeg,
        double effectiveMag
    ){}
}