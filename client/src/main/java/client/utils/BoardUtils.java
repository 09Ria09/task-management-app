package client.utils;

import client.customExceptions.BoardException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Board;
import commons.BoardEvent;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javafx.util.Pair;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

//TODO
/*
 * !
 * !    check if passing of serverAddress is correct and ask for feedback
 * on this aspect  - can't figure out how to properly organize this
 * such that there is one serverAddress that
 * changes everywhere like in a prototype bean
 * !    add method for getting all boards
 * !    reformat duplicate code - either use response handler
 * or leave as is
 */
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

    private ExecutorService exec = Executors.newSingleThreadExecutor();
    public void registerForUpdatesBoards(final Consumer<BoardEvent> boardConsumer) {
        System.out.println("registering for updates");
        exec = Executors.newSingleThreadExecutor();
        exec.submit(() -> {
            while (!Thread.interrupted()) {
                String serverAddress = server.getServerAddress();
                Response response = ClientBuilder.newClient(new ClientConfig())
                        .target(serverAddress)
                        .path("api/boards/updates")
                        .request()
                        .accept(APPLICATION_JSON)
                        .get();
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
