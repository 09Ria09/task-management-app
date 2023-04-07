package client.utils;

import client.customExceptions.TaskException;
import client.mocks.MockRestUtils;
import client.mocks.MockServerUtils;
import client.scenes.CardCtrl;
import client.utils.ServerUtils;
import client.utils.TaskUtils;
import com.google.inject.Inject;
import commons.SubTask;
import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskUtilsTest {

    private TaskUtils taskUtils;
    private MockRestUtils mockRestUtils;
    private MockServerUtils mockServerUtils;

    @BeforeEach
    public void setUp() {
        mockRestUtils = new MockRestUtils();
        mockServerUtils = new MockServerUtils();
        mockServerUtils.setMockRestUtils(mockRestUtils);
        mockServerUtils.setServerAddress("http://example.com");
        taskUtils = new TaskUtils(mockServerUtils);
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
        try {
            taskUtils.addTask(1, 1, t);
            assertEquals(taskUtils.getTask(1, 1, t.getId()), t);
        } catch (TaskException e) {
            throw new RuntimeException(e);
        }
    }
}
