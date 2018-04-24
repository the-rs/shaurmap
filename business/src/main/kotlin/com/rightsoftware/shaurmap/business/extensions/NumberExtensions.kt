package com.rightsoftware.shaurmap.business.extensions

import java.math.BigDecimal

infix fun Int.fdiv(i: Int) = this / i.toDouble()

fun Int.progressFrom(total: Int, maxProgress: Int = 100) = ((this fdiv total) * maxProgress).toInt()

fun Double.round(precision: Int) =
        BigDecimal(this).setScale(precision, BigDecimal.ROUND_HALF_UP).toDouble()