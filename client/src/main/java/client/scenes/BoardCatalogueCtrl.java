package client.scenes;

import client.CustomAlert;
import client.customExceptions.BoardException;
import client.services.BoardService;
import client.utils.NetworkUtils;
import client.utils.ServerUtils;
import client.utils.WebSocketUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.application.Platform;
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
import java.util.function.Consumer;

public class BoardCatalogueCtrl implements Initializable {

    WebSocketUtils webSocketUtils;
    BoardService boardService;
    NetworkUtils networkUtils;
    ServerUtils serverUtils;
    MainCtrl mainCtrl;
    EditBoardCtrl editBoardCtrl;
    CustomAlert customAlert;
    Map<Long, Tab> boardsMap;

    @FXML
    TabPane catalogue;

    @Inject
    public BoardCatalogueCtrl(final WebSocketUtils webSocketUtils,
                              final MainCtrl mainCtrl, final NetworkUtils networkUtils,
                              final CustomAlert customAlert, final EditBoardCtrl editBoardCtrl,
                              final BoardService boardService){
        this.networkUtils = networkUtils;
        this.webSocketUtils = webSocketUtils;
        this.serverUtils=networkUtils.getServerUtils();
        this.boardService = boardService;
        this.mainCtrl=mainCtrl;
        this.customAlert=customAlert;
        this.editBoardCtrl=editBoardCtrl;
        boardsMap=new HashMap<>();
    }

    public WebSocketUtils getWebSocketUtils() {
        return webSocketUtils;
    }

    /** Initializes the catalogue */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        var joinBoardLoader = new FXMLLoader(getClass().getResource("JoinBoard.fxml"));
        joinBoardLoader.setControllerFactory(type -> new JoinBoardCtrl(networkUtils, mainCtrl,
            customAlert, this, webSocketUtils ));
        try {
            Node joinBoard = joinBoardLoader.load();
            var tab=new Tab("Add Board +", joinBoard);
            tab.setClosable(false);
            catalogue.getTabs().add(tab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createWebSockets(){
        Consumer<Board> renameBoard = (board) -> {
            Platform.runLater(() -> {
                Tab tab = boardsMap.get(board.id);
                tab.setText(board.getName());
            });
        };
        Consumer<Board> deleteBoard = (board) -> {
            Platform.runLater(() -> {
                this.catalogue.getTabs().remove(boardsMap.get(board.id));
            });
        };
        webSocketUtils.tryToConnect();
        webSocketUtils.registerForMessages("/topic/renameboard", renameBoard, Board.class);
        webSocketUtils.registerForMessages("/topic/deleteboard", deleteBoard, Board.class);
    }

    /**
     * Adds a new board to the catalogue
     * @param boardId the id of the board to add
     */
    public void addBoard(final long boardId) throws BoardException{
        try {
            Board board = boardService.getBoard(boardId);
            var tab = new Tab(board.getName());
            tab.setContent(createBoardOverview(board, tab));
            catalogue.getTabs().add(catalogue.getTabs().size() - 1, tab);
            boardsMap.put(boardId, tab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Node createBoardOverview(final Board board, final Tab tab)
            throws IOException {
        var boardLoader = new FXMLLoader(getClass().getResource("BoardOverview.fxml"));
        BoardOverviewCtrl boardOverviewCtrl = new BoardOverviewCtrl(mainCtrl,
                customAlert, networkUtils, this, editBoardCtrl, webSocketUtils);
        boardOverviewCtrl.setCurrentBoardId(board.id);
        boardOverviewCtrl.setTab(tab);
        boardLoader.setControllerFactory(type -> boardOverviewCtrl);
        tab.setOnClosed(event -> {
            boardOverviewCtrl.clear();
            Servers.getInstance().getServers()
                    .get(serverUtils.getServerAddress()).remove(board.id);
            boardsMap.remove(board.id);
            catalogue.getTabs().remove(tab);
        });
        return boardLoader.load();
    }

    /**
     * Populates the catalogue with the boards of the current server
     */
    public void populate() {
        int index = -1;
        if(this.catalogue.getTabs().size() > 1)
            for(int i = 0; i < this.catalogue.getTabs().size()-1; i++)
                if(this.catalogue.getTabs().get(i).isSelected())
                    index = i;
        this.boardsMap.clear();
        if (catalogue.getTabs().size() > 1) {
            this.catalogue.getTabs().subList(0, catalogue.getTabs().size() - 1).clear();
        }
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
        if(index >= 0 && this.catalogue.getTabs().size() > index+1)
            this.catalogue.getSelectionModel().select(index);
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

    public void refresh() {

        for(Map.Entry<Long, Tab> e : boardsMap.entrySet()){
            try {
                Board board = boardService.getBoard(e.getKey());
                e.getValue().setContent(createBoardOverview(board, e.getValue()));
            } catch (IOException ex) {
                System.out.println("Error while refreshing boards: " + ex.getMessage());
            }
            catch (BoardException ex2){
                boardsMap.remove(e.getKey());
                catalogue.getTabs().remove(e.getValue());
            }
        }
    }
}
