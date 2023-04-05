package client.utils;

import client.customExceptions.TagException;
import com.google.inject.Inject;
import commons.Tag;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javafx.util.Pair;

import java.util.*;

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
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/tags/" + boardId + "/tags", RestUtils.Methods.GET, null);
        try {
            return server.getRestUtils().handleResponse(response, new GenericType<List<Tag>>(){},
                    "getBoardTags");
        }
        catch(Exception e){
            throw new TagException(e.getMessage());
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
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/tags/" + boardId + "/" + listId + "/" + taskId + "/tags",
                RestUtils.Methods.GET, null);
        try {
            return server.getRestUtils().handleResponse(response, new GenericType<List<Tag>>(){},
                    "getTaskTags");
        }
        catch(Exception e){
            throw new TagException(e.getMessage());
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
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/tags/" + boardId + "/" + tagId, RestUtils.Methods.GET, null);
        try {
            return server.getRestUtils().handleResponse(response, Tag.class, "getBoardTag");
        }
        catch(Exception e){
            throw new TagException(e.getMessage());
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
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/tags/" + boardId + "/" + listId + "/" + taskId + "/" + tagId,
                RestUtils.Methods.GET, null);
        try {
            return server.getRestUtils().handleResponse(response, Tag.class, "getTaskTag");
        }
        catch(Exception e){
            throw new TagException(e.getMessage());
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
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/tags/" + boardId + "/tag", RestUtils.Methods.POST, tag);

        try {
            return server.getRestUtils().handleResponse(response, Tag.class, "addBoardTag");
        }
        catch(Exception e){
            throw new TagException(e.getMessage());
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
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/tags/" + boardId + "/" + listId + "/" + taskId + "/add",
                RestUtils.Methods.POST, tag);
        try {
            return server.getRestUtils().handleResponse(response, Tag.class, "addTaskTag");
        }
        catch(Exception e){
            throw new TagException(e.getMessage());
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
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/tags/" + boardId + "/" + tagId + "/rename", RestUtils.Methods.PUT,
                newName, new Pair<>("name", newName));
        try {
            return server.getRestUtils().handleResponse(response, Tag.class, "renameTag");
        }
        catch(Exception e){
            throw new TagException(e.getMessage());
        }
    }

    /**
     * Recolor a tag that is located in the board
     * @param boardId ID of the board
     * @param tagId Id of the tag that will be recolored
     * @param backgroundColor the new background color of the tag
     * @param fontColor the new font color of the tag
     * @return the recolored tag
     * @throws TagException throws an exception if something goes wrong
     */
    public Tag recolorTag(final long boardId, final long tagId,
                         final String backgroundColor, final String fontColor) throws TagException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/tags/" + boardId + "/" + tagId + "/recolor", RestUtils.Methods.PUT,
                new Pair<>("backgroundColor", backgroundColor), new Pair<>("fontColor", fontColor));
        try {
            return server.getRestUtils().handleResponse(response, Tag.class, "recolorTag");
        }
        catch(Exception e){
            throw new TagException(e.getMessage());
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
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/tags/" + boardId + "/delete/" + tagId, RestUtils.Methods.DELETE, null);
        try {
            return server.getRestUtils().handleResponse(response, Tag.class, "deleteBoardTag");
        }
        catch(Exception e){
            throw new TagException(e.getMessage());
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
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/tags/" + boardId + "/" + listId + "/" + taskId + "/delete/" + tagId,
                RestUtils.Methods.DELETE, null);
        try {
            return server.getRestUtils().handleResponse(response, Tag.class, "deleteTaskTag");
        }
        catch(Exception e){
            throw new TagException(e.getMessage());
        }
    }
}
