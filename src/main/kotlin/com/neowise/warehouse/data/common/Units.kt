package com.neowise.warehouse.data.common

enum class Units(val text: String) {
    COUNT("шт"),
    KG("кг");

    override fun toString(): String {
        return text
    }
}