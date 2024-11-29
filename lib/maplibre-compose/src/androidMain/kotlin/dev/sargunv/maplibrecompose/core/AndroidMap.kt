package dev.sargunv.maplibrecompose.core

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.*
import co.touchlab.kermit.Logger
import dev.sargunv.maplibrecompose.core.camera.CameraPosition
import dev.sargunv.maplibrecompose.core.data.GestureSettings
import dev.sargunv.maplibrecompose.core.data.OrnamentSettings
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.util.correctedAndroidUri
import dev.sargunv.maplibrecompose.core.util.toGravity
import dev.sargunv.maplibrecompose.core.util.toLatLng
import dev.sargunv.maplibrecompose.core.util.toMLNExpression
import dev.sargunv.maplibrecompose.core.util.toOffset
import dev.sargunv.maplibrecompose.core.util.toPointF
import dev.sargunv.maplibrecompose.core.util.toPosition
import dev.sargunv.maplibrecompose.core.util.toRectF
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Position
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration
import kotlin.time.DurationUnit
import org.maplibre.android.camera.CameraPosition as MLNCameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.maps.MapLibreMap as MLNMap
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style as MlnStyle

internal class AndroidMap(
  private val mapView: MapView,
  private val map: MapLibreMap,
  internal var layoutDir: LayoutDirection,
  internal var density: Density,
  internal var callbacks: MaplibreMap.Callbacks,
  internal var logger: Logger?,
  styleUrl: String,
) : MaplibreMap {

  override var styleUrl: String = ""
    set(value) {
      if (field == value) return
      logger?.i { "Setting style URL" }
      callbacks.onStyleChanged(this, null)
      val builder = MlnStyle.Builder().fromUri(value.correctedAndroidUri().toString())
      map.setStyle(builder) {
        logger?.i { "Style finished loading" }
        callbacks.onStyleChanged(this, AndroidStyle(it))
      }
      field = value
    }

  init {
    map.addOnCameraMoveListener { callbacks.onCameraMove(this) }

    map.addOnMapClickListener { coords ->
      val pos = coords.toPosition()
      callbacks.onClick(this, pos, screenLocationFromPosition(pos))
      true
    }

    map.addOnMapLongClickListener { coords ->
      val pos = coords.toPosition()
      callbacks.onClick(this, pos, screenLocationFromPosition(pos))
      true
    }

    this.styleUrl = styleUrl
  }

  override var isDebugEnabled
    get() = map.isDebugActive
    set(value) {
      map.isDebugActive = value
    }

  override fun setMaximumFps(maximumFps: Int) = mapView.setMaximumFps(maximumFps)

  override fun setGestureSettings(value: GestureSettings) {
    map.uiSettings.isRotateGesturesEnabled = value.isRotateGesturesEnabled
    map.uiSettings.isScrollGesturesEnabled = value.isScrollGesturesEnabled
    map.uiSettings.isTiltGesturesEnabled = value.isTiltGesturesEnabled
    map.uiSettings.isZoomGesturesEnabled = value.isZoomGesturesEnabled
  }

  override fun setOrnamentSettings(value: OrnamentSettings) {
    map.uiSettings.isLogoEnabled = value.isLogoEnabled
    map.uiSettings.logoGravity = value.logoAlignment.toGravity(layoutDir)

    map.uiSettings.isAttributionEnabled = value.isAttributionEnabled
    map.uiSettings.attributionGravity = value.attributionAlignment.toGravity(layoutDir)

    map.uiSettings.isCompassEnabled = value.isCompassEnabled
    map.uiSettings.compassGravity = value.compassAlignment.toGravity(layoutDir)

    with(density) {
      val left =
        (value.padding.calculateLeftPadding(layoutDir).coerceAtLeast(0.dp) + 8.dp).roundToPx()
      val top = (value.padding.calculateTopPadding().coerceAtLeast(0.dp) + 8.dp).roundToPx()
      val right =
        (value.padding.calculateRightPadding(layoutDir).coerceAtLeast(0.dp) + 8.dp).roundToPx()
      val bottom = (value.padding.calculateBottomPadding().coerceAtLeast(0.dp) + 8.dp).roundToPx()
      map.uiSettings.setAttributionMargins(left, top, right, bottom)
      map.uiSettings.setLogoMargins(left, top, right, bottom)
      map.uiSettings.setCompassMargins(left, top, right, bottom)
    }
  }

  private fun MLNCameraPosition.toCameraPosition(): CameraPosition =
    CameraPosition(
      target = target?.toPosition() ?: Position(0.0, 0.0),
      zoom = zoom,
      bearing = bearing,
      tilt = tilt,
      padding =
        padding?.let {
          PaddingValues.Absolute(
            left = it[0].dp,
            top = it[1].dp,
            right = it[2].dp,
            bottom = it[3].dp,
          )
        } ?: PaddingValues.Absolute(0.dp),
    )

  private fun CameraPosition.toMLNCameraPosition(): MLNCameraPosition =
    with(density) {
      MLNCameraPosition.Builder()
        .target(target.toLatLng())
        .zoom(zoom)
        .tilt(tilt)
        .bearing(bearing)
        .padding(
          left = padding.calculateLeftPadding(layoutDir).toPx().toDouble(),
          top = padding.calculateTopPadding().toPx().toDouble(),
          right = padding.calculateRightPadding(layoutDir).toPx().toDouble(),
          bottom = padding.calculateBottomPadding().toPx().toDouble(),
        )
        .build()
    }

  override var cameraPosition: CameraPosition
    get() = map.cameraPosition.toCameraPosition()
    set(value) {
      map.moveCamera(CameraUpdateFactory.newCameraPosition(value.toMLNCameraPosition()))
    }

  override suspend fun animateCameraPosition(finalPosition: CameraPosition, duration: Duration) =
    suspendCoroutine { cont ->
      map.animateCamera(
        CameraUpdateFactory.newCameraPosition(finalPosition.toMLNCameraPosition()),
        duration.toInt(DurationUnit.MILLISECONDS),
        object : MLNMap.CancelableCallback {
          override fun onFinish() = cont.resume(Unit)

          override fun onCancel() = cont.resume(Unit)
        },
      )
    }

  override fun positionFromScreenLocation(offset: DpOffset): Position =
    map.projection.fromScreenLocation(offset.toPointF(density)).toPosition()

  override fun screenLocationFromPosition(position: Position): DpOffset =
    map.projection.toScreenLocation(position.toLatLng()).toOffset(density)

  override fun queryRenderedFeatures(offset: DpOffset): List<Feature> {
    return map.queryRenderedFeatures(offset.toPointF(density)).map { Feature.fromJson(it.toJson()) }
  }

  override fun queryRenderedFeatures(offset: DpOffset, layerIds: Set<String>): List<Feature> {
    return map.queryRenderedFeatures(offset.toPointF(density), *layerIds.toTypedArray()).map {
      Feature.fromJson(it.toJson())
    }
  }

  override fun queryRenderedFeatures(
    offset: DpOffset,
    layerIds: Set<String>,
    predicate: Expression<Boolean>,
  ): List<Feature> {
    return map
      .queryRenderedFeatures(
        offset.toPointF(density),
        predicate.toMLNExpression(),
        *layerIds.toTypedArray(),
      )
      .map { Feature.fromJson(it.toJson()) }
  }

  override fun queryRenderedFeatures(rect: DpRect): List<Feature> {
    return map.queryRenderedFeatures(rect.toRectF(density)).map { Feature.fromJson(it.toJson()) }
  }

  override fun queryRenderedFeatures(rect: DpRect, layerIds: Set<String>): List<Feature> {
    return map.queryRenderedFeatures(rect.toRectF(density), *layerIds.toTypedArray()).map {
      Feature.fromJson(it.toJson())
    }
  }

  override fun queryRenderedFeatures(
    rect: DpRect,
    layerIds: Set<String>,
    predicate: Expression<Boolean>,
  ): List<Feature> {
    return map
      .queryRenderedFeatures(
        rect.toRectF(density),
        predicate.toMLNExpression(),
        *layerIds.toTypedArray(),
      )
      .map { Feature.fromJson(it.toJson()) }
  }
}
