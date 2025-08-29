package com.qvest.digital.techhour.jooq.internal.fixture

import com.qvest.digital.techhour.jooq.api.shop.Book
import com.qvest.digital.techhour.jooq.api.shop.Customer
import com.qvest.digital.techhour.jooq.api.shop.Purchase
import com.qvest.digital.techhour.jooq.internal.fixture.Data.playground
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.io.File
import java.time.Instant.parse as instant

internal interface Playground {

    val books: Map<Long, Book>

    val customers: Map<Long, Customer>

    val purchases: Map<Long, Purchase>

}

internal object Data  {

    val playground = object: Playground {
        override val books = listOf (
            Book (101, "SQL for Beginners", "Jane Doe",    "29.99".toBigDecimal ()),
            Book (102, "Mastering Python",  "John Smith",  "45.00".toBigDecimal ()),
            Book (103, "Data Science 101",  "Emily Stone", "39.95".toBigDecimal ())
        ).associateBy (Book::id)

        override val customers = listOf (
            Customer (1, "Alice Johnson", "alice@example.com", instant ("2023-02-10T12:43:04Z")),
            Customer (2, "Bob Smith",     "bob@example.com",   instant ("2023-05-21T15:10:32Z")),
            Customer (3, "Clara Lee",     "clara@example.com", instant ("2024-01-05T08:01:45Z"))
        ).associateBy (Customer::id)

        override val purchases = listOf (
            Purchase (1001, customers.getValue (1), books.getValue (101), 1, instant ("2024-02-15T13:43:31Z")),
            Purchase (1002, customers.getValue (2), books.getValue (102), 2, instant ("2024-06-01T08:52:12Z")),
            Purchase (1003, customers.getValue (1), books.getValue (103), 1, instant ("2024-08-10T10:12:11Z")),
            Purchase (1004, customers.getValue (3), books.getValue (101), 1, instant ("2024-09-05T17:43:43Z"))
        ).associateBy (Purchase::id)
    }

}

@Tag ("asciidoc")
internal class DataManual {

    @Test
    fun `should generate the data sql statements` () {
        asciidata.apply (File::mkdirs).resolve ("data.sql").apply {
            delete ()

            appendText ("""
                |insert into customer (id, name, email, since) values
                |${playground.customers.values.joinToString (",\n") {
                "    (%-1d, %-15s, %-19s, %s)".format (it.id, "'${it.name}'", "'${it.email}'", "'${it.since}'")
                }}
                |;
                |
                |insert into book (id, title, author, price) values
                |${playground.books.values.joinToString (",\n") {
                "    (%-2d, %-19s, %-13s, %2.2f)".format (it.id, "'${it.title}'", "'${it.author}'", it.price)
                }}
                |;
                |
                |insert into purchase (id, customer, book, quantity, at) values
                |${playground.purchases.values.joinToString (",\n") {
                "    (%-3d, %-1d, %-2d, %-1d, %s)".format (it.id, it.customer.id, it.book.id, it.quantity, "'${it.at}'")
                }}
                |;
                |
            """.trimMargin ())
        }
    }

    @Test
    fun `should generate the data tables` () {
        asciidata.apply (File::mkdirs).resolve ("data.tbl").apply {
            delete ()

            appendText ("""
                |=== Customer
                |[%header,format=csv]
                ||===
                |"ID","NAME","EMAIL","SINCE"
                |${playground.customers.values.joinToString ("\n") { """"${it.id}","${it.name}","${it.email}","${it.since}"""" }}
                ||===
                |
                |=== Book
                |[%header,format=csv]
                ||===
                |"ID","TITLE","AUTHOR","PRICE"
                |${playground.books.values.joinToString ("\n") { """"${it.id}","${it.title}","${it.author}","${it.price}"""" }}
                ||===
                |
                |=== Purchase
                |[%header,format=csv]
                ||===
                |"ID","CUSTOMER","BOOK","QUANTITY","AT"
                |${playground.purchases.values.joinToString ("\n") { """"${it.id}","${it.customer.id}","${it.book.id}","${it.quantity}","${it.at}"""" }}
                ||===
                |
            """.trimMargin ())
        }
    }

    private companion object {

        val asciidata = File ("./src/main/asciidoc/data/generated")

    }

}
