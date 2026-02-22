import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlin.android")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.configure<LibraryExtension> {
                compileSdk = 35
                defaultConfig {
                    minSdk = 24
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
                buildFeatures {
                    compose = true
                }
                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                    }
                }
            }
            extensions.configure<KotlinAndroidProjectExtension> {
                compilerOptions {
                    jvmTarget = JvmTarget.JVM_11
                }
            }

            dependencies {
                add("implementation", platform(library("androidx.compose.bom")))
                add("implementation", library("androidx.material3"))
                add("implementation", library("androidx.core.ktx"))
                add("implementation", library("koin.compose"))
                add("implementation", library("androidx.ui.tooling"))
            }
        }
    }
}

fun Project.library(alias: String) =
    extensions.getByType(VersionCatalogsExtension::class.java).named("libs").findLibrary(alias)
        .get()

