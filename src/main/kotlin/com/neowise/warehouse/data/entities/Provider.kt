package com.neowise.warehouse.data.entities

data class Provider(
    val id: Int,
    val name: String
) {

    companion object {
        val WRONG_PROVIDER = Provider(-1, "")
    }

    override fun toString() = name
}