package client.sceneManagement;

import client.scenes.EditBoardCtrl;
import client.scenes.TagOverviewCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

public class BoardScenes {
    private final Pair<EditBoardCtrl, Parent> editBoard;
    private final Pair<TagOverviewCtrl, Parent> tagOverview;

    public BoardScenes (final Pair<EditBoardCtrl, Parent> editBoard,
                        final Pair<TagOverviewCtrl, Parent> tagOverview) {
        this.editBoard = editBoard;
        this.tagOverview = tagOverview;
    }

    public Pair<EditBoardCtrl, Parent> getEditBoard() {
        return editBoard;
    }
    public Pair<TagOverviewCtrl, Parent> getTagOverview() {
        return tagOverview;
    }
}
