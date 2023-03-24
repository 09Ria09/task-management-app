package client.scene_management;

import client.scenes.CreateListCtrl;
import client.scenes.DeleteListCtrl;
import client.scenes.RenameListCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

public class ListScenes {

    private final Pair<CreateListCtrl, Parent> createList;

    private final Pair<DeleteListCtrl, Parent> deleteList;

    private final Pair<RenameListCtrl, Parent> renameList;





    public ListScenes(final Pair<CreateListCtrl, Parent> createList,
                      final Pair<DeleteListCtrl, Parent> deleteList,
                      final Pair<RenameListCtrl, Parent> renameList) {
        this.createList = createList;
        this.deleteList = deleteList;
        this.renameList = renameList;
    }

    public Pair<CreateListCtrl, Parent> getCreateList() {
        return createList;
    }

    public Pair<DeleteListCtrl, Parent> getDeleteList() {
        return deleteList;
    }

    public Pair<RenameListCtrl, Parent> getRenameList() {
        return renameList;
    }




}
