package client.scenes;

import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class CardCtrl {
    private long id;
    @FXML
    public Text text;

    /**
     * This initializes the card using a task
     * @param task the task used for initialization
     */
    public void initialize(final Task task) {
        this.id = task.id;
        this.text.setText(task.getName());
    }
}
