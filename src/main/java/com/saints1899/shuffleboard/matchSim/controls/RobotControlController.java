package com.saints1899.shuffleboard.matchSim.controls;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

public class RobotControlController implements Initializable {

    @FXML
    private Rectangle body;

    @FXML
    private Rectangle front;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

    }

    public Rectangle getBody() {
        return this.body;
    }

    public Rectangle getFront() {
        return this.front;
    }

}
