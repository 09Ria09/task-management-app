package server;

import commons.Board;
import commons.SubTask;
import commons.Task;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import server.api.TestBoardRepository;
import server.services.BoardService;
import server.services.SubTaskService;
import server.services.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class SubTaskServiceTest {

    private SubTaskService service;
    private BoardService boardService;
    private TaskService taskService;

    @BeforeEach
    public void setup() {
        service = new SubTaskService();
        boardService = new BoardService();
        taskService = new TaskService();
        TestBoardRepository repo = new TestBoardRepository();
        ReflectionTestUtils.setField(service, "boardRepository", repo);
        ReflectionTestUtils.setField(boardService, "boardRepository", repo);
        ReflectionTestUtils.setField(taskService, "boardRepository", repo);
    }

    @Test
    public void testGetSubTasks() {
        Board board = new Board("Test Board", new ArrayList<>(), new ArrayList<>());
        boardService.addBoard(board);
        TaskList taskList = new TaskList("Test TaskList", new ArrayList<>());
        Task task = new Task("Test Task", "description", new ArrayList<>());
        SubTask subTask1 = new SubTask("Test SubTask 1", false);
        SubTask subTask2 = new SubTask("Test SubTask 2", false);
        task.addSubtask(subTask1);
        task.addSubtask(subTask2);
        taskList.addTask(task);
        board.addTaskList(taskList);
        boardService.addBoard(board);

        List<SubTask> subTasks = service.getSubTasks(board.getId(), taskList.getId(), task.getId());
        assertNotNull(subTasks);
        assertEquals(2, subTasks.size());
        assertEquals(subTask1.id, subTasks.get(0).id);
        assertEquals(subTask2.id, subTasks.get(1).id);
    }

    @Test
    public void testGetSubTask() {
        Board board = new Board("Test Board", new ArrayList<>(), new ArrayList<>());
        TaskList taskList = new TaskList("Test TaskList", new ArrayList<>());
        Task task = new Task("Test Task", "description", new ArrayList<>());
        SubTask subTask = new SubTask("Test SubTask", false);
        task.addSubtask(subTask);
        taskList.addTask(task);
        board.addTaskList(taskList);
        boardService.addBoard(board);

        SubTask retrievedSubTask = service.getSubTask(board.getId(), taskList.getId(), task.getId(),
            subTask.id);
        assertNotNull(retrievedSubTask);
        assertEquals(subTask.id, retrievedSubTask.id);
        assertEquals(subTask.getName(), retrievedSubTask.getName());
    }

    @Test
    public void testAddSubTask() {
        Board board = new Board("Test Board", new ArrayList<>(), new ArrayList<>());
        boardService.addBoard(board);
        TaskList taskList = new TaskList("Test TaskList", new ArrayList<>());
        Task task = new Task("Test Task", "description", new ArrayList<>());
        SubTask subTask1 = new SubTask("Test SubTask 1", false);
        SubTask subTask2 = new SubTask("Test SubTask 2", false);
        task.addSubtask(subTask1);
        taskList.addTask(task);
        board.addTaskList(taskList);
        boardService.addBoard(board);

        assertEquals(1, taskService.getTask(board.id, taskList.id, task.id).getSubtasks().size());
        service.addSubTask(board.id, taskList.id, task.id, subTask2);
        assertEquals(2, taskService.getTask(board.id, taskList.id, task.id).getSubtasks().size());
    }

    @Test
    public void testRenameSubTask() {
        Board board = new Board("Test Board", new ArrayList<>(), new ArrayList<>());
        TaskList taskList = new TaskList("Test TaskList", new ArrayList<>());
        Task task = new Task("Test Task", "description", new ArrayList<>());
        SubTask subTask = new SubTask("Test SubTask", false);
        task.addSubtask(subTask);
        taskList.addTask(task);
        board.addTaskList(taskList);
        boardService.addBoard(board);

        service.renameSubTask(board.id, taskList.id, task.id, subTask.id, "NewName");
        assertEquals("NewName", service.getSubTask(board.id, taskList.id, task.id, subTask.id).getName());
    }

    @Test
    public void testCompleteSubTask() {
        Board board = new Board("Test Board", new ArrayList<>(), new ArrayList<>());
        TaskList taskList = new TaskList("Test TaskList", new ArrayList<>());
        Task task = new Task("Test Task", "description", new ArrayList<>());
        SubTask subTask = new SubTask("Test SubTask", false);
        task.addSubtask(subTask);
        taskList.addTask(task);
        board.addTaskList(taskList);
        boardService.addBoard(board);

        service.completeSubTask(board.id, taskList.id, task.id, subTask.id, true);
        assertTrue(service.getSubTask(board.id, taskList.id, task.id, subTask.id).isCompleted());
    }

    @Test
    public void testRemoveSubTask() {
        Board board = new Board("Test Board", new ArrayList<>(), new ArrayList<>());
        boardService.addBoard(board);
        TaskList taskList = new TaskList("Test TaskList", new ArrayList<>());
        Task task = new Task("Test Task", "description", new ArrayList<>());
        SubTask subTask1 = new SubTask("Test SubTask 1", false);
        SubTask subTask2 = new SubTask("Test SubTask 2", false);
        task.addSubtask(subTask1);
        task.addSubtask(subTask2);
        taskList.addTask(task);
        board.addTaskList(taskList);
        boardService.addBoard(board);

        assertEquals(2, taskService.getTask(board.id, taskList.id, task.id).getSubtasks().size());
        SubTask removedSubTask = service.removeSubTask(board.id, taskList.id, task.id, subTask1);
        assertEquals(1, taskService.getTask(board.id, taskList.id, task.id).getSubtasks().size());
        assertFalse(taskService.getTask(board.id, taskList.id, task.id).getSubtasks().contains(removedSubTask));
    }

    @Test
    public void testRemoveSubTaskByID() {
        Board board = new Board("Test Board", new ArrayList<>(), new ArrayList<>());
        boardService.addBoard(board);
        TaskList taskList = new TaskList("Test TaskList", new ArrayList<>());
        Task task = new Task("Test Task", "description", new ArrayList<>());
        SubTask subTask1 = new SubTask("Test SubTask 1", false);
        SubTask subTask2 = new SubTask("Test SubTask 2", false);
        task.addSubtask(subTask1);
        task.addSubtask(subTask2);
        taskList.addTask(task);
        board.addTaskList(taskList);
        boardService.addBoard(board);

        assertEquals(2, taskService.getTask(board.id, taskList.id, task.id).getSubtasks().size());
        SubTask removedSubTask = service.removeSubTaskById(board.id, taskList.id, task.id, subTask1.id);
        assertEquals(1, taskService.getTask(board.id, taskList.id, task.id).getSubtasks().size());
        assertFalse(taskService.getTask(board.id, taskList.id, task.id).getSubtasks().contains(removedSubTask));
    }

    @Test
    public void testReorderSubTask() {
        Board board = new Board("Test Board", new ArrayList<>(), new ArrayList<>());
        boardService.addBoard(board);
        TaskList taskList = new TaskList("Test TaskList", new ArrayList<>());
        Task task = new Task("Test Task", "description", new ArrayList<>());
        SubTask subTask1 = new SubTask("Test SubTask 1", false);
        SubTask subTask2 = new SubTask("Test SubTask 2", false);
        SubTask subTask3 = new SubTask("Test SubTask 3", false);
        task.addSubtask(subTask1);
        task.addSubtask(subTask2);
        task.addSubtask(subTask3);
        taskList.addTask(task);
        board.addTaskList(taskList);
        boardService.addBoard(board);

        service.reorderSubTask(board.id, taskList.id, task.id, subTask1.id, 2);
        assertEquals(subTask1, service.getSubTasks(board.id, taskList.id, task.id).get(2));
    }

    @Test
    public void testReorderSubTaskException() {
        Board board = new Board("Test Board", new ArrayList<>(), new ArrayList<>());
        TaskList taskList = new TaskList("Test TaskList", new ArrayList<>());
        Task task = new Task("Test Task", "description", new ArrayList<>());
        SubTask subTask = new SubTask("Test SubTask", false);
        task.addSubtask(subTask);
        taskList.addTask(task);
        board.addTaskList(taskList);
        boardService.addBoard(board);

        assertThrows(NoSuchElementException.class, () -> {
            service.reorderSubTask(board.id, taskList.id, 2424424, 34985853, 0);
        });
    }

}