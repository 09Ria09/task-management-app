package server.api;

import java.util.*;

import commons.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.BoardService;

@RestController
@RequestMapping("/api")
public class BoardController {

    private BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * Get util method to get all boards, retrieved as a list
     *
     * @return a list of all the boards stored in the repository
     */
    @GetMapping("/get/boards")
    public List<Board> getBoards() {
        return boardService.getBoards();
    }

    /**
     * Get util method to get a specific board
     *
     * @param id the id of the board that will be retrieved
     * @return the board with the corresponding id
     */
    @GetMapping("/get/board/{id}")
    public Board getBoard(@PathVariable("id") long id) {
        return boardService.getBoard(id);
    }

    /**
     * Get util method to get all the tasklists from a specific board
     *
     * @param id the id of the board that will be accesses
     * @return a list of tasklists from the corresponding board
     */
    @GetMapping("/get/board/{id}/tasklists")
    public List<TaskList> getTaskLists(@PathVariable("id") long id) {
        return boardService.getLists(id);
    }

    /**
     * Get util method for a specific list of a specific board
     *
     * @param board_id the id of the board from which we want to access the list
     * @param taskList_id the id of the list that will be accessed
     * @return the corresponding list from the corresponding board
     */
    @GetMapping("/get/board/{board_id}/tasklist/{taskList_id}")
    public TaskList getTaskList(
            @PathVariable("board_id") long board_id,
            @PathVariable("taskList_id") long taskList_id
    ) {
        return boardService.getList(board_id, taskList_id);
    }

    /**
     * Get util method to get all the tasks in a specific list of a specific board
     *
     * @param board_id the id of the board from which we want to access the tasks
     * @param taskList_id the id of the list from which we want to access the tasks
     * @return a list of tasks from the corresponding list and board
     */
    @GetMapping("/get/board/{board_id}/tasklist/{taskList_id}/tasks")
    public List<Task> getTasks(
            @PathVariable("board_id") long board_id,
            @PathVariable("taskList_id") long taskList_id
    ) {
        return boardService.getTasks(board_id, taskList_id);
    }

    /**
     * Get util method to get a specific task from a specific tasklist and board
     *
     * @param board_id the id of the board from which we want to access the task
     * @param taskList_id the id of the tasklist from which we want to access the task
     * @param task_id the id of the task we want to access
     * @return the task corresponding to all the given ids
     */
    @GetMapping("/get/board/{board_id}/tasklist/{taskList_id}/task/{task_id}")
    public Task getTask(
            @PathVariable("board_id") long board_id,
            @PathVariable("taskList_id") long taskList_id,
            @PathVariable("taks_id") long task_id
    ) {
        return boardService.getTask(board_id, taskList_id, task_id);
    }

    /**
     * Post util method to add a board
     *
     * @param board the board you want to add
     */
    @PostMapping("/post/board")
    public void addBoard(@RequestBody Board board) {
        boardService.addBoard(board);
    }

    /**
     * Post util method to add a tasklist to a specific board
     *
     * @param board_id the id from the board you want to add a tasklist to
     * @param taskList the tasklist that you want to add to board with the given id
     */
    @PostMapping("/post/{board_id}/tasklist")
    public void addTaskList(
            @PathVariable("board_id") long board_id,
            @RequestBody TaskList taskList
    ) {
        boardService.addList(board_id, taskList);
    }

    /**
     * Post util method to add a task to a specific board and list
     *
     * @param board_id the id from the board you want to add the task to
     * @param tasklist_id the id from the list you want to add the task to
     * @param task the task you want to add to the entities with the given ids
     */
    @PostMapping("/post/{board_id}/{tasklist_id}/task")
    public void addTask(
            @PathVariable("board_id") long board_id,
            @PathVariable("tasklist_id") long tasklist_id,
            @RequestBody Task task
    ) {
        boardService.addTask(board_id, tasklist_id, task);
    }

    /**
     * Delete request to delete a board with a specific id
     *
     * @param board_id the corresponding id of the board that will be deleted
     */
    @DeleteMapping("/delete/{board_id}")
    public void deleteBoard(@PathVariable("board_id") long board_id) {
        boardService.removeBoardByID(board_id);
    }

    /**
     * Delete request to delete a tasklist with a list id and board id
     *
     * @param board_id the id of the board from where the list will be deleted
     * @param tasklist_id the id of the list that will be deleted
     */
    @DeleteMapping("/delete/{board_id}/{tasklist_id}")
    public void deleteTaskList(
            @PathVariable("board_id") long board_id,
            @PathVariable("tasklist_id") long tasklist_id
    ) {
        boardService.removeListByID(board_id, tasklist_id);
    }

    /**
     * Delete request to delete a task with a board, list and task id
     *
     * @param board_id the id of the board from where the task will be deleted
     * @param tasklist_id the id of the tasklist from where the list will be deleted
     * @param task_id the id of the task that will be deleted
     */
    @DeleteMapping("/delete/{board_id}/{tasklist_id}/{task_id}")
    public void deleteTask(
            @PathVariable("board_id") long board_id,
            @PathVariable("tasklist_id") long tasklist_id,
            @PathVariable("task_id") long task_id
    ) {
        boardService.removeTaskByID(board_id, tasklist_id, task_id);
    }

    /**
     * Put request to change the name of a board
     *
     * @param board_id the id of the board of which the name will be changed
     * @param name the new name for the board
     */
    @PutMapping("/put/{board_id}")
    public void renameBoard(
            @PathVariable("board_id") long board_id,
            @RequestParam String name
    ) {
        boardService.renameBoard(board_id, name);
    }

    /**
     * Put request to change the name of a tasklist
     *
     * @param board_id the id of the board from where the list will be renamed
     * @param tasklist_id the id of the list of which the name will be changed
     * @param name the new name for the list
     */
    @PutMapping("/put/{board_id}/{tasklist_id}")
    public void renameTaskList(
            @PathVariable("board_id") long board_id,
            @PathVariable("tasklist_id") long tasklist_id,
            @RequestParam String name
    ) {
        boardService.renameList(board_id, tasklist_id, name);
    }

    /**
     * Put request to change the name of a task
     *
     * @param board_id the id of the board from where the task will be renamed
     * @param tasklist_id the id of the list from where the task will be renamed
     * @param task_id the id of the task of which the name will be changed
     * @param name the new name for the list
     */
    @PutMapping("/put/{board_id}/{tasklist_id}/{task}")
    public void renameTask(
            @PathVariable("board_id") long board_id,
            @PathVariable("tasklist_id") long tasklist_id,
            @PathVariable("task_id") long task_id,
            @RequestParam String name
    ) {
        boardService.renameTask(board_id, tasklist_id, task_id, name);
    }



}
