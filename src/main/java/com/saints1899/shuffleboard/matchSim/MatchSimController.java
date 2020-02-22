package com.saints1899.shuffleboard.matchSim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import edu.wpi.first.shuffleboard.api.data.MapData;
import edu.wpi.first.shuffleboard.api.data.types.MapType;
import edu.wpi.first.shuffleboard.api.widget.*;
import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

@Description(dataTypes = { MapType.class }, name = "Match Simulator", summary = "Tracks the robot on the match field.")
@ParametrizedController(value = "MatchSim.fxml")
public class MatchSimController extends SimpleAnnotatedWidget<MapData>
        implements Initializable, ChangeListener<MapData> {
    private MatchField _matchField;
    private RobotSprite _robot;
    private KeyEvent _currentKeyPressed;
    public BooleanProperty swapOrientation;

    @FXML
    private BorderPane _root;

    @FXML
    private Pane virtualField;

    @FXML
    private ImageView fieldBackground;

    @FXML
    private Label locationValue;

    @FXML
    private Label rotationValue;

    /**
     * Due to the nature of this class the getView should be used like a traditional
     * constructor The @FXML properties won't be valid until this method is called.
     */
    @Override
    public Pane getView() {
        return _root;
    }

    private void mainLoop() {
        this.processKeyPress();
        this.updateRobotLocation();
    }

    private void updateRobotLocation() {
        this.locationValue.setText(_robot.getPosition().toString());
        this.rotationValue.setText(Double.toString(_robot.getHeading()));
    }

    private void processKeyPress() {
        if (_currentKeyPressed == null)
            return;

        switch (_currentKeyPressed.getCode()) {
        case A:
            _matchField.moveLeft(_robot);
            break;
        case D:
            _matchField.moveRight(_robot);
            break;
        case W:
            _matchField.moveForward(_robot);
            break;
        case S:
            _matchField.moveBackward(_robot);
            break;

        default:
            break;
        }
        _matchField.positionSprites();
        // now reset it
        _currentKeyPressed = null;
    }

    public void onKeyPressed(KeyEvent e) {
        _currentKeyPressed = e;
    }

    public void onProcessFile() {

        Thread thread = new Thread() {
            public void run() {
                try {
                    processZebraData("C:\\Users\\garrm\\Downloads\\Match7.csv");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    public void moveRight() {
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // ensure the field image resizes as the container does
        fieldBackground.fitWidthProperty().bind(virtualField.widthProperty());
        // virtualField.maxHeightProperty().bind(fieldBackground.fitHeightProperty());

        ChangeListener<Number> fieldSizeChangeListener = (observable, oldValue, newValue) -> {
            _matchField.setScale(virtualField.getWidth());

        };
        virtualField.widthProperty().addListener(fieldSizeChangeListener);

        _matchField = new MatchField(this.virtualField);
        _robot = new RobotSprite();
        _matchField.addSprite(_robot);

        // create the main loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                mainLoop();
            }
        };

        // Set up a listener that gets triggered every time the map is updated.
        dataProperty().addListener(this);

        timer.start();

    }

    public void processZebraData(String filename) throws InterruptedException {
        String row;
        BufferedReader csvReader = null;
        try {
            csvReader = new BufferedReader(new FileReader(filename));
            // first two lines are junk for now
            csvReader.readLine();
            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                // convert the numbers from ft to cms
                double ftToCm = 30.48;
                double x = Double.parseDouble(data[0]) * ftToCm;
                double y = Double.parseDouble(data[1]) * ftToCm;

                Thread.sleep(100);

                _matchField.move(_robot, x, y, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void changed(ObservableValue<? extends MapData> observable, MapData oldValue, MapData newValue) {
        try {
            double x = (double) this.dataProperty().get().get("Location/x");
            double y = (double) this.dataProperty().get().get("Location/y");
            double heading = (double) this.dataProperty().get().get("Location/heading");

            _matchField.move(_robot, x, y, heading);
        } catch (Exception e) {
            // NO OP just ignore
        }
    }
}
