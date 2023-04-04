package client.utils;

import client.customExceptions.BoardException;
import client.customExceptions.TaskException;
import com.google.inject.Inject;
import commons.Board;
import commons.BoardColorScheme;
import commons.Task;
import commons.TaskPreset;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

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

    /**
     *
     * @param boardId the id of the board to get
     * @return the board
     */
    public Board getBoard(final long boardId) throws BoardException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId)
                .request()
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Board.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new BoardException("Board not found.");
        } else {
            throw new BoardException("An error occurred while fetching the board");
        }
    }
    /**
     * Create a board on the server.
     *
     * @param board The board to create.
     * @return The created board.
     */

    public Board addBoard(final Board board) throws BoardException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards")
                .request()
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Board.class);
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new BoardException("You inputted a wrong value");
        } else {
            throw new BoardException("An error occurred while adding the board");
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
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/"+boardId)
                .queryParam("name", newName)
                .request()
                .accept(APPLICATION_JSON)
                .put(Entity.entity(newName, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Board.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new BoardException("Board not found.");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new BoardException("You inputted a wrong value");
        }else {
            throw new BoardException("An error occurred while renaming the board");
        }

    }

    /**
     * Delete a board on the server
     *
     * @param boardId the id of the board to delete
     * @return a response indicating whether the board was deleted successfully or not
     */
    public Board deleteBoard(final long boardId) throws BoardException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId)
                .request()
                .accept(APPLICATION_JSON)
                .delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Board.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new BoardException("Board not found.");
        } else {
            throw new BoardException("An error occurred while deleting the board");
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
            String serverAddress = server.getServerAddress();
            Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                    .path("api/boards/" + boardId + "/join")
                    .queryParam("memberName", memberName)
                    .request()
                    .accept(APPLICATION_JSON)
                    .put(Entity.entity(memberName, APPLICATION_JSON));
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(Board.class);
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                throw new BoardException("Board not found.");
            } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
                throw new BoardException("You inputted a wrong value");
            } else {
                throw new BoardException("An error occurred while joining the board");
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
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId + "/boardcolorscheme")
                .request()
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<BoardColorScheme>() {});
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new BoardException("Board not found.");
        } else {
            throw new BoardException("An error occurred while fetching the board's color scheme");
        }

    }
///{boardid}/setboardcolorscheme
    public BoardColorScheme setBoardColorScheme(final long boardId,
                                                final BoardColorScheme boardColorScheme)
            throws BoardException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId + "/setboardcolorscheme")
                .request()
                .accept(APPLICATION_JSON)
                .put(Entity.entity(boardColorScheme, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<BoardColorScheme>() {});
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new BoardException("Board not found.");
        } else {
            throw new BoardException("An error occurred while fetching the board's color scheme");
        }

    }

//    public TaskPreset setTaskPreset(final long boardColorSchemeId, final TaskPreset taskPreset) {
//        String serverAddress = server.getServerAddress();
//        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
//                .path("api/boards/" + boardColorSchemeId + "/taskpresets/")
//                .request()
//                .accept(APPLICATION_JSON)
//                .put(Entity.entity(boardColorScheme, APPLICATION_JSON));
//    }

    public TaskPreset addTaskPreset(final long boardId, final TaskPreset taskPreset)
            throws BoardException {
        String serverAddress = server.getServerAddress();
        Response response =  ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId + "/addtaskpreset")
                .request()
                .accept(APPLICATION_JSON)
                .post(Entity.entity(taskPreset, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(TaskPreset.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new BoardException("Board not found.");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new BoardException("An error occurred while adding the task preset");
        } else {
            throw new BoardException("An error occurred while adding the task preset");
        }
    }

//    public Board addBoard(final Board board) throws BoardException {
//        String serverAddress = server.getServerAddress();
//        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
//                .path("api/boards")
//                .request()
//                .accept(APPLICATION_JSON)
//                .post(Entity.entity(board, APPLICATION_JSON));
//
//        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
//            return response.readEntity(Board.class);
//        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
//            throw new BoardException("You inputted a wrong value");
//        } else {
//            throw new BoardException("An error occurred while adding the board");
//        }
//    }

}
