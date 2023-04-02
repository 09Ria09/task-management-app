package client.scenes;

import client.customExceptions.BoardException;
import client.utils.BoardUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class AdminBoardCtrl {

    @FXML
    private ListView boardList;
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
            boardList.getItems().add(button);
        }
    }
}
