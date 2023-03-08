package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SelectServerCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField addressField;

    @Inject
    public SelectServerCtrl(final ServerUtils server, final MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    //TODO
    /*
    -Take input from addressField
    -verify input for validity
    -connect to server
     */
    public void showServerBoards(){
        mainCtrl.showBoardOverview();
    }

}
