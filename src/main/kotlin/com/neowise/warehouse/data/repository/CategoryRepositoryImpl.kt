package com.neowise.warehouse.data.repository

import com.neowise.warehouse.data.DatabaseConnector
import com.neowise.warehouse.data.entities.Category
import com.neowise.warehouse.util.logger.Logger
import java.sql.SQLException

class CategoryRepositoryImpl : CategoryRepository {

    private val logger = Logger.getLogger(CategoryRepositoryImpl::class)

    init {
        println("init repository")
    }
    companion object {
        private const val TABLE = "Categories"
        private const val FETCH_QUERY = "SELECT * FROM $TABLE"
        private const val CREATE_QUERY = "INSERT INTO $TABLE(name) VALUES(?)"
        private const val UPDATE_QUERY = "UPDATE $TABLE SET name = ? WHERE id = ?"
        private const val REMOVE_QUERY = "DELETE FROM $TABLE WHERE id = ?"
    }

    override fun fetch() : List<Category> {
        val items = mutableListOf<Category>()
        try {
            val statement = DatabaseConnector.connection.prepareStatement(FETCH_QUERY)
            val resultSet = statement.executeQuery()
            while(resultSet.next()) {
                items += Category(
                    resultSet.getInt(1),
                    resultSet.getString(2)
                )
            }
            resultSet.close()
        }
        catch (e: SQLException) {
            logger.log("Error on fetch category data", e)
        }
        return items
    }

    override fun create(item: Category) : Category {
        return try {
            val statement = DatabaseConnector.connection.prepareStatement(CREATE_QUERY)
            statement.setString(1, item.name)
            statement.execute()
            statement.close()
            item.copy(id = lastId(TABLE))
        } catch (e: SQLException) {
            logger.log("Error on create category", e)
            Category.WRONG
        }
    }

    override fun update(target: Category, new: Category) : Boolean {
        return try {
            val statement = DatabaseConnector.connection.prepareStatement(UPDATE_QUERY)
            statement.setString(1, new.name)
            statement.setInt(2, target.id)
            statement.execute()
            statement.close()
            true
        } catch (e: SQLException) {
            logger.log("Error on update category", e)
            false
        }
    }

    override fun remove(target: Category) : Boolean {
        return try {
            val statement = DatabaseConnector.connection.prepareStatement(REMOVE_QUERY)
            statement.setInt(1, target.id)
            statement.execute()
            statement.close()
            true
        } catch (e: SQLException) {
            logger.log("Error on remove category", e)
            false
        }
    }
}