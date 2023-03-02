package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {

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
        ArrayList<Task> listBeforeAdding = taskList.getTasks();
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
        Task task3 = new Task("task3", "desc3");
        TaskList taskList = new TaskList("List1");
        taskList.addTask(task1);
        taskList.addTask(task2);
//        taskList.addTask(task3);
        System.out.println(taskList.toString());
        assertEquals("Task (" + task1.id + ") : " + task1.getName() + "\nDescription : " + task1.getDescription()
                        + "/n" + "Task (" + task2.id + ") : " + task2.getName() + "\nDescription : " + task2.getDescription()
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
}