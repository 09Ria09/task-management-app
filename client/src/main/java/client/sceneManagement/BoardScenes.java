package client.sceneManagement;

import client.scenes.EditBoardCtrl;
import client.scenes.JoinBoardCtrl;
import client.scenes.TagOverviewCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

public class BoardScenes {
    private final Pair<EditBoardCtrl, Parent> editBoard;

    private final Pair<JoinBoardCtrl, Parent> joinBoard;

    private final Pair<TagOverviewCtrl, Parent> tagOverview;

    public BoardScenes (final Pair<EditBoardCtrl, Parent> editBoard,
                        final Pair<JoinBoardCtrl, Parent> joinBoard,
                        final Pair<TagOverviewCtrl, Parent> tagOverview) {
        this.editBoard = editBoard;
        this.joinBoard = joinBoard;
        this.tagOverview = tagOverview;
    }

    public Pair<EditBoardCtrl, Parent> getEditBoard() {
        return editBoard;
    }

    public Pair<JoinBoardCtrl, Parent> getJoinBoard() {
        return joinBoard;
    }

    public Pair<TagOverviewCtrl, Parent> getTagOverview() {
        return tagOverview;
    }

}
