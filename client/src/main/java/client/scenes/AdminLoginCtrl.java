package client.scenes;

import client.customExceptions.BoardException;
import client.utils.BoardUtils;
import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.List;

public class AdminLoginCtrl {

    private MainCtrl mainCtrl;

    private ServerUtils serverUtils;

    private BoardUtils boardUtils;

    private AdminBoardCtrl adminBoardCtrl;

    @FXML
    private TextField password;

    @Inject
    public void initialize(final MainCtrl mainCtrl, final ServerUtils serverUtils,
                           final BoardUtils boardUtils, final AdminBoardCtrl adminBoardCtrl) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
        this.boardUtils = boardUtils;
        this.adminBoardCtrl = adminBoardCtrl;
    }

    public void confirm() throws Exception {
        if (password.getText().equals(serverUtils.getAdminKey())) {
            mainCtrl.showAdminBoard();
            adminBoardCtrl.addAllBoards();
        }
        else {
            System.out.println("Password incorrect. Expected " + serverUtils.getAdminKey());
        }
    }

    public void cancel() {
        password.clear();
        mainCtrl.showJoinBoard();
    }

}
