package com.qvest.digital.techhour.jooq.internal.shop

import com.qvest.digital.techhour.jooq.api.shop.Customer
import com.qvest.digital.techhour.jooq.api.shop.RevenueDao
import com.qvest.digital.techhour.jooq.internal.jooq.Tables.BOOK
import com.qvest.digital.techhour.jooq.internal.jooq.Tables.CUSTOMER
import com.qvest.digital.techhour.jooq.internal.jooq.Tables.PURCHASE
import com.qvest.digital.techhour.jooq.internal.shop.RevenueDao.revenuefy
import org.jooq.DSLContext
import org.jooq.impl.DSL.sum
import javax.sql.DataSource

internal class RevenueDaoCodeJooq (override val datasource: DataSource): RevenueDao, JooqAware {

    override fun of (customer: Customer) = jooq {
        revenue ()
            .having (CUSTOMER.ID.eq (customer.id))
            .fetchOne ()
                ?.let (::revenuefy)
    }

    override fun all () = jooq {
        revenue ()
            .fetch ()
            .map (::revenuefy)
    }

    private fun DSLContext.revenue () =
        select (CUSTOMER.ID, CUSTOMER.NAME, CUSTOMER.EMAIL, CUSTOMER.SINCE, sum (PURCHASE.QUANTITY * BOOK.PRICE).`as` ("amount"))
            .from (PURCHASE)
            .join (CUSTOMER).onKey ()
            .join (BOOK).onKey ()
            .groupBy (CUSTOMER.ID, CUSTOMER.NAME, CUSTOMER.EMAIL, CUSTOMER.SINCE)
}
