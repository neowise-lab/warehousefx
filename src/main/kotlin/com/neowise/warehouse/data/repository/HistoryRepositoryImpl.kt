package com.neowise.warehouse.data.repository

import com.neowise.warehouse.data.DatabaseConnector
import com.neowise.warehouse.data.entities.History
import com.neowise.warehouse.data.entities.Product
import com.neowise.warehouse.util.logger.Logger
import java.sql.SQLException

class HistoryRepositoryImpl : HistoryRepository {

    companion object {
        private const val TABLE = "History"
        private const val FETCH_QUERY = "SELECT * FROM $TABLE where product = ?"
        private const val CREATE_QUERY = "INSERT INTO $TABLE(appointment, volume, date, product) VALUES(?, ?, ?, ?)"
    }

    private val logger = Logger.getLogger(HistoryRepositoryImpl::class)

    override fun fetch(s: Product) : List<History> {
        val items = mutableListOf<History>()
        try {
            val statement = DatabaseConnector.connection.prepareStatement(FETCH_QUERY)
            statement.setInt(1, s.id)

            val resultSet = statement.executeQuery()
            while(resultSet.next()) {
                items += History(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
                )
            }
            resultSet.close()
        }
        catch (e: SQLException) {
            logger.log("Error on fetch histories of product: #${s.id} ${s.name}", e)
        }
        return items
    }

    override fun create(item: History, s: Product): Boolean {
        return try {
            val statement = DatabaseConnector.connection.prepareStatement(CREATE_QUERY)
            statement.setString(1, item.appointment)
            statement.setString(2, item.volume)
            statement.setString(3, item.date)
            statement.setInt(4, s.id)
            statement.execute()
            statement.close()
            true
        } catch (e: SQLException) {
            logger.log("Error on create history for product: #${s.id} ${s.name}", e)
            false
        }
    }
}