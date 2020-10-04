package com.neowise.warehouse.ui.error

import com.neowise.warehouse.app.App
import com.neowise.warehouse.ui.base.Dialog
import com.neowise.warehouse.util.logger.Log
import com.neowise.warehouse.util.logger.Logger
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.stage.FileChooser
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.util.*

class ErrorViewer : Dialog("error-viewer", App.DARK_THEME) {

    @FXML
    private lateinit var message: Label
    @FXML
    private lateinit var errorField: TextArea

    private var log: Log? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
    }

    fun show(log: Log) {
        this.log = log
        this.message.text = log.message
        this.errorField.text = log.log
        show()
    }

    @FXML
    private fun saveForReport(event: ActionEvent) {

        log?.let {

            val chooser = FileChooser()
            chooser.extensionFilters.add(FileChooser.ExtensionFilter("Log files", "*.log"))
            chooser.initialFileName = "WAREHOUSE_LOG_${it.dateTime.replace(':', '.')}"
            val file = chooser.showSaveDialog(window)
            if (file != null) {
                try {
                    if (file.exists()) file.delete()
                    file.createNewFile()
                    val fos = FileOutputStream(file)
                    fos.write(it.log.toByteArray())
                    fos.flush()
                    fos.close()

                    window.close()
                }
                catch (e: IOException) {
                    Logger.getLogger(ErrorViewer::class).log("Error on save log")
                }
            }
        }
    }

    @FXML
    private fun cancel() {
        window.close()
    }
}