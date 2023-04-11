package client.scenes;

import client.CustomAlert;
import client.customExceptions.BoardException;
import client.customExceptions.TagException;
import client.utils.*;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import commons.Task;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class CreateTaskCtrl {

    private final NetworkUtils networkUtils;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ListCtrl listCtrl;
    private final BoardCatalogueCtrl boardCatalogueCtrl;
    private final TagUtils tagUtils;
    private final CustomAlert customAlert;
    private final WebSocketUtils webSocketUtils;

    @FXML
    private Label tagMessage;
    @FXML
    private Button addTagButton;
    @FXML
    private ChoiceBox<Tag> tagChoice;
    @FXML
    private ListView<Tag> tagsView;
    @FXML
    private TextField taskName;

    @FXML
    private TextArea taskDesc;

    BoardUtils boardUtils;

    //this sets up the server, mainctrl and listctrl variables
    @Inject
    public CreateTaskCtrl(final MainCtrl mainCtrl,
                          final BoardCatalogueCtrl boardCatalogueCtrl,
                          final CustomAlert customAlert,
                          final WebSocketUtils webSocketUtils, final NetworkUtils networkUtils) {
        this.mainCtrl = mainCtrl;
        this.boardCatalogueCtrl=boardCatalogueCtrl;
        this.customAlert = customAlert;
        this.webSocketUtils = webSocketUtils;
        this.networkUtils = networkUtils;
        this.server = networkUtils.getServerUtils();
        this.tagUtils = networkUtils.getTagUtils();
        this.boardUtils = networkUtils.getBoardUtils();
    }

    private void setTagsList() {
        tagsView.setCellFactory(lv -> {
            ListCell<Tag> cell = new ListCell<>() {
                @Override
                protected void updateItem(final Tag tag, final boolean empty) {
                    super.updateItem(tag, empty);
                    if (tag == null || empty) {
                        setGraphic(null);
                    } else {
                        try {
                            var cardLoader = new FXMLLoader(getClass()
                                    .getResource("TaskTagCard.fxml"));
                            Node card = cardLoader.load();

                            TaskTagCardCtrl taskTagCardCtrl = cardLoader.getController();
                            taskTagCardCtrl.initialize(tag);


                            Button removeButton = (Button) card.lookup("#removeButton");
                            removeButton.setOnAction(event -> {
                                tagsView.getItems().remove(tag);
                            });
                            setGraphic(card);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            };
            return cell;
        });
    }

    //this is run when the cancel button is pressed, it sends the user back to the overview
    public void cancel() {
        clearFields();
        mainCtrl.showBoardCatalogue();
    }

    //this is run when the confirm button is pressed,
    //it is meant to inform the server to create a new task with the
    //appropriate name and description however this interface does not work yet
    public void confirm() {
        try {
            Task task = getTask();
            listCtrl.addCard(task);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showBoardCatalogue();
    }

    //this is run to get the description and name of
    //the task out of the text boxes and to create a new Task object
    private Task getTask() {
        try {
            Task task = new Task(taskName.getText(), taskDesc.getText(),
                boardUtils.getBoard(listCtrl.getBoardID()).findDefaultTaskPreset());
            List<Tag> taskTags = new ArrayList<>(tagsView.getItems());
            for(Tag tag : taskTags) {
                task.addTag(tag);
            }
            return task;
        } catch (BoardException e) {
            throw new RuntimeException(e);
        }
    }

    //this clears the text fields of the UI to allow them to be reusable
    private void clearFields() {
        taskDesc.clear();
        taskName.clear();
        tagsView.getItems().setAll(new ArrayList<>());
    }

    public void setListCtrl(final ListCtrl listCtrl) {
        this.listCtrl = listCtrl;
        setTagsList();
        setChoiceBox();
        Consumer<Board> deleteBoard = (board) -> {
            Platform.runLater(this::cancel);
        };
        webSocketUtils.registerForMessages("/topic/deleteboard", deleteBoard, Board.class);
    }

    public void addTag() {
        if(tagChoice.getValue() != null) {
            Tag tag = tagChoice.getValue();
            if(!tagsView.getItems().contains(tag)) {
                tagsView.getItems().add(tag);
            } else {
                Alert alert = customAlert.showAlert("This tag is already selected");
                alert.showAndWait();
            }
        }
    }

    private void setChoiceBox() {
        try {
            tagChoice.getItems().setAll(tagUtils.getBoardTags(listCtrl.getBoardID()));
            if(!tagChoice.getItems().isEmpty()) {
                tagChoice.setVisible(true);
                addTagButton.setVisible(true);
                tagMessage.setVisible(false);
                tagChoice.setValue(tagChoice.getItems().get(0));
                tagChoice.setConverter(new StringConverter<Tag>() {
                    @Override
                    public String toString(final Tag object) {
                        return object == null ? "" : object.getName();
                    }

                    @Override
                    public Tag fromString(final String string) {
                        return tagChoice.getItems().stream()
                                .filter(t -> t.getName().equals(string)).findFirst().orElse(null);
                    }
                });
            } else {
                tagChoice.setVisible(false);
                addTagButton.setVisible(false);
                tagMessage.setVisible(true);
            }
        } catch (TagException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
        }
    }

}