package client.utils;

import client.customExceptions.BoardException;
import client.mocks.MockRestUtils;
import client.mocks.MockServerUtils;
import client.mocks.ResponseClone;
import commons.Board;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BoardUtilsTest {


    private BoardUtils boardUtils;
    private MockRestUtils mockRestUtils;
    private MockServerUtils mockServerUtils;

    @BeforeEach
    public void setUp() {
        mockRestUtils = new MockRestUtils();
        mockServerUtils = new MockServerUtils();
        mockServerUtils.setMockRestUtils(mockRestUtils);
        mockServerUtils.setServerAddress("http://example.com");
        boardUtils = new BoardUtils(mockServerUtils);
    }

    @Test
    public void testGetBoard_success() throws Exception {
        Board expectedResult = new Board();
        expectedResult.setName("Example board");
        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        Board result = boardUtils.getBoard(1L);
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    public void testGetBoard_throwsException() {
        ResponseClone mockResponse = new ResponseClone(Response.Status.NOT_FOUND.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(BoardException.class, () -> {
            boardUtils.getBoard(1L);
        });
    }
    @Test
    void testAddBoard_success() throws Exception {
        Board inputBoard = new Board();
        inputBoard.setName("New board");
        Board expectedResult = new Board();
        expectedResult.setName("New board");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        Board result = boardUtils.addBoard(inputBoard);
        assertEquals(expectedResult.getName(), result.getName());
    }

    @Test
    void testAddBoard_throwsException() {
        Board inputBoard = new Board();
        inputBoard.setName("Invalid board");

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(BoardException.class, () -> {
            boardUtils.addBoard(inputBoard);
        });
    }

    @Test
    void testRenameBoard_success() throws Exception {
        long boardId = 1L;
        String newName = "Updated board name";

        Board expectedResult = new Board();
        expectedResult.setId(boardId);
        expectedResult.setName(newName);

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        Board result = boardUtils.renameBoard(boardId, newName);
        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
    }
    @Test
    void testRenameBoard_throwsException() {
        long boardId = 1L;
        String newName = "";

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(BoardException.class, () -> {
            boardUtils.renameBoard(boardId, newName);
        });
    }
    @Test
    void testDeleteBoard_success() throws Exception {
        long boardId = 1L;

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertDoesNotThrow(() -> {
            boardUtils.deleteBoard(boardId);
        });
    }

    @Test
    void testDeleteBoard_throwsException() {
        long boardId = 1L;

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(BoardException.class, () -> {
            boardUtils.deleteBoard(boardId);
        });
    }

    @Test
    void testJoinBoard_throwsException() {
        String inviteKey = "invalid-invite-key";

        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(BoardException.class, () -> {
            boardUtils.joinBoard(inviteKey, "test user");
        });
    }

    @Test
    void testJoinBoard_wrongKey() {
        String inviteKey = "002CD";
        Board expectedResult = new Board();
        expectedResult.setId(1L);
        expectedResult.setName("Example board");
        ResponseClone mockResponse = new ResponseClone(Response.Status.BAD_REQUEST.getStatusCode(), null);
        mockRestUtils.setMockResponse(mockResponse);

        assertThrows(BoardException.class, () -> {
            boardUtils.joinBoard(inviteKey, "test user");
        });
    }

    @Test
    void testGetBoards_success() throws Exception {
        Board board1 = new Board();
        board1.setId(1L);
        board1.setName("Board 1");
        Board board2 = new Board();
        board2.setId(2L);
        board2.setName("Board 2");
        List<Board> expectedResult = new ArrayList<>();
        expectedResult.add(board1);
        expectedResult.add(board2);

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        List<Board> result = boardUtils.getBoards();
        assertEquals(expectedResult.size(), result.size());
        for (int i = 0; i < expectedResult.size(); i++) {
            assertEquals(expectedResult.get(i).getId(), result.get(i).getId());
            assertEquals(expectedResult.get(i).getName(), result.get(i).getName());
        }
    }

    @Test
    void testGetBoards_empty() throws BoardException {
        List<Board> expectedResult = Collections.emptyList();

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), expectedResult);
        mockRestUtils.setMockResponse(mockResponse);

        List<Board> result = boardUtils.getBoards();
        assertEquals(0, result.size());
    }

    @Test
    void testGetBoardInviteKey_success() throws Exception {
        long boardId = 1L;
        Board board = new Board();
        board.setId(boardId);
        board.setName("Example board");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), board);
        mockRestUtils.setMockResponse(mockResponse);

        String result = boardUtils.getBoardInviteKey(boardId);
        assertEquals(board.getInviteKey(), result);
    }

    @Test
    void testJoinBoard_wrongInviteKey() throws Exception {
        long boardId = 1L;
        Board board = new Board();
        board.setId(boardId);
        board.setName("Example board");

        ResponseClone mockResponse = new ResponseClone(Response.Status.OK.getStatusCode(), board);
        mockRestUtils.setMockResponse(mockResponse);

        String wrongInviteKey = "000-invite-key";

        assertThrows(BoardException.class, () -> {
            boardUtils.joinBoard(wrongInviteKey, "test user");
        });
    }
}