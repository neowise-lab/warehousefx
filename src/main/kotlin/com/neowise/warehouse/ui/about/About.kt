package com.neowise.warehouse.ui.about

import com.neowise.warehouse.app.App.Companion.DARK_THEME
import com.neowise.warehouse.ui.base.Dialog
import javafx.fxml.FXML
import java.net.URL
import java.util.*

class About: Dialog("about", DARK_THEME) {

    override fun initialize(location: URL?, resources: ResourceBundle?) {
    }

    @FXML
    private fun close() {
        window.close()
    }
}