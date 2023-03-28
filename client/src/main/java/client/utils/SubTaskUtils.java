package client.utils;

import client.customExceptions.TaskException;
import com.google.inject.Inject;
import commons.SubTask;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class SubTaskUtils {

    private final ServerUtils server;

    @Inject
    public SubTaskUtils(final ServerUtils server) {
        this.server = server;
    }

    public SubTask addSubTask(final long boardId, final long taskListId, final long taskId, final SubTask subTask)
            throws TaskException {
        String serverAddress = server.getServerAddress();
        Response response =  ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/subtasks/" + boardId + "/" + taskListId + "/" + taskId + "/subtask")
                .request()
                .accept(APPLICATION_JSON)
                .post(Entity.entity(subTask, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(SubTask.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TaskException("Board, task list or task not found.");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new TaskException("You inputted a wrong value");
        } else {
            throw new TaskException("An error occurred while adding the task");
        }
    }

}
