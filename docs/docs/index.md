# Overview

## Introduction

MapLibre Compose is a [Compose Multiplatform][compose] wrapper around the
[MapLibre][maplibre] SDKs for rendering interactive maps. You can use it to add
maps to your Compose UIs on Android and iOS.

## Usage

- [Getting Started](./getting-started.md)
- [API Reference](./api/index.html)
- [Demo App][repo-demo]

## Status

A large subset of MapLibre's features are already supported, but the full
breadth of the MapLibre SDKs is not yet covered. What is already supported may
have bugs. API stability is not yet guaranteed; we're still exploring how best
to express an interactive map API in Compose.

Android and iOS support is implemented with [MapLibre Native][maplibre-native].

| Feature                                           | Android            | iOS                | Desktop | Web |
| ------------------------------------------------- | ------------------ | ------------------ | ------- | --- |
| Render a map                                      | :white_check_mark: | :white_check_mark: | :x:     | :x: |
| Configure ornaments (compass, logo, attribution)  | :white_check_mark: | :white_check_mark: | :x:     | :x: |
| Configure gestures (pan, zoom, rotate, pitch)     | :white_check_mark: | :white_check_mark: | :x:     | :x: |
| Respond to a map click or long click              | :white_check_mark: | :white_check_mark: | :x:     | :x: |
| Query visible map features                        | :white_check_mark: | :white_check_mark: | :x:     | :x: |
| Get, set, and animate the camera position         | :white_check_mark: | :white_check_mark: | :x:     | :x: |
| Convert between screen and geographic coordinates | :white_check_mark: | :white_check_mark: | :x:     | :x: |
| Get the currently visible region and bounding box | :white_check_mark: | :white_check_mark: | :x:     | :x: |
| Insert, remove, and replace layers                | :white_check_mark: | :white_check_mark: | :x:     | :x: |
| Configure layers with expressions                 | :white_check_mark: | :white_check_mark: | :x:     | :x: |
| Add data sources by URI or GeoJSON                | :white_check_mark: | :white_check_mark: | :x:     | :x: |
| Snapshot the map as an image                      | :x:                | :x:                | :x:     | :x: |
| Configure the offline cache                       | :x:                | :x:                | :x:     | :x: |
| Configure layer transitions                       | :x:                | :x:                | :x:     | :x: |
| Add images to the style                           | :x:                | :x:                | :x:     | :x: |

[compose]: https://www.jetbrains.com/compose-multiplatform/
[maplibre]: https://maplibre.org/
[maplibre-native]: https://github.com/maplibre/maplibre-native
[repo-demo]: https://github.com/sargunv/maplibre-compose/tree/main/demo-app
