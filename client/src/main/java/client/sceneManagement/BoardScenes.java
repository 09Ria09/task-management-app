package client.sceneManagement;

import client.scenes.EditBoardCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

public class BoardScenes {
    private final Pair<EditBoardCtrl, Parent> editBoard;

    public BoardScenes (final Pair<EditBoardCtrl, Parent> editBoard) {
        this.editBoard = editBoard;
    }

    public Pair<EditBoardCtrl, Parent> getEditBoard() {
        return editBoard;
    }
}
