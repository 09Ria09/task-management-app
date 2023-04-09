package client.utils;

import client.customExceptions.BoardException;
import client.customExceptions.TaskListException;
import com.google.inject.Inject;
import commons.TaskList;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javafx.util.Pair;

import java.util.List;

//TODO
/*
 *
 * !    reformat duplicate code
 */
public class TaskListUtils {
    private final ServerUtils server;

    @Inject
    public TaskListUtils(final ServerUtils server) {
        this.server = server;
    }

    public ServerUtils getServer() {
        return server;
    }

    /**
     *   Get all lists of a board
     * @param boardId the id of the board
     * @return the lists of the board
     */
    public List<TaskList> getTaskLists(final long boardId)
            throws TaskListException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/lists/" + boardId + "/tasklists/",
                RestUtils.Methods.GET, null);
        try {
            return server.getRestUtils().handleResponse(response,
                    new GenericType<List<TaskList>>(){}, "getTaskLists");
        }
        catch(Exception e){
            throw new TaskListException(e.getMessage());
        }
    }

    /**
     *   Get a specific list from a board
     * @param boardId the id of the board the list belongs to
     * @param taskListId the id of the list to get
     * @return the list
     */
    public TaskList getTaskList(final long boardId,final long taskListId)
            throws TaskListException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/lists/" + boardId + "/tasklist/" + taskListId,
                RestUtils.Methods.GET, null);
        try {
            return server.getRestUtils().handleResponse(response,
                    TaskList.class, "getTaskList");
        }
        catch(Exception e){
            throw new TaskListException(e.getMessage());
        }
    }


    public TaskList createTaskList(final long boardId,final TaskList taskList)
            throws BoardException, TaskListException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/lists/" + boardId + "/tasklist", RestUtils.Methods.POST,
                taskList);
        try {
            return server.getRestUtils().handleResponse(response,
                    TaskList.class, "createTaskList");
        }
        catch(Exception e){
            throw new TaskListException(e.getMessage());
        }
    }
    /**
     * Update a list on the server
     *
     * @param boardId the id of the board the list belongs to
     * @param taskListId  the id of the list to update
     * @param newName the new name of the list
     * @return a response indicating whether the list was updated successfully or not
     */
    public TaskList renameTaskList(final long boardId,final long taskListId,final String newName)
            throws TaskListException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/lists/" + boardId + "/" + taskListId, RestUtils.Methods.PUT,
                newName, new Pair<>("name", newName));
        try {
            return server.getRestUtils().handleResponse(response,
                    TaskList.class, "renameTaskList");
        }
        catch(Exception e){
            throw new TaskListException(e.getMessage());
        }
    }

    public TaskList deleteTaskList(final long boardId,final long taskListId)
            throws TaskListException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/lists/" + boardId + "/" + taskListId, RestUtils.Methods.DELETE, null);
        try {
            return server.getRestUtils().handleResponse(response,
                    TaskList.class, "deleteTaskList");
        }
        catch(Exception e){
            throw new TaskListException(e.getMessage());
        }
    }

    public TaskList reorderTask(final long boardId,final long taskListId,
                                final long taskID, final int newIndex) throws TaskListException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/lists/" + boardId + "/" + taskListId + "/reorder/" + taskID,
                RestUtils.Methods.PUT,
                newIndex,
                new Pair<>("newIndex", newIndex));
        try {
            return server.getRestUtils().handleResponse(response,
                    TaskList.class, "reorderTasks");
        }
        catch(Exception e){
            throw new TaskListException(e.getMessage());
        }
    }
}
