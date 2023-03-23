package client.scenes;

import client.utils.TaskListUtils;
import client.utils.customExceptions.TaskListException;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class RenameListCtrl {
    private final TaskListUtils listUtils;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField boardIdInput;
    @FXML
    private TextField listIdInput;
    @FXML
    private TextField listNameInput;

    private long boardId;
    private long listId;
    private String listName;

    @Inject
    public RenameListCtrl(final TaskListUtils listUtils, final MainCtrl mainCtrl) {
        this.listUtils = listUtils;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Returns user to board overview
     */
    public void cancel() {
        boardIdInput.clear();
        listIdInput.clear();
        listNameInput.clear();
        mainCtrl.showBoardOverview();
    }

    /**
     * When user hits confirm, it renames the list
     */
    public void confirm() {
        try {
            boardId = Long.parseLong(boardIdInput.getText());
            listId = Long.parseLong(listIdInput.getText());
            listName = listNameInput.getText();
            listUtils.renameTaskList(boardId, listId, listName);
            boardIdInput.clear();
            listIdInput.clear();
            listNameInput.clear();
            mainCtrl.showBoardOverview();
        } catch (TaskListException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

}
