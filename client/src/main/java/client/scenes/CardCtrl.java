package client.scenes;

import client.CustomAlert;
import client.customExceptions.TagException;
import client.customExceptions.TaskException;
import client.services.CardService;
import client.services.DetailedTaskViewService;
import client.utils.NetworkUtils;
import client.utils.TagUtils;
import client.utils.TaskListUtils;
import client.utils.TaskUtils;
import commons.Tag;
import commons.Task;
import commons.TaskList;
import commons.TaskPreset;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import javax.inject.Inject;
import java.util.*;

public class CardCtrl {
    private Task task;

    private BoardOverviewCtrl boardOverviewCtrl;

    @FXML
    public Label title;

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
    @FXML
    private StackPane progressPane;

    @FXML
    private ImageView descIcon;

    @FXML
    private FlowPane tagList;

    @FXML
    private AnchorPane cardPane;
    @FXML
    private AnchorPane root;

    private MainCtrl mainCtrl;
    private ListCtrl listController;

    private NetworkUtils networkUtils;
    private TaskListUtils taskListUtils;
    private TaskUtils taskUtils;
    private CustomAlert customAlert;
    private CardService cardService;

    /**
     * This initializes the card using a task
     *
     * @param task the task used for initialization
     */
    @Inject
    public void initialize(final Task task, final ListCtrl listCtrl,
                            final CustomAlert customAlert,
                           final Pair<BoardOverviewCtrl, MainCtrl> controllers,
                           final NetworkUtils networkUtils,
                           final boolean selected) {
        this.listController = listCtrl;
        this.customAlert = customAlert;
        this.mainCtrl = controllers.getValue();
        this.networkUtils = networkUtils;
        this.taskListUtils = networkUtils.getTaskListUtils();
        this.taskUtils = networkUtils.getTaskUtils();
        this.tagUtils = networkUtils.getTagUtils();
        this.boardOverviewCtrl = controllers.getKey();
        this.setTask(task);
        this.onUnhover();
        cardService = new CardService(networkUtils, task, listController.getBoardID(),
                listController.getTaskList(), customAlert);
        tagList.setHgap(5.00);
        tagList.setVgap(5.00);
        cardPane.setOnMouseEntered(event -> onHover());
        cardPane.setOnMouseExited(event -> onUnhover());
        TaskPreset preset = task.getTaskPreset();
        cardPane.setStyle(cardPane.getStyle() + "-fx-background-color: #" +
                preset.getBackgroundColor().substring(2) + ";");
        cardPane.setFocusTraversable(true);
        root.setOnKeyPressed(this::handleKeyboardInput);
        setSelected(selected);
    }

    public void setSelected(final boolean selected){
        Color textFill = Color.valueOf(task.getTaskPreset().getFontColor());
        if(selected)
            textFill = textFill.invert();
        title.setTextFill(textFill);
    }

    public void handleKeyboardInput(final KeyEvent event){

        if (event.getCode() == KeyCode.E) {

            listController.isEditing = true;
            event.consume();
            edit();
        }
        else if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
            deleteTask();
        }
        else if (event.getCode() == KeyCode.T) {
            addTag();
            event.consume();
        }
        else if (event.getCode() == KeyCode.C) {
            setPreset();
            event.consume();
        }
    }

    public Label getTitle() {
        return title;
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

    public void edit(){
        editTitleTextField.setVisible(true);
        editTitleTextField.setManaged(true);
        editTitleTextField.requestFocus();

        editTitleTextField.setText(title.getText());
        editTitleTextField.setEditable(false);
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    editTitleTextField.setEditable(true);
                    t.purge();
                });
            }
        }, 100);
    }

    public void handleEditTitle(final KeyEvent event) {

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

    public boolean isEditing() {
        return editTitleTextField.isVisible();
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
        if(cardService.moveUp()) {
            listController.hardRefresh(listController.getTaskList(), listController.getBoardID());
            return true;
        }
        return false;
    }

    /**
     * This will move the card one position down in the listview
     * So in the list of tasks of a tasklist it will go 1 index up
     *
     * @return it will return a boolean depending on if the task could be moved down
     */
    public boolean moveDown() {
        if(cardService.moveDown()) {
            listController.hardRefresh(listController.getTaskList(), listController.getBoardID());
            return true;
        }
        return false;
    }

    public boolean deleteTask() {
        return cardService.deleteTask();
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
        this.editButton.setOpacity(1.0d);
        this.deleteButton.setOpacity(1.0d);
    }

    /**
     * When the card is not hovered, the edit and delete buttons are hidden
     */
    public void onUnhover() {
        this.editButton.setOpacity(0.0d);
        this.deleteButton.setOpacity(0.0d);
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
                TaskPreset presetChange = cardService.setPreset(preset);
                cardPane.setStyle("-fx-background-color: #"
                        + presetChange.getBackgroundColor().substring(2, 8) + ";");
            }
        });
    }
}
