package com.qvest.digital.techhour.jooq.api.shop

import java.time.Instant

data class Purchase (val id: Long, val customer: Customer, val book: Book, val quantity: Int, val at: Instant)
