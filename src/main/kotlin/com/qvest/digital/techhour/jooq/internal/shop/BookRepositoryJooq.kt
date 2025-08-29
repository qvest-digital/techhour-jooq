package com.qvest.digital.techhour.jooq.internal.shop

import com.qvest.digital.techhour.jooq.api.shop.Book
import com.qvest.digital.techhour.jooq.api.shop.BookRepository
import com.qvest.digital.techhour.jooq.internal.jooq.Tables.BOOK
import com.qvest.digital.techhour.jooq.internal.jooq.tables.records.BookRecord
import javax.sql.DataSource

internal class BookRepositoryJooq (override val datasource: DataSource): BookRepository, JooqAware {

    override fun upsert (book: Book): Unit = jooq (tx = true) {
        insertInto (BOOK).set (book.asRecord ())
            .onDuplicateKeyUpdate ()
            .setNonKeyToExcluded ()
            .execute ()
    }

    override fun all () = jooq {
        selectFrom (BOOK).fetch ().map { it.asBook () }
    }

    internal companion object {

        fun BookRecord.asBook () = Book (id, title, author, price)

        fun Book.asRecord () = BookRecord (id, title, author, price)

    }

}
