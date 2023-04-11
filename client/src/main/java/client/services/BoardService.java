package client.services;

import client.customExceptions.BoardException;
import client.utils.BoardUtils;
import com.google.inject.Inject;
import commons.Board;

import java.util.List;

public class BoardService {

    private BoardUtils boardUtils;

    @Inject
    public BoardService(final BoardUtils boardUtils) {
        this.boardUtils = boardUtils;
    }

    public List<Board> getAllBoards() throws BoardException {
        return boardUtils.getBoards();
    }
    //here I check if the deletion is done from the dashboard
    //or by an user in the overview, so that I dont call
    //the deleteBoard method twice
    public void deleteBoard(final Board board, final boolean adminAction) throws BoardException {
        if (adminAction) {
            boardUtils.deleteBoard(board.getId());
        }
    }

    public Board getBoard(final long boardId) throws BoardException {
        return boardUtils.getBoard(boardId);
    }
    public void stop() {
        boardUtils.stop();
    }
}
