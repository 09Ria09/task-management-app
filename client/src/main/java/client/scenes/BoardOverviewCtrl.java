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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final DataFormat taskCustom = new DataFormat("task.custom");
    @FXML
    private final ArrayList<ListView<String>> lists;
    @FXML
    private HBox listsContainer;

    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.lists = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < 3; ++i)
            addList("title " + i);
    }

    public void addCard(String text, int list) {
        lists.get(list).getItems().add(text);
    }

    public void addCard() {
        addCard("test"+Math.random(),0);
    }

    public void addList(String title) {
        var kids = listsContainer.getChildren();
        var newList = new ListView<String>();
        newList.setPrefWidth(200);
        newList.setOnDragDetected(event -> dragDetected(newList, event));
        newList.setOnDragEntered(event -> dragEntered(newList, event));
        newList.setOnDragOver(event -> dragOver(newList, event));
        newList.setOnDragExited(event -> dragExited(newList, event));
        newList.setOnDragDropped(event -> dragDropped(newList, event));
        newList.setOnDragDone(event -> dragDone(newList, event));
        if (!kids.isEmpty()) {
            var lb = kids.get(kids.size() - 1).getLayoutBounds();
            var lx = kids.get(kids.size() - 1).getLayoutX();
            newList.setLayoutX(lx + lb.getMaxX());
        }

        VBox newVBox = new VBox();
        newVBox.getChildren().add(new Text(title));
        newVBox.getChildren().add(newList);

        kids.add(newVBox);
        lists.add(newList);
    }

    public void addList() {
        addList("test"+Math.random());
    }

    public void dragDetected(ListView<String> lv, MouseEvent event) {
        Dragboard dragboard = lv.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent cc = new ClipboardContent();
        cc.put(taskCustom, lv.getSelectionModel().getSelectedItem());
        dragboard.setContent(cc);
        event.consume();
    }

    public void dragEntered(ListView<String> lv, DragEvent event) {
        lv.setStyle("-fx-background-color: blue");
        event.consume();
    }

    public void dragExited(ListView<String> lv, DragEvent event) {
        lv.setStyle("-fx-background-color: white");
        event.consume();
    }

    public void dragOver(ListView<String> lv, DragEvent event) {
        if (event.getDragboard().hasContent(taskCustom))
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        event.consume();
    }

    public void dragDropped(ListView<String> lv, DragEvent event) {
        if (event.getDragboard().hasContent(taskCustom)) {
            lv.getItems().add((String) event.getDragboard().getContent(taskCustom));
            event.setDropCompleted(true);
        } else
            event.setDropCompleted(true);
        event.consume();
    }

    public void dragDone(ListView<String> lv, DragEvent event) {
        if (event.getTransferMode() == TransferMode.MOVE) {
            lv.getItems().remove(lv.getSelectionModel().getSelectedItem());
        }
        event.consume();
    }
}