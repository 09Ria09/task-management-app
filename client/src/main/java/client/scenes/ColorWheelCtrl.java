package client.scenes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ColorWheelCtrl implements Initializable {

    private final Consumer<Color> callback;
    @FXML
    ColorPicker color;
    @FXML
    Button cancel;
    @FXML
    Button confirm;

    public ColorWheelCtrl(Consumer<Color> callback) {
        this.callback = callback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancel.setOnAction(event -> {
            try {
                callback.accept(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        confirm.setOnAction(event -> {
            try {
                callback.accept(color.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
