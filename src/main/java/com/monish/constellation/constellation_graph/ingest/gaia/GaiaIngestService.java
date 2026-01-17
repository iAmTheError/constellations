package com.monish.constellation.constellation_graph.ingest.gaia;

import com.monish.constellation.constellation_graph.repository.StarRepository;
import com.monish.constellation.constellation_graph.domain.node.Star;
import com.monish.constellation.constellation_graph.ingest.model.StarRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class GaiaIngestService {
    private final GaiaTapClient client;
    private final VOTableParser parser = new VOTableParser();
    private final StarRepository starRepository;

    public GaiaIngestService(GaiaTapClient client, StarRepository starRepository){
        this.client = client;
        this.starRepository = starRepository;
    }
   //this upper code  this works in production but not in tests, this is the original code but did not allow mocking of parser in tests,

//used the lower code for testing as it allowed mocking of parser, but fails while building application as Spring cannot find a suitable constructor
/* 
public class GaiaIngestService {
    private final GaiaTapClient client;
    private final VOTableParser parser;
    private final StarRepository starRepository;

    // ✅ Spring will use this (only needs beans it already has)
    public GaiaIngestService(GaiaTapClient client, StarRepository starRepository) {
        this(client, new VOTableParser(), starRepository);
    }

    // ✅ Extra constructor for unit tests (package-private is perfect)
    GaiaIngestService(GaiaTapClient client, VOTableParser parser, StarRepository starRepository) {
        this.client = client;
        this.parser = parser;
        this.starRepository = starRepository;
    }
        */
        

    public record IngestResult(int parsed, int saved){}

    public IngestResult ingestBrightestStars(double maxGmag, int limit) throws Exception{
        try(InputStream in = client.queryBrightStarsVOTable(maxGmag,limit)){
            int parsed = 0;
            int saved = 0;

            List<Star> batch = new ArrayList<>(1000);
            for(StarRow r : parser.parse(in)){
                parsed++;
                Star star = new Star(r.sourceId(), r.ra(), r.dec(), r.gmag());
                batch.add(star);

                if(batch.size() >=1000){
                    saved += saveBatch(batch);
                    batch.clear();
                }
            }
            if(!batch.isEmpty()){
                saved += saveBatch(batch);
            }
            return new IngestResult(parsed,saved);
        }
    }
    @Transactional
    protected int saveBatch(List<Star> batch){
        starRepository.saveAll(batch);
        return batch.size();
    }
}
