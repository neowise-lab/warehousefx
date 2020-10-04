package com.neowise.warehouse.ui.history

import com.neowise.warehouse.app.App.Companion.DARK_THEME
import com.neowise.warehouse.data.entities.History
import com.neowise.warehouse.data.entities.Product
import com.neowise.warehouse.data.repository.HistoryRepository
import com.neowise.warehouse.data.repository.RepositoryFactory
import com.neowise.warehouse.ui.base.FXMLView
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import java.net.URL
import java.util.*

class Histories : FXMLView("history-list", DARK_THEME) {

    @FXML
    private lateinit var table: TableView<History>

    @FXML
    private lateinit var appointmentColumn: TableColumn<History, String>
    @FXML
    private lateinit var volumeColumn: TableColumn<History, String>
    @FXML
    private lateinit var dateColumn: TableColumn<History, String>

    private lateinit var historyRepository: HistoryRepository

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        historyRepository = RepositoryFactory.historyRepository()

        appointmentColumn.cellValueFactory = PropertyValueFactory("appointment")
        volumeColumn.cellValueFactory = PropertyValueFactory("volume")
        dateColumn.cellValueFactory = PropertyValueFactory("date")
    }

    fun productChanged(product: Product?) {
        if (product == null) {
            table.items.clear()
        }
        else {
            table.items.setAll(historyRepository.fetch(product))
        }
    }
}