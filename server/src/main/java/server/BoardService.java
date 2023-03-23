package server;

import commons.Board;
import commons.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BoardService {

    /**
     * The JPA repository that contains all the data about the boards, lists and tasks.
     * It is constructed automatically.
     */
    @Autowired
    private BoardRepository boardRepository;

    /**
     * @return a list of all the boards stored in the repository.
     */
    public List<Board> getBoards() {
        return new ArrayList<>(boardRepository.findAll());
    }

    /**
     * @param boardID the id of the board that will be acessed.
     * @return the board with the corresponding id, or throw a no
     * such element exception if there is none.
     */
    public Board getBoard(final long boardID) {
        return boardRepository.findById(boardID)
                .orElseThrow(() -> new NoSuchElementException("Board not found"));
    }

    /**
     * Adds a new board to the repository to make it persistent.
     *
     * @param board the board that will be added.
     * @return the board that was added.
     */
    public Board addBoard(final Board board) {
        boardRepository.save(board);
        return board;
    }

    /**
     * Removes a board from the repository.
     *
     * @param board the board to remove.
     */
    public void removeBoard(final Board board) {
        boardRepository.delete(board);
    }

    /**
     * Removes a board from the repository, given by the id
     *
     * @param boardID the id of the board that will be removed
     */
    public void removeBoardByID(final long boardID) {
        Board board = getBoard(boardID);
        boardRepository.delete(board);
    }

    /**
     * Change the name of a board to a new name, if the provided id is assigned to a board.
     *
     * @param boardID the id of the board to rename.
     * @param newName the new name of the board.
     */
    public void renameBoard(final long boardID, final String newName) {
        Board board = getBoard(boardID);
        board.setName(newName);
        boardRepository.save(board);
    }

    /**
     * Creates a default board that contains 3 lists : To Do, In Progress, Done
     */
    public void createDefaultBoard(){
        TaskList todo = new TaskList("To Do");
        TaskList inprogress = new TaskList("In Progress");
        TaskList done = new TaskList("Done");
        Board b = new Board("Main", List.of(todo, inprogress, done), new ArrayList<>());
        this.addBoard(b);
    }
}