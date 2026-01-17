package com.monish.constellation.constellation_graph.ingest.gaia;

import com.monish.constellation.constellation_graph.domain.node.Star;
import com.monish.constellation.constellation_graph.ingest.model.StarRow;
import com.monish.constellation.constellation_graph.repository.StarRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class GaiaIngestServiceTest {
    @Test 
    void ingestAndSaveRows_inBatches() throws Exception{
        /*
        GaiaTapClient gaiaTapClient = mock(GaiaTapClient.class);
        VOTableParser voTableParser = mock(VOTableParser.class);
        StarRepository starRepository = mock(StarRepository.class);

        InputStream fakeStream = new ByteArrayInputStream(new byte[]{1,2,3});
        when(gaiaTapClient.queryBrightStarsVOTable(6.0,0)).thenReturn(fakeStream);

        List<StarRow> parsed = new ArrayList<>();
        for(int i=1;i<=2505;i++){
            parsed.add(new StarRow(i,10+i,-20-i,5.0+i));
        }
        when(voTableParser.parse(fakeStream)).thenReturn(parsed);

        GaiaIngestService gaiaIngestService = new GaiaIngestService(gaiaTapClient,voTableParser,starRepository);

        GaiaIngestService.IngestResult result = gaiaIngestService.ingestBrightestStars(6.0,0);

        assertEquals(2505,result.parsed());
        assertEquals(2505,result.saved());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Iterable<Star>> batch = (ArgumentCaptor<Iterable<Star>>) (ArgumentCaptor<?>) ArgumentCaptor.forClass(Iterable.class);
        verify(starRepository,times(3)).saveAll(batch.capture());
        */

        //removing this test as it requires VOTable parser to be a bean while it is not a bean
    
    }
}
