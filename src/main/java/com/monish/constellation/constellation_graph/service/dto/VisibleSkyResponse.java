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
    double limitingMagnitude,
    List<VisibleStar> stars
)
{
    public record VisibleStar(
        long sourceId,
        double raDeg,
        double decDeg,
        double gmag,
        double altDeg,
        double azdeg,
        double effectiveMag
    ){}
}