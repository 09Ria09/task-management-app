package objects;

import client.scenes.CardCtrl;
import commons.Task;
import javafx.scene.control.ListCell;

public class CardCell extends ListCell<Task> {

    protected CardCtrl cardCtrl;

    public CardCtrl getController() {
        return cardCtrl;
    }

}
