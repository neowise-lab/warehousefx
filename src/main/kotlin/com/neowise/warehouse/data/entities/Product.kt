package com.neowise.warehouse.data.entities

import com.neowise.warehouse.data.common.Units

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val volume: Double,
    val unit: Units
) {

    companion object {
       val WRONG_PRODUCT = Product(-1, "", "", .0, Units.COUNT)
    }

    override fun toString(): String = name
}