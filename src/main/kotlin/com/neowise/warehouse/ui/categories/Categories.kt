package com.neowise.warehouse.ui.categories

import com.neowise.warehouse.app.App.Companion.DARK_THEME
import com.neowise.warehouse.data.entities.Category
import com.neowise.warehouse.data.repository.CategoryRepository
import com.neowise.warehouse.data.repository.RepositoryFactory
import com.neowise.warehouse.ui.base.FXMLView
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.ListView
import java.net.URL
import java.util.*

class Categories : FXMLView("category-list", DARK_THEME) {

    private lateinit var categoryRepository: CategoryRepository
    private lateinit var categoryEditor: CategoryEditor

    @FXML
    private lateinit var categoryList: ListView<Category>

    lateinit var onCategoryChange: (Category?) -> Unit

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        categoryRepository = RepositoryFactory.categoryRepository()
        categoryEditor = CategoryEditor()
        onCategoryChange = { println("do nothing :)")}

        categoryList.selectionModel.selectedItemProperty().addListener { _, _, value ->
            onCategoryChange(value)
        }
    }

    fun fetchData() {
        val list = categoryRepository.fetch()
        categoryList.items.clear()
        categoryList.items.add(Category.ALL_CATEGORY)
        categoryList.items.addAll(list)
        categoryList.selectionModel.select(0)
    }

    @FXML
    private fun create(event: ActionEvent) {
        categoryEditor.showAsCreate(
            onCreate = { new ->
                val item = categoryRepository.create(new)
                if(item != Category.WRONG) {
                    categoryList.items.add(item)
                }
            }
        )
    }

    @FXML
    private fun edit(event: ActionEvent) {
        val item = categoryList.selectionModel.selectedItem

        if (item != null && item != Category.ALL_CATEGORY) {

            categoryEditor.showAsEdit(item,
                onEdit = { old, new ->
                    if (categoryRepository.update(old, new)) {
                        categoryList.items[categoryList.items.indexOf(old)] = new
                    }
                },
                onDelete = { cat ->
                    if (categoryRepository.remove(cat)) {
                        categoryList.items.remove(cat)
                    }
                }
            )
        }
    }
}