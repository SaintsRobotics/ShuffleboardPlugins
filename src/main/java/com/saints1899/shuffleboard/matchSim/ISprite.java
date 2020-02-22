package com.saints1899.shuffleboard.matchSim;

import javafx.scene.Node;

public interface ISprite {
    Position getPosition();

    double getLength();

    double getWidth();

    double getHeading();

    void setHeading(double heading);

    void move(Position position);

    void rotate(double degrees);

    void translate(int x, int y);

    Node getUI();
}