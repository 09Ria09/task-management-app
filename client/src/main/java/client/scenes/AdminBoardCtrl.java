package client.scenes;

import client.customExceptions.BoardException;
import client.utils.BoardUtils;
import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.List;

public class AdminBoardCtrl {

    @FXML
    private VBox boardList;
    private BoardUtils boardUtils;

    @Inject
    public void initialize(final BoardUtils boardUtils) {
        this.boardUtils = boardUtils;
    }

    public void addAllBoards() throws BoardException {
        List<Board> boards = boardUtils.getBoards();

        for (Board board : boards) {
            Button button = new Button();
            button.setText(board.getName());
            boardList.getChildren().add(button);
        }
    }
}
