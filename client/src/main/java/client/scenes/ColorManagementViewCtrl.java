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
import javafx.scene.paint.Color;

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
    private String white = "0xffffffff";

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

    public void createTaskColorPreset() throws BoardException {
        System.out.println("y");
        taskColorPresetName = taskColorPresetNameInput.getText();
        if(taskColorPresetName.isEmpty()|| taskColorPresetName == null) {
            throw new IllegalArgumentException("Empty name not allowed");
        }
        TaskPreset taskpreset = new TaskPreset(taskColorPresetName, board);
        board.getTaskPresets().add(taskpreset);
        boardUtils.addTaskPreset(board.id, taskpreset);
        System.out.println(board.getBoardColorScheme().toString());
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
    }

    public void resetBoardColors() {
        board.getBoardColorScheme().setBoardBackgroundColor(white);
        board.getBoardColorScheme().setBoardTextColor(white);
        boardBackgroundColorInput.setValue(Color.valueOf(white));
        boardTextColorInput.setValue(Color.valueOf(white));
    }

    public void resetListColors() {
        board.getBoardColorScheme().setListBackgroundColor(white);
        board.getBoardColorScheme().setListTextColor(white);
        listBackgroundColorInput.setValue(Color.valueOf(white));
        listTextColorInput.setValue(Color.valueOf(white));
    }





}
