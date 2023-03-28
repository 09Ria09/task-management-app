package client.sceneManagement;

import client.scenes.EditBoardCtrl;
import client.scenes.JoinBoardCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

public class BoardScenes {
    private final Pair<EditBoardCtrl, Parent> editBoard;

    private final Pair<JoinBoardCtrl, Parent> joinBoard;

    public BoardScenes (final Pair<EditBoardCtrl, Parent> editBoard,
                        final Pair<JoinBoardCtrl, Parent> joinBoard) {
        this.editBoard = editBoard;
        this.joinBoard = joinBoard;
    }

    public Pair<EditBoardCtrl, Parent> getEditBoard() {
        return editBoard;
    }

    public Pair<JoinBoardCtrl, Parent> getJoinBoard() {
        return joinBoard;
    }

}
