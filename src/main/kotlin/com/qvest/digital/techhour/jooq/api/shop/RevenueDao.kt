package com.qvest.digital.techhour.jooq.api.shop

interface RevenueDao {

    fun of (customer: Customer): Revenue?

    fun all (): List<Revenue>

}
