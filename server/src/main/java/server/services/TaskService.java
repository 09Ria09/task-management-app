package server.services;

import commons.Board;
import commons.Task;
import commons.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    @Autowired
    private BoardRepository boardRepository;


    public Board getBoard(final long boardID) {
        return boardRepository.findById(boardID)
                .orElseThrow(() -> new NoSuchElementException("Board not found"));
    }

    //finds a task list given a boardId and listId
    public TaskList getList(final long boardId, final long listId) {
        Board board = getBoard(boardId);
        return board.getTaskListById(listId)
                .orElseThrow(() -> new NoSuchElementException("Task list not found"));
    }


    /**
     * Returns all the tasks of the list assigned to the provided id.
     * It returns an empty list if the id is not assigned to a real list.
     * @param listId the id of the list from which we want to access the task.
     * @param boardId the id of the board from which we want to access the list.
     * @return a list of all the tasks of the list.
     */
    public List<Task> getTasks(final long boardId, final long listId){
        return getList(boardId, listId).getTasks();
    }

    /**
     * Returns a specific task of a specific list from a specific board, by ids.
     * The id of the board, list and the id of the task are needed to access it.
     * @param taskId the id of the task.
     * @param listId the id of the task list.
     * @param boardId the id of the board.
     * @return the task corresponding to the specified ids.
     */
    public Task getTask(final long boardId, final long listId, final long taskId) {
        TaskList list = getList(boardId, listId);
        return list.getTaskById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
    }

    /**
     * Adds a new task to a specific board in the list.
     * @param boardId the id of the board where the task will be added.
     * @param task the task that will be added to the board and made persistent.
     * @param listId the id of the list to which to add the task.
     */
    public Task addTask(final long boardId, final long listId, final Task task){
        TaskList list = getList(boardId, listId);
        Board board = getBoard(boardId);

        list.addTask(task);

        //I am not sure whether this actually updates the list in the board.
        boardRepository.save(board);

        return task;
    }

    /**
     * Changes the name of a task in a list to a new name.
     * @param boardID the id of the board where the list to rename is.
     * @param listID the id of the list where the task is.
     * @param taskId the id of the task to rename.
     * @param newName the new name of the task.
     */
    public Task renameTask(final long boardID, final long listID,
                           final long taskId, final String newName){
        Board board = getBoard(boardID);
        Task task = getTask(boardID, listID, taskId);

        task.setName(newName);

        //I am not sure whether this actually updates the list in the board.
        boardRepository.save(board);

        return task;
    }

    /**
     * Removes the specified list from the specified board.
     * @param boardId the id of the board where the task will be removed.
     * @param listId the id of the list where the task will be removed.
     * @param task the task that will be removed from the board.
     */
    public Task removeTask(final long boardId, final long listId, final Task task){
        Board board = getBoard(boardId);
        TaskList list = getList(boardId, listId);
        list.removeTask(task);

        //I am not sure whether this actually updates the list in the board.
        boardRepository.save(board);

        return task;
    }

    /**
     * Removes the specified task from the specified board.
     * @param boardId the id of the board where the task will be removed.
     * @param listId the id of the list where the task will be removed.
     * @param taskId the task that will be removed from the board.
     */
    public Task removeTaskById(final long boardId, final long listId, final long taskId){
        Board board = getBoard(boardId);
        TaskList list = getList(boardId, listId);
        Task task = getTask(boardId, listId, taskId);

        list.removeTask(task);

        boardRepository.save(board);

        return task;
    }

    public Task editDescription(final long boardid, final long listid,
                                  final long taskid, final String description) {
        Board board = getBoard(boardid);
        Task task = getTask(boardid, listid, taskid);

        task.setDescription(description);

        //I am not sure whether this actually updates the list in the board.
        boardRepository.save(board);

        return task;
    }
}