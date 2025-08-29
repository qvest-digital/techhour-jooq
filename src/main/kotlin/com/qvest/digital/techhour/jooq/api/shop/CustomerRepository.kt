package com.qvest.digital.techhour.jooq.api.shop

interface CustomerRepository {

    fun upsert (customer: Customer)

    fun all (): List<Customer>

}
