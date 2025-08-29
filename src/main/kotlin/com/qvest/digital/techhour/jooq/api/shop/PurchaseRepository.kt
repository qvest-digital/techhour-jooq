package com.qvest.digital.techhour.jooq.api.shop

interface PurchaseRepository {

    fun upsert (purchase: Purchase)

    fun all (): List<Purchase>

}
