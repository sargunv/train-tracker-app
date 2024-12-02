package dev.sargunv.maplibrecompose.core

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import cocoapods.MapLibre.MLNAltitudeForZoomLevel
import cocoapods.MapLibre.MLNCameraChangeReason
import cocoapods.MapLibre.MLNCameraChangeReasonGestureOneFingerZoom
import cocoapods.MapLibre.MLNCameraChangeReasonGesturePan
import cocoapods.MapLibre.MLNCameraChangeReasonGesturePinch
import cocoapods.MapLibre.MLNCameraChangeReasonGestureRotate
import cocoapods.MapLibre.MLNCameraChangeReasonGestureTilt
import cocoapods.MapLibre.MLNCameraChangeReasonGestureZoomIn
import cocoapods.MapLibre.MLNCameraChangeReasonGestureZoomOut
import cocoapods.MapLibre.MLNCameraChangeReasonProgrammatic
import cocoapods.MapLibre.MLNFeatureProtocol
import cocoapods.MapLibre.MLNLoggingBlockHandler
import cocoapods.MapLibre.MLNLoggingConfiguration
import cocoapods.MapLibre.MLNLoggingLevel
import cocoapods.MapLibre.MLNLoggingLevelDebug
import cocoapods.MapLibre.MLNLoggingLevelError
import cocoapods.MapLibre.MLNLoggingLevelFault
import cocoapods.MapLibre.MLNLoggingLevelInfo
import cocoapods.MapLibre.MLNLoggingLevelVerbose
import cocoapods.MapLibre.MLNLoggingLevelWarning
import cocoapods.MapLibre.MLNMapCamera
import cocoapods.MapLibre.MLNMapDebugCollisionBoxesMask
import cocoapods.MapLibre.MLNMapDebugTileBoundariesMask
import cocoapods.MapLibre.MLNMapDebugTileInfoMask
import cocoapods.MapLibre.MLNMapDebugTimestampsMask
import cocoapods.MapLibre.MLNMapView
import cocoapods.MapLibre.MLNMapViewDelegateProtocol
import cocoapods.MapLibre.MLNOrnamentPosition
import cocoapods.MapLibre.MLNOrnamentPositionBottomLeft
import cocoapods.MapLibre.MLNOrnamentPositionBottomRight
import cocoapods.MapLibre.MLNOrnamentPositionTopLeft
import cocoapods.MapLibre.MLNOrnamentPositionTopRight
import cocoapods.MapLibre.MLNStyle
import cocoapods.MapLibre.MLNZoomLevelForAltitude
import cocoapods.MapLibre.allowsTilting
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.util.toBoundingBox
import dev.sargunv.maplibrecompose.core.util.toCGPoint
import dev.sargunv.maplibrecompose.core.util.toCGRect
import dev.sargunv.maplibrecompose.core.util.toCLLocationCoordinate2D
import dev.sargunv.maplibrecompose.core.util.toDpOffset
import dev.sargunv.maplibrecompose.core.util.toFeature
import dev.sargunv.maplibrecompose.core.util.toMLNOrnamentPosition
import dev.sargunv.maplibrecompose.core.util.toNSPredicate
import dev.sargunv.maplibrecompose.core.util.toPosition
import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Position
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.TimeSource
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGPoint
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGSize
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.UIKit.UIEdgeInsets
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UIGestureRecognizer
import platform.UIKit.UIGestureRecognizerStateBegan
import platform.UIKit.UIGestureRecognizerStateEnded
import platform.UIKit.UILongPressGestureRecognizer
import platform.UIKit.UITapGestureRecognizer
import platform.darwin.NSObject
import platform.darwin.NSUInteger
import platform.darwin.sel_registerName

internal class IosMap(
  private var mapView: MLNMapView,
  internal var size: CValue<CGSize>,
  internal var layoutDir: LayoutDirection,
  internal var density: Density,
  internal var insetPadding: PaddingValues,
  internal var callbacks: MaplibreMap.Callbacks,
  internal var logger: Logger?,
) : MaplibreMap {

  // hold strong references to things that the sdk keeps weak references to
  private val gestures = mutableListOf<Gesture<*>>()
  private val delegate: Delegate

  override var styleUrl: String = ""
    set(value) {
      if (field == value) return
      logger?.i { "Setting style URL" }
      callbacks.onStyleChanged(this, null)
      mapView.setStyleURL(NSURL(string = value))
      field = value
    }

  init {
    mapView.automaticallyAdjustsContentInset = false

    addGestures(
      Gesture(UITapGestureRecognizer()) {
        if (state != UIGestureRecognizerStateEnded) return@Gesture
        val point = locationInView(this@IosMap.mapView).toDpOffset()
        callbacks.onClick(this@IosMap, positionFromScreenLocation(point), point)
      },
      Gesture(UILongPressGestureRecognizer()) {
        if (state != UIGestureRecognizerStateBegan) return@Gesture
        val point = locationInView(this@IosMap.mapView).toDpOffset()
        callbacks.onLongClick(this@IosMap, positionFromScreenLocation(point), point)
      },
    )

    // delegate log level configuration to Kermit logger
    MLNLoggingConfiguration.sharedConfiguration.setHandler(LoggingBlockHandler(this))
    MLNLoggingConfiguration.sharedConfiguration.loggingLevel = MLNLoggingLevelVerbose

    delegate = Delegate(this)
    mapView.delegate = delegate
  }

  private class LoggingBlockHandler(private val map: IosMap) : MLNLoggingBlockHandler {
    override fun invoke(level: MLNLoggingLevel, path: String?, line: NSUInteger, message: String?) {
      when (level) {
        MLNLoggingLevelFault -> map.logger?.a { "$message" }
        MLNLoggingLevelError -> map.logger?.e { "$message" }
        MLNLoggingLevelWarning -> map.logger?.w { "$message" }
        MLNLoggingLevelInfo -> map.logger?.i { "$message" }
        MLNLoggingLevelDebug -> map.logger?.d { "$message" }
        MLNLoggingLevelVerbose -> map.logger?.v { "$message" }
        else -> error("Unexpected logging level: $level")
      }
    }
  }

  private class Delegate(private val map: IosMap) : NSObject(), MLNMapViewDelegateProtocol {

    val timeSource = TimeSource.Monotonic
    var lastFrameTime = timeSource.markNow()

    override fun mapViewWillStartLoadingMap(mapView: MLNMapView) {
      map.logger?.i { "Map will start loading" }
    }

    override fun mapViewDidFailLoadingMap(mapView: MLNMapView, withError: NSError) {
      map.logger?.e { "Map failed to load: $withError" }
    }

    override fun mapViewDidFinishLoadingMap(mapView: MLNMapView) {
      map.logger?.i { "Map finished loading" }
    }

    override fun mapView(mapView: MLNMapView, didFinishLoadingStyle: MLNStyle) {
      map.logger?.i { "Style finished loading" }
      map.callbacks.onStyleChanged(map, IosStyle(didFinishLoadingStyle))
    }

    private val anyGesture =
      (MLNCameraChangeReasonGestureOneFingerZoom or
        MLNCameraChangeReasonGesturePan or
        MLNCameraChangeReasonGesturePinch or
        MLNCameraChangeReasonGestureRotate or
        MLNCameraChangeReasonGestureTilt or
        MLNCameraChangeReasonGestureZoomIn or
        MLNCameraChangeReasonGestureZoomOut)

    @ObjCSignatureOverride
    override fun mapView(
      mapView: MLNMapView,
      regionWillChangeWithReason: MLNCameraChangeReason,
      animated: Boolean,
    ) {
      map.callbacks.onCameraMoveStarted(
        map,
        if (regionWillChangeWithReason and anyGesture != 0uL) {
          CameraMoveReason.GESTURE
        } else if (regionWillChangeWithReason and MLNCameraChangeReasonProgrammatic != 0uL) {
          CameraMoveReason.PROGRAMMATIC
        } else {
          map.logger?.w { "Unknown camera move reason: $regionWillChangeWithReason" }
          CameraMoveReason.UNKNOWN
        },
      )
    }

    override fun mapViewRegionIsChanging(mapView: MLNMapView) {
      map.callbacks.onCameraMoved(map)
    }

    @ObjCSignatureOverride
    override fun mapView(
      mapView: MLNMapView,
      regionDidChangeWithReason: MLNCameraChangeReason,
      animated: Boolean,
    ) {
      map.callbacks.onCameraMoveEnded(map)
    }

    override fun mapViewDidFinishRenderingFrame(mapView: MLNMapView, fullyRendered: Boolean) {
      val time = timeSource.markNow()
      val duration = time - lastFrameTime
      lastFrameTime = time
      map.onFpsChanged(1.0 / duration.toDouble(DurationUnit.SECONDS))
    }
  }

  internal class Gesture<T : UIGestureRecognizer>(
    val recognizer: T,
    val isCooperative: Boolean = true,
    private val action: T.() -> Unit,
  ) : NSObject() {
    init {
      recognizer.addTarget(target = this, action = sel_registerName(::handleGesture.name + ":"))
    }

    @OptIn(BetaInteropApi::class)
    @ObjCAction
    fun handleGesture(sender: UIGestureRecognizer) {
      @Suppress("UNCHECKED_CAST") action(sender as T)
    }
  }

  private inline fun <reified T : UIGestureRecognizer> addGestures(vararg gestures: Gesture<T>) {
    gestures.forEach { gesture ->
      if (gesture.isCooperative) {
        mapView.gestureRecognizers!!.filterIsInstance<T>().forEach {
          gesture.recognizer.requireGestureRecognizerToFail(it)
        }
      }
      this.gestures.add(gesture)
      mapView.addGestureRecognizer(gesture.recognizer)
    }
  }

  override var isDebugEnabled: Boolean
    get() = mapView.debugMask != 0uL
    set(value) {
      mapView.debugMask =
        if (value)
          MLNMapDebugTileBoundariesMask or
            MLNMapDebugTileInfoMask or
            MLNMapDebugTimestampsMask or
            MLNMapDebugCollisionBoxesMask
        else 0uL
    }

  override val visibleBoundingBox: BoundingBox
    get() = mapView.visibleCoordinateBounds.toBoundingBox()

  override fun setMaximumFps(maximumFps: Int) {
    mapView.preferredFramesPerSecond = maximumFps.toLong()
  }

  override fun setGestureSettings(value: GestureSettings) {
    mapView.rotateEnabled = value.isRotateGesturesEnabled
    mapView.scrollEnabled = value.isScrollGesturesEnabled
    mapView.allowsTilting = value.isTiltGesturesEnabled
    mapView.zoomEnabled = value.isZoomGesturesEnabled
  }

  private fun calculateMargins(
    ornamentPosition: MLNOrnamentPosition,
    uiPadding: PaddingValues,
  ): CValue<CGPoint> {
    return when (ornamentPosition) {
      MLNOrnamentPositionTopLeft ->
        CGPointMake(
          (uiPadding.calculateLeftPadding(layoutDir).value -
              insetPadding.calculateLeftPadding(layoutDir).value)
            .toDouble()
            .coerceAtLeast(0.0) + 8.0,
          (uiPadding.calculateTopPadding().value - insetPadding.calculateTopPadding().value)
            .toDouble()
            .coerceAtLeast(0.0) + 8.0,
        )

      MLNOrnamentPositionTopRight ->
        CGPointMake(
          (uiPadding.calculateRightPadding(layoutDir).value -
              insetPadding.calculateRightPadding(layoutDir).value)
            .toDouble()
            .coerceAtLeast(0.0) + 8.0,
          (uiPadding.calculateTopPadding().value - insetPadding.calculateTopPadding().value)
            .toDouble()
            .coerceAtLeast(0.0) + 8.0,
        )

      MLNOrnamentPositionBottomLeft ->
        CGPointMake(
          (uiPadding.calculateLeftPadding(layoutDir).value -
              insetPadding.calculateLeftPadding(layoutDir).value)
            .toDouble()
            .coerceAtLeast(0.0) + 8.0,
          (uiPadding.calculateBottomPadding().value - insetPadding.calculateBottomPadding().value)
            .toDouble()
            .coerceAtLeast(0.0) + 8.0,
        )

      MLNOrnamentPositionBottomRight ->
        CGPointMake(
          (uiPadding.calculateRightPadding(layoutDir).value -
              insetPadding.calculateRightPadding(layoutDir).value)
            .toDouble()
            .coerceAtLeast(0.0) + 8.0,
          (uiPadding.calculateBottomPadding().value - insetPadding.calculateBottomPadding().value)
            .toDouble()
            .coerceAtLeast(0.0) + 8.0,
        )

      else -> error("Invalid ornament position")
    }
  }

  override fun setOrnamentSettings(value: OrnamentSettings) {
    mapView.logoView.hidden = !value.isLogoEnabled
    mapView.logoViewPosition = value.logoAlignment.toMLNOrnamentPosition(layoutDir)
    mapView.setLogoViewMargins(calculateMargins(mapView.logoViewPosition, value.padding))

    mapView.attributionButton.hidden = !value.isAttributionEnabled
    mapView.attributionButtonPosition = value.attributionAlignment.toMLNOrnamentPosition(layoutDir)
    mapView.setAttributionButtonMargins(
      calculateMargins(mapView.attributionButtonPosition, value.padding)
    )

    mapView.compassView.hidden = !value.isCompassEnabled
    mapView.compassViewPosition = value.compassAlignment.toMLNOrnamentPosition(layoutDir)
    mapView.setCompassViewMargins(calculateMargins(mapView.compassViewPosition, value.padding))
  }

  private fun MLNMapCamera.toCameraPosition(paddingValues: PaddingValues) =
    CameraPosition(
      target = centerCoordinate.toPosition(),
      bearing = heading,
      tilt = pitch,
      zoom =
        MLNZoomLevelForAltitude(
          altitude = altitude,
          pitch = pitch,
          latitude = centerCoordinate.useContents { latitude },
          size = size,
        ),
      padding = paddingValues,
    )

  private fun CameraPosition.toMLNMapCamera(): MLNMapCamera {
    return MLNMapCamera().let {
      it.centerCoordinate = target.toCLLocationCoordinate2D()
      it.pitch = tilt
      it.heading = bearing
      it.altitude =
        MLNAltitudeForZoomLevel(
          zoomLevel = zoom,
          pitch = tilt,
          latitude = target.latitude,
          size = size,
        )
      it
    }
  }

  override var cameraPosition: CameraPosition
    get() =
      mapView.camera.toCameraPosition(
        paddingValues =
          mapView.cameraEdgeInsets.useContents {
            PaddingValues.Absolute(
              left = left.dp,
              top = top.dp,
              right = right.dp,
              bottom = bottom.dp,
            )
          }
      )
    set(value) {
      mapView.setCamera(
        value.toMLNMapCamera(),
        withDuration = 0.0,
        animationTimingFunction = null,
        edgePadding = value.padding.toEdgeInsets(),
        completionHandler = null,
      )
    }

  override var onFpsChanged: (Double) -> Unit = { _ -> }

  private fun PaddingValues.toEdgeInsets(): CValue<UIEdgeInsets> =
    UIEdgeInsetsMake(
      top = calculateTopPadding().value.toDouble(),
      left = calculateLeftPadding(layoutDir).value.toDouble(),
      bottom = calculateBottomPadding().value.toDouble(),
      right = calculateRightPadding(layoutDir).value.toDouble(),
    )

  override suspend fun animateCameraPosition(finalPosition: CameraPosition, duration: Duration) =
    suspendCoroutine { cont ->
      mapView.flyToCamera(
        finalPosition.toMLNMapCamera(),
        withDuration = duration.toDouble(DurationUnit.SECONDS),
        edgePadding = finalPosition.padding.toEdgeInsets(),
        completionHandler = { cont.resume(Unit) },
      )
    }

  override fun positionFromScreenLocation(offset: DpOffset): Position =
    mapView.convertPoint(point = offset.toCGPoint(), toCoordinateFromView = null).toPosition()

  override fun screenLocationFromPosition(position: Position): DpOffset =
    mapView
      .convertCoordinate(position.toCLLocationCoordinate2D(), toPointToView = null)
      .toDpOffset()

  override fun queryRenderedFeatures(offset: DpOffset): List<Feature> {
    return mapView.visibleFeaturesAtPoint(point = offset.toCGPoint()).map {
      (it as MLNFeatureProtocol).toFeature()
    }
  }

  override fun queryRenderedFeatures(offset: DpOffset, layerIds: Set<String>): List<Feature> {
    return mapView
      .visibleFeaturesAtPoint(point = offset.toCGPoint(), inStyleLayersWithIdentifiers = layerIds)
      .map { (it as MLNFeatureProtocol).toFeature() }
  }

  override fun queryRenderedFeatures(
    offset: DpOffset,
    layerIds: Set<String>,
    predicate: Expression<Boolean>,
  ): List<Feature> {
    return mapView
      .visibleFeaturesAtPoint(
        point = offset.toCGPoint(),
        inStyleLayersWithIdentifiers = layerIds,
        predicate = predicate.toNSPredicate(),
      )
      .map { (it as MLNFeatureProtocol).toFeature() }
  }

  override fun queryRenderedFeatures(rect: DpRect): List<Feature> {
    return mapView.visibleFeaturesInRect(rect = rect.toCGRect()).map {
      (it as MLNFeatureProtocol).toFeature()
    }
  }

  override fun queryRenderedFeatures(rect: DpRect, layerIds: Set<String>): List<Feature> {
    return mapView
      .visibleFeaturesInRect(rect = rect.toCGRect(), inStyleLayersWithIdentifiers = layerIds)
      .map { (it as MLNFeatureProtocol).toFeature() }
  }

  override fun queryRenderedFeatures(
    rect: DpRect,
    layerIds: Set<String>,
    predicate: Expression<Boolean>,
  ): List<Feature> {
    return mapView
      .visibleFeaturesInRect(
        rect = rect.toCGRect(),
        inStyleLayersWithIdentifiers = layerIds,
        predicate = predicate.toNSPredicate(),
      )
      .map { (it as MLNFeatureProtocol).toFeature() }
  }
}
