package com.github.jackchen.android.sample.api

/**
 * Marks the specified class as a sample object.
 *
 * By default, subclasses of Activity or Fragment or DialogFragment
 * are supported as sample objects. Or, you can create new class
 * to add new support. The new class need to use the [Extension]
 * annotation and implement from the ActionProcessor interface.
 *
 * @author airsaid
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Register(
  /**
   * The title of the sample object.
   *
   * If not specified, the simple class name of the sample class
   * will be used by default.
   */
  val title: String = "",

  /**
   * The description of the sample object.
   *
   * If not specified, the description will not showed.
   */
  val desc: String = "",

  /**
   * The path of the sample object.
   *
   * If not specified, the generation will be done automatically
   * based on the package name of the simple class.
   */
  val path: String = "",

  /**
   * The path title of the [path].
   *
   * When specified, the category name of the path will be displayed in the list.
   * If it is not specified, the name of the most recent [path] will be displayed.
   */
  val pathTitle: String = "",

  /**
   * The path description of the [path].
   *
   * When specified, the category desc of the path will be displayed in the list.
   * If it is not specified, it will not be displayed.
   */
  val pathDesc: String = ""
)
