package com.qvest.digital.techhour.jooq.internal.framework

import org.junit.jupiter.api.extension.ExtensionContext

internal fun ExtensionContext.namespace () = ExtensionContext.Namespace.create (requiredTestClass)

internal fun ExtensionContext.store () = getStore (namespace ())
