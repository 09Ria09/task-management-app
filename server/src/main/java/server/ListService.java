package server;

import commons.Board;
import commons.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import server.database.BoardRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class ListService {

    @Autowired
    private BoardRepository boardRepository;


    public Board getBoard(final long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("Board not found"));
    }

    /**
     * Returns all the task lists of the board assigned to the provided id.
     * It returns an empty list if the id is not assigned to a board.
     * @param boardId the id of the board from which we want to access the board.
     * @return a list of all the task lists of the board.
     */
    public List<TaskList> getLists(final long boardId){
        return getBoard(boardId)
                .getListTaskList();
    }

    /**
     * Returns a specific list of a specific board, by ids.
     * Both the id of the board and the id of the list are needed to access it.
     *
     * @param boardId the id of the board.
     * @param listId  the id of the task list.
     * @return the task list corresponding to the specified ids.
     */
    public TaskList getList(final long boardId, final long listId){
        return getBoard(boardId)
                .getTaskListById(listId)
                .orElseThrow(() -> new NoSuchElementException("List not found"));
    }

    /**
     * Adds a new task list to a specific board in the JPARepository.
     * @param boardID the id of the board where the list will be added.
     * @param list the task list that will be added to the board and made persistent.
     */
    public TaskList addList(final long boardID, final TaskList list){
        System.out.println("add list : " + list.getName());
        Board board = getBoard(boardID);
        board.addTaskList(list);
        boardRepository.save(board);
        return list;
    }

    /**
     * Changes the name of a task list in a board to a new name.
     * @param boardID the id of the board where the list to rename is.
     * @param listID the id of the list to rename.
     * @param newName the new name of the list.
     */
    public TaskList renameList(final long boardID, final long listID, final String newName){
        Board board = getBoard(boardID);
        TaskList list = board.getTaskListById(listID)
                .orElseThrow(() -> new NoSuchElementException("List doesn't exist"));
        list.setName(newName);
        return list;
    }

    /**
     * Removes the specified list from the specified board.
     * @param boardID the id of the board where the list will be removed.
     * @param list the task list that will be removed from the board.
     */
    public TaskList removeList(final long boardID, final TaskList list){
        Board board = getBoard(boardID);
        board.removeTaskList(list);
        boardRepository.save(board);
        return list;
    }

    /**
     * Removes the list specified by a board if and a list id
     *
     * @param boardID the id of the board where the list will be removed
     * @param taskListID the id of the task list that will be removed
     */
    public Board removeListByID(final long boardID, final long taskListID) {
        Board board = getBoard(boardID);
        TaskList list = board.getTaskListById(taskListID)
                .orElseThrow(() -> new NoSuchElementException("No such task list"));
        board.removeTaskList(list);

        return board;
    }


}
