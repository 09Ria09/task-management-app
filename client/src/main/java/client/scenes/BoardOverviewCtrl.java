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
import client.utils.*;
import com.google.inject.Inject;
import commons.Board;
import commons.Task;
import commons.TaskList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class BoardOverviewCtrl {

    private final ServerUtils server;
    private final WebSocketUtils webSocketUtils;
    private final TaskListUtils taskListUtils;
    private final BoardUtils boardUtils;
    private final BoardCatalogueCtrl boardCatalogueCtrl;
    private final MainCtrl mainCtrl;
    private final CustomAlert customAlert;
    private final Map<Long, ListCtrl> listsMap;
    private long currentBoardId;
    private Tab tab;
    private List<TaskList> taskLists;
    private Board board;

    private Timer refreshTimer;

    private final EditBoardCtrl editBoardCtrl;

    @FXML
    private HBox listsContainer;

    @FXML
    private GridPane buttonsGridPane;

    @FXML

    private Label inviteKeyLabel;

    @FXML
    private ScrollPane listScrollPane;
    @FXML
    private Button disconnectButton;
    @FXML
    private Button colorManagementViewButton;
    @FXML
    private Button addListButton;
    @FXML
    private Button copyInviteKeyButton;
    @FXML
    private Button renameBoardButton;
    @FXML
    private Button deleteBoardButton;

    @FXML
    private Button tagOverviewButton;

    @Inject
    public BoardOverviewCtrl(final MainCtrl mainCtrl,
                             final CustomAlert customAlert, final BoardUtils boardUtils,
                             final BoardCatalogueCtrl boardCatalogueCtrl,
                             final EditBoardCtrl editBoardCtrl,
                             final WebSocketUtils webSocketUtils) {
        this.mainCtrl = mainCtrl;
        this.server = webSocketUtils.getServerUtils();
        this.taskListUtils = new TaskListUtils(server);
        this.listsMap = new HashMap<>();
        this.taskLists = new ArrayList<>();
        this.customAlert = customAlert;
        this.boardCatalogueCtrl = boardCatalogueCtrl;
        this.boardUtils = boardUtils;
        this.webSocketUtils = webSocketUtils;
        this.editBoardCtrl = editBoardCtrl;
    }

    public void initialize() {
        this.webSocketUtils.tryToConnect();
        try {
            refresh();
            board = boardUtils.getBoard(currentBoardId);
            Consumer<Board> consumer = (board) -> {
                Platform.runLater(() -> {
                    this.board = board;
                    this.taskLists = this.board.getListTaskList();
                    refreshLists(FXCollections.observableList(taskLists));
                });
            };
            Consumer<Task> changeTaskTag = (task) -> {
                Platform.runLater(() -> {
                    for (TaskList l : this.board.getListTaskList())
                        l.getTaskById(task.id).ifPresent((t) -> {
                            List<Task> tasks = listsMap.get(l.id).list.getItems();
                            int index = tasks.indexOf(tasks.stream()
                                    .filter(t2 -> t2.id == t.id)
                                    .findAny().orElse(null));
                            if (index >= 0) {
                                tasks.remove(index);
                                tasks.add(index, task);
                            }
                        });
                });
            };
            webSocketUtils.registerForBoardMessages("/topic/" + board.id +
                    "/refreshboard", consumer);
            webSocketUtils.registerForTaskMessages("/topic/" + board.id + "/modifytask",
                    changeTaskTag);
            Consumer<Board> changeColor = (board) -> {
                Platform.runLater(() -> {
                    this.board.setBoardColorScheme(board.getBoardColorScheme());
                    this.refreshColor();
                    for(ListCtrl l : this.listsMap.values())
                        l.refreshColor(this.board);
                });
            };
            webSocketUtils.registerForBoardMessages("/topic/" + board.id + "/changecolor",
                    changeColor);
        }
        catch(BoardException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * adds a list to the board
     *
     * @param taskList the list to be added
     */
    public void addList(final TaskList taskList) {
        var kids = listsContainer.getChildren();
        var listLoader = new FXMLLoader(getClass().getResource("List.fxml"));
        listLoader.setControllerFactory(type ->
                new ListCtrl(mainCtrl, new TaskListUtils(server),
                        new TaskUtils(server), customAlert,
                        boardUtils, new Pair(new LayoutUtils(), webSocketUtils)));
        try {
            VBox list = listLoader.load();
            list.prefHeightProperty().bind(Bindings
                    .multiply(listScrollPane.heightProperty(), 0.95));
            ListCtrl listCtrl = listLoader.getController();
            listCtrl.initialize();
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


    /*
    This method is used for enabling clients to switch the server
    It is attached to a button in the board overview scene
    Currently all it does is switch the scene but
     */
    public void switchServer() {
        boardCatalogueCtrl.close();
        mainCtrl.showSelectServer();
        server.disconnect();
        webSocketUtils.disconnect();
    }

    /**
     * This creates and runs a refresh timer at a specified period
     *
     * @param refreshPeriod the time period in miliseconds
     */
    public void refreshTimer(final long refreshPeriod) {
        if (refreshTimer == null)
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
                board = boardUtils.getBoard(currentBoardId);
                taskLists = taskListUtils.getTaskLists(currentBoardId);
                tab.setText(board.getName());
                var data = FXCollections.observableList(taskLists);
                refreshLists(data);
                refreshColor();
            } catch (TaskListException e) {
                Alert alert = customAlert.showAlert(e.getMessage());
                alert.showAndWait();
            } catch (BoardException e) {
                tab.getOnClosed().handle(null);
            }
        });
    }

    /**
     * This method refreshes the lists of the board.
     *
     * @param lists the list of lists
     */
    private void refreshLists(final List<TaskList> lists) {
        List<Long> listsId = lists.stream().map(taskList -> taskList.id).toList();
        Iterator<Map.Entry<Long, ListCtrl>> iter = listsMap.entrySet().iterator();
        while (iter.hasNext()) {
            // this removes any lists in excess
            var current = iter.next();
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
        if (refreshTimer != null) {
            refreshTimer.cancel();
            refreshTimer.purge();
            refreshTimer = null;
        }
        listsContainer.getChildren().clear();
        listsMap.clear();
    }

    public List<TaskList> getTaskLists() {
        return taskLists;
    }

    public long getCurrentBoardId() {
        return this.currentBoardId;
    }

    public Board deleteBoard() throws BoardException {
        Long idToDelete = getCurrentBoardId();
        System.out.println(idToDelete);
        Board board = boardUtils.deleteBoard(idToDelete);
        refresh();
        return board;
    }

    public Board renameBoard() throws BoardException {
        Board board = boardUtils.getBoard(currentBoardId);
        editBoardCtrl.setBoard(board);
        mainCtrl.showEditBoard();
        refresh();
        return board;
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
            refreshColor();
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(0), new KeyValue(blur.radiusProperty(), 0)),
                    new KeyFrame(Duration.millis(1000), new KeyValue(blur.radiusProperty(), 0)),
                    new KeyFrame(Duration.millis(1500), new KeyValue(blur.radiusProperty(), 10))
            );
            timeline.play();
            timeline.setOnFinished(event -> inviteKeyLabel.setVisible(false));
        } catch (BoardException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
        }
    }

    public void tagOverview() throws BoardException {
        Board board = boardUtils.getBoard(currentBoardId);
        mainCtrl.showTagOverview(board);
    }

    public void setTab(final Tab tab) {
        this.tab = tab;
        tab.setOnSelectionChanged(event -> {
            if (tab.isSelected()) {
                refresh();
                refreshTimer(5000000);
            } else {
                if (refreshTimer == null)
                    return;
                refreshTimer.cancel();
                refreshTimer.purge();
                refreshTimer = null;
            }
        });
    }

    public void colorManagementView() {
        mainCtrl.showColorManagementView(board);
    }


    public Board getBoard() {
        return board;
    }

    public void refreshColor(){
        try{
            board.getBoardColorScheme().
                    setBoardTextColor(boardUtils.
                            getBoardColorScheme(board.id).getBoardTextColor());
            System.out.println(boardUtils.
                    getBoardColorScheme(board.id).getBoardBackgroundColor());
            String hexColor = board.getBoardColorScheme().getBoardBackgroundColor().substring(2, 8);
            String textColor = board.getBoardColorScheme().getBoardTextColor().substring(2, 8);
            int red = Integer.parseInt(hexColor.substring(0, 2), 16);
            int green = Integer.parseInt(hexColor.substring(2, 4), 16);
            int blue = Integer.parseInt(hexColor.substring(4, 6), 16);
            tab.setStyle("-fx-text-base-color: #" + textColor + ";");
            double alpha = 0.7;
            listScrollPane.setStyle("-fx-background:#" + board.getBoardColorScheme()
                    .getBoardBackgroundColor().substring(2, 8) + ";");
            buttonsGridPane.setStyle(String
                    .format("-fx-background-color: rgba(%d, %d, %d, %.1f);",
                            red, green, blue, alpha));
        }
        catch(BoardException e){
            System.out.println("Error when changing colors: " + e.getMessage());
        }

    }
}

