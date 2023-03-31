package client.utils;

import client.customExceptions.TaskException;
import client.customExceptions.SubTaskException;
import com.google.inject.Inject;
import commons.SubTask;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class SubTaskUtils {

    private final ServerUtils server;

    @Inject
    public SubTaskUtils(final ServerUtils server) {
        this.server = server;
    }

    public SubTask addSubTask(final long boardId, final long taskListId,
                              final long taskId, final SubTask subTask)
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

    public SubTask getSubTask(final long boardId, final long taskListId,
                              final long taskId, final long subTaskId) throws SubTaskException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/subtasks/" + boardId + "/tasklist/" + taskListId +
                        "/tasks/" + taskId + "/subtasks/" + subTaskId)
                .request()
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(SubTask.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new SubTaskException("Task not found.");
        } else {
            throw new SubTaskException("An error occurred while fetching the task");
        }

    }

    public List<SubTask> getSubTasks(final long boardId, final long taskListId, final long taskId)
            throws SubTaskException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/subtasks/" + boardId + "/tasklist/" + taskListId +
                        "/tasks/" + taskId + "/subtasks")
                .request()
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<List<SubTask>>() {});
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new SubTaskException("Sub tasks not found.");
        } else {
            throw new SubTaskException("An error occurred while fetching the sub tasks");
        }
    }

    public SubTask renameSubTask(final long boardId, final long taskListId,
                                 final long taskId, final long subTaskId,
                                 final String newName)
            throws SubTaskException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/subtasks/" + boardId + "/" + taskListId + "/" + taskId + "/" + subTaskId)
                .queryParam("name", newName)
                .request()
                .accept(APPLICATION_JSON)
                .put(Entity.entity(newName, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(SubTask.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new SubTaskException("Sub task not found.");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new SubTaskException("Bad request");
        } else {
            throw new SubTaskException("An error occurred while renaming the sub task"
                    +response.getStatus());
        }
    }

    public SubTask deleteSubTask(final long boardId, final long taskListId,
                                 final long taskId, final long subTaskId)
            throws SubTaskException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/subtasks/" + boardId + "/" + taskListId
                        + "/" + taskId + "/" + subTaskId)
                .request()
                .accept(APPLICATION_JSON)
                .delete();
        System.out.println(response.getStatus());
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(SubTask.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new SubTaskException("Sub task not found." + response.getStatus());
        } else {
            throw new SubTaskException("An error occurred while deleting the sub task"
                    + response.getStatus());
        }
    }

    public SubTask reorderTask(final long boardId, final long taskListId,
                               final long taskId, final long subTaskId,
                               final int newIndex) throws SubTaskException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/subtasks/" + boardId + "/" + taskListId + "/" +
                        taskId + "/reorder/" + subTaskId)
                .queryParam("newIndex", newIndex)
                .request()
                .accept(APPLICATION_JSON)
                .put(Entity.entity(newIndex, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(SubTask.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new SubTaskException("Task not found.");
        } else {
            throw new SubTaskException("An error occurred while reordering the sub tasks");
        }
    }
}
