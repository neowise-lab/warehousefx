package com.neowise.warehouse.ui.categories

import com.neowise.warehouse.app.App.Companion.DARK_THEME
import com.neowise.warehouse.data.common.DialogMode
import com.neowise.warehouse.data.entities.Category
import com.neowise.warehouse.ui.base.Dialog
import com.neowise.warehouse.util.IllegalFieldValueException
import com.neowise.warehouse.util.getFieldString
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import java.net.URL
import java.util.*

class CategoryEditor: Dialog("category-editor", DARK_THEME) {

    @FXML
    private lateinit var title: Label
    @FXML
    private lateinit var nameField: TextField
    @FXML
    private lateinit var deleteButton: Button

    private lateinit var current: Category

    private lateinit var onCreate: (Category) -> Unit
    private lateinit var onEdit: (Category, Category) -> Unit
    private lateinit var onDelete: (Category) -> Unit

    private var mode: DialogMode = DialogMode.CREATE

    override fun initialize(location: URL?, resources: ResourceBundle?) {
    }


    fun showAsCreate(onCreate: (Category) -> Unit) {
        this.onCreate = onCreate

        title.text = "Новая категория"
        nameField.text = ""
        deleteButton.isVisible = false
        mode = DialogMode.CREATE
        show()
    }

    fun showAsEdit(category: Category, onEdit: (Category, Category) -> Unit, onDelete: (Category) -> Unit) {
        this.onEdit = onEdit
        this.onDelete = onDelete
        this.current = category

        title.text = "Изменить категорию"
        current = category
        nameField.text = category.name
        deleteButton.isVisible = true
        mode = DialogMode.EDIT
        show()
    }

    @FXML
    private fun cancel() {
        window.close()
    }

    @FXML
    private fun save() {
        try {
            val name = nameField.getFieldString()
            when(mode) {
                DialogMode.CREATE -> onCreate(Category(id = -1, name = name))
                DialogMode.EDIT -> onEdit(current, current.copy(name = name))
            }
            window.close()
        }
        catch(e: IllegalFieldValueException) {}
    }

    @FXML
    private fun delete() {
        onDelete(current)
        window.close()
    }
}