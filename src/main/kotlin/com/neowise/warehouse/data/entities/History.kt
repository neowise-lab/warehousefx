package com.neowise.warehouse.data.entities

import com.neowise.warehouse.data.common.Units

data class History(
    val id: Int,
    val product: Int,
    val appointment: String,
    val volume: String,
    val date: String
)