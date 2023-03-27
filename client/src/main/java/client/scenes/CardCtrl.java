package client.scenes;

import client.CustomAlert;
import client.customExceptions.TaskException;
import client.utils.TaskListUtils;
import client.customExceptions.TaskListException;
import client.utils.TaskUtils;
import commons.Task;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

public class CardCtrl {
    private Task task;
    @FXML
    public Label title;

    @FXML
    private Label description;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Rectangle progressBar;

    @FXML
    private StackPane progressPane;

    private MainCtrl mainCtrl;
    private ListCtrl listController;
    private TaskListUtils taskListUtils;
    private TaskUtils taskUtils;
    private CustomAlert customAlert;

    /**
     * This initializes the card using a task
     * @param task the task used for initialization
     */
    @Inject
    public void initialize(final Task task, final ListCtrl listCtrl,
                           final TaskListUtils listUtils, final CustomAlert customAlert,
                           final TaskUtils taskUtils, final MainCtrl mainCtrl) {
        this.task= task;
        this.title.setText(task.getName());
        this.description.setText(task.getDescription());
        if(this.task.getProgress() < 0)
            this.progressPane.setVisible(false);
        else
            this.progressBar.setWidth(this.task.getProgress()*280.0D);
        this.listController = listCtrl;
        this.taskListUtils = listUtils;
        this.customAlert = customAlert;
        this.taskUtils = taskUtils;
        this.mainCtrl = mainCtrl;
        this.onUnhover();
    }

    /**
     * @param o an object
     * @return true if the object provided is the same to this object
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardCtrl cardCtrl = (CardCtrl) o;
        return Objects.equals(task, cardCtrl.task);
    }

    /**
     * @return the hashCode of this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(task);
    }

    /**
     * This will move the card one position up in the listview
     * So in the list of tasks of a tasklist it will go 1 index down
     *
     * @return it will return a boolean depending if the task could be moved up
     */
    public boolean moveUp() {
        try {
            TaskList taskList = listController.getTaskList();
            List<Task> tasks = taskList.getTasks();
            int index = tasks.indexOf(task);
            if (index > 0) {
                taskListUtils.reorderTask(listController.getBoardID(),
                        taskList.id, task.id, index - 1);
                taskList.reorder(task.id, index - 1);
                listController.hardRefresh(taskList, listController.getBoardID());
                return true;
            } else {
                return false;
            }
        }catch (TaskListException e){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText("Oops, something went wrong!");
            alert.setContentText("We're sorry :( something went wrong : "+ e.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    /**
     * This will move the card one position down in the listview
     * So in the list of tasks of a tasklist it will go 1 index up
     *
     * @return it will return a boolean depending if the task could be moved down
     */
    public boolean moveDown() {
        try {
            TaskList taskList = listController.getTaskList();
            List<Task> tasks = taskList.getTasks();
            int index = tasks.indexOf(task);
            if (index < tasks.size() - 1) {
                taskListUtils.reorderTask(listController.getBoardID(),
                        taskList.id, task.id, index + 1);
                taskList.reorder(task.id, index + 1);
                listController.hardRefresh(taskList, listController.getBoardID());
                return true;
            } else {
                return false;
            }
        } catch (TaskListException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    public boolean deleteTask() {
        try {
            TaskList taskList = listController.getTaskList();
            taskUtils.deleteTask(listController.getBoardID(), taskList.id, task.id);
            return true;
        } catch (TaskException e){
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    public void editTask() {
        mainCtrl.showEditTask(this, customAlert);
    }

    public Task getTask() {
        return task;
    }

    public TaskUtils getTaskUtils() {
        return taskUtils;
    }

    public ListCtrl getListController() {
        return listController;
    }

    /**
     * When the card is hovered, the edit and delete buttons are shown
     */
    public void onHover(){
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
}
