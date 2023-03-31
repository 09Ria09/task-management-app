package server.services;

import commons.Board;
import commons.SubTask;
import commons.Task;
import commons.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

import java.io.IOException;
import java.util.*;

@Service
public class SubTaskService {

    @Autowired
    private BoardRepository boardRepository;

    public Board getBoard(final long boardID) {
        return boardRepository.findById(boardID)
                .orElseThrow(() -> new NoSuchElementException("Board not found"));
    }

    public TaskList getList(final long boardId, final long listId) {
        Board board = getBoard(boardId);
        return board.getTaskListById(listId)
                .orElseThrow(() -> new NoSuchElementException("Task list not found"));
    }

    public Task getTask(final long boardId, final long listId, final long taskId) {
        TaskList taskList = getList(boardId, listId);
        return taskList.getTaskById(taskId)
            .orElseThrow(() -> new NoSuchElementException("Task not found"));

    }

    public List<SubTask> getSubTasks(final long boardId, final long listId, final long taskId) {
        return getTask(boardId, listId, taskId).getSubtasks();
    }

    public SubTask getSubTask(final long boardId, final long listId,
                              final long taskId, final long subTaskId) {
        Task task = getTask(boardId, listId, taskId);
        return task.getSubTaskById(subTaskId)
                .orElseThrow(() -> new NoSuchElementException("Sub Task not found"));
    }

    public SubTask addSubTask(final long boardId, final long listId,
                              final long taskId, final SubTask subTask) {
        Task task = getTask(boardId, listId, taskId);
        Board board = getBoard(boardId);

        task.addSubtask(subTask);

        boardRepository.save(board);

        return subTask;
    }

    /**
     *
     * @param boardId id of board to access
     * @param listId id of list to access
     * @param taskId id of task to access
     * @param subTaskId id of sub task to access
     * @param name new name for the subtask
     * @return renamed subtask
     */
    public SubTask renameSubTask(final long boardId, final long listId,
                                 final long taskId, final long subTaskId,
                                 final String name) {
        SubTask subTask = getSubTask(boardId, listId, taskId, subTaskId);
        subTask.setName(name);
        Board board = getBoard(boardId);
        boardRepository.save(board);
        return subTask;
    }

    /**
     *
     * @param boardId id of board to access
     * @param listId id of list to access
     * @param taskId id of task to access
     * @param subTask sub task to be removed from task
     * @return removed subtask
     */
    public SubTask removeSubTask(final long boardId, final long listId,
                                 final long taskId, final SubTask subTask) {
        Task task = getTask(boardId, listId, taskId);
        Board board = getBoard(boardId);
        task.removeSubtask(subTask);
        boardRepository.save(board);
        return subTask;
    }

    /**
     *
     * @param boardId id of board to access
     * @param listId id of list to access
     * @param taskId id of task to access
     * @param subTaskId id of sub task to be removed from task
     * @return removed subtask
     */
    public SubTask removeSubTaskById(final long boardId, final long listId,
                                     final long taskId, final long subTaskId) {
        Task task = getTask(boardId, listId, taskId);
        SubTask subTask = task.getSubtaskById(subTaskId);
        Board board = getBoard(boardId);
        task.removeSubtask(subTask);
        boardRepository.save(board);
        return subTask;
    }

    public SubTask reorderSubTask(final long boardId, final long listId,
                                  final long taskId, final long subTaskId,
                                  final int newIndex) {
        Board board = boardRepository.getById(boardId);
        TaskList list = board.getTaskListById(listId)
                .orElseThrow(() -> new NoSuchElementException("No such task list"));
        Optional<Task> task = list.getTaskById(taskId);
        if(task.isPresent()) {
            task.get().reorderSubTasks(subTaskId, newIndex);
            boardRepository.save(board);
            return task.get().getSubtaskById(subTaskId);
        }
        throw new NoSuchElementException("No such task");
    }
}
