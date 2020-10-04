package com.neowise.warehouse.ui.products

import com.neowise.warehouse.app.App.Companion.DARK_THEME
import com.neowise.warehouse.data.entities.Provider
import com.neowise.warehouse.data.repository.ProviderRepository
import com.neowise.warehouse.data.repository.RepositoryFactory
import com.neowise.warehouse.ui.base.Dialog
import com.neowise.warehouse.util.IllegalFieldValueException
import com.neowise.warehouse.util.getFieldString
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import java.net.URL
import java.util.*

class Providers : Dialog("provider-list", DARK_THEME) {

    @FXML
    private lateinit var providerList: ListView<Provider>
    @FXML
    private lateinit var nameField: TextField

    private lateinit var providerRepository: ProviderRepository

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        providerRepository = RepositoryFactory.providerRepository()

        providerList.selectionModel.selectedItemProperty().addListener { _, _, value ->
            if (value != null) selectedItem(value)
        }

        fetchData()
    }

    private fun fetchData() {
        providerList.items.setAll(providerRepository.fetch())
        providerList.selectionModel.select(0)
    }

    private fun selectedItem(value: Provider) {
        nameField.text = value.name
    }

    @FXML
    private fun create() {
        try {
            val name = nameField.getFieldString()
            val item = providerRepository.create(Provider(-1, name))
            if (item != Provider.WRONG_PROVIDER) providerList.items.add(item)
        }
        catch(e: IllegalFieldValueException) {}
    }

    @FXML
    private fun edit() {
        try {
            val target = providerList.selectionModel.selectedItem
            val index = providerList.selectionModel.selectedIndex

            val name = nameField.getFieldString()
            val new = target.copy(name = name)

            if (providerRepository.update(target, new)) {
                providerList.items[index] = new
            }
        }
        catch(e: IllegalFieldValueException) {}
    }

    @FXML
    private fun delete() {
        val target = providerList.selectionModel.selectedItem
        if(providerRepository.remove(target)) {
            providerList.items.remove(target)
        }
    }

    @FXML
    private fun close() {
        window.close()
    }
}