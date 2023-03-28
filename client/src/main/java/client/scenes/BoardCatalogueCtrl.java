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
import java.util.ResourceBundle;

public class BoardCatalogueCtrl implements Initializable {
    ServerUtils serverUtils;
    BoardUtils boardUtils;
    MainCtrl mainCtrl;
    CustomAlert customAlert;

    @FXML
    TabPane catalogue;

    @Inject
    public BoardCatalogueCtrl(final ServerUtils serverUtils, final BoardUtils boardUtils,
                              final MainCtrl mainCtrl,
                              final CustomAlert customAlert){
        this.serverUtils=serverUtils;
        this.boardUtils=boardUtils;
        this.mainCtrl=mainCtrl;
        this.customAlert=customAlert;
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
    public int addBoard(final long boardId){
        try {
            Board board = boardUtils.getBoard(boardId);
            var boardLoader = new FXMLLoader(getClass().getResource("BoardOverview.fxml"));
            BoardOverviewCtrl boardOverviewCtrl = new BoardOverviewCtrl(serverUtils, mainCtrl,
                customAlert, boardUtils);
            boardOverviewCtrl.setCurrentBoardId(boardId);
            boardOverviewCtrl.refreshTimer(500);
            boardLoader.setControllerFactory(type -> boardOverviewCtrl);
            Node boardOverview = boardLoader.load();
            var tab = new Tab(board.getName(), boardOverview);
            tab.setClosable(true);
            tab.setOnClosed(event -> {
                Servers.getInstance().getServers()
                    .get(serverUtils.getServerAddress()).remove(boardId);
                boardOverviewCtrl.clear();
            });
            catalogue.getTabs().add(catalogue.getTabs().size() - 1, tab);
            //TODO: sort them alphabetically
            return catalogue.getTabs().size() - 1;
        } catch (BoardException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Populates the catalogue with the boards of the current server
     */
    public void populate() {
        var boards  = Servers.getInstance().getServers()
            .get(serverUtils.getServerAddress());
        for (Long boardID : boards) {
            addBoard(boardID);
        }
    }

    /**
     * Adds a new board to the catalogue
     * @param boardId the id of the board to add
     */
    public void addNew(final long boardId) {
        Servers.getInstance().getServers().get(serverUtils.getServerAddress()).add(boardId);
        catalogue.getSelectionModel().select(addBoard(boardId));
    }

    /** Closes all the tabs */
    public void close() {
        Servers.getInstance().save();
        for (Tab tab : catalogue.getTabs()) {
            if (tab.getOnClosed() != null)
                tab.getOnClosed().handle(null);
        }
    }
}
