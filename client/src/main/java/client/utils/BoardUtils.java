package client.utils;

import client.customExceptions.BoardException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Board;
import commons.BoardColorScheme;
import commons.TaskPreset;
import commons.BoardEvent;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javafx.util.Pair;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;



public class BoardUtils {

    private final ServerUtils server;
    @Inject
    public BoardUtils(final ServerUtils server) {
        this.server = server;
    }

    public List<Board> getBoards() throws BoardException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/boards",
                RestUtils.Methods.GET, null);
        return server.getRestUtils().handleResponse(response, new GenericType<List<Board>>() {},
                "getBoards");
    }

    /**
     *
     * @param boardId the id of the board to get
     * @return the board
     */
    public Board getBoard(final long boardId) throws BoardException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/boards/" + boardId, RestUtils.Methods.GET, null);
        try {
            return server.getRestUtils().handleResponse(response, Board.class, "getBoard");
        }
        catch(Exception e){
            throw new BoardException(e.getMessage());
        }
    }
    /**
     * Create a board on the server.
     *
     * @param board The board to create.
     * @return The created board.
     */

    public Board addBoard(final Board board) throws BoardException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/boards", RestUtils.Methods.POST, board);
        try {
            return server.getRestUtils().handleResponse(response, Board.class, "addBoard");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new BoardException(e.getMessage());
        }
    }


    /**
     * Update a board on the server
     *
     * @param boardId the id of the board to update
     * @param newName the new name of the board
     * @return a response indicating whether the update was successful
     */
    public Board renameBoard(final long boardId,final String newName) throws BoardException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/boards/"+boardId, RestUtils.Methods.PUT,
                newName,
                new Pair<>("name", newName));
        try {
            return server.getRestUtils().handleResponse(response, Board.class, "renameBoard");
        }
        catch(Exception e){
            throw new BoardException(e.getMessage());
        }

    }

    /**
     * Delete a board on the server
     *
     * @param boardId the id of the board to delete
     * @return a response indicating whether the board was deleted successfully or not
     */
    public Board deleteBoard(final long boardId) throws BoardException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/boards/" + boardId, RestUtils.Methods.DELETE, null);
        try {
            return server.getRestUtils().handleResponse(response, Board.class, "deleteBoard");
        }
        catch(Exception e){
            throw new BoardException(e.getMessage());
        }
    }


    /**
     * Join a board on the server
     *
     * @param inviteKey the invite key of the board to join
     * @param memberName the name of the member joining the board
     *   (will either be kept as username or will relate to IP address of client)
     * @return a response indicating whether the board was joined successfully or not
     * @throws BoardException if the invite key is invalid/not found/other errors
     */
    public Board joinBoard(final String inviteKey, final String memberName) throws BoardException {
        long boardId;
        try {
            boardId = Long.parseLong(inviteKey.substring(0, 3));
            Board board = getBoard(boardId);
            if (!inviteKey.equals(board.getInviteKey())) {
                throw new BoardException("Wrong invite key");
            }
            Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                    "api/boards/" + boardId + "/join", RestUtils.Methods.PUT,
                    memberName, new Pair<>("memberName", memberName));
            try {
                return server.getRestUtils().handleResponse(response, Board.class, "joinBoard");
            }
            catch(Exception e){
                throw new BoardException(e.getMessage());
            }
        } catch (NumberFormatException | IndexOutOfBoundsException  e) {
            throw new BoardException("Invalid invite key format");
        }
    }

    /**
     * get the invite key of a board based on its id
     * (used for copying inv code)
     * @param boardId the id of the board
     * @return the invite key of the board
     * @throws BoardException if the board is not found/other errors
     */
    public String getBoardInviteKey(final long boardId) throws BoardException{
        Board board = getBoard(boardId);
        return board.getInviteKey();
    }

    public BoardColorScheme getBoardColorScheme(final long boardId) throws BoardException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/boards/" + boardId, RestUtils.Methods.GET, null);
        try {
            return server.getRestUtils().handleResponse(response, Board.class, "getBoard")
                    .getBoardColorScheme();
        }
        catch(Exception e){
            throw new BoardException(e.getMessage());
        }

    }
    public BoardColorScheme setBoardColorScheme(final long boardId,
                                                final BoardColorScheme boardColorScheme)
            throws BoardException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/boards/" + boardId + "/setboardcolorscheme",
                RestUtils.Methods.PUT, boardColorScheme);
        try {
            return server.getRestUtils().handleResponse(response,
                    BoardColorScheme.class, "setBoardColorScheme");
        }
        catch(Exception e){
            throw new BoardException(e.getMessage());
        }
    }



    public TaskPreset addTaskPreset(final long boardId, final TaskPreset taskPreset){
        RestUtils restUtils = server.getRestUtils();
        Response response = restUtils.sendRequest(server.getServerAddress(),
                "api/boards/" + boardId + "/addtaskpreset",
                RestUtils.Methods.POST, taskPreset);
        try {
            return restUtils.handleResponse(response, TaskPreset.class, "addTaskPreset");
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public TaskPreset removeTaskPreset(final long boardId, final long taskPresetId){
        RestUtils restUtils = server.getRestUtils();
        Response response = restUtils.sendRequest(server.getServerAddress(),
                "api/boards/" + boardId + "/removetaskpreset/"+taskPresetId,
                RestUtils.Methods.DELETE, null);
        try {
            return restUtils.handleResponse(response, TaskPreset.class, "removeTaskPreset");
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public TaskPreset updateTaskPreset(final long boardId, final TaskPreset taskPreset){
        RestUtils restUtils = server.getRestUtils();
        Response response = restUtils.sendRequest(server.getServerAddress(),
                "api/boards/" + boardId + "/updatetaskpreset",
                RestUtils.Methods.PUT, taskPreset);
        try {
            return restUtils.handleResponse(response, TaskPreset.class, "updateTaskPreset");
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }


    private ExecutorService exec = Executors.newSingleThreadExecutor();
    public void registerForUpdatesBoards(final Consumer<BoardEvent> boardConsumer) {

        exec = Executors.newSingleThreadExecutor();
        exec.submit(() -> {
            while (!Thread.interrupted()) {
                Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                        "api/boards/updates", RestUtils.Methods.GET, null);
                if(response.getStatus() == 204 ) {
                    continue;
                }
                String responseBody = response.readEntity(String.class);
                BoardEvent event = null;
                try {
                    event = new ObjectMapper().readValue(responseBody, BoardEvent.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                boardConsumer.accept(event);
            }
        });
    }

    public void stop() {
        exec.shutdownNow();
    }
}
