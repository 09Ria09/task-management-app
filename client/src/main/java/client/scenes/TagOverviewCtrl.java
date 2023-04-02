package client.scenes;

import client.CustomAlert;
import client.customExceptions.BoardException;
import client.customExceptions.TagException;
import client.utils.BoardUtils;
import client.utils.TagUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Popup;

import java.io.IOException;
import java.util.*;
import java.util.Timer;

public class TagOverviewCtrl {

    @FXML
    private ListView<Tag> tags;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextField tagName;
    private Board board;
    private TagUtils tagUtils;
    private CustomAlert customAlert;
    private Timer refreshTimer;
    private BoardUtils boardUtils;
    private MainCtrl mainCtrl;

    @Inject
    public TagOverviewCtrl(final TagUtils tagUtils, final CustomAlert customAlert,
                           final BoardUtils boardUtils, final MainCtrl mainCtrl) {
        this.tagUtils = tagUtils;
        this.customAlert = customAlert;
        this.boardUtils = boardUtils;
        refreshTimer = new Timer();
        this.mainCtrl = mainCtrl;
    }

    public void setBoard(final Board board) {
        this.board = board;
        update();
        hardRefresh(board.getTags());
    }

    private void update() {
        tags.setCellFactory(lv -> {
            ListCell<Tag> cell = new ListCell<>() {
                @Override
                protected void updateItem(final Tag tag, final boolean empty) {
                    super.updateItem(tag, empty);
                    if (tag == null || empty) {
                        setGraphic(null);
                    } else {
                        try {
                            var cardLoader = new FXMLLoader(getClass().getResource("TagCard.fxml"));
                            Node card = cardLoader.load();
                            TagCardCtrl tagCardCtrl = cardLoader.getController();
                            tagCardCtrl.initialize(tag, tagUtils, board, customAlert);
                            setGraphic(card);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            };
            cell.setOnMouseEntered(event -> {
                lv.getSelectionModel().select(cell.getIndex());
            });
            cell.setOnMouseExited(event -> {
                lv.getSelectionModel().clearSelection(cell.getIndex());
            });
            cell.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2) {
                    try {
                        var tagEditLoader = new FXMLLoader(getClass().getResource("TagEdit.fxml"));
                        Node tagEdit = tagEditLoader.load();
                        TagEditCtrl tagEditCtrl = tagEditLoader.getController();
                        tagEditCtrl.initialize(cell.getItem(), tagUtils, board, customAlert);

                        Popup popup = new Popup();
                        popup.getContent().add(tagEdit);
                        popup.setAutoHide(true);

                        mainCtrl.showTagEdit(popup);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return cell;
        });
    }

    public boolean addTag() {
        String name = tagName.getText();
        String color = colorPicker.getValue().toString().substring(2, 8);
        if(name.equals("")) {
            Alert alert = customAlert.showAlert("The tag should have a name");
            alert.showAndWait();
            return false;
        }

        if(name.length() > 16) {
            Alert alert = customAlert.showAlert("The name of the tag should be shorter than 16 characters");
            alert.showAndWait();
            return false;
        }

        Tag tag = new Tag(name, color);
        try {
            tagUtils.addBoardTag(board.id, tag);
            tags.getItems().add(tag);
            return true;
        } catch (TagException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    public void refreshTimer(final long refreshPeriod) {
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        }, 0, refreshPeriod);
    }

    public void refresh() {
        Platform.runLater(() -> {
            try {
                List<Tag> oldTags = board.getTags();
                this.board = boardUtils.getBoard(board.id);
                List<Tag> newTags = board.getTags();

                if(!oldTags.equals(newTags)) {
                    hardRefresh(newTags);
                }
            } catch (BoardException e) {
                Alert alert = customAlert.showAlert(e.getMessage());
                alert.showAndWait();
            }
        });
    }

    private void hardRefresh(final List<Tag> newTags) {
        tags.getItems().setAll(newTags);
    }

    public void goBack() {
        mainCtrl.showBoardCatalogue();
    }

}
