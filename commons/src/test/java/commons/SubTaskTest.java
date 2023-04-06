package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubTaskTest {

    @Test
    public void checkConstructor() {
        SubTask testSubTask = new SubTask("SubTask1", false);
        assertNotNull(testSubTask);
        assertEquals(testSubTask.getName(), "SubTask1");
    }

    @Test
    public void checkEmptyConstructor() {
        SubTask testSubTask = new SubTask();
        assertNotNull(testSubTask);
        assertEquals(testSubTask.getName(), "");
        assertFalse(testSubTask.isCompleted());
    }

    @Test
    public void testGetters() {
        SubTask testSubTask = new SubTask("SubTask1", true);
        assertEquals(testSubTask.getName(), "SubTask1");
        assertTrue(testSubTask.isCompleted());
    }

    @Test
    public void testSetters() {
        SubTask testSubTask = new SubTask("SubTask1", false);
        testSubTask.setName("SubTask2");
        assertEquals(testSubTask.getName(), "SubTask2");
        testSubTask.setCompleted(true);
        assertTrue(testSubTask.isCompleted());
    }

    @Test
    public void testEquals() {
        SubTask testSubTask = new SubTask("SubTask1", true);
        SubTask testSubTask2 = new SubTask("SubTask1", true);
        SubTask testSubTask3 = new SubTask("SubTask1", false);
        assertEquals(testSubTask2, testSubTask);
        assertNotEquals(testSubTask3, testSubTask);
    }

    @Test
    public void testHashCode() {
        SubTask testSubTask = new SubTask("SubTask1", true);
        SubTask testSubTask2 = new SubTask("SubTask1", true);
        SubTask testSubTask3 = new SubTask("SubTask1", false);
        assertEquals(testSubTask2.hashCode(), testSubTask.hashCode());
        assertNotEquals(testSubTask3.hashCode(), testSubTask.hashCode());
    }

    @Test
    public void testToString() {
        SubTask testSubTask = new SubTask("SubTask1", false);
        assertNotNull(testSubTask.toString());
    }

    @Test
    public void testCloneConstructor() {
        SubTask subTask = new SubTask("SubTask1", true);
        SubTask newSubTask = new SubTask(subTask);
        assertEquals(subTask, newSubTask);
    }
}
