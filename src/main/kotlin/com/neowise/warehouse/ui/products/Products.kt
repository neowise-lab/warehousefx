package com.neowise.warehouse.ui.products

import com.neowise.warehouse.app.App.Companion.DARK_THEME
import com.neowise.warehouse.data.entities.Category
import com.neowise.warehouse.data.entities.Product
import com.neowise.warehouse.data.repository.RepositoryFactory
import com.neowise.warehouse.ui.base.FXMLView
import com.neowise.warehouse.ui.history.GiveOutDialog
import com.neowise.warehouse.ui.history.ReceiveDialog
import com.neowise.warehouse.util.doubleAsString
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.util.Callback
import java.net.URL
import java.util.*

class Products : FXMLView("product-list", DARK_THEME) {

    @FXML
    private lateinit var table: TableView<Product>

    @FXML
    private lateinit var nameColumn: TableColumn<Product, String>
    @FXML
    private lateinit var descriptionColumn: TableColumn<Product, String>
    @FXML
    private lateinit var volumeColumn: TableColumn<Product, Double>
    @FXML
    private lateinit var unitColumn: TableColumn<Product, String>

    @FXML
    private lateinit var addButton: Button
    @FXML
    private lateinit var editButton: Button
    @FXML
    private lateinit var receiveButton: Button
    @FXML
    private lateinit var giveOutButton: Button

    lateinit var onProductChange: (Product?) -> Unit

    private val productRepository = RepositoryFactory.productRepository()
    private val historyRepository = RepositoryFactory.historyRepository()

    private val providers = Providers()
    private val productEditor = ProductEditor()
    private val giveOut = GiveOutDialog()
    private val receive = ReceiveDialog()

    private var category: Category? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        onProductChange = { println("Do nothing :)") }

        nameColumn.cellValueFactory = PropertyValueFactory("name")
        descriptionColumn.cellValueFactory = PropertyValueFactory("description")
        unitColumn.cellValueFactory = PropertyValueFactory("unit")
        volumeColumn.cellValueFactory = PropertyValueFactory("volume")

        volumeColumn.cellFactory = Callback {
            object : TableCell<Product, Double>() {
                override fun updateItem(item: Double?, empty: Boolean) {
                    super.updateItem(item, empty)
                    text = if (empty || item == null) "" else doubleAsString(item)
                }
            }
        }

        table.selectionModel.selectedItemProperty().addListener { _, _, value ->
            onProductChange(value)

            receiveButton.isDisable = (value == null)
            giveOutButton.isDisable = (value == null)
        }
    }

    fun categoryChanged(category: Category?) {
        this.category = category
        if (category == null) {
            table.items.clear()
        }
        else {
            table.items.setAll(productRepository.fetch(category))
            addButton.isDisable = category == Category.ALL_CATEGORY
        }
    }

    @FXML
    private fun add(event: ActionEvent) {
        if (category != null && category != Category.ALL_CATEGORY) {
            productEditor.setCategory(category!!)
            productEditor.showAsCreate(onCreate = {
                val item = productRepository.create(it, category!!)
                if (item != Product.WRONG_PRODUCT) add(item)
            })
        }
    }

    @FXML
    private fun edit(event: ActionEvent) {
        val item = table.selectionModel.selectedItem
        if (item != null) {
            productEditor.showAsEdit(item,
                onEdit = { old, new ->
                    if (productRepository.update(old, new))
                    replace(old, new)
                },
                onDelete = {
                    if (productRepository.remove(it))
                    remove(it)
                })
        }
    }

    @FXML
    private fun showProviders(event: ActionEvent) {
        providers.show()
    }

    @FXML
    private fun receive(event: ActionEvent) {
        val item = table.selectionModel.selectedItem
        if (item != null) {
            receive.show(item, onSave = { old, new, history ->
                if (historyRepository.create(history, new)) {
                    productRepository.update(old, new)
                    replace(old, new)
                }
            })
        }
    }

    @FXML
    private fun giveOut(event: ActionEvent) {
        val item = table.selectionModel.selectedItem
        if (item != null) {
            giveOut.show(item, onSave = { old, new, history ->
                if (historyRepository.create(history, new)) {
                    productRepository.update(old, new)
                    replace(old, new)
                }
            })
        }
    }

    private fun add(product: Product) {
        table.items.add(product)
    }

    private fun remove(product: Product) {
        table.items.remove(product)
    }

    private fun replace(old: Product, new: Product) {
        val index = table.items.indexOf(old)
        table.items[index] = new
        table.selectionModel.select(index)
    }
}