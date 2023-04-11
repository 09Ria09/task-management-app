package client.scenes;

import client.CustomAlert;
import client.customExceptions.BoardException;
import client.utils.BoardUtils;
import client.utils.NetworkUtils;
import client.utils.ServerUtils;
import client.utils.WebSocketUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import commons.TaskList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.util.ArrayList;

public class JoinBoardCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final BoardUtils boardUtils;
    private final BoardCatalogueCtrl boardCatalogueCtrl;
    private final CustomAlert customAlert;

    private final WebSocketUtils webSocketUtils;

    private final NetworkUtils networkUtils;

    @FXML
    private TextField inviteKeyInput;

    @FXML
    private Button joinButton;

    @FXML
    private TextField boardNameInput;

    @FXML
    private Button createButton;

    @Inject
    public JoinBoardCtrl(final NetworkUtils networkUtils,
                        final MainCtrl mainCtrl,
                         final CustomAlert customAlert,
                         final BoardCatalogueCtrl boardCatalogueCtrl,
                         final WebSocketUtils webSocketUtils) {
        this.boardCatalogueCtrl = boardCatalogueCtrl;
        this.customAlert = customAlert;
        this.mainCtrl = mainCtrl;
        this.webSocketUtils = webSocketUtils;
        this.networkUtils = networkUtils;
        this.server = networkUtils.getServerUtils();
        this.boardUtils = networkUtils.getBoardUtils();
    }

    public void adminLogin() {
        mainCtrl.showAdminLogin();
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
            boardCatalogueCtrl.addNew(joinedBoard.getId());
        } catch (BoardException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
        }
    }

    public void createBoard() {
        try {
            Board board = new Board(boardNameInput.getText(), new ArrayList<TaskList>(),
                    new ArrayList<Tag>());
            Board createdBoard = boardUtils.addBoard(board);
            boardCatalogueCtrl.addNew(createdBoard.getId());
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        } catch (BoardException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
        }
        boardNameInput.clear();
    }

    public void switchServer() {
        boardCatalogueCtrl.close();
        mainCtrl.showSelectServer();
        server.disconnect();
        webSocketUtils.disconnect();
    }
}
