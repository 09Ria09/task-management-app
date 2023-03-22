package client.scenes;

import client.utils.TaskListUtils;
import commons.Task;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ListCtrl implements Initializable {
    private static final DataFormat taskCustom = new DataFormat("task.custom");
    private MainCtrl mainCtrl;
    @FXML
    ListView<Task> list;
    @FXML
    Text title;
    @FXML
    VBox vBox;

    private TaskList taskList;
    private long boardID;
    private TaskListUtils taskListUtils;

    public ListCtrl(TaskListUtils taskListUtils) {
        this.taskListUtils = taskListUtils;
    }

    public void passMain(final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    private void setDragHandlers(final ListView<Task> list) {
        list.setOnDragDetected(event -> dragDetected(list, event));
        list.setOnDragEntered(event -> dragEntered(list, event));
        list.setOnDragOver(event -> dragOver(list, event));
        list.setOnDragExited(event -> dragExited(list, event));
        list.setOnDragDropped(event -> dragDropped(list, event));
        list.setOnDragDone(event -> dragDone(list, event));
    }
    public void dragDetected(final ListView<Task> lv, final MouseEvent event) {
        Dragboard dragboard = lv.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent cc = new ClipboardContent();
        if (lv.getSelectionModel().getSelectedItem() == null)
            return;
        var selectedTask=lv.getSelectionModel().getSelectedItem();
        cc.put(taskCustom, selectedTask);
        dragboard.setContent(cc);
        event.consume();
    }
    public void dragEntered(final ListView<Task> lv, final DragEvent event) {
        lv.setStyle("-fx-effect: innershadow(gaussian, rgba(0,0,0,0.8), 20, 0, 0, 0);");
        event.consume();
    }

    public void dragExited(final ListView<Task> lv, final DragEvent event) {
        lv.setStyle("-fx-effect: none;");
        event.consume();
    }

    public void dragOver(final ListView<Task> lv, final DragEvent event) {
        if (event.getDragboard().hasContent(taskCustom))
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        event.consume();
    }

    public void dragDropped(final ListView<Task> lv, final DragEvent event) {
        if (event.getDragboard().hasContent(taskCustom)) {
            lv.getItems().add((Task) event.getDragboard().getContent(taskCustom));
            event.setDropCompleted(true);
        } else
            event.setDropCompleted(false);
        event.consume();
    }

    public void dragDone(final ListView<Task> lv, final DragEvent event) {
        Task selectedTask = lv.getSelectionModel().getSelectedItem();
        if (selectedTask != null && event.getTransferMode() == TransferMode.MOVE &&
            event.getEventType()==DragEvent.DRAG_DONE) {
            lv.getItems().remove(selectedTask);
        }
        event.consume();
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        ListCtrl controller = this;
        setDragHandlers(list);
        list.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(final Task task, final boolean empty) {
                super.updateItem(task, empty);
                if (task == null || empty) {
                    setGraphic(null);
                } else {
                    try {
                        var cardLoader = new FXMLLoader(getClass().getResource("Card.fxml"));
                        Node card = cardLoader.load();
                        CardCtrl cardCtrl = cardLoader.getController();
                        cardCtrl.initialize(task, controller, taskListUtils);
                        setGraphic(card);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void setLayoutX(final double x){
        list.setLayoutX(x);
    }

    /**
     * This refreshes the tasks of the list.
     * @param newTaskList the list for which the tasks must be refreshed.
     */
    public void refresh(final TaskList newTaskList, long boardID) {
        this.boardID = boardID;
        if (!Objects.equals(title.getText(), newTaskList.getName())) // if the title is different
            title.setText(newTaskList.getName()); // update it

        list.getItems().retainAll(newTaskList.getTasks()); // retain only the tasks
        // that are also in newTaskList
        for (Task task : newTaskList.getTasks()) { // go through all the received tasks
            if (!list.getItems().contains(task)) // if this task isn't there
                list.getItems().add(task); // add it
        }
    }

    /**
     * this adds a task to a specific list
     *
     * @param task the task to be added
     */
    public void addCard(final Task task) {
        if (list == null) {
            list = new ListView<Task>();
        }
        list.getItems().add(task);
    }

    /**
     * this adds a task to a specific list
     */
    public void addCard() {
        mainCtrl.showCreateTask();
    }

    public VBox getRoot() {
        return vBox;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public long getBoardID() {
        return boardID;
    }
}
