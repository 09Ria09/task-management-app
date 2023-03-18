package client.scenes;

import client.utils.TaskListUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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
    public RenameListCtrl(TaskListUtils listUtils, MainCtrl mainCtrl) {
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
        boardId = Long.parseLong(boardIdInput.getText());
        listId = Long.parseLong(listIdInput.getText());
        listName = listNameInput.getText();
        listUtils.renameTaskList(boardId, listId, listName);

        boardIdInput.clear();
        listIdInput.clear();
        listNameInput.clear();
        mainCtrl.showBoardOverview();
    }


}
