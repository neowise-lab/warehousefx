package com.neowise.warehouse.data.repository

import com.neowise.warehouse.data.DatabaseConnector
import com.neowise.warehouse.data.entities.Provider
import com.neowise.warehouse.util.logger.Logger
import java.sql.SQLException

class ProviderRepositoryImpl : ProviderRepository {

    companion object {
        private const val TABLE = "Providers"
        private const val FETCH_QUERY = "SELECT * FROM $TABLE"
        private const val CREATE_QUERY = "INSERT INTO $TABLE(name) VALUES(?)"
        private const val UPDATE_QUERY = "UPDATE $TABLE SET name = ? WHERE id = ?"
        private const val REMOVE_QUERY = "DELETE FROM $TABLE WHERE id = ?"
    }

    private val logger = Logger.getLogger(ProviderRepositoryImpl::class)

    override fun fetch() : List<Provider> {
        val items = mutableListOf<Provider>()
        try {
            val statement = DatabaseConnector.connection.prepareStatement(FETCH_QUERY)
            val resultSet = statement.executeQuery()
            while(resultSet.next()) {
                items += Provider(
                    resultSet.getInt(1),
                    resultSet.getString(2)
                )
            }
            resultSet.close()
        }
        catch (e: SQLException) {
            logger.log("Error on fetch providers", e)
        }
        return items
    }

    override fun create(item: Provider) : Provider {
        return try {
            val statement = DatabaseConnector.connection.prepareStatement(CREATE_QUERY)
            statement.setString(1, item.name)
            statement.execute()
            statement.close()
            item.copy(id = lastId(TABLE))
        } catch (e: SQLException) {
            logger.log("Error on create provider", e)
            Provider.WRONG_PROVIDER
        }
    }

    override fun update(target: Provider, new: Provider): Boolean {
        return try {
            val statement = DatabaseConnector.connection.prepareStatement(UPDATE_QUERY)
            statement.setString(1, new.name)
            statement.setInt(2, target.id)
            statement.execute()
            statement.close()
            true
        }
        catch (e: SQLException) {
            logger.log("Error on update provider", e)
            false
        }
    }

    override fun remove(target: Provider) : Boolean {
        return try {
            val statement = DatabaseConnector.connection.prepareStatement(REMOVE_QUERY)
            statement.setInt(1, target.id)
            statement.execute()
            statement.close()
            true
        }
        catch (e: SQLException) {
            logger.log("Error on remove provider", e)
            false
        }
    }
}