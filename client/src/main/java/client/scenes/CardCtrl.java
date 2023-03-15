package client.scenes;

import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.Objects;

public class CardCtrl {
    private Task task;
    @FXML
    public Text text;

    /**
     * This initializes the card using a task
     * @param task the task used for initialization
     */
    public void initialize(final Task task) {
        this.task= task;
        this.text.setText(task.getName());
    }

    /**
     * @param o an object
     * @return true if the object provided is the same to this object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardCtrl cardCtrl = (CardCtrl) o;
        return Objects.equals(task, cardCtrl.task);
    }

    /**
     * @return the hashCode of this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(task);
    }
}
