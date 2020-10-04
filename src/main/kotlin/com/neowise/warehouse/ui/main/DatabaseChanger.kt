package com.neowise.warehouse.ui.main

import com.neowise.warehouse.app.App.Companion.DARK_THEME
import com.neowise.warehouse.data.common.DBSource
import com.neowise.warehouse.ui.base.Dialog
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.stage.FileChooser
import java.io.File
import java.net.URL
import java.util.*

class DatabaseChanger : Dialog("change-db", DARK_THEME) {

    @FXML
    private lateinit var changeOptionList: ListView<DBSource>
    private lateinit var fileChooser: FileChooser

    var onOpen: (file: File) -> Unit = {}
    var onCreate: (file: File) -> Unit = {}
    var onCancel: () -> Unit = {}

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        changeOptionList.items.addAll(DBSource.values())
        changeOptionList.selectionModel.select(0)
        fileChooser = FileChooser()
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Файлы базы данных", "*.wdb"))
    }

    @FXML
    private fun select() {
        val item = changeOptionList.selectionModel.selectedItem!!
        fileChooser.title = item.text
        when(item) {
            DBSource.OPEN -> {
                val file = fileChooser.showOpenDialog(window)
                if (file != null) {
                    onOpen(file)
                    window.close()
                }
            }
            DBSource.CREATE_NEW -> {
                val file = fileChooser.showSaveDialog(window)
                if (file != null) {
                    onCreate(file)
                    window.close()
                }
            }
        }
    }


    @FXML
    private fun cancel() {
        window.close()
        onCancel()
    }
}