package com.neowise.warehouse.data.repository

import com.neowise.warehouse.data.entities.History
import com.neowise.warehouse.data.entities.Product

interface HistoryRepository {
    fun fetch(s: Product) : List<History>
    fun create(item: History, s: Product): Boolean
}