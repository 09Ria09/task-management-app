package client.scenes;

import client.CustomAlert;
import client.utils.TaskListUtils;
import client.customExceptions.TaskListException;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RenameListCtrl {
    private final TaskListUtils listUtils;
    private final MainCtrl mainCtrl;
    private final CustomAlert customAlert;

    @FXML
    private TextField listNameInput;

    private String listName;

    RenameListSingleton renameListSingleton = RenameListSingleton.getInstance();

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
        listNameInput.clear();
        mainCtrl.showBoardCatalogue();
    }

    /**
     * When user hits confirm, it renames the list
     */
    public void confirm() throws TaskListException {
        try {
            String newName = listNameInput.getText();
            long boardId = renameListSingleton.getBoardId();
            long listId = renameListSingleton.getListId();
            listUtils.renameTaskList(boardId, listId, newName);
            listNameInput.clear();
            mainCtrl.showBoardCatalogue();
        } catch(Exception e) {
            throw new TaskListException("Renaming task list unsuccessful");
        }
    }

}
