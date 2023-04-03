package client.scenes;

import client.customExceptions.BoardException;
import client.utils.BoardUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ColorManagementViewCtrl {

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

    private BoardColorScheme boardColorScheme;
    @FXML
    private TextField taskColorPresetNameInput;
    private String taskColorPresetName;

    private BoardUtils boardUtils;
    private Board board;

    @Inject
    public ColorManagementViewCtrl(final ServerUtils server, final MainCtrl mainCtrl,
                                   final BoardUtils boardUtils,
                                   final BoardOverviewCtrl boardOverviewCtrl)
            throws BoardException {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.boardUtils = boardUtils;

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
        if(taskColorPresetName.isEmpty()|| taskColorPresetName == null) {
            throw new IllegalArgumentException("Empty name not allowed");
        }
        TaskPreset taskpreset = new TaskPreset(taskColorPresetName);

    }

    public void back() throws BoardException {
        System.out.println(board.getBoardColorScheme().toString());
        boardUtils.setBoardColorScheme(board.id, board.getBoardColorScheme());
        mainCtrl.showBoardCatalogue();
    }

    public void setBoard(final Board board) {
        this.board = board;
    }

    public void resetBoardColors() {
        board.getBoardColorScheme().setBoardBackgroundColor("0xcce6ff");
        board.getBoardColorScheme().setBoardTextColor("0x000000");
    }

    public void resetListColors() {
        board.getBoardColorScheme().setListBackgroundColor("0xD29FE3");
        board.getBoardColorScheme().setListTextColor("0x000000");
    }




}
