package client.sceneManagement;

import client.scenes.CreateListCtrl;
import javafx.scene.Parent;
import javafx.util.Pair;

public class ListScenes {

    private final Pair<CreateListCtrl, Parent> createList;






    public ListScenes(final Pair<CreateListCtrl, Parent> createList) {
        this.createList = createList;
    }

    public Pair<CreateListCtrl, Parent> getCreateList() {
        return createList;
    }





}
