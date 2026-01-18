package com.monish.constellation.constellation_graph.service.sky;

import org.springframework.stereotype.Service;
import com.monish.constellation.constellation_graph.domain.node.Star;
import com.monish.constellation.constellation_graph.repository.StarRepository;
import com.monish.constellation.constellation_graph.service.dto.VisibleSkyResponse.VisibleStar;
import com.monish.constellation.constellation_graph.service.sky.astronomy.TwilightService;
import com.monish.constellation.constellation_graph.service.dto.VisibleSkyResponse;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class SkyVisibilityService {
    private static final int STAR_QUERY_LIMIT = 1000;

    private final StarRepository starRepository;
    private final HorizontalCoordsService horizontalCoordsService;
    private final TwilightService twilightService;
    private final VisibilityFilter visibilityFilter;

    public SkyVisibilityService(
        TwilightService twilightService,
        StarRepository starRepository,
        HorizontalCoordsService horizontalCoordsService,
        VisibilityFilter visibilityFilter
    ){
        this.twilightService = twilightService;
        this.starRepository = starRepository;
        this.horizontalCoordsService = horizontalCoordsService;
        this.visibilityFilter = visibilityFilter;
    }

    public VisibleSkyResponse visible(double lat, double lon, int bortle, double minAltDeg, Instant timeUtc){
        boolean isNight = twilightService.isDarkEnough(minAltDeg, lon, timeUtc);    
        double limitingMag = BortleScale.limitingMagnitude(bortle) + BortleScale.candidateMargin();

        if(!isNight){
            return new VisibleSkyResponse(
                lat,
                lon,
                bortle,
                minAltDeg,
                timeUtc,
                false,
                limitingMag,
                List.of()
            );
        }

        List<Star> candidates = starRepository.findCandidateStars(limitingMag + 1.0, STAR_QUERY_LIMIT);
        List<VisibleStar> visibleStars = new ArrayList<>();

        for(Star star : candidates){
            var horizontal = horizontalCoordsService.Horizontal(
                star.getRa(),
                star.getDec(),
                lat,
                lon,
                timeUtc
            );

            var visibility = visibilityFilter.evaluate(star.getGmag(), horizontal.altDeg(), minAltDeg, limitingMag);

            if(!visibility.isVisible()){
                continue;
            }

            visibleStars.add(new VisibleStar(
                star.getSourceId(),
                star.getRa(),
                star.getDec(),
                star.getGmag(),
                horizontal.altDeg(),
                horizontal.azDeg(),
                visibility.effectiveMag()
            ));
        }
        return new VisibleSkyResponse(
            lat,
            lon,
            bortle,
            minAltDeg,
            timeUtc,
            true,
            limitingMag,
            visibleStars 
        );
    }
}
