package com.monish.constellation.constellation_graph.service.sky;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BortleScaleTest {
    @Test
    void bortle1IsDarkerThanBortle9(){
        assertTrue(BortleScale.limitingMagnitude(1)>BortleScale.limitingMagnitude(9));
    }

    @Test
    void rejectsOutOfRange(){
        assertThrows(IllegalArgumentException.class, () -> BortleScale.limitingMagnitude(0));
        assertThrows(IllegalArgumentException.class, () -> BortleScale.limitingMagnitude(10));
    }

    @Test
    void candidateMarginIsPositive(){
        assertTrue(BortleScale.candidateMargin()>0);
    }

    @Test
    void limitingMagnitudesDecreaseMonotonically(){
        double previous = Double.MAX_VALUE;
        for(int bortle=1;bortle<=9;bortle++){
            double current = BortleScale.limitingMagnitude(bortle);
            assertTrue(current < previous, "Bortle "+bortle+" limiting magnitude "+current+" is not less than previous "+previous);
            previous = current;
        }
    }
}
