package com.neowise.warehouse.app

import com.neowise.warehouse.data.Configuration
import com.neowise.warehouse.ui.error.ErrorViewer
import com.neowise.warehouse.ui.main.DatabaseChanger
import com.neowise.warehouse.ui.main.Main
import com.neowise.warehouse.util.logger.Logger
import javafx.application.Application
import javafx.stage.Stage
import java.io.File
import kotlin.system.exitProcess

class App : Application() {

    companion object {
        const val DARK_THEME = "/css/dark.css"
        const val APP_ICON = "/icon.png"
    }

    override fun start(primaryStage: Stage) {

        Configuration.load(File(".config").absolutePath)

        val errorViewer = ErrorViewer()

        Logger.onError { log ->
            errorViewer.show(log)
        }

        if (Configuration.firstRun) {
            Configuration.firstRun = false
        }
        else {
            runApp()
        }
    }

    private fun runApp() {
        if (Configuration.databasePath != "") {
            showMain()
        }
        else {
            showDatabaseChanger()
        }
    }

    fun showMain() {
        if (Configuration.connectDatabase()) {
            Main().show()
        }
    }

    fun showDatabaseChanger() {
        val databaseChanger = DatabaseChanger()
        databaseChanger.onOpen = {
            Configuration.changeDatabase(it)
            Main().show()
        }
        databaseChanger.onCreate = {
            Configuration.createDatabase(it)
            Main().show()
        }
        databaseChanger.onCancel = {
            exitProcess(0)
        }
        databaseChanger.show()
    }
}
