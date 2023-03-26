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

//import client.sceneManagement.ListScenes;
import client.CustomAlert;
import client.sceneManagement.ListScenes;
import client.sceneManagement.ServerScenes;
import client.sceneManagement.TaskScenes;
import client.scenes.connectScenes.SelectServerCtrl;
import client.scenes.connectScenes.ServerTimeoutCtrl;
import client.scenes.connectScenes.UnexpectedErrorCtrl;
import client.scenes.connectScenes.WrongServerCtrl;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;


public class MainCtrl {

    private Stage primaryStage;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverview;

    private Scene createList;
    private CreateListCtrl createListCtrl;

    private CreateTaskCtrl createTaskCtrl;

    private Scene createTask;

    private EditTaskCtrl editTaskCtrl;
    private Scene editTask;

    private RenameListCtrl renameListCtrl;
    private Scene renameList;

    private SelectServerCtrl selectServerCtrl;

    private Scene selectServer;

    private WrongServerCtrl wrongServerCtrl;
    private Scene wrongServer;

    private ServerTimeoutCtrl serverTimeoutCtrl;
    private Scene serverTimeout;

    private UnexpectedErrorCtrl unexpectedErrorCtrl;
    private Scene unexpectedError;



    /**
     * Initializes the main controller.
     * @param primaryStage the primary stage
     * @param boardOverview the board overview scene
     * @param listScenes the list scenes
     * @param serverScenes the server scenes
     */
    public void initialize(final Stage primaryStage,
                           final Pair<BoardOverviewCtrl, Parent> boardOverview,
//                           final Pair<CreateListCtrl, Parent> createList,
//                           final Pair<DeleteListCtrl, Parent> deleteList,
//                           final Pair<RenameListCtrl, Parent> renameList,
                           final ListScenes listScenes,
                           final ServerScenes serverScenes,
                           final TaskScenes taskScenes){
        this.primaryStage = primaryStage;
        primaryStage.getIcons().add(new javafx.scene
                .image.Image("file:src/main/resources/client/images/icon.png"));

        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());

        this.createTaskCtrl = taskScenes.getCreateTask().getKey();
        this.createTask = new Scene(taskScenes.getCreateTask().getValue());

        this.editTaskCtrl = taskScenes.getEditTask().getKey();
        this.editTask = new Scene(taskScenes.getEditTask().getValue());

        this.createListCtrl = listScenes.getCreateList().getKey();
        this.createList = new Scene(listScenes.getCreateList().getValue());


        this.renameListCtrl = listScenes.getRenameList().getKey();
        this.renameList = new Scene(listScenes.getRenameList().getValue());

        this.selectServerCtrl = serverScenes.getSelectServer().getKey();
        this.selectServer = new Scene(serverScenes.getSelectServer().getValue());

        this.wrongServerCtrl = serverScenes.getWrongServer().getKey();
        this.wrongServer = new Scene(serverScenes.getWrongServer().getValue());

        this.serverTimeoutCtrl = serverScenes.getServerTimeout().getKey();
        this.serverTimeout = new Scene(serverScenes.getServerTimeout().getValue());

        this.unexpectedErrorCtrl = serverScenes.getUnexpectedError().getKey();
        this.unexpectedError = new Scene(serverScenes.getUnexpectedError().getValue());

        showSelectServer();
        primaryStage.show();
    }

    /**
     * Shows the board overview scene.
     */
    public void showBoardOverview() {
        primaryStage.setTitle("Talio: Board Overview");
        boardOverviewCtrl.refreshTimer(500);
        primaryStage.setScene(boardOverview);
    }

    /**
     * Changes the scene to the popup that allows users to create a new task list and name it.
     */
    public void showCreateList() {
        primaryStage.setTitle("Talio: Create List");
        primaryStage.setScene(createList);
    }

    /**
     * Changes the scene to the popup that allows users to create a new task and name it.
     */
    public void showCreateTask(final ListCtrl ctrl) {
        primaryStage.setTitle("Talio: Create Task");
        primaryStage.setScene(createTask);
        createTaskCtrl.setListCtrl(ctrl);
    }

    /**
     * Changed scene to a scene where a user can edit a task
     *
     * @param cardCtrl    the cradCtrl from that specific task
     * @param customAlert
     */
    public void showEditTask(final CardCtrl cardCtrl, CustomAlert customAlert) {
        primaryStage.setTitle("Talio : Edit Task");
        primaryStage.setScene(editTask);
        editTaskCtrl.setCardCtrl(cardCtrl);
        editTaskCtrl.setCustomAlert(customAlert);
    }


    /**
     * Changes scene to a scene where a user can rename a task list.
     */
    public void showRenameList() {
        primaryStage.setTitle("Talio: Rename List");
        primaryStage.setScene(renameList);
    }

    /**
     * Shows the select server scene.
     */
    public void showSelectServer() {
        primaryStage.setTitle("Talio: Select Your Server");
        primaryStage.setScene(selectServer);
    }
    /**
     * Shows the wrong server scene.
     */
    public void showWrongServer() {
        primaryStage.setTitle("Talio: server not found");
        primaryStage.setScene(wrongServer);

    }
    /**
     * Shows the server timeout scene.
     */
    public void showTimeout() {
        primaryStage.setTitle("Talio: server timed out");
        primaryStage.setScene(serverTimeout);
    }
    /**
     * Shows the unexpected error scene.
     */
    public void showUnexpectedError() {
        primaryStage.setTitle("Talio: unexpected error");
        primaryStage.setScene(unexpectedError);
    }
}