package client.mocks;

import client.customExceptions.BoardException;
import client.utils.BoardUtils;
import client.utils.ServerUtils;
import commons.Board;

import java.util.ArrayList;
import java.util.List;

public class MockBoardUtils extends BoardUtils {

    private List<Board> boards;
    private boolean stopped;

    public MockBoardUtils(final ServerUtils server) {
        super(server);
        boards = new ArrayList<>();
    }

    public void setBoards(final List<Board> boards) {
        this.boards = boards;
    }

    @Override
    public List<Board> getBoards() throws BoardException {
        return new ArrayList<>(boards);
    }

    @Override
    public Board deleteBoard(final long boardId) {
        Board removedBoard = null;
        for (int i = 0; i < boards.size(); i++) {
            if (boards.get(i).getId() == boardId) {
                removedBoard = boards.get(i);
                boards.remove(i);
                break;
            }
        }
        return removedBoard;
    }

    @Override
    public Board getBoard(final long boardId) {
        for (Board board : boards) {
            if (board.getId() == boardId) {
                return board;
            }
        }
        return null;
    }
    @Override
    public void stop() {
        stopped = true;
    }

    public boolean isStopped() {
        return stopped;
    }
}
