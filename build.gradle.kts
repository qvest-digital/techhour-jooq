import org.gradle.internal.impldep.org.junit.platform.launcher.TagFilter.includeTags

plugins {
    alias (libs.plugins.dev.monosoul.jooq.docker)
    alias (libs.plugins.org.jetbrains.kotlin.jvm)
    alias (libs.plugins.org.asciidoctor.jvm.convert)
}

group   = "com.qvest.digital"
version = "0.0.0-SNAPSHOT"

repositories {
    mavenCentral ()
}

dependencies {
    implementation (libs.org.flywaydb.flyway.core)
    runtimeOnly    (libs.org.flywaydb.flyway.database.postgresql)
    implementation (libs.org.jooq.jooq)
    implementation (libs.org.postgresql.postgresql)
    jooqCodegen    (libs.org.postgresql.postgresql)

    testImplementation (platform (libs.org.junit.junit.bom))
    testImplementation (libs.org.assertj.assertj.core)
    testImplementation ("org.junit.jupiter:junit-jupiter-api")
    testImplementation ("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly ("org.junit.platform:junit-platform-launcher")
}

jooq {
    withContainer {
        image {
            name = "postgres:17.5-alpine"
        }
    }
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

    generateJooqClasses {
        basePackageName.set ("${project.group}.techhour.jooq.internal.jooq")
        outputDirectory.set (project.layout.buildDirectory.dir ("generated/sources/jooq"))
        includeFlywayTable.set (false)
        flywayProperties.apply {
            put ("flyway.sqlMigrationPrefix", "v")
            put ("flyway.sqlMigrationSeparator", "-")
        }
    }

    test {
        useJUnitPlatform {
            excludeTags ("asciidoc")
        }
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}
