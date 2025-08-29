package com.qvest.digital.techhour.jooq.internal.framework

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.testcontainers.containers.PostgreSQLContainer
import java.io.Closeable
import javax.sql.DataSource

internal class DataSourceExtension: BeforeAllCallback, ParameterResolver, AfterAllCallback {

    private val postgres = PostgreSQLContainer ("postgres:17.5-alpine")
        .withUsername ("postgres")
        .withPassword ("postgres")
        .withDatabaseName ("techhour")

    override fun beforeAll (context: ExtensionContext) {
        postgres.start ()

        context.store ().put (key, DataSourceFactory.create (postgres))
    }

    override fun afterAll (context: ExtensionContext) {
        runCatching {
            context.store ().remove (key).let {
                    d ->
                if (d is Closeable)
                    d.close ()
            }
        }
        runCatching { postgres.close () }
    }

    override fun supportsParameter (parameterContext: ParameterContext, extensionContext: ExtensionContext) =
        parameterContext.parameter.getType () == DataSource::class.java

    override fun resolveParameter (parameterContext: ParameterContext, extensionContext: ExtensionContext): Any? =
        extensionContext.store ().get (key) // we are only supporting one type that was checked by jupiter prior

    private companion object {

        const val key = "datasource"

    }

}
