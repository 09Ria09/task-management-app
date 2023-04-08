package client.utils;

import client.customExceptions.TaskListException;
import client.mocks.MockRestUtils;
import client.mocks.MockServerUtils;
import client.mocks.ResponseClone;
import commons.TaskList;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class TaskListUtilsTest {
    private TaskListUtils taskListUtils;
    private MockRestUtils mockRestUtils;
    private MockServerUtils mockServerUtils;

    @BeforeEach
    public void setUp() {
        mockRestUtils = new MockRestUtils();
        mockServerUtils = new MockServerUtils();
        mockServerUtils.setMockRestUtils(mockRestUtils);
        mockServerUtils.setServerAddress("http://example.com");
        taskListUtils = new TaskListUtils(mockServerUtils);
    }

    @Test
    public void testGetTaskLists_success() throws Exception {
        long boardId = 1L;
        TaskList taskList1 = new TaskList();
        taskList1.id = 1L;
        taskList1.setName("Task List 1");
        TaskList taskList2 = new TaskList();
        taskList2.id = 2L;
        taskList2.setName("Task List 2");
        List<TaskList> expectedResult = new ArrayList<>();
        expectedResult.add(taskList1);
        expectedResult.add(taskList2);

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        List<TaskList> result = taskListUtils.getTaskLists(boardId);
        assertEquals(expectedResult.size(), result.size());
        for (int i = 0; i < expectedResult.size(); i++) {
            assertEquals(expectedResult.get(i).getId(), result.get(i).getId());
            assertEquals(expectedResult.get(i).getName(), result.get(i).getName());
        }
    }

    @Test
    public void testGetTaskLists_throwsException() {
        long boardId = 1L;

        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TaskListException.class, () -> {
            taskListUtils.getTaskLists(boardId);
        });
    }

    @Test
    public void testGetTaskList_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        TaskList expectedResult = new TaskList();
        expectedResult.id = taskListId;
        expectedResult.setName("Do you like pizza?");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        TaskList result = taskListUtils.getTaskList(boardId, taskListId);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testGetTaskList_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;

        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TaskListException.class, () -> {
            taskListUtils.getTaskList(boardId, taskListId);
        });
    }

    @Test
    public void testCreateTaskList_success() throws Exception {
        long boardId = 1L;
        TaskList inputTaskList = new TaskList();
        inputTaskList.setName("Yes, I do");
        TaskList expectedResult = new TaskList();
        expectedResult.id = 1L;
        expectedResult.setName("Which one is your favorite?");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        TaskList result = taskListUtils.createTaskList(boardId, inputTaskList);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testCreateTaskList_throwsException() {
        long boardId = 1L;
        TaskList inputTaskList = new TaskList();
        inputTaskList.setName("I prefer the one with pineapple");

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TaskListException.class, () -> {
            taskListUtils.createTaskList(boardId, inputTaskList);
        });
    }

    @Test
    public void testRenameTaskList_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        String newName = "I regret having this conversation";
        TaskList expectedResult = new TaskList();
        expectedResult.id = taskListId;
        expectedResult.setName(newName);

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        TaskList result = taskListUtils.renameTaskList(boardId, taskListId, newName);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testRenameTaskList_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;
        String newName = "Why? Everyone has their own preferences";

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TaskListException.class, () -> {
            taskListUtils.renameTaskList(boardId, taskListId, newName);
        });
    }

    @Test
    public void testDeleteTaskList_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        TaskList expectedResult = new TaskList();
        expectedResult.id = taskListId;
        expectedResult.setName("Sure.. but not pineapple");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        TaskList result = taskListUtils.deleteTaskList(boardId, taskListId);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testDeleteTaskList_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TaskListException.class, () -> {
            taskListUtils.deleteTaskList(boardId, taskListId);
        });
    }

    @Test
    public void testReorderTask_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        int newIndex = 2;
        TaskList expectedResult = new TaskList();
        expectedResult.id = taskListId;
        expectedResult.setName("Ok yeah sure, what's your favourite then?");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        TaskList result = taskListUtils.reorderTask(boardId, taskListId, taskId, newIndex);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testReorderTask_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        int newIndex = -1;

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TaskListException.class, () -> {
            taskListUtils.reorderTask(boardId, taskListId, taskId, newIndex);
        });
    }

    @Test
    public void testGetServer() {
        assertEquals(mockServerUtils, taskListUtils.getServer());
    }
}
