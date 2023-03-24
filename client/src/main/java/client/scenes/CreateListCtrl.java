package client.scenes;

import client.CustomAlert;
import client.utils.ServerUtils;
import client.utils.TaskListUtils;
import client.customExceptions.BoardException;
import client.customExceptions.TaskListException;
import com.google.inject.Inject;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;


public class CreateListCtrl {

    private final ServerUtils server;
    private final TaskListUtils listUtils;
    private final MainCtrl mainCtrl;
    private final CustomAlert customAlert;

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
                          final MainCtrl mainCtrl, final CustomAlert customAlert) {
        this.server = server;
        this.listUtils = listUtils;
        this.mainCtrl = mainCtrl;
        this.customAlert = customAlert;
    }

    /**
     * Creates a list with the given name
     */
    public void confirm() {
        try {
            listName = listNameInput.getText();
            boardId = Long.parseLong(boardIdInput.getText());
            TaskList list = new TaskList(listName);
            listUtils.createTaskList(boardId, list);
            listNameInput.clear();
            boardIdInput.clear();
            showServerBoards();
        } catch (BoardException | TaskListException | NumberFormatException e) {
            Alert alert = customAlert.showAlert(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Method to return to board overview scene
     */
    public void showServerBoards(){
        mainCtrl.showBoardOverview();
    }

    /**
     * Returns user to board overview
     */
    public void cancel() {
        listNameInput.clear();
        showServerBoards();
    }

}
