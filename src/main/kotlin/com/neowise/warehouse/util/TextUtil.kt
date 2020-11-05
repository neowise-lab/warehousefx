package com.neowise.warehouse.util

import java.text.DecimalFormat

private val decimalFormat = DecimalFormat("#,##0.0")
private val doubleFormat = DecimalFormat("###0.0")

fun doubleAsAmount(v: Double): String {
    return dropZero(decimalFormat.format(v) ?: "0")
}

fun doubleAsString(v: Double) : String {
    return dropZero(doubleFormat.format(v) ?: "0")
}

fun doubleFromString(v: String): Number {
    return doubleFormat.parse(v.replace('.', ',')) ?: .0
}

fun amountAsDouble(v: String): Double {
    return decimalFormat.parse(v).toDouble()
}

fun dropZero(v: String): String {
    val commaIndex = v.indexOf(',')
    if (commaIndex == -1) return v
    val zeros = v.substring(commaIndex+1)
    if (zeros == "0") return v.substring(0, commaIndex)
    return v
}