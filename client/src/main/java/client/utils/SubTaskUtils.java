package client.utils;

import client.customExceptions.TaskException;
import client.customExceptions.SubTaskException;
import com.google.inject.Inject;
import commons.SubTask;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javafx.util.Pair;

import java.util.List;

public class SubTaskUtils {

    private final ServerUtils server;

    @Inject
    public SubTaskUtils(final ServerUtils server) {
        this.server = server;
    }

    public SubTask addSubTask(final long boardId, final long taskListId,
                              final long taskId, final SubTask subTask)
            throws TaskException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/subtasks/" + boardId + "/" + taskListId + "/" + taskId + "/subtask",
                RestUtils.Methods.POST, subTask);
        try {
            return server.getRestUtils().handleResponse(response, SubTask.class, "addSubTask");
        }
        catch(Exception e){
            throw new TaskException(e.getMessage());
        }
    }

    public SubTask getSubTask(final long boardId, final long taskListId,
                              final long taskId, final long subTaskId) throws SubTaskException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/subtasks/" + boardId + "/tasklist/" + taskListId +
                        "/tasks/" + taskId + "/subtasks/" + subTaskId,
                RestUtils.Methods.GET, null);
        try {
            return server.getRestUtils().handleResponse(response, SubTask.class, "getSubTask");
        }
        catch(Exception e){
            throw new SubTaskException(e.getMessage());
        }

    }

    public List<SubTask> getSubTasks(final long boardId, final long taskListId, final long taskId)
            throws SubTaskException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/subtasks/" + boardId + "/tasklist/" + taskListId +
                "/tasks/" + taskId + "/subtasks", RestUtils.Methods.GET, null);
        try {
            return server.getRestUtils().handleResponse(response,
                    new GenericType<List<SubTask>>(){}, "getSubTasks");
        }
        catch(Exception e){
            throw new SubTaskException(e.getMessage());
        }
    }

    public SubTask renameSubTask(final long boardId, final long taskListId,
                                 final long taskId, final long subTaskId,
                                 final String newName)
            throws SubTaskException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/subtasks/" + boardId + "/" + taskListId + "/" + taskId + "/" + subTaskId,
                RestUtils.Methods.PUT, newName, new Pair<>("name", newName));
        try {
            return server.getRestUtils().handleResponse(response, SubTask.class, "renameSubTask");
        }
        catch(Exception e){
            throw new SubTaskException(e.getMessage());
        }
    }

    public SubTask completeSubTask(final long boardId, final long taskListId,
                                 final long taskId, final long subTaskId,
                                 final boolean isComplete)
            throws SubTaskException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/subtasks/" + boardId + "/" + taskListId + "/" + taskId + "/"
                        + subTaskId + "/complete", RestUtils.Methods.PUT, isComplete,
                new Pair<>("complete", isComplete));
        try {
            return server.getRestUtils().handleResponse(response, SubTask.class, "completeSubTask");
        }
        catch(Exception e){
            throw new SubTaskException(e.getMessage());
        }
    }

    public SubTask deleteSubTask(final long boardId, final long taskListId,
                                 final long taskId, final long subTaskId)
            throws SubTaskException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/subtasks/" + boardId + "/" + taskListId
                        + "/" + taskId + "/" + subTaskId, RestUtils.Methods.DELETE, null);
        try {
            return server.getRestUtils().handleResponse(response, SubTask.class, "deleteSubTask");
        }
        catch(Exception e){
            throw new SubTaskException(e.getMessage());
        }
    }

    public SubTask reorderSubTask(final long boardId, final long taskListId,
                                  final long taskId, final long subTaskId,
                                  final int newIndex) throws SubTaskException {
        Response response = server.getRestUtils().sendRequest(server.getServerAddress(),
                "api/subtasks/" + boardId + "/" + taskListId + "/" +
                        taskId + "/reorder/" + subTaskId, RestUtils.Methods.PUT,
                newIndex, new Pair<>("newIndex", newIndex));
        try {
            return server.getRestUtils().handleResponse(response, SubTask.class, "reorderSubTask");
        }
        catch(Exception e){
            throw new SubTaskException(e.getMessage());
        }
    }
}
