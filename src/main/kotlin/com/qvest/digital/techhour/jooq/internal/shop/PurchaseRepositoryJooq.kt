package com.qvest.digital.techhour.jooq.internal.shop

import com.qvest.digital.techhour.jooq.api.shop.Book
import com.qvest.digital.techhour.jooq.api.shop.Customer
import com.qvest.digital.techhour.jooq.api.shop.Purchase
import com.qvest.digital.techhour.jooq.api.shop.PurchaseRepository
import com.qvest.digital.techhour.jooq.internal.jooq.Tables.BOOK
import com.qvest.digital.techhour.jooq.internal.jooq.Tables.CUSTOMER
import com.qvest.digital.techhour.jooq.internal.jooq.Tables.PURCHASE
import com.qvest.digital.techhour.jooq.internal.jooq.tables.records.PurchaseRecord
import com.qvest.digital.techhour.jooq.internal.shop.BookRepositoryJooq.Companion.asBook
import com.qvest.digital.techhour.jooq.internal.shop.CustomerRepositoryJooq.Companion.asCustomer
import java.time.ZoneOffset.UTC
import javax.sql.DataSource

internal class PurchaseRepositoryJooq (override val datasource: DataSource): PurchaseRepository, JooqAware {

    override fun upsert (purchase: Purchase): Unit = jooq (tx = true) {
        insertInto (PURCHASE).set (purchase.asRecord ())
            .onDuplicateKeyUpdate ()
            .setNonKeyToExcluded ()
            .execute ()
    }

    override fun all () = jooq {
        selectFrom (PURCHASE.join (CUSTOMER).onKey ().join (BOOK).onKey ())
            .fetch ()
            .map {
                val c = it.into (CUSTOMER)
                val b = it.into (BOOK)
                val p = it.into (PURCHASE)

                p.asPurchase (c.asCustomer (), b.asBook ())
            }
    }

    internal companion object {

        fun PurchaseRecord.asPurchase (customer: Customer, book: Book) = Purchase (id, customer, book, quantity, at.toInstant ())

        fun Purchase.asRecord () = PurchaseRecord (id, customer.id, book.id, quantity, at.atOffset (UTC))

    }

}
