package com.monish.constellation.constellation_graph.service.sky;

import com.monish.constellation.constellation_graph.domain.node.*;
import com.monish.constellation.constellation_graph.repository.StarRepository;
import com.monish.constellation.constellation_graph.service.sky.astronomy.TwilightService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;      

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SkyVisibilityServiceTest {
    @Test
    void returnsVisibleStarsAtNight(){
        TwilightService twilight = mock(TwilightService.class);
        StarRepository repo = mock(StarRepository.class);
        HorizontalCoordsService horizontal = mock(HorizontalCoordsService.class);
        VisibilityFilter filter = mock(VisibilityFilter.class);
        
        Star star = new Star(1L, 101.287, -16.716, 2.0);
        when(repo.findCandidateStars(anyDouble(),anyInt())).thenReturn(List.of(star));

        when(twilight.isDarkEnough(anyDouble(),anyDouble(),any())).thenReturn(true);

        SkyVisibilityService svc = new SkyVisibilityService(twilight, repo, horizontal, filter);
        
        var resp = svc.visible(
                -27.47,
                153.02,
                6,
                15.0,
                Instant.parse("2025-01-01T12:00:00Z")
        );

        assertTrue(resp.isNight());
        assertFalse(resp.stars().isEmpty());

    }
}
