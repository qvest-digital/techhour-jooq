package com.qvest.digital.techhour.jooq.internal.shop

import com.qvest.digital.techhour.jooq.api.shop.Revenue
import com.qvest.digital.techhour.jooq.internal.fixture.Data.playground
import com.qvest.digital.techhour.jooq.internal.framework.DataSourceExtension
import com.qvest.digital.techhour.jooq.internal.framework.FlywayExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.sql.DataSource

@ExtendWith (DataSourceExtension::class, FlywayExtension::class)
internal class Demo {

    @Test
    fun `should play nicely` (datasource: DataSource) {
        val books = BookRepositoryJooq (datasource)
        val customers = CustomerRepositoryJooq (datasource)
        val purchases = PurchaseRepositoryJooq (datasource)
        val revenuesV = RevenueDaoViewJooq (datasource)
        val revenuesC = RevenueDaoCodeJooq (datasource)

        repeat (2) {
            playground.books.values.forEach (books::upsert)
            playground.customers.values.forEach (customers::upsert)
            playground.purchases.values.forEach (purchases::upsert)
        }

        assertThat (books.all ()).containsExactlyInAnyOrderElementsOf (playground.books.values)
        assertThat (customers.all ()).containsExactlyInAnyOrderElementsOf (playground.customers.values)
        assertThat (purchases.all ()).containsExactlyInAnyOrderElementsOf (playground.purchases.values)

        listOf (
            revenuesV.all (),
            revenuesC.all ()
        ).forEach {
            assertThat (it).containsExactlyInAnyOrder (
                Revenue (playground.customers.getValue (1), "69.94".toBigDecimal ()),
                Revenue (playground.customers.getValue (2), "90.00".toBigDecimal ()),
                Revenue (playground.customers.getValue (3), "29.99".toBigDecimal ()),
            )
        }

        playground.customers.values.forEach {
            assertThat (revenuesV.of (it)).isEqualTo (revenuesC.of (it))
        }
    }

}
