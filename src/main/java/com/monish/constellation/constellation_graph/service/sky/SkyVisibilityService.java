package com.monish.constellation.constellation_graph.service.sky;

import org.springframework.stereotype.Service;
import com.monish.constellation.constellation_graph.service.dto.VisibleSkyResponse;

import java.time.Instant;
import java.util.List;

@Service
public class SkyVisibilityService {
    public VisibleSkyResponse visible(double lat, double lon, int bortle, double minAltDeg, Instant timeUtc){
        boolean isNight = true;
        double limitingMag = 6.0;

        return new VisibleSkyResponse(
            lat,
            lon,
            bortle,
            minAltDeg,
            timeUtc,
            isNight,
            limitingMag,
            List.of()
        );
    }
}
