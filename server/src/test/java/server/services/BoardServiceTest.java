package server.services;

import commons.Board;
import commons.BoardColorScheme;
import commons.TaskPreset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import server.api.TestBoardRepository;
import server.services.BoardService;

import java.util.List;
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
    public void testRemoveBoardById(){
        Board b = new Board();
        service.addBoard(b);
        service.removeBoardByID(b.id);
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

    @Test
    public void testJoinBoard() {
        Board b = new Board();
        b.setName("Board1");
        service.addBoard(b);
        service.joinBoard(b.id, "ME");
        b = service.getBoard(b.id);
        assertTrue(b.getBoardMembers().contains("ME"));
    }

    @Test
    public void testSetColorScheme(){
        BoardColorScheme colorScheme = new BoardColorScheme();
        Board b = new Board();
        service.addBoard(b);
        b.id = 2;
        service.setBoardColorScheme(2, colorScheme);
        assertEquals(colorScheme, service.getBoard(2).getBoardColorScheme());
    }

    @Test
    public void addTaskPreset(){
        Board board = new Board("test", List.of(), List.of());
        TaskPreset preset1 = new TaskPreset("task1");
        TaskPreset preset2 = new TaskPreset("task2");
        service.addBoard(board);
        board.id = 3;
        service.setTaskPreset(3, preset1);
        board.id = 3;
        assertFalse(service.getBoard(3).getTaskPresets().contains(preset2));
        assertTrue(service.getBoard(3).getTaskPresets().contains(preset1));
    }

    @Test
    public void removeTaskPreset(){
        Board board = new Board("test", List.of(), List.of());
        TaskPreset preset1 = new TaskPreset("Dire");
        TaskPreset preset2 = new TaskPreset("Straits");
        service.addBoard(board);
        board.id = 3;
        service.setTaskPreset(3, preset1);
        board.id = 3;
        assertFalse(service.getBoard(3).getTaskPresets().contains(preset2));
        assertTrue(service.getBoard(3).getTaskPresets().contains(preset1));
        service.removeTaskPreset(3, preset1.id);
        assertFalse(service.getBoard(3).getTaskPresets().contains(preset1));
    }

    @Test
    public void updateTaskPreset(){
        Board board = new Board("test", List.of(), List.of());
        TaskPreset preset1 = new TaskPreset("Oasis");
        service.addBoard(board);
        board.id = 3;
        service.setTaskPreset(3, preset1);
        board.id = 3;
        assertEquals("Oasis", service.getBoard(3).getTaskPresets().get(0).getName());
        preset1.setName("sisaO");
        service.updateTaskPreset(3, preset1);
        assertEquals("sisaO", service.getBoard(3).getTaskPresets().get(0).getName());
    }
}
