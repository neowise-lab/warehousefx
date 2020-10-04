package com.neowise.warehouse.util

import javafx.scene.control.ComboBox
import javafx.scene.control.DatePicker
import javafx.scene.control.TextField
import javafx.util.StringConverter
import java.time.LocalDate
import java.util.function.Consumer

class IllegalFieldValueException(msg: String) : Exception(msg)

@Throws(IllegalFieldValueException::class)
fun TextField.getFieldString(): String {
    val text = this.text.trim { it <= ' ' }

    if (text.isEmpty()) {
        this.styleClass.add("wrong-credentials")
        println(styleClass)
        throw IllegalFieldValueException("\"" + this.promptText + "\" тулдирилмаган")
    }

    this.styleClass.remove("wrong-credentials")

    return text
}

@Throws(IllegalFieldValueException::class)
fun DatePicker.getFieldDate(): LocalDate {

    if (value == null) {
        styleClass.add("wrong-credentials")
        throw IllegalFieldValueException("\"$promptText\" тулдирилмаган")
    }

    styleClass.remove("wrong-credentials")
    return value
}

@Throws(IllegalFieldValueException::class)
fun TextField.getFieldDouble(): Double {
    val text = text.trim { it <= ' ' }

    if (text.isEmpty()) {
        styleClass.add("wrong-credentials")
        println("err: $text")
        throw IllegalFieldValueException("\"$promptText\" тулдирилмаган")
    }

    val v: Double
    try {
        v = doubleFromString(text).toDouble()
        styleClass.remove("wrong-credentials")
    } catch (e: NumberFormatException) {
        styleClass.add("wrong-credentials")
        println("err: $text")
        throw IllegalFieldValueException("\"$promptText\" хато тулдирилган")
    }

    return v
}

@Throws(IllegalFieldValueException::class)
fun TextField.getFieldInteger(): Int {
    val text = text.trim { it <= ' ' }

    if (text.isEmpty()) {
        styleClass.add("wrong-credentials")
        println("err: $text")
        throw IllegalFieldValueException("\"$promptText\" тулдирилмаган")
    }

    val v: Int
    try {
        v = Integer.parseInt(text)
        styleClass.remove("wrong-credentials")
    } catch (e: NumberFormatException) {
        styleClass.add("wrong-credentials")
        println("err: $text")
        throw IllegalFieldValueException("\"" + promptText + "\" хато тулдирилган")
    }

    return v
}

@Throws(IllegalFieldValueException::class)
fun TextField.getFieldAmound(): Double {
    val text = text.trim { it <= ' ' }

    if (text.isEmpty()) {
        styleClass.add("wrong-credentials")
        throw IllegalFieldValueException("\"$promptText\" тулдирилмаган")
    }

    val v: Double
    try {
        v = amountAsDouble(text)
        styleClass.remove("wrong-credentials")
    } catch (e: NumberFormatException) {
        styleClass.add("wrong-credentials")
        throw IllegalFieldValueException("\"$promptText\" хато тулдирилган")
    }

    return v
}

@Throws(IllegalFieldValueException::class)
fun <T> ComboBox<*>.getComboItem(): T {
    val v = selectionModel.selectedItem
        ?: throw IllegalFieldValueException("\"$promptText\" сайланмаган")
    return v as T
}

class DoubleConverter(private val callback: Consumer<Boolean>? = null) : StringConverter<Double>() {

    override fun toString(obj: Double?): String {
        return if (obj == null) {
            callback?.accept(java.lang.Boolean.FALSE)
            "0"
        } else {
            callback?.accept(true)
            doubleAsString(obj)
        }
    }

    override fun fromString(string: String?): Double? {
        return if (string == null || string == "") 0.0 else doubleFromString(
            string
        ).toDouble()
    }
}