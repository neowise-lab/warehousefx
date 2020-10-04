package com.neowise.warehouse.data.entities

data class Category(val id: Int, val name: String) {
    override fun toString(): String = name

    companion object {
        val ALL_CATEGORY = Category(-1, "Все")
        val WRONG = Category(-2, "WRONG")
    }
}