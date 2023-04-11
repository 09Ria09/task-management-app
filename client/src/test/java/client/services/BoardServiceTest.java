package client.services;

import client.customExceptions.BoardException;
import client.mocks.MockBoardUtils;
import client.mocks.MockServerUtils;
import client.utils.ServerUtils;
import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardServiceTest {
    private BoardService boardService;
    private MockBoardUtils mockBoardUtils;

    @BeforeEach
    public void setUp() {
        mockBoardUtils = new MockBoardUtils(null);
        boardService = new BoardService(mockBoardUtils);
    }

    @Test
    public void testGetAllBoards() throws BoardException {
        List<Board> testBoards = new ArrayList<>();
        testBoards.add(new Board());
        testBoards.add(new Board());
        mockBoardUtils.setBoards(testBoards);

        List<Board> boards = boardService.getAllBoards();
        assertEquals(testBoards.size(), boards.size(), "The number of boards should match");
    }
    @Test
    void testDeleteBoardAsAdmin() throws BoardException {
        List<Board> initialBoards = new ArrayList<>();
        Board boardToDelete = new Board();
        initialBoards.add(boardToDelete);
        initialBoards.add(new Board());

        mockBoardUtils.setBoards(initialBoards);

        boardService.deleteBoard(boardToDelete, true);
        List<Board> remainingBoards = mockBoardUtils.getBoards();
        assertEquals(1, remainingBoards.size());
        assertFalse(remainingBoards.contains(boardToDelete));
    }

    @Test
    void testGetBoard() throws BoardException {
        List<Board> testBoards = new ArrayList<>();
        Board board1 = new Board();
        board1.id = 1;
        Board board2 = new Board();
        board2.id = 2;
        testBoards.add(board1);
        testBoards.add(board2);
        mockBoardUtils.setBoards(testBoards);

        Board board = boardService.getBoard(1);
        assertNotNull(board);
        assertEquals(1, board.getId());
    }
    @Test
    void testStop() {
        assertFalse(mockBoardUtils.isStopped());
        boardService.stop();
        assertTrue(mockBoardUtils.isStopped());
    }
}