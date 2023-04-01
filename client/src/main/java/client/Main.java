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
package client;

import client.sceneManagement.BoardScenes;
import client.scenes.*;
import client.sceneManagement.ListScenes;
import client.sceneManagement.ServerScenes;
import client.sceneManagement.TaskScenes;
import client.scenes.adminScenes.AdminBoardCtrl;
import client.scenes.adminScenes.AdminLoginCtrl;
import client.scenes.connectScenes.SelectServerCtrl;
import client.scenes.connectScenes.ServerTimeoutCtrl;
import client.scenes.connectScenes.UnexpectedErrorCtrl;
import client.scenes.connectScenes.WrongServerCtrl;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main extends Application {

    private static final Injector INJECTOR = Guice.createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(final String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(final Stage primaryStage) throws IOException {


        var lists = FXML.load(BoardOverviewCtrl.class, "client", "scenes", "BoardOverview.fxml");
        var createList = FXML.load(CreateListCtrl.class, "client", "scenes", "CreateList.fxml");
        var renameList = FXML.load(RenameListCtrl.class, "client", "scenes", "RenameList.fxml");
        var serverSelection = FXML
                .load(SelectServerCtrl.class, "client",
                        "scenes", "connectScenes", "SelectServer.fxml");
        var wrongServer = FXML.load(WrongServerCtrl.class, "client",
                "scenes", "connectScenes", "WrongServer.fxml");
        var serverTimeout = FXML.load(ServerTimeoutCtrl.class, "client",
                "scenes", "connectScenes", "ConnectionTimeout.fxml");
        var unexpectedError = FXML.load(UnexpectedErrorCtrl.class, "client",
                "scenes", "connectScenes", "UnexpectedError.fxml");
        var adminBoard = FXML.load(AdminBoardCtrl.class, "client", "scenes", "AdminBoards.fxml");
        var adminLogin = FXML.load(AdminLoginCtrl.class, "client", "scenes", "AdminLogin.fxml");
        var listScenes = new ListScenes(createList, renameList);
        var serverScenes = new ServerScenes(serverSelection, wrongServer,
                serverTimeout, unexpectedError, adminLogin, adminBoard);
        var createTask = FXML.load(CreateTaskCtrl.class, "client", "scenes", "CreateTask.fxml");
        var joinBoard = FXML.load(JoinBoardCtrl.class, "client", "scenes", "JoinBoard.fxml");
        var editTask = FXML.load(EditTaskCtrl.class, "client", "scenes", "EditTask.fxml");
        var detailedTaskView = FXML.load(DetailedTaskViewCtrl.class,
                "client", "scenes", "DetailedTaskView.fxml");
        var taskScenes = new TaskScenes(createTask, editTask, detailedTaskView);
        var editBoard = FXML.load(EditBoardCtrl.class, "client", "scenes", "EditBoard.fxml");
        var boardScenes = new BoardScenes(editBoard, joinBoard);

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage,lists, listScenes, serverScenes, taskScenes, boardScenes);
    }
}