//plugins {
//    id("com.android.application")
//    id("org.jetbrains.kotlin.android")
//    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
//}
//
//android {
//    namespace = "com.google.ai.sample"
//    compileSdk = 34
//
//    defaultConfig {
//        applicationId = "com.google.ai.sample"
//        minSdk = 26
//        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        vectorDrawables {
//            useSupportLibrary = true
//        }
//    }
//
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//    buildFeatures {
//        compose = true
//    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.4"
//    }
//}
//
//dependencies {
//    implementation("androidx.core:core-ktx:1.9.0")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
//    implementation("androidx.activity:activity-compose:1.8.1")
//    implementation("androidx.navigation:navigation-compose:2.7.5")
//
//    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-graphics")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3")
//
//    implementation("io.coil-kt:coil-compose:2.5.0")
//
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//    debugImplementation("androidx.compose.ui:ui-tooling")
//    debugImplementation("androidx.compose.ui:ui-test-manifest")
//
//    implementation("com.google.ai.client.generativeai:generativeai:0.1.1")
//
//    // Added for HTTP requests to Flask API
//    implementation("com.squareup.okhttp3:okhttp:4.12.0")
//    // Ensure coroutines are explicitly included (might already be transitive)
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
//}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.google.ai.sample"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.google.ai.sample"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.navigation:navigation-compose:2.7.5") // Added for navigation

    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    implementation("io.coil-kt:coil-compose:2.5.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("com.google.ai.client.generativeai:generativeai:0.1.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}