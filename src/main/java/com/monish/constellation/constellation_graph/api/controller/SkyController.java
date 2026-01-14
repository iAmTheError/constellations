package com.monish.constellation.constellation_graph.api.controller;

import com.monish.constellation.constellation_graph.service.sky.SkyVisibilityService;
import com.monish.constellation.constellation_graph.service.dto.VisibleSkyResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class SkyController {
    private final SkyVisibilityService skyVisibilityService;
    
    public SkyController(SkyVisibilityService skyVisibilityService){
        this.skyVisibilityService = skyVisibilityService;
    }

    @GetMapping("api/sky/visible")
    public VisibleSkyResponse visible(
        @RequestParam double lat,
        @RequestParam double lon,
        @RequestParam int bortle,
        @RequestParam(defaultValue = "15") double minAltDeg,
        @RequestParam(required = false) String time
    ){
        if(lat < -90 || lat > 90){
            throw new IllegalArgumentException("lat must be between -90 and 90");
        }
        if(lon < -180 || lon > 180){
            throw new IllegalArgumentException("lon must be within -180 and 180");
        }
        if(bortle < 1 || bortle > 9){
            throw new IllegalArgumentException("bortle must be between 1 and 9");
        }
        if(minAltDeg < 0 || minAltDeg >90){
            throw new IllegalArgumentException("minAltDeg must be between 0 and 90");
        }
        Instant timeUtc;
        if(time==null || time.isBlank()){
            timeUtc = Instant.now();
        }else{
            timeUtc = Instant.parse(time);
        }
        return skyVisibilityService.visible(lat,lon,bortle,minAltDeg,timeUtc);
    }
}
