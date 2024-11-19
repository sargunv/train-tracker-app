package dev.sargunv.maplibrekmp.core

import android.graphics.PointF
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import dev.sargunv.maplibrekmp.core.camera.CameraPosition
import dev.sargunv.maplibrekmp.core.data.GestureSettings
import dev.sargunv.maplibrekmp.core.data.OrnamentSettings
import dev.sargunv.maplibrekmp.core.data.XY
import dev.sargunv.maplibrekmp.core.util.correctedAndroidUri
import dev.sargunv.maplibrekmp.core.util.toGravity
import dev.sargunv.maplibrekmp.core.util.toLatLng
import dev.sargunv.maplibrekmp.core.util.toPointF
import dev.sargunv.maplibrekmp.core.util.toPosition
import dev.sargunv.maplibrekmp.core.util.toXY
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Position
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.maps.MapLibreMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration
import kotlin.time.DurationUnit
import org.maplibre.android.camera.CameraPosition as MLNCameraPosition
import org.maplibre.android.maps.MapLibreMap as MLNMap
import org.maplibre.android.maps.Style as MlnStyle

internal class AndroidMap(
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
      val left = value.padding.calculateLeftPadding(layoutDir).roundToPx()
      val top = value.padding.calculateTopPadding().roundToPx()
      val right = value.padding.calculateRightPadding(layoutDir).roundToPx()
      val bottom = value.padding.calculateBottomPadding().roundToPx()
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

  override fun positionFromScreenLocation(xy: XY): Position =
    map.projection.fromScreenLocation(PointF(xy.x, xy.y)).toPosition()

  override fun screenLocationFromPosition(position: Position): XY =
    map.projection.toScreenLocation(position.toLatLng()).toXY()

  override fun queryRenderedFeatures(xy: XY, layerIds: Set<String>): List<Feature> =
    map.queryRenderedFeatures(xy.toPointF(), *layerIds.toTypedArray()).map {
      Feature.fromJson(it.toJson())
    }
}
