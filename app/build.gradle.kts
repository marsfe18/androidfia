plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.polstat.luthfiani"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.polstat.luthfiani"
        minSdk = 30
        targetSdk = 33
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
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {



    implementation("androidx.core:core-ktx:1.12.0")

    implementation("androidx.compose.material:material-icons-extended-desktop:1.6.0-beta01")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.navigation:navigation-compose:2.7.5")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0-rc01")

    implementation("androidx.activity:activity-compose:1.8.1")

    implementation("com.google.android.material:material:1.10.0")

    implementation("androidx.compose.foundation:foundation-layout:1.5.4")
    implementation("androidx.compose.material3:material3:1.2.0-alpha11")
//    implementation("androidx.compose.material3:material3-icons-extended:1.0.0-beta01")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")
    implementation("androidx.compose.runtime:runtime:1.5.4")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.4")

    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

    implementation("io.coil-kt:coil-compose:2.4.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("androidx.test.ext:truth:1.5.0")
    testImplementation("org.robolectric:robolectric:4.9.2")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //live data
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")

    //jwt
    implementation("com.auth0.android:jwtdecode:2.0.0")

}