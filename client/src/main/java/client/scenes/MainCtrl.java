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
import client.sceneManagement.BoardScenes;
import client.sceneManagement.ListScenes;
import client.sceneManagement.ServerScenes;
import client.sceneManagement.TaskScenes;
import client.scenes.adminScenes.AdminBoardCtrl;
import client.scenes.adminScenes.AdminLoginCtrl;
import client.scenes.connectScenes.SelectServerCtrl;
import client.scenes.connectScenes.ServerTimeoutCtrl;
import client.scenes.connectScenes.UnexpectedErrorCtrl;
import client.scenes.connectScenes.WrongServerCtrl;
import commons.Board;
import commons.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Pair;


public class MainCtrl {

    private Stage primaryStage;

    private Scene createList;
    private CreateListCtrl createListCtrl;

    private CreateTaskCtrl createTaskCtrl;
    private Scene createTask;

    private EditTaskCtrl editTaskCtrl;
    private Scene editTask;

    private DetailedTaskViewCtrl detailedTaskViewCtrl;
    private Scene detailedTaskView;

    private Scene renameList;

    private SelectServerCtrl selectServerCtrl;

    private Scene selectServer;

    private WrongServerCtrl wrongServerCtrl;
    private Scene wrongServer;

    private ServerTimeoutCtrl serverTimeoutCtrl;
    private Scene serverTimeout;

    private UnexpectedErrorCtrl unexpectedErrorCtrl;
    private Scene unexpectedError;
    private BoardCatalogueCtrl boardCatalogueCtrl;
    private Scene boardCatalogue;

    private EditBoardCtrl editBoardCtrl;

    private Scene editBoard;

    private ColorManagementViewCtrl colorManagementViewCtrl;
    private Scene colorManagementView;
    private TagOverviewCtrl tagOverviewCtrl;
    private Scene tagOverview;

    private AdminLoginCtrl adminLoginCtrl;
    private AdminBoardCtrl adminBoardCtrl;

    private Scene adminLogin;
    private Scene adminBoards;



    /**
     * Initializes the main controller.
     */
    public void initialize(final Stage primaryStage,
                           final ListScenes listScenes,
                           final ServerScenes serverScenes,
                           final TaskScenes taskScenes,
                           final Pair<BoardCatalogueCtrl, Parent> boardCatalogue,
                           final BoardScenes boardScenes){
        this.primaryStage = primaryStage;
        primaryStage.getIcons().add(new javafx.scene
                .image.Image("file:src/main/resources/client/images/icon.png"));
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1380);

        this.createTaskCtrl = taskScenes.getCreateTask().getKey();
        this.createTask = new Scene(taskScenes.getCreateTask().getValue());
        this.editTaskCtrl = taskScenes.getEditTask().getKey();
        this.editTask = new Scene(taskScenes.getEditTask().getValue());
        this.detailedTaskViewCtrl = taskScenes.getDetailedTaskView().getKey();
        this.detailedTaskView = new Scene(taskScenes.getDetailedTaskView().getValue());

        this.createListCtrl = listScenes.getCreateList().getKey();
        this.createList = new Scene(listScenes.getCreateList().getValue());


        this.selectServerCtrl = serverScenes.getSelectServer().getKey();
        this.selectServer = new Scene(serverScenes.getSelectServer().getValue());
        this.wrongServerCtrl = serverScenes.getWrongServer().getKey();
        this.wrongServer = new Scene(serverScenes.getWrongServer().getValue());
        this.serverTimeoutCtrl = serverScenes.getServerTimeout().getKey();
        this.serverTimeout = new Scene(serverScenes.getServerTimeout().getValue());
        this.unexpectedErrorCtrl = serverScenes.getUnexpectedError().getKey();
        this.unexpectedError = new Scene(serverScenes.getUnexpectedError().getValue());

        this.boardCatalogueCtrl=boardCatalogue.getKey();
        this.boardCatalogue=new Scene(boardCatalogue.getValue());
        primaryStage.setOnCloseRequest(e-> {
            boardCatalogue.getKey().close();
        });

        this.colorManagementViewCtrl = boardScenes.getColorManagementView().getKey();
        this.colorManagementView = new Scene(boardScenes.getColorManagementView().getValue());

        this.editBoardCtrl = boardScenes.getEditBoard().getKey();
        this.editBoard = new Scene(boardScenes.getEditBoard().getValue());
        this.tagOverviewCtrl = boardScenes.getTagOverview().getKey();
        this.tagOverview = new Scene(boardScenes.getTagOverview().getValue());
        this.adminLoginCtrl = serverScenes.getAdminLogin().getKey();
        this.adminLogin = new Scene(serverScenes.getAdminLogin().getValue());
        this.adminBoardCtrl = serverScenes.getAdminBoard().getKey();
        this.adminBoards = new Scene(serverScenes.getAdminBoard().getValue());

        showSelectServer();
        primaryStage.show();
    }

    public void showAdminLogin() {
        primaryStage.setTitle("Talio: Admin Portal");
        resize();
        primaryStage.setScene(adminLogin);
    }

    public void showAdminBoard() {
        primaryStage.setTitle("Talio: Admin Boards");
        resize();
        primaryStage.setScene(adminBoards);
    }

    /**
     * Changes the scene to the popup that allows users to create a new task list and name it.
     */
    public void showCreateList(final long boardId) {
        primaryStage.setTitle("Talio: Create List");
        createListCtrl.boardId=boardId;
        resize();
        primaryStage.setScene(createList);
    }

    /**
     * Changes the scene to the tab pane of boards.
     */
    public void showBoardCatalogue() {
        primaryStage.setTitle("Talio");
        resize();
        primaryStage.setScene(boardCatalogue);
        boardCatalogueCtrl.refresh();
    }

    public void showEditBoard() {
        primaryStage.setTitle("Talio: Edit Board");
        resize();
        primaryStage.setScene(editBoard);
    }

    /**
     * Changes the scene to the popup that allows users to create a new task and name it.
     */
    public void showCreateTask(final ListCtrl ctrl) {
        primaryStage.setTitle("Talio: Create Task");
        resize();
        createTaskCtrl.setListCtrl(ctrl);
        primaryStage.setScene(createTask);
    }

    /**
     * Changed scene to a scene where a user can edit a task
     *
     * @param cardCtrl    the cradCtrl from that specific task
     * @param customAlert
     */
    public void showEditTask(final CardCtrl cardCtrl, final CustomAlert customAlert) {
        primaryStage.setTitle("Talio : Edit Task");
        resize();
        primaryStage.setScene(editTask);
        editTaskCtrl.setCardCtrl(cardCtrl);
        editTaskCtrl.setCustomAlert(customAlert);
    }


    /**
     * Changes scene to a scene where a user can rename a task list.
     */
    public void showRenameList() {
        primaryStage.setTitle("Talio: Rename List");
        resize();
        primaryStage.setScene(renameList);
    }

    /**
     * Shows the select server scene.
     */
    public void showSelectServer() {
        primaryStage.setTitle("Talio: Select Your Server");
        resize();
        primaryStage.setScene(selectServer);
    }
    /**
     * Shows the wrong server scene.
     */
    public void showWrongServer() {
        primaryStage.setTitle("Talio: server not found");
        resize();
        primaryStage.setScene(wrongServer);

    }
    /**
     * Shows the server timeout scene.
     */
    public void showTimeout() {
        primaryStage.setTitle("Talio: server timed out");
        resize();
        primaryStage.setScene(serverTimeout);
    }
    /**
     * Shows the unexpected error scene.
     */
    public void showUnexpectedError() {
        primaryStage.setTitle("Talio: unexpected error");
        resize();
        primaryStage.setScene(unexpectedError);
    }

    public void showColorManagementView(final Board board) {
        primaryStage.setTitle("Talio: color management view");
        colorManagementViewCtrl.setBoard(board);
        resize();
        primaryStage.setScene(colorManagementView);
    }

    /** Populate the Board Catalogue */
    public void populateBoardCatalogue() {
        boardCatalogueCtrl.populate();
    }

    public void showDetailedTaskView(final Task task, final ListCtrl listController) {
        primaryStage.setTitle("Talio: Detailed Task View");
        resize();
        primaryStage.setScene(detailedTaskView);
        detailedTaskViewCtrl.setTask(task);
        detailedTaskViewCtrl.setMainCtrl(this);
        detailedTaskViewCtrl.setListController(listController);
        detailedTaskViewCtrl.registerWebSockets();
    }

    private void resize(){
        primaryStage.setHeight(primaryStage.getHeight());
        primaryStage.setWidth(primaryStage.getWidth());
    }

    public void showTagOverview(final Board board) {
        primaryStage.setTitle("Talio: Tag Overview");
        tagOverviewCtrl.setBoard(board);
        primaryStage.setScene(tagOverview);
    }

    public void showTagEdit(final Popup popup) {
        popup.show(primaryStage);
    }


}