package com.qvest.digital.techhour.jooq.internal.shop

import com.qvest.digital.techhour.jooq.api.shop.Customer
import com.qvest.digital.techhour.jooq.api.shop.RevenueDao
import com.qvest.digital.techhour.jooq.internal.jooq.Tables.REVENUE
import com.qvest.digital.techhour.jooq.internal.shop.RevenueDao.revenuefy
import javax.sql.DataSource

internal class RevenueDaoViewJooq (override val datasource: DataSource): RevenueDao, JooqAware {

    override fun of (customer: Customer) = jooq {
        selectFrom (REVENUE)
            .where (REVENUE.ID.eq (customer.id))
            .fetchOne ()
                ?.let (::revenuefy)
    }

    override fun all () = jooq {
        selectFrom (REVENUE)
            .fetch ()
            .map (::revenuefy)
    }

}
