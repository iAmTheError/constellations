package com.monish.constellation.constellation_graph.service.sky;

import org.springframework.stereotype.Service;

@Service
public class VisibilityFilter { 
    private static final double EXTINCTION_COEFFICIENT = 0.2; 

    public record VisibilityResult(boolean isVisible, double effectiveMag){
        static VisibilityResult notVisible(){
            return new VisibilityResult(false, Double.NaN);
        }
    }

    public VisibilityResult evaluate(
        double gMag,
        double altDeg,
        double minAltDeg,
        double limitingMag
    ){
        if(altDeg < minAltDeg){
            return VisibilityResult.notVisible();
        }

        double zenithAngleRad = Math.toRadians(90.0 - altDeg);
        double airmass = 1.0 / Math.cos(zenithAngleRad);

        airmass = Math.min(5.0,airmass);

        double extinction = EXTINCTION_COEFFICIENT * (airmass - 1.0);
        double effectiveMag = gMag + extinction;

        boolean Visible = effectiveMag <= limitingMag;

        return new VisibilityResult(Visible, effectiveMag);
    }
}
