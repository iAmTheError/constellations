package com.monish.constellation.constellation_graph.api.controller;

import com.monish.constellation.constellation_graph.ingest.gaia.GaiaIngestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class IngestController {
    private final GaiaIngestService gaiaIngestService;

    public IngestController(GaiaIngestService gaiaIngestService) {
        this.gaiaIngestService = gaiaIngestService;
    }

    @PostMapping("/ingest/bright-stars")
    public GaiaIngestService.IngestResult ingestBrightStars(@RequestParam(defaultValue = "6.0") double maxGmag, @RequestParam(defaultValue = "100") int limit) throws Exception {
        var result = gaiaIngestService.ingestBrightestStars(maxGmag, limit);
        return result;
    }
}