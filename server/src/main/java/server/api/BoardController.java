package server.api;

import java.util.*;

import commons.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.BoardService;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private BoardService boardService;

    @Autowired
    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * Get util method to get all boards, retrieved as a list
     *
     * @return a list of all the boards stored in the repository
     */
    @GetMapping(path = { "", "/" })
    public List<Board> getBoards() {
        return boardService.getBoards();
    }

    /**
     * Get util method to get a specific board
     *
     * @param id the id of the board that will be retrieved
     * @return the board with the corresponding id
     */
    @GetMapping("/{boardid}")
    public Board getBoard(@PathVariable("id") final long id) {
        return boardService.getBoard(id);
    }

    /**
     * Get util method to get all the tasklists from a specific board
     *
     * @param id the id of the board that will be accesses
     * @return a list of tasklists from the corresponding board
     */
    @GetMapping("/{boardid}/tasklists")
    public List<TaskList> getTaskLists(@PathVariable("id") final long id) {
        return boardService.getLists(id);
    }

    /**
     * Get util method for a specific list of a specific board
     *
     * @param boardid the id of the board from which we want to access the list
     * @param taskListid the id of the list that will be accessed
     * @return the corresponding list from the corresponding board
     */
    @GetMapping("/get/board/{boardid}/tasklist/{taskListid}")
    public TaskList getTaskList(
            @PathVariable("boardid") final long boardid,
            @PathVariable("taskListid") final long taskListid
    ) {
        return boardService.getList(boardid, taskListid);
    }

    /**
     * Get util method to get all the tasks in a specific list of a specific board
     *
     * @param boardid the id of the board from which we want to access the tasks
     * @param taskListid the id of the list from which we want to access the tasks
     * @return a list of tasks from the corresponding list and board
     */
    @GetMapping("/get/board/{boardid}/tasklist/{taskListid}/tasks")
    public List<Task> getTasks(
            @PathVariable("boardid") final long boardid,
            @PathVariable("taskListid") final long taskListid
    ) {
        return boardService.getTasks(boardid, taskListid);
    }

    /**
     * Get util method to get a specific task from a specific tasklist and board
     *
     * @param boardid the id of the board from which we want to access the task
     * @param taskListid the id of the tasklist from which we want to access the task
     * @param taskid the id of the task we want to access
     * @return the task corresponding to all the given ids
     */
    @GetMapping("/get/board/{boardid}/tasklist/{taskListid}/task/{taskid}")
    public Task getTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("taskListid") final long taskListid,
            @PathVariable("taksid") final long taskid
            @PathVariable("taskid") final long taskid
    ) {
        return boardService.getTask(boardid, taskListid, taskid);
    }

    /**
     * Post util method to add a board
     *
     * @param board the board you want to add
     */
 @PostMapping(path = { "", "/" })
    public void addBoard(@RequestBody final Board board) {
        boardService.addBoard(board);
    }

    /**
     * Post util method to add a tasklist to a specific board
     *
     * @param boardid the id from the board you want to add a tasklist to
     * @param taskList the tasklist that you want to add to board with the given id
     */
    @PostMapping("/post/{boardid}/tasklist")
    public void addTaskList(
            @PathVariable("boardid") final long boardid,
            @RequestBody final TaskList taskList
    ) {
        boardService.addList(boardid, taskList);
    }

    /**
     * Post util method to add a task to a specific board and list
     *
     * @param boardid the id from the board you want to add the task to
     * @param tasklistid the id from the list you want to add the task to
     * @param task the task you want to add to the entities with the given ids
     */
    @PostMapping("/post/{boardid}/{tasklistid}/task")
    public void addTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid,
            @RequestBody final Task task
    ) {
        boardService.addTask(boardid, tasklistid, task);
    }

    /**
     * Delete request to delete a board with a specific id
     *
     * @param boardid the corresponding id of the board that will be deleted
     */
    @DeleteMapping("/delete/{boardid}")
    public void deleteBoard(@PathVariable("boardid") final long boardid) {
        boardService.removeBoardByID(boardid);
    }

    /**
     * Delete request to delete a tasklist with a list id and board id
     *
     * @param boardid the id of the board from where the list will be deleted
     * @param tasklistid the id of the list that will be deleted
     */
    @DeleteMapping("/delete/{boardid}/{tasklistid}")
    public void deleteTaskList(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid
    ) {
        boardService.removeListByID(boardid, tasklistid);
    }

    /**
     * Delete request to delete a task with a board, list and task id
     *
     * @param boardid the id of the board from where the task will be deleted
     * @param tasklistid the id of the tasklist from where the list will be deleted
     * @param taskid the id of the task that will be deleted
     */
    @DeleteMapping("/delete/{boardid}/{tasklistid}/{taskid}")
    public void deleteTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid,
            @PathVariable("taskid") final long taskid
    ) {
        boardService.removeTaskByID(boardid, tasklistid, taskid);
    }

    /**
     * Put request to change the name of a board
     *
     * @param boardid the id of the board of which the name will be changed
     * @param name the new name for the board
     */
    @PutMapping("/put/{boardid}")
    public void renameBoard(
            @PathVariable("boardid") final long boardid,
            @RequestParam final String name
    ) {
        boardService.renameBoard(boardid, name);
    }

    /**
     * Put request to change the name of a tasklist
     *
     * @param boardid the id of the board from where the list will be renamed
     * @param tasklistid the id of the list of which the name will be changed
     * @param name the new name for the list
     */
    @PutMapping("/put/{boardid}/{tasklistid}")
    public void renameTaskList(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid,
            @RequestParam final String name
    ) {
        boardService.renameList(boardid, tasklistid, name);
    }

    /**
     * Put request to change the name of a task
     *
     * @param boardid the id of the board from where the task will be renamed
     * @param tasklistid the id of the list from where the task will be renamed
     * @param taskid the id of the task of which the name will be changed
     * @param name the new name for the list
     */
    @PutMapping("/put/{boardid}/{tasklistid}/{taskid}")
    public void renameTask(
            @PathVariable("board_id") final long boardid,
            @PathVariable("tasklist_id") final long tasklistid,
            @PathVariable("taskid") final long taskid,
            @RequestParam final String name
    ) {
        boardService.renameTask(boardid, tasklistid, taskid, name);
    }



}
