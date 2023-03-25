package server;

import commons.Board;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import server.api.TestBoardRepository;
import server.services.BoardService;
import server.services.ListService;

import static org.junit.jupiter.api.Assertions.*;

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
}
