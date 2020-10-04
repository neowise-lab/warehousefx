package com.neowise.warehouse.data.repository

import com.neowise.warehouse.data.entities.Category
import com.neowise.warehouse.data.entities.Product

interface ProductRepository {
    fun fetch(s: Category) : List<Product>
    fun create(item: Product, s: Category) : Product
    fun update(target: Product, new: Product) : Boolean
    fun remove(target: Product) : Boolean
}