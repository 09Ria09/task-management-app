package client.scenes;

import client.CustomAlert;
import client.customExceptions.TaskException;
import client.utils.TaskListUtils;
import client.utils.TaskUtils;
import com.google.inject.Inject;
import commons.SubTask;
import commons.Task;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.io.IOException;

public class DetailedTaskViewCtrl {

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

    @Inject
    public DetailedTaskViewCtrl(final MainCtrl mainCtrl, final TaskListUtils taskListUtils,
                                final TaskUtils taskUtils, final CustomAlert customAlert) {
        this.taskListUtils = taskListUtils;
        this.taskUtils = taskUtils;
        this.mainCtrl = mainCtrl;
        this.customAlert = customAlert;
    }

    public void setTask(Task task) {
        this.task = task;
        this.update();
    }

    private void update() {
        taskNameText.setText(this.task.getName());
        taskDescriptionText.setText(this.task.getDescription());
        eventHandlers();
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
    }

    public void setListController(ListCtrl listController) {
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

            if(!newDesc.equals("")) {
                taskUtils.editDescription(listController.getBoardID(),
                        listController.getTaskList().id,
                        task.id, newDesc);
                return true;
            }
        } catch (TaskException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
            return false;
        }
        return false;
    }

    public void eventHandlers() {
        taskDescriptionText.setOnKeyReleased(event -> keyReleased(event));
    }

    private void keyReleased(KeyEvent event) {
        saveDescription();
        event.consume();
    }
}
