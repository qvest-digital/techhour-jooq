package com.qvest.digital.techhour.jooq.api.shop

interface BookRepository {

    fun upsert (book: Book)

    fun all (): List<Book>

}
