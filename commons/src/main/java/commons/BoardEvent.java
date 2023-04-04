package commons;

public class BoardEvent {
    private String eventType;
    private Board board;

    public BoardEvent() {}

    //I made this class only to help with how I handle the responses
    //from the server to update the admin dashboard so that it works
    //with multiple actions on boards (currently only add and delete,
    //if we have enough time ill do rename too)
    public BoardEvent(final String eventType, final Board board) {
        this.eventType = eventType;
        this.board = board;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(final Board board) {
        this.board = board;
    }
}
