plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "no.uio.ifi.in2000.team7.boatbuddy"
    compileSdk = 34

    defaultConfig {
        applicationId = "no.uio.ifi.in2000.team7.boatbuddy"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")


    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.02"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.02"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //ktor
    val ktor_version = "2.3.8"
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-client-android:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("io.ktor:ktor-serialization-gson:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.test.espresso:espresso-core:3.5.1")

    val lifecycle_version = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    //OSM
    implementation("org.osmdroid:osmdroid-android:6.1.10")
    implementation("androidx.appcompat:appcompat:1.6.1") // Use the latest version available

    //For navigation
    val navVersion = "2.7.7"
    implementation("androidx.navigation:navigation-compose:$navVersion")


    //mapbox
    implementation("com.mapbox.maps:android:11.2.1")
    implementation("com.mapbox.extension:maps-compose:11.2.1")

    //for dotenv variable
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    //location permission
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("com.google.accompanist:accompanist-permissions:0.35.0-alpha")
}