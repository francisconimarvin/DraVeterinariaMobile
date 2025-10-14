plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.draveterinaria"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.draveterinaria"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // === ❌ ELIMINAR ESTAS LÍNEAS (porque la versión la maneja el BOM y/o están duplicadas) ===
    // implementation("androidx.compose.material3:material3:1.6.0")
    // implementation(libs.androidx.material3) <== Esta línea es una duplicación
    // implementation("androidx.compose.material3:material3-window-size-class:1.3.2")

    // === ✅ MANTENER ESTAS LÍNEAS (versiones que NO están en el BOM o son necesarias) ===
    implementation("androidx.compose.material:material-icons-extended:1.1.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0-rc02")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // ⚠️ Navegación: Cambiamos a la notación de BOM si está disponible, o la mantenemos con versión.
    // Por simplicidad, es mejor dejar la versión explícita si el BOM no la maneja directamente, pero
    // la forma estándar para Compose Navigation es sin versión cuando se usa el BOM.
    implementation ("androidx.navigation:navigation-compose:2.7.7")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // === 🥇 EL BOM DEBE IR PRIMERO O ÚNICO (si se usa dos veces, es mejor la primera) ===
    implementation(platform("androidx.compose:compose-bom:2024.04.00")) // <== Usa solo una vez la versión
    // implementation(platform(libs.androidx.compose.bom)) <== Esta es la duplicación que puedes eliminar

    // === ✅ LIBRERÍAS SIN VERSIÓN (el BOM provee la versión correcta) ===
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Si quieres usar Material3 y confiar en el BOM:
    implementation("androidx.compose.material3:material3") // Sin especificar versión aquí

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    // ... (El resto de dependencias de testing)
}