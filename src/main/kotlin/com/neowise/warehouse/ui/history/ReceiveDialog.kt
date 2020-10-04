package com.neowise.warehouse.ui.history

import com.neowise.warehouse.app.App.Companion.DARK_THEME
import com.neowise.warehouse.data.entities.History
import com.neowise.warehouse.data.entities.Product
import com.neowise.warehouse.data.entities.Provider
import com.neowise.warehouse.data.repository.ProviderRepository
import com.neowise.warehouse.data.repository.RepositoryFactory
import com.neowise.warehouse.ui.base.Dialog
import com.neowise.warehouse.util.*
import javafx.fxml.FXML
import javafx.scene.control.*
import java.net.URL
import java.time.LocalDate
import java.util.*

class ReceiveDialog : Dialog("receive", DARK_THEME) {

    @FXML
    private lateinit var title: Label
    @FXML
    private lateinit var volumeField: TextField
    @FXML
    private lateinit var unitField: TextField
    @FXML
    private lateinit var dateField: TextField
    @FXML
    private lateinit var providerCombo: ComboBox<Provider>

    private lateinit var providerRepository: ProviderRepository

    private lateinit var onSave: (Product, Product, History) -> Unit
    private lateinit var product: Product

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        providerRepository = RepositoryFactory.providerRepository()

        volumeField.textFormatter = TextFormatter(DoubleConverter())

        providerCombo.items.addAll(providerRepository.fetch())
        providerCombo.selectionModel.select(0)
    }

    fun show(product: Product, onSave: (Product, Product, History) -> Unit) {
        this.product = product
        this.onSave = onSave

        volumeField.clear()
        unitField.text = product.unit.text
        dateField.text = LocalDate.now().toString()
        providerCombo.editor.clear()
        providerCombo.selectionModel.select(0)
        window.show()
    }

    @FXML
    private fun save() {
        try {
            val volume = volumeField.getFieldDouble()
            val stringVolume = "+ ${doubleAsString(volume)} ${product.unit}"
            val date = LocalDate.now().toString()
            val appointment = providerCombo.editor.getFieldString()

            onSave(
                product,
                product.copy(volume = product.volume + volume),
                History(-1, product.id, appointment, stringVolume, date)
            )
            window.close()
        }
        catch (e: IllegalFieldValueException) {}
    }

    @FXML
    private fun cancel() {
        window.close()
    }
}