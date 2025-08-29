package com.qvest.digital.techhour.jooq.api.shop

import java.math.BigDecimal

data class Revenue (val customer: Customer, val revenue: BigDecimal)
