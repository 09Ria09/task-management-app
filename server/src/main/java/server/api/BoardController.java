package server.api;

import java.util.*;

import commons.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.BoardService;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

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
     * @param boardid the id of the board that will be retrieved
     * @return the board with the corresponding id
     */
    @GetMapping("/{boardid}")
    public Board getBoard(@PathVariable("boardid") final long boardid) {
        return boardService.getBoard(boardid);
    }

    /**
     * Get util method to get all the tasklists from a specific board
     *
     * @param boardid the id of the board that will be accesses
     * @return a list of tasklists from the corresponding board
     */
    @GetMapping("/{boardid}/tasklists")
    public List<TaskList> getTaskLists(@PathVariable("boardid") final long boardid) {
        return boardService.getLists(boardid);
    }

    /**
     * Get util method for a specific list of a specific board
     *
     * @param boardid the id of the board from which we want to access the list
     * @param taskListid the id of the list that will be accessed
     * @return the corresponding list from the corresponding board
     */
    @GetMapping("/{boardid}/tasklist/{taskListid}")
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
    @GetMapping("/{boardid}/tasklist/{taskListid}/tasks")
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
    @GetMapping("/{boardid}/tasklist/{taskListid}/task/{taskid}")
    public Task getTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("taskListid") final long taskListid,
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
    @PostMapping("/{boardid}/tasklist")
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
    @PostMapping("/{boardid}/{tasklistid}/task")
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
     * @return the deleted board if it was deleted successfully,
     * otherwise a not found/error response
     */
    @DeleteMapping("/{boardid}")
    public ResponseEntity<Board> deleteBoard(@PathVariable("boardid") final long boardid) {
        try {
            //the remove method checks if the board exists anyway
            //so we can be certain that the getBoard will also return something non-null
            boardService.removeBoardByID(boardid);
            return ResponseEntity.ok(boardService.getBoard(boardid));
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log the error or perform error-handling
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    /**
     * Delete request to delete a tasklist with a list id and board id
     *
     * @param boardid the id of the board from where the list will be deleted
     * @param tasklistid the id of the list that will be deleted
     */
    @DeleteMapping("/{boardid}/{tasklistid}")
    public ResponseEntity<TaskList> deleteTaskList(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid
    ) {
        try {
            boardService.removeListByID(boardid, tasklistid);
            return ResponseEntity.ok(boardService.getList(boardid, tasklistid));
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Delete request to delete a task with a board, list and task id
     *
     * @param boardid the id of the board from where the task will be deleted
     * @param tasklistid the id of the tasklist from where the list will be deleted
     * @param taskid the id of the task that will be deleted
     */
    @DeleteMapping("/{boardid}/{tasklistid}/{taskid}")
    public ResponseEntity<Task> deleteTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid,
            @PathVariable("taskid") final long taskid
    ) {
        try {
            boardService.removeTaskByID(boardid, tasklistid, taskid);
            return ResponseEntity.ok(boardService.getTask(boardid, tasklistid, taskid));
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Put request to change the name of a board
     *
     * @param boardid the id of the board of which the name will be changed
     * @param name the new name for the board
     */
    @PutMapping("/{boardid}")
    public ResponseEntity<Board> renameBoard(
            @PathVariable("boardid") final long boardid,
            @RequestParam final String name
    ) {
        try{
            //the remove method checks if the board exists anyway
            //so we can be certain that the getBoard will also return something non-null
            boardService.renameBoard(boardid, name);
            return ResponseEntity.ok(boardService.getBoard(boardid));
        } catch (NullPointerException e) {
            //we should update the boardService such that we have consistent
            //handling of exceptions - not found for example differs
            // for removing board and removing tasklist
            return ResponseEntity.badRequest().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            // Log the error and perform error-handling
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Put request to change the name of a tasklist
     *
     * @param boardid    the id of the board from where the list will be renamed
     * @param tasklistid the id of the list of which the name will be changed
     * @param name       the new name for the list
     * @return  the tasklist with the new name if it was renamed successfully,
     * otherwise a not found/error response
     */
    @PutMapping("/{boardid}/{tasklistid}")
    public ResponseEntity<TaskList> renameTaskList(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid,
            @RequestParam final String name
    ) {
        try {
            boardService.renameList(boardid, tasklistid, name);
            return ResponseEntity.ok(boardService.getList(boardid, tasklistid));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Put request to change the name of a task
     *
     * @param boardid the id of the board from where the task will be renamed
     * @param tasklistid the id of the list from where the task will be renamed
     * @param taskid the id of the task of which the name will be changed
     * @param name the new name for the list
     */
    @PutMapping("/{boardid}/{tasklistid}/{taskid}")
    public ResponseEntity<Task> renameTask(
            @PathVariable("boardid") final long boardid,
            @PathVariable("tasklistid") final long tasklistid,
            @PathVariable("taskid") final long taskid,
            @RequestParam final String name
    ) {
        try {
            boardService.renameTask(boardid, tasklistid, taskid, name);
            return ResponseEntity.ok(boardService.getTask(boardid, tasklistid, taskid));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }



}
