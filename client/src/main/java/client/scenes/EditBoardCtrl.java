package client.scenes;

import client.CustomAlert;
import client.customExceptions.BoardException;
import client.utils.BoardUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import org.glassfish.jersey.internal.inject.Custom;

public class EditBoardCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private final BoardUtils boardUtils;

    private final CustomAlert customAlert;


    private Board board;

    @FXML
    private TextField boardName;

    //this sets up the server and mainctrl variables
    @Inject
    public EditBoardCtrl(final ServerUtils server, final MainCtrl mainCtrl,
                         final BoardUtils boardUtils, final CustomAlert customAlert) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardUtils = boardUtils;
        this.customAlert = customAlert;
    }

    public void setBoard(final Board board) {
        this.board = board;
    }

    //this is run when the cancel button is pressed, it sends the user back to the board list
    public void cancel() {
        clearFields();
        mainCtrl.showBoardOverview();
    }

    //this is run when the confirm button is pressed,
    //it is meant to inform the server to rename the board with the
    //appropriate name
    public void confirm() {
        try {
            board.setName(boardName.getText());
            boardUtils.renameBoard(board.id, boardName.getText());
        } catch (Exception e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
        }
        clearFields();
        mainCtrl.showBoardOverview();
    }

    //this clears the text fields of the UI to allow them to be reusable
    private void clearFields() {
        boardName.clear();
    }
}
