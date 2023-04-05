package client.scenes;

import commons.TaskPreset;
import javafx.fxml.FXML;

import java.awt.*;

public class TaskColorPresetCardCtrl {

    private TaskPreset taskPreset;

    @FXML
    private Label name;

    @FXML
    private Color backgroundColorPicker;
    @FXML
    private Color fontColorPicker;
    @FXML
    private Checkbox isDefault;

    public void initialize(final TaskPreset taskPreset) {
        this.taskPreset = taskPreset;
    }
}
