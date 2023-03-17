package client.utils;

import com.google.inject.Inject;
import commons.Task;
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
 * !    add method for getting all tasks
 * !    reformat duplicate code
 * provide error messages on all ResponseEntities such that
 * response.readEntity(String.class) actually outputs something
 */
public class TaskUtils {

    private final ServerUtils server;
    @Inject
    public TaskUtils(final ServerUtils server) {
        this.server = server;
    }

    /**
     * Get a specific task from a specific list.
     *
     * @param boardId  The id of the board.
     * @param taskListId The id of the list.
     * @param taskId  The id of the task.
     * @return The task.
     */
    public Task getTask(final long boardId, final long taskListId, final long taskId) {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId + "/tasklist/" + taskListId + "/task/" + taskId)
                .request()
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Task.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            System.out.println("Task not found.");
        } else {
            System.out.println("An error occurred while fetching the task: "
                    + response.readEntity(String.class));
        }

        return null;
    }

    /**
     * add a task through the api
     * @param boardId the id of the board the task list belongs to
     * @param taskListId the id of the task list the task belongs to
     * @param task the task to add
     * @return the added task
     */
    public Task addTask(final long boardId, final long taskListId, final Task task) {
        String serverAddress = server.getServerAddress();
        Response response =  ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId + "/" + taskListId + "/task")
                .request()
                .accept(APPLICATION_JSON)
                .post(Entity.entity(task, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Task.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            System.out.println("Board or task list not found.");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            System.out.println("Bad request: " + response.readEntity(String.class));
        } else {
            System.out.println("An error occurred while adding the task: "
                    + response.readEntity(String.class));
        }

        return null;
    }

    /**
     * rename a task through the api
     * @param boardId the id of the board the task belongs to
     * @param taskListId the id of the task list the task belongs to
     * @param taskId the id of the task to rename
     * @param newName the new name of the task
     * @return the renamed task
     * */
    public Task renameTask(final long boardId, final long taskListId,
                           final long taskId, final String newName) {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId + "/" + taskListId + "/" + taskId)
                .queryParam("name", newName)
                .request()
                .accept(APPLICATION_JSON)
                .put(Entity.entity(newName, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Task.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            System.out.println("Task not found.");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            System.out.println("Bad request: " + response.readEntity(String.class));
        } else {
            System.out.println("An error occurred while renaming the task: "
                    + response.readEntity(String.class));
        }

        return null;
    }


    /**
     * delete a task
     * @param boardId The id of the board.
     * @param taskListId The id of the list.
     * @param taskId The id of the task.
     * @return The deleted task.
     */
    public Task deleteTask(final long boardId, final long taskListId, final long taskId) {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId + "/  " + taskListId + "/" + taskId)
                .request()
                .accept(APPLICATION_JSON)
                .delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Task.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            System.out.println("Task not found.");
        } else {
            System.out.println("An error occurred while deleting the task: "
                    + response.readEntity(String.class));
        }

        return null;
    }

}
