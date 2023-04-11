package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {

    @Test
    public void testConstructors(){
        TaskList list1 = new TaskList();
        assertNotNull(list1);
        TaskList list2 = new TaskList("GGGGGGGGG");
        assertNotNull(list2);
        TaskList list3 = new TaskList("A", new ArrayList<>());
        assertNotNull(list3);
    }

    @Test
    void getName() {
        TaskList taskList = new TaskList("List1");
        assertEquals("List1", taskList.getName());
    }

    @Test
    void setName() {
        TaskList taskList = new TaskList("List1");
        taskList.setName("New List1");
        assertEquals("New List1", taskList.getName());
    }

    @Test
    void getTasks() {
        Task task1 = new Task("task1", "desc1");
        Task task2 = new Task("task2", "desc2");
        ArrayList<Task> taskList1 = new ArrayList<>();
        taskList1.add(task1);
        taskList1.add(task2);
        TaskList taskList2 = new TaskList("TaskList");
        taskList2.addTask(task1);
        taskList2.addTask(task2);
        assertEquals(taskList1, taskList2.getTasks());
    }

    @Test
    void addTask() {
        Task task1 = new Task("task1", "desc1");
        Task task2 = new Task("task2", "desc2");
        TaskList taskList = new TaskList("List1");
        List<Task> listBeforeAdding = taskList.getTasks();
        taskList.addTask(task1);
        taskList.addTask(task2);
        assertEquals(2, taskList.getTasks().size());


    }

    @Test
    void removeTask() {
        Task task1 = new Task("task1", "desc1");
        Task task2 = new Task("task2", "desc2");
        TaskList taskList = new TaskList("List1");
        taskList.addTask(task1);
        taskList.addTask(task2);
        taskList.removeTask(task1);
        assertEquals(task2, taskList.getTasks().get(0));
    }

    @Test
    void testToString() {
        Task task1 = new Task("task1", "desc1");
        Task task2 = new Task("task2", "desc2");
        TaskList taskList = new TaskList("List1");
        taskList.addTask(task1);
        taskList.addTask(task2);

        assertEquals("TaskList (" + taskList.id + ") : " + taskList.getName() +
                        "\n" + task1.toString() + "\n" + task2.toString() + "\n"
                , taskList.toString());
    }

    @Test
    void testEquals() {
        Task task1 = new Task("task1", "desc1");
        Task task2 = new Task("task2", "desc2");
        TaskList taskList1 = new TaskList("List1");
        taskList1.addTask(task1);
        taskList1.addTask(task2);

        TaskList taskList2 = new TaskList("List1");
        taskList2.addTask(task1);
        taskList2.addTask(task2);
        assertEquals(taskList1, taskList2);
    }

    @Test
    void testHashCode() {
        Task task1 = new Task("task1", "desc1");
        Task task2 = new Task("task2", "desc2");
        TaskList taskList1 = new TaskList("taskList1");
        TaskList taskList2 = new TaskList("taskList1");
        taskList1.addTask(task1);
        taskList1.addTask(task2);
        taskList2.addTask(task1);
        taskList2.addTask(task2);
        assertEquals(taskList1.hashCode(), taskList2.hashCode());
    }

    @Test
    public void testGetTaskById(){
        TaskList list = new TaskList("A");
        Task task1 = new Task("ABBA", "IS GREAT !");
        task1.id = 666;
        List<Task> tasks = new ArrayList<>(List.of(new Task("Voulez-vous", "Waterloo"), task1));
        list.setTasks(tasks);
        assertTrue(list.getTaskById(666).isPresent());
        assertEquals(task1, list.getTaskById(666).get());
    }

    @Test
    public void testGetHighestTaskId(){
        TaskList list = new TaskList("A");
        Task task1 = new Task("I LOVE", "IKEA !");
        Task task2 = new Task("I", "DON'T !");
        task1.id = 666;
        task2.id = 333;
        List<Task> tasks = new ArrayList<>(List.of(task1, task2));
        list.setTasks(tasks);
        assertEquals(666, list.findHighestTaskID());
    }

    @Test
    public void testReorder(){
        TaskList list = new TaskList("A");
        Task task1 = new Task("I LOVE", "IKEA !");
        Task task2 = new Task("I", "DON'T !");
        task1.id = 666;
        task2.id = 333;
        List<Task> tasks = new ArrayList<>(List.of(task1, task2));
        list.setTasks(tasks);
        list.reorder(333, 0);
        assertEquals(task2, list.getTasks().get(0));
    }

    @Test
    public void testNotReorder(){
        TaskList list = new TaskList("A");
        Task task1 = new Task("I LOVE", "IKEA !");
        Task task2 = new Task("I", "DON'T !");
        task1.id = 666;
        task2.id = 333;
        List<Task> tasks = new ArrayList<>(List.of(task1, task2));
        list.setTasks(tasks);
        list.reorder(1, 3);
        list.reorder(333, 1);
        assertEquals(tasks, list.getTasks());
    }
}