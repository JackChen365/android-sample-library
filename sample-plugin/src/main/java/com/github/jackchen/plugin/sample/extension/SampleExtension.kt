package com.github.jackchen.plugin.sample.extension

import org.gradle.api.provider.Property

/**
 * Configuration object for [com.github.jackchen.plugin.sample.SamplePlugin].
 *
 * @author airsaid
 */
abstract class SampleExtension {

  /**
   * Whether to enable debug mode. default disabled.
   */
  abstract val enableDebug: Property<Boolean>

  init {
    enableDebug.convention(false)
  }
}