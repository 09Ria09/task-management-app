package client.scenes;

import client.CustomAlert;
import client.customExceptions.BoardException;
import client.utils.BoardUtils;
import client.utils.ServerUtils;
import client.utils.WebSocketUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.TaskPreset;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ColorManagementViewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;


    @FXML
    ListView<TaskPreset> taskColorPresetList;
    @FXML
    private ColorPicker boardBackgroundColorInput;
    @FXML
    private ColorPicker boardTextColorInput;
    @FXML
    private ColorPicker listBackgroundColorInput;
    @FXML
    private ColorPicker listTextColorInput;

    @FXML
    private TextField taskColorPresetNameInput;
    @FXML
    ColorPicker taskBackgroundColorInput;
    @FXML
    ColorPicker taskTextColorInput;

    private String taskColorPresetName;

    private BoardUtils boardUtils;
    private final WebSocketUtils webSocketUtils;
    private Board board;
    private CustomAlert customAlert;


    @Inject
    public ColorManagementViewCtrl(final ServerUtils server, final MainCtrl mainCtrl,
                                   final BoardUtils boardUtils,
                                   final WebSocketUtils webSocketUtils,
                                   final CustomAlert customAlert) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.boardUtils = boardUtils;
        this.webSocketUtils = webSocketUtils;
        this.customAlert = customAlert;
    }

    /**
     * Initialize the color management view
     * @param location the location
     * @param resources the resources
     */
    public void initialize(final URL location, final ResourceBundle resources) {
        var ctrl = this;
        taskColorPresetList.setCellFactory(lv ->
            new ListCell<>() {
                @Override
                protected void updateItem(final TaskPreset taskPreset, final boolean empty) {
                    super.updateItem(taskPreset, empty);
                    if (taskPreset == null || empty) {
                        setGraphic(null);
                        setBackground(Background.EMPTY);
                    } else {
                        try {
                            var presetLoader = new FXMLLoader(getClass()
                                .getResource("TaskColorPresetCard.fxml"));
                            presetLoader.setControllerFactory(type ->
                                new TaskColorPresetCardCtrl(boardUtils, board,
                                    customAlert, ctrl));
                            Node preset = presetLoader.load();
                            TaskColorPresetCardCtrl presetCtrl =
                                presetLoader.getController();
                            presetCtrl.initialize(taskPreset);
                            setGraphic(preset);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        taskColorPresetList.setPlaceholder(new Label("No task color presets"));
    }


    public void setBoardBackgroundColor() {
        board.getBoardColorScheme().
            setBoardBackgroundColor(String.valueOf(boardBackgroundColorInput.getValue()));
    }

    public void setBoardTextColor() {
        board.getBoardColorScheme().
            setBoardTextColor(String.valueOf(boardTextColorInput.getValue()));
    }

    public void setListBackgroundColor() {
        board.getBoardColorScheme().
                setListBackgroundColor(String.valueOf(listBackgroundColorInput.getValue()));
    }

    public void setListTextColor() {
        board.getBoardColorScheme().
                setListTextColor(String.valueOf(listTextColorInput.getValue()));
    }

    public void createTaskColorPreset() {
        taskColorPresetName = taskColorPresetNameInput.getText();
        if (taskColorPresetName.isEmpty() || taskColorPresetName == null) {
            Alert alert = customAlert.showAlert("Empty name not allowed.");
            alert.showAndWait();
            return;
        }
        TaskPreset taskPreset = new TaskPreset(taskColorPresetName,
            taskBackgroundColorInput.getValue().toString(),
            taskTextColorInput.getValue().toString());
        boardUtils.addTaskPreset(board.id, taskPreset);
        populateTaskColorPresetList();
    }

    public void back() throws BoardException {
        System.out.println(board.getBoardColorScheme().toString());
        boardUtils.setBoardColorScheme(board.id, board.getBoardColorScheme());
        mainCtrl.showBoardCatalogue();
    }

    public void setBoard(final Board board) {
        this.board = board;
        this.boardBackgroundColorInput.setValue(Color.valueOf(
                board.getBoardColorScheme().getBoardBackgroundColor()));
        this.boardTextColorInput.setValue(Color.valueOf(
                board.getBoardColorScheme().getBoardTextColor()));
        this.listBackgroundColorInput.setValue(Color.valueOf(
                board.getBoardColorScheme().getListBackgroundColor()));
        this.listTextColorInput.setValue(Color.valueOf(
                board.getBoardColorScheme().getListTextColor()));

        webSocketUtils.tryToConnect();
        Consumer<Board> deleteBoard = (b) -> {
            Platform.runLater(mainCtrl::showBoardCatalogue);
        };
        webSocketUtils.registerForMessages("/topic/deleteboard", deleteBoard, Board.class);
    }

    public void resetBoardColors() {
        board.getBoardColorScheme().setBoardBackgroundColor("0xffffffff");
        board.getBoardColorScheme().setBoardTextColor("0x000000");
        boardBackgroundColorInput.setValue(Color.valueOf("0xffffffff"));
        boardTextColorInput.setValue(Color.valueOf("0x000000"));
    }

    public void resetListColors() {
        board.getBoardColorScheme().setListBackgroundColor("0xffffffff");
        board.getBoardColorScheme().setListTextColor("0x000000");
        listBackgroundColorInput.setValue(Color.valueOf("0xffffffff"));
        listTextColorInput.setValue(Color.valueOf("0x000000"));
    }

    /**
     * Populate the task color preset list
     */
    public void populateTaskColorPresetList() {
        try {
            taskColorPresetList.getItems().clear();
            board = boardUtils.getBoard(board.id);
            taskColorPresetList.getItems().addAll(board.getTaskPresets());
        } catch (BoardException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove a task color preset
     * @param taskPreset the task color preset to remove
     */
    public void removeTaskPreset(final TaskPreset taskPreset) {
        boardUtils.removeTaskPreset(board.id, taskPreset.getId());
        taskColorPresetList.getItems().remove(taskPreset);
    }
}
