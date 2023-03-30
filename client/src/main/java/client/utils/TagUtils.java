package client.utils;

import client.customExceptions.TagException;
import com.google.inject.Inject;
import commons.Tag;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.util.*;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class TagUtils {

    private final ServerUtils server;

    @Inject
    public TagUtils(final ServerUtils server) {
        this.server = server;
    }

    /**
     * Get all tags in a board
     * @param boardId ID of the board
     * @return the list of tags from the board
     * @throws TagException throws an exception if something goes wrong
     */
    public List<Tag> getBoardTags(final long boardId) throws TagException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/tags/" + boardId + "/tags")
                .request()
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<>() {});
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TagException("Tag not found.");
        } else {
            throw new TagException("An error occurred while fetching the tags");
        }
    }

    /**
     * Get all the tags from a task
     * @param boardId ID of the board
     * @param listId ID of the task list
     * @param taskId ID of the task
     * @return the list of tags from the task
     * @throws TagException throws an exception if something goes wrong
     */
    public List<Tag> getTaskTags(final long boardId, final long listId,
                                 final long taskId) throws TagException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/tags/" + boardId + "/" + listId + "/" + taskId + "/tags")
                .request()
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<>() {});
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TagException("Tag not found.");
        } else {
            throw new TagException("An error occurred while fetching the tags");
        }
    }

    /**
     * Get a specific tag from the board
     * @param boardId ID of the board
     * @param tagId ID of the tag
     * @return the tag that corresponds to the ID's
     * @throws TagException throws an error if something goes wrong
     */
    public Tag getBoardTag(final long boardId, final long tagId) throws TagException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/tags/" + boardId + "/" + tagId)
                .request()
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Tag.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TagException("Tag not found.");
        } else {
            throw new TagException("An error occurred while fetching the tag");
        }
    }

    /**
     * Get a specific tag from a task
     * @param boardId ID of the board
     * @param listId ID of the list
     * @param taskId ID of the task
     * @param tagId ID of the tag
     * @return the tag that corresponds to the ID's
     * @throws TagException throws an exception if something goes wrong
     */
    public Tag getTaskTag(final long boardId, final long listId,
                          final long taskId, final long tagId) throws TagException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/tags/" + boardId + "/" + listId + "/" + taskId + "/" + tagId)
                .request()
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Tag.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TagException("Tag not found.");
        } else {
            throw new TagException("An error occurred while fetching the tag");
        }
    }

    /**
     * Add a tag to a board
     * @param boardId ID of the board
     * @param tag the tag to add
     * @return the tag that is added
     * @throws TagException throws an exception if something goes wrong
     */
    public Tag addBoardTag(final long boardId, final Tag tag) throws TagException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/tags/" + boardId + "/tag")
                .request()
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tag, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Tag.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TagException("Board not found.");
        } else if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new TagException("You inputted a wrong value");
        } else {
            throw new TagException("An error occurred while adding the tag");
        }
    }

    /**
     * Add a tag to a task
     * @param boardId ID of the board
     * @param listId ID of the list
     * @param taskId ID of the task
     * @param tag the tag to add
     * @return the tag that is added
     * @throws TagException throws an exception if something goes wrong
     */
    public Tag addTaskTag(final long boardId, final long listId,
                          final long taskId, final Tag tag) throws TagException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/tags/" + boardId + "/" + listId + "/" + taskId + "/add")
                .request()
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tag, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Tag.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TagException("Board not found.");
        } else if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new TagException("You inputted a wrong value");
        } else {
            throw new TagException("An error occurred while adding the tag");
        }
    }

    /**
     * Rename a tag that is located in the board
     * @param boardId ID of the board
     * @param tagId ID of the tag that will be renamed
     * @param newName the new name of the tag
     * @return the renamed tag
     * @throws TagException throws an exception if something goes wrong
     */
    public Tag renameTag(final long boardId, final long tagId,
                         final String newName) throws TagException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/tags/" + boardId + "/" + tagId + "/rename")
                .request()
                .accept(APPLICATION_JSON)
                .put(Entity.entity(newName, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Tag.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TagException("Tag not found.");
        } else if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new TagException("You inputted a wrong value");
        }else {
            throw new TagException("An error occurred while renaming the tag");
        }
    }

    /**
     * Recolor a tag that is located in the board
     * @param boardId ID of the board
     * @param tagId Id of the tag that will be recolored
     * @param newColor the new color of the tag
     * @return the recolored tag
     * @throws TagException throws an exception if something goes wrong
     */
    public Tag recolorTag(final long boardId, final long tagId,
                         final String newColor) throws TagException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/tags/" + boardId + "/" + tagId + "/recolor")
                .request()
                .accept(APPLICATION_JSON)
                .put(Entity.entity(newColor, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Tag.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TagException("Tag not found.");
        } else if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new TagException("You inputted a wrong value");
        }else {
            throw new TagException("An error occurred while recoloring the tag");
        }
    }

    /**
     * Delete a tag from a board
     * @param boardId ID of the board
     * @param tagId ID of the tag that will be deleted
     * @return the deleted tag
     * @throws TagException throws an exception if something goes wrong
     */
    public Tag deleteBoardTag(final long boardId, final long tagId) throws TagException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/tags/" + boardId + "/delete/" + tagId)
                .request()
                .accept(APPLICATION_JSON)
                .delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Tag.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TagException("Tag not found.");
        } else {
            throw new TagException("An error occurred while deleting the tag");
        }
    }

    /**
     * Delete a tag from a task
     * @param boardId ID of the board
     * @param listId ID of the list
     * @param taskId ID of the task
     * @param tagId ID of the tag that wil be deleted
     * @return the deleted tag
     * @throws TagException throws an exception if something goes wrong
     */
    public Tag deleteTaskTag(final long boardId, final long listId,
                             final long taskId, final long tagId) throws TagException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/tags/" + boardId + "/" + listId + "/" + taskId + "/" + tagId)
                .request()
                .accept(APPLICATION_JSON)
                .delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Tag.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TagException("Tag not found.");
        } else {
            throw new TagException("An error occurred while deleting the tag");
        }
    }
}
