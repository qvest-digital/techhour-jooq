package com.qvest.digital.techhour.jooq.internal.shop

import com.qvest.digital.techhour.jooq.api.shop.Customer
import com.qvest.digital.techhour.jooq.api.shop.Revenue
import com.qvest.digital.techhour.jooq.internal.jooq.Tables.CUSTOMER
import com.qvest.digital.techhour.jooq.internal.jooq.Tables.REVENUE
import com.qvest.digital.techhour.jooq.internal.jooq.tables.records.RevenueRecord
import com.qvest.digital.techhour.jooq.internal.shop.CustomerRepositoryJooq.Companion.asCustomer
import org.jooq.Record

internal object RevenueDao {

    fun revenuefy (record: Record): Revenue {
        val c = record.into (CUSTOMER)
        val r = record.into (REVENUE)

        return r.asRevenue (c.asCustomer ())
    }

    fun RevenueRecord.asRevenue (customer: Customer) = Revenue (customer, amount)

}
