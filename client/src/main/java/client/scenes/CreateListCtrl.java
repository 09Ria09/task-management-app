package client.scenes;

import client.utils.ServerUtils;
import client.utils.TaskListUtils;
import com.google.inject.Inject;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class CreateListCtrl {

    private final ServerUtils server;
    private final TaskListUtils listUtils;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField listNameInput;
    @FXML
    private TextField boardIdInput;
//    @FXML
//    private Button confirm;
//    @FXML
//    private Button cancel;

    long boardId;
    String listName;

    /**
     * Constructor
     *
     * @param server
     * @param listUtils
     * @param mainCtrl
     */
    @Inject
    public CreateListCtrl(final ServerUtils server, final TaskListUtils listUtils,
                          final MainCtrl mainCtrl) {
        this.server = server;
        this.listUtils = listUtils;
        this.mainCtrl = mainCtrl;
    }

    /**
     *
     * @param event When the user clicks confirm in the create list window,
     *              the new list will be added to the board and they will be
     *              returned to the board overview.
     */
    public void confirm(final ActionEvent event) {
        listName = listNameInput.getText();
        boardId = Long.parseLong(boardIdInput.getText());
        TaskList list = new TaskList(listName);
        listUtils.createTaskList(boardId, list);
        showServerBoards();

    }

    /**
     * Method to return to board overview scene
     */
    public void showServerBoards(){
        mainCtrl.showBoardOverview();
    }

    /**
     *
     * @param event When the user presses cancel in the create list window,
     *              they will be returned to the board overview.
     */
    public void cancel(final ActionEvent event) {
        listNameInput.clear();
        showServerBoards();
    }

}
