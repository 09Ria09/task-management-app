package client.scenes;

import client.CustomAlert;
import client.customExceptions.BoardException;
import client.utils.BoardUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class JoinBoardCtrl {
    private final ServerUtils server;
    private final BoardUtils boardUtils;
    private final MainCtrl mainCtrl;
    private final BoardOverviewCtrl boardOverviewCtrl;
    private final CustomAlert customAlert;


    @FXML
    private TextField inviteKeyInput;

    @FXML
    private Button joinButton;

    @FXML
    private TextField boardNameInput;

    @FXML
    private Button createButton;

    @Inject
    public JoinBoardCtrl(final ServerUtils server, final MainCtrl mainCtrl,
                         final CustomAlert customAlert, final BoardUtils boardUtils,
                         final BoardOverviewCtrl boardOverviewCtrl) {
        this.mainCtrl = mainCtrl;
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.server = server;
        this.customAlert = customAlert;
        this.boardUtils = boardUtils;
    }

    /**
     * joins a board based on the invite key
     * inputted by the user
     */
    public void joinBoard(){
        String inviteKey = inviteKeyInput.getText();
        String memberName = "toBeImplemented";
        try{
            Board joinedBoard = boardUtils.joinBoard(inviteKey, memberName);
            boardOverviewCtrl.setCurrentBoardId(joinedBoard.getId());
            mainCtrl.showBoardOverview();
        } catch (BoardException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
        }
    }
}
