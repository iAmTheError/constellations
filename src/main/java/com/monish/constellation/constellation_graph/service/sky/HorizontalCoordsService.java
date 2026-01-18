package com.monish.constellation.constellation_graph.service.sky;
import com.monish.constellation.constellation_graph.service.sky.astronomy.*;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class HorizontalCoordsService {
    private final AstronomyFacade astronomyFacade;

    public HorizontalCoordsService(AstronomyFacade astronomyFacade){
        this.astronomyFacade = astronomyFacade;
    }

    public AstronomyFacade.Horizontal Horizontal(
        double raDeg,
        double decDeg,
        double latDeg,
        double lonDeg,
        Instant timeUtc
    ){
        return astronomyFacade.raDecToAltAz(raDeg, decDeg, latDeg, lonDeg, timeUtc);
    }
}
