package dev.sargunv.maplibrecompose.demoapp.demos

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.sargunv.maplibrecompose.compose.ClickResult
import dev.sargunv.maplibrecompose.compose.MaplibreMap
import dev.sargunv.maplibrecompose.compose.layer.CircleLayer
import dev.sargunv.maplibrecompose.compose.layer.SymbolLayer
import dev.sargunv.maplibrecompose.compose.rememberCameraState
import dev.sargunv.maplibrecompose.compose.source.rememberGeoJsonSource
import dev.sargunv.maplibrecompose.core.camera.CameraPosition
import dev.sargunv.maplibrecompose.core.source.GeoJsonOptions
import dev.sargunv.maplibrecompose.demoapp.DEFAULT_STYLE
import dev.sargunv.maplibrecompose.demoapp.generated.Res
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.Position
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.resources.ExperimentalResourceApi

private const val GBFS_FILE = "files/data/lime_seattle.gbfs.json"

private val SEATTLE = Position(latitude = 47.607, longitude = -122.342)

private val LIME_GREEN = Color(50, 205, 5)

@Composable
fun ClusteredPointsDemo() = Column {
  val cameraState =
    rememberCameraState(firstPosition = CameraPosition(target = SEATTLE, zoom = 10.0))

  val coroutineScope = rememberCoroutineScope()

  MaplibreMap(modifier = Modifier.weight(1f), styleUrl = DEFAULT_STYLE, cameraState = cameraState) {
    val gbfsData by rememberGbfsFeatureState(GBFS_FILE)

    val bikeSource =
      rememberGeoJsonSource(
        "bikes",
        gbfsData,
        GeoJsonOptions(cluster = true, clusterMaxZoom = 16, clusterRadius = 50),
      )

    CircleLayer(
      id = "clustered-bikes",
      source = bikeSource,
      filter = has(const("point_count")),
      color = const(LIME_GREEN),
      opacity = const(0.5f),
      radius =
        step(
          input = get(const("point_count")),
          fallback = const(15),
          25 to const(20),
          100 to const(30),
          500 to const(40),
          1000 to const(50),
          5000 to const(60),
        ),
      onClick = { features ->
        features.firstOrNull()?.geometry?.let {
          coroutineScope.launch {
            cameraState.animateTo(
              cameraState.position.copy(
                target = (it as Point).coordinates,
                zoom = (cameraState.position.zoom + 2).coerceAtMost(20.0),
              )
            )
          }
          ClickResult.Consume
        } ?: ClickResult.Pass
      },
    )

    SymbolLayer(
      id = "clustered-bikes-count",
      source = bikeSource,
      filter = has(const("point_count")),
      textField = format(get(const("point_count_abbreviated"))),
      textFont = literal(listOf(const("Noto Sans Regular"))),
      textColor = const(MaterialTheme.colorScheme.onBackground),
    )

    CircleLayer(
      id = "unclustered-bikes-shadow",
      source = bikeSource,
      filter = !has(const("point_count")),
      radius = const(13.0),
      color = const(Color.Black),
      blur = const(1.0),
      translate = point(0.0, 1.0),
    )

    CircleLayer(
      id = "unclustered-bikes",
      source = bikeSource,
      filter = !has(const("point_count")),
      color = const(LIME_GREEN),
      radius = const(7),
      strokeWidth = const(3),
      strokeColor = const(Color.White),
    )
  }
}

@Composable
private fun rememberGbfsFeatureState(gbfsFilePath: String): State<FeatureCollection> {
  val dataState = remember { mutableStateOf(FeatureCollection()) }
  LaunchedEffect(gbfsFilePath) {
    val response = readGbfsData(gbfsFilePath)
    dataState.value = response
  }
  return dataState
}

@OptIn(ExperimentalResourceApi::class)
private suspend fun readGbfsData(gbfsFilePath: String): FeatureCollection {
  val bodyString = Res.readBytes(gbfsFilePath).decodeToString()
  val body = Json.parseToJsonElement(bodyString).jsonObject
  val bikes = body["data"]!!.jsonObject["bikes"]!!.jsonArray.map { it.jsonObject }
  val features =
    bikes.map { bike ->
      Feature(
        id = bike["bike_id"]!!.jsonPrimitive.content,
        geometry =
          Point(
            Position(
              longitude = bike["lon"]!!.jsonPrimitive.double,
              latitude = bike["lat"]!!.jsonPrimitive.double,
            )
          ),
        properties =
          mapOf(
            "vehicle_type" to (bike["vehicle_type"] ?: JsonNull),
            "vehicle_type_id" to (bike["vehicle_type_id"] ?: JsonNull),
            "last_reported" to (bike["last_reported"] ?: JsonNull),
            "vehicle_range_meters" to (bike["vehicle_range_meters"] ?: JsonNull),
            "is_reserved" to (bike["is_reserved"] ?: JsonNull),
            "is_disabled" to (bike["is_disabled"] ?: JsonNull),
          ),
      )
    }
  return FeatureCollection(features)
}
