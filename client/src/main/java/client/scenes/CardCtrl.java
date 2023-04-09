package client.scenes;

import client.CustomAlert;
import client.customExceptions.TaskException;
import client.utils.TaskListUtils;
import client.customExceptions.TaskListException;
import client.utils.TaskUtils;
import commons.Tag;
import commons.Task;
import commons.TaskList;
import commons.TaskPreset;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

public class CardCtrl {
    private Task task;

    private BoardOverviewCtrl boardOverviewCtrl;

    @FXML
    private Label title;

    @FXML
    private Button editButton;

    @FXML
    private TextField editTitleTextField;

    @FXML
    private Button deleteButton;

    @FXML
    private Button descButton;

    @FXML
    private Rectangle progressBar;

    @FXML
    private StackPane progressPane;

    @FXML
    private ImageView descIcon;

    @FXML
    private ImageView arrowIcon;

    @FXML
    private FlowPane tagList;

    @FXML
    private AnchorPane cardPane;

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
                           final TaskUtils taskUtils, final MainCtrl mainCtrl,
                           final BoardOverviewCtrl boardOverviewCtrl) {
        this.task= task;
        this.title.setText(task.getName());
        this.listController = listCtrl;
        this.taskListUtils = listUtils;
        this.customAlert = customAlert;
        this.taskUtils = taskUtils;
        this.mainCtrl = mainCtrl;
        this.setTask(task);
        this.onUnhover();
        this.boardOverviewCtrl = boardOverviewCtrl;
        tagList.setHgap(5.00);
        tagList.setVgap(5.00);

        cardPane.setOnMouseEntered(event -> {
            boardOverviewCtrl.setSelectedTask(task);
            cardPane.requestFocus();
        });

        cardPane.setOnMouseExited(event -> {
        });

        cardPane.setOnKeyReleased(event -> {
            if (event.isShiftDown()) {
                if (event.getCode() == KeyCode.UP) {
                    moveUp();
                } else if (event.getCode() == KeyCode.DOWN) {
                    moveDown();
                }
            } else if (event.getCode() == KeyCode.E) {
                editTitleTextField.setVisible(true);
                editTitleTextField.setManaged(true);
                editTitleTextField.requestFocus();
                editTitleTextField.setText(title.getText());
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER) {
                editTask();
            } else if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
                deleteTask();
            }
        });
        editTitleTextField.addEventHandler(KeyEvent.KEY_PRESSED, this::handleEditTitle);


        TaskPreset preset = task.getTaskPreset();
        cardPane.setStyle(cardPane.getStyle() +"-fx-background-color: #" +
            preset.getBackgroundColor().substring(2) + ";");
        title.setStyle(title.getStyle()+ "-fx-text-fill: #" +
            preset.getFontColor().substring(2) + ";");
    }

    private void setTags(final List<Tag> tags) {
        for(Tag tag : tags) {
            Pane tagPane = new Pane();
            tagPane.setPrefSize(60, 10);
            tagPane.setStyle("-fx-background-radius: 5px; -fx-border-radius: 5px;" +
                    " -fx-background-color: #" + tag.getColorBackground() + ";");
            tagList.getChildren().add(tagPane);
        }
    }
    private void handleEditTitle(final KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String newTitle = editTitleTextField.getText().trim();
            if (!newTitle.isEmpty()) {
                title.setText(newTitle);
                task.setName(newTitle);
                try {
                    this.taskUtils.renameTask(listController.getBoardID(),
                            listController.getTaskList().id,
                            task.getId(), this.editTitleTextField.getText());
                } catch (TaskException e) {
                    Alert alert = customAlert.showAlert(e.getMessage());
                    alert.showAndWait();
                }
            }
            editTitleTextField.setVisible(false);
            editTitleTextField.setManaged(false);
            event.consume();
        }
    }
    public void setTask(final Task task) {
        this.task = task;
        if (Objects.equals(this.task.getDescription(), "")) {
            this.descButton.setDisable(true);
            descIcon.setVisible(false);
        }

        if (this.task.getTags() != null) {
            setTags(this.task.getTags());
        }
        if (this.taskUtils.getProgress(task) < 0)
            this.progressPane.setVisible(false);
        else
            this.progressBar.setWidth(this.taskUtils.getProgress(task) * 215.0D);
        this.title.setText(task.getName());
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
     * @return it will return a boolean depending on if the task could be moved up
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
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    /**
     * This will move the card one position down in the listview
     * So in the list of tasks of a tasklist it will go 1 index up
     *
     * @return it will return a boolean depending on if the task could be moved down
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
        mainCtrl.showDetailedTaskView(task, listController);
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
        this.title.setFont(Font.font(18));
        this.title.setTextFill(Color.BLACK);
    }

    /**
     * When the card is not hovered, the edit and delete buttons are hidden
     */
    public void onUnhover(){
        this.editButton.setOpacity(0.0d);
        this.deleteButton.setOpacity(0.0d);
        this.title.setFont(Font.font(18));
        this.title.setTextFill(Color.BLACK);
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

    /**
     * When the description button is hovered, the background is set to grey.
     */
    public void onHoverDesc(){
        this.descButton.setStyle("-fx-background-color: #BBBBBB;");
    }

    /**
     * When the delete button is not hovered, the background is set to transparent.
     */
    public void onUnhoverDesc(){
        this.descButton.setStyle("-fx-background-color: transparent;");
    }


}
