package com.qvest.digital.techhour.jooq.internal.framework

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import javax.sql.DataSource

internal class FlywayExtension: BeforeAllCallback {

    override fun beforeAll (context: ExtensionContext) {
        val datasource = context.store ().get ("datasource", DataSource::class.java)
            ?: return

        Flyway.configure ().configuration (
            mapOf (
                "flyway.sqlMigrationPrefix" to "v",
                "flyway.sqlMigrationSeparator" to "-"
            )
        )
            .dataSource (datasource)
            .load ()
            .migrate ()
    }

}
