package com.saints1899.shuffleboard.matchSim;

import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class MatchField {
    public class Dimensions {
        int topY;
        int topX;
        int bottomX;
        int bottomY;
        int imageWidth;

        double fieldWidth;
        double fieldLength;

        public double fieldLengthInPixels() {
            return this.bottomX - this.topX;
        }

        public double fieldWidthInPixels() {
            return this.bottomY - this.topY;
        }

        ArrayList<double[]> Obstacles = new ArrayList<double[]>();
    }

    public Dimensions dimensions = new Dimensions();
    private ArrayList<ISprite> _sprites = new ArrayList<ISprite>();
    private ArrayList<ObstacleSprite> _obstacles = new ArrayList<ObstacleSprite>();
    private Pane _virtualField;
    private final double INCREMENTAL_MOVE_DISTANCE = 5;
    private final double INCREMENTAL_ROTATION = 5;
    private double _uiScale = 1;
    private double _pixelsToViewableRatio = 1;

    public MatchField(Pane virtualField) {
        _virtualField = virtualField;
        boolean use2020 = true;

        if (use2020) {
            this.dimensions.topX = 98;
            this.dimensions.topY = 27;
            this.dimensions.bottomX = 1038;
            this.dimensions.bottomY = 512;
            this.dimensions.imageWidth = 1134;

            // the following are in centimeters
            this.dimensions.fieldLength = 1598.295;
            this.dimensions.fieldWidth = 821.055;
        } else {
            this.dimensions.topX = 217;
            this.dimensions.topY = 40;
            this.dimensions.bottomX = 1372;
            this.dimensions.bottomY = 1875;
            this.dimensions.imageWidth = 1592;

            // the following are in centimeters
            this.dimensions.fieldLength = 1645.92;
            this.dimensions.fieldWidth = 822.96;
        }

        // define the top left edge
        this.dimensions.Obstacles.add(new double[] { 136, 29, 115, 21, 76, 126, 97, 134 });
        this.dimensions.Obstacles.add(new double[] { 98, 407, 135, 508, 116, 518, 77, 412 });
        this.dimensions.Obstacles.add(new double[] { 1000, 30, 1038, 132, 1058, 122, 1022, 24 });
        this.dimensions.Obstacles.add(new double[] { 1038, 407, 1000, 508, 1022, 515, 1058, 415 });

        // basic field boundaries too
        this.dimensions.Obstacles.add(new double[] { dimensions.topX, dimensions.topY, dimensions.topX - 10,
                dimensions.topY, dimensions.topX - 10, dimensions.bottomY, dimensions.topX, dimensions.bottomY });
        this.dimensions.Obstacles.add(new double[] { dimensions.topX, dimensions.topY, dimensions.bottomX,
                dimensions.topY, dimensions.bottomX, dimensions.topY - 10, dimensions.topX, dimensions.topY - 10 });

        this.dimensions.Obstacles
                .add(new double[] { dimensions.topX, dimensions.bottomY, dimensions.topX, dimensions.bottomY + 10,
                        dimensions.bottomX, dimensions.bottomY + 10, dimensions.bottomX, dimensions.bottomY });
        this.dimensions.Obstacles.add(new double[] { dimensions.bottomX, dimensions.topY, dimensions.bottomX + 10,
                dimensions.topY, dimensions.bottomX + 10, dimensions.bottomY, dimensions.bottomX, dimensions.bottomY });
    }

    public void setScale(double virtualWidth) {
        double metersToPixelRatio = (this.dimensions.bottomX - this.dimensions.topX) / this.dimensions.fieldLength;
        _pixelsToViewableRatio = virtualWidth / this.dimensions.imageWidth;
        _uiScale = metersToPixelRatio * _pixelsToViewableRatio;

        // as the scale has changed update all the sprites
        this.positionSprites();
        this.resizeSprites();
        this.addObstacles();
    }

    public double getScale() {
        return _uiScale;
    }

    public void addSprite(ISprite sprite) {
        // adjust the size based on the virtual field size
        _sprites.add(sprite);
        this.updateSpriteSize(sprite);
        _virtualField.getChildren().add(sprite.getUI());
    }

    public void addObstacles() {
        // remove if its already been added
        _virtualField.getChildren().removeIf(n -> {
            return n instanceof ObstacleSprite;
        });

        for (double[] obstacle : this.dimensions.Obstacles) {

            // update points to new points
            double[] scaledPoints = new double[obstacle.length];
            for (int i = 0; i < obstacle.length; i++) {
                scaledPoints[i] = obstacle[i] * _pixelsToViewableRatio;
            }
            ObstacleSprite sprite = new ObstacleSprite(scaledPoints);
            _virtualField.getChildren().add(sprite);
        }
    }

    public void positionSprites() {
        for (ISprite sprite : _sprites) {
            this.updateSpritePosition(sprite);
        }
    }

    public void resizeSprites() {
        for (ISprite sprite : _sprites) {
            this.updateSpriteSize(sprite);
        }
    }

    public void moveLeft(ISprite sprite) {
        sprite.rotate(-INCREMENTAL_ROTATION);
        this.moveRelativePosition(sprite, 0);
    }

    public void moveRight(ISprite sprite) {
        sprite.rotate(INCREMENTAL_ROTATION);
        this.moveRelativePosition(sprite, 0);
    }

    public void moveForward(ISprite sprite) {
        this.moveRelativePosition(sprite, INCREMENTAL_MOVE_DISTANCE);
    }

    public void moveBackward(ISprite sprite) {
        this.moveRelativePosition(sprite, -INCREMENTAL_MOVE_DISTANCE);
    }

    protected void moveRelativePosition(ISprite sprite, double distance) {
        double x = 0;
        double y = 0;
        double heading = sprite.getHeading();
        Position pos = sprite.getPosition();

        heading = Utils.rotate(heading, 90);
        if (heading == 90) {
            x = distance;
        } else {
            heading = Utils.rotate(heading, 270);
            double radianHeading = Math.toRadians(heading);
            y = Math.sin(radianHeading) * distance;
            x = Math.cos(radianHeading) * distance;

        }

        double newXPos = pos.getX() + x;
        double newYPos = pos.getY() + y;

        // constrain the movement
        if (newXPos < 0) {
            newXPos = 0;
        }
        if (newYPos < 0) {
            newYPos = 0;
        }
        this.move(sprite, newXPos, newYPos);
    }

    public void move(ISprite sprite, double x, double y) {
        this.move(sprite, x, y, sprite.getHeading());
    }

    public void move(ISprite sprite, double x, double y, double heading) {
        // check bounds
        // double trueFieldWidth = this.dimensions.fieldWidth - (sprite.getWidth() / 2);
        // double trueFieldLength = this.dimensions.fieldLength - (sprite.getLength() /
        // 2);
        // if (x <= 0)
        // x = 0;
        // if (x >= trueFieldLength)
        // x = trueFieldLength;
        // if (y <= 0)
        // y = 0;
        // if (y >= trueFieldWidth)
        // y = trueFieldWidth;

        _virtualField.getChildren().forEach(n -> {
            if (n instanceof ObstacleSprite) {
                Bounds b = sprite.getUI().getBoundsInParent();
                Rectangle tempShape = new Rectangle(b.getMinX(), b.getMinY(), b.getMaxX(), b.getMaxY());
                Shape intersect = Shape.intersect(tempShape, (Shape) n);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    return;
                }
            }
        });

        sprite.move(new Position(x, y));
        sprite.setHeading(heading);
        this.updateSpritePosition(sprite);
    }

    private void updateSpriteSize(ISprite sprite) {
        ImageView r = (ImageView) sprite.getUI();
        double width = sprite.getWidth() * _uiScale;
        double height = sprite.getLength() * _uiScale;
        r.setFitHeight(height);
        r.setFitWidth(width);
        // r.setHeight();

        // r.prefWidth(width);
        // r.prefHeight(height);
    }

    private void updateSpritePosition(ISprite sprite) {
        // check that we've not moved off the field!
        Position pos = sprite.getPosition();
        double x = pos.getX() * _uiScale;
        double y = pos.getY() * _uiScale;
        double heading = sprite.getHeading();

        // remap based on origin being bottom x, bottom y.
        x = (this.dimensions.fieldLengthInPixels() * _pixelsToViewableRatio) - x;
        y = (this.dimensions.fieldWidthInPixels() * _pixelsToViewableRatio) - y;

        // add the offset of the field and adjust for
        double xOffset = this.dimensions.topX * _pixelsToViewableRatio;
        double yOffset = this.dimensions.topY * _pixelsToViewableRatio;
        Node ui = sprite.getUI();
        ui.setTranslateX((int) (x + xOffset));
        ui.setTranslateY((int) (y + yOffset));
        ui.setRotate(Utils.rotate(heading, 270));

    }
}