package client.mocks;

import client.customExceptions.BoardException;
import client.services.BoardService;
import client.utils.BoardUtils;
import commons.Board;

import java.util.ArrayList;
import java.util.List;

public class MockBoardService extends BoardService {
    private List<Board> boards = new ArrayList<>();
    public int stopCounter = 0;

    public MockBoardService(final BoardUtils boardUtils) {
        super(boardUtils);
    }

    @Override
    public void deleteBoard(final Board board, final boolean adminAction) throws BoardException {
        boards.remove(board);
    }

    @Override
    public List<Board> getAllBoards() throws BoardException {
        return new ArrayList<>(boards);
    }

    @Override
    public void stop() {
        stopCounter++;
    }

    public void addMockBoard(final Board board) {
        boards.add(board);
    }
}