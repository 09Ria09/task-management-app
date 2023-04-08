package client.utils;

import client.customExceptions.SubTaskException;
import client.customExceptions.TaskException;
import client.mocks.MockRestUtils;
import client.mocks.MockServerUtils;
import client.mocks.ResponseClone;
import commons.SubTask;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskUtilsTest {
    private SubTaskUtils subTaskUtils;
    private MockRestUtils mockRestUtils;
    private MockServerUtils mockServerUtils;

    @BeforeEach
    public void setUp() {
        mockRestUtils = new MockRestUtils();
        mockServerUtils = new MockServerUtils();
        mockServerUtils.setMockRestUtils(mockRestUtils);
        mockServerUtils.setServerAddress("http://iwonderifIamallowedtoputyoutubehere.com");
        subTaskUtils = new SubTaskUtils(mockServerUtils);
    }

    @Test
    public void testAddSubTask_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        SubTask inputSubTask = new SubTask();
        inputSubTask.setName("I really like pizza with tuna");
        SubTask expectedResult = new SubTask();
        expectedResult.setId(1L);
        expectedResult.setName("And you dare judge my preference?");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        SubTask result = subTaskUtils.addSubTask(boardId, taskListId, taskId, inputSubTask);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testAddSubTask_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        SubTask inputSubTask = new SubTask();
        inputSubTask.setName("What do you mean?");
        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(TaskException.class, () -> {
            subTaskUtils.addSubTask(boardId, taskListId, taskId, inputSubTask);
        });
    }
    @Test
    public void testGetSubTask_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        long subTaskId = 1L;
        SubTask expectedResult = new SubTask();
        expectedResult.setId(subTaskId);
        expectedResult.setName("Tuna pizza is horrible");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        SubTask result = subTaskUtils.getSubTask(boardId, taskListId, taskId, subTaskId);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testGetSubTask_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        long subTaskId = 1L;

        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(SubTaskException.class, () -> {
            subTaskUtils.getSubTask(boardId, taskListId, taskId, subTaskId);
        });
    }
    @Test
    public void testGetSubTasks_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        SubTask subTask1 = new SubTask();
        subTask1.setId(1L);
        subTask1.setName("FINE. Let's talk about music since we cant agree on pizza");
        SubTask subTask2 = new SubTask();
        subTask2.setId(2L);
        subTask2.setName("Sure, what music do you like listening to?");
        List<SubTask> expectedResult = new ArrayList<>();
        expectedResult.add(subTask1);
        expectedResult.add(subTask2);

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        List<SubTask> result = subTaskUtils.getSubTasks(boardId, taskListId, taskId);
        assertEquals(expectedResult.size(), result.size());
        for (int i = 0; i < expectedResult.size(); i++) {
            assertEquals(expectedResult.get(i).getId(), result.get(i).getId());
            assertEquals(expectedResult.get(i).getName(), result.get(i).getName());
        }
    }

    @Test
    public void testGetSubTasks_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;

        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(SubTaskException.class, () -> {
            subTaskUtils.getSubTasks(boardId, taskListId, taskId);
        });
    }

    @Test
    public void testRenameSubTask_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        long subTaskId = 1L;
        String newName = "I love listening to Nintendocore";
        SubTask expectedResult = new SubTask();
        expectedResult.setId(subTaskId);
        expectedResult.setName(newName);

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        SubTask result = subTaskUtils.renameSubTask(boardId, taskListId, taskId, subTaskId, newName);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testRenameSubTask_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        long subTaskId = 1L;
        String newName = "???? what even is that?";

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(SubTaskException.class, () -> {
            subTaskUtils.renameSubTask(boardId, taskListId, taskId, subTaskId, newName);
        });
    }

    @Test
    public void testCompleteSubTask_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        long subTaskId = 1L;
        SubTask expectedResult = new SubTask();
        expectedResult.setId(subTaskId);
        expectedResult.setName("It's like a mix of metal and video game music");
        expectedResult.setCompleted(true);

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        SubTask result = subTaskUtils.completeSubTask(boardId, taskListId, taskId, subTaskId, true);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
        assertEquals(expectedResult.isCompleted(), result.isCompleted());
    }

    @Test
    public void testCompleteSubTask_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        long subTaskId = 1L;

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(SubTaskException.class, () -> {
            subTaskUtils.completeSubTask(boardId, taskListId, taskId, subTaskId, false);
        });
    }

    @Test
    public void testDeleteSubTask_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        long subTaskId = 1L;
        SubTask expectedResult = new SubTask();
        expectedResult.setId(subTaskId);
        expectedResult.setName("OK I guess. I like listening to folkloric romanian music");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        SubTask result = subTaskUtils.deleteSubTask(boardId, taskListId, taskId, subTaskId);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testDeleteSubTask_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        long subTaskId = 1L;

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(SubTaskException.class, () -> {
            subTaskUtils.deleteSubTask(boardId, taskListId, taskId, subTaskId);
        });
    }

    @Test
    public void testReorderSubTask_success() throws Exception {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        long subTaskId = 1L;
        int newIndex = 2;
        SubTask expectedResult = new SubTask();
        expectedResult.setId(subTaskId);
        expectedResult.setName("Oh! I actually really like <As muri dar nu acuma>");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        SubTask result = subTaskUtils.reorderSubTask(boardId, taskListId, taskId, subTaskId, newIndex);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testReorderSubTask_throwsException() {
        long boardId = 1L;
        long taskListId = 1L;
        long taskId = 1L;
        long subTaskId = 1L;
        int newIndex = 2;

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(SubTaskException.class, () -> {
            subTaskUtils.reorderSubTask(boardId, taskListId, taskId, subTaskId, newIndex);
        });
    }
}