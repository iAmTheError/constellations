package com.monish.constellation.constellation_graph.ingest.gaia;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

@Component
public class GaiaTapClient{
    private static final String GAIA_TAP_URL = "https://mast.stsci.edu/vo-tap/api/v0.1/gaiadr3/sync";

    private final RestClient restClient;

    public GaiaTapClient(RestClient.Builder restClient){
        this.restClient = restClient.build();
    }
    public InputStream queryBrightStarsVOTable(double maxGmag, int limit){
                String adql = """
                SELECT TOP %s source_id, ra, dec, phot_g_mean_mag
                FROM gaia_source
                WHERE phot_g_mean_mag <= %s
                ORDER BY phot_g_mean_mag ASC
                """.formatted(limit, maxGmag);

                //did not work
                //if(limit>0){
                  //  adql.replaceFirst("SELECT","SELECT TOP "+limit);
                //}

                MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
                body.add("REQUEST", "doQuery");
                body.add("LANG", "ADQL");
                body.add("FORMAT", "votable");
                body.add("QUERY", adql);

                byte[] bytes =restClient.post()
                .uri(GAIA_TAP_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_XML, MediaType.TEXT_XML)
                .body(body)
                .retrieve()
                .body(byte[].class);

        return new ByteArrayInputStream(bytes);
    }
}
            