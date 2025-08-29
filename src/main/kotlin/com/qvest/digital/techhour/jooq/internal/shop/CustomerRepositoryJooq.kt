package com.qvest.digital.techhour.jooq.internal.shop

import com.qvest.digital.techhour.jooq.api.shop.Customer
import com.qvest.digital.techhour.jooq.api.shop.CustomerRepository
import com.qvest.digital.techhour.jooq.internal.jooq.Tables.CUSTOMER
import com.qvest.digital.techhour.jooq.internal.jooq.tables.records.CustomerRecord
import java.time.ZoneOffset.UTC
import javax.sql.DataSource

internal class CustomerRepositoryJooq (override val datasource: DataSource): CustomerRepository, JooqAware {

    override fun upsert (customer: Customer): Unit = jooq (tx = true) {
        insertInto (CUSTOMER).set (customer.asRecord ())
            .onDuplicateKeyUpdate ()
            .setNonKeyToExcluded ()
            .execute ()
    }

    override fun all () = jooq {
        selectFrom (CUSTOMER)
            .fetch ()
            .map { it.asCustomer () }
    }

    internal companion object {

        fun CustomerRecord.asCustomer () = Customer (id, name, email, since.toInstant ())

        fun Customer.asRecord () = CustomerRecord (id, name, email, since.atOffset (UTC))

    }

}
