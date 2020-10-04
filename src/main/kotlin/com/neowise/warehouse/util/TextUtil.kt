package com.neowise.warehouse.util

import java.text.DecimalFormat

private val decimalFormat = DecimalFormat("#,##0.0")
private val doubleFormat = DecimalFormat("###0.0")

fun sizeAsString(w: Double, h: Double): String {
    return "Эни: ${String.format("%.1f", w)} х Буйи: ${String.format("%.1f", h)}"
}

fun tFormSizeAsString(w: Double, h: Double, ew: Double, eh: Double): String {
    return "Эни: ${String.format("%.1f", w)} х Буйи: ${String.format("%.1f", h)} (К.эни: ${String.format("%.1f", ew)} х к.буйи: ${String.format("%.1f", eh)})"
}

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

private fun dropZero(v: String): String {
    val commaIndex = v.indexOf(',')
    if (commaIndex == -1) return v
    val zeros = v.substring(commaIndex+1)
    if (zeros == "0") return v.substring(0, commaIndex)
    return v
}