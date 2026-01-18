package com.monish.constellation.constellation_graph.service.sky;

import org.junit.jupiter.api.Test;

import com.monish.constellation.constellation_graph.service.sky.VisibilityFilter.VisibilityResult;

import static org.junit.jupiter.api.Assertions.*;

class VisibilityFilterTest{

    private final VisibilityFilter filter  = new VisibilityFilter();

    @Test
    void starBelowMinAltIsNotVisible(){
        var r = filter.evaluate(5.0,5.0,10.0,6.0);
        assertFalse(r.isVisible());
    }

    @Test
    void brightHighStarsVisible(){
        VisibilityResult r = filter.evaluate(3.0,60.0,10.0,5.0);
        assertTrue(r.isVisible());
        assertTrue(r.effectiveMag() > 3.0);
    }

    @Test
    void faintStarisRejected(){
        var r = filter.evaluate(7.0,60.0,10.0,5.0);
        assertFalse(r.isVisible());
    }
}

