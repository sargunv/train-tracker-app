package dev.sargunv.kzip

import kotlinx.io.Source

public expect fun unzip(
  zipArchive: Source,
  handleFile: (path: String, content: Source) -> Unit = { _, content -> content.close() },
)
