package client.scenes;

import client.CustomAlert;
import client.customExceptions.SubTaskException;
import client.services.SubCardService;
import client.utils.NetworkUtils;
import client.utils.SubTaskUtils;
import client.utils.TaskListUtils;
import com.google.inject.Inject;
import commons.SubTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

public class SubCardCtrl {
    private SubTask subTask;
    @FXML
    private Label text;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private CheckBox checkbox;
    @FXML
    private HBox root;

    private ListCtrl listController;
    private TaskListUtils taskListUtils;
    private CustomAlert customAlert;
    private SubTaskUtils subTaskUtils;
    private NetworkUtils networkUtils;
    private DetailedTaskViewCtrl detailedTaskViewCtrl;
    private SubCardService subCardService;


    @Inject
    void initialize(final SubTask subTask, final ListCtrl listCtrl,
                    final CustomAlert customAlert,
                     final NetworkUtils networkUtils,
                    final DetailedTaskViewCtrl detailedTaskViewCtrl
    ) {
        this.subTask= subTask;
        this.text.setText(subTask.getName());
        this.listController = listCtrl;
        this.networkUtils = networkUtils;
        this.customAlert = customAlert;
        this.subTaskUtils = networkUtils.getSubTaskUtils();
        this.taskListUtils = networkUtils.getTaskListUtils();
        this.subCardService = new SubCardService(networkUtils, subTask, customAlert,
                listController.getBoardID(), listController.getTaskList().id,
                detailedTaskViewCtrl.getTask());
        this.detailedTaskViewCtrl = detailedTaskViewCtrl;
        this.checkbox.setSelected(subTask.isCompleted());
        this.root.setOpacity(subTask.isCompleted() ? 0.5D : 1.0D);
    }

    public void deleteSubTask() {
        subCardService.deleteSubTask();
    }

    public void renameSubTask() {
        TextInputDialog dialog = new TextInputDialog(subTask.getName());
        dialog.setTitle("Talio: Rename Sub Task");
        dialog.setHeaderText("Enter new name");
        dialog.setContentText("Name:");
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("client/images/icon.png"));
        Optional<String> newName = dialog.showAndWait();
        newName.ifPresent(s -> subCardService.setSubTaskName(s));
        try {
            subTask = subTaskUtils.getSubTask(listController.getBoardID(),
                    listController.getTaskList().id, detailedTaskViewCtrl.getTask().id,
                    subTask.id);
            text.setText(subTask.getName());
        } catch (SubTaskException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean moveSubTaskUp() {
        subCardService.moveUp();
        if(!Objects.equals(detailedTaskViewCtrl.getTask().getName(), text.getText())) {
            text.setText(text.getText());
            return true;
        }
        return false;
    }

    public boolean moveSubTaskDown() {
        subCardService.moveDown();
        if(!Objects.equals(detailedTaskViewCtrl.getTask().getName(), text.getText())) {
            text.setText(text.getText());
            return true;
        }
        return false;
    }

    /**
     * When the card is hovered, the edit and delete buttons are shown
     */
    public void onHover(){
        if(!checkbox.isSelected())
            this.editButton.setOpacity(1.0d);
        this.deleteButton.setOpacity(1.0d);
    }

    /**
     * When the card is not hovered, the edit and delete buttons are hidden
     */
    public void onUnhover(){
        this.editButton.setOpacity(0.0d);
        this.deleteButton.setOpacity(0.0d);
    }

    /**
     * When the edit button is hovered, the background is set to grey.
     */
    public void onHoverEdit(){
        this.editButton.setStyle("-fx-background-color: #BBBBBB;");
    }

    /**
     * When the edit button is not hovered, the background is set to transparent.
     */
    public void onUnhoverEdit(){
        this.editButton.setStyle("-fx-background-color: transparent;");
    }

    /**
     * When the delete button is hovered, the background is set to grey.
     */
    public void onHoverDelete(){
        this.deleteButton.setStyle("-fx-background-color: #BBBBBB;");
    }

    /**
     * When the delete button is not hovered, the background is set to transparent.
     */
    public void onUnhoverDelete(){
        this.deleteButton.setStyle("-fx-background-color: transparent;");
    }

    public void onCheckboxChanged(final ActionEvent event){
        this.root.setOpacity(checkbox.isSelected() ? 0.5D : 1.0D);
        subCardService.completeSubTask(checkbox.isSelected());
    }
}
