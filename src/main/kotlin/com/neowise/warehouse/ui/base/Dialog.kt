package com.neowise.warehouse.ui.base

import javafx.stage.Modality

abstract class Dialog(fxml: String, style: String) : FXMLView(fxml, style) {

    protected val window = Window()

    init {
        window.initContent(view)
        window.initDraggable(view)
        window.initModality(Modality.APPLICATION_MODAL)
    }

    fun show() {
        window.show()
    }
}