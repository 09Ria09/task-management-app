package client.scenes;

import client.CustomAlert;
import client.utils.BoardUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.TaskPreset;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.awt.*;

public class TaskColorPresetCardCtrl {

    private TaskPreset taskPreset;

    @FXML
    private Label name;

    @FXML
    private ColorPicker backgroundColorPicker;
    @FXML
    private ColorPicker fontColorPicker;
    @FXML
    private Checkbox isDefault;
    BoardUtils boardUtils;
    private Board board;
    private CustomAlert customAlert;
    private ColorManagementViewCtrl colorManagementViewCtrl;

    @Inject
    public TaskColorPresetCardCtrl(final BoardUtils boardUtils, final Board board,
                                   final CustomAlert customAlert,
                                   final ColorManagementViewCtrl colorManagementViewCtrl) {
        this.boardUtils = boardUtils;
        this.board = board;
        this.customAlert = customAlert;
        this.colorManagementViewCtrl = colorManagementViewCtrl;
    }
    /**
     * Initialize the task preset card
     * @param taskPreset the task preset to initialize
     */
    public void initialize(final TaskPreset taskPreset) {
        this.taskPreset = taskPreset;
        name.setText(taskPreset.getName());
        backgroundColorPicker.setValue(Color.valueOf(taskPreset.getBackgroundColor()));
        fontColorPicker.setValue(Color.valueOf(taskPreset.getFontColor()));
        System.out.println(taskPreset);
    }

    /**
     * Delete the task preset
     */
    public void delete(){
        if(taskPreset.isDefault()){
            Alert alert= new CustomAlert().showAlert("Can't delete default task preset");
            alert.showAndWait();
        }
        else {
            colorManagementViewCtrl.removeTaskPreset(taskPreset);
        }
    }
    /**
     * Update the task preset
     */
    public void update(){
        taskPreset.setName(name.getText());
        taskPreset.setBackgroundColor(backgroundColorPicker.getValue().toString());
        taskPreset.setFontColor(fontColorPicker.getValue().toString());
        boardUtils.updateTaskPreset(board.getId(),taskPreset);
    }
    /**
     * Set the task preset as default
     */
    public void makeDefault(){
        taskPreset.setDefault(true);
        update();
    }
}
