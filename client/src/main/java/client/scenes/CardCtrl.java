package client.scenes;

import client.CustomAlert;
import client.customExceptions.TagException;
import client.customExceptions.TaskException;
import client.utils.NetworkUtils;
import client.utils.TagUtils;
import client.utils.TaskListUtils;
import client.customExceptions.TaskListException;
import client.utils.TaskUtils;
import commons.Tag;
import commons.Task;
import commons.TaskList;
import commons.TaskPreset;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
import javafx.util.Duration;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CardCtrl {
    private Task task;

    private BoardOverviewCtrl boardOverviewCtrl;

    @FXML
    private Label title;

    @FXML
    private Button editButton;

    private TagUtils tagUtils;
    @FXML
    private TextField editTitleTextField;

    @FXML
    private Button deleteButton;

    @FXML
    private Button descButton;

    @FXML
    private Rectangle progressBar;
    private boolean selected;
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

    private NetworkUtils networkUtils;
    private TaskListUtils taskListUtils;
    private TaskUtils taskUtils;
    private CustomAlert customAlert;

    /**
     * This initializes the card using a task
     *
     * @param task the task used for initialization
     */
    @Inject
    public void initialize(final Task task, final ListCtrl listCtrl,
                            final CustomAlert customAlert,
                           final BoardOverviewCtrl boardOverviewCtrl,
                           final NetworkUtils networkUtils, final MainCtrl mainCtrl) {
        this.listController = listCtrl;
        this.customAlert = customAlert;
        this.mainCtrl = mainCtrl;
        this.networkUtils = networkUtils;
        this.taskListUtils = networkUtils.getTaskListUtils();
        this.taskUtils = networkUtils.getTaskUtils();
        this.tagUtils = networkUtils.getTagUtils();
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.setTask(task);
        this.onUnhover();
        tagList.setHgap(5.00);
        tagList.setVgap(5.00);
        cardPane.setOnMouseEntered(event -> onHover());
        cardPane.setOnMouseExited(event -> onUnhover());
        cardPane.setOnMouseClicked(event -> {
            PauseTransition pause = new PauseTransition(Duration.millis(250));
            pause.setOnFinished(e -> {
                if (event.getClickCount() == 1) {
                    selectTask();
                    cardPane.requestFocus();
                }
            });
            pause.play();

            if (event.getClickCount() == 2) {
                pause.stop();
                editTask();
            }

            event.consume();
        });
        startShortcuts();
        editTitleTextField.addEventHandler(KeyEvent.KEY_PRESSED, this::handleEditTitle);
        TaskPreset preset = task.getTaskPreset();
        cardPane.setStyle(cardPane.getStyle() + "-fx-background-color: #" +
                preset.getBackgroundColor().substring(2) + ";");
        title.setStyle(title.getStyle() + "-fx-text-fill: #" +
                preset.getFontColor().substring(2) + ";");
    }

    private void setTags(final List<Tag> tags) {
        for (Tag tag : tags) {
            Pane tagPane = new Pane();
            tagPane.setPrefSize(60, 10);
            tagPane.setStyle("-fx-background-radius: 5px; -fx-border-radius: 5px;" +
                    " -fx-background-color: #" + tag.getColorBackground() + ";");
            tagList.getChildren().add(tagPane);
        }
    }

    private void startShortcuts(){
        cardPane.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
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
            } else if (event.getCode() == KeyCode.T) {
                addTag();
                event.consume();
            } else if (event.getCode() == KeyCode.C) {
                setPreset();
                event.consume();
            }
        });

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
        } catch (TaskListException e) {
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
        } catch (TaskException e) {
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


    /**
     * When the card is hovered, the edit and delete buttons are shown
     */
    public void onHover() {
        if (!selected) { // Apply hover styles only if the card is not selected
            this.editButton.setOpacity(1.0d);
            this.deleteButton.setOpacity(1.0d);
            this.title.setFont(Font.font(18));
            this.title.setTextFill(Color.BLACK);
        }
    }

    /**
     * When the card is not hovered, the edit and delete buttons are hidden
     */
    public void onUnhover() {
        if (!selected) { // Revert hover styles only if the card is not selected
            this.editButton.setOpacity(0.0d);
            this.deleteButton.setOpacity(0.0d);
            this.title.setFont(Font.font(18));
            this.title.setTextFill(Color.BLACK);
        }
    }

    /**
     * When the edit button is hovered, the background is set to grey.
     */
    public void onHoverEdit() {
        this.editButton.setStyle("-fx-background-color: #BBBBBB;");
    }

    /**
     * When the edit button is not hovered, the background is set to transparent.
     */
    public void onUnhoverEdit() {
        this.editButton.setStyle("-fx-background-color: transparent;");
    }

    /**
     * When the delete button is hovered, the background is set to grey.
     */
    public void onHoverDelete() {
        this.deleteButton.setStyle("-fx-background-color: #BBBBBB;");
    }

    /**
     * When the delete button is not hovered, the background is set to transparent.
     */
    public void onUnhoverDelete() {
        this.deleteButton.setStyle("-fx-background-color: transparent;");
    }

    /**
     * When the description button is hovered, the background is set to grey.
     */
    public void onHoverDesc() {
        this.descButton.setStyle("-fx-background-color: #BBBBBB;");
    }

    /**
     * When the delete button is not hovered, the background is set to transparent.
     */
    public void onUnhoverDesc() {
        this.descButton.setStyle("-fx-background-color: transparent;");
    }

    public void selectTask() {
        selected = true;
        cardPane.setStyle(cardPane.getStyle()
                + "-fx-border-color: #FFA500; -fx-border-width: 2px;");
    }
    //i have to fix the difference between hovering and selecting
    public void deselectTask() {
        selected = false;
        cardPane.setStyle(cardPane.getStyle()
                .replace("-fx-border-color: #FFA500; -fx-border-width: 2px;", ""));
        onUnhover();
    }

    public void addTag() {
        List<Tag> boardTags = boardOverviewCtrl.getBoard().getTags();
        if (boardTags.isEmpty()) {
            Alert alert = customAlert.showAlert("There are no tags on this board.");
            alert.showAndWait();
            return;
        }

        ChoiceDialog<Tag> dialog = new ChoiceDialog<>(boardTags.get(0), boardTags);
        dialog.setTitle("Add Tag");
        dialog.setHeaderText(null);
        dialog.setContentText("Select a tag:");
        Optional<Tag> result = dialog.showAndWait();
        result.ifPresent(tag -> {
            if (task.getTags().contains(tag)) {
                Alert alert = customAlert.showAlert("This task already has the selected tag.");
                alert.showAndWait();
            } else {
                try {
                    tagUtils.addTaskTag(listController.getBoardID(),
                            listController.getTaskList().id, task.id, tag);
                    task.getTags().add(tag);
                    Pane tagPane = new Pane();
                    tagPane.setPrefSize(60, 10);
                    tagPane.setStyle("-fx-background-radius: 5px; -fx-border-radius: 5px;" +
                            " -fx-background-color: #" + tag.getColorBackground() + ";");
                    tagList.getChildren().add(tagPane);
                } catch (TagException e) {
                    Alert alert = customAlert.showAlert(e.getMessage());
                    alert.showAndWait();
                }
            }
        });
    }

    public void setPreset() {
        List<TaskPreset> boardColors = boardOverviewCtrl.getBoard().getTaskPresets();

        ChoiceDialog<TaskPreset> dialog = new ChoiceDialog<>(boardColors.get(0), boardColors);
        dialog.setTitle("Change preset");
        dialog.setHeaderText(null);
        dialog.setContentText("Select a preset:");
        Optional<TaskPreset> result = dialog.showAndWait();
        result.ifPresent(preset -> {
            if (task.getTaskPreset().equals(preset)) {
                Alert alert = customAlert.showAlert("This task already has the selected preset.");
                alert.showAndWait();
            } else {
                taskUtils.setPreset(listController.getBoardID(),
                        listController.getTaskList().id, task.id, preset);
                task.setTaskPreset(preset);
                TaskPreset presetChange = task.getTaskPreset();
                System.out.println(presetChange);
                System.out.println(presetChange.getBackgroundColor());
                System.out.println(presetChange.getFontColor());
                cardPane.setStyle("-fx-background-color: #"
                        + presetChange.getBackgroundColor().substring(2, 8) + ";");
                title.setStyle("-fx-text-fill: #" +
                        presetChange.getFontColor().substring(2, 8) + ";");
            }
        });
    }
}
