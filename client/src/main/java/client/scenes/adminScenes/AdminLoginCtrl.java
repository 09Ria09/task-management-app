package client.scenes.adminScenes;

import client.scenes.MainCtrl;
import client.utils.BoardUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AdminLoginCtrl {

    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private BoardUtils boardUtils;
    private AdminBoardCtrl adminBoardCtrl;

    @FXML
    private TextField password;

    @Inject
    public AdminLoginCtrl(final MainCtrl mainCtrl, final ServerUtils serverUtils,
                          final BoardUtils boardUtils, final AdminBoardCtrl adminBoardCtrl) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
        this.boardUtils = boardUtils;
        this.adminBoardCtrl = adminBoardCtrl;
    }

    @FXML
    public void confirm() throws Exception {
        if (password.getText().equals(serverUtils.getAdminKey())) {
            mainCtrl.showAdminBoard();
            adminBoardCtrl.addAllBoards();
        } else {
            System.out.println("Password incorrect. Expected " + serverUtils.getAdminKey());
        }
    }

    @FXML
    public void cancel() {
        password.clear();
        mainCtrl.showJoinBoard();
    }
}
