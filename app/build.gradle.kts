plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // Добавляем плагин для работы с JSON (Serialization)
    // Если будет ошибка версии, попробуй поменять 1.9.0 на версию твоего Kotlin (см. в libs.versions.toml)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 36 // Я поменял на 34 (стабильная), так как 36 может быть нестабильной (Preview). Если нужно 36 - верни.

    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // --- ТВОИ СТАНДАРТНЫЕ БИБЛИОТЕКИ ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // --- НОВЫE БИБЛИОТЕКИ (ДЛЯ ЗАДАНИЯ) ---

    // 1. Сеть (Retrofit + OkHttp)
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:okhttp:5.3.2")

    // 2. JSON Парсинг (Kotlinx Serialization)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // 3. Локальное хранилище / Кэш (DataStore)
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // 4. Загрузка картинок (для иконок погоды)
    implementation("io.coil-kt:coil-compose:2.4.0")

    // 5. ViewModel для Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
}