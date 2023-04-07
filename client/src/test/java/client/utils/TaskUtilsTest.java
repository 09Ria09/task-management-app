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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void addTaskTest() {
        Task t = new Task("name", "desc");
        Board b = new Board();
        b.setName("name");
        TaskList tl = new TaskList("name");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), b);
        mockRestUtils.setMockResponse(mockResponse);

        try {
            boardUtils.addBoard(b);
        } catch (BoardException e) {
            throw new RuntimeException(e);
        }
        try {
            taskListUtils.createTaskList(b.getId(), tl);
        } catch (BoardException e) {
            throw new RuntimeException(e);
        } catch (TaskListException e) {
            throw new RuntimeException(e);
        }
        try {
            taskUtils.addTask(b.getId(), tl.getId(), t);
            assertEquals(taskUtils.getTask(b.getId(), tl.getId(), t.getId()), t);
        } catch (TaskException e) {
            throw new RuntimeException(e);
        }
    }
}
