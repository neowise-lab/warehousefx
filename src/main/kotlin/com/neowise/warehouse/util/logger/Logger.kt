package com.neowise.warehouse.util.logger

import java.io.FileWriter
import java.lang.Exception
import java.time.LocalDateTime
import kotlin.reflect.KClass

class Logger {

    companion object {

        var logPath = ".logs"

        private lateinit var onError: (log: Log) -> Unit

        fun onError(onError: (log: Log) -> Unit) {
            Companion.onError = onError
        }

        fun getLogger(clazz: KClass<*>) : Logger {
            return Logger(clazz)
        }
    }


    private val clazz: KClass<*>?

    private constructor() {
        clazz = null
    }

    private constructor(clazz: KClass<*>) {
        this.clazz = clazz
    }

    fun log(message: String, ex: Exception? = null) {
        val log = generateLog(message, ex)
        writeLog(log)
        println(message)
        ex?.let {
            onError(log)
            throw RuntimeException(it)
        }
    }

    private fun generateLog(message: String, ex: Exception?) : Log {
        val dateTime = LocalDateTime.now().toString()
        val stackTrace = ex?.let {
            buildString {
                it.stackTrace.forEach {
                    append(it.toString())
                    append(System.lineSeparator())
                }
            }
        }
        val log =  buildString {
            append("${System.lineSeparator()}-------------------------------- $dateTime -----------------------------------")
            append("${System.lineSeparator()} class: ${clazz?.qualifiedName}")
            append("${System.lineSeparator()} message: $message")
            append("${System.lineSeparator()} stacktrace: ${System.lineSeparator()}")
            append(stackTrace)
        }
        return Log(message, log, dateTime)
    }

    private fun writeLog(log: Log) {
        val writer = FileWriter(logPath, true)
        writer.append(log.log)
        writer.flush()
        writer.close()
    }
}