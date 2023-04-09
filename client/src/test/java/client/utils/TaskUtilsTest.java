package client.utils;

import client.customExceptions.BoardException;
import client.customExceptions.TaskException;
import client.customExceptions.TaskListException;
import client.mocks.MockRestUtils;
import client.mocks.MockServerUtils;
import client.mocks.ResponseClone;
import client.scenes.CardCtrl;
import client.utils.ServerUtils;
import client.utils.TaskUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.SubTask;
import commons.Task;
import commons.TaskList;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskUtilsTest {

    private TaskUtils taskUtils;
    private MockRestUtils mockRestUtils;
    private MockServerUtils mockServerUtils;
    private BoardUtils boardUtils;
    private TaskListUtils taskListUtils;

    @BeforeEach
    public void setUp() {
        mockRestUtils = new MockRestUtils();
        mockServerUtils = new MockServerUtils();
        mockServerUtils.setMockRestUtils(mockRestUtils);
        mockServerUtils.setServerAddress("http://example.com");
        taskUtils = new TaskUtils(mockServerUtils);
        boardUtils = new BoardUtils(mockServerUtils);
        taskListUtils = new TaskListUtils(mockServerUtils);
    }

    @Test
    public void testNoProgress(){
        Task t = new Task("name", "desc");
        assertEquals(-1.0D, taskUtils.getProgress(t));
    }

    @Test
    public void testZeroProgress(){
        Task t = new Task("name", "desc");
        for(int i = 1; i <= 10; i++)
            t.addSubtask(new SubTask("Step " + i, false));
        assertEquals(0.0D, taskUtils.getProgress(t));
    }

    @Test
    public void testHalfProgress(){
        Task t = new Task("name", "desc");
        for(int i = 1; i <= 10; i++)
            t.addSubtask(new SubTask("Step " + i, i%2 == 0));
        assertEquals(0.5D, taskUtils.getProgress(t));
    }

    @Test
    public void testGetTasks_throwsException() {
        long boardId = 1L;
        long listId = 1L;

        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TaskException.class, () -> {
            taskUtils.getTasks(boardId, listId);
        });
    }

    @Test
    public void testGetTasks_success() throws Exception {
        long boardId = 1L;
        TaskList taskList = new TaskList();
        taskList.id = 1L;
        taskList.setName("Task List 1");

        Task task1 = new Task();
        task1.id = 1L;
        task1.setName("Task 1");

        Task task2 = new Task();
        task2.id = 2L;
        task2.setName("Task 2");

        List<Task> expectedResult = new ArrayList<>();
        expectedResult.add(task1);
        expectedResult.add(task2);

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        List<Task> result = taskUtils.getTasks(boardId, taskList.id);
        assertEquals(expectedResult.size(), result.size());
        for (int i = 0; i < expectedResult.size(); i++) {
            assertEquals(expectedResult.get(i).getId(), result.get(i).getId());
            assertEquals(expectedResult.get(i).getName(), result.get(i).getName());
        }
    }

    @Test
    public void testGetTask_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        Task expectedResult = new Task();
        expectedResult.id = taskId;
        expectedResult.setName("Task 1");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        Task result = taskUtils.getTask(boardId, taskListId, taskId);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testGetTask_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;

        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TaskException.class, () -> {
            taskUtils.getTask(boardId, taskListId, taskId);
        });
    }

    @Test
    public void testAddTask_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        Task expectedResult = new Task();
        expectedResult.id = 1L;
        expectedResult.setName("New Task");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        Task result = taskUtils.addTask(boardId, taskListId, expectedResult);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testAddTask_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;
        Task task = new Task();
        task.setName("Error Task");

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TaskException.class, () -> {
            taskUtils.addTask(boardId, taskListId, task);
        });
    }

    @Test
    public void testRenameTask_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        Task expectedResult = new Task();
        expectedResult.id = taskId;
        expectedResult.setName("Renamed Task");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        Task result = taskUtils.renameTask(boardId, taskListId, taskId, "Renamed Task");
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testRenameTask_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TaskException.class, () -> {
            taskUtils.renameTask(boardId, taskListId, taskId, "New Name");
        });
    }

    @Test
    public void testDeleteTask_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        Task expectedResult = new Task();
        expectedResult.id = taskId;
        expectedResult.setName("Task 1");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        Task result = taskUtils.deleteTask(boardId, taskListId, taskId);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testDeleteTask_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TaskException.class, () -> {
            taskUtils.deleteTask(boardId, taskListId, taskId);
        });
    }

    @Test
    public void testEditDescription_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        Task expectedResult = new Task();
        expectedResult.id = taskId;
        expectedResult.setName("Task 1");
        expectedResult.setDescription("Description 1");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        Task result = taskUtils.editDescription(boardId, taskListId, taskId, "Description 1");
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getDescription(), result.getDescription());
    }

    @Test
    public void testEditDescription_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TaskException.class, () -> {
            taskUtils.editDescription(boardId, taskListId, taskId, "New Desc");
        });
    }
}
