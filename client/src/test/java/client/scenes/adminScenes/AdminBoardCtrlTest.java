package client.scenes.adminScenes;

import client.mocks.MockBoardService;
import client.mocks.MockBoardUtils;
import client.mocks.MockMainCtrl;
import client.services.BoardService;
import client.scenes.MainCtrl;
import commons.Board;
import commons.BoardEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminBoardCtrlTest {

    private TestableAdminBoardCtrl adminBoardCtrl;
    private MockBoardService mockBoardService;
    private MockMainCtrl mockMainCtrl;

    @BeforeEach
    public void setUp() {
        mockBoardService = new MockBoardService(null);
        mockMainCtrl = new MockMainCtrl();
        adminBoardCtrl = new TestableAdminBoardCtrl(mockBoardService, mockMainCtrl);
    }

    @Test
    public void testSolveEvent() {
        Board board = new Board();
        BoardEvent addEvent = new BoardEvent("ADD", board);
        BoardEvent deleteEvent = new BoardEvent("DELETE", board);

        adminBoardCtrl.solveEvent(addEvent);
        assertEquals(1, adminBoardCtrl.boardList.size());

        adminBoardCtrl.solveEvent(deleteEvent);
        assertEquals(0, adminBoardCtrl.boardList.size());
    }

    @Test
    public void testGoBack() {
        adminBoardCtrl.goBack();
        assertEquals(1, mockBoardService.stopCounter);
        assertTrue( mockMainCtrl.isMethodCalled("showBoardCatalogue"));
    }

    @Test
    public void testStop() {
        adminBoardCtrl.stop();
        assertEquals(1, mockBoardService.stopCounter);
    }

    private static class TestableAdminBoardCtrl extends AdminBoardCtrl {
        List<Board> boardList = new ArrayList<>();

        public TestableAdminBoardCtrl(BoardService boardService, MainCtrl mainCtrl) {
            super(boardService, mainCtrl);
        }

        @Override
        public void addBoard(Board board) {
            boardList.add(board);
        }

        @Override
        public void deleteBoard(final Board board, final boolean adminAction) {
            boardList.remove(board);
        }
    }
}