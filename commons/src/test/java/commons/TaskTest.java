package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void checkConstructor() {
        Task testTask = new Task("Task1", "This is task 1");
        Task testTask2 = new Task();
        Task testTask3 = new Task("MOUAHAHA", "Random", new ArrayList<>());
        Task testTask4 = new Task("Task4", "Desc", new ArrayList<>(), new ArrayList<>());
        assertNotNull(testTask);
        assertNotNull(testTask2);
        assertNotNull(testTask3);
        assertNotNull(testTask4);
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
    public void testGetSubtasks(){
        Task testTask = new Task("Task1", "This is task 1", new ArrayList<>());
        testTask.addSubtask(new SubTask("1", false));
        testTask.addSubtask(new SubTask("2", false));
        assertEquals(2, testTask.getSubtasks().size());
    }

    @Test
    public void testRemoveSubtask(){
        Task testTask = new Task("Task1", "This is task 1", new ArrayList<>(List.of(new SubTask("AZZ", false),
                new SubTask("AZ", false),
                new SubTask("A", false))));
        assertEquals(3, testTask.getSubtasks().size());
        testTask.removeSubtask(testTask.getSubtasks().get(0));
        testTask.removeSubtask(testTask.getSubtasks().get(0));
        assertEquals(1, testTask.getSubtasks().size());
    }

    @Test
    public void testGetSubtaskById(){
        SubTask s1 = new SubTask("AZZ", false);
        s1.id = 1000;
        Task testTask = new Task("Task1", "This is task 1", new ArrayList<>(List.of(s1,
                new SubTask("AZ", false),
                new SubTask("A", false))));
        assertTrue(testTask.getSubTaskById(100).isEmpty());
        assertTrue(testTask.getSubTaskById(1000).isPresent());
        assertEquals(s1, testTask.getSubTaskById(1000).get());
    }

    @Test
    public void testReorderSubTask() {
        SubTask subTask1 = new SubTask("Name1", false);
        subTask1.setId(1);
        SubTask subTask2 = new SubTask("Name2", false);
        subTask2.setId(2);
        SubTask subTask3 = new SubTask("Name3", false);
        subTask3.setId(3);
        Task testTask = new Task("Task", "Desc", new ArrayList<>());
        testTask.addSubtask(subTask1);
        testTask.addSubtask(subTask2);
        testTask.addSubtask(subTask3);
        testTask.reorderSubTasks(subTask3.getId(), 0);
        assertFalse(testTask.reorderSubTasks(10, 0));
        assertFalse(testTask.reorderSubTasks(subTask3.getId(), 0));
        assertEquals(testTask.getSubtasks().get(0), subTask3);
    }

    @Test
    public void testGetSubTaskByID() {
        SubTask subTask1 = new SubTask("Name1", false);
        subTask1.setId(1);
        SubTask subTask2 = new SubTask("Name2", false);
        subTask2.setId(2);
        SubTask subTask3 = new SubTask("Name3", false);
        subTask3.setId(3);
        Task testTask = new Task("Task", "Desc", new ArrayList<>());
        testTask.addSubtask(subTask1);
        testTask.addSubtask(subTask2);
        testTask.addSubtask(subTask3);
        assertTrue(testTask.getSubTaskById(subTask3.id).isPresent());
        assertEquals(testTask.getSubTaskById(subTask3.id).get(), subTask3);
        assertEquals(testTask.getSubtaskById(subTask3.id), subTask3);
    }

    @Test
    public void testAddRemoveTags() {
        Tag tag1 = new Tag("Name1", "FFFFFF");
        Tag tag2 = new Tag("Name2", "FFFFFF");
        Task testTask = new Task("Task", "Desc", new ArrayList<>(), new ArrayList<>());
        testTask.addTag(tag1);
        testTask.addTag(tag2);
        assertEquals(testTask.getTags().size(), 2);
        testTask.removeTag(tag1);
        assertEquals(testTask.getTags().size(), 1);
    }

    @Test
    public void testGetTagByID() {
        Tag tag1 = new Tag("Name1", "FFFFFF");
        Tag tag2 = new Tag("Name2", "FFFFFF");
        tag1.id = 1000;
        Task testTask = new Task("Task", "Desc", new ArrayList<>(), new ArrayList<>());
        testTask.addTag(tag1);
        testTask.addTag(tag2);
        assertTrue(testTask.getTagById(1000).isPresent());
        assertEquals(testTask.getTagById(1000).get(), tag1);
    }

}
