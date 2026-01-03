package com.monish.constellation.constellation_graph.api.controller;

import com.monish.constellation.constellation_graph.ingest.gaia.GaiaTapClient;
import com.monish.constellation.constellation_graph.ingest.gaia.VOTableParser;
import com.monish.constellation.constellation_graph.ingest.model.StarRow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GaiaSmokeController {
    private final GaiaTapClient client;
    private final VOTableParser parser= new VOTableParser();

    public GaiaSmokeController(GaiaTapClient client){
        this.client = client;
    }

    @GetMapping("/admin/gaia-smoke")
    public List<StarRow> smoke() throws XMLStreamException{
        try{
            InputStream in = client.queryBrightStarsVOTable(3.0,10);
            List<StarRow> stars = new ArrayList<>();
            for(StarRow row : parser.parse(in)){
                stars.add(row);
            }
            return stars;
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
