/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class BoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final DataFormat taskCustom = new DataFormat("task.custom");
    private final Map<Long, Integer> listsMap;
    private long currentBoardId;

    @FXML
    private HBox listsContainer;

    @Inject
    public BoardOverviewCtrl(final ServerUtils server, final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.listsMap = new HashMap<>();
    }

    /**
     * Initializes the board overview by setting the board to refresh at a fixed rate
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> refresh()); // Platform.runLater() had to be used to prevent
                // thread-caused errors
            }
        }, 0, 500);
    }

    /**
     * this adds a task to a specific list
     *
     * @param task the task to be added
     * @param list the list which will receive the task
     */
    public void addCard(final Task task, final long list) {
        ((ListView<Task>) getVBox(list).getChildren().get(1)).getItems().add(task);
    }

    /**
     * adds a list to the board
     * @param taskList the list to be added
     */
    public void addList(final TaskList taskList) {
        var kids = listsContainer.getChildren();
        var newList = new ListView<Task>();
        newList.setPrefWidth(200);
        setDragHandlers(newList);
        newList.setCellFactory(param -> new ListCell<>() {
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
                        cardCtrl.initialize(task);
                        setGraphic(card);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        if (!kids.isEmpty()) {
            var lb = kids.get(kids.size() - 1).getLayoutBounds();
            var lx = kids.get(kids.size() - 1).getLayoutX();
            newList.setLayoutX(lx + lb.getMaxX());
        }
        VBox newVBox = new VBox();
        newList.getItems().addAll(taskList.getTasks());

        newVBox.getChildren().add(new Text(taskList.getName()));
        newVBox.getChildren().add(newList);

        kids.add(newVBox);
        listsMap.put(taskList.id, kids.size() - 1);
    }

    private void setDragHandlers(final ListView<Task> list) {
        list.setOnDragDetected(event -> dragDetected(list, event));
        list.setOnDragEntered(event -> dragEntered(list, event));
        list.setOnDragOver(event -> dragOver(list, event));
        list.setOnDragExited(event -> dragExited(list, event));
        list.setOnDragDropped(event -> dragDropped(list, event));
        list.setOnDragDone(event -> dragDone(list, event));
    }

    public void addList() {
        mainCtrl.showCreateList();
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

    /*
    This method is used for enabling clients to switch the server
    It is attached to a button in the board overview scene
    Currently all it does is switch the scene but
     */
    public void switchServer() {
        mainCtrl.showSelectServer();
        server.disconnect();
    }

    /**
     * This method refreshes the board overview.
     */
    private void refresh() {
        if(server.isTalioServer().isPresent()){
            return;
        }
        var data = server.getLists(currentBoardId);
        //System.out.println(data);
        data = FXCollections.observableList(data);
        refreshLists(data);
    }

    /**
     * This method refreshes the lists of the board.
     * @param lists the list of lists
     */
    private void refreshLists(final List<TaskList> lists) {
        List<Long> listsId = lists.stream().map(taskList -> taskList.id).toList();
        for (Map.Entry<Long, Integer> list : listsMap.entrySet()) {
            // this removes any lists in excess
            if (!listsId.contains(list.getKey())) {
                listsContainer.getChildren().remove((int) list.getValue());
                listsMap.remove(list.getKey());
            }
        }

        for (TaskList list : lists) {
            // this adds or modifies existing lists
            if (!listsMap.containsKey(list.id)) // if the list is new
                addList(list); // simply add it
            else { // if the list was already there
                Text title = (Text) getVBox(list.id).getChildren().get(0); // get the title
                if (!Objects.equals(title.getText(), list.getName())) // if the title is different
                    title.setText(list.getName()); // update it
                refreshTasks(list); // refresh the tasks
            }
        }
    }

    /**
     * This refreshes the tasks of the list.
     * @param newTaskList the list for which the tasks must be refreshed.
     */
    private void refreshTasks(final TaskList newTaskList) {
        ListView<Task> currentTasks =
            (ListView<Task>) getVBox(newTaskList.id).getChildren().get(1);

        currentTasks.getItems().retainAll(newTaskList.getTasks()); // retain only the tasks
        // that are also in newTaskList
        for (Task task : newTaskList.getTasks()) { // go thru all the received tasks
            if (!currentTasks.getItems().contains(task)) // if this task isn't there
                currentTasks.getItems().add(task); // add it
        }
    }

    /**
     * Get VBox matching the provided id
     * @param id TaskList id
     * @return the VBox
     */
    private VBox getVBox(final long id){
        return (VBox) listsContainer.getChildren().get(listsMap.get(id));
    }

    public void setCurrentBoardId(final long currentBoardId) {
        this.currentBoardId = currentBoardId;
    }
}
