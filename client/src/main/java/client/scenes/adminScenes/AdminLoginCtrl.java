package client.scenes.adminScenes;

import client.scenes.MainCtrl;
import client.utils.BoardUtils;
import client.utils.NetworkUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AdminLoginCtrl {

    private MainCtrl mainCtrl;
    private NetworkUtils networkUtils;
    private AdminBoardCtrl adminBoardCtrl;

    private BoardUtils boardUtils;
    private ServerUtils serverUtils;
    @FXML
    private TextField password;

    @Inject
    public AdminLoginCtrl(final MainCtrl mainCtrl, final NetworkUtils networkUtils,
                          final AdminBoardCtrl adminBoardCtrl) {
        this.mainCtrl = mainCtrl;
        this.adminBoardCtrl = adminBoardCtrl;
        this.networkUtils = networkUtils;
        this.serverUtils = networkUtils.getServerUtils();
        this.boardUtils = networkUtils.getBoardUtils();
    }

    @FXML
    public void confirm() throws Exception {
        if (password.getText().equals(serverUtils.getAdminKey())) {
            mainCtrl.showAdminBoard();
            adminBoardCtrl.addAllBoards();
            boardUtils.registerForUpdatesBoards(event -> adminBoardCtrl.solveEvent(event));
        } else {return;}
    }

    @FXML
    public void cancel() {
        password.clear();
        mainCtrl.showBoardCatalogue();
    }
}
