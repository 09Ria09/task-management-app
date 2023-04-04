package client.utils;

import client.customExceptions.TaskException;
import com.google.inject.Inject;
import commons.SubTask;
import commons.Task;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javafx.util.Pair;

import java.util.List;

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
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/boards/" + boardId + "/tasklist" + taskListId + "/tasks",
                RestUtils.Methods.GET, null);
        try {
            return server.getRestUtils().handleResponse(response,
                    new GenericType<List<Task>>(){}, "getTasks");
        }
        catch(Exception e){
            throw new TaskException(e.getMessage());
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
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/tasks/" + boardId + "/tasklist/" + taskListId + "/task/" + taskId,
                RestUtils.Methods.GET, null);
        try {
            return server.getRestUtils().handleResponse(response, Task.class, "getTask");
        }
        catch(Exception e){
            throw new TaskException(e.getMessage());
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
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/tasks/" + boardId + "/" + taskListId + "/task",
                RestUtils.Methods.POST, task);
        try {
            return server.getRestUtils().handleResponse(response, Task.class, "addTask");
        }
        catch(Exception e){
            throw new TaskException(e.getMessage());
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
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/tasks/" + boardId + "/" + taskListId + "/" + taskId,
                RestUtils.Methods.PUT, newName,
                new Pair<>("name", newName));
        try {
            return server.getRestUtils().handleResponse(response, Task.class, "renameTask");
        }
        catch(Exception e){
            throw new TaskException(e.getMessage());
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

        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/tasks/" + boardId + "/  " + taskListId + "/" + taskId,
                RestUtils.Methods.DELETE, null);
        try {
            return server.getRestUtils().handleResponse(response, Task.class, "deleteTask");
        }
        catch(Exception e){
            throw new TaskException(e.getMessage());
        }
    }

    public Task editDescription(final long boardId, final long taskListId,
                                  final long taskId,
                                  final String newDescription) throws TaskException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/tasks/" + boardId + "/" + taskListId + "/desc/" + taskId,
                RestUtils.Methods.PUT, newDescription,
                new Pair<>("description", newDescription));
        try {
            return server.getRestUtils().handleResponse(response, Task.class,
                    "editTaskDescription");
        }
        catch(Exception e){
            throw new TaskException(e.getMessage());
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
