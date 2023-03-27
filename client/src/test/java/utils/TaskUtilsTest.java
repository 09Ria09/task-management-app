package utils;

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

    private TaskUtils utils;

    @BeforeEach
    @Inject
    public void setup(){
        utils = new TaskUtils(new ServerUtils());
    }

    @Test
    public void testNoProgress(){
        Task t = new Task("name", "desc");
        assertEquals(-1.0D, utils.getProgress(t));
    }

    @Test
    public void testZeroProgress(){
        Task t = new Task("name", "desc");
        for(int i = 1; i <= 10; i++)
            t.addSubtask(new SubTask("Step " + i, false));
        assertEquals(0.0D, utils.getProgress(t));
    }

    @Test
    public void testHalfProgress(){
        Task t = new Task("name", "desc");
        for(int i = 1; i <= 10; i++)
            t.addSubtask(new SubTask("Step " + i, i%2 == 0));
        assertEquals(0.5D, utils.getProgress(t));
    }
}
