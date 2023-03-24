package client.scenes;

import client.CustomAlert;
import client.utils.TaskListUtils;
import client.customExceptions.TaskListException;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class RenameListCtrl {
    private final TaskListUtils listUtils;
    private final MainCtrl mainCtrl;
    private final CustomAlert customAlert;

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
    public RenameListCtrl(final TaskListUtils listUtils, final MainCtrl mainCtrl,
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
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
        }
    }

}
