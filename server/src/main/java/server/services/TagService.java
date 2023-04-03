package server.services;

import commons.Board;
import commons.Tag;
import commons.Task;
import commons.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

import java.util.NoSuchElementException;
import java.util.*;

@Service
public class TagService {

    @Autowired
    private BoardRepository boardRepository;

    /**
     * Get the board corresponding to the boardID
     * @param boardID ID of the board
     * @return the board
     */
    public Board getBoard(final long boardID) {
        return boardRepository.findById(boardID)
                .orElseThrow(() -> new NoSuchElementException("Board not found"));
    }

    /**
     * Get the task corresponding to the ID's
     * @param boardID ID of the board
     * @param listID ID of the task list
     * @param taskID ID of the task
     * @return the task that corresponds to the given ID's
     */
    public Task getTask(final long boardID, final long listID, final long taskID) {
        Board board = getBoard(boardID);
        TaskList taskList = board.getTaskListById(listID)
                .orElseThrow(() -> new NoSuchElementException("Task list not found"));
        return taskList.getTaskById(taskID)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
    }

    /**
     * Returns all tasks in a given board
     * @param boardID ID of the board
     * @return a list of tasks
     */
    public List<Task> getAllTasks(final long boardID) {
        Board board = getBoard(boardID);
        List<TaskList> taskLists = board.getListTaskList();
        List<Task> tasks = new ArrayList<>();

        for(TaskList taskList : taskLists) {
            tasks.addAll(taskList.getTasks());
        }

        return tasks;
    }

    /**
     * Returns a list of all tags in a board
     * @param boardID the ID of the board
     * @return a list containing all the tags
     */
    public List<Tag> getBoardTags(final long boardID) {
        return getBoard(boardID).getTags();
    }

    /**
     * Returns a list of all tags in a task
     * @param boardID ID of the board
     * @param listID ID of the task list
     * @param taskID ID of the task we want the tags from
     * @return a list of tags from the corresponding ID's
     */
    public List<Tag> getTaskTags(final long boardID, final long listID, final long taskID) {
        return getTask(boardID, listID, taskID).getTags();
    }

    /**
     * Get a tag from a board given by an ID
     * @param boardID ID of the board
     * @param tagID ID of the tag that will be returned
     * @return the tag that is found with the corresponding ID's
     */
    public Tag getBoardTagByID(final long boardID, final long tagID) {
        Board board = getBoard(boardID);
        return board.getTagById(tagID)
                .orElseThrow(() -> new NoSuchElementException("Tag not found"));
    }

    /**
     * get a tag from a task by an ID
     * @param boardID ID of the board
     * @param listID ID of the list
     * @param taskID ID of the task
     * @param tagID ID of the tag that will be returned
     * @return the tag that is found with the corresponding ID's
     */
    public Tag getTaskTagByID(final long boardID, final long listID,
                              final long taskID, final long tagID) {
        Task task = getTask(boardID, listID, taskID);
        return task.getTagById(tagID)
                .orElseThrow(() -> new NoSuchElementException("Tag not found"));
    }

    /**
     * Adds a tag to the board with the given ID
     * @param boardID ID of the board
     * @param tag the tag that will be added
     * @return the tag that is added
     */
    public Tag addBoardTag(final long boardID, final Tag tag) {
        Board board = getBoard(boardID);
        board.addTag(tag);

        boardRepository.save(board);

        return tag;
    }

    /**
     * Adds a tag to a task with the given ID's
     * @param boardID ID of the board
     * @param listID ID of the task list
     * @param taskID ID of the task
     * @param tagID the tag that will be added
     * @return the tag that is added
     */
    public Tag addTaskTag(final long boardID, final long listID,
                          final long taskID, final long tagID) {
        Board board = getBoard(boardID);
        Tag tag = getBoardTagByID(boardID, tagID);
        Task task = getTask(boardID, listID, taskID);

        task.addTag(tag);
        boardRepository.save(board);

        return tag;
    }

    /**
     * Function to rename a tag to a given name
     * @param boardID ID of the board
     * @param tagID ID of the tag that will be renamed
     * @param newName the new name of the tag
     * @return the tag that has a new name
     */
    public Tag renameTag(final long boardID, final long tagID, final String newName) {
        Board board = getBoard(boardID);
        Tag tag = getBoardTagByID(boardID, tagID);

        tag.setName(newName);

        boardRepository.save(board);
        return tag;
    }

    /**
     * Function to change the color of a tag
     * @param boardID ID of the board
     * @param tagID ID of the tag that will get a new color
     * @param newColor the new color of the tag
     * @return the tag with a different color
     */
    public Tag recolorTag(final long boardID, final long tagID, final String newColor) {
        Board board = getBoard(boardID);
        Tag tag = getBoardTagByID(boardID, tagID);

        tag.setColor(newColor);

        boardRepository.save(board);
        return tag;
    }

    /**
     * Removes a tag from the board given by its ID
     * @param boardID ID of the board
     * @param tagID ID of the tag that will be removed
     * @return the removed tag
     */
    public Tag removeBoardTag(final long boardID, final long tagID) {
        Board board = getBoard(boardID);
        Tag tag = getBoardTagByID(boardID, tagID);
        List<Task> tasks = getAllTasks(boardID);

        for(Task task : tasks) {
            task.getTags().remove(tag);
        }

        board.removeTag(tag);

        boardRepository.save(board);
        return tag;
    }

    /**
     * Removes a tag from a task given by its ID
     * @param boardID ID of the board
     * @param listID ID of the task list
     * @param taskID ID of the task
     * @param tagID ID of the tag that will be removed
     * @return the removed tag
     */
    public Tag removeTaskTag(final long boardID, final long listID,
                             final long taskID, final long tagID) {
        Board board = getBoard(boardID);
        Task task = getTask(boardID, listID, taskID);
        Tag tag = getBoardTagByID(boardID, tagID);

        task.removeTag(tag);
        boardRepository.save(board);
        return tag;
    }


}
