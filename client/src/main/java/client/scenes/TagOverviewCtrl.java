package client.scenes;

import client.CustomAlert;
import client.customExceptions.TagException;
import client.utils.BoardUtils;
import client.utils.LayoutUtils;
import client.utils.TagUtils;
import client.utils.WebSocketUtils;
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
import java.util.function.Consumer;

public class TagOverviewCtrl {

    @FXML
    private ListView<Tag> tags;
    @FXML
    private ColorPicker backgroundColorPicker;
    @FXML
    private ColorPicker fontColorPicker;
    @FXML
    private TextField tagName;
    private Board board;
    private TagUtils tagUtils;
    private CustomAlert customAlert;
    private BoardUtils boardUtils;
    private final WebSocketUtils webSocketUtils;
    private final LayoutUtils layoutUtils;
    private MainCtrl mainCtrl;

    @Inject
    public TagOverviewCtrl(final TagUtils tagUtils, final CustomAlert customAlert,
                           final BoardUtils boardUtils, final MainCtrl mainCtrl,
                           final WebSocketUtils webSocketUtils,
                           final LayoutUtils layoutUtils) {
        this.tagUtils = tagUtils;
        this.customAlert = customAlert;
        this.boardUtils = boardUtils;
        this.webSocketUtils = webSocketUtils;
        this.layoutUtils = layoutUtils;
        this.mainCtrl = mainCtrl;
    }

    public void initialize(){
        this.tagName.textProperty().addListener(layoutUtils.createMaxFieldLength(16, tagName));
    }

    public void setBoard(final Board board) {
        this.board = board;
        update();
        hardRefresh(board.getTags());
        Consumer<Tag> addTagConsumer = (tag) -> {
            Platform.runLater(() -> {
                if(board.getTags().contains(tag))
                    return;
                board.addTag(tag);
                tags.getItems().add(tag);
            });
        };
        Consumer<Tag> deleteTagConsumer = (tag) -> {
            Platform.runLater(() -> {
                board.removeTag(tag);
                tags.getItems().remove(tag);
            });
        };
        Consumer<Tag> changeTagConsumer = (tag) -> {
            Platform.runLater(() -> {
                Tag previous = board.getTagById(tag.id).orElse(null);
                if(previous == null)
                    return;
                int index = board.getTags().indexOf(previous);
                if(board.getTags().remove(previous)){
                    tags.getItems().remove(previous);
                    board.getTags().add(index, tag);
                    tags.getItems().add(index, tag);
                }
            });
        };
        this.webSocketUtils.registerForTagMessages("/topic/" + board.id +
                "/addtag", addTagConsumer);
        this.webSocketUtils.registerForTagMessages("/topic/" + board.id +
                "/deletetag", deleteTagConsumer);
        this.webSocketUtils.registerForTagMessages("/topic/" + board.id +
                "/changetag", changeTagConsumer);
        Consumer<Board> deleteBoard = (b) -> {
            Platform.runLater(this::goBack);
        };
        webSocketUtils.registerForMessages("/topic/deleteboard", deleteBoard, Board.class);
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
        String backgroundColor = backgroundColorPicker.getValue().toString().substring(2, 8);
        String fontColor = fontColorPicker.getValue().toString().substring(2, 8);
        if(name.equals("")) {
            Alert alert = customAlert.showAlert("The tag should have a name");
            alert.showAndWait();
            return false;
        }

        Tag tag = new Tag(name, backgroundColor, fontColor);
        if(tags.getItems().contains(tag)){
            Alert alert = customAlert.showAlert("This tag already exists !");
            alert.showAndWait();
            return false;
        }
        try {
            tagUtils.addBoardTag(board.id, tag);
            tagName.setText("");
            return true;
        } catch (TagException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    private void hardRefresh(final List<Tag> newTags) {
        tags.getItems().setAll(newTags);
    }

    public void goBack() {
        mainCtrl.showBoardCatalogue();
    }

}
