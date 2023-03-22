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

import client.scenes.*;
import client.utils.ListScenes;
import client.utils.ServerScenes;
import client.utils.TaskScenes;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

import com.google.inject.Guice;

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
        var deleteList = FXML.load(DeleteListCtrl.class, "client", "scenes", "DeleteList.fxml");
        var renameList = FXML.load(RenameListCtrl.class, "client", "scenes", "RenameList.fxml");
        var serverSelection = FXML
                .load(SelectServerCtrl.class, "client", "scenes", "SelectServer.fxml");
        var wrongServer = FXML.load(WrongServerCtrl.class, "client", "scenes", "WrongServer.fxml");
        var serverTimeout = FXML.load(ServerTimeoutCtrl.class, "client",
                "scenes", "ConnectionTimeout.fxml");
        var serverDown = FXML.load(ServerDownCtrl.class, "client", "scenes", "ServerDown.fxml");
        var listScenes = new ListScenes(createList, deleteList, renameList);
        var serverScenes = new ServerScenes(serverSelection, wrongServer,
                serverTimeout, serverDown);
        var createTask = FXML.load(CreateTaskCtrl.class, "client", "scenes", "CreateTask.fxml");
        var taskScenes = new TaskScenes(createTask);

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage,lists, listScenes, serverScenes, taskScenes);
    }
}