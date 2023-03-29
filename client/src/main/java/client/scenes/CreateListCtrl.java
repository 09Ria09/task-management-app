package client.scenes;

import client.CustomAlert;
import client.utils.LayoutUtils;
import client.utils.ServerUtils;
import client.utils.TaskListUtils;
import client.customExceptions.BoardException;
import client.customExceptions.TaskListException;
import com.google.inject.Inject;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class CreateListCtrl {

    private final ServerUtils server;
    private final TaskListUtils listUtils;
    private final BoardOverviewCtrl boardOverviewCtrl;
    private final MainCtrl mainCtrl;
    private final CustomAlert customAlert;

    private final LayoutUtils layoutUtils;

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

    long boardId;
    String listName;

    /**
     * Constructor
     *
     * @param server
     * @param listUtils
     * @param boardOverviewCtrl
     * @param mainCtrl
     */
    @Inject
    public CreateListCtrl(final ServerUtils server, final TaskListUtils listUtils,
                          final BoardOverviewCtrl boardOverviewCtrl, final MainCtrl mainCtrl,
                          final CustomAlert customAlert, final LayoutUtils layoutUtils) {
        this.server = server;
        this.listUtils = listUtils;
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.mainCtrl = mainCtrl;
        this.customAlert = customAlert;
        this.layoutUtils = layoutUtils;
    }

    public void initialize(){
        listNameInput.textProperty().addListener(
                this.layoutUtils.createMaxFieldLength(20, listNameInput));
        listNameInput.fontProperty().bind(layoutUtils.createFontBinding(root, 0.08D, 30.0d));
        title.fontProperty().bind(layoutUtils.createFontBinding(root, 0.12D, 48.0d));
        confirmButton.prefHeightProperty().bind(root.heightProperty().multiply(0.075D));
        confirmButton.prefWidthProperty().bind(root.widthProperty().multiply(0.15D));
        confirmButton.fontProperty().bind(layoutUtils.createFontBinding(root, 0.08D, 30.0d));
        cancelButton.prefHeightProperty().bind(root.heightProperty().multiply(0.075D));
        cancelButton.prefWidthProperty().bind(root.widthProperty().multiply(0.15D));
        cancelButton.fontProperty().bind(layoutUtils.createFontBinding(root, 0.08D, 30.0d));
    }

    /**
     * Creates a list with the given name
     */
    public void confirm() {
        try {
            listName = listNameInput.getText();
            boardId = boardOverviewCtrl.getCurrentBoardId();
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
