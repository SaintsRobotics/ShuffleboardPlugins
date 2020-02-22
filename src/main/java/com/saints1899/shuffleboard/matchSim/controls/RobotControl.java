package com.saints1899.shuffleboard.matchSim.controls;

import java.io.IOException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class RobotControl extends AnchorPane {
    RobotControlController controller;

    ObjectProperty<Paint> bodyColor = new SimpleObjectProperty<>(Color.BLUE);
    ObjectProperty<Paint> frontColor = new SimpleObjectProperty<>(Color.RED);

    public RobotControl() {
        super();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Robot.fxml"));
            controller = new RobotControlController();
            loader.setController(controller);
            Node robot = loader.load();

            this.getChildren().add(robot);
            AnchorPane.setTopAnchor(robot, 0.0);
            AnchorPane.setBottomAnchor(robot, 0.0);
            AnchorPane.setLeftAnchor(robot, 0.0);
            AnchorPane.setRightAnchor(robot, 0.0);

            // controller.getBody().fillProperty().bind(this.bodyColor);
            // controller.getFront().fillProperty().bind(this.frontColor);

        } catch (Exception io) {

        }
    }

    // public void setWidth(double width) {
    // double acutalWidth = this.getWidth();

    // double scale = width / acutalWidth;
    // this.setScaleX(scale);
    // }

    // public void setHeight(double height) {
    // double actualHeight = this.getHeight();
    // double scale = height / actualHeight;
    // this.setScaleX(scale);
    // }
}