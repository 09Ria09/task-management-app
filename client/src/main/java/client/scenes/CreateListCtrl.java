package client.scenes;

import client.CustomAlert;
import client.utils.*;
import client.customExceptions.BoardException;
import client.customExceptions.TaskListException;
import com.google.inject.Inject;
import commons.Board;
import commons.TaskList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;


public class CreateListCtrl {

    private final ServerUtils server;
    private final TaskListUtils listUtils;
    private final BoardOverviewCtrl boardOverviewCtrl;
    private final MainCtrl mainCtrl;
    private final CustomAlert customAlert;

    private final LayoutUtils layoutUtils;
    private final WebSocketUtils webSocketUtils;

    @FXML
    private TextField listNameInput;

    @FXML
    private Label title;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    @FXML
    private VBox root;

    private long boardId;
    String listName;
    private BoardUtils boardUtils;

    /**
     * Constructor
     */
    @Inject
    public CreateListCtrl(final TaskListUtils listUtils,
                          final BoardOverviewCtrl boardOverviewCtrl, final MainCtrl mainCtrl,
                          final CustomAlert customAlert, final LayoutUtils layoutUtils,
                          final WebSocketUtils webSocketUtils, final BoardUtils boardUtils) {
        this.server = listUtils.getServer();
        this.listUtils = listUtils;
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.mainCtrl = mainCtrl;
        this.customAlert = customAlert;
        this.layoutUtils = layoutUtils;
        this.webSocketUtils = webSocketUtils;
        this.boardUtils=boardUtils;
    }

    public void initialize(){
        listNameInput.textProperty().addListener(
                this.layoutUtils.createMaxFieldLength(20, listNameInput));
    }

    /**
     * Creates a list with the given name
     */
    public void confirm() {
        try {
            listName = listNameInput.getText();
            TaskList list = new TaskList(listName);
            listUtils.createTaskList(boardId, list);
            listNameInput.clear();
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
        mainCtrl.showBoardCatalogue();
    }

    /**
     * Returns user to board overview
     */
    public void cancel() {
        listNameInput.clear();
        showServerBoards();
    }

    public void setBoardId(final long boardId) {
        this.boardId = boardId;
        webSocketUtils.tryToConnect();
        Consumer<Board> deleteBoard = (board) -> {
            Platform.runLater(this::cancel);
        };
        webSocketUtils.registerForMessages("/topic/deleteboard", deleteBoard, Board.class);
    }
}
