package com.monish.constellation.constellation_graph.domain.node;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Id;

@Node
public class Star {
    @Id
    private final Long sourceId;

    private final double ra;
    private final double dec;
    private final double gmag;

    public Star(Long sourceId, double ra, double dec, double gmag){
        this.sourceId = sourceId;
        this.ra = ra;
        this.dec = dec;
        this.gmag = gmag;
    }

    public Long getSourceId() {
        return sourceId;
    }
    public double getRa() {
        return ra;
    }
    public double getDec() {
        return dec;
    }
    public double getGmag() {
        return gmag;
    }
}
