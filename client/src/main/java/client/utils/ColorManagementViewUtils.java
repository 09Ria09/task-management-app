package client.utils;

import client.customExceptions.BoardException;
import commons.BoardColorScheme;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;


public class ColorManagementViewUtils {

//    private final ServerUtils server;
//
//
//    public ColorManagementViewUtils(final ServerUtils server) {
//        this.server = server;
//    }
//
//    public BoardColorScheme getBoardColorScheme(final long boardId) throws BoardException {
//        String serverAddress = server.getServerAddress();
//        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
//                .path("api/boardcolorscheme")
//                .request()
//                .accept(APPLICATION_JSON)
//                .get();
//
//        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
//            return response.readEntity(new GenericType<BoardColorScheme>() {});
//        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
//            throw new BoardException("Board not found.");
//        } else {
//            throw new BoardException("An error occurred while fetching the board's color scheme");
//        }
//
//    }
}
