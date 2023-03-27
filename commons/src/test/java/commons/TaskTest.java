package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void checkConstructor() {
        Task testTask = new Task("Task1", "This is task 1");
        assertNotNull(testTask);
        assertEquals(testTask.getName(), "Task1");
    }

    @Test
    public void testGetters() {
        Task testTask = new Task("Task1", "This is task 1");
        assertEquals(testTask.getName(), "Task1");
        assertEquals(testTask.getDescription(), "This is task 1");
    }

    @Test
    public void testSetters() {
        Task testTask = new Task("Task1", "This is task 1");
        testTask.setName("Task2");
        assertEquals(testTask.getName(), "Task2");
        testTask.setDescription("This is not task 1");
        assertEquals(testTask.getDescription(), "This is not task 1");
    }

    @Test
    public void testEquals() {
        Task testTask = new Task("Task1", "This is task 1");
        Task testTask2 = new Task("Task1", "This is task 1");
        Task testTask3 = new Task("Task3", "This is task 1");
        assertEquals(testTask2, testTask);
        assertNotEquals(testTask3, testTask);
    }

    @Test
    public void testHashCode() {
        Task testTask = new Task("Task1", "This is task 1");
        Task testTask2 = new Task("Task1", "This is task 1");
        Task testTask3 = new Task("Task3", "This is task 1");
        assertEquals(testTask.hashCode(), testTask2.hashCode());
        assertNotEquals(testTask.hashCode(), testTask3.hashCode());
    }

    @Test
    public void testToString() {
        Task testTask = new Task("Task1", "This is task 1");
        assertNotNull(testTask.toString());
    }

    @Test
    public void testNoProgress(){
        Task t = new Task("title", "desc");
        assertEquals(-1.0D, t.getProgress());
    }

    @Test
    public void testZeroProgress(){
        Task t = new Task("title", "desc");
        for(int i = 1; i <= 10; i++)
            t.addSubtask(new SubTask("Step " + i, false));
        assertEquals(0.0D, t.getProgress());
    }

    @Test
    public void testHalfProgress(){
        Task t = new Task("title", "desc");
        for(int i = 1; i <= 10; i++)
            t.addSubtask(new SubTask("Step " + i, i%2 == 0));
        assertEquals(0.5D, t.getProgress());
    }
}
