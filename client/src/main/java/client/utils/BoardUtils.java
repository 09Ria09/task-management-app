package client.utils;

import com.google.inject.Inject;
import commons.Board;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;


import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

//TODO
/*
 * !    find a way to pass errors to frontend controllers instead
 * of just printing them
 * !    check if passing of serverAddress is correct and ask for feedback
 * on this aspect  - can't figure out how to properly organize this
 * such that there is one serverAddress that
 * changes everywhere like in a prototype bean
 * !    add method for getting all boards
 * !    reformat duplicate code
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
    public Board getBoard(final long boardId) {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId)
                .request()
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Board.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            System.out.println("Board not found.");
        } else {
            System.out.println("An error occurred while fetching the board: "
                    + response.readEntity(String.class));
        }

        return null;
    }
    /**
     * Create a board on the server.
     *
     * @param board The board to create.
     * @return The created board.
     */

    public Board addBoard(final Board board) {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards")
                .request()
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Board.class);
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            System.out.println("Bad request: " + response.readEntity(String.class));
        } else {
            System.out.println("An error occurred while adding the board: "
                    + response.readEntity(String.class));
        }

        return null;
    }


    /**
     * Update a board on the server
     *
     * @param boardId the id of the board to update
     * @param newName the new name of the board
     * @return a response indicating whether the update was successful
     */
    public Board renameBoard(final long boardId,final String newName) {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/"+boardId)
                .queryParam("name", newName)
                .request()
                .accept(APPLICATION_JSON)
                .post(Entity.entity(newName, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Board.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            System.out.println("Board not found.");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            System.out.println("Bad request: " + response.readEntity(String.class));
        }else {
            System.out.println("An error occurred while renaming the board: "
                    + response.readEntity(String.class));
        }

        return null;
    }

    /**
     * Delete a board on the server
     *
     * @param boardId the id of the board to delete
     * @return a response indicating whether the board was deleted successfully or not
     */
    public Board deleteBoard(final long boardId) {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId)
                .request()
                .accept(APPLICATION_JSON)
                .delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Board.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            System.out.println("Board not found.");
        } else {
            System.out.println("An error occurred while deleting the board: "
                    + response.readEntity(String.class));
        }

        return null;
    }

}
