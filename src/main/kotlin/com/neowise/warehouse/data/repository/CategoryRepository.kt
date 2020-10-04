package com.neowise.warehouse.data.repository

import com.neowise.warehouse.data.entities.Category

interface CategoryRepository {
    fun fetch() : List<Category>
    fun create(item: Category): Category
    fun update(target: Category, new: Category): Boolean
    fun remove(target: Category): Boolean
}