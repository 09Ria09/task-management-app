package server;

import commons.Board;
import commons.Task;
import commons.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;

public class TaskService {

    @Autowired
    private BoardRepository boardRepository;


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
    public Task getTask(final long boardId, final long listId, final long taskId){
        List<Task> tasklist = getList(boardId, listId).getTasks();

        for (Task task : tasklist) {
            if (task.getId() == taskId) {
                return task;
            }
        }

        return null;
    }

    /**
     * Adds a new task to a specific board in the list.
     * @param boardId the id of the board where the task will be added.
     * @param task the task that will be added to the board and made persistent.
     * @param listId the id of the list to which to add the task.
     */
    public void addTask(final long boardId, final long listId, final Task task){
        TaskList list = getList(boardId, listId);
        list.addTask(task);
    }

    /**
     * Changes the name of a task in a list to a new name.
     * @param boardID the id of the board where the list to rename is.
     * @param listID the id of the list where the task is.
     * @param taskId the id of the task to rename.
     * @param newName the new name of the task.
     */
    public void renameTask(final long boardID, final long listID,
                           final long taskId, final String newName){
        Task task = getTask(boardID, listID, taskId);

        task.setName(newName);
    }

    /**
     * Removes the specified list from the specified board.
     * @param boardId the id of the board where the task will be removed.
     * @param listId the id of the list where the task will be removed.
     * @param task the task that will be removed from the board.
     */
    public void removeTask(final long boardId, final long listId, final Task task){
        TaskList list = getList(boardId, listId);
        list.removeTask(task);
    }

    /**
     * Removes the specified list from the specified board.
     * @param boardId the id of the board where the task will be removed.
     * @param listId the id of the list where the task will be removed.
     * @param taskId the task that will be removed from the board.
     */
    public void removeTaskById(final long boardId, final long listId, final long taskId){
        TaskList list = getList(boardId, listId);

        for (Task task : list.getTasks()) {
            if (taskId == task.getId()) {
                list.removeTask(task);
            }
        }
    }


    //finds a task list given a boardId and listId
    public TaskList getList(final long boardId, final long listId) {
        List<TaskList> listlist =  boardRepository.findById(boardId).map(Board::getListTaskList)
                .orElse(new ArrayList<>());

        for (TaskList list : listlist) {
            if (listId == list.getId()) {
                return list;
            }
        }

        return null;
    }
}