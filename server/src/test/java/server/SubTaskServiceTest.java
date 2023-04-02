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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubTaskServiceTest {

    private SubTaskService service;
    private BoardService boardService;

    @BeforeEach
    public void setup() {
        service = new SubTaskService();
        boardService = new BoardService();
        TestBoardRepository repo = new TestBoardRepository();
        ReflectionTestUtils.setField(service, "boardRepository", repo);
        ReflectionTestUtils.setField(boardService, "boardRepository", repo);
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

}