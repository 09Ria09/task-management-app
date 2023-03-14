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
    public BoardOverviewCtrl(final ServerUtils server, final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.lists = new ArrayList<>();
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        for (int i = 0; i < 3; ++i)
            addList("title " + i);
    }

    public void addCard(final String text, final int list) {
        lists.get(list).getItems().add(text);
    }

    public void addCard() {
        addCard("test"+Math.random(),0);
    }

    //Should be implemented in the server utils
    public void addList(final String title) {
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
        mainCtrl.showCreateList();
        //addList("test"+Math.random());
    }

    public void dragDetected(final ListView<String> lv, final MouseEvent event) {
        Dragboard dragboard = lv.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent cc = new ClipboardContent();
        if(lv.getSelectionModel().getSelectedItem()==null)
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
            event.setDropCompleted(true);
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
    public void switchServer(){
        mainCtrl.showSelectServer();
    }
}
