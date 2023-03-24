package client.scenes;

import client.CustomAlert;
import client.utils.TaskListUtils;
import client.customExceptions.TaskListException;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class DeleteListCtrl {

    private final TaskListUtils listUtils;
    private final MainCtrl mainCtrl;
    private CustomAlert customAlert;

    @FXML
    private TextField boardIdInput;
    @FXML
    private TextField listIdInput;
    private long boardId;
    private long listId;

    @Inject
    public DeleteListCtrl(final TaskListUtils listUtils, final MainCtrl mainCtrl,
                          final CustomAlert customAlert) {
        this.listUtils = listUtils;
        this.mainCtrl = mainCtrl;
        this.customAlert = customAlert;
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
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
        }
    }

}
