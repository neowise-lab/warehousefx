package com.neowise.warehouse.data.common

enum class DBSource(val text: String) {
    OPEN("Открыть имеющийся"),
    CREATE_NEW("Создать новую");

    override fun toString(): String {
        return text
    }
}