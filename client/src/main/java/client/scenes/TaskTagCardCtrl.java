package client.scenes;

import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class TaskTagCardCtrl {

    @FXML
    private Label tagName;
    @FXML
    private AnchorPane pane;
    private Tag tag;


    public void initialize(final Tag tag) {
        this.tag = tag;
        this.tagName.setText(tag.getName());
        this.pane.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px;" +
            " -fx-background-color: #" + tag.getColorBackground() + ";");
        this.tagName.setStyle("-fx-text-fill: #" + tag.getColorFont() + ";");
    }
}
