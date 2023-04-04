package client.sceneManagement;

import client.scenes.ColorManagementViewCtrl;
import client.scenes.EditBoardCtrl;
import client.scenes.TagOverviewCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

public class BoardScenes {
    private final Pair<EditBoardCtrl, Parent> editBoard;
    private final Pair<ColorManagementViewCtrl, Parent> colorManagementView;
    private final Pair<TagOverviewCtrl, Parent> tagOverview;

    public BoardScenes (final Pair<EditBoardCtrl, Parent> editBoard,
                        final Pair<ColorManagementViewCtrl, Parent> colorManagementView,
                        final Pair<TagOverviewCtrl, Parent> tagOverview) {
        this.editBoard = editBoard;
        this.colorManagementView = colorManagementView;
        this.tagOverview = tagOverview;
    }

    public Pair<EditBoardCtrl, Parent> getEditBoard() {
        return editBoard;
    }

    public Pair<ColorManagementViewCtrl, Parent> getColorManagementView() {
        return colorManagementView;
    }
    public Pair<TagOverviewCtrl, Parent> getTagOverview() {
        return tagOverview;
    }
}
