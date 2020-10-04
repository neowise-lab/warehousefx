package com.neowise.warehouse.ui.base

import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent

abstract class FXMLView(fxml: String, style: String) : Initializable {

    val view: Parent

    init {
        val loader = FXMLLoader()
        loader.location = this.javaClass.getResource("/fxml/$fxml.fxml")
        loader.setController(this)
        view = loader.load()
        view.stylesheets += style
    }
}