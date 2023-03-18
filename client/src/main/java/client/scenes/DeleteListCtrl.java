package client.scenes;

import client.utils.TaskListUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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
        boardId = Long.parseLong(boardIdInput.getText());
        listId = Long.parseLong(listIdInput.getText());
        listUtils.deleteTaskList(boardId, listId);
        boardIdInput.clear();
        listIdInput.clear();
        mainCtrl.showBoardOverview();
    }

}
