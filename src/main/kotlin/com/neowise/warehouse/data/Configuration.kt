package com.neowise.warehouse.data

import com.neowise.warehouse.app.App
import com.neowise.warehouse.util.logger.Logger
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*

object Configuration {

    private val logger = Logger.getLogger(Configuration::class)

    private val properties = Properties()
    private lateinit var path: String

    fun load(path: String) {
        Configuration.path = path
        val file = File(path)
        if (!file.exists()) file.createNewFile()
        properties.load(FileInputStream(path))
    }

    var firstRun: Boolean
        get() = properties.getProperty("first_run", "true")!!.toBoolean()
        set(value) {
            properties.setProperty("first_run", value.toString())
            properties.store(FileOutputStream(path), "")
        }

    var databasePath: String
        get() = properties.getProperty("database", "")!!
        set(value) {
            properties.setProperty("database", value)
            properties.store(FileOutputStream(path), "")
        }

    fun createDatabase(newPath: File): Boolean {
        return try {
            if (newPath.exists()) newPath.delete()
            newPath.createNewFile()
            copyDatabase(newPath)
            reconnectDatabase(newPath)
            true
        } catch (e: Exception) {
            logger.log("Error on create database: $newPath", e)
            false
        }
    }

    fun changeDatabase(newPath: File): Boolean {
        return try {
            reconnectDatabase(newPath)
            true
        } catch (e: ConnectorException) {
            logger.log("Error on change database: $newPath", e)
            false
        }
    }

    private fun reconnectDatabase(path: File) {
        return try {
            databasePath = path.absolutePath
            DatabaseConnector.connect(path.absolutePath)
        } catch (e: Exception) {
            logger.log("Error on change database: ${path.absolutePath}", e)
            throw ConnectorException(e)
        }
    }

    private fun copyDatabase(path: File) {
        try {
            val input = App::class.java.getResourceAsStream("/.database")
            val bytes = ByteArray(input.available())
            input.read(bytes)
            input.close()

            val output = FileOutputStream(path)
            output.write(bytes)
            output.flush()
            output.close()
        }
        catch (e: Exception) {
            throw e
        }
    }

    fun connectDatabase(): Boolean {
        return try {
            DatabaseConnector.connect(databasePath)
            true
        } catch (e: Exception) {
            logger.log("Error on connect database: $databasePath", e)
            false
        }
    }
}