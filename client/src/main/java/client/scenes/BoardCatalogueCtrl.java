package client.scenes;

import client.CustomAlert;
import client.customExceptions.BoardException;
import client.utils.BoardUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import objects.Servers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

public class BoardCatalogueCtrl implements Initializable {
    ServerUtils serverUtils;
    BoardUtils boardUtils;
    MainCtrl mainCtrl;
    EditBoardCtrl editBoardCtrl;
    CustomAlert customAlert;
    Map<Long, Tab> boardsMap;

    @FXML
    TabPane catalogue;

    @Inject
    public BoardCatalogueCtrl(final ServerUtils serverUtils, final BoardUtils boardUtils,
                              final MainCtrl mainCtrl, final CustomAlert customAlert,
                              final EditBoardCtrl editBoardCtrl){
        this.serverUtils=serverUtils;
        this.boardUtils=boardUtils;
        this.mainCtrl=mainCtrl;
        this.customAlert=customAlert;
        this.editBoardCtrl=editBoardCtrl;
        boardsMap=new HashMap<>();
    }

    /** Initializes the catalogue */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        var joinBoardLoader = new FXMLLoader(getClass().getResource("JoinBoard.fxml"));
        joinBoardLoader.setControllerFactory(type -> new JoinBoardCtrl(serverUtils,
            customAlert, boardUtils, this));
        try {
            Node joinBoard = joinBoardLoader.load();
            var tab=new Tab("Add Board +", joinBoard);
            tab.setClosable(false);
            catalogue.getTabs().add(tab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new board to the catalogue
     * @param boardId the id of the board to add
     * @return the index of the tab
     */
    public void addBoard(final long boardId) throws BoardException{
        try {
            Board board = boardUtils.getBoard(boardId);
            var tab = new Tab(board.getName());
            var boardLoader = new FXMLLoader(getClass().getResource("BoardOverview.fxml"));
            BoardOverviewCtrl boardOverviewCtrl = new BoardOverviewCtrl(serverUtils, mainCtrl,
                customAlert, boardUtils, this, editBoardCtrl);
            boardOverviewCtrl.setCurrentBoardId(boardId);
            boardOverviewCtrl.setTab(tab);
            boardLoader.setControllerFactory(type -> boardOverviewCtrl);
            Node boardOverview = boardLoader.load();
            tab.setContent(boardOverview);
            tab.setOnClosed(event -> {
                boardOverviewCtrl.clear();
                Servers.getInstance().getServers()
                    .get(serverUtils.getServerAddress()).remove(boardId);
                boardsMap.remove(tab);
                catalogue.getTabs().remove(tab);
            });
            catalogue.getTabs().add(catalogue.getTabs().size() - 1, tab);
            //TODO: sort them alphabetically
            boardsMap.put(boardId, tab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Populates the catalogue with the boards of the current server
     */
    public void populate() {
        var boards = Servers.getInstance().getServers()
            .get(serverUtils.getServerAddress());
        Iterator<Long> boardIterator = boards.iterator();
        while (boardIterator.hasNext()) {
            Long boardID = boardIterator.next();
            try {
                addBoard(boardID);
            } catch (BoardException e) {
                boardIterator.remove();
            }
        }
    }

    /**
     * Adds a new board to the catalogue
     * @param boardId the id of the board to add
     */
    public void addNew(final long boardId) {
        if(Servers.getInstance().getServers().get(serverUtils.getServerAddress()).add(boardId)) {
            Servers.getInstance().save();
            try {
                addBoard(boardId);
            } catch (BoardException e) {
                throw new RuntimeException(e);
            }
        }
        catalogue.getSelectionModel().select(boardsMap.get(boardId));
    }

    /** Closes all the tabs */
    public void close() {
        Servers.getInstance().save();
        if(catalogue.getTabs().size() > 1)
            catalogue.getTabs().remove(0, catalogue.getTabs().size() - 1);
    }
}
