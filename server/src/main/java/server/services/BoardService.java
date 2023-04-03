package server.services;

import commons.Board;
import commons.BoardColorScheme;
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
     * I have adapted this such that we can access the id of a board
     * once it is saved and then update it with the invite key
     * I am not sure if this is the best approach, but I havent
     * found anything better
     * @param board the board that will be added.
     * @return the board that was added.
     */
    public Board addBoard(final Board board) {
        Board saved = boardRepository.save(board);
        String inviteKey = generateInviteKey(saved.getId());
        Board updateWithInviteKey = new Board(saved.getName(), saved.getListTaskList(),
                saved.getTags(), inviteKey);
        updateWithInviteKey.id = saved.getId();
        return boardRepository.save(updateWithInviteKey);
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

    /**
     * Adds a member to a board, if the board exists.
     * @param boardID the id of the board to add the member to.
     * @param memberName the name of the member to add.
     * @return the board with the new member.
     */
    public Board joinBoard(final long boardID, final String memberName) {
        Board board = getBoard(boardID);
        board.getBoardMembers().add(memberName);
        boardRepository.save(board);
        return board;
    }

    public BoardColorScheme setBoardColorScheme(final long boardID,
                                                final BoardColorScheme boardColorScheme) {
        Board board = getBoard(boardID);
        board.setBoardColorScheme(boardColorScheme);
        return boardRepository.save(board).getBoardColorScheme();
    }

    /**
     *  This basically generates a random string of two uppercase letters
     *  for the invite key so that it is nice and easy to remember
     * @return a new string of two uppercase letters chosen at random
     */
    private String createKeyPart() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] keyCharacters = new char[2];
        for(int i = 0; i < 2; i++) {
            int randomIndex = (int) (Math.random() * alphabet.length());
            keyCharacters[i] = alphabet.charAt(randomIndex);
        }
        return new String(keyCharacters);
    }

    /**
     * this actually generates the invite key
     * by combining the id and the random part
     * @param boardId the id of the board
     * @return the invite key
     */
    private String generateInviteKey(final long boardId) {
        return String.format("%03d", boardId) + createKeyPart();
    }

}