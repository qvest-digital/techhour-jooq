package com.qvest.digital.techhour.jooq.api.shop

import java.time.Instant

data class Customer (val id: Long, val name: String, val email: String?, val since: Instant)
