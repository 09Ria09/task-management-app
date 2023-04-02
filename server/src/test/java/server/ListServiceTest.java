package server;

import commons.Board;
import commons.Task;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import server.api.TestBoardRepository;
import server.services.BoardService;
import server.services.ListService;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListServiceTest {
    private ListService service;
    private BoardService boardService;

    @BeforeEach
    public void setup(){
        service = new ListService();
        boardService = new BoardService();
        TestBoardRepository repo = new TestBoardRepository();
        ReflectionTestUtils.setField(service, "boardRepository", repo);
        ReflectionTestUtils.setField(boardService, "boardRepository", repo);
    }

    @Test
    public void testGetLists(){
        Board b = new Board();
        boardService.addBoard(b);
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
        boardService.addBoard(b);
        service.addList(b.id, l);
        assertEquals(l, service.getList(b.id, l.id));
    }

    @Test
    public void testRenameList(){
        Board b = new Board();
        TaskList l = new TaskList("list1");
        boardService.addBoard(b);
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
        boardService.addBoard(b);
        assertEquals(0, service.getLists(b.id).size());
        service.addList(b.id, l1);
        service.addList(b.id, l2);
        assertEquals(2, service.getLists(b.id).size());
        service.removeList(b.id, l1);
        assertEquals(1, service.getLists(b.id).size());
    }

    @Test
    public void testRemoveListByID() {
        Board board = new Board("Test Board", new LinkedList<>(), new LinkedList<>());
        TaskList list1 = new TaskList("List 1");
        TaskList list2 = new TaskList("List 2");
        board.addTaskList(list1);
        board.addTaskList(list2);
        boardService.addBoard(board);

        service.removeListByID(board.getId(), list2.getId());
        List<TaskList> lists = service.getLists(board.getId());
        assertEquals(1, lists.size());
        assertEquals(list1.getId(), lists.get(0).getId());
    }

    @Test
    public void testReorderTask() {
        Board board = new Board("Test Board", new LinkedList<>(), new LinkedList<>());
        TaskList list = new TaskList("List 1");
        Task task1 = new Task("Task 1", "description");
        Task task2 = new Task("Task 2", "description");
        Task task3 = new Task("Task 3", "description");
        list.addTask(task1);
        list.addTask(task2);
        list.addTask(task3);
        board.addTaskList(list);
        boardService.addBoard(board);

        service.reorderTask(board.getId(), list.getId(), task2.getId(), 0);

        TaskList updatedList = service.getList(board.getId(), list.getId());
        assertEquals(3, updatedList.getTasks().size());
        assertEquals(task2.getId(), updatedList.getTasks().get(0).getId());
        assertEquals(task1.getId(), updatedList.getTasks().get(1).getId());
        assertEquals(task3.getId(), updatedList.getTasks().get(2).getId());
    }
}
