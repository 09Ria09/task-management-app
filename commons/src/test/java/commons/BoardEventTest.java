package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardEventTest {

    @Test
    public void getEventTypeTest() {
        BoardEvent testBoardEvent = new BoardEvent("event", new Board());
        assertEquals(testBoardEvent.getEventType(), "event");
    }

    @Test
    public void setEventTypeTest() {
        BoardEvent testBoardEvent = new BoardEvent("event", new Board());
        assertEquals(testBoardEvent.getEventType(), "event");
        testBoardEvent.setEventType("newEvent");
        assertEquals(testBoardEvent.getEventType(), "newEvent");
    }

    @Test
    public void getBoardTest() {
        Board board = new Board();
        BoardEvent testBoardEvent = new BoardEvent("event", board);
        assertEquals(board, testBoardEvent.getBoard());
    }

    @Test
    public void setBoardTest() {
        Board board = new Board();
        BoardEvent testBoardEvent = new BoardEvent();
        testBoardEvent.setBoard(board);
        assertEquals(board, testBoardEvent.getBoard());
    }
}
