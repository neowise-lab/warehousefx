package com.neowise.warehouse.ui.history

import com.neowise.warehouse.app.App.Companion.DARK_THEME
import com.neowise.warehouse.data.entities.History
import com.neowise.warehouse.data.entities.Product
import com.neowise.warehouse.ui.base.Dialog
import com.neowise.warehouse.util.*
import javafx.fxml.FXML
import javafx.scene.control.*
import java.net.URL
import java.time.LocalDate
import java.util.*

class GiveOutDialog : Dialog("give-out", DARK_THEME) {

    @FXML
    private lateinit var title: Label
    @FXML
    private lateinit var volumeField: TextField
    @FXML
    private lateinit var unitField: TextField
    @FXML
    private lateinit var dateField: TextField
    @FXML
    private lateinit var consumerField: TextField

    private lateinit var onSave: (Product, Product, History) -> Unit
    private lateinit var product: Product

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        volumeField.textFormatter = TextFormatter(DoubleConverter())
    }

    fun show(product: Product, onSave: (Product, Product, History) -> Unit) {
        this.product = product
        this.onSave = onSave

        volumeField.clear()
        unitField.text = product.unit.text
        dateField.text = LocalDate.now().toString()
        consumerField.clear()
        window.show()
    }

    @FXML
    private fun save() {
        try {
            val volume = volumeField.getFieldDouble()
            val stringVolume = "- ${doubleAsString(volume)} ${product.unit}"
            val date = LocalDate.now().toString()
            val appointment = consumerField.getFieldString()

            onSave(
                product,
                product.copy(volume = product.volume - volume),
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