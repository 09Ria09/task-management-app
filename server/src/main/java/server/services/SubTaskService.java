package server.services;

import commons.Board;
import commons.SubTask;
import commons.Task;
import commons.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

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
}
