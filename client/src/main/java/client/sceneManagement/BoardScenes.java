package client.sceneManagement;

import client.scenes.ColorManagementViewCtrl;
import client.scenes.EditBoardCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

public class BoardScenes {
    private final Pair<EditBoardCtrl, Parent> editBoard;
    private final Pair<ColorManagementViewCtrl, Parent> colorManagementView;

    public BoardScenes (final Pair<EditBoardCtrl, Parent> editBoard,
                        final Pair<ColorManagementViewCtrl, Parent> colorManagementView) {
        this.editBoard = editBoard;
        this.colorManagementView = colorManagementView;
    }

    public Pair<EditBoardCtrl, Parent> getEditBoard() {
        return editBoard;
    }

    public Pair<ColorManagementViewCtrl, Parent> getColorManagementView() {
        return colorManagementView;
    }
}
