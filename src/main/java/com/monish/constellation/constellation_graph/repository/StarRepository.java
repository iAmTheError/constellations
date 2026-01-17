package com.monish.constellation.constellation_graph.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import com.monish.constellation.constellation_graph.domain.node.Star;
import org.springframework.data.neo4j.repository.query.Query;


public interface StarRepository extends Neo4jRepository<Star, Long> {
    @Query("""
    MATCH (s:Star)
    WHERE s.gmag <= $maxGmag
    RETURN s
    ORDER BY s.gmag ASC
    LIMIT $limit
    """)
    List<Star> findCandidateStars(double maxGmag, int limit);
}

