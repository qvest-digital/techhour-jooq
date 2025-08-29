package com.qvest.digital.techhour.jooq.api.shop

import java.math.BigDecimal

data class Book (val id: Long, val title: String, val author: String, val price: BigDecimal)
