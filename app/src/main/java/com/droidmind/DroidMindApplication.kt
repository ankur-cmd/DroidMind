package com.droidmind

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for DroidMind.
 *
 * The [HiltAndroidApp] annotation triggers Hilt's code generation — it
 * creates the base dependency container that every other Hilt component
 * (Activities, ViewModels, etc.) attaches to. Every Hilt-powered app needs
 * exactly one class annotated like this.
 */
@HiltAndroidApp
class DroidMindApplication : Application()