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

import client.utils.ServerScenes;
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


    private SelectServerCtrl selectServerCtrl;

    private Scene selectServer;

    private WrongServerCtrl wrongServerCtrl;
    private Scene wrongServer;

    private ServerTimeoutCtrl serverTimeoutCtrl;
    private Scene serverTimeout;

    private ServerDownCtrl serverDownCtrl;
    private Scene serverDown;



    /**
     * Initializes the main controller.
     * @param primaryStage the primary stage
     * @param boardOverview the board overview scene
     * @param serverScenes the server scenes
     */
    public void initialize(final Stage primaryStage,
                           final Pair<BoardOverviewCtrl, Parent> boardOverview,
                           final Pair<CreateListCtrl, Parent> createList,
                           final ServerScenes serverScenes){
        this.primaryStage = primaryStage;


        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());

        this.createListCtrl = createList.getKey();
        this.createList = new Scene(createList.getValue());

        this.selectServerCtrl = serverScenes.getSelectServer().getKey();
        this.selectServer = new Scene(serverScenes.getSelectServer().getValue());

        this.wrongServerCtrl = serverScenes.getWrongServer().getKey();
        this.wrongServer = new Scene(serverScenes.getWrongServer().getValue());

        this.serverTimeoutCtrl = serverScenes.getServerTimeout().getKey();
        this.serverTimeout = new Scene(serverScenes.getServerTimeout().getValue());

        this.serverDownCtrl = serverScenes.getServerDown().getKey();
        this.serverDown = new Scene(serverScenes.getServerDown().getValue());

        showSelectServer();
        primaryStage.show();
    }

    /**
     * Shows the board overview scene.
     */
    public void showBoardOverview() {
        primaryStage.setTitle("Talio: Board Overview");
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
     * Shows the server down/other errors scene.
     */
    public void showServerDown() {
        primaryStage.setTitle("Talio: server is down");
        primaryStage.setScene(serverDown);
    }
}