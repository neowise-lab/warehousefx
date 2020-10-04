package com.neowise.warehouse.ui.actionbar

import com.neowise.warehouse.app.App.Companion.DARK_THEME
import com.neowise.warehouse.data.Configuration
import com.neowise.warehouse.ui.about.About
import com.neowise.warehouse.ui.base.FXMLView
import com.neowise.warehouse.ui.main.DatabaseChanger
import javafx.event.ActionEvent
import javafx.fxml.FXML
import java.net.URL
import java.util.*

class ActionBar : FXMLView("action-bar", DARK_THEME) {

    lateinit var onCloseListener: () -> Unit
    lateinit var onChangeDatabase: () -> Unit
    lateinit var onMinimizeListener: () -> Unit
    private lateinit var databaseChanger: DatabaseChanger

    private val about = About()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        databaseChanger = DatabaseChanger()
    }

    @FXML
    private fun changeDB(event: ActionEvent) {
        databaseChanger.onOpen = {
            if (Configuration.changeDatabase(it)) {
                onChangeDatabase()
            }
        }
        databaseChanger.onCreate = {
            if (Configuration.createDatabase(it)) {
                onChangeDatabase()
            }
        }
        databaseChanger.show()
    }

    @FXML
    private fun showAbout(event: ActionEvent) {
        about.show()
    }

    @FXML
    private fun minimize(event: ActionEvent) {
        onMinimizeListener()
    }

    @FXML
    private fun close(event: ActionEvent) {
        onCloseListener()
    }
}