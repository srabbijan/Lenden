plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false

    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false

    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false

    // 🗄️ Database plugins - For Room database
    alias(libs.plugins.room) apply false                 // Room database plugin
    alias(libs.plugins.ksp) apply false                  // Kotlin Symbol Processing (for Room)
}