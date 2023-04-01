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
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.Optional;

public class DetailedTaskViewCtrl {

    @FXML
    public Button editButton;
    @FXML
    private Text taskNameText;
    @FXML
    private TextArea taskDescriptionText;
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
        taskNameText.setText(this.task.getName());
        taskDescriptionText.setText(this.task.getDescription());
        setEventHandlers();
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
                                    customAlert, subTaskUtils,
                                    DetailedTaskViewCtrl.this);
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

    public boolean saveDescription() {
        try {
            String newDesc = taskDescriptionText.getText();
            if(newDesc.equals(task.getDescription())) {
                return false;
            }
            taskUtils.editDescription(listController.getBoardID(),
                    listController.getTaskList().id,
                    task.id, newDesc);
            return true;
        } catch (TaskException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    public boolean saveName(final String newName) {
        try {
            if(!newName.equals("") && !newName.equals(task.getName())) {
                taskUtils.renameTask(listController.getBoardID(),
                        listController.getTaskList().id,
                        task.id, newName);
                return true;
            }
        } catch (TaskException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
            return false;
        }
        return false;
    }

    public void setEventHandlers() {
        taskDescriptionText.setOnKeyReleased(this::keyReleasedDesc);
    }

    private void keyReleasedDesc(final KeyEvent event) {
        saveDescription();
        event.consume();
    }


    public void editName() {
        TextInputDialog dialog = new TextInputDialog(this.task.getName());
        dialog.setTitle("Talio: Change Your Name");
        dialog.setHeaderText("Change Your Name:");
        dialog.setContentText("Name:");

        Optional<String> newName = dialog.showAndWait();

        newName.ifPresent(this::saveName);
        try {
            task = taskUtils.getTask(listController.getBoardID(),
                    listController.getTaskList().id, task.id);
            taskNameText.setText(task.getName());
        } catch (TaskException e) {
            throw new RuntimeException(e);
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

    public Task getTask() {
        return this.task;
    }
}
