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
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class BoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final DataFormat taskCustom = new DataFormat("task.custom");
    private final Map<Long, VBox> listsMap;
    private final Map<Long, Map<Long, Integer>> tasksMap;
    @FXML
    private HBox listsContainer;

    @Inject
    public BoardOverviewCtrl(final ServerUtils server, final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.listsMap = new HashMap<>();
        this.tasksMap = new HashMap<>();
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
     * adds a list to the board
     * @param taskList the list to be added
     */
    public void addList(final TaskList taskList) {
        var kids = listsContainer.getChildren();
        var newList = new ListView<String>();

        newList.setPrefWidth(200);
        setDragHandlers(newList);
        if (!kids.isEmpty()) {
            var lb = kids.get(kids.size() - 1).getLayoutBounds();
            var lx = kids.get(kids.size() - 1).getLayoutX();
            newList.setLayoutX(lx + lb.getMaxX());
        }
        VBox newVBox = new VBox();
        newList.getItems().addAll(convertTasksToCards(taskList));

        newVBox.getChildren().add(new Text(taskList.getName()));
        newVBox.getChildren().add(newList);

        kids.add(newVBox);
        listsMap.put(taskList.id, newVBox);
        tasksMap.put(taskList.id, new HashMap<>());
    }

    private void setDragHandlers(final ListView<String> list) {
        list.setOnDragDetected(event -> dragDetected(list, event));
        list.setOnDragEntered(event -> dragEntered(list, event));
        list.setOnDragOver(event -> dragOver(list, event));
        list.setOnDragExited(event -> dragExited(list, event));
        list.setOnDragDropped(event -> dragDropped(list, event));
        list.setOnDragDone(event -> dragDone(list, event));
    }

    public void dragDetected(final ListView<String> lv, final MouseEvent event) {
        Dragboard dragboard = lv.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent cc = new ClipboardContent();
        if (lv.getSelectionModel().getSelectedItem() == null)
            return;
        cc.put(taskCustom, lv.getSelectionModel().getSelectedItem());
        dragboard.setContent(cc);
        event.consume();
    }

    public void dragEntered(final ListView<String> lv, final DragEvent event) {
        lv.setStyle("-fx-effect: innershadow(gaussian, rgba(0,0,0,0.8), 20, 0, 0, 0);");
        event.consume();
    }

    public void dragExited(final ListView<String> lv, final DragEvent event) {
        lv.setStyle("-fx-effect: none;");
        event.consume();
    }

    public void dragOver(final ListView<String> lv, final DragEvent event) {
        if (event.getDragboard().hasContent(taskCustom))
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        event.consume();
    }

    public void dragDropped(final ListView<String> lv, final DragEvent event) {
        if (event.getDragboard().hasContent(taskCustom)) {
            lv.getItems().add((String) event.getDragboard().getContent(taskCustom));
            event.setDropCompleted(true);
        } else
            event.setDropCompleted(false);
        event.consume();
    }

    public void dragDone(final ListView<String> lv, final DragEvent event) {
        if (event.getTransferMode() == TransferMode.MOVE) {
            lv.getItems().remove(lv.getSelectionModel().getSelectedItem());
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
    }

    /**
     * This method refreshes the board overview.
     */
    private void refresh() {
        var data = server.getLists();
        //System.out.println(data);
        data = FXCollections.observableList(data);
        refreshLists(data);
    }

    /**
     * This method refreshes the lists of the board.
     * @param lists the list of lists
     */
    private void refreshLists(final List<TaskList> lists) {
        List<Long> listsId=lists.stream().map(taskList -> taskList.id).toList();
        for (Map.Entry<Long, VBox> list : listsMap.entrySet()) { // this removes any lists in excess
            if(!listsId.contains(list.getKey()))
                listsMap.remove(list.getKey());
        }
        for (TaskList list : lists) { // this adds or modifies existing lists
            if (!listsMap.containsKey(list.id))
                addList(list);
            else {
                Text title = (Text) listsMap.get(list.id).getChildren().get(0);
                if (!Objects.equals(title.getText(), list.getName()))
                    title.setText(list.getName());
                refreshTasks(list);
            }
        }
    }

    /**
     * This refreshes the tasks of the list.
     * @param newTaskList the list for which the tasks must be refreshed.
     */
    private void refreshTasks(final TaskList newTaskList) {
        ListView<String> currentTasks =
            (ListView<String>) listsMap.get(newTaskList.id).getChildren().get(1);
        currentTasks.getItems().retainAll(convertTasksToCards(newTaskList));
        for (Task task : newTaskList.getTasks()) {
            if (!currentTasks.getItems().contains(task.getName())) //TODO: make this based on id
                currentTasks.getItems().add(task.getName()); //TODO: also should support ordering
        }
    }
    /* private void refreshTasks(final TaskList newTaskList) {
        ListView<String> displayedTasks =
            (ListView<String>) listsMap.get(newTaskList.id).getChildren().get(1);
        List<Long> newTaskIds=newTaskList.getTasks().stream().map(task -> task.id).toList();
        for (Map.Entry<Long, Integer> entry : tasksMap.get(newTaskList.id).entrySet()) {
            if(!newTaskIds.contains(entry.getKey())){
                displayedTasks.getItems().remove((int)
                tasksMap.get(newTaskList.id).remove(entry.getKey()));
            }
        }
        for (Task task : newTaskList.getTasks()) {
            if (!tasksMap.get(newTaskList.id).containsKey(task.id)) {
                displayedTasks.getItems().add(task.getName());
                tasksMap.get(newTaskList.id).put(task.id, displayedTasks.getItems().size()-1);
            }
        }
    }*/

    /**
     * converts a TaskList to a list of strings which contains the titles of the tasks
     * @param taskList the list that must be converted
     * @return an array of titles of the cards
     */
    private String[] convertTasksToCards(final TaskList taskList) {
        return taskList.getTasks().stream().map(Task::getName).toArray(String[]::new);
    }
}