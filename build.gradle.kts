import org.gradle.internal.impldep.org.junit.platform.launcher.TagFilter.includeTags

plugins {
    alias (libs.plugins.org.jetbrains.kotlin.jvm)
    alias (libs.plugins.org.asciidoctor.jvm.convert)
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
    val asciidoctortest by registering (Test::class) {
        group = asciidoctor.get ().group

        useJUnitPlatform {
            includeTags ("asciidoc")
        }
    }

    asciidoctor {
        dependsOn (asciidoctortest)

        val base = project.layout.projectDirectory.file ("src/main/asciidoc").asFile
        val output = project.layout.buildDirectory.file ("manual").get ().asFile

        outputDirProperty.set (output)
        sourceDirProperty.set (base)

        baseDirFollowsSourceDir ()

        sources {
            include ("index.adoc")
        }
    }

    test {
        useJUnitPlatform {
            excludeTags ("asciidoc")
        }
    }
}
