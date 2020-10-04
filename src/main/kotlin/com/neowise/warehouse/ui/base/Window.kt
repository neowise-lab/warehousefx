package com.neowise.warehouse.ui.base

import com.neowise.warehouse.app.App.Companion.APP_ICON
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.effect.BlurType
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle

open class Window: Stage(StageStyle.TRANSPARENT) {

    private var initX = .0
    private var initY = .0

    fun initContent(content: Parent) {
        content.effect = DropShadow(8.0, Color.color(.0, .0, .0, .5))
        val container = StackPane(content)
        container.background = Background(BackgroundFill(Color.TRANSPARENT, null, null))
        container.padding = Insets(10.0)
        scene = Scene(container, Color.TRANSPARENT)
        icons.add(Image(Window::class.java.getResourceAsStream(APP_ICON)))
    }

    fun initDraggable(parent: Parent) {
        parent.onMousePressed = EventHandler {
            initX = it.sceneX
            initY = it.sceneY
        }

        parent.onMouseDragged = EventHandler {
            x = it.screenX - initX
            y = it.screenY - initY
        }
    }
}