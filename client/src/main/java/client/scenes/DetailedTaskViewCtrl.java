package client.scenes;

import client.CustomAlert;
import client.customExceptions.TaskException;
import client.utils.SubTaskUtils;
import client.utils.TaskListUtils;
import client.utils.TaskUtils;
import com.google.inject.Inject;
import commons.SubTask;
import commons.Task;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.Optional;

public class DetailedTaskViewCtrl {

    @FXML
    public Button editButton;
    @FXML
    private Label taskNameText;
    @FXML
    private TextField taskNameTextField;
    @FXML
    private Label taskDescriptionText;
    @FXML
    private TextArea taskDescriptionTextArea;
    @FXML
    private ListView<SubTask> subTasks;
    private Task task;
    private CustomAlert customAlert;
    private MainCtrl mainCtrl;
    private TaskUtils taskUtils;
    private TaskListUtils taskListUtils;
    private ListCtrl listController;
    private SubTaskUtils subTaskUtils;

    @Inject
    public DetailedTaskViewCtrl(final MainCtrl mainCtrl, final TaskListUtils taskListUtils,
                                final TaskUtils taskUtils, final CustomAlert customAlert,
                                final SubTaskUtils subTaskUtils) {
        this.taskListUtils = taskListUtils;
        this.taskUtils = taskUtils;
        this.mainCtrl = mainCtrl;
        this.customAlert = customAlert;
        this.subTaskUtils = subTaskUtils;
    }

    public void initialize() {
        this.taskNameTextField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue){
                try {
                    this.onFocusLostTaskName();
                } catch (TaskException e) {
                    System.out.println(e.getMessage());
                }
            }
        }));

        this.taskDescriptionTextArea.focusedProperty().addListener(((observable,
                                                                     oldValue, newValue) -> {
            if(!newValue){
                try {
                    this.onFocusLostTaskDescription();
                } catch (TaskException e) {
                    System.out.println(e.getMessage());
                }
            }
        }));
    }

    public void onTaskNameClicked(final MouseEvent event){
        if(event.getButton() != MouseButton.PRIMARY)
            return;
        this.taskNameTextField.setText(this.taskNameText.getText());
        this.taskNameTextField.setVisible(true);
        this.taskNameText.setVisible(false);
    }

    public void onTaskDescriptionClicked(final MouseEvent event){
        if(event.getButton() != MouseButton.PRIMARY)
            return;
        this.taskDescriptionTextArea.setText(this.taskDescriptionText.getText());
        this.taskDescriptionTextArea.setVisible(true);
        this.taskDescriptionText.setVisible(false);
    }

    private void onFocusLostTaskName() throws TaskException {
        this.taskNameText.setText(this.taskNameTextField.getText());
        this.taskNameTextField.setVisible(false);
        this.taskNameText.setVisible(true);
        if(this.taskNameTextField.getText().equals(task.getName()))
            return;
        try {
            this.taskUtils.renameTask(listController.getBoardID(), listController.getTaskList().id,
                    task.getId(), this.taskNameTextField.getText());
        } catch (TaskException e) {
            throw new TaskException("Renaming task unsuccessful");
        }
    }

    private void onFocusLostTaskDescription() throws TaskException {
        this.taskDescriptionText.setText(this.taskDescriptionTextArea.getText());
        this.taskDescriptionTextArea.setVisible(false);
        this.taskDescriptionText.setVisible(true);
        if(this.taskDescriptionTextArea.getText().equals(task.getDescription()))
            return;
        try {
            this.taskUtils.editDescription(listController.getBoardID(),
                    listController.getTaskList().id,
                    task.getId(), this.taskDescriptionTextArea.getText());
        } catch (TaskException e) {
            throw new TaskException("Editing the description of the task unsuccessful");
        }
    }

    /**
     * Setter for the task, after it sets the task it will update
     * the fields to the details of the task
     * @param task
     */
    public void setTask(final Task task) {
        this.task = task;
        this.update();
    }

    /**
     * It will update all the fields with the new values from the task
     */
    private void update() {
        taskNameText.setVisible(true);
        taskDescriptionText.setVisible(true);
        taskNameText.setText(this.task.getName());
        taskDescriptionText.setText(this.task.getDescription());
        subTasks.setCellFactory(lv -> {
            ListCell<SubTask> cell = new ListCell<>() {
                @Override
                protected void updateItem(final SubTask subTask, final boolean empty) {
                    super.updateItem(subTask, empty);
                    if (subTask == null || empty) {
                        setGraphic(null);
                    } else {
                        try {
                            var cardLoader = new FXMLLoader(getClass().getResource("SubCard.fxml"));
                            Node card = cardLoader.load();
                            SubCardCtrl subCardCtrl = cardLoader.getController();
                            subCardCtrl.initialize(subTask, listController, taskListUtils,
                                    customAlert, taskUtils, mainCtrl);
                            setGraphic(card);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            };
            cell.setOnMouseEntered(event -> {
                lv.getSelectionModel().select(cell.getIndex());
            });
            cell.setOnMouseExited(event -> {
                lv.getSelectionModel().clearSelection(cell.getIndex());
            });
            return cell;
        });

        subTasks.getItems().setAll(task.getSubtasks());
    }

    public void setListController(final ListCtrl listController) {
        this.listController = listController;
    }

    public void goBack() {
        mainCtrl.showBoardOverview();
    }

    public boolean deleteTask() {
        try {
            TaskList taskList = listController.getTaskList();
            taskUtils.deleteTask(listController.getBoardID(), taskList.id, task.id);
            mainCtrl.showBoardOverview();
            return true;
        } catch (TaskException e){
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
            return false;
        }
    }


    //This function still needs implementation, should be done by Edsard
    public void addSubTask() {
        TextInputDialog dialog = new TextInputDialog("Name");
        dialog.setTitle("Talio:  Add A Sub Task");
        dialog.setHeaderText("Create A New Sub Task:");
        dialog.setContentText("Name:");

        Optional<String> newName = dialog.showAndWait();
        SubTask subTask;

        if (newName.isPresent()) {
            subTask = makeSubTask(newName.get());
            addCard(subTask);
        }
    }

    public SubTask makeSubTask(final String name) {
        return new SubTask(name, false);
    }

    public void addCard(final SubTask subTask) {
        try {
            if (subTasks == null) {
                subTasks = new ListView<>();
            }
            subTasks.getItems().add(subTask);
            subTaskUtils.addSubTask(listController.getBoardID(),
                    listController.getTaskList().id, task.id, subTask);
        } catch (TaskException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
