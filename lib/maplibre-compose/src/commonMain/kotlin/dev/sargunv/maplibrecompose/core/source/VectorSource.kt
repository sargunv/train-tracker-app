package dev.sargunv.maplibrecompose.core.source

/**
 *  A map data source of tiled vector data.
 * */
public expect class VectorSource(id: String, configUrl: String) : Source
