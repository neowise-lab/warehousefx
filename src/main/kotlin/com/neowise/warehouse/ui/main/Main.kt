package com.neowise.warehouse.ui.main

import com.neowise.warehouse.data.DatabaseConnector
import com.neowise.warehouse.ui.actionbar.ActionBar
import com.neowise.warehouse.ui.base.Window
import com.neowise.warehouse.ui.categories.Categories
import com.neowise.warehouse.ui.history.Histories
import com.neowise.warehouse.ui.products.Products
import javafx.scene.layout.BorderPane

class Main : Window() {

    private val actionBar = ActionBar()
    private val categories = Categories()
    private val products = Products()
    private val histories = Histories()

    init {
        initContent(content())
        initDraggable(actionBar.view)
        categories.onCategoryChange = products::categoryChanged
        products.onProductChange = histories::productChanged

        actionBar.onMinimizeListener = { isIconified = true }
        actionBar.onCloseListener = ::close

        actionBar.onChangeDatabase = {
            categories.fetchData()
        }

        categories.fetchData()
    }

    private fun content() : BorderPane {
        val borderPane = BorderPane()
        borderPane.top = actionBar.view
        borderPane.left = categories.view
        borderPane.center = products.view
        borderPane.right = histories.view

        return borderPane
    }
}