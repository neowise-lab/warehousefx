package com.neowise.warehouse.util

class ItemCache<T> {
    private val cache = mutableListOf<T>()

    fun reproduce(count: Int, instanceCreator: () -> T) {
        cache.clear()
        for (i in 0..count) {
            cache += instanceCreator()
        }
    }

    operator fun get(i: Int): T {
        return cache[i]
    }
}