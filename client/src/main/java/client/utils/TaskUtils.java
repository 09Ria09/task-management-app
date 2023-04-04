package client.utils;

import client.customExceptions.TaskException;
import com.google.inject.Inject;
import commons.SubTask;
import commons.Task;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

//TODO
/*
 * !    reformat duplicate code -
 * either use responsehandler or leave as is
 */
public class TaskUtils {

    private final ServerUtils server;
    @Inject
    public TaskUtils(final ServerUtils server) {
        this.server = server;
    }

    public List<Task> getTasks(final long boardId, final long taskListId)
            throws TaskException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/boards/" + boardId + "/tasklist" + taskListId + "/tasks")
                .request()
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<List<Task>>() {});
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TaskException("Tasks not found.");
        } else {
            throw new TaskException("An error occurred while fetching the tasks");
        }
    }
    /**
     * Get a specific task from a specific list.
     *
     * @param boardId  The id of the board.
     * @param taskListId The id of the list.
     * @param taskId  The id of the task.
     * @return The task.
     */
    public Task getTask(final long boardId, final long taskListId, final long taskId)
            throws TaskException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/tasks/" + boardId + "/tasklist/" + taskListId + "/task/" + taskId)
                .request()
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Task.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TaskException("Task not found.");
        } else {
            throw new TaskException("An error occurred while fetching the task");
        }
    }

    /**
     * add a task through the api
     * @param boardId the id of the board the task list belongs to
     * @param taskListId the id of the task list the task belongs to
     * @param task the task to add
     * @return the added task
     */
    public Task addTask(final long boardId, final long taskListId, final Task task)
            throws TaskException {
        String serverAddress = server.getServerAddress();
        Response response =  ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/tasks/" + boardId + "/" + taskListId + "/task")
                .request()
                .accept(APPLICATION_JSON)
                .post(Entity.entity(task, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Task.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TaskException("Board or task list not found.");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new TaskException("You inputted a wrong value");
        } else {
            throw new TaskException("An error occurred while adding the task");
        }
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
                           final long taskId, final String newName)
            throws TaskException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/tasks/" + boardId + "/" + taskListId + "/" + taskId)
                .queryParam("name", newName)
                .request()
                .accept(APPLICATION_JSON)
                .put(Entity.entity(newName, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Task.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TaskException("Task not found.");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new TaskException("You inputted a wrong value");
        } else {
            throw new TaskException("An error occurred while renaming the task");
        }
    }


    /**
     * delete a task
     * @param boardId The id of the board.
     * @param taskListId The id of the list.
     * @param taskId The id of the task.
     * @return The deleted task.
     */
    public Task deleteTask(final long boardId, final long taskListId, final long taskId)
            throws TaskException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/tasks/" + boardId + "/  " + taskListId + "/" + taskId)
                .request()
                .accept(APPLICATION_JSON)
                .delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Task.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TaskException("Task not found.");
        } else {
            throw new TaskException("An error occurred while deleting the task");
        }
    }

    public Task editDescription(final long boardId, final long taskListId,
                                  final long taskId,
                                  final String newDescription) throws TaskException {
        String serverAddress = server.getServerAddress();
        Response response = ClientBuilder.newClient(new ClientConfig()).target(serverAddress)
                .path("api/tasks/" + boardId + "/" + taskListId + "/desc/" + taskId)
                .queryParam("description", newDescription)
                .request()
                .accept(APPLICATION_JSON)
                .put(Entity.entity(newDescription, APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(Task.class);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new TaskException("Task not found.");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new TaskException("You inputted a wrong value");
        } else {
            throw new TaskException("An error occurred while renaming the task");
        }
    }

    /**
     * Computes the progress of a given task. If the task has no subtasks,
     * the progress returned is -1. Else the progress is the amount of completed tasks
     * divided by the total amount of tasks.
     * @param task the task from which the progress is computed
     * @return the progress of the given task (between 0 and 1, or -1)
     */
    public double getProgress(final Task task){
        if(task.getSubtasks().isEmpty())
            return -1.0d;
        else
            return ((double)task.getSubtasks().stream()
                    .filter(SubTask::isCompleted)
                    .count())/((double)task.getSubtasks().size());
    }
}
