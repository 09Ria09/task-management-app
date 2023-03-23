package client.scenes;

import client.utils.ServerUtils;
import client.utils.TaskListUtils;
import client.utils.customExceptions.BoardException;
import client.utils.customExceptions.TaskListException;
import com.google.inject.Inject;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;


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
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
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
