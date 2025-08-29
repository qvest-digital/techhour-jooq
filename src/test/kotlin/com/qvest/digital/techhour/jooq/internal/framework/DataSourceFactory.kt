package com.qvest.digital.techhour.jooq.internal.framework

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.testcontainers.containers.PostgreSQLContainer
import javax.sql.DataSource

object DataSourceFactory {

    fun create (url: String, username: String, password: String): DataSource = HikariDataSource (
        HikariConfig ().also {
            it.jdbcUrl = url
            it.username = username
            it.password = password
            it.driverClassName = "org.postgresql.Driver"
            it.maximumPoolSize = 10
            it.isAutoCommit = true
        }
    )

    fun create (container: PostgreSQLContainer<*>): DataSource = create (container.jdbcUrl, container.username, container.password)

}
