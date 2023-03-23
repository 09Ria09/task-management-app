package client.utils;

import client.utils.customExceptions.BoardException;
import client.utils.customExceptions.TaskListException;
import com.google.inject.Inject;
import commons.TaskList;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

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

    /**
     *   Get all lists of a board
     * @param boardId the id of the board
     * @return the lists of the board
     */
    public List<TaskList> getTaskLists(final long boardId)
            throws TaskListException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId + "/tasklists/")
                .request()
                .accept(APPLICATION_JSON) //I think this is where implicit deserialization happens
                .get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<List<TaskList>>() {});
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TaskListException("Task lists not found");
        } else {
            throw new TaskListException("An error occurred while fetching the task lists");
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
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId + "/tasklist/" + taskListId)
                .request()
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(TaskList.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TaskListException("Task list not found.");
        } else {
            throw new TaskListException("An error occurred while fetching the task list");
        }
    }


    public TaskList createTaskList(final long boardId,final TaskList taskList)
            throws BoardException, TaskListException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/lists/" + boardId + "/tasklist")
                .request()
                .accept(APPLICATION_JSON)
                .post(Entity.entity(taskList, APPLICATION_JSON));
        System.out.println(response.getStatus());
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(TaskList.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new BoardException("Board not found.");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new TaskListException("Bad request");
        } else {
            throw new TaskListException("An error occurred while creating the task list");
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
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/lists/" + boardId + "/" + taskListId)
                .queryParam("name", newName)
                .request()
                .accept(APPLICATION_JSON)
                .put(Entity.entity(newName, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(TaskList.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TaskListException("Task list not found.");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new TaskListException("Bad request");
        } else {
            throw new TaskListException("An error occurred while updating the task list");
        }
    }

    public TaskList deleteTaskList(final long boardId,final long taskListId)
            throws TaskListException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/lists/" + boardId + "/" + taskListId)
                .request()
                .accept(APPLICATION_JSON)
                .delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(TaskList.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TaskListException("Task list not found.");
        } else {
            throw new TaskListException("An error occurred while deleting the task list");
        }
    }

    public TaskList reorderTask(final long boardId,final long taskListId,
                                final long taskID, final int newIndex) throws TaskListException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId + "/" + taskListId + "/reorder/" + taskID)
                .queryParam("newIndex", newIndex)
                .request()
                .accept(APPLICATION_JSON)
                .put(Entity.entity(newIndex, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(TaskList.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TaskListException("Task list not found.");
        } else {
            throw new TaskListException("An error occurred while reordering the task list");
        }
    }
}
