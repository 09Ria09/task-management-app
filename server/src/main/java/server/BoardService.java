package server;

import commons.Board;
import commons.Task;
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
        return new ArrayList<Board>(boardRepository.findAll());
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
     * Returns all the task lists of the board assigned to the provided id.
     * It returns an empty list if the id is not assigned to a board.
     *
     * @param boardId the id of the board from which we want to access the board.
     * @return a list of all the task lists of the board.
     */
    public List<TaskList> getLists(final long boardId) {
        Board board = getBoard(boardId);
        return board.getListTaskList();
    }

    /**
     * Returns a specific list of a specific board, by ids.
     * Both the id of the board and the id of the list are needed to access it.
     *
     * @param boardId the id of the board.
     * @param listId  the id of the task list.
     * @return the task list corresponding to the specified ids.
     */
    public TaskList getList(final long boardId, final long listId) {
        Board board = getBoard(boardId);
        return board.getTaskListById(listId)
                .orElseThrow(() -> new NoSuchElementException("Task list not found"));
    }

    /**
     * Adds a new task list to a specific board in the JPARepository.
     *
     * @param boardID the id of the board where the list will be added.
     * @param list    the task list that will be added to the board and made persistent.
     * @return the task list that was added.
     */
    public TaskList addList(final long boardID, final TaskList list) {
        Board board = getBoard(boardID);
        board.addTaskList(list);
        boardRepository.save(board);
        return list;
    }

    /**
     * Removes the specified list from the specified board.
     *
     * @param boardID the id of the board where the list will be removed.
     * @param list    the task list that will be removed from the board.
     */
    public void removeList(final long boardID, final TaskList list) {
        Board board = getBoard(boardID);
        board.removeTaskList(list);
        boardRepository.save(board);
    }

    /**
     * Removes the list specified by a board if and a list id
     *
     * @param boardID    the id of the board where the list will be removed
     * @param taskListID the id of the task list that will be removed
     */
    public void removeListByID(final long boardID, final long taskListID) {
        Board board = getBoard(boardID);
        TaskList list = getList(boardID, taskListID);
        board.removeTaskList(list);
        boardRepository.save(board);
    }

    /**
     * Changes the name of a task list in a board to a new name.
     *
     * @param boardID the id of the board where the list to rename is.
     * @param listID  the id of the list to rename.
     * @param newName the new name of the list.
     */
    public void renameList(final long boardID, final long listID, final String newName) {
        Board board = getBoard(boardID);
        TaskList list = getList(boardID, listID);
        list.setName(newName);
        boardRepository.save(board);
    }

    /**
     * Gets a list of the tasks that are in a specified task list inside a specific board.
     *
     * @param boardId the id of the board.
     * @param listId  the id of the list.
     * @return the list of the tasks that are in the task list corresponding to the ids given.
     */
    public List<Task> getTasks(final long boardId, final long listId) {
        TaskList list = getList(boardId, listId);
        return list.getTasks();
    }

    /**
     * Gets a specific task from a specific task list in a specific board, by ids.
     * The id of the board, the list and the task are needed to access it.
     *
     * @param boardId the id of the board.
     * @param listId  the id of the list.
     * @param taskId  the id of the task.
     * @return the task corresponding to the given ids.
     */
    public Task getTask(final long boardId, final long listId, final long taskId) {
        TaskList list = getList(boardId, listId);
        return list.getTaskById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
    }

    /**
     * Adds a new task in a specific list in a specific board to the JPA Repository.
     *
     * @param boardID the id of the board where the task will be added.
     * @param listID  the id of the list where the task will be added.
     * @param task    the task that will be added to the task list and made persistent.
     * @return the task that was added.
     */
    public Task addTask(final long boardID, final long listID, final Task task) {
        Board board = getBoard(boardID);
        TaskList list = getList(boardID, listID);
        list.addTask(task);
        boardRepository.save(board);
        return task;
    }

    /**
     * Removes a task from a list in a specific board.
     *
     * @param boardID the id of the board where the task will be removed.
     * @param listID  the id of the list from which the task will be removed.
     * @param task    the task that will be removed.
     */
    public void removeTask(final long boardID, final long listID, final Task task) {
        Board board = getBoard(boardID);
        TaskList list = getList(boardID, listID);
        list.removeTask(task);
        boardRepository.save(board);
    }

    /**
     * Removes a task from a list in a specific board, given by the ids
     *
     * @param boardID the id of the board where the task will be removed
     * @param listID  the id of the list from which the task will be removed
     * @param taskID  the id of the task that will be removed
     */
    public void removeTaskByID(final long boardID, final long listID, final long taskID) {
        Board board = getBoard(boardID);
        TaskList list = getList(boardID, listID);
        Task task = getTask(boardID, listID, taskID);
        list.removeTask(task);
        boardRepository.save(board);
    }

    /**
     * Changes the name of a task to a new name.
     *
     * @param boardID the id of the board where the task will be renamed.
     * @param listID  the id of the list in which the task will be renamed.
     * @param taskId  the id of the task to rename.
     * @param newName the new name of the task.
     */
    public void renameTask(final long boardID, final long listID,
                           final long taskId, final String newName) {
        Board board = getBoard(boardID);
        TaskList list = getList(boardID, listID);
        Task task = getTask(boardID, listID, taskId);
        task.setName(newName);
        boardRepository.save(board);
    }
}
