package com.neowise.warehouse.data.repository

import com.neowise.warehouse.data.DatabaseConnector

private const val LAST_ID_QUERY = "SELECT last_insert_rowid() FROM"

internal fun lastId(table: String): Int {
    val statement = DatabaseConnector.connection.prepareStatement("$LAST_ID_QUERY $table")
    val resultSet = statement.executeQuery()
    val id = resultSet.getInt(1)
    resultSet.close()
    statement.close()
    return id
}