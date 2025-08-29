plugins {
    alias (libs.plugins.org.jetbrains.kotlin.jvm)
}

group   = "com.qvest.digital"
version = "0.0.0-SNAPSHOT"

repositories {
    mavenCentral ()
}

dependencies {
    testImplementation (platform (libs.org.junit.junit.bom))
    testImplementation (libs.org.assertj.assertj.core)
    testImplementation ("org.junit.jupiter:junit-jupiter-api")
    testImplementation ("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly ("org.junit.platform:junit-platform-launcher")
}

kotlin {
    jvmToolchain (21)
}

tasks {
    test {
        useJUnitPlatform ()
    }
}
