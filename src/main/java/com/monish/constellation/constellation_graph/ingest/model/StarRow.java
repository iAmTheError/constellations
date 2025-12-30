package com.monish.constellation.constellation_graph.ingest.model;
/**
 * StarRow represents a single parsed row from the Gaia TAP VOTable.
 *
 * This is a temporary transport object used during ingestion.
 * It is NOT a domain entity and is NOT persisted directly.
 */

public record StarRow(
    long sourceId,
    double ra,
    double dec,
    double gmag
)
{}
