package client.sceneManagement;

import client.scenes.CreateListCtrl;
import client.scenes.RenameListCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

public class ListScenes {

    private final Pair<CreateListCtrl, Parent> createList;

    private final Pair<RenameListCtrl, Parent> renameList;





    public ListScenes(final Pair<CreateListCtrl, Parent> createList,
                      final Pair<RenameListCtrl, Parent> renameList) {
        this.createList = createList;
        this.renameList = renameList;
    }

    public Pair<CreateListCtrl, Parent> getCreateList() {
        return createList;
    }

    public Pair<RenameListCtrl, Parent> getRenameList() {
        return renameList;
    }




}
