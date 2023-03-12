package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


import javafx.event.ActionEvent;

public class CreateListCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField listNameInput;
//    @FXML
//    private Button confirm;
//    @FXML
//    private Button cancel;

    String listName;

    @Inject
    public CreateListCtrl(final ServerUtils server, final MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }


    public void confirm(final ActionEvent event) {
        listName = listNameInput.getText();
        MainCtrl.getBoardOverviewCtrl().addList(listName);
        showServerBoards();
    }

    public void showServerBoards(){
        mainCtrl.showBoardOverview();
    }

    public void cancel(final ActionEvent event) {
        showServerBoards();
    }



}
