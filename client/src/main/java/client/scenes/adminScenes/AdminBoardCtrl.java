package client.scenes.adminScenes;

import client.customExceptions.BoardException;
import client.utils.BoardUtils;
import commons.Board;
import com.google.inject.Inject;
import commons.Tag;
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

    @Inject
    public AdminBoardCtrl(final BoardUtils boardUtils) {
        this.boardUtils = boardUtils;
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        numOfListsColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getListTaskList().size()));
        tagsColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getTags()
                        .stream().map(Tag::getName).reduce((a, b) -> a + ", " + b).orElse("")));
        inviteKeyColumn.setCellValueFactory(new PropertyValueFactory<>("inviteKey"));
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Board board = getTableRow().getItem();
                    deleteBoard(board);
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
    }

    private void deleteBoard(final Board board) {
        try {

            // Remove the board from the table view
            boardTableView.getItems().remove(board);
            // Use boardUtils to delete the board
            boardUtils.deleteBoard(board.getId());

        } catch (BoardException e) {
            e.printStackTrace();
        }
    }
}
