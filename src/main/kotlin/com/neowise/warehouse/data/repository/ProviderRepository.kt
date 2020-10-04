package com.neowise.warehouse.data.repository

import com.neowise.warehouse.data.entities.Provider

interface ProviderRepository {
    fun fetch() : List<Provider>
    fun create(item: Provider) : Provider
    fun update(target: Provider, new: Provider): Boolean
    fun remove(target: Provider) : Boolean
}