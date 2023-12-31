package server.services;

import commons.Board;
import commons.Task;
import commons.TaskList;
import commons.TaskPreset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import server.api.TestBoardRepository;
import server.services.BoardService;
import server.services.ListService;
import server.services.TaskService;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {
    private BoardService boardService;
    private ListService listService;
    private TaskService service;

    @BeforeEach
    public void setup(){
        boardService = new BoardService();
        listService = new ListService();
        service = new TaskService();
        TestBoardRepository repo = new TestBoardRepository();
        ReflectionTestUtils.setField(service, "boardRepository", repo);
        ReflectionTestUtils.setField(boardService, "boardRepository", repo);
        ReflectionTestUtils.setField(listService, "boardRepository", repo);
    }

    @Test
    public void testGetTasks(){
        Board b = new Board();
        TaskList l = new TaskList("list1");
        Task t = new Task("a", "b");
        boardService.addBoard(b);
        listService.addList(b.id, l);
        assertEquals(0, service.getTasks(b.id, l.id).size());
        service.addTask(b.id, l.id, t);
        assertTrue(service.getTasks(b.id, l.id).contains(t));
    }

    @Test
    public void testGetTask(){
        Board b = new Board();
        TaskList l = new TaskList("list1");
        Task t = new Task("a", "b");
        boardService.addBoard(b);
        listService.addList(b.id, l);
        service.addTask(b.id, l.id, t);
        assertEquals(t, service.getTask(b.id, l.id, t.id));
    }

    @Test
    public void testRenameTask(){
        Board b = new Board();
        TaskList l = new TaskList("list1");
        Task t = new Task("a", "b");
        boardService.addBoard(b);
        listService.addList(b.id, l);
        service.addTask(b.id, l.id, t);
        assertEquals("a", service.getTask(b.id, l.id, t.id).getName());
        service.renameTask(b.id, l.id, t.id, "task-a");
        assertEquals("task-a", service.getTask(b.id, l.id, t.id).getName());
    }

    @Test
    public void testRemoveTask(){
        Board b = new Board();
        TaskList l = new TaskList("list1");
        Task t = new Task("a", "b");
        boardService.addBoard(b);
        listService.addList(b.id, l);
        service.addTask(b.id, l.id, t);
        assertEquals(t, service.getTask(b.id, l.id, t.id));
        service.removeTask(b.id, l.id, t);
        assertThrows(NoSuchElementException.class, () -> service.getTask(b.id, l.id, t.id));
    }

    @Test
    public void testEditDescriptionTask(){
        Board b = new Board();
        TaskList l = new TaskList("list1");
        Task t = new Task("a", "b");
        boardService.addBoard(b);
        listService.addList(b.id, l);
        service.addTask(b.id, l.id, t);
        assertEquals("b", service.getTask(b.id, l.id, t.id).getDescription());
        service.editDescription(b.id, l.id, t.id, "task-b");
        assertEquals("task-b", service.getTask(b.id, l.id, t.id).getDescription());
    }

    @Test
    public void testRemoveTaskById(){
        Board b = new Board();
        TaskList l = new TaskList("list1");
        Task t = new Task("a", "b");
        boardService.addBoard(b);
        listService.addList(b.id, l);
        service.addTask(b.id, l.id, t);
        assertEquals(t, service.getTask(b.id, l.id, t.id));
        service.removeTaskById(b.id, l.id, t.id);
        assertThrows(NoSuchElementException.class, () -> service.getTask(b.id, l.id, t.id));
    }

    @Test
    public void testSetPresetTask(){
        Board b = new Board();
        TaskList l = new TaskList("list1");
        Task t = new Task("a", "b");
        boardService.addBoard(b);
        listService.addList(b.id, l);
        service.addTask(b.id, l.id, t);
        TaskPreset preset = new TaskPreset("WAW");
        service.setPreset(b.id, l.id, t.id, preset);
        assertEquals(preset, service.getTask(b.id, l.id, t.id).getTaskPreset());
    }
}
