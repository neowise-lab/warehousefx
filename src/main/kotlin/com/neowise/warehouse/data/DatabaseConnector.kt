package com.neowise.warehouse.data

import org.sqlite.Function
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DatabaseConnector {

    var isConnected = false
    lateinit var connection: Connection

    fun connect(databasePath: String) {
        try {
            if (isConnected) connection.close()

            Class.forName("org.sqlite.JDBC")

            connection = DriverManager.getConnection("jdbc:sqlite:$databasePath")
            isConnected = true

            Function.create(connection, "hasMatch", object : Function() {
                override fun xFunc() {
                    if (args() != 2) {
                        throw SQLException("argument mismatch for function hasMatch(a, b)")
                    }
                    val targetString = super.value_text(0)
                    val searchInTarget = super.value_text(1)
                    val contains = targetString.toLowerCase().contains(searchInTarget.toLowerCase())
                    result(if (contains) 1 else 0)
                }
            })
        }
        catch (e: Exception) {
            throw ConnectorException(e)
        }
    }
}