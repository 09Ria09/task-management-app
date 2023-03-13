package server;

import commons.Board;
import commons.Task;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import server.api.TestBoardRepository;
import server.database.BoardRepository;

import static org.junit.jupiter.api.Assertions.*;

public class BoardServiceTest {
    private BoardService service;
    private TestBoardRepository repo;

    @BeforeEach
    public void setup(){
        service = new BoardService();
        repo = new TestBoardRepository();
        ReflectionTestUtils.setField(service, "boardRepository", repo);
    }

    @Test
    public void testGetExistingBoard(){
        Board b = new Board();
        service.addBoard(b);
        assertEquals(b, service.getBoard(b.id));
    }

    @Test
    public void testNonExistingBoard(){
        assertNull(service.getBoard(5L));
    }

    @Test
    public void databaseIsUsed() {
        Board b = new Board();
        b.setName("test");
        service.addBoard(b);
        assertTrue(repo.calledMethods.contains("save"));
    }

    @Test
    public void testRemoveBoard(){
        Board b = new Board();
        service.addBoard(b);
        service.removeBoard(b);
        assertNull(service.getBoard(b.id));
    }

    @Test
    public void testRenameBoard(){
        Board b = new Board();
        b.setName("Board1");
        service.addBoard(b);
        assertEquals("Board1", service.getBoard(b.id).getName());
        service.renameBoard(b.id, "Board2");
        assertEquals("Board2", service.getBoard(b.id).getName());
    }

    @Test
    public void testGetLists(){
        Board b = new Board();
        service.addBoard(b);
        TaskList l1 = new TaskList("list1");
        TaskList l2 = new TaskList("list2");
        assertEquals(0, service.getLists(b.id).size());
        service.addList(b.id, l1);
        service.addList(b.id, l2);
        assertTrue(service.getLists(b.id).contains(l1));
        assertTrue(service.getLists(b.id).contains(l2));
    }

    @Test
    public void testGetList(){
        Board b = new Board();
        TaskList l = new TaskList("list1");
        service.addBoard(b);
        service.addList(b.id, l);
        assertEquals(l, service.getList(b.id, l.id));
    }

    @Test
    public void testRenameList(){
        Board b = new Board();
        TaskList l = new TaskList("list1");
        service.addBoard(b);
        service.addList(b.id, l);
        assertEquals("list1", service.getList(b.id, l.id).getName());
        service.renameList(b.id, l.id, "list2");
        assertEquals("list2", service.getList(b.id, l.id).getName());
    }

    @Test
    public void testRemoveList(){
        Board b = new Board();
        TaskList l1 = new TaskList("list1");
        TaskList l2 = new TaskList("list2");
        service.addBoard(b);
        assertEquals(0, service.getLists(b.id).size());
        service.addList(b.id, l1);
        service.addList(b.id, l2);
        assertEquals(2, service.getLists(b.id).size());
        service.removeList(b.id, l1);
        assertEquals(1, service.getLists(b.id).size());
    }

    @Test
    public void testGetTasks(){
        Board b = new Board();
        TaskList l = new TaskList("list1");
        Task t = new Task("a", "b");
        service.addBoard(b);
        service.addList(b.id, l);
        assertEquals(0, service.getTasks(b.id, l.id).size());
        service.addTask(b.id, l.id, t);
        assertTrue(service.getTasks(b.id, l.id).contains(t));
    }

    @Test
    public void testGetTask(){
        Board b = new Board();
        TaskList l = new TaskList("list1");
        Task t = new Task("a", "b");
        service.addBoard(b);
        service.addList(b.id, l);
        service.addTask(b.id, l.id, t);
        assertEquals(t, service.getTask(b.id, l.id, t.id));
    }

    @Test
    public void testRenameTask(){
        Board b = new Board();
        TaskList l = new TaskList("list1");
        Task t = new Task("a", "b");
        service.addBoard(b);
        service.addList(b.id, l);
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
        service.addBoard(b);
        service.addList(b.id, l);
        service.addTask(b.id, l.id, t);
        assertEquals(t, service.getTask(b.id, l.id, t.id));
        service.removeTask(b.id, l.id, t);
        assertNull(service.getTask(b.id, l.id, t.id));
    }
}
