package com.neowise.warehouse.ui.products

import com.neowise.warehouse.app.App.Companion.DARK_THEME
import com.neowise.warehouse.data.common.DialogMode
import com.neowise.warehouse.data.common.Units
import com.neowise.warehouse.data.entities.Category
import com.neowise.warehouse.data.entities.Product
import com.neowise.warehouse.ui.base.Dialog
import com.neowise.warehouse.util.*
import javafx.fxml.FXML
import javafx.scene.control.*
import java.net.URL
import java.util.*

class ProductEditor : Dialog("product-editor", DARK_THEME) {

    @FXML
    private lateinit var title: Label
    @FXML
    private lateinit var nameField: TextField
    @FXML
    private lateinit var descriptionField: TextField
    @FXML
    private lateinit var volumeField: TextField
    @FXML
    private lateinit var deleteButton: Button
    @FXML
    private lateinit var unitsCombo: ComboBox<Units>

    private lateinit var onCreate: (Product) -> Unit
    private lateinit var onEdit: (Product, Product) -> Unit
    private lateinit var onDelete: (Product) -> Unit

    private lateinit var category: Category
    private var current: Product? = null

    private var mode = DialogMode.CREATE

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        volumeField.textFormatter = TextFormatter(DoubleConverter())
        unitsCombo.items.addAll(Units.values())
        unitsCombo.selectionModel.select(0)
    }

    fun setCategory(category: Category) {
        this.category = category
    }

    fun showAsEdit(product: Product, onEdit: (Product, Product) -> Unit, onDelete: (Product) -> Unit) {

        this.current = product
        this.onEdit = onEdit
        this.onDelete = onDelete

        title.text = "Изменить продукт"
        nameField.text = product.name
        descriptionField.text = product.description
        volumeField.text = product.volume.toString()
        deleteButton.isVisible = true
        unitsCombo.selectionModel.select(product.unit)

        mode = DialogMode.EDIT
        show()
    }

    fun showAsCreate(onCreate: (Product) -> Unit) {
        this.onCreate = onCreate

        title.text = "Новый продукт в ${category.name}"
        nameField.clear()
        descriptionField.clear()
        volumeField.clear()
        unitsCombo.selectionModel.select(0)
        deleteButton.isVisible = false

        mode = DialogMode.CREATE
        show()
    }

    @FXML
    private fun save() {
        try {
            val name = nameField.getFieldString()
            val description = descriptionField.text
            val volume = volumeField.getFieldDouble()
            val unit = unitsCombo.getComboItem<Units>()

            val model = Product(current?.id ?: -1, name, description, volume, unit)

            when(mode) {
                DialogMode.CREATE -> onCreate(model)
                DialogMode.EDIT -> onEdit(current!!, model)
            }

            window.close()
        }
        catch (e: IllegalFieldValueException) {}
    }

    @FXML
    private fun cancel() {
        window.close()
    }

    @FXML
    private fun delete() {
        onDelete(current!!)
        window.close()
    }
}