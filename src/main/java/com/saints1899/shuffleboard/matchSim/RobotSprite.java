package com.saints1899.shuffleboard.matchSim;

import com.saints1899.shuffleboard.matchSim.controls.RobotControl;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RobotSprite implements ISprite {
    private Position _position;
    private Node _ui;
    private double _width = 0;
    private double _length = 0;
    private double _heading = 0;

    public RobotSprite() {
        this(new Position(1340, 621), 66.04, 83.82);
    }

    public RobotSprite(Position initialPosition, double width, double length) {
        _position = initialPosition;

        String path = getClass().getResource("controls/robot.png").toString();
        Image img = new Image(path);
        ImageView robotImageView = new ImageView(img);

        _ui = robotImageView;

        _width = width;
        _length = length;
        _ui.prefWidth(width);
        _ui.prefHeight(length);
    }

    @Override
    public Position getPosition() {
        return _position;
    }

    @Override
    public void move(Position position) {
        _position = position;
    }

    @Override
    public void rotate(double degrees) {
        double heading = Utils.rotate(_heading, degrees);
        this.setHeading(heading);
    }

    public void setHeading(double degrees) {
        _heading = degrees;
    }

    @Override
    public double getHeading() {
        return _heading;
    }

    @Override
    public void translate(int x, int y) {
        _ui.setTranslateX(x);
        _ui.setTranslateY(y);
    }

    @Override
    public Node getUI() {
        return _ui;
    }

    public void buildRobot() {

    }

    @Override
    public double getLength() {
        return _length;
    }

    @Override
    public double getWidth() {
        return _width;
    }

}