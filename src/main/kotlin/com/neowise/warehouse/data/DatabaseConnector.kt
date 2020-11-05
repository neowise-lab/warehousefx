package com.neowise.warehouse.data

import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager

object DatabaseConnector {

    var isConnected = false
    lateinit var connection: Connection

    fun connect(databasePath: String) {
        try {
            if (isConnected) connection.close()

            Class.forName("org.sqlite.JDBC")

            connection = DriverManager.getConnection("jdbc:sqlite:$databasePath")
            isConnected = true
        }
        catch (e: Exception) {
            throw ConnectorException(e)
        }
    }
}