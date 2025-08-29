package com.qvest.digital.techhour.jooq.internal.shop

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import javax.sql.DataSource

internal interface JooqAware {

    val datasource: DataSource

    fun <T> jooq (tx: Boolean = false, f: DSLContext.() -> T): T = DSL.using (datasource, SQLDialect.POSTGRES).let {
        context ->
        when (tx) {
            true -> context.transactionResult { tx -> tx.dsl ().f () }
            else -> context.f ()
        }
    }

}
