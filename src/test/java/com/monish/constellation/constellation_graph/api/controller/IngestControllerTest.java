package com.monish.constellation.constellation_graph.api.controller;

import com.monish.constellation.constellation_graph.ingest.gaia.GaiaIngestService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;

import jakarta.annotation.Resource;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IngestController.class)
public class IngestControllerTest {
    @Resource 
    MockMvc mvc;

    @MockitoBean
    GaiaIngestService ingestService;

    @Test
    void ingestEndpointReturnsJson() throws Exception{
        when(ingestService.ingestBrightestStars(6.0,2000)).thenReturn(new GaiaIngestService.IngestResult(150,150));
    mvc.perform(post("/admin/ingest/bright-stars"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.parsed").value(150))
        .andExpect(jsonPath("$.saved").value(150));
    }
}
