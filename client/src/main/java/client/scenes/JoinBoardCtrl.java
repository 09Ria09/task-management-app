package client.scenes;

import client.CustomAlert;
import client.customExceptions.BoardException;
import client.utils.BoardUtils;
import client.utils.ServerUtils;
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
                         final BoardCatalogueCtrl boardCatalogueCtrl) {
        this.boardCatalogueCtrl = boardCatalogueCtrl;
        this.server = server;
        this.customAlert = customAlert;
        this.boardUtils = boardUtils;
        this.mainCtrl = mainCtrl;
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
}
