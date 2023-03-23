package client.scenes;

import client.utils.TaskListUtils;
import client.utils.customExceptions.TaskListException;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class DeleteListCtrl {

    private final TaskListUtils listUtils;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField boardIdInput;
    @FXML
    private TextField listIdInput;
    private long boardId;
    private long listId;

    @Inject
    public DeleteListCtrl(final TaskListUtils listUtils, final MainCtrl mainCtrl) {
        this.listUtils = listUtils;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Returns user to board overview
     */
    public void cancel() {
        boardIdInput.clear();
        listIdInput.clear();
        mainCtrl.showBoardOverview();
    }

    /**
     * Deletes the list that the user has selected
     */
    public void delete() {
        try {
            boardId = Long.parseLong(boardIdInput.getText());
            listId = Long.parseLong(listIdInput.getText());
            listUtils.deleteTaskList(boardId, listId);
            boardIdInput.clear();
            listIdInput.clear();
            mainCtrl.showBoardOverview();
        } catch (TaskListException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

}
