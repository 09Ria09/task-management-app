package client.utils;

import com.google.inject.Inject;

import commons.TaskList;
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
 * !    add method for getting all task lists
 * !    reformat duplicate code
 */
public class TaskListUtils {
    private final ServerUtils server;

    @Inject
    public TaskListUtils(final ServerUtils server) {
        this.server = server;
    }

    /**
     *   Get a specific list from a board
     * @param boardId the id of the board the list belongs to
     * @param taskListId the id of the list to get
     * @return the list
     */
    public TaskList getTaskList(final long boardId,final long taskListId) {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId + "/tasklist/" + taskListId)
                .request()
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(TaskList.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            System.out.println("Task list not found.");
        } else {
            System.out.println("An error occurred while fetching the task list: "
                    + response.readEntity(String.class));
        }

        return null;
    }


    public TaskList createTaskList(final long boardId,final TaskList taskList) {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId + "/tasklist")
                .request()
                .accept(APPLICATION_JSON)
                .post(Entity.entity(taskList, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(TaskList.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            System.out.println("Board not found.");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            System.out.println("Bad request: " + response.readEntity(String.class));
        } else {
            System.out.println("An error occurred while creating the task list: "
                    + response.readEntity(String.class));
        }

        return null;
    }
    /**
     * Update a list on the server
     *
     * @param boardId the id of the board the list belongs to
     * @param taskListId  the id of the list to update
     * @param newName the new name of the list
     * @return a response indicating whether the list was updated successfully or not
     */
    public TaskList renameTaskList(final long boardId,final long taskListId,final String newName) {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId + "/" + taskListId)
                .queryParam("name", newName)
                .request()
                .accept(APPLICATION_JSON)
                .put(Entity.entity(newName, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(TaskList.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            System.out.println("Task list not found.");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            System.out.println("Bad request: " + response.readEntity(String.class));
        } else {
            System.out.println("An error occurred while updating the task list: "
                    + response.readEntity(String.class));
        }

        return null;
    }

    public TaskList deleteTaskList(final long boardId,final long taskListId) {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId + "/" + taskListId)
                .request()
                .accept(APPLICATION_JSON)
                .delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(TaskList.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            System.out.println("Task list not found.");
        } else {
            System.out.println("An error occurred while deleting the task list: "
                    + response.readEntity(String.class));
        }

        return null;
    }
}
