package client.scenes.adminScenes;

import client.customExceptions.BoardException;
import client.scenes.MainCtrl;
import client.utils.BoardUtils;
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

    private BoardUtils boardUtils;

    private MainCtrl mainCtrl;

    @Inject
    public AdminBoardCtrl(final BoardUtils boardUtils, final MainCtrl mainCtrl) {
        this.boardUtils = boardUtils;
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
        List<Board> boards = boardUtils.getBoards();
        boardTableView.setItems(FXCollections.observableArrayList(boards));
        System.out.println("Added all boards");
    }

    private void deleteBoard(final Board board, final boolean adminAction) {
        try {
            System.out.println("Deleting board " + board.getName());
            Platform.runLater(() -> {
                boardTableView.getItems().remove(board);
            });
            System.out.println(board);
            //here I check if the deletion is done from the dashboard
            //or by an user in the overview, so that I dont call
            //the deleteBoard method twice
            if (adminAction) {
                boardUtils.deleteBoard(board.getId());
            }

        } catch (BoardException e) {
            e.printStackTrace();
        }
    }
    public void addBoard(final Board board) {
        System.out.println("Adding board " + board.getName());
        Platform.runLater(() -> {
            boardTableView.getItems().add(board);
        });
    }
    public void stop() {
        boardUtils.stop();
    }

    public void solveEvent(final BoardEvent event) {
        String eventType = event.getEventType();
        Board board = event.getBoard();
        System.out.println("Solving event " + eventType + " for board " + board.getName());
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
    private void goBack() {
        System.out.println("Going back to board catalogue");
        boardUtils.stop();
        mainCtrl.showBoardCatalogue();
    }
}
