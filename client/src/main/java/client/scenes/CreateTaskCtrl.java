package client.scenes;

import client.CustomAlert;
import client.customExceptions.TagException;
import client.utils.ServerUtils;
import client.utils.TagUtils;
import com.google.inject.Inject;
import commons.Tag;
import commons.Task;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.*;

public class CreateTaskCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ListCtrl listCtrl;
    private final BoardCatalogueCtrl boardCatalogueCtrl;
    private final TagUtils tagUtils;
    private final CustomAlert customAlert;

    @FXML
    private ChoiceBox<Tag> tagChoice;
    @FXML
    private ListView<Tag> tagsView;
    @FXML
    private TextField taskName;

    @FXML
    private TextArea taskDesc;

    //this sets up the server, mainctrl and listctrl variables
    @Inject
    public CreateTaskCtrl(final ServerUtils server, final MainCtrl mainCtrl,
                          final BoardCatalogueCtrl boardCatalogueCtrl,
                          final TagUtils tagUtils, final CustomAlert customAlert) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardCatalogueCtrl=boardCatalogueCtrl;
        this.tagUtils = tagUtils;
        this.customAlert = customAlert;
    }

    private void setChoiceBox() {
        try {
            tagChoice.getItems().setAll(tagUtils.getBoardTags(listCtrl.getBoardID()));
        } catch (TagException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
        }
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
                            var cardLoader = new FXMLLoader(getClass().getResource("TaskTagCard.fxml"));
                            Node card = cardLoader.load();
                            TaskTagCardCtrl taskTagCardCtrl = cardLoader.getController();
                            taskTagCardCtrl.initialize(tag);
                            setGraphic(card);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            };
            cell.setOnMouseClicked(event -> {
                tagsView.getItems().remove(cell.getItem());
            });
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
        Task task = new Task(taskName.getText(), taskDesc.getText());
        List<Tag> taskTags = new ArrayList<>(tagsView.getItems());
        for(Tag tag : taskTags) {
            task.addTag(tag);
        }
        return task;
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
    }

    public void addTag() {
        if(tagChoice.getValue() != null) {
            tagsView.getItems().add(tagChoice.getValue());
        }
    }
}