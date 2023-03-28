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

import client.CustomAlert;
import client.customExceptions.BoardException;
import client.customExceptions.TaskListException;
import client.utils.BoardUtils;
import client.utils.ServerUtils;
import client.utils.TaskListUtils;
import client.utils.TaskUtils;
import com.google.inject.Inject;
import commons.TaskList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class BoardOverviewCtrl {

    private final ServerUtils server;
    private final TaskListUtils taskListUtils;
    private final BoardUtils boardUtils;
    private final BoardCatalogueCtrl boardCatalogueCtrl;
    private final MainCtrl mainCtrl;
    private final CustomAlert customAlert;
    private final Map<Long, ListCtrl> listsMap;
    private long currentBoardId;

    private List<TaskList> taskLists;

    private Timer refreshTimer;

    @FXML
    private HBox listsContainer;

    @FXML

    private Label inviteKeyLabel;

    @Inject
    public BoardOverviewCtrl(final ServerUtils server, final MainCtrl mainCtrl,
                             final CustomAlert customAlert, final BoardUtils boardUtils,
                             final BoardCatalogueCtrl boardCatalogueCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.taskListUtils = new TaskListUtils(server);
        this.boardUtils = boardUtils;
        this.listsMap = new HashMap<>();
        this.taskLists = new ArrayList<>();
        this.customAlert = customAlert;
        this.boardCatalogueCtrl=boardCatalogueCtrl;
    }

    /**
     * adds a list to the board
     * @param taskList the list to be added
     */
    public void addList(final TaskList taskList) {
        var kids = listsContainer.getChildren();
        var listLoader = new FXMLLoader(getClass().getResource("List.fxml"));
        listLoader.setControllerFactory(type -> new ListCtrl(mainCtrl, new TaskListUtils(server),
            new TaskUtils(server), customAlert));
        try {
            Node list = listLoader.load();
            ListCtrl listCtrl = listLoader.getController();
            listCtrl.refresh(taskList, currentBoardId);
            listCtrl.setServer(server);
            if (!kids.isEmpty()) {
                var lb = kids.get(kids.size() - 1).getLayoutBounds();
                var lx = kids.get(kids.size() - 1).getLayoutX();
                listCtrl.setLayoutX(lx + lb.getMaxX());
            }
            kids.add(list);
            listsMap.put(taskList.id, listCtrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addList() {
        mainCtrl.showCreateList(currentBoardId);
    }

    public void addTask() {
        //mainCtrl.showCreateTask();
    }



    public void renameList() {
        mainCtrl.showRenameList();
    }


    /*
    This method is used for enabling clients to switch the server
    It is attached to a button in the board overview scene
    Currently all it does is switch the scene but
     */
    public void switchServer() {
        boardCatalogueCtrl.close();
        mainCtrl.showSelectServer();
        server.disconnect();
    }

    /**
     * This creates and runs a refresh timer at a specified period
     *
     * @param refreshPeriod the time period in miliseconds
     */
    public void refreshTimer(final long refreshPeriod) {
        if(refreshTimer==null)
            refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        }, 0, refreshPeriod);
    }

    /**
     * This method refreshes the board overview.
     */
    public void refresh() {
        Platform.runLater(() -> {
            try {
                taskLists = taskListUtils.getTaskLists(currentBoardId);
            } catch (TaskListException e) {
                Alert alert = customAlert.showAlert(e.getMessage());
                alert.showAndWait();
            }
            //System.out.println(data);
            var data = FXCollections.observableList(taskLists);
            refreshLists(data);
        });
    }

    /**
     * This method refreshes the lists of the board.
     * @param lists the list of lists
     */
    private void refreshLists(final List<TaskList> lists) {
        List<Long> listsId = lists.stream().map(taskList -> taskList.id).toList();
        Iterator<Map.Entry<Long, ListCtrl>> iter=listsMap.entrySet().iterator();
        while (iter.hasNext()){
            // this removes any lists in excess
            var current=iter.next();
            if (!listsId.contains(current.getKey())) {
                listsContainer.getChildren().remove(current.getValue().getRoot());
                iter.remove();
            }
        }

        for (TaskList list : lists) {
            // this adds or modifies existing lists
            if (!listsMap.containsKey(list.id)) // if the list is new
                addList(list); // simply add it
            else { // if the list was already there
                listsMap.get(list.id).refresh(list, currentBoardId);
            }
        }
    }

    public void setCurrentBoardId(final long currentBoardId) {
        this.currentBoardId = currentBoardId;
    }

    public void clear() {
        refreshTimer.cancel();
        refreshTimer.purge();
        listsContainer.getChildren().clear();
        listsMap.clear();
    }

    public List<TaskList> getTaskLists() {
        return taskLists;
    }

    public long getCurrentBoardId() {
        return this.currentBoardId;
    }

    public void copyInviteKey() {
        try {
            String inviteKey = boardUtils.getBoardInviteKey(currentBoardId);
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(inviteKey);
            clipboard.setContent(content);
            GaussianBlur blur = new GaussianBlur();
            inviteKeyLabel.setEffect(blur);
            inviteKeyLabel.setText("Invite key: " + inviteKey);
            inviteKeyLabel.setVisible(true);

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0), new KeyValue(blur.radiusProperty(), 0)),
                    new KeyFrame(Duration.seconds(2), new KeyValue(blur.radiusProperty(), 10))
            );
            timeline.play();
            timeline.setOnFinished( event -> inviteKeyLabel.setVisible(false));
        } catch (BoardException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
        }
    }
}
