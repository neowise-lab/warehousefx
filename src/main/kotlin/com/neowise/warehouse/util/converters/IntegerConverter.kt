package com.neowise.warehouse.util.converters

import javafx.util.StringConverter

import java.util.function.Consumer

/**
 * @author
 */
class IntegerConverter(private val callback: Consumer<Boolean>?) : StringConverter<Int>() {

    constructor() : this(null)

    override fun toString(obj: Int?): String {
        return if (obj == null) {
            callback?.accept(java.lang.Boolean.FALSE)
            ""
        } else {
            callback?.accept(true)
            if (obj == 0) "" else obj.toString()
        }
    }

    override fun fromString(string: String?): Int? {
        return if (string == null || string == "") 0 else Integer.parseInt(string)
    }
}
