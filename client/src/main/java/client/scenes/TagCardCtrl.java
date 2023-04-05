package client.scenes;

import client.CustomAlert;
import client.customExceptions.TagException;
import client.utils.TagUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


public class TagCardCtrl {

    @FXML
    private AnchorPane pane;
    @FXML
    private Label tagName;
    private Tag tag;
    private TagUtils tagUtils;
    private Board board;
    private CustomAlert customAlert;

    @Inject
    public void initialize(final Tag tag, final TagUtils tagUtils,
                           final Board board, final CustomAlert customAlert) {
        this.tag = tag;
        this.tagName.setText(tag.getName());
        this.pane.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px;" +
                " -fx-background-color: #" + tag.getColorBackground() + ";");
        this.tagName.setStyle("-fx-text-fill: #" + tag.getColorFont() + ";");
        this.tagUtils = tagUtils;
        this.board = board;
        this.customAlert = customAlert;
    }

    @FXML
    public boolean deleteTag() {
        try {
            tagUtils.deleteBoardTag(board.id, tag.id);
            return true;
        } catch (TagException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
            return false;
        }
    }

}
