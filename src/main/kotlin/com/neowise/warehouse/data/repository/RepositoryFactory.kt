package com.neowise.warehouse.data.repository

object RepositoryFactory {

    private var categoryRepository: CategoryRepository? = null
    private var productRepository: ProductRepository? = null
    private var providerRepository: ProviderRepository? = null
    private var historyRepository: HistoryRepository? = null

    fun categoryRepository(): CategoryRepository {
        if (categoryRepository == null) {
            categoryRepository = CategoryRepositoryImpl()
        }
        return categoryRepository!!
    }

    fun productRepository(): ProductRepository {
        if (productRepository == null) {
            productRepository = ProductRepositoryImpl()
        }
        return productRepository!!
    }

    fun providerRepository(): ProviderRepository {
        if (providerRepository == null) {
            providerRepository = ProviderRepositoryImpl()
        }
        return providerRepository!!
    }


    fun historyRepository(): HistoryRepository {
        if (historyRepository == null) {
            historyRepository = HistoryRepositoryImpl()
        }
        return historyRepository!!
    }
}