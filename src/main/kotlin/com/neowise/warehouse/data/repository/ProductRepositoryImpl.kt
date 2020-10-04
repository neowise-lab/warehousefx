package com.neowise.warehouse.data.repository

import com.neowise.warehouse.data.DatabaseConnector
import com.neowise.warehouse.data.common.Units
import com.neowise.warehouse.data.entities.Category
import com.neowise.warehouse.data.entities.Product
import com.neowise.warehouse.util.logger.Logger
import java.sql.PreparedStatement
import java.sql.SQLException

class ProductRepositoryImpl : ProductRepository {

    companion object {
        private const val TABLE = "Products"
        private const val FETCH_QUERY_ALL = "SELECT * FROM $TABLE"
        private const val FETCH_QUERY = "SELECT * FROM $TABLE WHERE category = ?"
        private const val CREATE_QUERY = "INSERT INTO $TABLE(name, desc, volume, unit, category) VALUES(?, ?, ?, ?, ?)"
        private const val UPDATE_QUERY = "UPDATE $TABLE SET name = ?, desc = ?, volume = ?, unit = ? WHERE id = ?"
        private const val REMOVE_QUERY = "DELETE FROM $TABLE WHERE id = ?"
    }

    private val logger = Logger.getLogger(ProductRepositoryImpl::class)

    override fun fetch(s: Category) : List<Product> {
        return try {
            val statement = if (s == Category.ALL_CATEGORY) {
                DatabaseConnector.connection.prepareStatement(FETCH_QUERY_ALL)
            } else {
                val statement = DatabaseConnector.connection.prepareStatement(FETCH_QUERY)
                statement.setInt(1, s.id)
                statement
            }
            fetch(statement)
        } catch (e: SQLException) {
            logger.log("Error on fetch products in category: #${s.id} ${s.name}", e)
            listOf()
        }
    }

    private fun fetch(statement: PreparedStatement) : List<Product> {
        val resultSet = statement.executeQuery()
        val items = mutableListOf<Product>()
        while(resultSet.next()) {
            items.add(Product(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getDouble(4),
                Units.valueOf(resultSet.getString(5))
            ))
        }
        resultSet.close()
        return items
    }

    override fun create(item: Product, s: Category) : Product {
        try {
            val statement = DatabaseConnector.connection.prepareStatement(CREATE_QUERY)
            statement.setString(1, item.name)
            statement.setString(2, item.description)
            statement.setDouble(3, item.volume)
            statement.setString(4, item.unit.name)
            statement.setInt(5, s.id)
            statement.execute()
            statement.close()

            return item.copy(id = lastId(TABLE))
        }
        catch (e: SQLException) {
            logger.log("Error on create product in category: #${s.id} ${s.name}", e)
            return Product.WRONG_PRODUCT
        }
    }

    override fun update(target: Product, new: Product): Boolean {
        return try {
            val statement = DatabaseConnector.connection.prepareStatement(UPDATE_QUERY)
            statement.setString(1, new.name)
            statement.setString(2, new.description)
            statement.setDouble(3, new.volume)
            statement.setString(4, new.unit.name)
            statement.setInt(5, target.id)
            statement.execute()
            statement.close()
            false
        } catch (e: SQLException) {
            logger.log("Error on update product: #${target.id} ${target.name}", e)
            false
        }
    }

    override fun remove(target: Product) : Boolean {
        return try {
            val statement = DatabaseConnector.connection.prepareStatement(REMOVE_QUERY)
            statement.setInt(1, target.id)
            statement.execute()
            statement.close()
            true
        } catch (e: SQLException) {
            logger.log("Error on remove product: #${target.id} ${target.name}", e)
            true
        }
    }
}