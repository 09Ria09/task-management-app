package client.scenes;

import client.utils.ServerUtils;
import client.utils.TaskListUtils;
import client.utils.TaskUtils;
import client.utils.customExceptions.TaskListException;
import com.google.inject.Inject;
import client.utils.customExceptions.TaskException;
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
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ListCtrl implements Initializable {
    private static final DataFormat taskCustom = new DataFormat("task.custom");
    private MainCtrl mainCtrl;
    @FXML
    ListView<Task> list;
    @FXML
    Text title;
    @FXML
    VBox vBox;

    private Task draggedTask;
    private int totalAmountOfTasks;
    private TaskList taskList;
    private long boardID;
    private TaskListUtils taskListUtils;
    private TaskUtils taskUtils;
    private ServerUtils server;


    @Inject
    public ListCtrl(final MainCtrl mainCtrl, final TaskListUtils taskListUtils,
                    final TaskUtils taskUtils) {
        this.taskListUtils = taskListUtils;
        this.taskUtils = taskUtils;
        this.mainCtrl = mainCtrl;
    }

    private void setDragHandlers(final ListCtrl listCtrl) {
        list.setOnDragDetected(event -> dragDetected(listCtrl, event));
        list.setOnDragEntered(event -> dragEntered(listCtrl, event));
        list.setOnDragOver(event -> dragOver(listCtrl, event));
        list.setOnDragExited(event -> dragExited(listCtrl, event));
        list.setOnDragDropped(event -> dragDropped(listCtrl, event));
        list.setOnDragDone(event -> dragDone(listCtrl, event));
    }
    public void dragDetected(final ListCtrl listCtrl, final MouseEvent event) {
        ListView<Task> lv = listCtrl.list;
        Dragboard dragboard = lv.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent cc = new ClipboardContent();
        if (lv.getSelectionModel().getSelectedItem() == null)
            return;
        var selectedTask=lv.getSelectionModel().getSelectedItem();
        cc.put(taskCustom, selectedTask);
        dragboard.setContent(cc);

        draggedTask = selectedTask;
        totalAmountOfTasks = 0;
        try {
            for(TaskList taskList1 : taskListUtils.getTaskLists(boardID)) {
                totalAmountOfTasks = totalAmountOfTasks + taskList1.getTasks().size();
            }
        } catch (TaskListException e) {
            throw new RuntimeException(e);
        }
        try {
            taskUtils.deleteTask(listCtrl.boardID, listCtrl.taskList.id, selectedTask.id);
        } catch (TaskException e) {
            throw new RuntimeException(e);
        }

        event.consume();
    }

    /**
     * Enters the drag
     * When entered, it will save the task that is currently being dragged in the controller,
     * and it will count the amount of tasks in the board
     *
     * @param listCtrl
     * @param event
     */
    public void dragEntered(final ListCtrl listCtrl, final DragEvent event) {
        ListView<Task> lv = listCtrl.list;
        lv.setStyle("-fx-effect: innershadow(gaussian, rgba(0,0,0,0.8), 20, 0, 0, 0);");
        event.consume();
    }

    public void dragExited(final ListCtrl listCtrl, final DragEvent event) {
        ListView<Task> lv = listCtrl.list;
        lv.setStyle("-fx-effect: none;");
        event.consume();
    }

    public void dragOver(final ListCtrl listCtrl, final DragEvent event) {
        ListView<Task> lv = listCtrl.list;
        if (event.getDragboard().hasContent(taskCustom))
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        event.consume();
    }

    public void dragDropped(final ListCtrl listCtrl, final DragEvent event) {
        ListView<Task> lv = listCtrl.list;
        if (event.getDragboard().hasContent(taskCustom)) {
            Task task = (Task) event.getDragboard().getContent(taskCustom);
            lv.getItems().add(task);

            try {
                taskUtils.addTask(listCtrl.boardID, taskList.id, task);
            } catch (TaskException e) {
                throw new RuntimeException(e);
            }

            event.setDropCompleted(true);
        } else
            event.setDropCompleted(false);
        event.consume();
    }

    /**
     * When the drag is done it will clean everything up in the database and overview
     * If the total amount of task is lower than when the drag started, it will add it back
     *
     * @param listCtrl this list controller
     * @param event the drag event
     */
    public void dragDone(final ListCtrl listCtrl, final DragEvent event) {
        ListView<Task> lv = listCtrl.list;
        Task selectedTask = lv.getSelectionModel().getSelectedItem();
        if (selectedTask != null && event.getTransferMode() == TransferMode.MOVE &&
            event.getEventType()==DragEvent.DRAG_DONE) {
            lv.getItems().remove(selectedTask);
        } else {
            int count = 0;
            try {
                for(TaskList taskList1 : taskListUtils.getTaskLists(boardID)) {
                    count = count + taskList1.getTasks().size();
                }
            } catch (TaskListException e) {
                throw new RuntimeException(e);
            }
            if(count != totalAmountOfTasks) {
                try {
                    taskUtils.addTask(listCtrl.boardID, taskList.id, draggedTask);
                } catch (TaskException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        event.consume();
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        ListCtrl controller = this;
        setDragHandlers(controller);
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
     *
     * @param newTaskList the list for which the tasks must be refreshed.
     * @param boardID the board where the tasklist is situated in
     */
    public void refresh(final TaskList newTaskList, final long boardID) {
        this.boardID = boardID;
        this.taskList = newTaskList;
        nameRefresh(newTaskList.getName(), boardID);

        list.getItems().retainAll(newTaskList.getTasks()); // retain only the tasks
        // that are also in newTaskList
        for (Task task : newTaskList.getTasks()) { // go through all the received tasks
            if (!list.getItems().contains(task)) { // if this task isn't there
                list.getItems().add(task); // add it
            } else {
                if(list.getItems().indexOf(task) != newTaskList.getTasks().indexOf(task)) {
                    hardRefresh(newTaskList, boardID);
                    return;
                }
            }
        }
    }

    /**
     * This hard refresh refreshes the list by completely by removing the old items and adding
     * all the items from the given list
     * It's needed when you change the order in a list
     *
     * @param newTaskList the new tasklist that will replace the old one
     * @param boardID the board where the task is situated in
     */
    public void hardRefresh(final TaskList newTaskList, final long boardID) {
        this.boardID = boardID;
        this.taskList = newTaskList;
        nameRefresh(newTaskList.getName(), boardID);

        list.getItems().setAll(newTaskList.getTasks());// Change the tasklist to the new tasklist
    }

    public void nameRefresh(final String name, final long boardID) {
        if (!Objects.equals(title.getText(), name)) // if the title is different
            title.setText(name); // update it
    }

    /**
     * this adds a task to a specific list
     *
     * @param task the task to be added
     */
    public void addCard(final Task task) {
        try {
            if (list == null) {
                list = new ListView<Task>();
            }
            list.getItems().add(task);
            taskUtils.addTask(boardID, this.getTaskList().id, task);
        } catch (TaskException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * this adds a task to a specific list
     */
    public void addCard() {
        mainCtrl.showCreateTask(this);
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

    public void setServer(final ServerUtils server) {
        this.server = server;
        this.taskUtils = new TaskUtils(server);
        this.taskListUtils = new TaskListUtils(server);
    }
}
