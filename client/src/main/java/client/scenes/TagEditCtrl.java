package client.scenes;

import client.CustomAlert;
import client.customExceptions.TagException;
import client.utils.NetworkUtils;
import client.utils.TagUtils;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class TagEditCtrl {

    @FXML
    private Label labelName;
    @FXML
    private TextField tagNameField;
    @FXML
    private ColorPicker backgroundColorPicker;
    @FXML
    private ColorPicker fontColorPicker;
    @FXML
    private AnchorPane pane;
    private Tag tag;
    private TagUtils tagUtils;
    private Board board;
    private CustomAlert customAlert;

    private NetworkUtils networkUtils;

    public void initialize(final Tag tag, final NetworkUtils networkUtils,
                           final Board board, final CustomAlert customAlert) {
        this.tag = tag;
        labelName.setText(tag.getName());
        this.pane.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px;" +
            " -fx-background-color: #" + tag.getColorBackground() + ";");

        this.labelName.setStyle("-fx-text-fill: #" + tag.getColorFont() + ";");
        this.networkUtils = networkUtils;
        this.tagUtils = networkUtils.getTagUtils();
        this.board = board;
        this.customAlert = customAlert;
    }

    public void onLabelNameClicked(final MouseEvent event){
        if(event.getButton() != MouseButton.PRIMARY)
            return;
        this.tagNameField.setText(this.labelName.getText());
        this.tagNameField.setVisible(true);
        this.labelName.setVisible(false);
    }

    private void enterPressed() throws TagException {
        this.labelName.setText(this.tagNameField.getText());
        this.tagNameField.setVisible(false);
        this.labelName.setVisible(true);
        if(this.tagNameField.getText().equals(tag.getName()))
            return;
        try {
            if(tagNameField.getText().length() > 16) {
                throw new TagException("The new name is too long");
            } else {
                this.tagUtils.renameTag(board.id, tag.getId(),
                        this.tagNameField.getText());
            }
        } catch (TagException e) {
            throw new TagException("Renaming tag unsuccessful");
        }
    }

    public void tagNameKeyPressed(final KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)) {
            try {
                enterPressed();
            } catch (TagException e) {
                Alert alert = customAlert.showAlert("The tag should have a name");
                alert.showAndWait();
            }
        }
    }


    public boolean changeColor() {
        String backgroundColor = backgroundColorPicker.getValue().toString().substring(2, 8);
        String fontColor = fontColorPicker.getValue().toString().substring(2, 8);
        try {
            tagUtils.recolorTag(board.id, tag.id, backgroundColor, fontColor);
            this.pane.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px;" +
                " -fx-background-color: #" +
                tagUtils.getBoardTag(board.id, tag.id).getColorBackground() + ";");
            this.labelName.setStyle("-fx-text-fill: #" +
                tagUtils.getBoardTag(board.id, tag.id).getColorFont() + ";");
            return true;
        } catch (TagException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
            return false;
        }
    }
}
