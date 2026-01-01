package com.monish.constellation.constellation_graph.ingest.gaia;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class GaiaTapClient{
    private static final String GAIA_TAP_URL = "https://mast.stsci.edu/vo-tap/api/v0.1/gaiadr3/sync";

    private final RestClient restClient;

    public GaiaTapClient(RestClient.Builder restClient){
        this.restClient = restClient.build();
    }
    public InputStream queryBrightStarsVOTable(double maxGmag, int limit){
                String adql = """
                SELECT source_id, ra, dec, phot_g_mean_mag
                FROM gaia_source
                WHERE phot_g_mean_mag <= %s
                ORDER BY phot_g_mean_mag ASC
                """.formatted(maxGmag);

                if(limit>0){
                    adql.replaceFirst("SELECT","SELECT TOP "+limit);
                }

                String body = "REQUEST=doQuery"+
                        "&LANG=ADQL"+
                        "&FORMAT=votable"+
                        "&QUERY="+ URLEncoder.encode(adql, StandardCharsets.UTF_8);

                InputStream responseStream = restClient.post()
                        .uri(GAIA_TAP_URL)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_XML)
                        .body(body)
                        .retrieve()
                        .body(InputStream.class);
    }
}