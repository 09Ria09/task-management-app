package client.scenes.adminScenes;

import client.customExceptions.BoardException;
import client.scenes.MainCtrl;
import client.services.BoardService;
import commons.Board;
import com.google.inject.Inject;
import commons.BoardEvent;
import commons.Tag;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AdminBoardCtrl {

    @FXML
    private TableView<Board> boardTableView;
    @FXML
    private TableColumn<Board, String> nameColumn;
    @FXML
    private TableColumn<Board, Integer> numOfListsColumn;
    @FXML
    private TableColumn<Board, String> tagsColumn;

    @FXML
    private TableColumn<Board, String> inviteKeyColumn;
    @FXML
    private TableColumn<Board, Void> deleteColumn;

    private BoardService boardService;

    private MainCtrl mainCtrl;

    @Inject
    public AdminBoardCtrl(final BoardService boardService, final MainCtrl mainCtrl) {
        this.boardService = boardService;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        numOfListsColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getListTaskList().size()));
        tagsColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getTags()
                        .stream().map(Tag::getName).reduce((a, b) -> a + ", " + b).orElse("")));
        inviteKeyColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getInviteKey()));
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setId("deleteButton");
                deleteButton.setOnAction(event -> {
                    Board board = getTableRow().getItem();
                    deleteBoard(board, true);
                });
            }

            @Override
            protected void updateItem(final Void item, final boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    public void addAllBoards() throws BoardException {
        List<Board> boards = boardService.getAllBoards();
        boardTableView.setItems(FXCollections.observableArrayList(boards));
    }

    public void deleteBoard(final Board board, final boolean adminAction) {
        try {
            Platform.runLater(() -> {
                boardTableView.getItems().remove(board);
            });
            boardService.deleteBoard(board, adminAction);
        } catch (BoardException e) {
            e.printStackTrace();
        }
    }
    public void addBoard(final Board board) {
        Platform.runLater(() -> {
            boardTableView.getItems().add(board);
        });
    }
    public void stop() {
        boardService.stop();
    }

    public void solveEvent(final BoardEvent event) {
        String eventType = event.getEventType();
        Board board = event.getBoard();
        switch (eventType) {
            case "ADD":
                addBoard(board);
                break;
            case "DELETE":
                deleteBoard(board, false);
                break;
            default:
                break;
        }
    }

    @FXML
    public void goBack() {
        boardService.stop();
        mainCtrl.showBoardCatalogue();
    }
}
