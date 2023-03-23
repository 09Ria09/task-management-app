package server;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import server.api.TestBoardRepository;

import java.util.NoSuchElementException;

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
        assertThrows(NoSuchElementException.class, () -> service.getBoard(5L));
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
        assertThrows(NoSuchElementException.class, () -> service.getBoard(b.id));
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
}
