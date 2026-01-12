package com.monish.constellation.constellation_graph.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import com.monish.constellation.constellation_graph.domain.node.Star;

public interface StarRepository extends Neo4jRepository<Star, Long> {}

