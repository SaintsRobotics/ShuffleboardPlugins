package com.saints1899.shuffleboard.largetext;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableNumberValue;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;

@Description(
    dataTypes = String.class,
    name = "Large Text",
    summary = "Displays large text"
)
public class LargeTextWidget extends SimpleAnnotatedWidget<String> {
    private Pane view = new StackPane();

    public LargeTextWidget() {
        Label label = new Label();
        label.textProperty().bind(this.dataOrDefault);
        this.view.getChildren().add(label);
        label.setTextFill(Color.RED);
        
        ObservableNumberValue scale = Bindings.min(
            this.view.widthProperty().divide(label.widthProperty()),
            this.view.heightProperty().divide(label.heightProperty())
        );
        label.scaleXProperty().bind(scale);
        label.scaleYProperty().bind(scale);
    }

    @Override
    public Pane getView() {
        return this.view;
    }
}
