package com.monish.constellation.constellation_graph.service.sky.astronomy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;


public class TwilightServiceTest {
    AstronomyFacade astronomy = new AstronomyFacade();
    TwilightService twilightService = new TwilightService(astronomy);
    
    @Test
    void returnsFalseAtDay(){
        Instant dayTime = Instant.parse("2025-06-21T12:00:00Z"); // noon UTC
        boolean isDark = twilightService.isDarkEnough(0.0, 0.0, dayTime); // equator
        assertFalse(isDark, "Should not be dark enough at noon on equator");
    }

    @Test
    void returnsTrueAtNight(){
        Instant nightTime = Instant.parse("2025-06-21T00:00:00Z"); // midnight UTC
        boolean isDark = twilightService.isDarkEnough(0.0, 0.0, nightTime); // equator
        assertTrue(isDark, "Should be dark enough at midnight on equator");
    }
}
