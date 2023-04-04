package client.scenes;

import client.CustomAlert;
import client.customExceptions.BoardException;
import client.utils.*;
import client.customExceptions.TaskListException;
import com.google.inject.Inject;
import client.customExceptions.TaskException;
import commons.Board;
import commons.Task;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListCtrl implements Initializable {
    private static final DataFormat taskCustom = new DataFormat("task.custom");
    private MainCtrl mainCtrl;

    private final CustomAlert customAlert;
    @FXML
    ListView<Task> list;
    @FXML
    Label title;
    @FXML
    Button addTaskButton;
    @FXML
    TextField titleField;
    @FXML
    VBox vBox;

    private int indexToDrop;
    private Task draggedTask;
    private int totalAmountOfTasks;
    private TaskList taskList;
    private long boardID;
    private Board board;
    private TaskListUtils taskListUtils;
    private TaskUtils taskUtils;
    private BoardUtils boardUtils;
    private ServerUtils server;
    @FXML
    TextField simpleTaskNameInput;
    String simpleTaskName;

    private LayoutUtils layoutUtils;

    RenameListSingleton renameListSingleton = RenameListSingleton.getInstance();




    @Inject
    public ListCtrl(final MainCtrl mainCtrl, final TaskListUtils taskListUtils,
                    final TaskUtils taskUtils, final CustomAlert customAlert,
                    final LayoutUtils layoutUtils, final BoardUtils boardUtils) {
        this.taskListUtils = taskListUtils;
        this.taskUtils = taskUtils;
        this.mainCtrl = mainCtrl;
        this.customAlert = customAlert;
        this.layoutUtils = layoutUtils;
        this.boardUtils = boardUtils;
    }

    public void initialize(){
        this.titleField.textProperty().addListener(
                this.layoutUtils.createMaxFieldLength(20, titleField));
        this.titleField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue){
                try {
                    this.onFocusLostTitle();
                } catch (TaskListException e) {
                    System.out.println(e.getMessage());
                }
            }
        }));
    }

    private void setDragHandlers(final ListCtrl listCtrl) {
        list.setOnDragDetected(event -> dragDetected(listCtrl, event));
        list.setOnDragEntered(event -> dragEntered(listCtrl, event));
        list.setOnDragOver(event -> dragOver(listCtrl, event));
        list.setOnDragExited(event -> dragExited(listCtrl, event));
        list.setOnDragDropped(event -> dragDropped(listCtrl, event));
        list.setOnDragDone(event -> dragDone(listCtrl, event));
        list.setOnMouseExited(event -> mouseExited(listCtrl, event));

    }

    private void mouseExited(final ListCtrl listCtrl, final MouseEvent event) {
        ListView<Task> lv = listCtrl.list;
        lv.getSelectionModel().clearSelection();
    }

    public void dragDetected(final ListCtrl listCtrl, final MouseEvent event) {
        ListView<Task> lv = listCtrl.list;
        Dragboard dragboard = lv.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent cc = new ClipboardContent();
        if (lv.getSelectionModel().getSelectedItem() == null)
            return;
        var selectedTask=lv.getSelectionModel().getSelectedItem();
        indexToDrop = lv.getSelectionModel().getSelectedIndex();
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
                if(taskList.getTasks().size() > 0 && indexToDrop >= 0) {
                    TaskList updatedList = taskListUtils.getTaskList(listCtrl.boardID, taskList.id);
                    Optional<Task> optionalTask =
                            updatedList.getTaskById(updatedList.findHighestTaskID());
                    if(optionalTask.isPresent()) {
                        task = optionalTask.get();
                    }
                    taskListUtils.reorderTask(listCtrl.boardID, taskList.id, task.id, indexToDrop);
                    updatedList = taskListUtils.getTaskList(listCtrl.boardID, taskList.id);
                    hardRefresh(updatedList, listCtrl.boardID);
                }
            } catch (TaskException | TaskListException e) {
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

                    TaskList updatedList = taskListUtils.getTaskList(listCtrl.boardID, taskList.id);
                    Optional<Task> optionalTask =
                            updatedList.getTaskById(updatedList.findHighestTaskID());
                    if(optionalTask.isPresent()) {
                        draggedTask = optionalTask.get();
                    }
                    taskListUtils.reorderTask(listCtrl.boardID, taskList.id,
                            draggedTask.id, indexToDrop);

                    taskListUtils.reorderTask(listCtrl.boardID, taskList.id,
                            draggedTask.id, indexToDrop);
                } catch (TaskException | TaskListException e) {
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
        list.setCellFactory(lv -> {
            ListCell<Task> cell = new ListCell<>() {
                @Override
                protected void updateItem(final Task task, final boolean empty) {
                    super.updateItem(task, empty);
                    if (task == null || empty) {
                        setGraphic(null);
                        setBackground(Background.EMPTY);
                    } else {
                        try {
                            setBackground(new Background(new BackgroundFill(Color.
                                    TRANSPARENT, null, null)));
                            var cardLoader = new FXMLLoader(getClass().getResource("Card.fxml"));
                            Node card = cardLoader.load();
                            CardCtrl cardCtrl = cardLoader.getController();
                            cardCtrl.initialize(task, controller, taskListUtils,
                                    customAlert, taskUtils, mainCtrl);
                            setGraphic(card);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            };
            cell.setOnDragEntered(event -> {
                if(cell.getIndex() >= taskList.getTasks().size()) {
                    indexToDrop = taskList.getTasks().size();
                } else {
                    indexToDrop = cell.getIndex();
                }
                event.consume();
            });

            cell.setOnMouseClicked(event -> {
                if(cell.getIndex() < taskList.getTasks().size() && event.getClickCount() == 2) {
                    mainCtrl.showDetailedTaskView(cell.getItem(), this);
                }
            });
            return cell;
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
        refreshColor();

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
            Alert alert = customAlert.showAlert(e.getMessage());
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

    /**
     * deletes list from board by delete button
     * @throws TaskListException
     */
    public void delete() throws TaskListException {
        try {
            long id = getTaskList().id;
            taskListUtils.deleteTaskList(getBoardID(), id);
        } catch (Exception e) {
            throw new TaskListException("Deleting task list unsuccessful");
        }
    }

    /**
     * Fired when the list title is clicked. It hides the Label and shows a textfield instead.
     * @param event the mouse click event
     */
    public void onTitleClicked(final MouseEvent event){
        if(event.getButton() != MouseButton.PRIMARY)
            return;
        this.titleField.setText(this.title.getText());
        this.titleField.setVisible(true);
        this.title.setVisible(false);
    }

    /**
     * Fired when the task list title fields loses the focus. It then checks if the name
     * is different. If it is, the change is sent to the server.
     * @throws TaskListException if there was an error when changing the name to the server.
     */
    public void onFocusLostTitle() throws TaskListException{
        this.title.setText(this.titleField.getText());
        this.titleField.setVisible(false);
        this.title.setVisible(true);
        if(this.titleField.getText().equals(taskList.getName()))
            return;
        try {
            this.taskListUtils.renameTaskList(boardID, taskList.getId(), this.titleField.getText());
        } catch (TaskListException e) {
            throw new TaskListException("Renaming task list unsuccessful");
        }
    }

    public void addSimpleTask() throws TaskException {
        simpleTaskName = simpleTaskNameInput.getText();
        simpleTaskNameInput.clear();
        try {
            if(!simpleTaskName.isEmpty() || simpleTaskName != null) {
                Task task = new Task(simpleTaskName, null);
                taskUtils.addTask(boardID, taskList.id, task);
            }
        } catch (TaskException e) {
            throw new TaskException("Task must have a name.");
        }

    }

    public void refreshColor(){
        try {
            board = boardUtils.getBoard(getBoardID());
            list.setStyle("-fx-background-color:#" + board.getBoardColorScheme().
                    getListBackgroundColor().substring(2, 8) + ";");

            title.setStyle("-fx-text-fill:#" + board.getBoardColorScheme().
                    getListTextColor().substring(2, 8) + ";");
            addTaskButton.setStyle("-fx-text-fill:#" + board.getBoardColorScheme().
                    getListTextColor().substring(2, 8) + ";");
        } catch (BoardException e) {
            throw new RuntimeException(e);
        }


    }

}
